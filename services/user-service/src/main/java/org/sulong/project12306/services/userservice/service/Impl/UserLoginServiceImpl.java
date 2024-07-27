package org.sulong.project12306.services.userservice.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sulong.project12306.framework.cache.DistributedCache;
import org.sulong.project12306.framework.common.toolkit.BeanUtil;
import org.sulong.project12306.framework.convention.exception.ClientException;
import org.sulong.project12306.framework.convention.exception.ServiceException;
import org.sulong.project12306.framework.designpattern.chain.AbstractChainContext;
import org.sulong.project12306.framework.user.core.UserContext;
import org.sulong.project12306.framework.user.core.UserInfoDTO;
import org.sulong.project12306.framework.user.toolkit.JWTUtil;
import org.sulong.project12306.services.userservice.common.constant.RedisKeyConstant;
import org.sulong.project12306.services.userservice.common.enums.UserChainMarkEnum;
import org.sulong.project12306.services.userservice.dao.entity.*;
import org.sulong.project12306.services.userservice.dao.mapper.*;
import org.sulong.project12306.services.userservice.dto.req.UserDeletionReqDTO;
import org.sulong.project12306.services.userservice.dto.req.UserLoginReqDTO;
import org.sulong.project12306.services.userservice.dto.req.UserRegisterReqDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserLoginRespDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserQueryRespDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserRegisterRespDTO;
import org.sulong.project12306.services.userservice.service.UserLoginService;
import org.sulong.project12306.services.userservice.service.UserService;
import org.sulong.project12306.services.userservice.toolkit.UserReuseUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.sulong.project12306.services.userservice.common.constant.RedisKeyConstant.USER_DELETION;
import static org.sulong.project12306.services.userservice.common.constant.RedisKeyConstant.USER_REGISTER_REUSE_SHARDING;
import static org.sulong.project12306.services.userservice.common.enums.UserRegisterErrorCodeEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserReuseMapper userReuseMapper;
    private final UserDeletionMapper userDeletionMapper;
    private final DistributedCache distributedCache;
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final AbstractChainContext<UserRegisterReqDTO> abstractChainContext;
    private final RedissonClient redissonClient;

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        String usernameOrMailOrPhone = requestParam.getUsernameOrMailOrPhone();
        boolean mailFlag = false;
        for (char c : usernameOrMailOrPhone.toCharArray()) {
            if (c == '@') {
                mailFlag = true;
                break;
            }
        }
        String username = requestParam.getUsernameOrMailOrPhone();
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username)
                .eq(UserDO::getPassword, requestParam.getPassword())
                .select(UserDO::getId, UserDO::getUsername, UserDO::getRealName);
        UserDO userDO = userMapper.selectOne(queryWrapper);
        if (userDO != null) {
            UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                    .userId(String.valueOf(userDO.getId()))
                    .username(userDO.getUsername())
                    .realName(userDO.getRealName())
                    .build();
            String accessToken = JWTUtil.generateAccessToken(userInfoDTO);
            UserLoginRespDTO actual = new UserLoginRespDTO(userInfoDTO.getUserId(), requestParam.getUsernameOrMailOrPhone(), userDO.getRealName(), accessToken);
            distributedCache.put(accessToken, JSON.toJSONString(actual), 30, TimeUnit.MINUTES);
            return actual;
        }
        throw new ServiceException("账号不存在或密码错误");
    }

    @Override
    public UserLoginRespDTO checkLogin(String accessToken) {
        return distributedCache.get(accessToken, UserLoginRespDTO.class);
    }

    @Override
    public void logout(String accessToken) {
        if (StrUtil.isNotBlank(accessToken)) {
            distributedCache.delete(accessToken);
        }
    }

    @Override
    public Boolean hasUsername(String username) {
        if (userRegisterCachePenetrationBloomFilter.contains(username)) {
            StringRedisTemplate redisTemplate = (StringRedisTemplate) distributedCache.getInstance();
            return redisTemplate.opsForSet().isMember(USER_REGISTER_REUSE_SHARDING
                    + UserReuseUtil.hashShardingIdx(username), username);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        abstractChainContext.handler(UserChainMarkEnum.USER_REGISTER_FILTER.name(), requestParam);
        RLock lock = redissonClient.getLock(RedisKeyConstant.LOCK_USER_REGISTER + requestParam.getUsername());
        if (!lock.tryLock()) {
            throw new ServiceException(HAS_USERNAME_NOTNULL);
        }
        try {
            try {
                int inserted = userMapper.insert(BeanUtil.convert(requestParam, UserDO.class));
                if (inserted < 1) {
                    throw new ServiceException(USER_REGISTER_FAIL);
                }
            } catch (DuplicateKeyException ex) {
                log.error("用户名 [{}] 重复注册", requestParam.getUsername());
                throw new ServiceException(HAS_USERNAME_NOTNULL);
            }
            String username = requestParam.getUsername();
            userReuseMapper.delete(username);
            StringRedisTemplate redisTemplate = (StringRedisTemplate) distributedCache.getInstance();
            redisTemplate.opsForSet().remove(USER_REGISTER_REUSE_SHARDING + UserReuseUtil.hashShardingIdx(username), username);
            userRegisterCachePenetrationBloomFilter.add(username);
        } finally {
            lock.unlock();
        }
        return BeanUtil.convert(requestParam, UserRegisterRespDTO.class);
    }

    @Override
    public void deletion(UserDeletionReqDTO requestParam) {
        String username = UserContext.getUserName();
        if (!Objects.equals(username, requestParam.getUserName())) {
            throw new ClientException("注销账号与登录账号不一致");
        }
        RLock lock = redissonClient.getLock(USER_DELETION + requestParam.getUserName());
        if (lock.tryLock()) {
            try {
                UserQueryRespDTO userQueryRespDTO = userService.queryUserByUsername(username);
                UserDeletionDO userDeletionDO = UserDeletionDO.builder()
                        .idType(userQueryRespDTO.getIdType())
                        .idCard(userQueryRespDTO.getIdCard())
                        .build();
                userDeletionMapper.insert(userDeletionDO);
                UserDO userDO = new UserDO();
                userDO.setUsername(username);
                userDO.setDeletionTime(System.currentTimeMillis());
                userMapper.deletionUser(userDO);

                distributedCache.delete(UserContext.getToken());
                userReuseMapper.insert(new UserReuseDO(username));
                StringRedisTemplate redisTemplate = (StringRedisTemplate) distributedCache.getInstance();
                redisTemplate.opsForSet().add(USER_REGISTER_REUSE_SHARDING + UserReuseUtil.hashShardingIdx(username), username);
            } finally {
                lock.unlock();
            }
        }
    }
}
