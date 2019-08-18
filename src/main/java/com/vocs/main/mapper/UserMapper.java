package com.vocs.main.mapper;

import com.vocs.main.bean.UserDto;
import com.vocs.main.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectByPrimaryKey(Integer id);

    List<UserDto> selectByCondition(UserDto record);

    int insert(User record);

    int insertSelective(User record);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int deleteByPrimaryKey(Integer id);

    int existsByLoginNameAnaPwd(UserDto record);

}