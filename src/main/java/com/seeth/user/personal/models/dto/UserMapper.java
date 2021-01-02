package com.seeth.user.personal.models.dto;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seeth.address.models.dto.AddressMapper;
import com.seeth.user.personal.models.User;

public class UserMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserMapper.class);

	public static User toEntity(UserModel userModel) {

		User user = new User();

		try {
			user.setId(userModel.getId());
			user.setUsername(userModel.getUsername());
			user.setPassword(userModel.getPassword());
			user.setFirstName(userModel.getFirstName());
			user.setMiddleName(userModel.getMiddleName());
			user.setLastName(userModel.getLastName());
			user.setAvatar(userModel.getAvatar());
			user.setSsnOrPan(userModel.getSsnOrPan());
			user.setPersonalEmailId(userModel.getPersonalEmailId());
			user.setMarketingEmailId(userModel.getMarketingEmailId());
			user.setDob(userModel.getDob());
			user.setGender(userModel.getGender());
			user.setMaritalStatus(userModel.getMaritalStatus());
			user.setPhoneNumber(userModel.getPhoneNumber());
			user.setVisaStatus(userModel.getVisaStatus());
			user.setLinkedInURL(userModel.getLinkedInURL());
			user.setHireDate(userModel.getHireDate());
			user.setActive(0);

			if(userModel.getAddress() != null) {
				user.setAddressDetail(AddressMapper.toEntity(userModel.getAddress()));
			}

		} catch(Exception e) {
			LOGGER.error("User ::: Model to entity conversion error");
		}

		return user;
	}

	public static UserModel toModel(User user) {

		UserModel userModel = new UserModel();

		userModel.setId(user.getId());
		userModel.setUsername(user.getUsername());
		//		userModel.setPassword(user.getPassword());
		userModel.setFirstName(user.getFirstName());
		userModel.setMiddleName(user.getMiddleName());
		userModel.setLastName(user.getLastName());
		userModel.setAvatar(user.getAvatar());
		userModel.setSsnOrPan(user.getSsnOrPan());
		userModel.setPersonalEmailId(user.getPersonalEmailId());
		userModel.setMarketingEmailId(user.getMarketingEmailId());
		userModel.setDob(user.getDob());
		userModel.setGender(user.getGender());
		userModel.setMaritalStatus(user.getMaritalStatus());
		userModel.setPhoneNumber(user.getPhoneNumber());

		userModel.setVisaStatus(user.getVisaStatus());
		userModel.setLinkedInURL(user.getLinkedInURL());
		userModel.setHireDate(user.getHireDate());

		userModel.setActive(user.getActive());
		userModel.setCreateDate(user.getCreateDate());
		userModel.setUpdateDate(user.getUpdateDate());

		if(user.getRoles() != null) {
			userModel.setRoles(user.getRoles().stream().map(p -> p.getUserRole()).collect(Collectors.toSet()));
		}
		
		if(user.getAddressDetail() != null) {
			userModel.setAddress(AddressMapper.toModel(user.getAddressDetail()));
		}

		return userModel;
	}

	public static void updateModel(User savedUser, UserModel userModel) {

		if(userModel.getFirstName() != null) {
			savedUser.setFirstName(userModel.getFirstName());
		}

		if(userModel.getMiddleName() != null) {
			savedUser.setMiddleName(userModel.getMiddleName());
		}

		if(userModel.getLastName() != null) {
			savedUser.setLastName(userModel.getLastName());
		}

		if(userModel.getSsnOrPan() != null) {
			savedUser.setSsnOrPan(userModel.getSsnOrPan());
		}

		if(userModel.getPersonalEmailId() != null) {
			savedUser.setPersonalEmailId(userModel.getPersonalEmailId());
		}

		if(userModel.getMarketingEmailId() != null) {
			savedUser.setMarketingEmailId(userModel.getMarketingEmailId());
		}
		
		if(userModel.getDob() != null) {
			savedUser.setDob(userModel.getDob());
		}
		
		if(userModel.getGender() != null) {
			savedUser.setGender(userModel.getGender());
		}
		
		if(userModel.getMaritalStatus() != null) {
			savedUser.setMaritalStatus(userModel.getMaritalStatus());
		}

		if(userModel.getPhoneNumber() != null) {
			savedUser.setPhoneNumber(userModel.getPhoneNumber());
		}

		if(userModel.getVisaStatus() != null) {
			savedUser.setVisaStatus(userModel.getVisaStatus());
		}

		if(userModel.getLinkedInURL() != null) {
			savedUser.setLinkedInURL(userModel.getLinkedInURL());
		}

		if(userModel.getHireDate() != null) {
			savedUser.setHireDate(userModel.getHireDate());
		}

		if(userModel.getActive() != 0) {
			savedUser.setActive(userModel.getActive());
		}

	}

}
