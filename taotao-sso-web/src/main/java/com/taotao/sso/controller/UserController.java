package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.manager.pojo.User;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	// 请求方法 GET
	// URL http://sso.taotao.com/user/check/{param}/{type}
	/**
	 * 实现检查数据是否可用
	 * 
	 * @param param
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
	// @ResponseBody
	public ResponseEntity<String> check(HttpServletRequest request, @PathVariable String param,
			@PathVariable Integer type) {
		try {
			boolean bool = this.userService.check(param, type);
			// 1.获取callback
			String callback = request.getParameter("callback");

			// 判断callback是否为空，其实是在判断是否使用了jsonp
			String result = "";
			if (StringUtils.isNotBlank(callback)) {
				// 如果使用了jsonp就进行包裹
				// 2.使用callback包裹返回数据 fun({abc:123})
				result = callback + "(" + bool + ")";
			} else {
				// 如果没有使用jsonp，不做包裹，原样放回
				result = "" + bool;
			}

			// 查询没问题，返回200
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	// 请求方法 GET
	// URL http://sso.taotao.com/user/{ticket}
	/**
	 * 根据ticket查询用户
	 * 
	 * @param ticket
	 * @return
	 */
	@RequestMapping(value = "{ticket}", method = RequestMethod.GET)
	public ResponseEntity<User> queryUserByTicket(@PathVariable String ticket) {
		try {
			User user = this.userService.queryUserByTicket(ticket);
			// 查询成功返回200
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

	}

}
