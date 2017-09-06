package com.tripayapp.api.impl;

import com.tripayapp.entity.*;
import com.tripayapp.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tripayapp.api.IAdminApi;
import com.tripayapp.api.IMerchantApi;
import com.tripayapp.model.PromoCodeDTO;
import com.tripayapp.model.RegisterDTO;

import java.util.List;

public class AdminApi implements IAdminApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final IMerchantApi merchantApi;
	private final MessageLogRepository messageLogRepository;
	private final EmailLogRepository emailLogRepository;
	private final UserRepository userRepository;
	private final PGDetailsRepository pgDetailsRepository;
	private final BankTransferRepository bankTransferRepository;
	private final PQTransactionRepository pqTransactionRepository;
	private final MBankTransferRepository mBankTransferRepository;
	public AdminApi(IMerchantApi merchantApi, MessageLogRepository messageLogRepository, EmailLogRepository emailLogRepository,
			UserRepository userRepository,PGDetailsRepository pgDetailsRepository,BankTransferRepository bankTransferRepository,
			PQTransactionRepository pqTransactionRepository, MBankTransferRepository mBankTransferRepository) {
		this.merchantApi = merchantApi;
		this.messageLogRepository = messageLogRepository;
		this.emailLogRepository = emailLogRepository;
		this.userRepository = userRepository;
		this.pgDetailsRepository = pgDetailsRepository;
		this.bankTransferRepository = bankTransferRepository;
		this.pqTransactionRepository = pqTransactionRepository;
		this.mBankTransferRepository = mBankTransferRepository;
	}

	@Override
	public Page<MessageLog> getMessageLogs(Pageable pageable) {
		return messageLogRepository.getAllMessage(pageable);
	}

	@Override
	public Page<EmailLog> getEmailLogs(Pageable pageable) {
		return emailLogRepository.getAllEmails(pageable);
	}

	@Override
	public Page<User> getOnlineUsers(Pageable pagable) {
		return null;
	}

	@Override
	public Page<User> getActiveUsers(Pageable pageable) {
		return null;
	}

	@Override
	public Page<User> getBlockedUsers(Pageable pageable) {
		return null;
	}

	@Override
	public Page<User> getInactiveUsers(Pageable pageable) {
		return null;
	}

	@Override
	public List<BankTransfer> getAllTransferReports() {
		return (List<BankTransfer>) bankTransferRepository.findAll();
	}

	@Override
	public PQTransaction getTransactionByRefNo(String transactionRefNo) {
		return pqTransactionRepository.findByTransactionRefNo(transactionRefNo);
	}

	@Override
	public void saveMerchant(RegisterDTO user) {
//		merchantApi.addMerchant(user);
	}

	@Override
	public void savePromoCode(PromoCodeDTO request) {
		
	}
	
	@Override
	public List<MBankTransfer> getAllMBankTransferReports() {
		return (List<MBankTransfer>) mBankTransferRepository.findAll();
	}

}
