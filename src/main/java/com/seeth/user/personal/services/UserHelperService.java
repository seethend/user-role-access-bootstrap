/**
 * 
 */
package com.seeth.user.personal.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.seeth.exception.models.AppRunTimeException;
import com.seeth.exception.models.AppErrorCode;
import com.seeth.globalsearch.models.GlobalSearchModel;
import com.seeth.user.personal.models.User;
import com.seeth.user.personal.models.dto.UserMapper;
import com.seeth.user.personal.models.dto.UserModel;
import com.seeth.user.personal.services.UserAuthHelper.AccessModuleType;
import com.seeth.user.utils.services.CurrentUserUtil;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;
import com.seeth.utils.response.AppErrorModel;
import com.seeth.utils.services.BasicUtilService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SeethendReddy
 *
 */
@Service
@Slf4j
public class UserHelperService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserAuthHelper userAuthHelper;

	private @Setter @Getter List<AppErrorModel> errors;

	/**
	 * 
	 * 31-Oct-2020 3:04:49 am
	 *
	 *
	 * @param userModel
	 * @return
	 * ResponseEntity<UserModel>
	 */
	public UserModel createNewUser(UserModel userModel) {

		if(userModel == null) {
			throw new AppRunTimeException(AppErrorCode.INVALID_INPUT_DATA);
		}

		if(!userAuthHelper.checkUserPermissions(userModel.getRoles(), UserAuthHelper.AccessModuleType.CREATE_USER)) {
			throw new AppRunTimeException(AppErrorCode.INSUFFICIENT_PRIVILEGES);
		}

		if(userService.getUserByUsername(userModel.getUsername()) != null) {
			throw new AppRunTimeException(AppErrorCode.USER_ALREADY_EXISTS);
		}

		String randomPassword = BasicUtilService.generateRandomPassword(6);

		UserModel userModelRes = userService.checkAndSaveUser(userModel, randomPassword);

		if(userModelRes == null) {
			throw new AppRunTimeException(AppErrorCode.DATA_NOT_SAVED);
		}

		return userModelRes;
	}

	/**
	 * 11-Dec-2020 12:28:14 am
	 *
	 *
	 * @param userId
	 * @param file
	 * @return
	 * String
	 */
	public String storeUserImage(MultipartFile file) {

		User currUser = CurrentUserUtil.getLoggedInUser();

		String userImage = userService.storeUserImage(currUser, file);

		if(userImage == null)
			throw new AppRunTimeException(AppErrorCode.IMAGE_NOT_SAVED);

		return userImage;
	}

	/**
	 * 17-Dec-2020 1:16:42 am
	 *
	 *
	 * @return
	 * Object
	 */
	public UserModel updatePassword(UserModel userModel) {

		if(userModel == null || userModel.getPassword() != null || userModel.getPassword() != null)
			throw new AppRunTimeException(AppErrorCode.INVALID_INPUT_DATA);

		User currUser = CurrentUserUtil.getLoggedInUser();

		User updatedUser = userService.updateCurrentUserPassword(currUser, userModel.getPassword());

		if(updatedUser == null)
			throw new AppRunTimeException(AppErrorCode.DATA_NOT_SAVED);

		return UserMapper.toModel(updatedUser);
	}

	/**
	 * 
	 * 31-Oct-2020 3:04:53 am
	 *
	 *
	 * @param userModel
	 * @return
	 * ResponseEntity<UserModel>
	 */
	public UserModel updateUser(UserModel userModel) {

		if(userModel == null || userModel.getId() == 0) {
			throw new AppRunTimeException(AppErrorCode.INVALID_INPUT_DATA);
		}

		User savedUser = userService.getUserByUserId(userModel.getId());

		if(savedUser == null) {
			log.debug("User doesn't exists");
			throw new AppRunTimeException(AppErrorCode.USER_NOT_FOUND);
		}

		UserModel savedUserModel = UserMapper.toModel(savedUser);

		if(!userAuthHelper.checkUserPermissions(savedUserModel.getRoles(), UserAuthHelper.AccessModuleType.UPDATE_USER)) {
			throw new AppRunTimeException(AppErrorCode.INSUFFICIENT_PRIVILEGES);
		}

		UserMapper.updateModel(savedUser, userModel);

		User updatedUser = userService.saveUserEntity(savedUser);

		if(updatedUser == null) {
			throw new AppRunTimeException(AppErrorCode.DATA_NOT_SAVED);
		}

		return UserMapper.toModel(updatedUser);
	}


	/**
	 * 
	 * 31-Oct-2020 3:04:57 am
	 *
	 *
	 * @param userModels
	 * @return
	 * ResponseEntity<List<UserModel>>
	 */
	public List<UserModel> saveUsers(List<UserModel> userModels) {

		if(userModels == null || userModels.isEmpty()) {
			throw new AppRunTimeException(AppErrorCode.INVALID_INPUT_DATA);
		}

		userService.setErrors(new ArrayList<>());

		List<UserModel> resUserModels = new ArrayList<>();

		log.debug("User from request body \n" + userModels.size());

		if(userModels != null && !userModels.isEmpty()) {
			resUserModels.addAll(userService.saveAllUsers(userModels));
		}

		errors = userService.getErrors().stream().map(p -> new AppErrorModel(p)).collect(Collectors.toList());

		return resUserModels;
	}

	/**
	 * 
	 * 31-Oct-2020 3:05:00 am
	 *
	 *
	 * @param userId
	 * @return
	 * ResponseEntity<Map<String,String>>
	 */
	public String deleteUser(int userId) {

		if(userId == 0) {
			throw new AppRunTimeException(AppErrorCode.INVALID_INPUT_DATA);
		}

		User user = userService.getUserByUserId(userId);

		if(user == null) {
			throw new AppRunTimeException(AppErrorCode.USER_NOT_FOUND);
		}

		UserModel userModel = UserMapper.toModel(user);
		if(!userAuthHelper.checkUserPermissions(userModel.getRoles(), UserAuthHelper.AccessModuleType.DELETE_USER)) {
			throw new AppRunTimeException(AppErrorCode.INSUFFICIENT_PRIVILEGES);
		}

		boolean delStatus = userService.deleteUser(user);

		if(!delStatus) {
			return "Successfully deleted the User !!!";
		} else {
			return "Failed to delete User !!!";
		}

	}

	/**
	 * 03-Nov-2020 4:38:10 pm
	 *
	 *
	 * @param userId
	 * @param userRoles
	 * @return
	 * ResponseEntity<AppResponseModel<List<UserModel>>>
	 */
	public UserModel addRoles(int userId, Set<UserRole> userRoles) {

		if(userId == 0 || userRoles == null || userRoles.isEmpty()) {
			throw new AppRunTimeException(AppErrorCode.INVALID_INPUT_DATA);
		}

		if(!userAuthHelper.checkUserRoleAlterPermissions(userId, userRoles)) {
			throw new AppRunTimeException(AppErrorCode.INSUFFICIENT_PRIVILEGES);
		}

		User existingUser = userService.getUserByUserId(userId);

		if(existingUser == null) {
			throw new AppRunTimeException(AppErrorCode.DATA_NOT_SAVED);
		}

		User updatedUser = userService.addRoles(existingUser, userRoles);

		return UserMapper.toModel(updatedUser);
	}

	/**
	 * 03-Nov-2020 4:38:15 pm
	 *
	 *
	 * @param userId
	 * @param userRoles
	 * @return
	 * ResponseEntity<AppResponseModel<List<UserModel>>>
	 */
	public UserModel removeRoles(int userId, Set<UserRole> userRoles) {

		if(userId == 0 || userRoles == null || userRoles.isEmpty()) {
			throw new AppRunTimeException(AppErrorCode.INVALID_INPUT_DATA);
		}

		if(!userAuthHelper.checkUserRoleAlterPermissions(userId, userRoles)) {
			throw new AppRunTimeException(AppErrorCode.INSUFFICIENT_PRIVILEGES);
		}

		User existingUser = userService.getUserByUserId(userId);

		if(existingUser == null) {
			throw new AppRunTimeException(AppErrorCode.USER_NOT_FOUND);
		}

		User updatedUser = userService.removeRoles(existingUser, userRoles);

		return UserMapper.toModel(updatedUser);
	}

	/**
	 * 25-Nov-2020 10:35:21 pm
	 * @param globalSearchModel 
	 *
	 *
	 * @param consIds
	 * @param itemsPerPage 
	 * @return
	 * AppResponseEntity<AppResponseModel<UserModel>>
	 */
	public void searchUsers(GlobalSearchModel globalSearchModel, int pageNumber, int itemsPerPage) {

		User currUser = CurrentUserUtil.getLoggedInUser();

		List<UserRole> currUserRoles = 
								currUser.getRoles()
									.stream()
									.map(p -> p.getUserRole())
									.collect(Collectors.toList());

		if(!currUserRoles.contains(UserRole.APP_OWNER)) {
			throw new AppRunTimeException(AppErrorCode.INSUFFICIENT_PRIVILEGES);
		}

		userService.searchUsers(globalSearchModel, pageNumber, itemsPerPage);
	}

	/**
	 * 28-Dec-2020 11:40:41 pm
	 *
	 *
	 * @param globalSearchModel
	 * @param pageNumber
	 * @param itemsPerPage
	 * void
	 */
	public Map<String, Long> fetchUserStats(UserRole userRole) {
		
		User loggedInUser = CurrentUserUtil.getLoggedInUser();

		Set<UserRole> loggedInUserRole = 
								loggedInUser.getRoles()
									.stream()
									.map(p -> p.getUserRole())
									.collect(Collectors.toSet());
		
		Set<UserRole> currUserRole = new HashSet<>();
		currUserRole.add(userRole);
		
		userAuthHelper.checkUserRolesPermissions(loggedInUserRole, currUserRole, AccessModuleType.CREATE_USER);
		
		return userService.fetchUserStats(userRole);
	}

	/**
	 * 30-Dec-2020 1:44:18 pm
	 *
	 *
	 * @param globalSearchModel
	 * @param pageNumber
	 * @param itemsPerPage
	 * void
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> exportConsultancyUsers(GlobalSearchModel globalSearchModel, int pageNumber, int itemsPerPage) {
		
		searchUsers(globalSearchModel, pageNumber, itemsPerPage);
		
		List<UserModel> userModels = (List<UserModel>) globalSearchModel.getResults();
		
		if(userModels == null)
			throw new AppRunTimeException(AppErrorCode.INTERNAL_SERVER_ERROR);
		
		Map<String, String> exportDetails = new TreeMap<>();
		
		if(userModels.isEmpty()) {
			exportDetails.put("status", "EMPTY");
			return exportDetails;
		}
		
		String excelPath = userService.storeAndReturnExcelPath(userModels);
		
		if(excelPath == null) {
			exportDetails.put("status", "ERROR");
			return exportDetails;
		}
		
		exportDetails.put("status", "SUCCESS");
		exportDetails.put("path", excelPath);
		
		return exportDetails;
	}

}
