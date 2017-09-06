package com.tripayapp.api.impl;

import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.*;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.StartupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import com.tripayapp.api.ISendMoneyApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.util.ClientException;
import com.tripayapp.util.ConvertUtil;

import java.util.Date;
import java.util.List;

public class SendMoneyApi implements ISendMoneyApi, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;

	private final ITransactionApi transactionApi;
	private final IUserApi userApi;
	private final BankTransferRepository bankTransferRepository;
	private final BanksRepository banksRepository;
	private final BankDetailRepository bankDetailRepository;
	private final PGDetailsRepository pgDetailsRepository;
	private final PQServiceRepository pqServiceRepository;
	private final MBankTransferRepository mBankTransferRepository;
	
	public SendMoneyApi(ITransactionApi transactionApi, IUserApi userApi,BankTransferRepository bankTransferRepository,
			BanksRepository banksRepository,BankDetailRepository bankDetailRepository,
			PGDetailsRepository pgDetailsRepository,PQServiceRepository pqServiceRepository,
			MBankTransferRepository mBankTransferRepository ) {
		this.transactionApi = transactionApi;
		this.userApi = userApi;
		this.bankTransferRepository = bankTransferRepository;
		this.banksRepository = banksRepository;
		this.bankDetailRepository = bankDetailRepository;
		this.pgDetailsRepository = pgDetailsRepository;
		this.pqServiceRepository = pqServiceRepository;
		this.mBankTransferRepository = mBankTransferRepository;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public String sendMoneyMobile(SendMoneyMobileDTO dto, String username, PQService service) {
		String transactionRefNo = System.currentTimeMillis() + "";
		transactionApi.initiateSendMoney(Double.parseDouble(dto.getAmount()),
				"Send Money Rs " + dto.getAmount() + " to " + dto.getMobileNumber() + " from " + username + "|"
						+ dto.getMessage(),
				service, transactionRefNo, username,
				dto.getMobileNumber(), dto.toJSON().toString());
		transactionApi.successSendMoney(transactionRefNo);
		return null;
	}

	@Override
	public String sendMoneyBankInitiate(SendMoneyBankDTO dto, String username, PQService service) {
		String transactionRefNo = System.currentTimeMillis() + "";
		transactionApi.initiateBankTransfer(Double.parseDouble(dto.getAmount()), "Send money to bank", service,
				transactionRefNo, username, StartupUtil.BANK, dto.toJSON().toString());
		Banks bank = banksRepository.findByCode(dto.getBankCode());
		BankDetails details = bankDetailRepository.findByIfscCode(dto.getIfscCode(),bank);
		User u = userApi.findByUserName(username);
		BankTransfer transfer = ConvertUtil.bankTransfer(dto,transactionRefNo,details,u);
		bankTransferRepository.save(transfer);
		sendMoneyBankSuccess(transactionRefNo);
		return null;
	}
	
	@Override
	public String sendMoneyBankFailed(String transactionRefNo) {
		transactionApi.failedBankTransfer(transactionRefNo);
		return null;
	}
	
	@Override
	public String sendMoneyBankSuccess(String transactionRefNo) {
		transactionApi.successBankTransfer(transactionRefNo);
		return null;
	}

	@Override
	public String sendMoneyStore(PayAtStoreDTO dto, String username, PQService service) {
		String transactionRefNo = System.currentTimeMillis() + "";
		transactionApi.initiateMerchantPayment(Double.parseDouble(dto.getAmount()),
				"Send Money Rs " + dto.getAmount() + " to " + dto.getServiceProvider() + " from " + username, service,
				transactionRefNo, username, dto.getServiceProvider(), dto.toJSON().toString(),false);
		transactionApi.successSendMoney(transactionRefNo);
		return null;
	}

	@Override
	public String prepareSendMoney(String username) {
		User receiverUser = userApi.findByUserName(username);
		String serviceCode = "SMR";
		if (receiverUser == null) {
			serviceCode = "SMU";
		} else {
			serviceCode = "SMR";
		}

		if (receiverUser == null) {
			RegisterDTO dto = new RegisterDTO();
			dto.setFirstName(" ");
			dto.setLastName(" ");
			dto.setMiddleName(" ");
			dto.setEmail("");
			dto.setContactNo(username);
			dto.setAddress("");
			dto.setLocationCode("");
			dto.setUserType(UserType.User);
			dto.setUsername(username);
			dto.setPassword("");
			dto.setConfirmPassword("");
			try {
				userApi.saveUnregisteredUserSendMoney(dto);
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}
		return serviceCode;
	}

	@Override
	public ResponseDTO preparePayStore(PayStoreDTO dto,User u) {
		ResponseDTO result = new ResponseDTO();
		long id = dto.getId();
		User merchant = userApi.findById(id);
		if(merchant != null){
			String authority = merchant.getAuthority();
			if(authority != null) {
				if (authority.contains(Authorities.MERCHANT) && authority.contains(Authorities.AUTHENTICATED)) {
						PGDetails merchantDetails = pgDetailsRepository.findByUser(merchant);
						if(merchantDetails != null) {
							boolean usingStore = merchantDetails.isStore();
							if (usingStore) {
								PQService merchantService = merchantDetails.getService();
								if (merchantService != null) {
									UserDetail merchantDetail = merchant.getUserDetail();
									if (merchantDetail != null) {
										String transactionRefNo = System.currentTimeMillis() + "";
										String description = "Payment of " + dto.getNetAmount() + " to " + merchantDetail.getFirstName();
										transactionApi.initiateMerchantPayment(dto.getNetAmount(), description, merchantService, transactionRefNo, u.getUsername(), merchant.getUsername(), dto.toJSON().toString(), false);
										transactionApi.successMerchantPayment(transactionRefNo, false);
										result.setStatus(ResponseStatus.SUCCESS);
										result.setMessage("Payment Successful");
										result.setDetails(dto.getNetAmount() + " successfully transferred to " + merchantDetail.getFirstName() + " \n VPayQwik Transaction ID : " + transactionRefNo);
									}
								} else {
									result.setStatus(ResponseStatus.FAILURE);
									result.setMessage("Service not defined for this merchant");
								}
							}else {
								result.setStatus(ResponseStatus.FAILURE);
								result.setMessage("Not authorized to use as store");
							}
						}
				}else {
					result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
					result.setMessage("Not a Valid Role");
				}
			}else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Authority is undefined");
			}
		}else {
			result.setStatus(ResponseStatus.FAILURE);
			result.setMessage("Merchant Not Found");
		}
		return result;
	}

	@Override
	public ResponseDTO refundMoneyToAccount(RefundDTO dto, User u) {
		ResponseDTO result = new ResponseDTO();
		PQTransaction transaction = transactionApi.processRefundTransaction(dto.getTransactionRefNo());
		if(transaction != null) {
			if(transaction.getStatus().equals(Status.Refunded)) {
				PQAccountDetail account = u.getAccountDetail();
				double balance = account.getBalance();
				if (account != null) {
					if (balance >= dto.getNetAmount()) {
						PQService service = pqServiceRepository.findServiceByCode("RMU");
						if (service != null) {
							refundMoney(dto, u, service);
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Amount successfully refunded");
						} else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("Service Not Found");
						}
					} else {
						result.setStatus(ResponseStatus.FAILURE);
						result.setMessage("Balance is " + balance + " which is less than " + dto.getNetAmount());
					}
				} else {
					result.setStatus(ResponseStatus.FAILURE);
					result.setMessage("User Account Not Available");
				}
			}else {
				result.setStatus(ResponseStatus.FAILURE);
				result.setMessage("Please try again later");
			}
		}else {
			result.setStatus(ResponseStatus.FAILURE);
			result.setMessage("Transaction Not Found");
		}

		return result;
	}

	@Override
	public void refundMoney(RefundDTO dto, User u, PQService service) {
		String refundUser = "refund@msewa.com";
		String transactionRefNo = System.currentTimeMillis()+"";
		String description = "Refund from " +u.getUsername()+ " to " +refundUser;
		transactionApi.initiateSendMoney(dto.getNetAmount(),description,service,transactionRefNo,u.getUsername(),refundUser,String.valueOf(dto.toJSON()));
		transactionApi.successSendMoney(transactionRefNo);
	}
	
	@Override
	public SendMoneyResponse sendMoneyMBankInitiate(SendMoneyBankDTO dto, String username, PQService service) {
		SendMoneyResponse resp = new SendMoneyResponse();
		String transactionRefNo = System.currentTimeMillis() + "";
		transactionApi.initiateMBankTransfer(Double.parseDouble(dto.getAmount()), "NEFT to Merchant Bank A/C", service,
				transactionRefNo, username, StartupUtil.MBANK, dto.toJSON().toString());
			Banks bank = banksRepository.findByCode(dto.getBankCode());
			BankDetails details = null;
			details = bankDetailRepository.findByIfscCode(dto.getIfscCode(),bank);
			User u = userApi.findByUserName(username);
			MBankTransfer transfer = ConvertUtil.mBankTransfer(dto,transactionRefNo,details,u);
			mBankTransferRepository.save(transfer);
			sendMoneyBankSuccess(transactionRefNo);
			resp.setValid(true);
		return resp;
	}

	@Override
	public SendMoneyResponse checkInputParameters(SendMoneyBankDTO dto) {
		SendMoneyResponse resp = new SendMoneyResponse();
		double amount = Double.parseDouble(dto.getAmount());
		String message = "";
		Banks bank = banksRepository.findByCode(dto.getBankCode());
		boolean valid =  true;
		if(amount < 500) {
			message = "You cannot transfer amount below 500/-";
			valid = false;
		} 
		if(bank == null) {
			valid = false;
			message = "Not a valid Bank";
			
		} else {
			BankDetails details = bankDetailRepository.findByIfscCode(dto.getIfscCode(),bank);
			if(details == null) {
				message = "Not a valid IFSC Code of Bank "+bank.getName();
				valid = false;
			}
		}
		resp.setValid(valid);
		resp.setMessage(message);
		return resp;
	}

}
