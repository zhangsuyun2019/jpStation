package com.vocs.main.service;


import com.vocs.main.bean.UserDto;
import com.vocs.main.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    int addUser(User user);

    int updateUser(User user);

    int deleteUser(Integer id);

    List<UserDto> searchUser(UserDto record);

    boolean existsByLoginNameAnaPwd(UserDto record);

}
