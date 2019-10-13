package com.vocs.main.mapper;

import com.vocs.main.pojo.Events;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Events record);

    int insertSelective(Events record);

    Events selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Events record);

    int updateByPrimaryKey(Events record);
}