package org.sulong.project12306.services.userservice.service;

import lombok.NonNull;
import org.sulong.project12306.services.userservice.dto.req.UserUpdateReqDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserQueryActualRespDTO;
import org.sulong.project12306.services.userservice.dto.resp.UserQueryRespDTO;

public interface UserService {
    /**
      根据用户 ID 查询用户信息

      @param userId 用户 ID
     * @return 用户详细信息
     */
    UserQueryRespDTO queryUserByUserId(@NonNull String userId);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户详细信息
     */

    UserQueryRespDTO queryUserByUsername(@NonNull String username);

    /**
     * 根据用户名查询用户无脱敏信息
     *
     * @param username 用户名
     * @return 用户详细信息
     */

    UserQueryActualRespDTO queryActualUserByUsername(@NonNull String username);

    /**
     * 根据证件类型和证件号查询注销次数
     *
     * @param idType 证件类型
     * @param idCard 证件号
     * @return 注销次数
     */
    Integer queryUserDeletionNum(Integer idType, String idCard);

    /**
     * 根据用户 ID 修改用户信息
     *
     * @param requestParam 用户信息入参
     */
    void update(UserUpdateReqDTO requestParam);
}
