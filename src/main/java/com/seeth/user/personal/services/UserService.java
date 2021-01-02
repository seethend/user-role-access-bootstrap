package com.seeth.user.personal.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.seeth.address.models.dto.AddressModel;
import com.seeth.exception.models.AppRunTimeException;
import com.seeth.exception.models.AppErrorCode;
import com.seeth.globalsearch.models.EntitySearchModel;
import com.seeth.globalsearch.models.FacetFilter;
import com.seeth.globalsearch.models.GlobalSearchModel;
import com.seeth.globalsearch.models.EntitySearchModel.SearchMeta;
import com.seeth.globalsearch.services.EntitySearchHelperService;
import com.seeth.user.personal.models.Role;
import com.seeth.user.personal.models.User;
import com.seeth.user.personal.models.UserAuthDetail;
import com.seeth.user.personal.models.dto.UserMapper;
import com.seeth.user.personal.models.dto.UserModel;
import com.seeth.user.personal.repositories.RoleRepository;
import com.seeth.user.personal.repositories.UserRepository;
import com.seeth.utils.models.status.UserStatusUtility.UserRole;
import com.tata.address.models.AddressDetail_;
import com.tata.user.personal.models.Role_;
import com.tata.user.personal.models.User_;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService implements UserDetailsService {

	private @Setter @Getter List<String> errors = new ArrayList<>();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EntitySearchModel entitySearchModel;


	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Optional<User> optionalUsers = userRepository.findByUsername(username);
		optionalUsers.orElseThrow(()-> new AppRunTimeException(AppErrorCode.LOGIN_REQUIRED));
		return optionalUsers.map(UserAuthDetail::new).get();
	}

	/**
	 * 
	 * 31-Oct-2020 2:07:27 am
	 *
	 *
	 * @param username
	 * @return
	 * User
	 */
	public User getUserByUsername(String username) {
		User user = null;

		Optional<User> optionalUsers = userRepository.findByUsername(username);

		if(optionalUsers.isPresent()) {
			user = optionalUsers.get();
		}

		return user;
	}

	/**
	 * 
	 * 31-Oct-2020 2:07:31 am
	 *
	 *
	 * @param userId
	 * @return
	 * User
	 */
	public User getUserByUserId(int userId) {
		User user = null;

		Optional<User> optionalUsers = userRepository.findById(userId);
		if(optionalUsers.isPresent()) {
			user = optionalUsers.get();
		}

		return user;
	}

	/**
	 * 
	 * 30-Oct-2020 1:48:59 am
	 *
	 *
	 * @param userId
	 * @return
	 * UserModel
	 */
	public UserModel fetchUser(int userId) {
		UserModel userModelRes = null;

		User user = getUserByUserId(userId);

		if(user != null) {
			userModelRes = UserMapper.toModel(user);
		} else {
			log.error("User not found");
		}

		return userModelRes;
	}

	/**
	 * 02-Nov-2020 5:19:48 pm
	 *
	 *
	 * @param user
	 * @return
	 * User
	 */
	public User saveUserEntity(User user) {
		User savedUser = null;

		try {

			if(user.getCreateDate() == null) {
				user.setCreateDate(new Date(System.currentTimeMillis()));
			} else {
				user.setUpdateDate(new Date(System.currentTimeMillis()));
			}

			savedUser = userRepository.save(user);
		} catch(Exception e) {
			log.error("Failed to save User");
		}

		return savedUser;
	}

	/**
	 * 
	 * 31-Oct-2020 2:07:14 am
	 *
	 *
	 * @param userModelReq
	 * @return
	 * UserModel
	 */
	public UserModel saveUser(User user) {

		UserModel userModelRes = null;

		User savedUser = null;

		try {
			savedUser = saveUserEntity(user);

			userModelRes = UserMapper.toModel(savedUser);

		} catch(Exception e) {
			log.error("Failed to save User");
		}

		return userModelRes;		
	}


	/**
	 * 07-Nov-2020 2:16:11 am
	 *
	 *
	 * @param userModel
	 * @return
	 * UserModel
	 */
	@Transactional
	public UserModel checkAndSaveUser(UserModel userModel, String randomPassword) {
		UserModel userModelRes = null;

		log.debug("User from request body \n" + userModel);

		Set<UserRole> userRoles = userModel.getRoles();

		if(userModel.getAddress() == null) {
			AddressModel addr = new AddressModel();
			addr.setStreet("");
			addr.setCity("");
			addr.setState("");
			addr.setCountry("");
			addr.setZipCode("");
			userModel.setAddress(addr);
		}

		User user = UserMapper.toEntity(userModel);

		user.setPassword(passwordEncoder.encode(randomPassword));

		user.getAddressDetail().setCreateDate(new Date(System.currentTimeMillis()));

		Set<Role> roles = getRolesEntity(userRoles);
		user.setRoles(roles);

		userModelRes = saveUser(user);

		return userModelRes;
	}

	/**
	 * 11-Dec-2020 12:47:54 am
	 *
	 *
	 * @param currUser
	 * @param file
	 * @return
	 * String
	 */
	public String storeUserImage(User currUser, MultipartFile file) {

		String imageUrl = "";

		try {

//			if(currUser.getAvatar() != null || !currUser.getAvatar().trim().isEmpty()) {
//				awss3Client.deleteFileFromS3Bucket(AWSS3Constant.USER_PROFILE, currUser.getAvatar());
//			}
//
//			imageUrl = awss3Client.uploadFile(file, AWSS3Constant.USER_PROFILE);

			currUser.setAvatar(imageUrl);

			User savedUserEntity = saveUserEntity(currUser);

			if(savedUserEntity == null)
				return null;

		} catch(Exception e) {
			log.error("Error --> " + e.getMessage());
			log.error("StackError --> " + Arrays.asList(e.getStackTrace()));
			return null;
		}

		return imageUrl;
	}

	/**
	 * 17-Dec-2020 1:24:41 am
	 *
	 *
	 * @param currUser
	 * @param password
	 * @return
	 * User
	 */
	public User updateCurrentUserPassword(User currUser, String password) {

		currUser.setPassword(passwordEncoder.encode(password));

		return saveUserEntity(currUser);
	}

	/**
	 * 05-Nov-2020 2:19:39 am
	 *
	 *
	 * @param userRoles
	 * @return
	 * Set<Role>
	 */
	public Set<Role> getRolesEntity(Set<UserRole> userRoles) {

		Set<Role> roles = roleRepository.findAllByUserRole(userRoles);

		if(roles == null)
			roles = new HashSet<>();

		return roles;
	}


	/**
	 * 
	 * 30-Oct-2020 12:08:46 am
	 *
	 *
	 * @param validUserModels
	 * @return
	 * List<UserModel>
	 */
	public List<UserModel> saveAllUsers(List<UserModel> userModels) {

		List<UserModel> resUserModels = new ArrayList<>();

		for(UserModel userModelReq : userModels) {

			if(getUserByUsername(userModelReq.getUsername()) == null) {

				UserModel userModelRes = new UserModel();

				User user = UserMapper.toEntity(userModelReq);

				User savedUser = null;

				try {
					savedUser = userRepository.save(user);
					userModelRes = UserMapper.toModel(savedUser);
				} catch(Exception e) {
					log.error("Error while saving user with username " + userModelReq.getUsername() + " \n" + Arrays.asList(e.getStackTrace()));
					getErrors().add(e.getMessage() + " --> while saving user with username " + userModelReq.getUsername());
				}

				resUserModels.add(userModelRes);
			} else {
				getErrors().add("User Already Exists --> while saving user with username " + userModelReq.getUsername());
				resUserModels.add(userModelReq);
			}
		}

		return resUserModels;
	}

	/**
	 * 
	 * 30-Oct-2020 12:09:04 am
	 *
	 *
	 * @param user
	 * @return
	 * boolean
	 */
	@Transactional
	public boolean deleteUser(User user) {

		int userId = user.getId();

		userRepository.delete(user);

		return userRepository.findById(userId).isPresent();
	}

	/**
	 * 05-Nov-2020 3:50:49 am
	 *
	 *
	 * @param existingUser
	 * @param userRoles
	 * @return
	 * User
	 */
	public User addRoles(User existingUser, Set<UserRole> userRoles) {

		UserModel exUserModel = UserMapper.toModel(existingUser);

		userRoles.removeAll(exUserModel.getRoles());

		Set<Role> roles = getRolesEntity(userRoles);

		existingUser.getRoles().addAll(roles);

		return saveUserEntity(existingUser);
	}

	/**
	 * 05-Nov-2020 3:54:46 am
	 *
	 *
	 * @param existingUser
	 * @param userRoles
	 * @return
	 * User
	 */
	public User removeRoles(User existingUser, Set<UserRole> userRoles) {

		Set<Role> roles = getRolesEntity(userRoles);

		existingUser.getRoles().removeAll(roles);

		return saveUserEntity(existingUser);
	}

	/**
	 * 16-Dec-2020 2:59:54 am
	 *
	 *
	 * void
	 */
	public void encodeAllUserPasswords() {

		List<User> allUsers = userRepository.findAll();

		allUsers.forEach(p -> {
			p.setPassword(passwordEncoder.encode(p.getPassword()));
		});

		userRepository.saveAll(allUsers);

	}

	/**
	 * 17-Dec-2020 1:39:04 am
	 * @param globalSearchModel 
	 * @param itemsPerPage 
	 * @param pageNumber 
	 *
	 *
	 * @return
	 * List<UserModel>
	 */
	public void searchUsers(GlobalSearchModel globalSearchModel, int pageNumber, int itemsPerPage) {

		HashMap<String, List<FacetFilter>> facets = globalSearchModel.getFacets();

		Map<String, SearchMeta> userProfileFieldDetails = entitySearchModel.getUserFieldDetails();

		Specification<User> finalSpec = 
				EntitySearchHelperService.getAllSpecifications(facets, userProfileFieldDetails);

		Pageable pageable = EntitySearchHelperService
				.getPageableOptions(globalSearchModel, pageNumber, itemsPerPage, userProfileFieldDetails);

		Page<User> userPages = userRepository.findAll(finalSpec, pageable);

		if(userPages == null)
			return;

		List<User> filteredUsers = userPages.getContent();

		if(filteredUsers == null  || filteredUsers.isEmpty())
			return;

		globalSearchModel.setResults(filteredUsers.stream().map(UserMapper::toModel).collect(Collectors.toList()));
	}

	/**
	 * 29-Dec-2020 2:05:53 am
	 *
	 *
	 * @param globalSearchModel
	 * @param pageNumber
	 * @param itemsPerPage
	 * void
	 * @return 
	 */
	public Map<String, Long> fetchUserStats(UserRole userRole) {

		Map<String, Long> userStats = new TreeMap<>();

		try {
			long totalUser = userRepository.findByRolesUserRole(userRole).size();

			long totalInactiveUser = userRepository.getUsersByActive(userRole, 0);

			long totalActiveUser = userRepository.getUsersByActive(userRole, 1);


			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date sqlDate = new Date(cal.getTime().getTime());

			log.debug("Last Month date " + sqlDate.getTime());

			long newUsers = userRepository.getUsersInLastMonth(userRole, sqlDate);

			userStats.put("totalUser", totalUser);
			userStats.put("newUsers", newUsers);
			userStats.put("totalActiveUser", totalActiveUser);
			userStats.put("totalInactiveUser", totalInactiveUser);

		} catch(Exception e) {
			log.error("Error while fetching stats for user " + e.getMessage());
			log.error("Error stack " + Arrays.asList(e.getStackTrace()));
		}

		return userStats;

	}

	/**
	 * 30-Dec-2020 1:56:55 pm
	 *
	 *
	 * @param userModels
	 * @return
	 * String
	 */
	public String storeAndReturnExcelPath(List<UserModel> userModels) {
		
		String uploadedS3ExcelFile = null;
		FileOutputStream out = null;
		
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet("Consultancy Users");

			int rownum = 0;

			Row headingRow = sheet.createRow(rownum++);
			createHeadingRowColumns(headingRow);
//			setRowStyle(headingRow, workbook);
			
			
			for (UserModel userModel : userModels) {
				Row row = sheet.createRow(rownum++);
				createNextRowColumns(userModel, row);
			}

			File excelFile = new File("consultancy_owners_list.xlsx");
			
			log.debug("Excel local path " + excelFile.getAbsolutePath());
			
			if(excelFile.exists())
				excelFile.delete();
			
			out = new FileOutputStream(excelFile);
			workbook.write(out);
			
//			awss3Client.deleteFileFromS3Bucket(AWSS3Constant.EXCEL_REPORTS, "consultancy_owners_list.xlsx");
			
//			uploadedS3ExcelFile = awss3Client.uploadFile(excelFile, AWSS3Constant.EXCEL_REPORTS, "consultancy_owners_list.xlsx");

		} catch (Exception e) {
			log.error("Error while exporting consultancy users excel " + e.getMessage());
			log.error("Error stack " + Arrays.asList(e.getStackTrace()));
			return null;
		} finally {
			try {
				if(out != null) out.close();
			} catch (IOException e) {
				log.error("Error while closing the resources " + e.getMessage());
				log.error("Error stack " + Arrays.asList(e.getStackTrace()));
			}
		}

		return uploadedS3ExcelFile;
	}

	/**
	 * 30-Dec-2020 4:21:00 pm
	 *
	 *
	 * @param headingRow
	 * void
	 * @param workbook 
	 */
	public void setRowStyle(Row headingRow, XSSFWorkbook workbook) {
		CellStyle style=null;

	    XSSFFont defaultFont = workbook.createFont();
	    defaultFont.setFontHeightInPoints((short)10);
	    defaultFont.setFontName("Arial");
	    defaultFont.setColor(IndexedColors.BLACK.getIndex());
	    defaultFont.setBold(false);
	    defaultFont.setItalic(false);

	    XSSFFont font = workbook.createFont();
	    font.setFontHeightInPoints((short)10);
	    font.setFontName("Arial");
	    font.setColor(IndexedColors.WHITE.getIndex());
	    font.setBold(true);
	    font.setItalic(false);

	    if(headingRow.isFormatted())
	    style = headingRow.getRowStyle();
	    style.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
	    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	    style.setAlignment(CellStyle.ALIGN_CENTER);
	    style.setFont(font);
	}

	/**
	 * 30-Dec-2020 3:58:29 pm
	 *
	 *
	 * @param headingRow
	 * void
	 */
	private void createHeadingRowColumns(Row headingRow) {
		headingRow.createCell(0).setCellValue(User_.ID);
		headingRow.createCell(1).setCellValue(User_.USERNAME);
		headingRow.createCell(2).setCellValue(User_.FIRST_NAME);
		headingRow.createCell(3).setCellValue(User_.MIDDLE_NAME);
		headingRow.createCell(4).setCellValue(User_.LAST_NAME);
		headingRow.createCell(5).setCellValue(User_.AVATAR);
		headingRow.createCell(6).setCellValue(User_.SSN_OR_PAN);
		headingRow.createCell(7).setCellValue(User_.PERSONAL_EMAIL_ID);
		headingRow.createCell(8).setCellValue(User_.MARKETING_EMAIL_ID);
		headingRow.createCell(9).setCellValue(User_.DOB);
		headingRow.createCell(10).setCellValue(User_.GENDER);
		headingRow.createCell(11).setCellValue(User_.MARITAL_STATUS);
		headingRow.createCell(12).setCellValue(User_.PHONE_NUMBER);
		headingRow.createCell(13).setCellValue(User_.VISA_STATUS);
		headingRow.createCell(14).setCellValue(User_.LINKED_IN_UR_L);
		headingRow.createCell(15).setCellValue(User_.HIRE_DATE);
		headingRow.createCell(16).setCellValue(User_.ACTIVE);
		headingRow.createCell(17).setCellValue(Role_.USER_ROLE);
		headingRow.createCell(18).setCellValue(AddressDetail_.ID);
		headingRow.createCell(19).setCellValue(AddressDetail_.STREET);
		headingRow.createCell(20).setCellValue(AddressDetail_.CITY);
		headingRow.createCell(21).setCellValue(AddressDetail_.STATE);
		headingRow.createCell(22).setCellValue(AddressDetail_.COUNTRY);
		headingRow.createCell(23).setCellValue(AddressDetail_.ZIP_CODE);
		headingRow.createCell(24).setCellValue(User_.CREATE_DATE);
		headingRow.createCell(25).setCellValue(User_.UPDATE_DATE);
	}

	/**
	 * 
	 * 30-Dec-2020 4:39:36 pm
	 *
	 *
	 * @param userModel
	 * @param row
	 * void
	 */
	private void createNextRowColumns(UserModel userModel, Row row) { // creating cells for each row
		
		row.createCell(0).setCellValue(userModel.getId());
		row.createCell(1).setCellValue(userModel.getUsername() == null ? "" : userModel.getUsername());
		row.createCell(2).setCellValue(userModel.getFirstName() == null ? "" : userModel.getFirstName());
		row.createCell(3).setCellValue(userModel.getMiddleName() == null ? "" : userModel.getMiddleName());
		row.createCell(4).setCellValue(userModel.getLastName() == null ? "" : userModel.getLastName());
		row.createCell(5).setCellValue(userModel.getAvatar() == null ? "" : userModel.getAvatar());
		row.createCell(6).setCellValue(userModel.getSsnOrPan() == null ? "" : userModel.getSsnOrPan());
		row.createCell(7).setCellValue(userModel.getPersonalEmailId() == null ? "" : userModel.getPersonalEmailId());
		row.createCell(8).setCellValue(userModel.getMarketingEmailId() == null ? "" : userModel.getMarketingEmailId());
		row.createCell(9).setCellValue(userModel.getDob() == null ? "" : userModel.getDob().toString());
		row.createCell(10).setCellValue(userModel.getGender() == null ? "" : userModel.getGender().name());
		row.createCell(11).setCellValue(userModel.getMaritalStatus() == null ? "" : userModel.getMaritalStatus().name());
		row.createCell(12).setCellValue(userModel.getPhoneNumber() == null ? "" : userModel.getPhoneNumber());
		row.createCell(13).setCellValue(userModel.getVisaStatus() == null ? "" : userModel.getVisaStatus().name());
		row.createCell(14).setCellValue(userModel.getLinkedInURL() == null ? "" : userModel.getLinkedInURL());
		row.createCell(15).setCellValue(userModel.getHireDate() == null ? "" : userModel.getHireDate().toString());
		row.createCell(16).setCellValue(userModel.getActive());
		
		try {
			String roles = String.join(",", userModel.getRoles().stream().map(p -> p.name()).collect(Collectors.toList()));
			row.createCell(17).setCellValue(roles == null ? "" : roles);
			
			if(userModel.getAddress() != null) {
				row.createCell(18).setCellValue(userModel.getAddress().getId());
				row.createCell(19).setCellValue(userModel.getAddress().getStreet() == null ? "" : userModel.getAddress().getStreet());
				row.createCell(20).setCellValue(userModel.getAddress().getState() == null ? "" : userModel.getAddress().getState());
				row.createCell(21).setCellValue(userModel.getAddress().getCity() == null ? "" : userModel.getAddress().getCity());
				row.createCell(22).setCellValue(userModel.getAddress().getCountry() == null ? "" : userModel.getAddress().getCountry());
				row.createCell(23).setCellValue(userModel.getAddress().getZipCode() == null ? "" : userModel.getAddress().getZipCode());
			}
		} catch(Exception e) {
			log.error("Error while tranforming roles and address to excel cells");
		}

		row.createCell(24).setCellValue(userModel.getCreateDate() == null ? "" : userModel.getCreateDate().toString());
		row.createCell(25).setCellValue(userModel.getUpdateDate() == null ? "" : userModel.getUpdateDate().toString());
	}
}

