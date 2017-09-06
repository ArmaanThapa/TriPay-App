package com.tripayapp.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.mobile.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.tripayapp.util.ClientException;

public interface IUserApi {

	List<UserDTO> findAllUser();

	double getWalletBalance(User u);

	boolean isVerified(KycDTO dto,User u);

	PGDetails findMerchantByMobile(String mobileNumber);

	ArrayList<Long> getDailyTransactionsCountFromDate();

	ArrayList<Double> getDailyTransactionsAmountFromDate();

	ResponseDTO requestOfflinePayment(PaymentRequestDTO dto,User merchant,User user);

	void changePassword(ChangePasswordDTO dto, Long id);

	boolean updateAccountType(String username,boolean kyc);

	void toggleStatus(String username, Status status);

	KYCResponse verifyByKycApi(KycDTO dto);

	AmountDTO getAdminLoginValues();

	void saveUser(RegisterDTO dto) throws ClientException;

	boolean isVBankAccountSaved(KycDTO dto,PQAccountDetail account);

	boolean sendOTP(String mobileNumber,User user);

	boolean containsAccountAndMobile(String vbankAccount,String mobileNumber);

	void updateVBankAccountDetails(KYCResponse dto,VBankAccountDetail accountDetail,User user);

	void saveMerchant(MRegisterDTO dto) throws ClientException;

	void saveUnregisteredUserSendMoney(RegisterDTO dto) throws ClientException;

	User findByUserName(String name);

	User findById(long id);

	User findByAccountDetail(PQAccountDetail accountDetail);

	PQAccountDetail saveOrUpdateAccount(PQAccountDetail accountDetail);

	void changePasswordRequest(User u);

	UserDTO checkPasswordToken(String key);

	void revertAuthority();

	boolean resendMobileToken(String username);

	boolean activateEmail(String key);

	boolean checkMobileToken(String key, String mobileNumber);

	void renewPassword(ChangePasswordDTO dto);

	void editUser(RegisterDTO user);

	UserDTO getUserById(Long id);

	List<User> getUserListByPage(int page, int perPage);

	List<User> getUserListByVerifiedUsersPage(int page, int perPage);

	long getUserCount();

	double getTotalWalletBalance();

	String handleLoginFailure(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication, String loginUsername,String ipAddress);

	void handleLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication, String loginUsername,String ipAddress);

	String authenticateVersion(String version);

	int updatePoints(long points,PQAccountDetail account);

	int getLoginAttempts();

	int updateBalance(double amount, User user);

	double dailyTransactionTotal(User user);

	void blockUser(String username);

	double dailyDebitTransactionTotal(User user);

	double monthlyTransactionTotal(User user);

	double monthlyLoadMoneyTransactionTotal(User user);

	boolean saveImage(User user, String url);

	int updateVersion(int versionCode,int subVersionCode);

	List<PQTransaction> getUserTransactions(User user);

	void renewPasswordFromAccount(ChangePasswordDTO dto);

	void updateUserAuthority(String authority, long id);

	boolean setNewMpin(MpinDTO dto);

	boolean changeCurrentMpin(MpinChangeDTO dto);

	boolean deleteCurrentMpin(String username);

	Page<User> getTotalUsers(Pageable pageable);

	Page<User> getActiveUsers(Pageable pageable);

	Page<User> getInactiveUsers(Pageable pageable);

	Page<User> getBlockedUsers(Pageable pageable);

	Page<User> getLockedUsers(Pageable pageable);

	List<User> getAllUsers();

	int getTransactionTimeDifference();

	boolean inviteByMobile(String number, String message,User user);

	void inviteByEmail(String subject, String mailTemplate, User user);

	void inviteByEmailAddress(String subject, String mailTemplate, String email);

	void reSendEmailOTP(User user);
	
	boolean redeemCode(User user, String promoCode);

	boolean changeEmail(User user , String email);

	List<PQVersion> getAllVersions();

	PQVersion getLatestVersion();

	int updateGcmId(String gcmId,String username);

	User saveOrUpdateUser(User u);

	double getBalanceByUserAccount(PQAccountDetail accountDetail);

	LoginLog getLastLoginOfUser(User u,Status status);

	boolean isValidLastLoginDevice(String device,User user);

	void requestNewLoginDevice(User user);

	boolean isValidLoginToken(String otp,User user);
}
