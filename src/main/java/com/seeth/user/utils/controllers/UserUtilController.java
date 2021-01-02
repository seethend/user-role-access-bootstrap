/**
 * 
 */
package com.seeth.user.utils.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.seeth.user.personal.models.User;
import com.seeth.user.personal.models.dto.UserMapper;
import com.seeth.user.personal.models.dto.UserModel;
import com.seeth.user.personal.services.UserService;
import com.seeth.user.utils.services.CurrentUserUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SeethendReddy
 *
 */
@RestController
@Api(value="",description = "User Resource")
@Slf4j
public class UserUtilController {
	
	@Autowired
	private UserService userService;

	@ApiOperation(value = "login", notes = "This endpoint is used to login")
	@PostMapping("/login")
	public String login(@RequestBody @ApiParam(value = "Login User Model") LoginUser loginUser) {
		log.error("should not be called");
		return "";
	}
	
	@ApiOperation(value = "getUser", notes = "This endpoint is used for retrieving user details")
	@GetMapping("secured/user/basic")
	public UserModel getUserBasicModel() {
		User user = CurrentUserUtil.getLoggedInUser();
		user.setPassword("");
		
		UserModel userModel = UserMapper.toModel(user);
		
		return userModel;
	}
	
	@ApiOperation(value = "getUser", notes = "This endpoint is used for retrieving user details")
	@GetMapping("secured/user")
	public UserModel getUserModel() {
		User user = CurrentUserUtil.getLoggedInUser();
		user.setPassword("");
		
		UserModel userModel = UserMapper.toModel(user);
		
		return userModel;
	}

	@GetMapping("encode")
	public void encodeTestPasswords() {
		userService.encodeAllUserPasswords();
	}
	
	@JsonSerialize
	class LoginUser {
		
		@JsonProperty
		private String username;

		@JsonProperty
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
	}
}
