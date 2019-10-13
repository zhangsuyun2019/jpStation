package com.vocs.main.mapper;

import com.vocs.main.bean.EventsDto;
import com.vocs.main.pojo.Events;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Events record);

    int insertSelective(Events record);

    Events selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Events record);

    int updateByPrimaryKey(Events record);

    /**
     * * 获取事件列表
     *
     * @param eventsDto:查询对象
     * @return
     */
    List<Events> selectByCondition(EventsDto eventsDto);
}