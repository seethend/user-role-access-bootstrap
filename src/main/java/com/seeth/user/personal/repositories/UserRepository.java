/**
 * 
 */
package com.seeth.user.personal.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.seeth.user.personal.models.User;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;


/**
 * @author Seethend Reddy D
 *
 *
 *
 *
 * Dec 3, 2018 12:38:28 AM
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

	public Optional<User> findByUsername(String username);
	
	public List<User> findByLastNameContainingIgnoreCase(String searchString);
	
	public List<User> findByFirstNameContainingIgnoreCase(String searchString);

	public List<User> findByRolesUserRole(UserRole userRole);

	@Query("SELECT COUNT(U.id) FROM User U LEFT JOIN U.roles R WHERE R.userRole = :userRole")
	public long getCountByUserRole(@Param("userRole") UserRole userRole);
	
	@Query("SELECT COUNT(U.id) FROM User U LEFT JOIN U.roles R WHERE R.userRole = :userRole and U.active = :active")
	public long getUsersByActive(@Param("userRole") UserRole userRole, @Param("active") int active);
	
	@Query("SELECT COUNT(U.id) FROM User U LEFT JOIN U.roles R WHERE R.userRole = :userRole and U.createDate >= :createDate")
	public long getUsersInLastMonth(@Param("userRole") UserRole userRole, @Param("createDate") Date createDate);
}
