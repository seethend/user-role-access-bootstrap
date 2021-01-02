/**
 * 
 */
package com.seeth.user.personal.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.seeth.user.personal.models.Role;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;

/**
 * @author SeethendReddy
 *
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

	/**
	 * 05-Nov-2020 2:21:30 am
	 *
	 *
	 * @param userRoles
	 * void
	 * @return 
	 */
	@Query("SELECT r FROM Role r where r.userRole IN ( :userRoles ) ")
	public Set<Role> findAllByUserRole(@Param("userRoles") Set<UserRole> userRoles);

}
