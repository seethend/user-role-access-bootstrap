/**
 * 
 */
package com.seeth.address.models.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SeethendReddy
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
@ApiModel(description = "Request body for User Address object")
public class AddressModel {

	@JsonProperty
	@ApiModelProperty(notes = "Address Identifier")
	private Integer id;

	@JsonProperty
	@ApiModelProperty(notes = "Street Name")
	private String street;

	@JsonProperty
	@ApiModelProperty(notes = "City Name")
	private String city;

	@JsonProperty
	@ApiModelProperty(notes = "State")
	private String state;

	@JsonProperty
	@ApiModelProperty(notes = "Country")
	private String country;

	@JsonProperty
	@ApiModelProperty(notes = "ZipCode")
	private String zipCode;
    
    @JsonProperty
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	@ApiModelProperty(notes = "Create Date")
	private Date createDate;
    
    @JsonProperty
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	@ApiModelProperty(notes = "Update Date")
	private Date updateDate;
	
}
