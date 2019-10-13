package com.vocs.main.controller;

import com.vocs.main.pojo.Events;
import com.vocs.main.response.BaseResponse;
import com.vocs.main.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/***
 * 事件controller
 * @author
 * @date 2019-10-13 13:00
 */
@Slf4j
@RequestMapping("event")
@Controller
@CrossOrigin
public class EventController {

  @Autowired
  private EventService eventService;

  @GetMapping("/selectLatestEvent")
  @ResponseBody
  public BaseResponse<Events> selectLatestEvent() {
    List<Events> list = eventService.searchEventList(null);
    if (CollectionUtils.isEmpty(list)) {
      return BaseResponse.fail("900", "没有数据");
    } else {
      return BaseResponse.success(list.get(0));
    }
  }
}
