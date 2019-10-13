package com.vocs.main.bean;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/** * 用户对象 */
@Data
@ToString
public class UserDto {

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

  private Boolean isAdmin;

  private Integer pageNum;

  private Integer pageSize;
}
