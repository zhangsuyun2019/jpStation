package com.vocs.main.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "files")
public class Files {
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