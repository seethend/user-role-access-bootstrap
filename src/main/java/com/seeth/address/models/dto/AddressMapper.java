/**
 * 
 */
package com.seeth.address.models.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seeth.address.models.AddressDetail;

/**
 * @author SeethendReddy
 *
 */
public class AddressMapper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressMapper.class);

	public static AddressDetail toEntity(AddressModel addressModel) {
		
		AddressDetail addressDetail = new AddressDetail();

		try {
			addressDetail.setId(addressModel.getId());
			addressDetail.setStreet(addressModel.getStreet());
			addressDetail.setState(addressModel.getState());
			addressDetail.setCity(addressModel.getCity());
			addressDetail.setCountry(addressModel.getCountry());
			addressDetail.setZipCode(addressModel.getZipCode());
		} catch(Exception e) {
			LOGGER.error("AddressDetail ::: Model to Entity conversion error");
		}
		
		return addressDetail;
	}
	
	public static AddressModel toModel(AddressDetail addressDetail) {
		
		AddressModel addressModel = new AddressModel();

		try {
			addressModel.setId(addressDetail.getId());
			addressModel.setStreet(addressDetail.getStreet());
			addressModel.setState(addressDetail.getState());
			addressModel.setCity(addressDetail.getCity());
			addressModel.setCountry(addressDetail.getCountry());
			addressModel.setZipCode(addressDetail.getZipCode());
			addressModel.setCreateDate(addressDetail.getCreateDate());
			addressModel.setUpdateDate(addressDetail.getUpdateDate());
		} catch (Exception e) {
			LOGGER.error("AddressDetail ::: Entity to Model conversion error");
		}
		
		return addressModel;
	}

	/**
	 * 08-Nov-2020 4:27:24 pm
	 *
	 *
	 * @param savedAddressDetail
	 * @param addressModel
	 * void
	 */
	public static void updateEntity(AddressDetail addressDetail, AddressModel addressModel) {

		if(addressModel.getStreet() != null) {
			addressDetail.setStreet(addressModel.getStreet());
		}
		
		if(addressModel.getState() != null) {
			addressDetail.setState(addressModel.getState());
		}
		
		if(addressModel.getCity() != null) {
			addressDetail.setCity(addressModel.getCity());
		}
		
		if(addressModel.getCountry() != null) {
			addressDetail.setCountry(addressModel.getCountry());
		}
		
		if(addressModel.getZipCode() != null) {
			addressDetail.setZipCode(addressModel.getZipCode());
		}
		
	}
}
