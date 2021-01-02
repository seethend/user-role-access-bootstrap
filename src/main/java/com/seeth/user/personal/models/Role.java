package com.seeth.user.personal.models;

/**
 * 
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.seeth.utils.models.status.UserStatusUtility.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cmps_roles")
public class Role {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "role_id")
	private int id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role_name")
	private UserRole userRole;
	
}