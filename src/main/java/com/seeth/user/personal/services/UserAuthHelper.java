package com.seeth.user.personal.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.seeth.utils.models.status.UserStatusUtility.UserRole;

@Service
public class UserAuthHelper {

	public enum AccessModuleType {
		CREATE_USER, UPDATE_USER, DELETE_USER

	}

	public boolean checkUserPermissions(Set<UserRole> roles, AccessModuleType createUser) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean checkUserRoleAlterPermissions(int userId, Set<UserRole> userRoles) {
		// TODO Auto-generated method stub
		return false;
	}

	public void checkUserRolesPermissions(Set<UserRole> loggedInUserRole, Set<UserRole> currUserRole,
			AccessModuleType createUser) {
		// TODO Auto-generated method stub
		
	}

}
