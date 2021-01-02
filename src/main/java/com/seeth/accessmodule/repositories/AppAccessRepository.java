/**
 * 
 */
package com.seeth.accessmodule.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seeth.accessmodule.models.AppAccessMapping;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;

/**
 * @author SeethendReddy
 *
 */
public interface AppAccessRepository extends JpaRepository<AppAccessMapping, Integer> {

	
	
	@Query("SELECT a FROM  AppAccessMapping a where a.role.userRole = :roleName and a.module.parentModule.id = ( "
			+ "SELECT m.id from AppModule m where m.moduleName = :moduleName"
			+ " )")
	public List<AppAccessMapping> getAccessModulesByRoleAndModule(@Param("roleName") String roleName, @Param("moduleName") String moduleName);
	
	
	
	@Query("SELECT a FROM  AppAccessMapping a where a.role.userRole IN ( :roleNames ) and a.module.parentModule.id = ( "
			+ "SELECT m.id from AppModule m where m.moduleName = :moduleName"
			+ " )")
	public List<AppAccessMapping> getAccessModulesByRolesAndModule(@Param("roleNames") Set<UserRole> roleNames, @Param("moduleName") String moduleName);
	
	
	
	@Query("SELECT a FROM  AppAccessMapping a where a.role.userRole = :roleName")
	public List<AppAccessMapping> getAccessModulesByRole(@Param("roleName") String roleName);
	
	
	
	@Query("SELECT a FROM  AppAccessMapping a where a.role.userRole IN ( :roleNames )")
	public List<AppAccessMapping> getAccessModulesByRoles(@Param("roleNames") Set<UserRole> roleNames);
	
}
