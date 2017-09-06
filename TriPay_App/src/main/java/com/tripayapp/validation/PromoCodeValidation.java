package com.tripayapp.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.tripayapp.model.PromoCodeDTO;
import com.tripayapp.model.ServiceType;
import com.tripayapp.model.error.PromoCodeError;

public class PromoCodeValidation {

	public PromoCodeError checkError(PromoCodeDTO code) {
		PromoCodeError error = new PromoCodeError();
		boolean valid = true;






		error.setValid(valid);
		return error;
	}
}
