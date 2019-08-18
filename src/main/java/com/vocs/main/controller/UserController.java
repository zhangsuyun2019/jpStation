package com.vocs.main.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vocs.main.bean.UserDto;
import com.vocs.main.pojo.User;
import com.vocs.main.response.BaseResponse;
import com.vocs.main.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * User controller
 */
@Slf4j
@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

	/***
	 * 获取用户列表（分页）
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "getUserListByPage")
    @ResponseBody
    public BaseResponse<Map<String, Object>> getUserListByPage(@RequestBody UserDto user) {
		Map<String, Object> map = new HashMap<>();
    	try {
    		if (null == user) {
    			BaseResponse.fail("900", "参数不能为空");
			}
			PageHelper.startPage(user.getPageNum(), user.getPageSize());
			List<UserDto> list = userService.searchUser(user);
			PageInfo<UserDto> page = new PageInfo<>(list);
			map.put("page", page);
			map.put("total", page.getTotal());
			return BaseResponse.success(map);
		} catch (Exception e) {
			log.error("查询用户列表异常:{}", e);
			return BaseResponse.fail("900","查询用户列表异常,异常为：" + e.getMessage());
		}
    }

	/***
	 * 新增用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "addUser", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse<String> addUser(@RequestBody User user) {
		BaseResponse<String> result = new BaseResponse<>();
		try {
			if (null == user) {
				BaseResponse.fail("900", "参数不能为空");
			}
			int insertCount = userService.addUser(user);
			if (insertCount > 0) {
				result.setCode("000");
				result.setMsg("新增用户成功");
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

	/***
	 * 更新用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse<String> updateUser(@RequestBody User user) {
		BaseResponse<String> result = new BaseResponse<>();
		try {
			if (null == user) {
				BaseResponse.fail("900", "参数不能为空");
			}
			int updateCount = userService.updateUser(user);
			if (updateCount > 0) {
				result.setCode("000");
				result.setMsg("更新用户成功");
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

	/***
	 * 删除用户
	 * @param user 用户对象
	 * @return
	 */
	@RequestMapping(value = "deleteUser", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse<String> deleteUser(@RequestBody User user) {
		BaseResponse<String> result = new BaseResponse<>();
		try {
			if (null == user || null == user.getId()) {
				BaseResponse.fail("900", "id参数不能为空");
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

	/***
	 * 登录校验
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "logValidate", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse<String> logValidate(@RequestBody UserDto user) {
		BaseResponse<String> result = new BaseResponse<>();
		try {
			if (null == user) {
				BaseResponse.fail("900", "参数不能为空");
			}
			if (StringUtils.isEmpty(user.getLoginName())
					|| StringUtils.isEmpty(user.getPassword())) {
				BaseResponse.fail("900", "参数'登录名'不能为空");
			}
			if (StringUtils.isEmpty(user.getPassword())) {
				BaseResponse.fail("900", "参数'密码'不能为空");
			}
			user.setPassword(MD5Encoder.encode(user.getPassword().getBytes()));
			Boolean validateResult = userService.existsByLoginNameAnaPwd(user);
			if (validateResult) {
				result.setCode("000");
				result.setMsg("校验用户成功");
				return result;
			} else {
				return BaseResponse.fail("900", "校验用户失败");
			}
		} catch (Exception e) {
			result.setCode("900");
			result.setMsg("校验用户失败,异常为:" + e.getMessage());
			log.error("校验用户异常:{}", e);
			return result;
		}
	}


}
