package com.seeth.user.personal.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seeth.globalsearch.models.GlobalSearchModel;
import com.seeth.user.personal.models.User;
import com.seeth.user.personal.models.dto.UserMapper;
import com.seeth.user.personal.models.dto.UserModel;
import com.seeth.user.personal.services.UserHelperService;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;
import com.seeth.utils.response.AppResponseEntity;
import com.seeth.utils.response.AppResponseModel;

@RestController
@Api(value="v1/user/",description = "User Resource")
@RequestMapping("/v1/user/secured/")
@Slf4j
public class UserController {
	
	@Autowired
	private UserHelperService userHelperService;
	
	@GetMapping("all")
	public String openForAll() {
		return "Welcome User !!!";
	}

	@ApiOperation(value = "getUser", notes = "This endpoint is used for retrieving user details")
	@GetMapping("me")
	public UserModel getUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		user.setPassword("");
		
		UserModel userModel = UserMapper.toModel(user);
		
		return userModel;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("admin")
	public String helloAdmin() {
		return "Hello Admin";
	}
	
	@ApiOperation(value = "logout", notes = "This endpoint is used for logging out the user")
	@GetMapping("logout")
	public String logout(HttpServletRequest request) {
		HttpSession session= request.getSession(false);
        SecurityContextHolder.clearContext();
        if(session != null) {
            session.invalidate();
        }
        return "Successfully logged out !!!";
	}

	
	@ApiOperation(value = "Create User", notes = "This endpoint is used for creting new user - owners and admins")
	@PostMapping("create")
	public AppResponseEntity<AppResponseModel<UserModel>> createUser(
			@RequestBody @ApiParam(value = "User Model") UserModel userModel) {
		
		log.debug("Controller ::: User create controller");
		return AppResponseEntity.sendSuccessResponse(
				userHelperService.createNewUser(userModel), HttpStatus.CREATED);
	}
	
	@PostMapping("upload")
	public AppResponseEntity<AppResponseModel<String>> updateUserImage(
			@RequestPart(value = "file") 
			@ApiParam(value = "User Image form data")
			MultipartFile file) {

		log.debug("Controller ::: User image store controller");
		return AppResponseEntity.sendSuccessResponse(
				userHelperService.storeUserImage(file), HttpStatus.OK);
	}

	@PostMapping("update")
	@ApiOperation(value = "Update User", notes = "This endpoint is used for update user details")
	public AppResponseEntity<AppResponseModel<UserModel>> updateUser(
			@RequestBody @ApiParam(value = "User Model") UserModel userModel) {
		
		log.debug("Controller ::: User update controller");
		return AppResponseEntity.sendSuccessResponse(
				userHelperService.updateUser(userModel), HttpStatus.OK);
	}
	
	@PostMapping("password")
	@ApiOperation(value = "Update Password", notes = "This endpoint is used for update user password")
	public AppResponseEntity<AppResponseModel<UserModel>> updatePassword(
			@RequestBody @ApiParam(value = "User Model") UserModel userModel) {

		log.debug("Controller ::: User update password controller");
		return AppResponseEntity.sendSuccessResponse(
				userHelperService.updatePassword(userModel), HttpStatus.OK); 
	}

	@DeleteMapping("delete/{userId}")
	@ApiOperation(value = "deleteUser", notes = "This endpoint is used for deleting user details")
	public AppResponseEntity<AppResponseModel<String>> deleteUser(
			@PathVariable @ApiParam(value = "User Identifier")  int userId) {
		
		log.debug("Controller ::: User delete controller");
		String deleteUserStatus = userHelperService.deleteUser(userId);
		
		if(deleteUserStatus.toLowerCase().contains("success")) {
			return AppResponseEntity.sendSuccessResponse(deleteUserStatus, HttpStatus.OK);
		} else {
			return AppResponseEntity.sendErrorResponse(deleteUserStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("createAll")
	@ApiOperation(value = "createAllUsers", notes = "This endpoint is used for creating multiple users")
	public AppResponseEntity<AppResponseModel<List<UserModel>>> createAllUser(
			@RequestBody @ApiParam(value = "List of User Models") List<UserModel> userModels) {
		
		log.debug("Controller ::: User createAll controller");
		return AppResponseEntity.sendPartialResponse(
				userHelperService.saveUsers(userModels), userHelperService.getErrors(), HttpStatus.OK);
	}

	@ApiOperation(value = "addUserRoles", notes = "This endpoint is used adding roles to users")
	@PostMapping("{userId}/roles/add")
	public AppResponseEntity<AppResponseModel<UserModel>> addUserRoles(
			@RequestBody @ApiParam(value = "Set of User Rules") Set<UserRole> userRoles, 
			@PathVariable @ApiParam(value = "User Indentifier") int userId) {
		
		log.debug("Controller ::: Add User Roles controller");
		return AppResponseEntity.sendSuccessResponse(
				 userHelperService.addRoles(userId, userRoles), HttpStatus.OK);
	}
	
	@ApiOperation(value = "removeUserRoles", notes = "This endpoint is used for removing roles from user")
	@PostMapping("{userId}/roles/remove")
	public AppResponseEntity<AppResponseModel<UserModel>> removeUserRoles(
			@RequestBody @ApiParam(value = "Set of User Rules") Set<UserRole> userRoles, 
			@PathVariable @ApiParam(value = "User Indentifier") int userId) {
		
		log.debug("Controller ::: Remove User Roles controller");
		return AppResponseEntity.sendSuccessResponse(
				 userHelperService.removeRoles(userId, userRoles), HttpStatus.OK);
	}
	
	@ApiOperation(value = "searchUsers", notes = "This endpoint is used for searching user details")
	@PostMapping("search/{pageNumber}/{itemsPerPage}")
	public AppResponseEntity<AppResponseModel<GlobalSearchModel>> fetchConsultancyUsers(
			@ApiParam(value = "User Search Global Model with criteria to search") 
			@RequestBody GlobalSearchModel globalSearchModel,
    		@PathVariable @ApiParam(value = "Page Number") int pageNumber, 
			@PathVariable @ApiParam(value = "Items in Page") int itemsPerPage) {
		
		log.debug("Controller ::: User search controller");
		userHelperService.searchUsers(globalSearchModel, pageNumber, itemsPerPage);
		
		return AppResponseEntity.sendSuccessResponse(globalSearchModel, HttpStatus.FOUND);
	}
	
	@ApiOperation(value = "Export Users", notes = "This endpoint is used for exporting user details")
	@PostMapping("export/{pageNumber}/{itemsPerPage}")
	public AppResponseEntity<AppResponseModel<Map<String, String>>> exportConsultancyUsers(
			@ApiParam(value = "User Export") 
			@RequestBody GlobalSearchModel globalSearchModel,
    		@PathVariable @ApiParam(value = "Page Number") int pageNumber, 
			@PathVariable @ApiParam(value = "Items in Page") int itemsPerPage) {
		
		log.debug("Controller ::: User search controller");
		Map<String, String> exportStatus = 
				userHelperService.exportConsultancyUsers(globalSearchModel, pageNumber, itemsPerPage);
		
		return AppResponseEntity.sendSuccessResponse(exportStatus, HttpStatus.FOUND);
	}
	
	@ApiOperation(value = "userStats", notes = "This endpoint is used for fetch user stats")
	@PostMapping("stats/{userRole}")
	public AppResponseEntity<AppResponseModel<Map<String, Long>>> fetchConsultancyUserStats(
			@ApiParam(value = "User Role for stats") 
			@PathVariable UserRole userRole) {
		
		log.debug("Controller ::: User stats controller");
		Map<String, Long> userStats = userHelperService.fetchUserStats(userRole);
		
		return AppResponseEntity.sendSuccessResponse(userStats, HttpStatus.FOUND);
	}
}
