package com.vocs.main.bean;


import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/***
 *  用户积分对象
 */
@Data
@ToString
public class UserIntegralDto {

	private Integer id;

	private Integer userId;

	private Integer score;

	private Date scordeDate;

	private BigDecimal balance;

	private String creator;

	private Date createTime;

	private String updater;

	private Date updateTime;

	private Integer pageNum;

	private Integer pageSize;

	private String scordeDateStr;

}
