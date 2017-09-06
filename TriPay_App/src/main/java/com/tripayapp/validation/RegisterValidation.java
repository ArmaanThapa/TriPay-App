package com.tripayapp.validation;

import java.util.List;

import com.tripayapp.entity.LocationDetails;
import com.tripayapp.repositories.LocationDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tripayapp.entity.User;
import com.tripayapp.entity.UserDetail;
import com.tripayapp.model.ChangePasswordDTO;
import com.tripayapp.model.RegisterDTO;
import com.tripayapp.model.Status;
import com.tripayapp.model.VerifyEmailDTO;
import com.tripayapp.model.error.ChangePasswordError;
import com.tripayapp.model.error.ForgotPasswordError;
import com.tripayapp.model.error.RegisterError;
import com.tripayapp.model.error.VerifyEmailError;
import com.tripayapp.repositories.UserDetailRepository;
import com.tripayapp.repositories.UserRepository;

public class RegisterValidation {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserDetailRepository userDetailRepository;
	private final LocationDetailsRepository locationDetailsRepository;

	public RegisterValidation(UserRepository UserRepository, PasswordEncoder passwordEncoder,
			UserDetailRepository userDetailRepository,LocationDetailsRepository locationDetailsRepository) {
		this.userRepository = UserRepository;
		this.passwordEncoder = passwordEncoder;
		this.userDetailRepository = userDetailRepository;
		this.locationDetailsRepository = locationDetailsRepository;
	}

	public RegisterError validateNormalUser(RegisterDTO dto) {
		RegisterError error = new RegisterError();
		boolean valid = true;
		if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
			error.setUsername("Username Required");
			valid = false;
		} else {
			User user = userRepository.findByUsernameAndStatus((dto.getUsername().toLowerCase()), Status.Active);
			if (user != null) {
				error.setUsername("Username Already Exist");
				error.setContactNo("User Already Exist");
				valid = false;
			}
		}

		if(CommonValidation.isNull(dto.getLocationCode())) {
			valid = false;
			error.setLocationCode("Pincode Required");
		}else {
			LocationDetails locationDetails = locationDetailsRepository.findLocationByPin(dto.getLocationCode());
			if(locationDetails == null) {
				valid = false;
				error.setLocationCode("Not a valid Pincode");
			}
		}

//		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
//			error.setPassword("Password Required");
//			valid = false;
//		}
//
//
//		if (dto.getConfirmPassword() == null || dto.getConfirmPassword().isEmpty()) {
//			error.setConfirmPassword("Password Required");
//			valid = false;
//		} else {
//			if (!dto.getPassword().equals(dto.getConfirmPassword())) {
//				error.setConfirmPassword("Password Mis-Match");
//				valid = false;
//			}
//		}

//		if (dto.getContactNo() == null || dto.getContactNo().isEmpty()) {
//			error.setContactNo("Contact No Required");
//			valid = false;
//		} else if (CommonValidation.isNumeric(dto.getContactNo())) {
//			if (!CommonValidation.checkLength10(dto.getContactNo())) {
//				error.setContactNo("Mobile number must be 10 digits long");
//				valid = false;
//			}
//		} else {
//			error.setContactNo("Please enter valid mobile number");
//			valid = false;
//		}

//		if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
//			error.setFirstName("First Name Required");
//			valid = false;
//		}
//
//		if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
//			error.setLastName("Last Name Required");
//			valid = false;
//		}
//
//		if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
//			error.setEmail("Email Required");
//			valid = false;
//		} else if (!(CommonValidation.isValidMail(dto.getEmail()))) {
//			error.setEmail("Please enter valid mail address");
//			valid = false;
//		} else {

			if(!CommonValidation.isNull(dto.getEmail())) {
				List<UserDetail> userDetail = userDetailRepository.checkMail(dto.getEmail());
				if (userDetail != null) {
					for (UserDetail ud : userDetail) {
						User user = userRepository.findByUsernameAndMobileStatusAndEmailStatus(ud.getContactNo(),
								Status.Active, Status.Active);
						if (user != null) {
							valid = false;
							error.setEmail("Email already exists");
						}
					}
//			}
				}
			}


		error.setValid(valid);
		return error;
	}

	public RegisterError validateEditUser(RegisterDTO dto) {
		RegisterError error = new RegisterError();
		boolean valid = true;

//		if (dto.getAddress() == null || dto.getAddress().isEmpty()) {
//			error.setAddress("Address Required");
//			valid = false;
//		}
//
//		if (dto.getFirstName() == null || dto.getFirstName().isEmpty()) {
//			error.setFirstName("First Name Required");
//			valid = false;
//		}
//
//		if (dto.getLastName() == null || dto.getLastName().isEmpty()) {
//			error.setLastName("Last Name Required");
//			valid = false;
//		}
//
//		if (dto.getEmail() == null) {
//			error.setLastName("Please Enter Valid Email ID");
//			valid = false;
//		}

		error.setValid(valid);

		return error;

	}

	public ChangePasswordError validateChangePassword(ChangePasswordDTO dto) {
		ChangePasswordError error = new ChangePasswordError();
		boolean valid = true;

//		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
//			error.setPassword("Insert Your New Password");
//			valid = false;
//		}
//
//		if (dto.getConfirmPassword() == null || dto.getConfirmPassword().isEmpty()) {
//			error.setConfirmPassword("Please Re-Type Your Password");
//			valid = false;
//		}
//
//		if (dto.getPassword() != null && dto.getConfirmPassword() != null) {
//			if (!dto.getPassword().equals(dto.getConfirmPassword())) {
//				error.setConfirmPassword("Password Mis-match");
//				valid = false;
//			}
//			if (CommonValidation.checkLength6(dto.getNewPassword())) {
//				error.setNewPassword("Password must be atleast 6 characters long");
//				valid = false;
//			}
//		}

		error.setValid(valid);
		return error;
	}

	public ChangePasswordError validateChangePasswordDTO(ChangePasswordDTO dto) {
		ChangePasswordError error = new ChangePasswordError();
		System.err.println("--1--");
		boolean valid = true;
			User u = userRepository.findByUsername(dto.getUsername());
			if (u != null) {
				System.err.println("--2--");
				if (!(passwordEncoder.matches(dto.getPassword(), u.getPassword()))) {
					System.err.println("--3--");
					error.setPassword("enter your current password correctly");
					valid = false;
				}
			}
		error.setValid(valid);
		return error;
	}

	public ChangePasswordError renewPasswordValidation(ChangePasswordDTO dto) {
		ChangePasswordError error = new ChangePasswordError();
		boolean valid = true;
		User user = userRepository.findByMobileTokenAndUsername(dto.getKey(), dto.getUsername());
		if (user == null) {
			valid = false;
			error.setKey("Invalid Key");
			error.setUsername("Invalid user");
		}

		if (!(dto.getConfirmPassword().equals(dto.getNewPassword()))) {
			valid = false;
			error.setConfirmPassword("Password Mis-Match");
		}
		error.setValid(valid);
		return error;
	}

	public ForgotPasswordError forgotPassword(String username) {
		ForgotPasswordError error = new ForgotPasswordError();
		if (!CommonValidation.checkLength10(username)) {
			error.setErrorLength("Mobile number must be 10 digits long");
			error.setValid(false);
			return error;
		}
		error.setValid(true);
		return error;

	}

	public VerifyEmailError checkMailError(VerifyEmailDTO email) {
		VerifyEmailError error = new VerifyEmailError();
		boolean valid = true;
		if (CommonValidation.isNull(email.getKey())) {
			error.setEmail("Please enter email in the field");
			valid = false;
		}
		error.setValid(valid);
		return error;
	}
	
	

}