package com.tripayapp.api;

import java.util.Date;
import java.util.List;

import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.mobile.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITransactionApi {

	PQTransaction findByTransactionRefNoAndStatus(String transactionReferenceNumber, Status status);
	List<PQTransaction> getMonthlyTransaction(int month);
	List<PQTransaction> getDailyTransactionBetweeen(Date from, Date to);
	List<PQTransaction> getDailyTransactionBetweenForAccount(Date from, Date to, PQAccountDetail account);
	List<PQTransaction> getDailyTransactionByDate(Date from);
	List<PQTransaction> getDailyTransactionByDateForAccount(Date from, PQAccountDetail account);
	List<PQTransaction> listTransactionByPaging(PagingDTO dto);
	List<PQTransaction> listTransactionByServiceAndDate(Date from,Date to,PQServiceType serviceType,boolean debit,TransactionType transactionType,Status status);

	Long countDailyTransactionByDateForAccount(Date from, PQAccountDetail account);
	Long countDailyTransactionByDate(Date from);
	double countTotalReceivedEBS();
	double countTotalReceivedEBSNow();
	double countTotalReceivedVNET();
	double countTotalReceivedVNETNow();
	double countTotalPay();
	double countTotalTopupNow();

	List<PQTransaction> getLoadMoneyTransactions(Status status);
	PQTransaction getLastReverseTransactionOfUser(PQAccountDetail account);
	PQTransaction getTransactionByRefNo(String transactionRefNo);
	Date getLastTranasactionTimeStamp(PQAccountDetail account);
	Date getLastTranasactionTimeStampByStatus(PQAccountDetail account,Status status);
	Page<PQTransaction> getTotalTransactions(Pageable page);
	PQTransaction processRefundTransaction(String transactionRefNo);
	Page<PQTransaction> getTotalTransactionsOfUser(Pageable page,String user);
	List<PQTransaction> transactionListByService(PQService service);
	List<PQTransaction> transactionListByServiceAndDebit(PQService service,boolean debit);
	List<PQTransaction> getTotalSuccessfulTransactionsOfUser(String user);

	PQTransaction getLastTransactionsOfUser(User user);

	void revertSendMoneyOperations();
	int updateFavouriteTransaction(String transactionRefNo,boolean favourite);
	void initiateSendMoney(double amount, String description, PQService service, String transactionRefNo, String senderUsername, String receiverUsername, String json);
	void successSendMoney(String transactionRefNo);
	void failedSendMoney(String transactionRefNo);

	void initiateSharePoints(long points, String description, String transactionRefNo, String senderUsername, String receiverUsername, String json);
	void successSharePoints(String transactionRefNo);

	void initiateMerchantPayment(double amount, String description, PQService service, String transactionRefNo, String senderUsername, String receiverUsername, String json,boolean isPG);
	void successMerchantPayment(String transactionRefNo,boolean isPG);
	void failedMerchantPayment(String transactionRefNo);
	OnePayResponse getTransaction(String transactionRefNo);

	void initiateVisaPayment(double amount, String description, PQService service, String transactionRefNo, String senderUsername, String json);
	void successVisaPayment(String transactionRefNo);
	void failedVisaPayment(String transactionRefNo);

	void initiateBillPayment(double amount, String description, PQService service, String transactionRefNo, String senderUsername, String receiverUsername, String json);
	void successBillPayment(String transactionRefNo);
	void failedBillPayment(String transactionRefNo);
	
	void initiateLoadMoney(double amount, String description, PQService service, String transactionRefNo, String senderUsername, String json);
	void successLoadMoney(String transactionRefNo);
	void failedLoadMoney(String transactionRefNo);
	
	void initiateBankTransfer(double amount, String description, PQService service, String transactionRefNo, String sender, String receiverUsername, String json);
	void successBankTransfer(String transactionRefNo);
	void failedBankTransfer(String transactionRefNo);
	
	void initiatePromoCode(double amount, String description, String transactionRefNo, PQService service,PromoCode promoCode, String senderUsername, String receiverUsername);
	void successPromoCode(String transactionRefNo);
	void failedPromoCode(String transactionRefNo);
	PQTransaction saveOrUpdate(PQTransaction transaction);
	void initiatePromoCodeOld(double amount, String description, PQService service, String transactionRefNo, String senderUsername, String receiverUsername, String json);
	void successPromoCodeOld(String transactionRefNo);
	void failedPromoCodeOld(String transactionRefNo);

	List<PQTransaction> findTransactionByAccount(Date from, Date to, PQAccountDetail account, double amount);
	List<PQTransaction> findTransactionByAccount(PQAccountDetail account);
	Page<PQTransaction> findByAccount(Pageable page,PQAccountDetail account);
	Page<PQTransaction> findByService(Pageable page,PQService service);

	List<PQTransaction> findTransactionByAccountAndAmount(Date from, Date to, PQAccountDetail account, double amount);
	void revertFailedBillPayment();
	List<PQTransaction> getTotalTransactions();
	
	List<PQTransaction> getDailyDebitTransaction(PQAccountDetail account);
	List<PQTransaction> getMonthlyDebitTransaction(PQAccountDetail account);
	
	List<PQTransaction> getDailyCreditTransation(PQAccountDetail account);
	List<PQTransaction> getMonthlyCreditTransation(PQAccountDetail account);
	
	List<PQTransaction> getDailyCreditAndDebitTransation(PQAccountDetail account);
	List<PQTransaction> getMonthlyCreditAndDebitTransation(PQAccountDetail account);
	
	double getDailyDebitTransactionTotalAmount(PQAccountDetail account);
	double getMonthlyDebitTransactionTotalAmount(PQAccountDetail account);
	
	double getDailyCreditTransationTotalAmount(PQAccountDetail account);
	double getMonthlyCreditTransationTotalAmount(PQAccountDetail account);
	
	double getDailyCreditAndDebitTransationTotalAmount(PQAccountDetail account);
	double getMonthlyCreditAndDebitTransationTotalAmount(PQAccountDetail account);

	void successBillPaymentNew(String transactionRefNo);
	
	void failedBillPaymentNew(String transactionRefNo);
	
	double getLastSuccessTransaction(PQAccountDetail account);

	void reverseTransaction(String transactionRefNo);

	void initiateBusBooking(double amount, String description, PQService service, String transactionRefNo, String sender,
			String receiver, String json);
	
	void successTravelBus(String transactionRefNo);

//	void failedTravelBus(String transactionRefNo);
	
	void initiateMBankTransfer(double amount, String description, PQService service, String transactionRefNo, String sender, String receiverUsername, String json);

	List<PQTransaction> getTotalTransactionsOfMerchant(String merchant);
}
