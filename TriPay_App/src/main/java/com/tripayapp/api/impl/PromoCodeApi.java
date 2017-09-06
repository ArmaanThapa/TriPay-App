package com.tripayapp.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.*;
import com.tripayapp.validation.CommonValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import com.tripayapp.api.IPromoCodeApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.util.ConvertUtil;
import com.tripayapp.util.PromoCodeByService;

public class PromoCodeApi implements IPromoCodeApi, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PromoCodeRepository promoCodeRepository;
	private MessageSource messageSource;
	private IUserApi userApi;
	private UserRepository userRepository;
	private RedeemCodeRepository redeemCodeRepository;
	private PQAccountDetailRepository pqAccountDetailRepository;
	private final ITransactionApi transactionApi;
	private final PQServiceRepository pqServiceRepository;
	private final PromoServicesRepository promoServicesRepository;
	private final InviteLogRepository inviteLogRepository;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public PromoCodeApi(PromoCodeRepository promoCodeRepository, IUserApi userApi, UserRepository userRepository,
			RedeemCodeRepository redeemCodeRepository, PQAccountDetailRepository pqAccountDetailRepository,
			ITransactionApi transactionApi, PQServiceRepository pqServiceRepository,PromoServicesRepository promoServicesRepository,InviteLogRepository inviteLogRepository) {
		this.promoCodeRepository = promoCodeRepository;
		this.userApi = userApi;
		this.userRepository = userRepository;
		this.redeemCodeRepository = redeemCodeRepository;
		this.pqAccountDetailRepository = pqAccountDetailRepository;
		this.transactionApi = transactionApi;
		this.pqServiceRepository = pqServiceRepository;
		this.promoServicesRepository = promoServicesRepository;
		this.inviteLogRepository = inviteLogRepository;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public void addPromocode(PromoCodeDTO request) {
		PromoCode savedCode = promoCodeRepository.save(ConvertUtil.convertPromotionCodeDTO(request));
		ArrayList<String> services = request.getServices();

		if(services != null && !services.isEmpty()){
				for(String s: services){
					PQService service = pqServiceRepository.findServiceByCode(s);
					if(service != null){
						PromoServices promoServices = promoServicesRepository.getByPromoAndService(savedCode,service);
						if(promoServices == null) {
							promoServices = new PromoServices();
							promoServices.setPromoCode(savedCode);
							promoServices.setService(service);
							promoServicesRepository.save(promoServices);
						}
					}
				}
		}
	}

	@Override
	public List<PromoCodeDTO> getAll() {
		return ConvertUtil.convertPromotionCodeList((List<PromoCode>) promoCodeRepository.findAll());
	}

	@Override
	public void savePromotionCode(PromoCodeDTO promoDTO) {
		promoCodeRepository.save(ConvertUtil.convertPromotionCodeDTO(promoDTO));
	}

	@Override
	public List<PromoCodeDTO> getpromotionCode() {
		return ConvertUtil.convertPromotionCodeList((List<PromoCode>) promoCodeRepository.findAll());
	}

    @Override
    public boolean isValidRegistrationDate(PromoCode code, User user) {
        Date codeStartDate = code.getStartDate();
        Date codeEndDate = code.getEndDate();
        Date regDate = user.getCreated();
        if(regDate.after(codeStartDate) && regDate.before(codeEndDate)){
            return true;
        }
        return false;
    }

    @Override
	public void updatePromotionCode(PromoCodeDTO promoDTOs) {
		PromoCode promoCode = promoCodeRepository.findOne(promoDTOs.getPromoCodeId());
		try {
			promoCode.setPromoCode(promoDTOs.getPromoCode());

			promoCode.setStartDate(sdf.parse(promoDTOs.getStartDate()));
			promoCode.setEndDate(sdf.parse(promoDTOs.getEndDate()));
			promoCode.setTerms(promoDTOs.getTerms());
			promoCode.setValue(promoDTOs.getValue());
			if (promoCode != null) {
				promoCodeRepository.save(promoCode);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

	@Override
	public void deletePromotionCode(Long id) {
		PromoCode promoCode = promoCodeRepository.findOne(id);
		if (promoCode != null) {
			promoCodeRepository.delete(promoCode);
		}
	}

	@Override
	public PromoCodeDTO findPromotionCodeById(Long id) {
		PromoCode promoCode = promoCodeRepository.findOne(id);
		PromoCodeDTO promoCodeDTO = ConvertUtil.convertPromotionCode(promoCode);
		return promoCodeDTO;
	}

	@Override
	public PromoCode checkPromoCodeValid(String code) {
		PromoCode vaildCode = promoCodeRepository.findByPromoCode(code);
		if (vaildCode != null) {
			if (vaildCode.getStatus().equals(Status.Active))
			    return vaildCode;
		}
		return null;
	}

	@Override
	public double calculateNetAmountOfCoupon(PromoCode code, double amount) {
		double value = 0.0;
		if(code != null){
				value = code.getValue();
			if(code.isFixed()){
				return value;
			}else{
				double temp = (value * amount) / 100;
				String twoPlaceFloat = String.format("%.2f",temp);
				value =  Double.parseDouble(twoPlaceFloat) ;
			}
		}
		return value;
	}

	@Override
	public boolean checkPromoCodeLength(String code) {
		boolean valid = false;
		String temp = code.trim();
		if (temp.length() >= 6 && temp.length() <= 10) {
			valid = true;
		}
		return valid;
	}

	@Override
	public boolean checkPromoCodeExpireDate(String code) {
		boolean valid = true;
		PromoCode vaildCode = promoCodeRepository.findByPromoCode(code);
		try {
			Date startDate = vaildCode.getStartDate();
			Date endDate = vaildCode.getEndDate();

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			Date currentDate = sdf.parse(sdf.format(calendar.getTime()));

			logger.info("Start Date " + startDate);
			logger.info("End Date " + endDate);
			logger.info("Current Date " + currentDate);

			if (currentDate.before(startDate) || currentDate.after(endDate)) {
					logger.info("PromoCode Not Usable");
					valid = false;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return valid;
	}

	@Override
	public boolean balanceCheck(double amount) {
		User promoCodeAccount = userRepository.findByUsername("promoCode@gmail.com");
		if (promoCodeAccount != null) {
			PQAccountDetail account = promoCodeAccount.getAccountDetail();
			if (account.getBalance() >= amount) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean redeemCode(User user, PromoCode code, double amount) {
		boolean valid = false;
		RedeemCode promoCodeUsedUser = redeemCodeRepository.findByPromoCodeAndUser(user, code);
		if (promoCodeUsedUser == null) {
			double codeAmount = calculateNetAmountOfCoupon(code,amount);
            PQService service = pqServiceRepository.findServiceByOperatorCode("PPS");
			double userCurrentBalance = user.getAccountDetail().getBalance();
			double newBalance = userCurrentBalance + codeAmount;
			String transactionRefNo = System.currentTimeMillis() + "";
			User preFundedUser = userRepository.findByUsername("promoCode@gmail.com");
			double remainPreFundedBalance = preFundedUser.getAccountDetail().getBalance();
			double newRemainPreFundedBalance = remainPreFundedBalance - codeAmount;
			PQAccountDetail userAccount = user.getAccountDetail();
			PQAccountDetail promoAccount = preFundedUser.getAccountDetail();
			promoAccount.setBalance(newRemainPreFundedBalance);
			pqAccountDetailRepository.save(promoAccount);
			transactionApi.initiatePromoCode(codeAmount,
					"Redeem Promo Code " + code.getPromoCode() + " of Rs. " + codeAmount, transactionRefNo,
					service, code, user.getUsername(), preFundedUser.getUsername());
			userAccount.setBalance(newBalance);
			pqAccountDetailRepository.save(userAccount);
			transactionApi.successPromoCode(transactionRefNo);
			RedeemCode redeemUser = new RedeemCode();
			redeemUser.setUser(user);
			redeemUser.setPromoCode(code);
			redeemCodeRepository.save(redeemUser);
			valid = true;
		}
		return valid;
	}

	@Override
	public PromoTransactionDTO findTransactionDateByService(UserDTO user, PromoCode code) {
		User active = userApi.findByUserName(user.getUsername());
        System.err.print("user account id is "+active.getAccountDetail().getId());
		PromoTransactionDTO dto = new PromoTransactionDTO();
		List<PromoServices> promoServices = promoServicesRepository.getByPromoCode(code);
		try {
			Date from = code.getStartDate();
			Date to = code.getEndDate();
			double amount = Double.parseDouble(code.getTerms());
            System.err.println("start Date::"+from);
            System.err.println("end Date::"+to);
			List<PQTransaction> list = transactionApi.findTransactionByAccountAndAmount(from, to, active.getAccountDetail(), amount);
			System.err.println("list in promo api is "+list);
			dto = ConvertUtil.convertPromoTransaction(list, code,promoServices);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public boolean checkTransaction(PromoTransactionDTO transDetail, PromoCode code) {
		boolean valid = true;
			logger.info("Transaction Service: " + transDetail.getServiceCode());
			logger.info("Transaction Amount: " + transDetail.getAmount());
			logger.info("Transaction Date: " + transDetail.getTransactionDate());
			PromoCode vaildCode = promoCodeRepository.findByPromoCode(code.getPromoCode());
			try {
				Date startDate = vaildCode.getStartDate();
				Date endDate = vaildCode.getEndDate();
				Date currentDate = sdf.parse(transDetail.getTransactionDate());
				logger.info("Start Date " + startDate);
				logger.info("End Date " + endDate);
				logger.info("Current Date " + currentDate);
				if (currentDate.before(startDate) || currentDate.after(endDate)) {
					logger.info("PromoCode Not Usable");
					valid = false;
				}
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
//		}
		return valid;
	}

   /*

   							PromoCode isValidCode = promoCodeApi.checkPromoCodeValid(dto.getPromoCode());
							if (isValidCode != null) {
								System.err.println("this is a valid code");
								PromoTransactionDTO transDetail = promoCodeApi.findTransactionDateByService(user, isValidCode);
								System.err.println("transDetail :: " + transDetail);
								if (transDetail != null) {
									System.err.println("contains valid transaction details");
									boolean isTransactionValid = promoCodeApi.checkTransaction(transDetail,
											isValidCode);
									if (isTransactionValid) {
										System.err.println("contains valid transaction ");
										boolean isValidDate = promoCodeApi
												.checkPromoCodeExpireDate((dto.getPromoCode()));
										if (isValidDate) {
											System.err.println("contains valid expiry details");
											boolean isvalidBalance = promoCodeApi.balanceCheck(isValidCode.getValue());
											if (isvalidBalance) {
												System.err.println("contains valid balance...");
												boolean isSuccess = promoCodeApi.redeemCode(userSession.getUser(),
														isValidCode, transDetail);
												if (isSuccess) {
													result.setMessage("Balance Credited");
													result.setStatus(ResponseStatus.SUCCESS);
													result.setDetails(null);
													User userOne = userApi.findByUserName(user.getUsername());
													result.setBalance(u.getAccountDetail().getBalance());
													return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
												}
												result.setMessage("Promo Code is applicable for one per User.");
												result.setStatus(ResponseStatus.FAILURE);
												result.setDetails(null);
												return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
											}
											result.setMessage("Promo Code is not available at this time. Please try after some time");
											result.setStatus(ResponseStatus.FAILURE);
											result.setDetails(null);
											return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
										}
										result.setMessage("Promo Code Date Expired");
										result.setStatus(ResponseStatus.FAILURE);
										result.setDetails(null);
										return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
									}
									result.setMessage("No Transaction Found For this Promo Code");
									result.setStatus(ResponseStatus.FAILURE);
									result.setDetails(null);
									return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
								}
								result.setMessage("No Transaction Available For this Promo Code");
								result.setStatus(ResponseStatus.FAILURE);
								result.setDetails(null);
								return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
							}
							result.setMessage("Please Enter a Valid Promo Code");
							result.setStatus(ResponseStatus.FAILURE);
							result.setDetails(null);
							return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

    */

    @Override
    public ResponseDTO process(RedeemDTO dto, User user) {
        ResponseDTO result = new ResponseDTO();
		String promoCode = dto.getPromoCode();
		PromoCode code = checkPromoCodeValid(promoCode);
		if(code != null){
			boolean isValidCode = checkPromoCodeExpireDate(promoCode);
			if(isValidCode){
				RedeemCode exists = redeemCodeRepository.findByPromoCodeAndUser(user,code);
				if(exists == null) {
					boolean containsValidBalance = balanceCheck(code.getValue());
					if (containsValidBalance) {
						double creditTotalMonthly = transactionApi.getMonthlyCreditTransationTotalAmount(user.getAccountDetail());
						double monthlyLimit = user.getAccountDetail().getAccountType().getMonthlyLimit();
						if(CommonValidation.monthlyCreditLimitCheck(monthlyLimit,creditTotalMonthly,code.getValue())) {
							List<PromoServices> promoServices = promoServicesRepository.getByPromoCode(code);
							List<String> serviceCodes = ConvertUtil.getServiceCodeFromPromoServices(promoServices);
							int length = serviceCodes.size();
							if (!serviceCodes.isEmpty() && length > 0) {
								if (serviceCodes.contains("IVP")) {
									result = processInviteFriend(code, user);
								} else if (serviceCodes.contains("RVP")) {
									if (length == 1) {
										result = processRegistration(code, user);
									} else {
										result = processRegistrationServices(code, user);
									}
								} else {
									result = processServices(code, user);
								}
							} else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("No Services Assigned for this Promo Code");
							}
						} else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("Monthly Credit Limit Exceeded, You can't redeem code");
						}
					} else {
						result.setStatus(ResponseStatus.FAILURE);
						result.setMessage("Promo Code not avaliable this time, Please try again later");
					}
				}else {
					result.setStatus(ResponseStatus.FAILURE);
					result.setMessage("Promo Code is applicable once per user");
				}
			}else {
				result.setStatus(ResponseStatus.FAILURE);
				result.setMessage("Promo Code Date Expired");
			}
		}else {
			result.setStatus(ResponseStatus.FAILURE);
			result.setMessage("Please enter a valid promo code");
		}
		return result;
    }

    @Override
    public ResponseDTO processInviteFriend(PromoCode code, User u) {
       	ResponseDTO result = new ResponseDTO();
		long count = inviteLogRepository.countInvitedFriends(u,code.getStartDate(),code.getEndDate());
		long terms =  Long.parseLong(code.getTerms());
		if(count >= terms) {
			boolean isRedeemed = redeemCode(u,code,count);
			if(isRedeemed){
				result.setStatus(ResponseStatus.SUCCESS);
				result.setMessage("Balance Credited");
			}else {
				result.setStatus(ResponseStatus.FAILURE);
				result.setMessage("Problem Occurred, Try Again Later");
			}
		}else {
			result.setStatus(ResponseStatus.FAILURE);
			result.setMessage("Your Invite Count is "+count+", You must do at least "+terms);
		}

        return result;
    }

    @Override
    public ResponseDTO processRegistration(PromoCode code, User u) {
        ResponseDTO result = new ResponseDTO();
		if(isValidRegistrationDate(code,u)){
			boolean isRedeemed = redeemCode(u,code,0.0);
			if(isRedeemed){
				result.setStatus(ResponseStatus.SUCCESS);
				result.setMessage("Balance Credited");
			}else {
				result.setStatus(ResponseStatus.FAILURE);
				result.setMessage("Problem Occurred, Please try again later");
			}
		}else {
			result.setStatus(ResponseStatus.FAILURE);
			result.setMessage("You must register between "+code.getStartDate()+" and "+code.getEndDate()+" to avail this offer");
		}
        return result;
    }

    @Override
    public ResponseDTO processRegistrationServices(PromoCode code, User u) {
        ResponseDTO result = new ResponseDTO();
        if(isValidRegistrationDate(code,u)){
			result = processServices(code,u);
		}else {
			result.setStatus(ResponseStatus.FAILURE);
			result.setMessage("You must register between "+code.getStartDate()+" and "+code.getEndDate()+" to avail this offer");
		}
		return result;
    }

    @Override
    public ResponseDTO processServices(PromoCode code, User u) {
        ResponseDTO result = new ResponseDTO();
		UserDTO userDTO = ConvertUtil.convertUser(u);
		PromoTransactionDTO transactionDTO = findTransactionDateByService(userDTO,code);
		if(transactionDTO != null){
			boolean isValidTransaction = checkTransaction(transactionDTO,code);
			if(isValidTransaction){
						double transactionAmount = Double.parseDouble(transactionDTO.getAmount());
						boolean isRedeemed = redeemCode(u,code,transactionAmount);
						if(isRedeemed){
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Balance Credited");
						}else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setDetails("Problem Occurred, Please try again later");
						}
			}else {
				result.setStatus(ResponseStatus.FAILURE);
				result.setMessage("No Transaction Available for this Promo Code");
			}
		}else {
			result.setStatus(ResponseStatus.FAILURE);
			result.setMessage("No Transaction Available for this Promo Code");
		}
        return result;
    }

    @Override
	public boolean checkActivePromoCode(String code) {
		boolean valid = false;
		PromoCode vaildCode = promoCodeRepository.findByPromoCode(code);
		if (vaildCode != null) {
			if (vaildCode.getStatus().equals(Status.Active))
				valid = true;
		}
		return valid;
	}
}
