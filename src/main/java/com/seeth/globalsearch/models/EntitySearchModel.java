/**
 * 
 */
package com.seeth.globalsearch.models;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.seeth.utils.models.status.UserStatusUtility.Gender;
import com.seeth.utils.models.status.UserStatusUtility.MaritalStatus;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;
import com.seeth.utils.models.status.UserStatusUtility.VisaStatus;
import com.tata.address.models.AddressDetail_;
import com.tata.user.personal.models.Role_;
import com.tata.user.personal.models.User_;

import lombok.Data;

/**
 * @author SeethendReddy
 *
 */
@Component
public class EntitySearchModel {
	
	@Data
	public class SearchMeta {
		private String attribute;
		private String fieldName;
		private Class<?> classType;
		private boolean filterEnable;
		private boolean sortEnable;

		public SearchMeta(String fieldName, Class<?> classType, boolean filterEnable, boolean sortEnable) {
			this.attribute = null;
			this.fieldName = fieldName;
			this.classType = classType;
			this.filterEnable = filterEnable;
			this.sortEnable = sortEnable;
		}


		public SearchMeta(String attribute, String fieldName, Class<?> classType, 
				boolean filterEnable, boolean sortEnable) {
			this.attribute = attribute;
			this.fieldName = fieldName;
			this.classType = classType;
			this.filterEnable = filterEnable;
			this.sortEnable = sortEnable;
		}
		
		
	}

	public Map<String, SearchMeta> getUserFieldDetails() {
		
		Map<String, SearchMeta> userFields = new HashMap<>();
		
		userFields.put("id", new SearchMeta(User_.ID, Integer.class, true, true));
		userFields.put("firstName", new SearchMeta(User_.FIRST_NAME, String.class, true, true));
		userFields.put("middleName", new SearchMeta(User_.MIDDLE_NAME, String.class, true, true));
		userFields.put("lastName", new SearchMeta(User_.LAST_NAME, String.class, true, true));
		userFields.put("ssnOrPan", new SearchMeta(User_.SSN_OR_PAN, String.class, true, true));
		userFields.put("personalEmailId", new SearchMeta(User_.PERSONAL_EMAIL_ID, String.class, true, true));
		userFields.put("marketingEmailId", new SearchMeta(User_.MARKETING_EMAIL_ID, String.class, true, true));
		userFields.put("dob", new SearchMeta(User_.DOB, Date.class, false, true));
		userFields.put("gender", new SearchMeta(User_.GENDER, Gender.class, true, true));
		userFields.put("maritalStatus", new SearchMeta(User_.MARITAL_STATUS, MaritalStatus.class, true, true));
		userFields.put("phoneNumber", new SearchMeta(User_.PHONE_NUMBER, String.class, true, true));
		userFields.put("visaStatus", new SearchMeta(User_.VISA_STATUS, VisaStatus.class, true, true));
		userFields.put("linkedInURL", new SearchMeta(User_.LINKED_IN_UR_L, String.class, true, true));
		userFields.put("hireDate", new SearchMeta(User_.HIRE_DATE, Date.class, false, true));
		userFields.put("active", new SearchMeta(User_.ACTIVE, Integer.class, true, true));
		userFields.put("createDate", new SearchMeta(User_.CREATE_DATE, Date.class, false, true));
		userFields.put("updateDate", new SearchMeta(User_.UPDATE_DATE, Date.class, false, true));
		
		// Roles Search Meta
		userFields.put("userRole", new SearchMeta(User_.ROLES, Role_.USER_ROLE, UserRole.class, true, true));

		// Address Search Meta
		userFields.put("street", new SearchMeta(User_.ADDRESS_DETAIL, AddressDetail_.STREET, String.class, true, true));
		userFields.put("city", new SearchMeta(User_.ADDRESS_DETAIL, AddressDetail_.CITY, String.class, true, true));
		userFields.put("state", new SearchMeta(User_.ADDRESS_DETAIL, AddressDetail_.STATE, String.class, true, true));
		userFields.put("country", new SearchMeta(User_.ADDRESS_DETAIL, AddressDetail_.COUNTRY, String.class, true, true));
		userFields.put("zipCode", new SearchMeta(User_.ADDRESS_DETAIL, AddressDetail_.ZIP_CODE, String.class, true, true));
		
		return userFields;
	}
	
}
