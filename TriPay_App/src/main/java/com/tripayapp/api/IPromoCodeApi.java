package com.tripayapp.api;

import java.util.List;

import com.tripayapp.entity.PromoCode;
import com.tripayapp.entity.RedeemCode;
import com.tripayapp.entity.User;
import com.tripayapp.model.PromoCodeDTO;
import com.tripayapp.model.PromoTransactionDTO;
import com.tripayapp.model.RedeemDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.mobile.ResponseDTO;

public interface IPromoCodeApi {

	void addPromocode(PromoCodeDTO request);

	List<PromoCodeDTO> getAll();

	void savePromotionCode(PromoCodeDTO promoDTO);

	List<PromoCodeDTO> getpromotionCode();

	boolean isValidRegistrationDate(PromoCode code,User user);

	void updatePromotionCode(PromoCodeDTO promoDTOs);

	void deletePromotionCode(Long id);

	PromoCodeDTO findPromotionCodeById(Long id);
	
	PromoCode checkPromoCodeValid(String code);

	double calculateNetAmountOfCoupon(PromoCode code,double amount);

	boolean checkPromoCodeLength(String code);
	
	boolean checkPromoCodeExpireDate(String code);
	
	boolean balanceCheck(double amount);
	
	boolean checkActivePromoCode(String code);
	
	boolean redeemCode(User userId , PromoCode code, double amount);
	
	PromoTransactionDTO findTransactionDateByService(UserDTO user, PromoCode code);
	
	boolean checkTransaction(PromoTransactionDTO transDetail, PromoCode code);

	ResponseDTO process(RedeemDTO dto, User user);

	ResponseDTO processInviteFriend(PromoCode code, User u);

	ResponseDTO processRegistration(PromoCode code,User u);

	ResponseDTO processRegistrationServices(PromoCode code,User u);

	ResponseDTO processServices(PromoCode code,User u);

}
