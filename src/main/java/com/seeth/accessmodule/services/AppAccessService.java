/**
 * 
 */
package com.seeth.accessmodule.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seeth.accessmodule.models.AppAccessMapping;
import com.seeth.accessmodule.repositories.AppAccessRepository;
import com.seeth.user.personal.services.UserAuthHelper.AccessModuleType;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;

/**
 * @author SeethendReddy
 *
 */
@Service
public class AppAccessService {

	@Autowired
	private AppAccessRepository accessRepository;

	/**
	 * 05-Nov-2020 12:59:56 am
	 *
	 *
	 * @param roles
	 * @param moduleType
	 * void
	 * @return 
	 */
	public Set<String> getUserAccessModules(Set<UserRole> roles, AccessModuleType moduleType) {
		
		List<AppAccessMapping> accessModulesByRolesAndModule = accessRepository.getAccessModulesByRolesAndModule(roles, moduleType.name());
		
		if(accessModulesByRolesAndModule == null)
			return new HashSet<>();
		
		Set<String> accessModulesSet = accessModulesByRolesAndModule.stream().map(p -> p.getModule().getModuleName()).collect(Collectors.toSet());
		
		return accessModulesSet;
	}
	
	
}
