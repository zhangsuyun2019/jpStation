package com.vocs.main.service.impl;


import com.vocs.main.bean.UserDto;
import com.vocs.main.mapper.UserMapper;
import com.vocs.main.pojo.User;
import com.vocs.main.service.UserService;
import com.vocs.main.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
    private UserMapper userMapper;


	@Override
	public int addUser(User user) {
		try {
			String password = MD5Util.encodeByMd5(user.getPassword());
			user.setPassword(password);
			return userMapper.insertSelective(user);
		} catch (NoSuchAlgorithmException e) {
			log.warn("addUser NoSuchAlgorithmException");
		} catch (UnsupportedEncodingException e) {
			log.warn("addUser UnsupportedEncodingException");
		}
		return 0;
	}

	@Override
	public int updateUser(User user) {
		try {
			String password = MD5Util.encodeByMd5(user.getPassword());
			UserDto userDto = new UserDto();
			userDto.setLoginName(user.getLoginName());
			userDto.setPassword(password);
			List<UserDto> userList = userMapper.selectByCondition(userDto);
			if (null != userList && !userList.isEmpty()) {
				user.setId(userList.get(0).getId());
				user.setUpdateTime(new Date());
				return userMapper.updateByPrimaryKeySelective(user);
			} else {
				return 0;
			}
		} catch (NoSuchAlgorithmException e) {
			log.warn("updateUser NoSuchAlgorithmException");
		} catch (UnsupportedEncodingException e) {
			log.warn("updateUser UnsupportedEncodingException");
		}
		return 0;
	}

	@Override
	public int deleteUser(Integer id) {
		return userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<UserDto> searchUser(UserDto record) {
		return userMapper.selectByCondition(record);
	}

	@Override
	public boolean existsByLoginNameAnaPwd(UserDto record) {
		return userMapper.existsByLoginNameAnaPwd(record) > 0 ? true : false;
	}
}
