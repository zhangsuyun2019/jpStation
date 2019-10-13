package com.vocs.main.service;

import com.vocs.main.bean.EventsDto;
import com.vocs.main.pojo.Events;

import java.util.List;

public interface EventService {

  /**
   * * 查询事件列表
   *
   * @param eventsDto 查询对象
   * @return
   */
  List<Events> searchEventList(EventsDto eventsDto);
}
