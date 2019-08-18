package com.vocs.main.mapper;

import com.vocs.main.bean.UserIntegralDto;
import com.vocs.main.pojo.UserIntegral;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserIntegralMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserIntegral record);

    int insertSelective(UserIntegral record);

    UserIntegralDto selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserIntegral record);

    int updateByPrimaryKey(UserIntegral record);

    /***
     * 获取用户积分信息列表
     * @param userIntegralDto:用户积分对象
     * @return
     */
    List<UserIntegralDto> selectByCondition(UserIntegralDto userIntegralDto);

}