/**
 * 
 */
package com.seeth.address.services;

import java.sql.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seeth.address.models.AddressDetail;
import com.seeth.address.models.dto.AddressMapper;
import com.seeth.address.models.dto.AddressModel;
import com.seeth.address.repositories.AddressRepository;

/**
 * @author SeethendReddy
 *
 */
@Service
public class AddressService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);
	
	@Autowired
	private AddressRepository addressRepository;
	


	/**
	 * 08-Nov-2020 4:30:50 pm
	 *
	 *
	 * @param addressModel
	 * @return
	 * AddressDetail
	 */
	private AddressDetail saveAddress(AddressModel addressModel) {
		return addressRepository.save(AddressMapper.toEntity(addressModel));
	}
	

	/**
	 * 
	 * 08-Nov-2020 4:32:02 pm
	 *
	 *
	 * @param addressDetail
	 * @return
	 * AddressDetail
	 */
	public AddressDetail saveAddress(AddressDetail addressDetail) {
		
		if(addressDetail.getCreateDate() == null) {
			addressDetail.setCreateDate(new Date(System.currentTimeMillis()));
		} else {
			addressDetail.setUpdateDate(new Date(System.currentTimeMillis()));
		}
		
		return addressRepository.save(addressDetail);
	}

	/**
	 *
	 * @param addressId
	 * @return
	 */
	public AddressModel fetchAddressModel(int addressId) {
		
		AddressDetail addressDetail = fetchAddress(addressId);
		
		return AddressMapper.toModel(addressDetail);
	}

	/**
	 * 08-Nov-2020 3:50:46 pm
	 *
	 *
	 * @param addressId
	 * @return
	 * AddressDetail
	 */
	public AddressDetail fetchAddress(int addressId) {
		
		AddressDetail addressDetail = null;
		
		Optional<AddressDetail> optionalAddressDetail = addressRepository.findById(addressId);
		
		if(!optionalAddressDetail.isPresent()) {
			LOGGER.error("Address not found");
			return null;
		}
		
		addressDetail = optionalAddressDetail.get();
		
		return addressDetail;
	}
	
	/**
	 * 
	 * 08-Nov-2020 3:56:43 pm
	 *
	 *
	 * @param addressModel
	 * @return
	 * AddressModel
	 */
	public AddressModel saveAddressModel(AddressModel addressModel) {
		
		AddressDetail addressDetail = saveAddress(addressModel);
		
		return AddressMapper.toModel(addressDetail);
	}

	/**
	 *
	 * @param addressModel
	 * @return
	 */
	public AddressModel updateAddress(AddressModel addressModel) {
		
		AddressDetail savedAddressDetail = fetchAddress(addressModel.getId());
		
		if(savedAddressDetail == null) {
			LOGGER.error("Address not found");
			return null;
		}
		
		AddressMapper.updateEntity(savedAddressDetail, addressModel);
		
		AddressDetail updatedAddressDetail = saveAddress(savedAddressDetail);
		
		if(updatedAddressDetail == null) {
			LOGGER.error("Address not saved");
			return null;
		}
		
		return AddressMapper.toModel(updatedAddressDetail);
	}
	
}
