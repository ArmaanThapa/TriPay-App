package com.tripayapp.validation;

import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.User;
import com.tripayapp.model.ForgotMpinDTO;
import com.tripayapp.model.MpinChangeDTO;
import com.tripayapp.model.MpinDTO;
import com.tripayapp.model.error.ChangeMpinError;
import com.tripayapp.model.error.ForgotMpinError;
import com.tripayapp.model.error.MpinError;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;

public class MpinValidation {

	private final IUserApi userApi;
	private final PasswordEncoder passwordEncoder;
	public MpinValidation(IUserApi userApi,PasswordEncoder passwordEncoder) {
		this.userApi = userApi;
		this.passwordEncoder = passwordEncoder;
	}

	public MpinError validateNewMpin(MpinDTO dto) {
		MpinError error = new MpinError();
		boolean valid = true;
//		if (CommonValidation.isNull(dto.getNewMpin())) {
//			error.setNewMpin("MPIN must not be Empty");
//			valid = false;
//		} else if (CommonValidation.isNull(dto.getConfirmMpin())) {
//			error.setConfirmMpin("Please Re-Enter your MPIN");
//			valid = false;
//		} else if (!(dto.getNewMpin().equals(dto.getConfirmMpin()))) {
//			error.setConfirmMpin("Both MPIN must be equal");
//			valid = false;
//		} else if (!(CommonValidation.isNumeric(dto.getNewMpin()))) {
//			error.setNewMpin("MPIN must be numeric");
//			valid = false;
//		} else if (!(CommonValidation.checkValidMpin(dto.getNewMpin()))) {
//			error.setNewMpin("MPIN must be 6 characters long");
//			valid = false;
//		}

		error.setValid(valid);
		return error;
	}

	public ChangeMpinError validateChangeMpin(MpinChangeDTO dto) {
		ChangeMpinError error = new ChangeMpinError();
		System.out.print("Inside MPIN validation API");
		boolean valid = true;
//		if (CommonValidation.isNull(dto.getOldMpin())) {
//			error.setOldMpin("Please enter your current MPIN");
//			valid = false;
//		} else if (CommonValidation.isNull(dto.getNewMpin())) {
//			error.setNewMpin("enter your new MPIN");
//			valid = false;
//		} else if (CommonValidation.isNull(dto.getConfirmMpin())) {
//			error.setConfirmMpin("Re Enter your new MPIN");
//			valid = false;
//		} else if (!CommonValidation.isNumeric(dto.getNewMpin())) {
//			error.setNewMpin("MPIN must be in numeric form");
//			valid = false;
//		} else if (!CommonValidation.checkValidMpin(dto.getNewMpin())) {
//			error.setNewMpin("MPIN but be 6 digits long");
//			valid = false;
//		} else if (!(dto.getNewMpin().equals(dto.getConfirmMpin()))) {
//			error.setConfirmMpin("Both MPIN must be equal");
//			valid = false;
//		}
		error.setValid(valid);
		return error;

	}

	public ForgotMpinError checkError(ForgotMpinDTO dto,User u){
		ForgotMpinError error = new ForgotMpinError();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		boolean valid = true;
		String currentPassword = u.getPassword();

		String currentDateOfBirth = dateFormat.format(u.getUserDetail().getDateOfBirth());
		if(!(passwordEncoder.matches(dto.getPassword(),currentPassword))){
			valid = false;
			error.setPassword("Password doesn't match");
		}

		if(!(dto.getDateOfBirth().equals(currentDateOfBirth))){
			valid = false;
			error.setDateOfBirth("Date Of Birth doesn't match");
		}
		error.setValid(valid);
		return error;
	}
}
