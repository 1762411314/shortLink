package org.sulong.project12306.services.userservice.service.Impl;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sulong.project12306.framework.convention.exception.ClientException;
import org.sulong.project12306.services.userservice.dao.entity.UserDO;
import org.sulong.project12306.services.userservice.dao.entity.UserMailDO;
import org.sulong.project12306.services.userservice.dao.mapper.UserDeletionMapper;
import org.sulong.project12306.services.userservice.dao.mapper.UserMailMapper;
import org.sulong.project12306.services.userservice.dao.mapper.UserMapper;
import org.sulong.project12306.services.userservice.dto.req.UserUpdateReqDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserQueryActualRespDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserQueryRespDTO;
import org.sulong.project12306.services.userservice.service.UserService;
import org.sulong.project12306.framework.common.toolkit.BeanUtil;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserDeletionMapper userDeletionMapper;
    private final UserMailMapper userMailMapper;
    @Override
    public UserQueryRespDTO queryUserByUserId(@NonNull String userId) {
        UserDO userDO=userMapper.queryUserByUserId(userId);
        if (userDO==null)
            throw new ClientException("用户不存在，请检查用户ID是否正确");
        return BeanUtil.convert(userDO, UserQueryRespDTO.class);
    }

    @Override
    public UserQueryRespDTO queryUserByUsername(@NonNull String username) {
        UserDO userDO=userMapper.queryUserByUsername(username);
        if (userDO==null)
            throw new ClientException("用户不存在，请检查用户名是否正确");
        return BeanUtil.convert(userDO, UserQueryRespDTO.class);
    }

    @Override
    public UserQueryActualRespDTO queryActualUserByUsername(@NonNull String username) {
        return BeanUtil.convert(queryUserByUsername(username), UserQueryActualRespDTO.class);
    }

    @Override
    public Integer queryUserDeletionNum(Integer idType, String idCard) {
        Long deletionCount=userMapper.queryUserDeletionNum(idType,idCard);
        return Optional.ofNullable(deletionCount).map(Long::intValue).orElse(0);
    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        UserQueryRespDTO userQueryRespDTO=queryUserByUsername(requestParam.getUsername());
        UserDO userDO=BeanUtil.convert(userQueryRespDTO,UserDO.class);
        userMapper.update(userDO);
        if (StrUtil.isNotBlank(requestParam.getAddress())
                &&!Objects.equals(requestParam.getMail(),userQueryRespDTO.getMail())){
            userMailMapper.delete(userQueryRespDTO.getMail());
            UserMailDO userMailDO=UserMailDO.builder()
                    .mail(requestParam.getMail())
                    .username(requestParam.getUsername())
                    .build();
            userMailMapper.insert(userMailDO);
        }
    }
}
