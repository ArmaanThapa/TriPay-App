package com.tripayapp.validation;

import com.tripayapp.model.InviteFriendsDTO;
import com.tripayapp.model.error.InviteFriendsError;
import com.tripayapp.util.LogCat;

public class InviteFriendValidation {

	public InviteFriendsError checkErrorByEmail(InviteFriendsDTO dto) {
		InviteFriendsError error = new InviteFriendsError();
		boolean valid = true;
//		if (CommonValidation.isNull(dto.getEmail())) {
//			error.setEmail("please enter email address");
//			valid = false;
//		}
//		if (CommonValidation.isNull(dto.getReceiversName())) {
//			error.setEmail("please enter name of your friend");
//			valid = false;
//		}
//		if (CommonValidation.isNull(dto.getMessage())) {
//			error.setEmail("please enter message");
//			valid = false;
//		}
//
//		if (!CommonValidation.isValidMail(dto.getEmail())) {
//			error.setEmail("enter valid mail address");
//			valid = false;
//		}
//
//		if (!CommonValidation.containsAlphabets(dto.getReceiversName())) {
//			error.setReceiversName("Name contains only alphabets");
//			valid = false;
//		}

		error.setValid(valid);
		return error;
	}

	public InviteFriendsError checkErrorByMobile(InviteFriendsDTO dto) {
		InviteFriendsError error = new InviteFriendsError();
		boolean valid = true;
//		if (CommonValidation.isNull(dto.getMobileNo())) {
//			error.setMobileNo("please enter email address");
//			valid = false;
//		}
//		if (CommonValidation.isNull(dto.getReceiversName())) {
//			error.setReceiversName("please enter name of your friend");
//			valid = false;
//		}
//
//		if (CommonValidation.isNull(dto.getMessage())) {
//			error.setMessage("please enter message");
//			valid = false;
//		}
//
//		if (!CommonValidation.checkLength10(dto.getMobileNo())) {
//			error.setMobileNo("Please enter 10 digit mobile number");
//			valid = false;
//		}
//
//		if (!CommonValidation.isNumeric(dto.getMobileNo())) {
//			error.setMobileNo("Mobile No. contains only digits");
//			valid = false;
//		}
//
//		if (!CommonValidation.containsAlphabets(dto.getReceiversName())) {
//			error.setReceiversName("Name contains only alphabets");
//			valid = false;
//		}
	
		error.setValid(valid);
		return error;
	}

}
