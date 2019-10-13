package com.vocs.main.bean;

import lombok.Data;

import java.util.Date;

@Data
public class EventsDto {
    private Integer id;

    private String eventName;

    private String desc;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;
}