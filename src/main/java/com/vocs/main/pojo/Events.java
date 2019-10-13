package com.vocs.main.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "events")
public class Events {
    private Integer id;

    private String eventName;

    private String desc;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;

}