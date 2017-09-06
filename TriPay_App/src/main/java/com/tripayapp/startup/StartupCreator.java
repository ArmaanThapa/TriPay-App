package com.tripayapp.startup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.tripayapp.util.StartupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.instantpay.util.InstantPayConstants;
import com.tripayapp.entity.PQAccountDetail;
import com.tripayapp.entity.PQAccountType;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserDetail;
import com.tripayapp.model.Gender;
import com.tripayapp.model.Status;
import com.tripayapp.model.UserType;
import com.tripayapp.repositories.PQAccountDetailRepository;
import com.tripayapp.repositories.PQAccountTypeRepository;
import com.tripayapp.repositories.UserDetailRepository;
import com.tripayapp.repositories.UserRepository;
//import com.tripayapp.travel.util.BusConstants;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.TriPayUtil;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.CommonValidation;


public class StartupCreator {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserDetailRepository userDetailRepository;
	private final PQAccountDetailRepository pqAccountDetailRepository;
	private final PQAccountTypeRepository pqAccountTypeRepository;

	public StartupCreator(UserRepository userRepository, PasswordEncoder passwordEncoder,
			UserDetailRepository userDetailRepository, PQAccountDetailRepository pqAccountDetailRepository,
			PQAccountTypeRepository pqAccountTypeRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userDetailRepository = userDetailRepository;
		this.pqAccountDetailRepository = pqAccountDetailRepository;
		this.pqAccountTypeRepository = pqAccountTypeRepository;
	}
//
//	public void create(){
//
//	}
	public void create() {
		List<UserDetail> userDetails = (List<UserDetail>) userDetailRepository.findAll();
		if (userDetails != null) {
			for (UserDetail detail : userDetails) {
				try {
					String mpin = detail.getMpin();
					if (isRawMpin(mpin)) {
						String hashedMpin = SecurityUtil.sha512(mpin);
						detail.setMpin(hashedMpin);
						userDetailRepository.save(detail);
					}
				} catch (Exception e) {
					continue;
				}
			}
		}

		PQAccountType acTypeKYC = pqAccountTypeRepository.findByCode(StartupUtil.KYC);
		if (acTypeKYC == null) {
			acTypeKYC = new PQAccountType();
			acTypeKYC.setCode(StartupUtil.KYC);
			acTypeKYC.setDailyLimit(50000);
			acTypeKYC.setMonthlyLimit(50000);
			acTypeKYC.setBalanceLimit(50000);
			acTypeKYC.setName("KYC");
			acTypeKYC.setDescription("Verified Account");
			acTypeKYC.setTransactionLimit(100);
			pqAccountTypeRepository.save(acTypeKYC);
		}


		PQAccountType acTypeSuperKYC = pqAccountTypeRepository.findByCode(StartupUtil.SUPER_KYC);
		if (acTypeSuperKYC == null) {
			acTypeSuperKYC = new PQAccountType();
			acTypeSuperKYC.setCode(StartupUtil.SUPER_KYC);
			acTypeSuperKYC.setDailyLimit(5000000);
			acTypeSuperKYC.setMonthlyLimit(5000000);
			acTypeSuperKYC.setBalanceLimit(5000000);
			acTypeSuperKYC.setName("KYC - MERCHANT");
			acTypeSuperKYC.setDescription("Verified Account");
			acTypeSuperKYC.setTransactionLimit(10000000);
			pqAccountTypeRepository.save(acTypeSuperKYC);
		}


		PQAccountType acTypeNonKYC = pqAccountTypeRepository.findByCode(StartupUtil.NON_KYC);
		if (acTypeNonKYC == null) {
			acTypeNonKYC = new PQAccountType();
			acTypeNonKYC.setCode(StartupUtil.NON_KYC);
			acTypeNonKYC.setDailyLimit(20000);
			acTypeNonKYC.setMonthlyLimit(20000);
			acTypeNonKYC.setBalanceLimit(20000);
			acTypeNonKYC.setName("Non - KYC");
			acTypeNonKYC.setDescription("Unverified Account");
			acTypeNonKYC.setTransactionLimit(5);
			pqAccountTypeRepository.save(acTypeNonKYC);
		}

		User admin = userRepository.findByUsername(StartupUtil.ADMIN);
		if (admin == null) {
			UserDetail detail = new UserDetail();
			detail.setAddress("JP Nagar");
			detail.setContactNo(StartupUtil.ADMIN);
			detail.setFirstName("TriPay");
			detail.setMiddleName("_");
			detail.setLastName("Admin");
			detail.setEmail(StartupUtil.ADMIN);
			userDetailRepository.save(detail);

			PQAccountDetail pqAccountDetail = new PQAccountDetail();
			pqAccountDetail.setBalance(0);
			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
			pqAccountDetail.setAccountType(acTypeKYC);
			pqAccountDetailRepository.save(pqAccountDetail);

			admin = new User();
			admin.setAuthority(Authorities.ADMINISTRATOR + "," + Authorities.AUTHENTICATED);
			admin.setPassword(passwordEncoder.encode("123456789"));
			admin.setCreated(new Date());
			admin.setMobileStatus(Status.Active);
			admin.setEmailStatus(Status.Active);
			admin.setUserType(UserType.Admin);
			admin.setUsername(detail.getContactNo());
			admin.setUserDetail(detail);
			admin.setAccountDetail(pqAccountDetail);
			userRepository.save(admin);
		}

		User settlement = userRepository.findByUsername(StartupUtil.SETTLEMENT);
		if (settlement == null) {
			UserDetail detail = new UserDetail();
			detail.setAddress("JP Nagar");
			detail.setContactNo(StartupUtil.SETTLEMENT);
			detail.setFirstName("TriPay");
			detail.setMiddleName("_");
			detail.setLastName("Settlement");
			detail.setEmail(StartupUtil.SETTLEMENT);
			userDetailRepository.save(detail);
			PQAccountDetail pqAccountDetail = new PQAccountDetail();
			pqAccountDetail.setBalance(0);
			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
			pqAccountDetail.setAccountType(acTypeKYC);
			pqAccountDetailRepository.save(pqAccountDetail);
			settlement = new User();
			settlement.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
			settlement.setPassword(passwordEncoder.encode("123456789"));
			settlement.setCreated(new Date());
			settlement.setMobileStatus(Status.Active);
			settlement.setEmailStatus(Status.Active);
			settlement.setUserType(UserType.Admin);
			settlement.setUsername(detail.getContactNo());
			settlement.setUserDetail(detail);
			settlement.setAccountDetail(pqAccountDetail);
			userRepository.save(settlement);
		}

		User commission = userRepository.findByUsername(StartupUtil.COMMISSION);
		if (commission == null) {
			UserDetail detail = new UserDetail();
			detail.setAddress("JP Nagar");
			detail.setContactNo(StartupUtil.COMMISSION);
			detail.setFirstName("TriPay");
			detail.setMiddleName("_");
			detail.setLastName("Settlement");
			detail.setEmail(StartupUtil.COMMISSION);
			userDetailRepository.save(detail);

			PQAccountDetail pqAccountDetail = new PQAccountDetail();
			pqAccountDetail.setBalance(0);
			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
			pqAccountDetail.setAccountType(acTypeKYC);
			pqAccountDetailRepository.save(pqAccountDetail);

			commission = new User();
			commission.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
			commission.setPassword(passwordEncoder.encode("123456789"));
			commission.setCreated(new Date());
			commission.setMobileStatus(Status.Active);
			commission.setEmailStatus(Status.Active);
			commission.setUserType(UserType.Admin);
			commission.setUsername(detail.getContactNo());
			commission.setUserDetail(detail);
			commission.setAccountDetail(pqAccountDetail);
			userRepository.save(commission);
		}

//		User travel = userRepository.findByUsername(StartupUtil.TRAVEL);
//		if (travel == null) {
//			UserDetail detail = new UserDetail();
//			detail.setAddress("JP Nagar");
//			detail.setContactNo(StartupUtil.TRAVEL);
//			detail.setFirstName("VPayQwik");
//			detail.setMiddleName("_");
//			detail.setLastName("Travel");
//			detail.setEmail(StartupUtil.TRAVEL);
//			userDetailRepository.save(detail);
//
//			PQAccountDetail pqAccountDetail = new PQAccountDetail();
//			pqAccountDetail.setBalance(0);
//			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
//			pqAccountDetail.setAccountType(acTypeKYC);
//			pqAccountDetailRepository.save(pqAccountDetail);
//
//			travel = new User();
//			travel.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
//			travel.setPassword(passwordEncoder.encode("123456789"));
//			travel.setCreated(new Date());
//			travel.setMobileStatus(Status.Active);
//			travel.setEmailStatus(Status.Active);
//			travel.setUserType(UserType.Locked);
//			travel.setUsername(detail.getContactNo());
//			travel.setUserDetail(detail);
//			travel.setAccountDetail(pqAccountDetail);
//			userRepository.save(travel);
//		}
//
//
//		User bank = userRepository.findByUsername(StartupUtil.BANK);
//		if (bank == null) {
//			UserDetail detail = new UserDetail();
//			detail.setAddress("JP Nagar");
//			detail.setContactNo(StartupUtil.BANK);
//			detail.setFirstName("VPayQwik");
//			detail.setMiddleName("_");
//			detail.setLastName("Bank");
//			detail.setEmail(StartupUtil.BANK);
//			userDetailRepository.save(detail);
//
//			PQAccountDetail pqAccountDetail = new PQAccountDetail();
//			pqAccountDetail.setBalance(0);
//			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
//			pqAccountDetail.setAccountType(acTypeKYC);
//			pqAccountDetailRepository.save(pqAccountDetail);
//
//			bank = new User();
//			bank.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
//			bank.setPassword(passwordEncoder.encode("@dm!nVP@yQw!k"));
//			bank.setCreated(new Date());
//			bank.setMobileStatus(Status.Active);
//			bank.setEmailStatus(Status.Active);
//			bank.setUserType(UserType.Admin);
//			bank.setUsername(detail.getContactNo());
//			bank.setUserDetail(detail);
//			bank.setAccountDetail(pqAccountDetail);
//			userRepository.save(bank);
//		}
//
//
//		User vatUser = userRepository.findByUsername(StartupUtil.VAT);
//		if (vatUser == null) {
//			UserDetail detail = new UserDetail();
//			detail.setAddress("Trinity Circle");
//			detail.setContactNo(StartupUtil.VAT);
//			detail.setFirstName("VPayQwik");
//			detail.setMiddleName("_");
//			detail.setLastName("VAT");
//			detail.setEmail(StartupUtil.VAT);
//			userDetailRepository.save(detail);
//
//			PQAccountDetail pqAccountDetail = new PQAccountDetail();
//			pqAccountDetail.setBalance(0);
//			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
//			pqAccountDetail.setAccountType(acTypeKYC);
//			pqAccountDetailRepository.save(pqAccountDetail);
//
//			vatUser = new User();
//			vatUser.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
//			vatUser.setPassword(passwordEncoder.encode("@dm!nVP@yQw!k"));
//			vatUser.setCreated(new Date());
//			vatUser.setMobileStatus(Status.Active);
//			vatUser.setEmailStatus(Status.Active);
//			vatUser.setUserType(UserType.Locked);
//			vatUser.setUsername(detail.getContactNo());
//			vatUser.setUserDetail(detail);
//			vatUser.setAccountDetail(pqAccountDetail);
//			userRepository.save(vatUser);
//		}
//
//		User kkCess = userRepository.findByUsername(StartupUtil.KRISHI_KALYAN);
//		if (kkCess == null) {
//			UserDetail detail = new UserDetail();
//			detail.setAddress("Trinity Circle");
//			detail.setContactNo(StartupUtil.KRISHI_KALYAN);
//			detail.setFirstName("VPayQwik");
//			detail.setMiddleName("_");
//			detail.setLastName("Krishi Kalyan Cess");
//			detail.setEmail(StartupUtil.KRISHI_KALYAN);
//			userDetailRepository.save(detail);
//
//			PQAccountDetail pqAccountDetail = new PQAccountDetail();
//			pqAccountDetail.setBalance(0);
//			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
//			pqAccountDetail.setAccountType(acTypeKYC);
//			pqAccountDetailRepository.save(pqAccountDetail);
//
//			kkCess = new User();
//			kkCess.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
//			kkCess.setPassword(passwordEncoder.encode("@dm!nVP@yQw!k"));
//			kkCess.setCreated(new Date());
//			kkCess.setMobileStatus(Status.Active);
//			kkCess.setEmailStatus(Status.Active);
//			kkCess.setUserType(UserType.Locked);
//			kkCess.setUsername(detail.getContactNo());
//			kkCess.setUserDetail(detail);
//			kkCess.setAccountDetail(pqAccountDetail);
//			userRepository.save(kkCess);
//		}
//
//
//
//		User sbCess = userRepository.findByUsername(StartupUtil.SWACCH_BHARAT);
//		if (sbCess == null) {
//			UserDetail detail = new UserDetail();
//			detail.setAddress("Trinity Circle");
//			detail.setContactNo(StartupUtil.SWACCH_BHARAT);
//			detail.setFirstName("VPayQwik");
//			detail.setMiddleName("_");
//			detail.setLastName("Swacch Bharat Cess");
//			detail.setEmail(StartupUtil.SWACCH_BHARAT);
//			userDetailRepository.save(detail);
//
//			PQAccountDetail pqAccountDetail = new PQAccountDetail();
//			pqAccountDetail.setBalance(0);
//			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
//			pqAccountDetail.setAccountType(acTypeKYC);
//			pqAccountDetailRepository.save(pqAccountDetail);
//
//			sbCess = new User();
//			sbCess.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
//			sbCess.setPassword(passwordEncoder.encode("@dm!nVP@yQw!k"));
//			sbCess.setCreated(new Date());
//			sbCess.setMobileStatus(Status.Active);
//			sbCess.setEmailStatus(Status.Active);
//			sbCess.setUserType(UserType.Locked);
//			sbCess.setUsername(detail.getContactNo());
//			sbCess.setUserDetail(detail);
//			sbCess.setAccountDetail(pqAccountDetail);
//			userRepository.save(sbCess);
//		}
//
//		User mVisa = userRepository.findByUsername(StartupUtil.MVISA);
//		if (mVisa == null) {
//
//			UserDetail detail = new UserDetail();
//			detail.setAddress("Trinity Circle");
//			detail.setContactNo("7022620747");
//			detail.setFirstName("VPayQwik");
//			detail.setMiddleName("_");
//			detail.setLastName("mVisa");
//			detail.setEmail(StartupUtil.MVISA);
//			userDetailRepository.save(detail);
//
//			PQAccountDetail pqAccountDetail = new PQAccountDetail();
//			pqAccountDetail.setBalance(0);
//			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
//			pqAccountDetail.setAccountType(acTypeKYC);
//			pqAccountDetailRepository.save(pqAccountDetail);
//
//			mVisa = new User();
//			mVisa.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
//			mVisa.setPassword(passwordEncoder.encode("@dm!nVP@yQw!k"));
//			mVisa.setCreated(new Date());
//			mVisa.setMobileStatus(Status.Active);
//			mVisa.setEmailStatus(Status.Active);
//			mVisa.setUserType(UserType.Locked);
//			mVisa.setUsername(detail.getEmail());
//			mVisa.setUserDetail(detail);
//			mVisa.setAccountDetail(pqAccountDetail);
//			userRepository.save(mVisa);
//
//		}

		User instantPay = userRepository.findByUsername(InstantPayConstants.USERNAME);
		if (instantPay == null) {
			UserDetail detail = new UserDetail();
			detail.setAddress("Koramangala");
			detail.setContactNo("1234567890");
			detail.setFirstName("Instant");
			detail.setMiddleName("P");
			detail.setLastName("Pay");
			detail.setEmail(InstantPayConstants.USERNAME);
			userDetailRepository.save(detail);

			PQAccountDetail pqAccountDetail = new PQAccountDetail();
			pqAccountDetail.setBalance(0);
			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
			pqAccountDetail.setAccountType(acTypeKYC);
			pqAccountDetailRepository.save(pqAccountDetail);

			instantPay = new User();
			instantPay.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
			instantPay.setPassword(passwordEncoder.encode("!nst@ntP@y"));
			instantPay.setCreated(new Date());
			instantPay.setMobileStatus(Status.Active);
			instantPay.setEmailStatus(Status.Active);
			instantPay.setUserType(UserType.Locked);
			instantPay.setUsername(detail.getEmail());
			instantPay.setUserDetail(detail);
			instantPay.setAccountDetail(pqAccountDetail);
			userRepository.save(instantPay);
		}


		for (int i = 0; i < 5; i++) {
			String username = "";
			String email = "";
			String firstName = "";
			String lastName = "";
			String date = "";
			String mpin = "";
			switch (i) {
			case 0:
				username = "9740116671";
				email = "prajun.adhikary@gmail.com";
				firstName = "Prajun";
				lastName = "Adhikary";
				date = "1984-03-14";
				mpin = "1984";
				break;
			case 1:
				username = "8769986881";
				email = "vibhanshuvyas60@gmail.com";
				firstName = "Vibhanshu";
				lastName = "Vyas";
				date = "1994-07-19";
				mpin = "4585";
				break;
			case 2:
				username = "9461553581";
				email = "gupta.gaurav@gmail.com";
				firstName = "Gaurav";
				lastName = "Gupta";
				date = "1985-07-21";
				mpin = "8775";
				break;
			case 3:
				username = "9449147913";
				email = "ajaysharmajkm@gmail.com";
				firstName = "Ajay";
				lastName = "Sharma";
				date = "1987-08-28";
				mpin = "1987";
				break;
			default:
				username = "7022620747";
				email = "kumar.pankaj11@gmail.com";
				firstName = "Pankaj";
				lastName = "Kumar";
				date = "1985-04-13";
				mpin = "1994";
				break;
			}

			User user = userRepository.findByUsername(username);
			if (user == null) {
				UserDetail detail = new UserDetail();
				detail.setAddress("Trinity Circle,Bangalore");
				detail.setContactNo(username);
				detail.setFirstName(firstName);
				detail.setMiddleName(" ");
				detail.setLastName(lastName);
				detail.setEmail(email);
				detail.setGender(Gender.M);
				try {
					detail.setMpin(SecurityUtil.sha512(mpin));
					detail.setDateOfBirth(sdf.parse(date));
				} catch (Exception e) {
					e.printStackTrace();
				}
				userDetailRepository.save(detail);
				PQAccountDetail pqAccountDetail = new PQAccountDetail();
				pqAccountDetail.setBalance(100);
				pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
				pqAccountDetail.setAccountType(acTypeNonKYC);
				pqAccountDetailRepository.save(pqAccountDetail);

				user = new User();
				user.setAuthority(Authorities.USER + "," + Authorities.AUTHENTICATED);
				user.setPassword(passwordEncoder.encode("123456789"));
				user.setCreated(new Date());
				user.setMobileStatus(Status.Active);
				user.setEmailStatus(Status.Active);
				user.setUserType(UserType.User);
				user.setUsername(detail.getContactNo());
				user.setUserDetail(detail);
				user.setAccountDetail(pqAccountDetail);
				userRepository.save(user);
			}

			User promoCode = userRepository.findByUsername(StartupUtil.PROMO_CODE);
			if (promoCode == null) {
				UserDetail detail = new UserDetail();
				detail.setAddress("BTM");
				detail.setFirstName("Promo");
				detail.setMiddleName(" ");
				detail.setLastName("Code");
				detail.setEmail(StartupUtil.PROMO_CODE);
				detail.setContactNo(StartupUtil.PROMO_CODE);
				userDetailRepository.save(detail);

				PQAccountDetail pqAccountDetail = new PQAccountDetail();
				pqAccountDetail.setBalance(10000);
				pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());

				pqAccountDetail.setAccountType(acTypeKYC);
				pqAccountDetailRepository.save(pqAccountDetail);

				promoCode = new User();
				promoCode.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
				promoCode.setPassword(passwordEncoder.encode("12345678"));
				promoCode.setCreated(new Date());
				promoCode.setMobileStatus(Status.Active);
				promoCode.setEmailStatus(Status.Active);
				promoCode.setUserType(UserType.Locked);
				promoCode.setUsername(detail.getContactNo());
				promoCode.setUserDetail(detail);
				promoCode.setAccountDetail(pqAccountDetail);
				userRepository.save(promoCode);
			}
		}


		User refundManager = userRepository.findByUsername(StartupUtil.REFUND);
		if (refundManager == null) {
			UserDetail detail = new UserDetail();
			detail.setAddress("TRINITY CIRCLE");
			detail.setFirstName("REFUND");
			detail.setMiddleName(" ");
			detail.setLastName("MANAGER");
			detail.setEmail(StartupUtil.REFUND);
			detail.setContactNo(StartupUtil.REFUND);
			userDetailRepository.save(detail);
			PQAccountDetail pqAccountDetail = new PQAccountDetail();
			pqAccountDetail.setBalance(0);
			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
			pqAccountDetail.setAccountType(acTypeKYC);
			pqAccountDetailRepository.save(pqAccountDetail);
			refundManager = new User();
			refundManager.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
			refundManager.setPassword(passwordEncoder.encode("12345678"));
			refundManager.setCreated(new Date());
			refundManager.setMobileStatus(Status.Active);
			refundManager.setEmailStatus(Status.Active);
			refundManager.setUserType(UserType.Locked);
			refundManager.setUsername(detail.getContactNo());
			refundManager.setUserDetail(detail);
			refundManager.setAccountDetail(pqAccountDetail);
			userRepository.save(refundManager);
		}


		User riskManager = userRepository.findByUsername(StartupUtil.RISK);
		if (riskManager == null) {
			UserDetail detail = new UserDetail();
			detail.setAddress("BTM");
			detail.setFirstName("Risk");
			detail.setMiddleName(" ");
			detail.setLastName("Manager");
			detail.setEmail(StartupUtil.RISK);
			detail.setContactNo(StartupUtil.RISK);
			userDetailRepository.save(detail);

			PQAccountDetail pqAccountDetail = new PQAccountDetail();
			pqAccountDetail.setBalance(10000);
			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());

			pqAccountDetail.setAccountType(acTypeKYC);
			pqAccountDetailRepository.save(pqAccountDetail);

			riskManager = new User();
			riskManager.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
			riskManager.setPassword(passwordEncoder.encode("123456789"));
			riskManager.setCreated(new Date());
			riskManager.setMobileStatus(Status.Active);
			riskManager.setEmailStatus(Status.Active);
			riskManager.setUserType(UserType.Locked);
			riskManager.setUsername(detail.getContactNo());
			riskManager.setUserDetail(detail);
			riskManager.setAccountDetail(pqAccountDetail);
			userRepository.save(riskManager);
		}
		
//		User mBank = userRepository.findByUsername(StartupUtil.MBANK);
//		if (mBank == null) {
//			UserDetail detail = new UserDetail();
//			detail.setAddress("Trinity Circle");
//			detail.setContactNo(StartupUtil.MBANK);
//			detail.setFirstName("VPayQwik");
//			detail.setMiddleName("_");
//			detail.setLastName("Merchant's Bank transfer");
//			detail.setEmail(StartupUtil.MBANK);
//			userDetailRepository.save(detail);
//
//			PQAccountDetail pqAccountDetail = new PQAccountDetail();
//			pqAccountDetail.setBalance(0);
//			pqAccountDetail.setAccountNumber(TriPayUtil.BASE_ACCOUNT_NUMBER + detail.getId());
//			pqAccountDetail.setAccountType(acTypeKYC);
//			pqAccountDetailRepository.save(pqAccountDetail);
//
//			sbCess = new User();
//			sbCess.setAuthority(Authorities.LOCKED + "," + Authorities.AUTHENTICATED);
//			sbCess.setPassword(passwordEncoder.encode("@dm!nVP@yQw!k"));
//			sbCess.setCreated(new Date());
//			sbCess.setMobileStatus(Status.Active);
//			sbCess.setEmailStatus(Status.Active);
//			sbCess.setUserType(UserType.Locked);
//			sbCess.setUsername(detail.getContactNo());
//			sbCess.setUserDetail(detail);
//			sbCess.setAccountDetail(pqAccountDetail);
//			userRepository.save(sbCess);
//		}
	}


	private boolean isRawMpin(String mpin) {
		boolean valid = false;
		if (!CommonValidation.isNull(mpin) && CommonValidation.checkLength4(mpin) && CommonValidation.isNumeric(mpin)) {
			valid = true;
		}
		return valid;
	}
}
