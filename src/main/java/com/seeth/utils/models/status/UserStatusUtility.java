/**
 * 
 */
package com.seeth.utils.models.status;

/**
 * @author SeethendReddy
 *
 */
public class UserStatusUtility {
	
	public enum UserRole {
		APP_OWNER, CONSULTANCY_OWNER, ADMIN, RECRUITER, JOB_APPLICANT
	}
	
	public enum Gender {
		MALE, FEMALE, NOT_IDENTIFIED
	}
	
	public enum MaritalStatus {
		SINGLE, MARRIED, DIVORCED
	}
	
	public enum EmploymentType {
		FULL_TIME, PART_TIME, CONTRACT, SELF_EMPLOYEED, FREELANCE, INTERNSHIP, TRAINEE, OTHER
	}
	
	public enum AchievementType {
		PUBLICATION, PATENT, COURSE, AWARD, OTHER
	}
	
	public enum DocumentType {
		ACHIEVEMENT, RESUME, CV, OTHER
	}
	
	public enum VisaStatus {
		APPROVED, PROCESSING, REJECTED
	}
}
