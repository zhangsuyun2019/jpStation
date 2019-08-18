package com.vocs.main.service;


import com.vocs.main.bean.UserDto;
import com.vocs.main.bean.UserIntegralDto;
import com.vocs.main.pojo.User;
import com.vocs.main.pojo.UserIntegral;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    int addUser(User user);

    int updateUser(User user);

    int deleteUser(Integer id);

    List<UserDto> searchUser(UserDto record);

    boolean existsByLoginNameAnaPwd(UserDto record);

    /***
     * 增加用户积分
     * @param userIntegral
     * @return
     */
    UserIntegralDto addIntegral(UserIntegral userIntegral);

    /***
     * 更新用户积分
     * @param userIntegral
     * @return
     */
    UserIntegralDto updateIntegral(UserIntegral userIntegral);

    /***
     * 查询用户积分列表
     * @param userIntegralDto 查询对象
     * @return
     */
    List<UserIntegralDto> searchIntegral(UserIntegralDto userIntegralDto);

}
