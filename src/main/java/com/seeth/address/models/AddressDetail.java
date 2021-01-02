/**
 * 
 */
package com.seeth.address.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SeethendReddy
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cmps_address_details")
public class AddressDetail {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
    @Column(name = "addr_id")
	private Integer id;

    @Column(name = "street", nullable = false, length = 500)
	private String street;

    @Column(name = "city", nullable = false, length = 500)
	private String city;

    @Column(name = "state", nullable = false, length = 500)
	private String state;

    @Column(name = "country", nullable = false, length = 500)
	private String country;

    @Column(name = "zip_code", nullable = true, length = 500)
	private String zipCode;
	
	@Column(name = "create_date", nullable = false)
	private Date createDate;
	
	@Column(name = "update_date")
	private Date updateDate;
    
}
