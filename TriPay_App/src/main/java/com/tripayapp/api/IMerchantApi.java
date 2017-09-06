package com.tripayapp.api;

import java.util.List;

import com.tripayapp.model.MRegisterDTO;
import com.tripayapp.model.MerchantDTO;
import com.tripayapp.model.RegisterDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.mobile.ResponseDTO;

public interface IMerchantApi {
	
	ResponseDTO addMerchant(MRegisterDTO user);
	
	List<MerchantDTO> getAll(String type);

}
