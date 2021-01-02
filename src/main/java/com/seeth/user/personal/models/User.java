package com.seeth.user.personal.models;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.seeth.address.models.AddressDetail;
import com.seeth.utils.models.status.UserStatusUtility.Gender;
import com.seeth.utils.models.status.UserStatusUtility.MaritalStatus;
import com.seeth.utils.models.status.UserStatusUtility.VisaStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
 * Sample JSON
{
	"active" 			: 1,
	"first_name" 		: "Seethend",
	"last_name" 		: "D",
	"middle_name"		: "Reddy"
	"username" 			: "seeth",
	"password" 			: "seeth",
    "hiredate" 			: "25-Oct-2020",
    "linkedin" 			: "Seethend Reddy",
    "marketing_email_id": "seeth@erwin.com",
    "personal_email_id" : "seeth@gmail.com",
    "phone_number"		: "9553010250",
    "technologies"		: "C~~~Java~~~Python",
    "visa_status"		: "Approved",
	"roles" : [
			{
				"name" : "JOB_APPLICANT"
			}	
	]
}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cmps_user")
public class User {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "user_id")
	private int id;

	@Column(name = "username", nullable = false, length = 250, unique = true)
	private String username;

	@Column(name = "password", nullable = false, length = 250)
	private String password;

	@Column(name = "first_name", nullable = false, length = 250)
	private String firstName;

	@Column(name = "middle_name", nullable = true, length = 250)
	private String middleName;

	@Column(name = "last_name", nullable = true, length = 250)
	private String lastName;

	@Column(name = "avatar", columnDefinition="TEXT NULL") 
    @Lob
	private String avatar;

	@Column(name = "ssn_or_pan", nullable = true, length = 250)
	private String ssnOrPan;

	@Column(name = "personal_email_id", nullable = true, length = 250)
	private String personalEmailId;

	@Column(name = "marketing_email_id", nullable = true, length = 250)
	private String marketingEmailId;
	
	@Column(name = "date_of_birth", nullable = true)
	private Date dob;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = true, length = 100)
	private Gender gender;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "marital_status", nullable = true, length = 100)
	private MaritalStatus maritalStatus;

	@Column(name = "phone_number", nullable = true, length = 10)
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "visa_status", nullable = true, length = 100)
	private VisaStatus visaStatus;

	@Column(name = "linkedin", nullable = true, length = 250)
	private String linkedInURL;

	@Column(name = "hire_date", nullable = true)
	private Date hireDate;

	@Column(name = "active", nullable = false, length = 1)
	private int active;

	@OneToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	@JoinTable(name="cmps_user_roles", 
				joinColumns= @JoinColumn(name="user_id"), 
				inverseJoinColumns = @JoinColumn(name="role_id")
	)
	private Set<Role> roles;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "addr_id")
	private AddressDetail addressDetail;
	
	@Column(name = "create_date", nullable = false)
	private Date createDate;
	
	@Column(name = "update_date", nullable = true)
	private Date updateDate;

	public User(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.middleName = user.getMiddleName();
		this.lastName = user.getLastName();
		this.avatar = user.getAvatar();
		this.personalEmailId = user.getPersonalEmailId();
		this.marketingEmailId = user.getMarketingEmailId();
		this.dob = user.getDob();
		this.gender = user.getGender();
		this.maritalStatus = user.getMaritalStatus();
		this.phoneNumber = user.getPhoneNumber();
		this.visaStatus = user.getVisaStatus();
		this.linkedInURL = user.getLinkedInURL();
		this.hireDate = user.getHireDate();
		this.active = user.getActive();
		this.roles = user.getRoles();
		this.addressDetail = user.getAddressDetail();
		this.createDate = user.getCreateDate();
		this.updateDate = user.getUpdateDate();
	}
	
}
