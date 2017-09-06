package com.tripayapp.api.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.xml.messaging.URLEndpoint;
import javax.xml.soap.*;

import com.instantpay.model.Balance;
import com.instantpay.util.InstantPayConstants;
import com.tripayapp.api.ISessionApi;
import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.Status;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.*;
import com.tripayapp.util.*;
import com.tripayapp.validation.CommonValidation;
import com.tripayapp.validation.TransactionValidation;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tripayapp.api.IMailSenderApi;
import com.tripayapp.api.ISMSSenderApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.mail.util.MailTemplate;
import com.tripayapp.sms.util.SMSAccount;
import com.tripayapp.sms.util.SMSTemplate;


public class UserApi implements IUserApi, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private MessageSource messageSource;
	private final UserRepository userRepository;
	private final UserDetailRepository userDetailRepository;
	private final PQAccountDetailRepository pqAccountDetailRepository;
	private final PQAccountTypeRepository pqAccountTypeRepository;
	private final PQServiceRepository pqServiceRepository;
	private final PQTransactionRepository pqTransactionRepository;
	private final LoginLogRepository loginLogRepository;
	private final PasswordEncoder passwordEncoder;
	private final IMailSenderApi mailSenderApi;
	private final ISMSSenderApi smsSenderApi;
	private final PQVersionRepository pqVersionRepository;
	private final InviteLogRepository inviteLogRepository;
	private final PGDetailsRepository pgDetailsRepository;
	private final ISessionApi sessionApi;
	private final VBankAccountDetailRepository vBankAccountDetailRepository;
	private final LocationDetailsRepository locationDetailsRepository;

	public UserApi(UserRepository userRepository, UserDetailRepository userDetailRepository,
			PQAccountDetailRepository pqAccountDetailRepository, PQAccountTypeRepository pqAccountTypeRepository,
			PQServiceRepository pqServiceRepository, PQTransactionRepository pqTransactionRepository,
			LoginLogRepository loginLogRepository, PasswordEncoder passwordEncoder, IMailSenderApi mailSenderApi,
			ISMSSenderApi smsSenderApi,PQVersionRepository pqVersionRepository,InviteLogRepository inviteLogRepository,PGDetailsRepository pgDetailsRepository,ISessionApi sessionApi,VBankAccountDetailRepository vBankAccountDetailRepository,LocationDetailsRepository locationDetailsRepository) {
		this.userRepository = userRepository;
		this.userDetailRepository = userDetailRepository;
		this.pqAccountDetailRepository = pqAccountDetailRepository;
		this.pqAccountTypeRepository = pqAccountTypeRepository;
		this.pqServiceRepository = pqServiceRepository;
		this.pqTransactionRepository = pqTransactionRepository;
		this.loginLogRepository = loginLogRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailSenderApi = mailSenderApi;
		this.smsSenderApi = smsSenderApi;
		this.pqVersionRepository = pqVersionRepository;
		this.inviteLogRepository = inviteLogRepository;
		this.pgDetailsRepository = pgDetailsRepository;
		this.sessionApi = sessionApi;
		this.vBankAccountDetailRepository = vBankAccountDetailRepository;
		this.locationDetailsRepository = locationDetailsRepository;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public void saveUser(RegisterDTO dto) throws ClientException {
		try {

			User user = new User();
			UserDetail userDetail = new UserDetail();
			userDetail.setAddress(dto.getAddress());
			userDetail.setContactNo(dto.getContactNo());
			userDetail.setFirstName(dto.getFirstName());
			userDetail.setLastName(dto.getLastName());
			userDetail.setMiddleName(dto.getMiddleName());
			userDetail.setEmail(dto.getEmail());
			userDetail.setDateOfBirth(formatter.parse(dto.getDateOfBirth()));
			userDetail.setGender(dto.getGender());
			if (dto.getUserType().equals(UserType.Admin) || dto.getUserType().equals(UserType.Merchant)) {

				if (dto.getUserType().equals(UserType.Admin)) {
					user.setAuthority(Authorities.ADMINISTRATOR + "," + Authorities.AUTHENTICATED);
				} else if (dto.getUserType().equals(UserType.Merchant)) {
					user.setAuthority(Authorities.MERCHANT + "," + Authorities.AUTHENTICATED);
				}
				user.setPassword(passwordEncoder.encode(dto.getPassword()));
				user.setMobileStatus(Status.Active);
				user.setEmailStatus(Status.Active);
				user.setUsername(dto.getUsername().toLowerCase());
				user.setUserType(dto.getUserType());
			} else {
				LocationDetails location = locationDetailsRepository.findLocationByPin(dto.getLocationCode());
				userDetail.setLocation(location);
				user.setAuthority(Authorities.USER + "," + Authorities.AUTHENTICATED);
				user.setPassword(passwordEncoder.encode(dto.getPassword()));
				user.setMobileStatus(Status.Inactive);
				user.setEmailStatus(Status.Active);
				user.setUsername(dto.getUsername().toLowerCase());
				user.setUserType(dto.getUserType());
				user.setEmailToken("E" + System.currentTimeMillis());
				String genOtp = CommonUtil.generateSixDigitNumericString();
				User u = userRepository.checkExistingMobileToken(genOtp);
				if (u != null) {
					String getOpt = CommonValidation.getOTP(genOtp, u);
					user.setMobileToken(getOpt);
				} else {
					user.setMobileToken(CommonUtil.generateSixDigitNumericString());
				}
			}			

			user.setUserDetail(userDetail);

			PQAccountType nonKYCAccountType = pqAccountTypeRepository.findByCode("NONKYC");
			PQAccountDetail pqAccountDetail = new PQAccountDetail();
			pqAccountDetail.setBalance(0);
			pqAccountDetail.setVBankCustomer(dto.isVbankCustomer());
			pqAccountDetail.setVijayaAccountNumber(dto.getAccountNumber());
            pqAccountDetail.setBranchCode(dto.getBranchCode());
			pqAccountDetail.setAccountType(nonKYCAccountType);
			user.setAccountDetail(pqAccountDetail);

			User tempUser = userRepository.findByUsernameAndStatus(user.getUsername(), Status.Inactive);
			if (tempUser == null) {
				userDetailRepository.save(userDetail);
				pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + userDetail.getId());
				pqAccountDetailRepository.save(pqAccountDetail);
				userRepository.save(user);
			} else {
				userRepository.delete(tempUser);
				userDetailRepository.delete(tempUser.getUserDetail());
				user.setAccountDetail(tempUser.getAccountDetail());
				userDetailRepository.save(userDetail);
				userRepository.save(user);
			}

			mailSenderApi.sendMail("Email Verification", MailTemplate.VERIFICATION_EMAIL, user,null);
//			smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP, SMSTemplate.VERIFICATION_MOBILE, user,null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error While Adding User : " + e.getMessage());
			throw new ClientException("Service Down.Please try Again Later.");
		}
	}

	@Override
	public boolean isVBankAccountSaved(KycDTO dto, PQAccountDetail account) {
		boolean isSaved = false;
		try {
			String accountNumber = SecurityUtil.sha1(dto.getAccountNumber());
			VBankAccountDetail vBankAccount = new VBankAccountDetail();
			vBankAccount.setAccountNumber(accountNumber);
			vBankAccount.setMobileNumber(dto.getMobileNumber());
			vBankAccount.setStatus(Status.Inactive);
			vBankAccountDetailRepository.save(vBankAccount);
			account.setvBankAccount(vBankAccount);
			pqAccountDetailRepository.save(account);
			isSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSaved;
	}

	@Override
	public boolean sendOTP(String mobileNumber,User user) {
		String otp = CommonUtil.generateNDigitNumericString(6);
		VBankAccountDetail vBankAccountDetail = user.getAccountDetail().getvBankAccount();
		vBankAccountDetail.setOtp(otp);
		vBankAccountDetailRepository.save(vBankAccountDetail);
		smsSenderApi.sendKYCSMS(SMSAccount.PAYQWIK_OTP,SMSTemplate.VERIFICATION_MOBILE_KYC,user,mobileNumber,otp);
		return true;
	}

	@Override
	public boolean containsAccountAndMobile(String vbankAccount, String mobileNumber) {
		VBankAccountDetail account  = vBankAccountDetailRepository.findByAccountNumber(vbankAccount);
		if(account != null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void updateVBankAccountDetails(KYCResponse dto, VBankAccountDetail accountDetail,User user) {
		try {
			String accountNumber = SecurityUtil.sha1(dto.getAccountNo());
			accountDetail.setAccountNumber(accountNumber);
			accountDetail.setStatus(Status.Inactive);
			accountDetail.setMobileNumber(dto.getMobileNo());
            accountDetail.setAccountName(dto.getAccountName());
			vBankAccountDetailRepository.save(accountDetail);
			PQAccountDetail account = user.getAccountDetail();
			account.setvBankAccount(accountDetail);
			pqAccountDetailRepository.save(account);
			sendOTP(dto.getMobileNo(),user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveMerchant(MRegisterDTO dto) throws ClientException {
		try {
			String password = dto.getContactNo();
			User user = new User();
			UserDetail userDetail = new UserDetail();
			userDetail.setContactNo(dto.getContactNo());
			userDetail.setFirstName(dto.getFirstName());
			userDetail.setLastName(dto.getLastName());
			userDetail.setMiddleName(" ");
			userDetail.setEmail(dto.getEmail());
			userDetail.setImage(dto.getImage());
			user.setAuthority(Authorities.MERCHANT + "," + Authorities.AUTHENTICATED);
			user.setPassword(passwordEncoder.encode(password));
			user.setMobileStatus(Status.Active);
			user.setEmailStatus(Status.Active);
			user.setUsername(dto.getEmail());
			user.setUserType(UserType.Merchant);
			user.setUserDetail(userDetail);

			PQAccountType kYCAccountType = pqAccountTypeRepository.findByCode("SUPERKYC");
			PQAccountDetail pqAccountDetail = new PQAccountDetail();
			pqAccountDetail.setBalance(0);
			pqAccountDetail.setAccountType(kYCAccountType);
			user.setAccountDetail(pqAccountDetail);

			User tempMerchant = userRepository.findByUsernameAndStatus(user.getUsername(), Status.Active);
			if (tempMerchant == null) {
				userDetailRepository.save(userDetail);
				pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + userDetail.getId());
				pqAccountDetailRepository.save(pqAccountDetail);
				userRepository.save(user);
			} else {
				userRepository.delete(tempMerchant);
				userDetailRepository.delete(tempMerchant.getUserDetail());
				user.setAccountDetail(tempMerchant.getAccountDetail());
				userDetailRepository.save(userDetail);
				userRepository.save(user);
			}
//			mailSenderApi.sendNoReplyMail("Verification Successful", MailTemplate.MERCHANT_REGISTRATION, user,password);
			smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP, SMSTemplate.VERIFICATION_SUCCESS, user,null);

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error While Adding User : " + e.getMessage());
			throw new ClientException("Service Down.Please try Again Later.");
		}
	}

	@Override
	public void editUser(RegisterDTO dto) {
		User user = userRepository.findByUsername(dto.getUsername());
		UserDetail detail = user.getUserDetail();
		if (user != null) {
			if (user.getEmailStatus() != Status.Active) {
				userDetailRepository.updateUserDetail(dto.getAddress(), dto.getFirstName(), dto.getLastName(),
						dto.getEmail(), dto.getUsername(),dto.getGender());
			} else {
				userDetailRepository.updateUserDetailOnly(dto.getAddress(), dto.getFirstName(), dto.getLastName(),
						dto.getUsername(),dto.getGender());
			}
		}
	}

	@Override
	public User findByUserName(String name) {
		User user = userRepository.findByUsername(name);
		return user;
	}

	@Override
	public User findById(long id) {
		System.out.println("Merchant Id  : :"+id );
		User user = userRepository.findUserByUserId(id);
		return user;
	}
		

	@Override
	public List<UserDTO> findAllUser() {
		List<User> user = userRepository.findAllUser();
		if (user != null) {
			return ConvertUtil.convertUserList(user);
		}
		return null;
	}


	@Override
	public double getWalletBalance(User u) {
		return pqAccountDetailRepository.getUserWalletBalance(u.getAccountDetail().getId());
	}

	@Override
	public boolean isVerified(KycDTO dto, User u) {
		boolean valid = false;
		VBankAccountDetail bankAccount = u.getAccountDetail().getvBankAccount();
		String otp = dto.getOtp();
		String validOTP = bankAccount.getOtp();
		if(otp.equals(validOTP)) {
			valid = true;
			bankAccount.setOtp(null);
			bankAccount.setStatus(Status.Active);
			vBankAccountDetailRepository.save(bankAccount);
			updateAccountType(u.getUsername(),true);
			User newUser = findByUserName(u.getUsername());
            smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP,SMSTemplate.VERIFICATION_KYC_SUCCESS,newUser,null);
		}
		return valid;
	}

	@Override
	public PGDetails findMerchantByMobile(String mobileNumber) {
		PGDetails pgDetails = null;
		List<PGDetails> pgList = (List<PGDetails>) pgDetailsRepository.findAll();
		for(PGDetails pg: pgList){
			if(pg != null) {
				User merchant = pg.getUser();
				if(merchant != null) {
					String mobile = merchant.getUserDetail().getContactNo();
					if(mobileNumber.equals(mobile)){
						pgDetails = pg;
						break;
					}
				}
			}
		}
		return pgDetails;
	}

	@Override
	public ArrayList<Long> getDailyTransactionsCountFromDate() {
		ArrayList<Long> fiveTransactions = new ArrayList<>();
		Calendar calender = Calendar.getInstance();
			for(int i = -1; i >= -5 ; i--) {
			calender.add(Calendar.DATE,i);
			Date date = new Date(calender.getTimeInMillis());
			System.err.println(date);
			fiveTransactions.add(pqTransactionRepository.countDailyTransactionByDate(date));
		}
		return fiveTransactions;
	}

	@Override
	public ArrayList<Double> getDailyTransactionsAmountFromDate() {
		ArrayList<Double> fiveTransactions = new ArrayList<>();
		Calendar calender = Calendar.getInstance();

		for(int i = -1; i >= -5 ; i--) {
			calender.add(Calendar.DATE,i);
			Date date = new Date(calender.getTimeInMillis());
			System.err.println(date);
			fiveTransactions.add(pqTransactionRepository.countDailyTransactionAmountByDate(date));
		}
		return fiveTransactions;
	}

	@Override
	public ResponseDTO requestOfflinePayment(PaymentRequestDTO dto, User merchant,User user) {
		ResponseDTO result = new ResponseDTO();
        String otp = CommonUtil.generateNDigitNumericString(6);
        user.setMobileToken(otp);
        userRepository.save(user);
        smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP,SMSTemplate.OFFLINE_VERIFICATION_MOBILE,user,merchant.getUserDetail().getFirstName());
        result.setStatus(ResponseStatus.SUCCESS);
        result.setMessage("OTP sent to ::"+user.getUsername());
        result.setDetails(dto.getAmount());
		return result;
	}

	@Override
	public void changePassword(ChangePasswordDTO dto, Long id) {
		User user = userRepository.findOne(id);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		userRepository.save(user);
	}

	@Override
	public boolean updateAccountType(String username, boolean kyc) {
		boolean isUpdated = false;
		PQAccountType updationType = null;
		String code = "";
		User user = userRepository.findByUsername(username);
		if(user != null){
			PQAccountDetail account = user.getAccountDetail();
			if(account != null){
				PQAccountType accountType = account.getAccountType();
				if(accountType != null){
					if(kyc) {
						code = "KYC";
					}else {
						code = "NONKYC";
					}
					updationType = pqAccountTypeRepository.findByCode(code);
					account.setAccountType(updationType);
					pqAccountDetailRepository.save(account);
					isUpdated = true;
				}
			}
		}

		return isUpdated;
	}

	@Override
	public boolean checkMobileToken(String key, String mobileNumber) {
		User user = userRepository.findByMobileTokenAndStatus(key, mobileNumber, Status.Inactive);
		if (user == null) {
			return false;
		} else {
			user.setMobileStatus(Status.Active);
			user.setMobileToken(null);
			userRepository.save(user);
			smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP, SMSTemplate.VERIFICATION_SUCCESS, user,null);
			return true;
		}
	}

	@Override
	public boolean resendMobileToken(String username) {
		User u = userRepository.findByUsernameAndStatus(username, Status.Inactive);
		if (u == null) {
			return false;
		} else {
			smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP, SMSTemplate.REGENERATE_OTP, u,null);
			return true;
		}
	}

	@Override
	public boolean activateEmail(String key) {
		User user = userRepository.findByEmailTokenAndEmailStatusAndMobileStatus(key, Status.Inactive, Status.Active);
		if (user != null) {
			user.setEmailStatus(Status.Active);
			user.setEmailToken(null);
			userRepository.save(user);
			mailSenderApi.sendMail("Email Verification Successful", MailTemplate.VERIFICATION_SUCCESS, user, null);
			return true;
		}
		return false;
	}

	@Override
	public void changePasswordRequest(User u) {
		u.setMobileToken(CommonUtil.generateSixDigitNumericString());
		userRepository.save(u);
		smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP, SMSTemplate.CHANGE_PASSWORD_REQUEST, u,null);
	}

	@Override
	public UserDTO checkPasswordToken(String key) {
		User user = userRepository.findByOTPAndStatus(key, Status.Active);
		if (user != null) {
			UserDTO dto = ConvertUtil.convertUser(user);
			return dto;
		}
		return null;
	}

	@Override
	public void revertAuthority() {

			List<User> users = userRepository.findAllBlockedUsers();
            Date now  = new Date();
            String authority = Authorities.USER+","+Authorities.AUTHENTICATED;
			for(User user : users){
                    Date last = loginLogRepository.findLastLoginDateOfUser(user);
                    if(last != null) {
                        long hours = (now.getTime() - last.getTime()) / (1000 * 60 * 60);
                        System.err.println(hours);
                        if(hours >= 12) {
                            updateUserAuthority(authority, user.getId());
                        }
                    }
			}
	}

	@Override
	public void renewPassword(ChangePasswordDTO dto) {
		User user = userRepository.findByMobileTokenAndUsername(dto.getKey(), dto.getUsername());
		if (user != null) {
			user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
			user.setMobileToken(null);
			userRepository.save(user);
			mailSenderApi.sendMail("Password Changed Successful", MailTemplate.CHANGE_PASSWORD_SUCCESS,
					user,null);
			smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP, SMSTemplate.CHANGE_PASSWORD_SUCCESS, user,null);
		}
	}

	@Override
	public void renewPasswordFromAccount(ChangePasswordDTO dto) {
		User user = userRepository.findByUsername(dto.getUsername());
		if (user != null) {
			user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
			userRepository.save(user);
			mailSenderApi.sendMail("Password Changed Successful", MailTemplate.CHANGE_PASSWORD_SUCCESS,
					user,null);
			smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP, SMSTemplate.CHANGE_PASSWORD_SUCCESS, user,null);
		}
	}

	@Override
	public void toggleStatus(String username, Status status) {
		User u = userRepository.findByUsername(username.toLowerCase());
		if (u != null) {
			u.setEmailStatus(status);
			userRepository.save(u);
		}
	}

	@Override
	public KYCResponse verifyByKycApi(KycDTO dto) {
		boolean valid = false;
        KYCResponse response = new KYCResponse();
		System.err.println("-----1");
			String stringResponse = "";
		try {

			System.err.println("-----2");

			String payload = TriPayUtil.getInString(dto);

            String newPayload = URLEncoder.encode(payload);
            System.err.println(newPayload);
			WebResource resource = Client.create().resource(TriPayUtil.KYC_URL).queryParam("INPUT",newPayload);
			System.err.println("-----3");
			ClientResponse clientResponse = resource.get(ClientResponse.class);
			System.err.println("-----4");
			stringResponse = clientResponse.getEntity(String.class);
//			System.out.println("KYC Response is not 200::" + stringResponse);
			String []responseParts = stringResponse.split("\\r?\\n");
            String jsonResponse = responseParts[0].trim();
            System.err.println("json response string is ::"+jsonResponse);
            JSONObject json = new JSONObject(jsonResponse);
            if(json != null) {
                String responseCode = JSONParserUtil.getString(json,"RESPONSECODE");
                System.err.println(responseCode);
                if(responseCode.equalsIgnoreCase("000")) {
                    String noOfAccount = JSONParserUtil.getString(json, "NOOFACNT");
                    int accounts = Integer.parseInt(noOfAccount);
                    System.err.format("accounts are %d", accounts);
                    if(accounts > 0) {
                        valid = true;
                        JSONArray accountArray = json.getJSONArray("INDV_ACNT");
                        JSONObject accountOne = accountArray.getJSONObject(0);
                        String accountNo = JSONParserUtil.getString(accountOne,"ACNO");
                        String accountName = JSONParserUtil.getString(accountOne,"ACNONAME");
                        String mobileNo = JSONParserUtil.getString(accountOne,"MOBNO");
                        response.setAccountName(accountName);
                        response.setMobileNo(mobileNo);
                        response.setAccountNo(accountNo);
                    }
                }

            }

		} catch (Exception e) {
			e.printStackTrace();
		}
		    response.setValid(valid);
			return response;
	}

	@Override
	public AmountDTO getAdminLoginValues() {
		User instantpay = findByUserName("instantpay@payqwik.in");
		User commission = findByUserName("commission@vpayqwik.com");
		User eTravos = findByUserName("ggupta@firstglobalmoney.com");
		User bank = findByUserName(StartupUtil.BANK);
		User mbank = findByUserName(StartupUtil.MBANK);

		User promo = findByUserName(StartupUtil.PROMO_CODE);
		PQService loadMoneyService = pqServiceRepository.findServiceByCode("LMC");
		double totalEBS = pqTransactionRepository.getValidAmountByService(loadMoneyService);
		PQService vNetService = pqServiceRepository.findServiceByCode("LMB");
		double totalVNet = pqTransactionRepository.getValidAmountByService(vNetService);
		double commissionAmount = commission.getAccountDetail().getBalance();
		double totalPayable = instantpay.getAccountDetail().getBalance();
		double bankAmount = bank.getAccountDetail().getBalance();
		double mBankAmount = mbank.getAccountDetail().getBalance();
		double promoBalance = promo.getAccountDetail().getBalance();
		double poolBalance = (totalEBS + totalVNet) - (commissionAmount + totalPayable + bankAmount);

		double totalEBSNow = 0;
		double topupTotal = 0;
		long totalUsers = 0;
		double totalVNetNow = 0;
		long totalMale = 0;
		long totalFemale = 0;
		long totalTransactions = 0;
		long onlineUsers = 0;
		String date = formatter.format(new Date());
		Date newDate = null;
		try {
			newDate = formatter.parse(date);
		 totalTransactions = pqTransactionRepository.countTotalTransactionsByStatus(Status.Success,TransactionType.DEFAULT);
		totalUsers = userRepository.getTotalUsersCount(UserType.User);
		 totalMale = userDetailRepository.countUsersByGender(Gender.M);
		 totalFemale = userDetailRepository.countUsersByGender(Gender.F);
		 onlineUsers = sessionApi.countActiveSessions();
		totalVNetNow = pqTransactionRepository.getValidAmountByServiceForDate(vNetService, newDate);
		 totalEBSNow = pqTransactionRepository.getValidAmountByServiceForDate(loadMoneyService, newDate);
		topupTotal = pqTransactionRepository.getValidTopupAmountForDate(instantpay.getAccountDetail(),newDate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		AmountDTO amountDTO = new AmountDTO();
		amountDTO.setBankAmount(bankAmount);
		amountDTO.setmBankAmount(mBankAmount);
		amountDTO.setWalletBalance(poolBalance);
		amountDTO.setTotalUsers(totalUsers);
		amountDTO.setTotalLoadMoneyEBS(totalEBS);
		amountDTO.setTotalLoadMoneyVNet(totalVNet);
		amountDTO.setTotalPayable(totalPayable);
		amountDTO.setMerchantPayable(eTravos.getAccountDetail().getBalance());
		amountDTO.setTotalCommission(commissionAmount);
		amountDTO.setTotalLoadMoneyEBSNow(totalEBSNow);
		amountDTO.setTotalLoadMoneyVNETNow(totalVNetNow);
		amountDTO.setTotalPayableNow(topupTotal);
		amountDTO.setPromoBalance(promoBalance);
		amountDTO.setFemaleUsers(totalFemale);
		amountDTO.setMaleUsers(totalMale);
		amountDTO.setTotalTransaction(totalTransactions);
		amountDTO.setDailyCounts(getDailyTransactionsCountFromDate());
		amountDTO.setDailyAmounts(getDailyTransactionsAmountFromDate());
		amountDTO.setOnlineUsers(onlineUsers);
		return amountDTO;
	}

	@Override
	public UserDTO getUserById(Long id) {
		User u = userRepository.findOne(id);
		if (u != null) {
			return ConvertUtil.convertUser(u);
		}
		return null;
	}

	@Override
	public List<User> getUserListByPage(int page, int perPage) {
		Pageable pageable = new PageRequest(page, perPage);
		List<User> result = userRepository.findWithPageable(pageable);
		return result;
	}

	@Override
	public long getUserCount() {
		return userRepository.getUserCount();
	}

	@Override
	public double getTotalWalletBalance() {
		double walletBalance = 0;
		double balance = 0;
		List<User> userList = userRepository.getTotalUsers(UserType.User);
		List<PGDetails> pgDetailsList = (List<PGDetails>)pgDetailsRepository.findAll();
		if(pgDetailsList != null && !(pgDetailsList.isEmpty())){
			for(PGDetails pg:pgDetailsList){
				userList.add(pg.getUser());
			}
		}
		for(User u:userList){
			balance = u.getAccountDetail().getBalance();
			walletBalance += balance;
		}
		return walletBalance;
	}


	@Override
	public void updateUserAuthority(String authority, long id) {
		userRepository.updateUserAuthority(authority, id);
	}

	@Override
	public String handleLoginFailure(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication, String loginUsername,String ipAddress) {
		// String loginUsername = (String) authentication.getPrincipal();
		logger.info("LOGIN USERNAME :: " + loginUsername);
		if (loginUsername != null) {
			User user = userRepository.findByUsername(loginUsername);
			if (user != null) {
				if (user.getMobileStatus() == Status.Active) {
					if (user.getAuthority().contains(Authorities.BLOCKED)) {
						return "Your account is blocked! Please contact support.";
					}
					LoginLog loginLog = new LoginLog();
					loginLog.setUser(user);
					loginLog.setRemoteAddress(ipAddress);
					loginLog.setServerIp(request.getRemoteAddr());
					loginLog.setStatus(Status.Failed);
					loginLogRepository.save(loginLog);

					List<LoginLog> llsFailed = loginLogRepository.findTodayEntryForUserWithStatus(user, Status.Failed);
					int failedCount = llsFailed.size();
					int remainingAttempts = getLoginAttempts() - failedCount;
					if (failedCount == getLoginAttempts()) {
						if (user.getUserType() == UserType.Merchant) {
							String authority = Authorities.MERCHANT + "," + Authorities.BLOCKED;
							updateUserAuthority(authority, user.getId());
						} else if (user.getUserType() == UserType.User) {
							String authority = Authorities.USER + "," + Authorities.BLOCKED;
							updateUserAuthority(authority, user.getId());
						} else if (user.getUserType() == UserType.Admin) {
							String authority = Authorities.ADMINISTRATOR + "," + Authorities.BLOCKED;
							updateUserAuthority(authority, user.getId());
						}
						logger.info("Account blocked for user {}", user.getUsername());
						return "Your account is blocked! Please try after 24 hrs.";
					}
					logger.info("incorrect password for user {} attempts remaining {}", user.getUsername(),
							remainingAttempts);
					return "Incorrect password. Remaining attempts " + remainingAttempts;
				} else {
					logger.info("user {} is inactive", user.getUsername());
					return "User doesn't exists";
				}
			} else {
				logger.info("user doesn't exist");
				return "User doesn't exists";
			}
		}
		return "Authentication Failed. Please try again";
	}

	@Override
	public void handleLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication, String loginUsername,String ipAddress) {
		logger.info("Authenticated LOGIN");
		// UserDetailsWrapper userDetailsWrapper = (UserDetailsWrapper)
		// authentication.getPrincipal();
		// String loginUsername = userDetailsWrapper.getUsername();

		if (loginUsername != null) {
			User user = userRepository.findByUsername(loginUsername);
			if (user != null) {
				LoginLog loginLog = new LoginLog();
				loginLog.setUser(user);
				loginLog.setRemoteAddress(ipAddress);
				loginLog.setServerIp(request.getRemoteAddr());
				loginLog.setStatus(Status.Success);
				loginLogRepository.save(loginLog);
				List<LoginLog> lls = loginLogRepository.findTodayEntryForUserWithStatus(user, Status.Failed);
				for (LoginLog ll : lls) {
					loginLogRepository.deleteLoginLogForId(Status.Deleted, ll.getId());
				}
			}
		}
	}

	@Override
	public String authenticateVersion(String version) {
		if(version != null && version.contains(".")) {
			int mainVersion = 0;
			int subVersion = 0;
			PQVersion pqVersion = null;
			String[] versionParts = version.split("\\.");
			mainVersion = Integer.parseInt(versionParts[0]);
			subVersion = Integer.parseInt(versionParts[1]);
			if(mainVersion > 0){
				pqVersion = pqVersionRepository.findByVersionNo(mainVersion,subVersion);
			}
			if(pqVersion != null){
				if(pqVersion.getStatus().equals(Status.Active)){
					return "true|Valid Version";
				}else if(pqVersion.getStatus().equals(Status.Inactive)){
					PQVersion latestVersion = pqVersionRepository.findLatestVersion();
					return "false|Please Update Your app to version "+latestVersion.getVersionCode()+"."+latestVersion.getSubversionCode();

				}
			}
		}
		return "Not a Valid Version Format";
	}

	@Override
	public int updatePoints(long points, PQAccountDetail account) {
		return pqAccountDetailRepository.updateUserPoints(points,account.getId());
	}

	@Override
	public int getLoginAttempts() {
		return 5;
	}

	@Override
	public int updateBalance(double balance, User user) {
		return pqAccountDetailRepository.updateUserBalance(balance, user.getAccountDetail().getId());
	}

	@Override
	public double dailyTransactionTotal(User user) {
		Calendar now = Calendar.getInstance();
		double total = 0;
		try {
			List<PQTransaction> trans = pqTransactionRepository.getDailyTransaction(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), user.getAccountDetail());
			if (trans.size() != 0) {
				total = pqTransactionRepository.getDailyTransactionTotal(now.get(Calendar.YEAR),
						(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), user.getAccountDetail());
			} else {
				total = 0;
			}
		} catch (NullPointerException e) {
			total = 0;
		} catch (Exception e) {
			total = -1;
			e.printStackTrace();
		}
		return total;
	}

	@Override
	public void blockUser(String username) {
	User u = findByUserName(username);
		if(u != null) {
			String authority = u.getAuthority();
			if(authority.contains(Authorities.USER) && authority.contains(Authorities.AUTHENTICATED)){
				authority = Authorities.USER+ ","+Authorities.LOCKED;
				updateUserAuthority(authority,u.getId());
				sessionApi.clearAllSessionForUser(u);
			}
		}
	}

	@Override
	public double dailyDebitTransactionTotal(User user) {
		Calendar now = Calendar.getInstance();
		double total = 0;
		try {
			List<PQTransaction> trans = pqTransactionRepository.getDailyDebitTransaction(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), user.getAccountDetail(), true);
			if (trans.size() != 0) {
				total = pqTransactionRepository.getDailyDebitTransactionTotal(now.get(Calendar.YEAR),
						(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), user.getAccountDetail(), true);
			} else {
				total = 0;
			}
		} catch (NullPointerException e) {
			total = 0;
		} catch (Exception e) {
			total = -1;
			e.printStackTrace();
		}
		return total;
	}

	@Override
	public double monthlyTransactionTotal(User user) {
		Calendar now = Calendar.getInstance();
		double total = 0;
		try {
			List<PQTransaction> trans = pqTransactionRepository.getMonthlyTransaction(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), user.getAccountDetail());
			if (trans.size() != 0) {
				total = pqTransactionRepository.getMonthlyTransactionTotal(now.get(Calendar.YEAR),
						(now.get(Calendar.MONTH) + 1), user.getAccountDetail());
			} else {
				total = 0;
			}
		} catch (NullPointerException e) {
			total = 0;
		} catch (Exception e) {
			total = -1;
			e.printStackTrace();
		}
		return total;
	}

	@Override
	public List<User> getUserListByVerifiedUsersPage(int page, int perPage) {
		Pageable pageable = new PageRequest(page, perPage);
		List<User> result = userRepository.findWithVerifiedUsersPageable(pageable);
		return result;
	}

	@Override
	public void saveUnregisteredUserSendMoney(RegisterDTO dto) throws ClientException {
		try {
			User user = new User();
			UserDetail userDetail = new UserDetail();
			userDetail.setContactNo(dto.getContactNo());
			userDetail.setFirstName(dto.getFirstName());
			userDetail.setLastName(dto.getLastName());
			userDetail.setMiddleName(dto.getMiddleName());
			userDetail.setEmail(dto.getEmail());
			if (dto.getUserType().equals(UserType.Admin) || dto.getUserType().equals(UserType.Merchant)) {
				if (dto.getUserType().equals(UserType.Admin)) {
					user.setAuthority(Authorities.ADMINISTRATOR + "," + Authorities.AUTHENTICATED);
				} else if (dto.getUserType().equals(UserType.Merchant)) {
					user.setAuthority(Authorities.MERCHANT + "," + Authorities.AUTHENTICATED);
				}
				user.setPassword(passwordEncoder.encode(dto.getPassword()));
				user.setMobileStatus(Status.Active);
				user.setEmailStatus(Status.Active);
				user.setUsername(dto.getUsername().toLowerCase());
				user.setUserType(dto.getUserType());
			} else {
				user.setAuthority(Authorities.USER + "," + Authorities.AUTHENTICATED);
				user.setPassword(passwordEncoder.encode(dto.getPassword()));
				user.setMobileStatus(Status.Inactive);
				user.setEmailStatus(Status.Inactive);
				user.setUsername(dto.getUsername().toLowerCase());
				user.setUserType(dto.getUserType());
				user.setMobileToken(CommonUtil.generateSixDigitNumericString());
			}
			user.setUserDetail(userDetail);

			PQAccountType nonKYCAccountType = pqAccountTypeRepository.findByCode("NONKYC");
			PQAccountDetail pqAccountDetail = new PQAccountDetail();
			pqAccountDetail.setBalance(0);
			pqAccountDetail.setAccountType(nonKYCAccountType);
			user.setAccountDetail(pqAccountDetail);

			User tempUser = userRepository.findByUsernameAndStatus(user.getUsername(), Status.Inactive);
			if (tempUser == null) {
				userDetailRepository.save(userDetail);
				pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + userDetail.getId());
				pqAccountDetailRepository.save(pqAccountDetail);
				userRepository.save(user);
			} else {
				userRepository.delete(tempUser);
				userDetailRepository.delete(tempUser.getUserDetail());
				user.setAccountDetail(tempUser.getAccountDetail());
				userDetailRepository.save(userDetail);
				userRepository.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error While Adding User : " + e.getMessage());
			throw new ClientException("Service Down.Please try Again Later.");
		}
	}

	@Override
	public User findByAccountDetail(PQAccountDetail accountDetail) {
		return userRepository.findByAccountDetails(accountDetail);
	}

	@Override
	public PQAccountDetail saveOrUpdateAccount(PQAccountDetail accountDetail) {
		return pqAccountDetailRepository.save(accountDetail);
	}

	@Override
	public boolean saveImage(User user, String url) {
		UserDetail userDetail = user.getUserDetail();
		userDetail.setImage(url);
		userDetailRepository.updateUserImage(url, userDetail.getContactNo());
		return true;
	}

    @Override
    public int updateVersion(int versionCode, int subVersionCode) {
        PQVersion version = pqVersionRepository.findByVersionNo(versionCode,subVersionCode);
        int rowsUpdated = 0;
        if(version == null){

            version = new PQVersion();
            version.setVersionCode(versionCode);
            version.setSubversionCode(subVersionCode);
			version.setStatus(Status.Active);
			pqVersionRepository.save(version);
			rowsUpdated = pqVersionRepository.disablePreviousVersionsBeforeID(version.getId());
        }else {
			rowsUpdated = pqVersionRepository.activateAllVersionsAfterID(version.getId());
        }
        return rowsUpdated;
    }

    @Override
	public List<PQTransaction> getUserTransactions(User user) {
		List<PQTransaction> listOfTransactions = pqTransactionRepository.getTotalTransactions(user.getAccountDetail());
		return listOfTransactions;
	}

	@Override
	public boolean setNewMpin(MpinDTO dto) {
		boolean updated = false;
		User user = userRepository.findByUsername(dto.getUsername());
		if (user != null && user.getUserDetail().getMpin() == null) {
			String mpin = dto.getNewMpin();
			try {
				String hashedMpin = SecurityUtil.sha512(mpin);
				userDetailRepository.updateUserMPIN(hashedMpin, user.getUsername());
				updated = true;
			} catch (Exception e) {
				updated = false;
			}
		}
		return updated;
	}

	@Override
	public boolean changeCurrentMpin(MpinChangeDTO dto) {
		boolean updated = false;
		User user = userRepository.findByUsername(dto.getUsername());
		if (user != null && user.getUserDetail().getMpin() != null) {
			String mpin = dto.getNewMpin();
            try {
                String hashedMpin = SecurityUtil.sha512(mpin);
                userDetailRepository.updateUserMPIN(hashedMpin, user.getUsername());
                updated = true;
            }catch(Exception e){
                updated = false;
            }
		}
		return updated;
	}

	@Override
	public boolean deleteCurrentMpin(String username) {
		boolean deleted = false;
		User user = userRepository.findByUsername(username);
		if (user != null && user.getUserDetail().getMpin() != null) {
			userDetailRepository.deleteUserMPIN(username);
			deleted = true;
		}
		return deleted;
	}

	@Override
	public Page<User> getTotalUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public double monthlyLoadMoneyTransactionTotal(User user) {
//		PQService loadMoneyService = pqServiceRepository.findServiceByCode("LMC");
		Calendar now = Calendar.getInstance();
		double total = 0;
		try {
			total = pqTransactionRepository.getMonthlyTransactionTotal(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), user.getAccountDetail());
		} catch (NullPointerException e) {
			total = 0;
		} catch (Exception e) {
			total = -1;
			e.printStackTrace();
		}
		return total;
	}

	@Override
	public int getTransactionTimeDifference() {
		return 1;
	}

	@Override
	public Page<User> getActiveUsers(Pageable pageable) {
		return userRepository.getActiveUsers(pageable);
	}

	@Override
	public Page<User> getInactiveUsers(Pageable pageable) {
		return userRepository.getInactiveUsers(pageable);
	}

	@Override
	public Page<User> getBlockedUsers(Pageable pageable) {
		return userRepository.getBlockedUsers(pageable);
	}

	@Override
	public Page<User> getLockedUsers(Pageable pageable) {
		return userRepository.getLockedUsers(pageable);
	}

	@Override
	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}
	


	@Override
	public boolean inviteByMobile(String number, String message,User u) {
		boolean valid = false;
        InviteLog exists = inviteLogRepository.getLogByUserAndMobile(u,number);
        if(exists == null) {
            valid = true;
            exists = new InviteLog();
            exists.setContactNo(number);
            exists.setUser(u);
            inviteLogRepository.save(exists);
//            smsSenderApi.promotionalEmails(number, message);
        }
        return valid;
	}

	@Override
	public void inviteByEmail(String subject, String mailTemplate, User user) {
		mailSenderApi.sendMail(subject, mailTemplate, user, null);
	}

	@Override
	public void inviteByEmailAddress(String subject, String mailTemplate, String email) {
		User user = new User();
		user.setUserDetail(new UserDetail());
		user.getUserDetail().setEmail(email);
		mailSenderApi.sendMail(subject, mailTemplate, user, null);
	}

	@Override
	public void reSendEmailOTP(User user) {
		mailSenderApi.sendMail("Email Verification", MailTemplate.VERIFICATION_EMAIL, user, null);
	}

	@Override
	public boolean redeemCode(User user, String promoCode) {
		user.getId();
		return false;
	}

	@Override
	public boolean changeEmail(User user, String email) {
		Status isActive = user.getEmailStatus();
		if (isActive.equals(Status.Inactive)) {
			userDetailRepository.updateChangeEmail(email, user.getUserDetail().getId());
			return true;
		}
		return false;
	}

	@Override
	public List<PQVersion> getAllVersions() {
		return pqVersionRepository.getAllVersions();
	}

	@Override
	public PQVersion getLatestVersion() {
		return pqVersionRepository.findLatestVersion();
	}

	@Override
	public int updateGcmId(String gcmId,String username) {
		int rowsUpdated = userRepository.updateGCMID(gcmId, username);
		return rowsUpdated;
	}

	@Override
	public User saveOrUpdateUser(User u) {
		return userRepository.save(u);
	}

	@Override
	public double getBalanceByUserAccount(PQAccountDetail accountDetail) {
		return pqAccountDetailRepository.getBalanceByUserAccount(accountDetail.getId());
	}

	@Override
	public LoginLog getLastLoginOfUser(User u,Status status) {

		return loginLogRepository.findLastLoginOfUser(u,status);
	}

	@Override
	public boolean isValidLastLoginDevice(String device, User user) {
		boolean isValidDevice = false;
		LoginLog loginLog = getLastLoginOfUser(user,Status.Success);
		if(loginLog != null) {
			String deviceId = loginLog.getRemoteAddress();
			if(deviceId != null) {
				isValidDevice = deviceId.equalsIgnoreCase(device);
			}
		}
		return isValidDevice;
	}

	@Override
	public void requestNewLoginDevice(User user) {
		String otp = CommonUtil.generateNDigitNumericString(6);
		user.setMobileToken(otp);
		userRepository.save(user);
		smsSenderApi.sendUserSMS(SMSAccount.PAYQWIK_OTP,SMSTemplate.VERIFICATION_MOBILE,user,null);
	}

	@Override
	public boolean isValidLoginToken(String otp,User user) {
		boolean isValidToken = false;
		String mobileToken = user.getMobileToken();
		if(mobileToken != null) {
			if(otp.equalsIgnoreCase(mobileToken)) {
				isValidToken = true;
			}
		}
		return isValidToken;
	}


}
