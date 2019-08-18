package com.vocs.main.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/***
 * 用户积分实体类
 */
@Data
@Table(name = "user_integral")
public class UserIntegral {
    private Integer id;

    private Integer userId;

    private Integer score;

    private Date scordeDate;

    private BigDecimal balance;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;

}