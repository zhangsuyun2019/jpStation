package com.vocs.main.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vocs.main.bean.UserDto;
import com.vocs.main.bean.UserIntegralDto;
import com.vocs.main.pojo.User;
import com.vocs.main.pojo.UserIntegral;
import com.vocs.main.response.BaseResponse;
import com.vocs.main.service.UserService;
import com.vocs.main.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/** * User controller */
@Slf4j
@RequestMapping("user")
@Controller
@CrossOrigin
public class UserController {

  @Autowired private UserService userService;

  @Autowired private StringRedisTemplate stringRedisTemplate;

  private final String REDIS_USER = "user:";

  /**
   * * 获取用户列表（分页）
   *
   * @param user
   * @return
   */
  @RequestMapping(value = "getUserListByPage")
  @ResponseBody
  public BaseResponse<Map<String, Object>> getUserListByPage(@RequestBody UserDto user) {
    Map<String, Object> map = new HashMap<>();
    try {
      if (null == user) {
        return BaseResponse.fail("900", "参数不能为空");
      }
      PageHelper.startPage(user.getPageNum(), user.getPageSize());
      List<UserDto> list = userService.searchUser(user);
      PageInfo<UserDto> page = new PageInfo<>(list);
      map.put("page", page);
      map.put("total", page.getTotal());
      return BaseResponse.success(map);
    } catch (Exception e) {
      log.error("查询用户列表异常:{}", e);
      return BaseResponse.fail("900", "查询用户列表异常,异常为：" + e.getMessage());
    }
  }

  /**
   * * 新增用户
   *
   * @param user
   * @return
   */
  @RequestMapping(value = "addUser", method = RequestMethod.POST)
  @ResponseBody
  public BaseResponse<String> addUser(@RequestBody User user) {
    BaseResponse<String> result = new BaseResponse<>();
    try {
      if (null == user) {
        return BaseResponse.fail("900", "参数不能为空");
      }
      if (StringUtils.isEmpty(user.getLoginName())) {
        return BaseResponse.fail("900", "参数'账号'不能为空");
      }
      if (StringUtils.isEmpty(user.getPassword())) {
        return BaseResponse.fail("900", "参数'密码'不能为空");
      }
      UserDto record = new UserDto();
      record.setLoginName(user.getLoginName());
      List<UserDto> list = userService.searchUser(record);
      if (null != list && !list.isEmpty()) {
        return BaseResponse.fail("900", "账号已存在");
      }
      int insertCount = userService.addUser(user);
      if (insertCount > 0) {
        list = userService.searchUser(record);
        result.setCode("000");
        result.setMsg("新增用户成功");
        result.setData(JSON.toJSONString(list.get(0)));
        return result;
      } else {
        return BaseResponse.fail("900", "新增用户失败");
      }
    } catch (Exception e) {
      result.setCode("900");
      result.setMsg("新增用户失败,异常为:" + e.getMessage());
      log.error("新增用户异常:{}", e);
      return result;
    }
  }

  /**
   * * 更新用户
   *
   * @param user
   * @return
   */
  @RequestMapping(value = "updateUser", method = RequestMethod.POST)
  @ResponseBody
  public BaseResponse<String> updateUser(@RequestBody User user) {
    BaseResponse<String> result = new BaseResponse<>();
    try {
      if (null == user) {
        return BaseResponse.fail("900", "参数不能为空");
      }
      if (StringUtils.isEmpty(user.getLoginName())) {
        return BaseResponse.fail("900", "参数'账号'不能为空");
      }
      int updateCount = userService.updateUser(user);
      if (updateCount > 0) {
        UserDto record = new UserDto();
        record.setLoginName(user.getLoginName());
        record.setId(user.getId());
        List<UserDto> list = userService.searchUser(record);
        result.setCode("000");
        result.setMsg("更新用户成功");
        result.setData(JSON.toJSONString(list.get(0)));
        return result;
      } else {
        return BaseResponse.fail("900", "更新用户失败");
      }
    } catch (Exception e) {
      result.setCode("900");
      result.setMsg("更新用户失败,异常为:" + e.getMessage());
      log.error("更新用户异常:{}", e);
      return result;
    }
  }

  /**
   * * 删除用户
   *
   * @param user 用户对象
   * @return
   */
  @RequestMapping(value = "deleteUser", method = RequestMethod.POST)
  @ResponseBody
  public BaseResponse<String> deleteUser(@RequestBody User user) {
    BaseResponse<String> result = new BaseResponse<>();
    try {
      if (null == user || null == user.getId()) {
        return BaseResponse.fail("900", "id参数不能为空");
      }
      int deleteCount = userService.deleteUser(user.getId());
      if (deleteCount > 0) {
        result.setCode("000");
        result.setMsg("删除用户成功");
        return result;
      } else {
        return BaseResponse.fail("900", "删除用户失败");
      }
    } catch (Exception e) {
      result.setCode("900");
      result.setMsg("删除用户失败,异常为:" + e.getMessage());
      log.error("删除用户异常:{}", e);
      return result;
    }
  }

  /**
   * * 登录校验
   *
   * @param user
   * @return
   */
  @RequestMapping(value = "logValidate", method = RequestMethod.POST)
  @ResponseBody
  public BaseResponse<String> logValidate(@RequestBody UserDto user) {
    BaseResponse<String> result = new BaseResponse<>();
    StringBuffer buf = new StringBuffer();
    try {
      if (null == user) {
        return BaseResponse.fail("900", "参数不能为空");
      }
      if (StringUtils.isEmpty(user.getLoginName()) || StringUtils.isEmpty(user.getPassword())) {
        return BaseResponse.fail("900", "参数'登录名'不能为空");
      }
      if (StringUtils.isEmpty(user.getPassword())) {
        return BaseResponse.fail("900", "参数'密码'不能为空");
      }
      String loginUserName = user.getLoginName();
      String password = MD5Util.encodeByMd5(user.getPassword());
      user.setPassword(password);
      Boolean validateResult = userService.existsByLoginNameAnaPwd(user);
      if (validateResult) {
        if (!stringRedisTemplate.hasKey(REDIS_USER + loginUserName)) {
          // 存入redis数据库,1天过期
          stringRedisTemplate
              .opsForValue()
              .set(REDIS_USER + loginUserName, loginUserName, 1L, TimeUnit.DAYS);
        }
        UserDto record = new UserDto();
        record.setLoginName(loginUserName);
        List<UserDto> userList = userService.searchUser(record);
        if (null != userList && !userList.isEmpty()) {
          UserDto userDto = userList.get(0);
          UserIntegralDto userIntegralDto = new UserIntegralDto();
          userIntegralDto.setUserId(userDto.getId());
          List<UserIntegralDto> list = userService.searchIntegral(userIntegralDto);
          if (null != list && !list.isEmpty()) {
            // 获取当前日期
            LocalDate now = LocalDate.now();
            // 获取前一天日期
            LocalDate preDay = LocalDate.now().minusDays(1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 获取前一天的用户积分
            List<UserIntegralDto> preList = null;
            if (list.stream()
                .anyMatch(
                    q ->
                        sdf.format(q.getScordeDate())
                            .equals(sdf.format(convertLocalDateToDate(preDay))))) {
              preList =
                  list.stream()
                      .filter(
                          q ->
                              sdf.format(q.getScordeDate())
                                  .equals(sdf.format(convertLocalDateToDate(preDay))))
                      .collect(Collectors.toList());
            }
            // 获取当天用户积分
            List<UserIntegralDto> nowList = null;
            if (list.stream()
                .anyMatch(
                    q ->
                        sdf.format(q.getScordeDate())
                            .equals(sdf.format(convertLocalDateToDate(now))))) {
              nowList =
                  list.stream()
                      .filter(
                          q ->
                              sdf.format(q.getScordeDate())
                                  .equals(sdf.format(convertLocalDateToDate(now))))
                      .collect(Collectors.toList());
            }
            // 如果当天积分不存在，那么就增加
            if (null == nowList || nowList.isEmpty()) {
              UserIntegral userIntegral = new UserIntegral();
              userIntegral.setUserId(userDto.getId());
              userIntegral.setScordeDate(new Date());

              if (null != preList && !preList.isEmpty()) {
                userIntegral.setUpdater(loginUserName);
                userIntegral.setId(preList.get(0).getId());
                userIntegral.setScore(preList.get(0).getScore() + 1);
                userIntegral.setBalance(preList.get(0).getBalance());
                if (userService.updateIntegral(userIntegral) != null) {
                  buf.append("更新积分成功");
                } else {
                  buf.append("更新积分失败");
                }
              } else {
                userIntegral.setCreator(loginUserName);
                userIntegral.setBalance(new BigDecimal("0.00"));
                userIntegral.setScore(1);
                if (userService.addIntegral(userIntegral) != null) {
                  buf.append("新增积分成功");
                } else {
                  buf.append("新增积分失败");
                }
              }
            }
          } else {
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setUserId(userDto.getId());
            userIntegral.setScordeDate(new Date());
            userIntegral.setCreator(loginUserName);
            userIntegral.setScore(1);
            userIntegral.setBalance(new BigDecimal("0.00"));
            if (userService.addIntegral(userIntegral) != null) {
              buf.append("新增积分成功");
            } else {
              buf.append("新增积分失败");
            }
          }
        } else {
          buf.append("账号'" + loginUserName + "'不存在");
        }
        result.setCode("000");
        result.setMsg("校验用户成功;" + buf.toString());
        return result;
      } else {
        if (stringRedisTemplate.hasKey(REDIS_USER + loginUserName)) {
          // 删除redis数据库
          stringRedisTemplate.delete(REDIS_USER + loginUserName);
        }
        return BaseResponse.fail("900", "用户名或密码错误");
      }
    } catch (Exception e) {
      result.setCode("900");
      result.setMsg("校验用户失败,异常为:" + e.getMessage());
      log.error("校验用户异常:{}", e);
      return result;
    }
  }

  /**
   * * 获取用户积分
   *
   * @param user
   * @return
   */
  @RequestMapping(value = "getScore", method = RequestMethod.POST)
  @ResponseBody
  public BaseResponse<String> getScore(@RequestBody User user) {
    BaseResponse<String> result = new BaseResponse<>();
    try {
      if (null == user) {
        return BaseResponse.fail("900", "参数不能为空");
      }
      if (StringUtils.isEmpty(user.getLoginName())) {
        return BaseResponse.fail("900", "参数'账号'不能为空");
      }

      if (!stringRedisTemplate.hasKey(REDIS_USER + user.getLoginName())) {
        return BaseResponse.fail("900", "用户未登录，请先登录");
      }

      UserDto record = new UserDto();
      record.setLoginName(user.getLoginName());
      List<UserDto> list = userService.searchUser(record);
      if (null != list && !list.isEmpty()) {
        UserIntegralDto userIntegralDto = new UserIntegralDto();
        userIntegralDto.setUserId(list.get(0).getId());
        List<UserIntegralDto> userIntegralDtoList = userService.searchIntegral(userIntegralDto);
        if (null != userIntegralDtoList && !userIntegralDtoList.isEmpty()) {
          result.setCode("000");
          result.setMsg("获取用户积分成功");
          result.setData(JSON.toJSONString(userIntegralDtoList.get(0)));
        } else {
          return BaseResponse.fail("900", "用户积分不存在");
        }
      } else {
        return BaseResponse.fail("900", "用户不存在");
      }
      return result;
    } catch (Exception e) {
      result.setCode("900");
      result.setMsg("获取用户积分失败,异常为:" + e.getMessage());
      log.error("获取用户积分异常:{}", e);
      return result;
    }
  }

  /**
   * * 转换data为localDate
   *
   * @param date
   * @return
   */
  public static LocalDate convertDateToLocalDate(Date date) {
    if (null == date) {
      return null;
    }
    Instant instant = date.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    return instant.atZone(zoneId).toLocalDate();
  }

  /**
   * * 转换localDate为date
   *
   * @param localDate
   * @return
   */
  public static Date convertLocalDateToDate(LocalDate localDate) {
    ZoneId zone = ZoneId.systemDefault();
    Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
    Date date = Date.from(instant);
    return date;
  }
}
