package com.vocs.main.service.impl;

import com.vocs.main.bean.EventsDto;
import com.vocs.main.mapper.EventsMapper;
import com.vocs.main.pojo.Events;
import com.vocs.main.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

  @Resource
  private EventsMapper eventsMapper;


  @Override
  public List<Events> searchEventList(EventsDto eventsDto) {
    return eventsMapper.selectByCondition(eventsDto);
  }
}
