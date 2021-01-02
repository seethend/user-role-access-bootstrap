/**
 * 
 */
package com.seeth.address.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seeth.address.models.dto.AddressModel;
import com.seeth.exception.models.AppRunTimeException;
import com.seeth.exception.models.AppErrorCode;
import com.seeth.user.personal.models.dto.UserModel;
import com.seeth.user.personal.services.UserAuthHelper;
import com.seeth.user.personal.services.UserService;
import com.seeth.user.personal.services.UserAuthHelper.AccessModuleType;

/**
 * @author SeethendReddy
 *
 */
@Service
public class AddressHelperService {
	
	@Autowired
	private UserAuthHelper userAuthHelper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;

	/**
	 * 10-Nov-2020 1:56:59 am
	 *
	 *
	 * @param id
	 * @param addressModel
	 * @return
	 * boolean
	 */
	private boolean checkForInvalidInput(int id, AddressModel addressModel) {

		return (id == 0 || addressModel == null || addressModel.getId() == 0);
	}

	/**
	 * 10-Nov-2020 2:01:06 am
	 *
	 *
	 * @param updatedAddressModel
	 * @return
	 * boolean
	 */
	private boolean checkForInvalidOutput(AddressModel updatedAddressModel) {
		return (updatedAddressModel == null || updatedAddressModel.getId() == 0);
	}
	
	/**
	 * 08-Nov-2020 4:05:17 pm
	 * @param id 
	 *
	 *
	 * @param addressModel
	 * @param moduleType 
	 * @return
	 * AddressModel
	 */
	public AddressModel updateUserAddress(int id, AddressModel addressModel) {
		
		if(checkForInvalidInput(id, addressModel)) {
			throw new AppRunTimeException(AppErrorCode.ADDRESS_NOT_FOUND);
		}
		
		UserModel currUserModel = userService.fetchUser(id);
		
		if(currUserModel == null) {
			throw new AppRunTimeException(AppErrorCode.USER_NOT_FOUND);
		}
		
		if(!userAuthHelper.checkUserPermissions(currUserModel.getRoles(), AccessModuleType.UPDATE_USER)) {
			throw new AppRunTimeException(AppErrorCode.UNAUTHORISED_ACCESS);
		}

		AddressModel updatedAddressModel = addressService.updateAddress(addressModel);
		
		if(checkForInvalidOutput(updatedAddressModel)) {
			throw new AppRunTimeException(AppErrorCode.DATA_NOT_SAVED);
		}
		
		return updatedAddressModel;
	}
	
}
