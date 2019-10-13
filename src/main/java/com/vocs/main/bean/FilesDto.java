package com.vocs.main.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FilesDto {
    private Integer id;

    private String fileName;

    private String fileType;

    private BigDecimal amount;

    private Date uploadDate;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;
}