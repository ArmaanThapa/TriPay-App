package com.tripayapp.api;

import com.tripayapp.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tripayapp.model.PromoCodeDTO;
import com.tripayapp.model.RegisterDTO;

import java.util.List;

public interface IAdminApi {

	Page<MessageLog> getMessageLogs(Pageable pageable);
	Page<EmailLog> getEmailLogs(Pageable pageable);
	Page<User> getOnlineUsers(Pageable pagable);
	Page<User> getActiveUsers(Pageable pageable);
	Page<User> getBlockedUsers(Pageable pageable);
	Page<User> getInactiveUsers(Pageable pageable);
	List<BankTransfer> getAllTransferReports();
	PQTransaction getTransactionByRefNo(String transactionRefNo);
	void saveMerchant(RegisterDTO user);
	void savePromoCode(PromoCodeDTO request);
	List<MBankTransfer> getAllMBankTransferReports();
	
}
