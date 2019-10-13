package com.vocs.main.mapper;

import com.vocs.main.bean.FilesDto;
import com.vocs.main.pojo.Files;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FilesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Files record);

    int insertSelective(Files record);

    Files selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Files record);

    int updateByPrimaryKey(Files record);

    /**
     * * 查询文件个数
     *
     * @param filesDto 查询对象
     * @return
     */
    int selectCountByCondition(FilesDto filesDto);

    /**
     * * 获取文件列表
     *
     * @param filesDto:查询对象
     * @return
     */
    List<Files> selectByCondition(FilesDto filesDto);
}