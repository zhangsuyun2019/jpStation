package com.vocs.main.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "user")
public class User {
    private Integer id;

    private String loginName;

    private String password;

    private String userName;

    private String company;

    private String position;

    private String phone;

    private String mobilePhone;

    private String email;

    private Integer age;

    private String nativePlace;

    private String workCity;

    private String openid;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;

}