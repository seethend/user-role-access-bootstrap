package com.seeth.user.personal.models.dto;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.seeth.address.models.dto.AddressModel;
import com.seeth.utils.models.status.UserStatusUtility.Gender;
import com.seeth.utils.models.status.UserStatusUtility.MaritalStatus;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;
import com.seeth.utils.models.status.UserStatusUtility.VisaStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
@ApiModel(description = "Request and response body for User Profile")
public class UserModel {

    @JsonProperty
	@ApiModelProperty(notes = "User Identifier")
	private int id;
	
    @JsonProperty
	@ApiModelProperty(notes = "User Name")
	private String username;
    
    @JsonProperty
	@ApiModelProperty(notes = "Password")
	private String password;
    
    @JsonProperty
	@ApiModelProperty(notes = "First Name")
	private String firstName;
    
    @JsonProperty
	@ApiModelProperty(notes = "Middle Name")
	private String middleName;
    
    @JsonProperty
	@ApiModelProperty(notes = "Last Name")
	private String lastName;
    
    @JsonProperty
	@ApiModelProperty(notes = "User Avatar Path")
	private String avatar;
    
    @JsonProperty
	@ApiModelProperty(notes = "SSN or PAN")
	private String ssnOrPan;
    
    @JsonProperty
	@ApiModelProperty(notes = "Personal Email Identifier")
	private String personalEmailId;
    
    @JsonProperty
	@ApiModelProperty(notes = "Marketing Email Identifier")
	private String marketingEmailId;
    
    @JsonProperty
	@ApiModelProperty(notes = "User date of birth")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date dob;
    
    @JsonProperty
	@ApiModelProperty(notes = "User gender")
    private Gender gender;
    
    @JsonProperty
	@ApiModelProperty(notes = "User marital status")
    private MaritalStatus maritalStatus; 
    
    @JsonProperty
	@ApiModelProperty(notes = "User Phone Number")
	private String phoneNumber;
    
    @JsonProperty
	@ApiModelProperty(notes = "Visa status of the User")
	private VisaStatus visaStatus;
    
    @JsonProperty
	@ApiModelProperty(notes = "Linked in Url")
	private String linkedInURL;
    
    @JsonProperty
	@ApiModelProperty(notes = "User hired dates")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date hireDate;
    
    @JsonProperty
	@ApiModelProperty(notes = "user active status")
	private int active;

    @JsonProperty
	@ApiModelProperty(notes = "user roles to be assigned")
	@Getter(value = AccessLevel.NONE)
    private Set<UserRole> roles = new HashSet<>();

    @JsonProperty
	@ApiModelProperty(notes = "User address")
    private AddressModel address;
    
    @JsonProperty
	@ApiModelProperty(notes = "user profile create date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date createDate;
    
    @JsonProperty
	@ApiModelProperty(notes = "user profile update date")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date updateDate;

	public Set<UserRole> getRoles() {
		
		if(roles == null)
			return new HashSet<>();
		
		return roles;
	}
}
