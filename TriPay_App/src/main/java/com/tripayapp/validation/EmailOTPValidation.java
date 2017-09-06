package com.tripayapp.validation;

import java.util.List;

import com.tripayapp.entity.User;
import com.tripayapp.entity.UserDetail;
import com.tripayapp.model.Status;
import com.tripayapp.model.error.VerifyEmailOTPError;
import com.tripayapp.repositories.UserDetailRepository;
import com.tripayapp.repositories.UserRepository;
import com.tripayapp.util.LogCat;

public class EmailOTPValidation {

	private final UserRepository userRepository;
	private final UserDetailRepository userDetailRepository;

	public EmailOTPValidation(UserRepository userRepository,
			UserDetailRepository userDetailRepository) {
		this.userRepository = userRepository;
		this.userDetailRepository = userDetailRepository;
	}

	public VerifyEmailOTPError checkEmailToken(String key) {
		VerifyEmailOTPError error = new VerifyEmailOTPError();
		boolean valid = true;
		
		/**
		 * Checking Active Email exist or not for the given token
		 */
		User user = userRepository.findByEmailToken(key);
		UserDetail userDetail = user.getUserDetail();
		List<UserDetail> userDetails = userDetailRepository.checkMail(userDetail.getEmail());
		if (userDetails != null) {
			for (UserDetail ud : userDetails) {
				User u = userRepository.findByUserDetails(ud);
				if (u.getEmailStatus() == Status.Active) {
					valid = false;
					error.setMessage("Verified Email already exist. Please try using another email.");		
				}
			}
		}
		
		/**
		 * Checking user with Inactive email but with active mobile number 
		 */
		user = userRepository.findByEmailTokenAndEmailStatusAndMobileStatus(key,
				Status.Inactive, Status.Active);
		if (user == null) {
			valid = false;
			error.setMessage("Email Verification failed.");
		} 
		
		error.setValid(valid);
		return error;
	}
}
