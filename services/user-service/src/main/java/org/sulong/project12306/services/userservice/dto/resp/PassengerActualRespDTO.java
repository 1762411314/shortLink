package org.sulong.project12306.services.userservice.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PassengerActualRespDTO {

    private String id;

    private String username;

    private String realName;

    private Integer idType;

    private String idCard;

    private Integer discountType;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;

    private Integer verifyStatus;
}
