package org.sulong.project12306.services.userservice.dao.mapper;

import org.sulong.project12306.services.userservice.dao.entity.PassengerDO;

import java.util.List;

public interface PassengerMapper {
    int insertPassenger(PassengerDO passengerDO) ;

    List<PassengerDO> listPassengerQueryByUsername(String username);

    int updatePassenger(PassengerDO passengerDO);

    PassengerDO selectPassenger(String username, String id);

    int delete(PassengerDO passengerDO);
}
