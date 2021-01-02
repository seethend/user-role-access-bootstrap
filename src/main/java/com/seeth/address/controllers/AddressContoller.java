/**
 * 
 */
package com.seeth.address.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seeth.address.models.dto.AddressModel;
import com.seeth.address.services.AddressHelperService;
import com.seeth.utils.response.AppResponseEntity;
import com.seeth.utils.response.AppResponseModel;

/**
 * @author SeethendReddy
 *
 */
@Api(value="v1/address",description = "Address Resource")
@RestController
@RequestMapping("v1/address/")
public class AddressContoller {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressContoller.class);
	
	@Autowired
	private AddressHelperService addressHelperService;
	
	@ApiOperation(value = "updateUserAddress", notes = "Updates the User Address")
	@PostMapping("secured/user/{userId}")
	public AppResponseEntity<AppResponseModel<AddressModel >> updateUserAddress
			(@ApiParam(value = "JSON post body with updated User Address") @RequestBody AddressModel addressModel,
										   @PathVariable int userId) {
		
		LOGGER.debug("Controller ::: User Address update controller");
		
		return AppResponseEntity.sendSuccessResponse(
				addressHelperService.updateUserAddress(userId, addressModel), HttpStatus.OK);
	}
}
