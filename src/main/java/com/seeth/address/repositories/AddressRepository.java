/**
 * 
 */
package com.seeth.address.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seeth.address.models.AddressDetail;

/**
 * @author SeethendReddy
 *
 */
public interface AddressRepository extends JpaRepository<AddressDetail, Integer> {

}
