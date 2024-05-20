//package org.sulong.project12306.services.userservice.service.Impl;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson2.JSON;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.TransactionStatus;
//import org.springframework.transaction.support.DefaultTransactionDefinition;
//import org.sulong.project12306.framework.cache.DistributedCache;
//import org.sulong.project12306.framework.common.toolkit.BeanUtil;
//import org.sulong.project12306.framework.convention.exception.ClientException;
//import org.sulong.project12306.framework.convention.exception.ServiceException;
//import org.sulong.project12306.framework.user.core.UserContext;
//import org.sulong.project12306.services.userservice.common.constant.RedisKeyConstant;
//import org.sulong.project12306.services.userservice.common.enums.VerifyStatusEnum;
//import org.sulong.project12306.services.userservice.dao.entity.PassengerDO;
//import org.sulong.project12306.services.userservice.dao.mapper.PassengerMapper;
//import org.sulong.project12306.services.userservice.dto.req.PassengerRemoveReqDTO;
//import org.sulong.project12306.services.userservice.dto.req.PassengerReqDTO;
//import org.sulong.project12306.services.userservice.dto.resp.PassengerActualRespDTO;
//import org.sulong.project12306.services.userservice.dto.resp.PassengerRespDTO;
//import org.sulong.project12306.services.userservice.service.PassengerService;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PassengerServiceImpl implements PassengerService {
//
//    private final DistributedCache distributedCache;
//    private final PassengerMapper passengerMapper;
//    private final PlatformTransactionManager platformTransactionManager;
//
//    @Override
//    public List<PassengerRespDTO> listPassengerQueryByUsername(String username) {
//        String actualPassengerListStr=getActualUserPassengerListStr(username);
//        Optional.ofNullable(actualPassengerListStr)
//                .map(each->JSON.parseArray(each, PassengerDO.class))
//                .map(each-> BeanUtil.convert(each,PassengerRespDTO.class));
//        return null;
//    }
//
//    private String getActualUserPassengerListStr(String username){
//        return distributedCache.safeGet(
//                RedisKeyConstant.USER_PASSENGER_LIST+username,
//                String.class,
//                ()->{
//                    List<PassengerDO> passengerDOList=passengerMapper.listPassengerQueryByUsername(username);
//                    return CollUtil.isNotEmpty(passengerDOList)? JSON.toJSONString(passengerDOList):null;
//                },
//                1,
//                TimeUnit.DAYS
//        );
//    }
//
//    @Override
//    public List<PassengerActualRespDTO> listPassengerQueryByIds(String username, List<Long> ids) {
//        String actualUserPassengerListStr = getActualUserPassengerListStr(username);
//        if (StrUtil.isEmpty(actualUserPassengerListStr)) {
//            return null;
//        }
//        return JSON.parseArray(actualUserPassengerListStr, PassengerDO.class)
//                .stream().filter(passengerDO -> ids.contains(passengerDO.getId()))
//                .map(each -> BeanUtil.convert(each, PassengerActualRespDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void savePassenger(PassengerReqDTO requestParam) {
//        TransactionDefinition transactionDefinition=new DefaultTransactionDefinition();
//        TransactionStatus transactionStatus= platformTransactionManager.getTransaction(transactionDefinition);
//        String username= UserContext.getUserName();
//        try{
//            PassengerDO passengerDO=new PassengerDO();
//            passengerDO.setUsername(username);
//            passengerDO.setCreateDate(new Date());
//            passengerDO.setVerifyStatus(VerifyStatusEnum.REVIEWED.getCode());
//            int inserted=passengerMapper.insertPassenger(passengerDO);
//            if (inserted<1){
//                throw new ServiceException(String.format("[%s] 新增乘车人失败", username));
//            }
//            platformTransactionManager.commit(transactionStatus);
//        }catch (Exception ex){
//            if (ex instanceof ServiceException){
//                log.error("{}，请求参数：{}", ex.getMessage(), JSON.toJSONString(requestParam));
//            } else {
//                log.error("[{}] 新增乘车人失败，请求参数：{}", username, JSON.toJSONString(requestParam), ex);
//            }
//            platformTransactionManager.rollback(transactionStatus);
//            throw ex;
//        }
//        delUserPassengerCache(username);
//    }
//
//    @Override
//    public void updatePassenger(PassengerReqDTO requestParam) {
//        TransactionDefinition transactionDefinition=new DefaultTransactionDefinition();
//        TransactionStatus transactionStatus=platformTransactionManager.getTransaction(transactionDefinition);
//        String username=UserContext.getUserName();
//        try {
//            PassengerDO passengerDO=BeanUtil.convert(requestParam, PassengerDO.class);
//            passengerDO.setUsername(username);
//            int inserted=passengerMapper.updatePassenger(passengerDO);
//            if (inserted<1){
//                throw new ServiceException(String.format("[%s] 新增乘车人失败", username));
//            }
//            platformTransactionManager.commit(transactionStatus);
//        }catch (Exception ex){
//            if (ex instanceof ServiceException) {
//                log.error("{}，请求参数：{}", ex.getMessage(), JSON.toJSONString(requestParam));
//            } else {
//                log.error("[{}] 修改乘车人失败，请求参数：{}", username, JSON.toJSONString(requestParam), ex);
//            }
//            platformTransactionManager.rollback(transactionStatus);
//            throw ex;
//        }
//        delUserPassengerCache(username);
//    }
//
//    @Override
//    public void removePassenger(PassengerRemoveReqDTO requestParam) {
//        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
//        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
//        String username = UserContext.getUserName();
//        PassengerDO passengerDO = passengerMapper.selectPassenger(username, requestParam.getId());
//        if (Objects.isNull(passengerDO)) {
//            throw new ClientException("乘车人数据不存在");
//        }
//        try {
//            // 逻辑删除，修改数据库表记录 del_flag
//            int deleted = passengerMapper.delete(passengerDO);
//            if (deleted<1) {
//                throw new ServiceException(String.format("[%s] 删除乘车人失败", username));
//            }
//            platformTransactionManager.commit(transactionStatus);
//        } catch (Exception ex) {
//            if (ex instanceof ServiceException) {
//                log.error("{}，请求参数：{}", ex.getMessage(), JSON.toJSONString(requestParam));
//            } else {
//                log.error("[{}] 删除乘车人失败，请求参数：{}", username, JSON.toJSONString(requestParam), ex);
//            }
//            platformTransactionManager.rollback(transactionStatus);
//            throw ex;
//        }
//        delUserPassengerCache(username);
//    }
//
//    private void delUserPassengerCache(String username){
//        distributedCache.delete(RedisKeyConstant.USER_PASSENGER_LIST+username);
//    }
//}
