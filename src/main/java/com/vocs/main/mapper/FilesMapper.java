package com.vocs.main.mapper;

import com.vocs.main.pojo.Files;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FilesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Files record);

    int insertSelective(Files record);

    Files selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Files record);

    int updateByPrimaryKey(Files record);
}