package com.tripayapp.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tripayapp.entity.*;
import com.tripayapp.mail.util.MailConstants;
import com.tripayapp.model.PagingDTO;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.repositories.*;
import com.tripayapp.util.StartupUtil;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ebs.util.EBSConstants;
import com.tripayapp.api.ICommissionApi;
import com.tripayapp.api.IMailSenderApi;
import com.tripayapp.api.ISMSSenderApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.mail.util.MailTemplate;
import com.tripayapp.model.OnePayResponse;
import com.tripayapp.model.Status;
import com.tripayapp.model.TransactionType;
import com.tripayapp.sms.util.SMSAccount;
import com.tripayapp.sms.util.SMSTemplate;
import com.tripayapp.util.CommonUtil;
import com.tripayapp.util.ConvertUtil;
import com.tripayapp.util.JSONParserUtil;

public class TransactionApi implements ITransactionApi, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;
	private final SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
	private final IUserApi userApi;
	private final PQTransactionRepository transactionRepository;
	private final ISMSSenderApi smsSenderApi;
	private final IMailSenderApi mailSenderApi;
	private final ICommissionApi commissionApi;
	private final PQAccountDetailRepository pqAccountDetailRepository;
	private final PQServiceRepository pqServiceRepository;
	private final SharePointsLogRepository sharePointsLogRepository;
	private final PGDetailsRepository pgDetailsRepository;

	public TransactionApi(IUserApi userApi, PQTransactionRepository transactionRepository, ISMSSenderApi smsSenderApi,
			IMailSenderApi mailSenderApi, ICommissionApi commissionApi,
			PQAccountDetailRepository pqAccountDetailRepository, PQServiceRepository pqServiceRepository,
			SharePointsLogRepository sharePointsLogRepository,PGDetailsRepository pgDetailsRepository) {
		this.userApi = userApi;
		this.transactionRepository = transactionRepository;
		this.smsSenderApi = smsSenderApi;
		this.mailSenderApi = mailSenderApi;
		this.commissionApi = commissionApi;
		this.pqAccountDetailRepository = pqAccountDetailRepository;
		this.pqServiceRepository = pqServiceRepository;
		this.sharePointsLogRepository = sharePointsLogRepository;
		this.pgDetailsRepository = pgDetailsRepository;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public PQTransaction findByTransactionRefNoAndStatus(String transactionReferenceNumber, Status status) {
		return transactionRepository.findByTransactionRefNoAndStatus(transactionReferenceNumber, status);
	}

	@Override
	public List<PQTransaction> getMonthlyTransaction(int month) {
		List<PQTransaction> pq = (List<PQTransaction>) transactionRepository.getMonthlyTransaction(month);
		return (List<PQTransaction>) pq;
	}

	@Override
	public List<PQTransaction> getDailyTransactionBetweeen(Date from, Date to) {
		List<PQTransaction> pq = (List<PQTransaction>) transactionRepository.getDailyTransactionBetween(from, to);
		return (List<PQTransaction>) pq;
	}

	@Override
	public List<PQTransaction> getDailyTransactionBetweenForAccount(Date from, Date to, PQAccountDetail account) {
		List<PQTransaction> pq = (List<PQTransaction>) transactionRepository.getDailyTransactionBetweenForAccount(from,
				to, account);
		return (List<PQTransaction>) pq;
	}

	@Override
	public List<PQTransaction> getDailyTransactionByDate(Date from) {
		List<PQTransaction> pq = (List<PQTransaction>) transactionRepository.getDailyTransactionByDate(from);
		return (List<PQTransaction>) pq;
	}

	@Override
	public List<PQTransaction> getDailyTransactionByDateForAccount(Date from, PQAccountDetail account) {
		List<PQTransaction> pq = (List<PQTransaction>) transactionRepository.getDailyTransactionByDateForAccount(from,
				account);
		return (List<PQTransaction>) pq;
	}

	@Override
	public List<PQTransaction> listTransactionByPaging(PagingDTO dto) {
		List<PQTransaction> transactionList = null;
		Date startDate = dto.getStartDate();
		Date endDate = dto.getEndDate();
		if((startDate!= null) && (endDate != null)){
			transactionList = transactionRepository.getTransactionBetween(startDate,endDate);
		}else {
			transactionList = (List<PQTransaction>)transactionRepository.findAll();
		}
		return transactionList;
	}

	@Override
	public List<PQTransaction> listTransactionByServiceAndDate(Date from, Date to, PQServiceType serviceType, boolean debit, TransactionType transactionType, Status status) {
		return transactionRepository.findTransactionByServiceAndDate(from,to,debit,transactionType,status);
	}

	@Override
	public Long countDailyTransactionByDate(Date from) {
		return transactionRepository.countDailyTransactionByDate(from);
	}

	@Override
	public double countTotalReceivedEBS() {
		try {
			PQService service = pqServiceRepository.findServiceByCode("LMC");
			double totalEBS = transactionRepository.getValidAmountByService(service);
			return totalEBS;
		}catch(Exception ex){
			return 0;
		}
	}

	@Override
	public double countTotalReceivedEBSNow() {
		try {
			PQService service = pqServiceRepository.findServiceByCode("LMC");
			String date = dateOnly.format(new Date());
			Date newDate = null;
			try {
				newDate = dateOnly.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			double totalEBS = transactionRepository.getValidAmountByServiceForDate(service, newDate);

			return totalEBS;
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public double countTotalReceivedVNET() {
		try {
			PQService service = pqServiceRepository.findServiceByCode("LMB");
			System.err.print(service);
			double totalVNet = transactionRepository.getValidAmountByService(service);
			return totalVNet;
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public double countTotalReceivedVNETNow() {
		try {
			PQService service = pqServiceRepository.findServiceByCode("LMB");
			String date = dateOnly.format(new Date());
			Date newDate = null;
			try {
				newDate = dateOnly.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			System.err.print(service);
			double totalVNet = transactionRepository.getValidAmountByServiceForDate(service, newDate);
			return totalVNet;
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public double countTotalTopupNow() {
		User user = userApi.findByUserName("instantpay@payqwik.in");
		double topupTotal = 0;
		String date = dateOnly.format(new Date());
		Date newDate = null;
		try {
			newDate = dateOnly.parse(date);
			topupTotal = transactionRepository.getValidTopupAmountForDate(user.getAccountDetail(),newDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topupTotal;
	}

	@Override
	public List<PQTransaction> getLoadMoneyTransactions(Status status) {
		PQService ebs  = pqServiceRepository.findServiceByOperatorCode("LMC");
		PQService vnet = pqServiceRepository.findServiceByOperatorCode("LMB");
		List<PQTransaction> transactionList = transactionRepository.getLoadMoneyTransactions(ebs,vnet,status);
		return transactionList;
	}

	@Override
	public double countTotalPay() {
		List<PQTransaction> filteredTransactions = getTotalTransactions();
		double amount = 0;
		for (PQTransaction t : filteredTransactions) {
			amount += t.getAmount();
		}
		return amount;
	}

	@Override
	public PQTransaction getLastReverseTransactionOfUser(PQAccountDetail account) {
		List<PQTransaction> trans = transactionRepository.getReverseTransactionsOfUser(account);
		if (trans != null && !trans.isEmpty()) {
			return trans.get(trans.size() - 1);
		}
		return null;
	}

	@Override
	public Long countDailyTransactionByDateForAccount(Date from, PQAccountDetail account) {
		return transactionRepository.countDailyTransactionByDateForAccount(from, account);
	}

	@Override
	public PQTransaction getTransactionByRefNo(String transactionRefNo) {
		return transactionRepository.findByTransactionRefNo(transactionRefNo);
	}

	@Override
	public Date getLastTranasactionTimeStamp(PQAccountDetail account) {
		return transactionRepository.getTimeStampOfLastTransaction(account);
	}

	@Override
	public Date getLastTranasactionTimeStampByStatus(PQAccountDetail account, Status status) {
		return transactionRepository.getTimeStampOfLastTransactionByStatus(account,status);
	}

	@Override
	public Page<PQTransaction> getTotalTransactions(Pageable page) {
		return transactionRepository.findAll(page);
	}

	@Override
	public PQTransaction processRefundTransaction(String transactionRefNo) {
		PQTransaction transaction = getTransactionByRefNo(transactionRefNo);
		if(transaction != null) {
			Status status = transaction.getStatus();
			if(status.equals(Status.Success)) {
				transaction.setStatus(Status.Refunded);
				transaction = saveOrUpdate(transaction);
			}
		}
		return transaction;
	}

	@Override
	public Page<PQTransaction> getTotalTransactionsOfUser(Pageable page, String username) {
		User user = userApi.findByUserName(username);
		PQAccountDetail account = user.getAccountDetail();
		return transactionRepository.findAllTransactionByUser(page, account);
	}

	@Override
	public List<PQTransaction> transactionListByService(PQService service) {
		return transactionRepository.findAllTransactionForMerchant(service);
	}

	@Override
	public List<PQTransaction> transactionListByServiceAndDebit(PQService service, boolean debit) {
		return transactionRepository.findTransactionByServiceAndDebit(service,debit);
	}

	@Override
	public List<PQTransaction> getTotalSuccessfulTransactionsOfUser(String user) {
		User u = userApi.findByUserName(user);
		PQAccountDetail account = u.getAccountDetail();
		return transactionRepository.findAllTransactionByUserAndStatus(account, Status.Success);
	}

	@Override
	public PQTransaction getLastTransactionsOfUser(User user) {
		List<PQTransaction> trans = transactionRepository.getTotalTransactions(user.getAccountDetail());
		if (trans != null && !trans.isEmpty()) {
			return trans.get(trans.size() - 1);
		}
		return null;
	}

	@Override
	public void revertSendMoneyOperations() {
		Date now = new Date();
		logger.info("Current date is :: " + now);
		PQService service = pqServiceRepository.findServiceByCode("SMU");
		List<PQTransaction> sendMoneyTransactions = transactionRepository.findTransactionByService(service);
		System.err.println("send money transactions "+sendMoneyTransactions);
		for (PQTransaction pq : sendMoneyTransactions) {
			String transactionRefNo = pq.getTransactionRefNo();
			if (transactionRefNo != null) {
				transactionRefNo = transactionRefNo.replaceAll("[^0-9]", "");
			}
			System.err.println(transactionRefNo);
			PQTransaction receiverTransaction = findByTransactionRefNoAndStatus(transactionRefNo + "C", Status.Success);
			System.err.println(receiverTransaction);
			if (receiverTransaction != null) {
				Date d = pq.getCreated();
				long daysSpent = (now.getTime() - d.getTime()) / (1000 * 60 * 60 * 24);
				logger.info("Days spent :: " + daysSpent);
				if (daysSpent >= 15) {
					User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
					if (receiver.getMobileStatus() == Status.Inactive) {
						revertUserSendMoney(transactionRefNo);
					}
				}
			}
		}
	}

	@Override
	public int updateFavouriteTransaction(String transactionRefNo, boolean favourite) {
		return transactionRepository.updateFavouriteTransaction(transactionRefNo, favourite);
	}

	@Override
	public void initiateSendMoney(double amount, String description, PQService service, String transactionRefNo,
			String senderUsername, String receiverUsername, String json) {

		User sender = userApi.findByUserName(senderUsername);
		PQAccountDetail senderAccount = sender.getAccountDetail();
		PQCommission senderCommission = commissionApi.findCommissionByServiceAndAmount(service, amount);
		double netCommissionValue = commissionApi.getCommissionValue(senderCommission, amount);
		double netTransactionAmount = amount;
		double senderCurrentBalance = 0;
		double senderUserBalance = senderAccount.getBalance();
		if (senderCommission.getType().equalsIgnoreCase("POST")) {
			netTransactionAmount = netTransactionAmount + netCommissionValue;
			logger.error("If POST then net Transaction with CommissionValue : " + netTransactionAmount);
		}
		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction exists = getTransactionByRefNo(transactionRefNo + "D");
		if (exists == null) {
			senderCurrentBalance = senderUserBalance - netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			senderTransaction.setAmount(amount);
			senderTransaction.setDescription(description);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "D");
			senderTransaction.setDebit(true);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setAccount(senderAccount);
			senderTransaction.setStatus(Status.Initiated);
			senderTransaction.setRequest(json);
			logger.error("Sender Bill Payment Initiated");


			senderAccount.setBalance(senderCurrentBalance);
			PQAccountDetail updatedDetails = pqAccountDetailRepository.save(senderAccount);
			transactionRepository.save(senderTransaction);
//			userApi.updateBalance(senderCurrentBalance, sender);
		}

		User settlementAccount = userApi.findByUserName("settlement@vpayqwik.com");
		PQTransaction settlementTransaction = new PQTransaction();
		PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "CS");
		PQAccountDetail settleAccount = settlementAccount.getAccountDetail();
		if (settlementTransactionExists == null) {
			double settlementUserBalance = settleAccount.getBalance();
			double settlementCurrentBalance = 0;
			settlementCurrentBalance = settlementUserBalance + netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			settlementTransaction.setAmount(netTransactionAmount);
			settlementTransaction.setDescription(description);
			settlementTransaction.setService(service);
			settlementTransaction.setTransactionRefNo(transactionRefNo + "CS");
			settlementTransaction.setDebit(false);
			settlementTransaction.setCurrentBalance(settlementCurrentBalance);
			settlementTransaction.setAccount(settleAccount);
			settlementTransaction.setTransactionType(TransactionType.SETTLEMENT);
			settlementTransaction.setStatus(Status.Success);
			logger.error("Settlement Bill Payment Success");

			PQAccountDetail updatedDetails = pqAccountDetailRepository.save(settleAccount);
			transactionRepository.save(settlementTransaction);
//			userApi.updateBalance(settlementCurrentBalance, settlementAccount);
		}

		User instantpayAccount = userApi.findByUserName(receiverUsername);
		PQTransaction instantpayTransaction = new PQTransaction();
		PQTransaction instantpayTransactionExists = getTransactionByRefNo(transactionRefNo + "C");
		PQAccountDetail ipayAccount = instantpayAccount.getAccountDetail();
		if (instantpayTransactionExists == null) {
			double receiverTransactionAmount = 0;
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				receiverTransactionAmount = netTransactionAmount - netCommissionValue;
			}
			double receiverCurrentBalance = ipayAccount.getBalance();
			instantpayTransaction.setCurrentBalance(receiverCurrentBalance);
			instantpayTransaction.setAmount(receiverTransactionAmount);
			instantpayTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			instantpayTransaction.setDescription(description);
			instantpayTransaction.setService(service);
			instantpayTransaction.setAccount(ipayAccount);
			instantpayTransaction.setTransactionRefNo(transactionRefNo + "C");
			instantpayTransaction.setDebit(false);
			instantpayTransaction.setStatus(Status.Initiated);
			logger.error("Receiver Bill Payment Initiated");
			transactionRepository.save(instantpayTransaction);
		}

		User commissionAccount = userApi.findByUserName("commission@vpayqwik.com");
		PQTransaction commissionTransaction = new PQTransaction();
		PQTransaction commissionTransactionExists = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransactionExists == null) {
			double commissionCurrentBalance = commissionAccount.getAccountDetail().getBalance();
			commissionTransaction.setCurrentBalance(commissionCurrentBalance);
			commissionTransaction.setAmount(netCommissionValue);
			commissionTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			commissionTransaction.setDescription(description);
			commissionTransaction.setService(service);
			commissionTransaction.setTransactionRefNo(transactionRefNo + "CC");
			commissionTransaction.setDebit(false);
			commissionTransaction.setTransactionType(TransactionType.COMMISSION);
			commissionTransaction.setAccount(commissionAccount.getAccountDetail());
			commissionTransaction.setStatus(Status.Initiated);
			logger.error("commission Bill Payment Initiated");
			transactionRepository.save(commissionTransaction);
		}
	}

	@Override
	public void successSendMoney(String transactionRefNo) {

		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		String senderUsername = "";
		String receiverUsername = "";
		double netTransactionAmount = 0;
		double netCommissionValue = 0;
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (senderTransaction != null) {
			logger.error("SenderTransaction TransactionRefNo in case of sucess " + senderTransaction);
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
			senderUsername = sender.getUsername();
			receiverUsername = receiver.getUsername();
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByIdentifier(senderTransaction.getCommissionIdentifier());
			netTransactionAmount = senderTransaction.getAmount();
			netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderTransaction.setStatus(Status.Success);
				logger.error("sender Debit success ");
				PQTransaction t = transactionRepository.save(senderTransaction);
				long accountNumber = senderTransaction.getAccount().getAccountNumber();
				long points = calculatePoints(netTransactionAmount);
				pqAccountDetailRepository.addUserPoints(points, accountNumber);
				if (t != null) {
					smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL,
							SMSTemplate.FUNDTRANSFER_SUCCESS_SENDER, sender, senderTransaction, receiverUsername);
					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.FUNDTRANSFER_SUCCESS_SENDER,
							sender, senderTransaction, receiverUsername);
				}
			}
		}

		PQTransaction settlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransaction != null) {
			User settlement = userApi.findByAccountDetail(settlementTransaction.getAccount());
			PQAccountDetail accountDetail = settlementTransaction.getAccount();
			PQAccountDetail settlementAccountDetail = settlement.getAccountDetail();
			double settlementTransactionAmount = settlementTransaction.getAmount();
			String settlementDescription = settlementTransaction.getDescription();
			double settlementCurrentBalance = settlementTransaction.getCurrentBalance();
			PQTransaction settlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {

				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				settlementTransaction1.setAmount(settlementTransactionAmount);
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setDescription(settlementDescription);
				settlementTransaction1.setService(senderService);
				settlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				settlementTransaction1.setDebit(false);
				settlementTransaction1.setAccount(accountDetail);
				settlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				settlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 2 success ");

				settlementAccountDetail.setBalance(settlementCurrentBalance);
				pqAccountDetailRepository.save(settlementAccountDetail);
				transactionRepository.save(settlementTransaction1);
//
// userApi.updateBalance(settlementCurrentBalance, settlement);
			}
		}

		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				PQAccountDetail receiverAccount = receiver.getAccountDetail();
				double receiverCurrentBalance = receiverTransaction.getCurrentBalance();
				receiverCurrentBalance = receiverCurrentBalance + netTransactionAmount;
				receiverTransaction.setStatus(Status.Success);
				receiverTransaction.setCurrentBalance(receiverCurrentBalance);
				logger.error("Receiver ");

				String serviceCode = receiverTransaction.getService().getCode();
				receiverAccount.setBalance(receiverCurrentBalance);
				PQAccountDetail updatedDetails = pqAccountDetailRepository.save(receiverAccount);
//				userApi.updateBalance(receiverCurrentBalance, receiver);
				PQTransaction t = transactionRepository.save(receiverTransaction);
				if (t != null) {

					if (serviceCode.equalsIgnoreCase("SMU")) {
						smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL,
								SMSTemplate.FUNDTRANSFER_SUCCESS_RECEIVER_UNREGISTERED, receiver, receiverTransaction,
								senderUsername);
						mailSenderApi.sendTransactionMail("VPayQwik Transaction",
								MailTemplate.FUNDTRANSFER_SUCCESS_RECEIVER_UNREGISTERED, receiver, receiverTransaction,
								senderUsername);
					} else {
						smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL,
								SMSTemplate.FUNDTRANSFER_SUCCESS_RECEIVER, receiver, receiverTransaction,
								senderUsername);
						mailSenderApi.sendTransactionMail("VPayQwik Transaction",
								MailTemplate.FUNDTRANSFER_SUCCESS_RECEIVER, receiver, receiverTransaction,
								senderUsername);
					}

				}
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				User commissionAccount = userApi.findByAccountDetail(commissionTransaction.getAccount());
				PQAccountDetail commissionAccountDetail = commissionAccount.getAccountDetail();
				double commissionCurrentBalance = commissionTransaction.getCurrentBalance();
				commissionCurrentBalance = commissionCurrentBalance + netCommissionValue;
				commissionTransaction.setStatus(Status.Success);
				commissionTransaction.setCurrentBalance(commissionCurrentBalance);
				logger.error("Commission success ");
				transactionRepository.save(commissionTransaction);
				commissionAccountDetail.setBalance(commissionCurrentBalance);
				PQAccountDetail commissionUpdatedDetails = pqAccountDetailRepository.save(commissionAccountDetail);
//				userApi.updateBalance(commissionCurrentBalance, commissionAccount);
			}
		}
	}

	@Override
	public void failedSendMoney(String transactionRefNo) {

		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			PQAccountDetail senderAccount = sender.getAccountDetail();
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
					senderTransaction.getAmount());
			double netTransactionAmount = senderTransaction.getAmount();
			double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			double senderCurrentBalance = 0;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				netTransactionAmount = netTransactionAmount + netCommissionValue;
			}
			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderCurrentBalance = senderUserBalance + netTransactionAmount;
				senderTransaction.setCurrentBalance(senderCurrentBalance);
				senderTransaction.setStatus(Status.Reversed);
				logger.error("Sender Bill Payment Reversed");
				transactionRepository.save(senderTransaction);
				senderAccount.setBalance(senderCurrentBalance);
				PQAccountDetail updatedDetails = pqAccountDetailRepository.save(senderAccount);
//				userApi.updateBalance(senderCurrentBalance, sender);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						sender, senderTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//						senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
						senderTransaction, null);
			}
		}

		PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (debitSettlementTransaction != null) {
			User debitSettelment = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
			PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
			double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
			String debitSettlementDescription = debitSettlementTransaction.getDescription();
			double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
			debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
			debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
			PQTransaction debitSettlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
				debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
				debitSettlementTransaction1.setDescription(debitSettlementDescription);
				debitSettlementTransaction1.setService(senderService);
				debitSettlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				debitSettlementTransaction1.setDebit(false);
				debitSettlementTransaction1.setAccount(accountDetail);
				debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				debitSettlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 4 success ");
				accountDetail.setBalance(debitSettlementCurrentBalance);
				pqAccountDetailRepository.save(accountDetail);
				transactionRepository.save(debitSettlementTransaction1);
//				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				receiverTransaction.setStatus(Status.Failed);
				logger.error("Receiver Bill Payment Failed");
				transactionRepository.save(receiverTransaction);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						receiver, receiverTransaction, null);
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
						receiverTransaction, null);
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				commissionTransaction.setStatus(Status.Failed);
				logger.error("Commission Bill Payment Failed");
				transactionRepository.save(commissionTransaction);
			}
		}
	}

	@Override
	public void initiateSharePoints(long points, String description, String transactionRefNo, String senderUsername,
			String receiverUsername, String json) {
		User sender = userApi.findByUserName(senderUsername);
		long senderPoints = sender.getAccountDetail().getPoints();
		SharePointsLog exists = sharePointsLogRepository.getByTransactionRefNo(transactionRefNo + "D");
		if (exists == null) {
			senderPoints -= points;
			exists = new SharePointsLog();
			exists.setPoints(points);
			exists.setDescription(description);
			exists.setTransactionRefNo(transactionRefNo + "D");
			exists.setAccount(sender.getAccountDetail());
			exists.setStatus(Status.Initiated);
			exists.setRequest(json);
			logger.error("Sender Share Points Initiated");
			sharePointsLogRepository.save(exists);
			userApi.updatePoints(senderPoints, sender.getAccountDetail());
		}

		User receiver = userApi.findByUserName(receiverUsername);
		SharePointsLog receiverLog = sharePointsLogRepository.getByTransactionRefNo(transactionRefNo + "C");
		if (receiverLog == null) {
			receiverLog = new SharePointsLog();
			receiverLog.setPoints(points);
			receiverLog.setDescription(description);
			receiverLog.setAccount(receiver.getAccountDetail());
			receiverLog.setTransactionRefNo(transactionRefNo + "C");
			receiverLog.setStatus(Status.Initiated);
			logger.error("Receiver Share Points Initiated");
			sharePointsLogRepository.save(receiverLog);
		}

	}

	@Override
	public void successSharePoints(String transactionRefNo) {

		long receiverCurrentPoints = 0;
		SharePointsLog senderLog = sharePointsLogRepository.getByTransactionRefNo(transactionRefNo + "D");
		SharePointsLog receiverLog = sharePointsLogRepository.getByTransactionRefNo(transactionRefNo + "C");

		if (senderLog != null) {
			logger.error("SenderTransaction TransactionRefNo in case of sucess " + senderLog);
			User sender = userApi.findByAccountDetail(senderLog.getAccount());
			User receiver = userApi.findByAccountDetail(receiverLog.getAccount());
			if ((senderLog.getStatus().equals(Status.Initiated))) {
				senderLog.setStatus(Status.Success);
				logger.error("sender Debit success ");
				sharePointsLogRepository.save(senderLog);
			}
		}

		if (receiverLog != null) {
			if ((receiverLog.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverLog.getAccount());
				receiverCurrentPoints = receiver.getAccountDetail().getPoints();
				receiverCurrentPoints += receiverLog.getPoints();
				receiverLog.setStatus(Status.Success);
				logger.error("Receiver ");
				sharePointsLogRepository.save(receiverLog);
				userApi.updatePoints(receiverCurrentPoints, receiver.getAccountDetail());
			}

		}

	}

	@Override
	public void initiateMerchantPayment(double amount,String description,PQService service,String transactionRefNo,
			String senderUsername, String receiverUsername,String json, boolean isPG) {
		User sender = userApi.findByUserName(senderUsername);
		System.err.println("amount::"+amount);
		PQCommission receiverCommission = commissionApi.findCommissionByServiceAndAmount(service, amount);
		double netCommissionValue = commissionApi.getCommissionValue(receiverCommission, amount);
		double netTransactionAmount = amount;
		double senderCurrentBalance = 0;
		double senderUserBalance = sender.getAccountDetail().getBalance();
		if (receiverCommission.getType().equalsIgnoreCase("POST")) {
			netTransactionAmount = netTransactionAmount - netCommissionValue;
			logger.error("If POST then  merchant  net Transaction with CommissionValue : " + netTransactionAmount);
		}

		if(isPG){
			//find vat service
				User vatUser = userApi.findByUserName(StartupUtil.VAT);
				PQAccountDetail vatAccount = vatUser.getAccountDetail();
				PQService vatService = pqServiceRepository.findServiceByCode("VAT");
				System.err.println(vatService);
				PQCommission vatCommission = commissionApi.findCommissionByServiceAndAmount(vatService,netCommissionValue);
				System.err.println(vatCommission);
				double vatValue = commissionApi.getCommissionValue(vatCommission,netCommissionValue);
				netTransactionAmount = netTransactionAmount - vatValue;
				System.err.println(vatValue);
			//  create transaction for vat

			PQTransaction vatTransaction = getTransactionByRefNo(transactionRefNo+"VAT");
				if(vatTransaction == null) {
					vatTransaction = new PQTransaction();
					vatTransaction.setDescription(description);
					vatTransaction.setStatus(Status.Initiated);
					vatTransaction.setTransactionType(TransactionType.VAT);
					vatTransaction.setService(service);
					vatTransaction.setAmount(vatValue);
					vatTransaction.setAccount(vatAccount);
					vatTransaction.setTransactionRefNo(transactionRefNo + "VAT");
					vatTransaction.setDebit(false);
					vatTransaction.setCurrentBalance(vatAccount.getBalance());
					transactionRepository.save(vatTransaction);
				}
			// find swacch bharat cess service
				User sbcUser = userApi.findByUserName(StartupUtil.SWACCH_BHARAT);
				PQAccountDetail sbcAccount = sbcUser.getAccountDetail();
				PQService sbcService = pqServiceRepository.findServiceByCode("SBC");
				System.err.println(sbcService);
				PQCommission sbcCommission = commissionApi.findCommissionByServiceAndAmount(sbcService,netCommissionValue);
				System.err.println(sbcCommission);
				double sbcValue = commissionApi.getCommissionValue(sbcCommission,netCommissionValue);
				System.err.println(sbcValue);
				netTransactionAmount = netTransactionAmount - sbcValue;
			//	create transaction in  SBC Transaction
			PQTransaction sbcTransaction = getTransactionByRefNo(transactionRefNo+"SBC");
			if(sbcTransaction == null) {
				sbcTransaction = new PQTransaction();
				sbcTransaction.setDescription(description);
				sbcTransaction.setStatus(Status.Initiated);
				sbcTransaction.setTransactionType(TransactionType.SBC);
				sbcTransaction.setService(service);
				sbcTransaction.setAmount(sbcValue);
				sbcTransaction.setAccount(sbcAccount);
				sbcTransaction.setTransactionRefNo(transactionRefNo + "SBC");
				sbcTransaction.setDebit(false);
				sbcTransaction.setCurrentBalance(sbcAccount.getBalance());
				transactionRepository.save(sbcTransaction);
			}


			//	find krishi kalyan cess
				User kkcUser = userApi.findByUserName(StartupUtil.KRISHI_KALYAN);
				PQAccountDetail kkcAccount = kkcUser.getAccountDetail();
			    PQService kkcService = pqServiceRepository.findServiceByCode("KKC");
				System.err.println(kkcService);
				PQCommission kkcCommission = commissionApi.findCommissionByServiceAndAmount(kkcService,netCommissionValue);
				System.err.println(kkcCommission);
				double kkcValue = commissionApi.getCommissionValue(kkcCommission,netCommissionValue);
				System.err.println(kkcValue);
				netTransactionAmount = netTransactionAmount - kkcValue;

			// create transaction in KKC Transaction
			PQTransaction kkcTransaction = getTransactionByRefNo(transactionRefNo+"KKC");
			if(kkcTransaction == null) {
				kkcTransaction = new PQTransaction();
				kkcTransaction.setDescription(description);
				kkcTransaction.setStatus(Status.Initiated);
				kkcTransaction.setTransactionType(TransactionType.KKC);
				kkcTransaction.setService(service);
				kkcTransaction.setAmount(kkcValue);
				kkcTransaction.setAccount(kkcAccount);
				kkcTransaction.setTransactionRefNo(transactionRefNo + "KKC");
				kkcTransaction.setDebit(false);
				kkcTransaction.setCurrentBalance(sbcAccount.getBalance());
				transactionRepository.save(kkcTransaction);
			}
		}
		System.err.println(netTransactionAmount);
		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction exists = getTransactionByRefNo(transactionRefNo + "D");
		if (exists == null) {
			PQAccountDetail senderAccount = sender.getAccountDetail();
			senderCurrentBalance = senderUserBalance - amount;
			senderTransaction.setCommissionIdentifier(receiverCommission.getIdentifier());
			senderTransaction.setAmount(amount);
			senderTransaction.setDescription(description);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "D");
			senderTransaction.setDebit(true);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setAccount(sender.getAccountDetail());
			senderTransaction.setStatus(Status.Initiated);
			senderTransaction.setRequest(json);
			logger.error("Merchant payment Initiated");
			senderAccount.setBalance(senderCurrentBalance);
			pqAccountDetailRepository.save(senderAccount);
			transactionRepository.save(senderTransaction);
		}

		User settlementAccount = userApi.findByUserName("settlement@vpayqwik.com");
		PQTransaction settlementTransaction = new PQTransaction();
		PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransactionExists == null) {
			double settlementUserBalance = settlementAccount.getAccountDetail().getBalance();
			double settlementCurrentBalance = 0;
			PQAccountDetail settlementAccountDetail = settlementAccount.getAccountDetail();
			settlementCurrentBalance = settlementUserBalance + netTransactionAmount;
			senderTransaction.setCommissionIdentifier(receiverCommission.getIdentifier());
			settlementTransaction.setAmount(netTransactionAmount);
			settlementTransaction.setDescription(description);
			settlementTransaction.setService(service);
			settlementTransaction.setTransactionRefNo(transactionRefNo + "CS");
			settlementTransaction.setDebit(false);
			settlementTransaction.setCurrentBalance(settlementCurrentBalance);
			settlementTransaction.setAccount(settlementAccount.getAccountDetail());
			settlementTransaction.setTransactionType(TransactionType.SETTLEMENT);
			settlementTransaction.setStatus(Status.Success);
			logger.error("Settlement Merchant Payment Success");
			settlementAccountDetail.setBalance(settlementCurrentBalance);
			pqAccountDetailRepository.save(settlementAccountDetail);
			transactionRepository.save(settlementTransaction);
		}

		User merchant = userApi.findByUserName(receiverUsername);
		PQTransaction merchantTransaction = new PQTransaction();
		PQTransaction merchantTransactionExists = getTransactionByRefNo(transactionRefNo + "C");
		if (merchantTransactionExists == null) {
			double receiverTransactionAmount = netTransactionAmount;
			double receiverCurrentBalance = merchant.getAccountDetail().getBalance();
			merchantTransaction.setCurrentBalance(receiverCurrentBalance);
			merchantTransaction.setAmount(receiverTransactionAmount);
			merchantTransaction.setCommissionIdentifier(receiverCommission.getIdentifier());
			merchantTransaction.setDescription(description);
			merchantTransaction.setService(service);
			merchantTransaction.setAccount(merchant.getAccountDetail());
			merchantTransaction.setTransactionRefNo(transactionRefNo + "C");
			merchantTransaction.setDebit(false);
			merchantTransaction.setStatus(Status.Initiated);
			logger.error("Receiver Merchant Payment Initiated");
			transactionRepository.save(merchantTransaction);
		}

		User commissionAccount = userApi.findByUserName("commission@vpayqwik.com");
		PQTransaction commissionTransaction = new PQTransaction();
		PQTransaction commissionTransactionExists = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransactionExists == null) {
			double commissionCurrentBalance = commissionAccount.getAccountDetail().getBalance();
			commissionTransaction.setCurrentBalance(commissionCurrentBalance);
			commissionTransaction.setAmount(netCommissionValue);
			commissionTransaction.setCommissionIdentifier(receiverCommission.getIdentifier());
			commissionTransaction.setDescription(description);
			commissionTransaction.setService(service);
			commissionTransaction.setTransactionRefNo(transactionRefNo + "CC");
			commissionTransaction.setDebit(false);
			commissionTransaction.setTransactionType(TransactionType.COMMISSION);
			commissionTransaction.setAccount(commissionAccount.getAccountDetail());
			commissionTransaction.setStatus(Status.Initiated);
			logger.error("commission Bill Payment Initiated");
			transactionRepository.save(commissionTransaction);
		}

	}

	@Override
	public void successMerchantPayment(String transactionRefNo,boolean isPG) {

		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		String senderUsername = "";
		String receiverUsername = "";
		double netTransactionAmount = 0;
		double netCommissionValue = 0;
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (senderTransaction != null) {
			logger.error("SenderTransaction TransactionRefNo in case of sucess " + senderTransaction);
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
			senderUsername = sender.getUsername();
			receiverUsername = receiver.getUsername();
			senderService = senderTransaction.getService();
			String receiverName = receiver.getUserDetail().getFirstName();
			senderCommission = commissionApi.findCommissionByIdentifier(senderTransaction.getCommissionIdentifier());
			netTransactionAmount = senderTransaction.getAmount();
			netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderTransaction.setStatus(Status.Success);
				logger.error("sender Debit success ");
				PQTransaction t = transactionRepository.save(senderTransaction);
				long accountNumber = senderTransaction.getAccount().getAccountNumber();
				long points = calculatePoints(netTransactionAmount);
				pqAccountDetailRepository.addUserPoints(points, accountNumber);
				if (t != null) {
					 smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL,
					 SMSTemplate.MERCHANT_SENDER,
					 sender, senderTransaction,receiverName);
//					 mailSenderApi.sendTransactionMail("VPayQwik Transaction",
//					 MailTemplate.MERCHANT_SENDER, sender,
//					 senderTransaction,receiverName, MailConstants.CC_MAIL);
					 mailSenderApi.sendTransactionMail("VPayQwik Transaction",
							 MailTemplate.MERCHANT_SENDER, sender,
							 senderTransaction,receiverName);
				}
			}
		}

		PQTransaction settlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransaction != null) {
			User settlement = userApi.findByAccountDetail(settlementTransaction.getAccount());
			PQAccountDetail accountDetail = settlementTransaction.getAccount();
			double settlementTransactionAmount = settlementTransaction.getAmount();
			String settlementDescription = settlementTransaction.getDescription();
			double settlementCurrentBalance = settlementTransaction.getCurrentBalance();
			PQTransaction settlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				settlementTransaction1.setAmount(settlementTransactionAmount);
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setDescription(settlementDescription);
				settlementTransaction1.setService(senderService);
				settlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				settlementTransaction1.setDebit(false);
				settlementTransaction1.setAccount(accountDetail);
				settlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				settlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 2 success ");
				accountDetail.setBalance(settlementCurrentBalance);
				pqAccountDetailRepository.save(accountDetail);
//				userApi.updateBalance(settlementCurrentBalance, settlement);
				transactionRepository.save(settlementTransaction1);
			}
		}

		// PQTransaction receiverTransaction =
		// getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				double receiverCurrentBalance = receiverTransaction.getCurrentBalance();
				double amount = receiverTransaction.getAmount();
				receiverCurrentBalance = receiverCurrentBalance + amount;
				receiverTransaction.setStatus(Status.Success);
				receiverTransaction.setCurrentBalance(receiverCurrentBalance);
				logger.error("Receiver BillPayment success ");
				String serviceCode = receiverTransaction.getService().getCode();
				PQAccountDetail receiverAccount = receiver.getAccountDetail();
				receiverAccount.setBalance(receiverCurrentBalance);
				pqAccountDetailRepository.save(receiverAccount);
//				userApi.updateBalance(receiverCurrentBalance, receiver);
				PQTransaction t = transactionRepository.save(receiverTransaction);

				if (t != null) {
					 smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL,
					 SMSTemplate.MERCHANT_RECEIVER,
					 receiver, receiverTransaction,senderUsername);
//					 mailSenderApi.sendTransactionMail("VPayQwik Transaction",
//					 MailTemplate.MERCHANT_RECEIVER,
//					 receiver, receiverTransaction,senderUsername,MailConstants.CC_MAIL);
					 
					 
					 mailSenderApi.sendTransactionMail("VPayQwik Transaction",
							 MailTemplate.MERCHANT_RECEIVER,
							 receiver, receiverTransaction,senderUsername);
				}
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				User commissionAccount = userApi.findByAccountDetail(commissionTransaction.getAccount());
				double commissionCurrentBalance = commissionTransaction.getCurrentBalance();
				commissionCurrentBalance = commissionCurrentBalance + netCommissionValue;
				commissionTransaction.setStatus(Status.Success);
				commissionTransaction.setCurrentBalance(commissionCurrentBalance);
				logger.error("Commission success ");
				PQAccountDetail accountDetail = commissionAccount.getAccountDetail();
				accountDetail.setBalance(commissionCurrentBalance);
				pqAccountDetailRepository.save(accountDetail);
//				userApi.updateBalance(commissionCurrentBalance, commissionAccount);
				transactionRepository.save(commissionTransaction);
			}
		}

		if(isPG) {
			PQTransaction vatTransaction = getTransactionByRefNo(transactionRefNo+"VAT");
			if(vatTransaction != null) {
				if(vatTransaction.getStatus().equals(Status.Initiated)) {
					User vatAccount =  userApi.findByAccountDetail(vatTransaction.getAccount());
					PQAccountDetail vatAccountDetail = vatAccount.getAccountDetail();
					double vatCurrentBalance = vatAccountDetail.getBalance() + vatTransaction.getAmount();
					vatAccountDetail.setBalance(vatCurrentBalance);
					vatTransaction.setStatus(Status.Success);
					vatTransaction.setCurrentBalance(vatCurrentBalance);
					pqAccountDetailRepository.save(vatAccountDetail);
					transactionRepository.save(vatTransaction);
				}
			}


			PQTransaction sbcTransaction = getTransactionByRefNo(transactionRefNo+"SBC");
			if(sbcTransaction != null) {
				if(sbcTransaction.getStatus().equals(Status.Initiated)) {
					User sbcAccount =  userApi.findByAccountDetail(sbcTransaction.getAccount());
					PQAccountDetail sbcAccountDetail = sbcAccount.getAccountDetail();
					double sbcCurrentBalance = sbcAccountDetail.getBalance() + sbcTransaction.getAmount();
					sbcAccountDetail.setBalance(sbcCurrentBalance);
					sbcTransaction.setStatus(Status.Success);
					sbcTransaction.setCurrentBalance(sbcCurrentBalance);
					pqAccountDetailRepository.save(sbcAccountDetail);
					transactionRepository.save(sbcTransaction);
				}
			}

			PQTransaction kkcTransaction = getTransactionByRefNo(transactionRefNo+"KKC");
			if(kkcTransaction != null) {
				if(kkcTransaction.getStatus().equals(Status.Initiated)) {
					User kkcAccount =  userApi.findByAccountDetail(kkcTransaction.getAccount());
					PQAccountDetail kkcAccountDetail = kkcAccount.getAccountDetail();
					double kkcCurrentBalance = kkcAccountDetail.getBalance() + kkcTransaction.getAmount();
					kkcAccountDetail.setBalance(kkcCurrentBalance);
					kkcTransaction.setStatus(Status.Success);
					kkcTransaction.setCurrentBalance(kkcCurrentBalance);
					pqAccountDetailRepository.save(kkcAccountDetail);
					transactionRepository.save(kkcTransaction);
				}
			}

		}


	}

	@Override
	public void failedMerchantPayment(String transactionRefNo) {
		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
					senderTransaction.getAmount());
			double netTransactionAmount = senderTransaction.getAmount();
			double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			double senderCurrentBalance = 0;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				netTransactionAmount = netTransactionAmount + netCommissionValue;
			}
			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderCurrentBalance = senderUserBalance + netTransactionAmount;
				senderTransaction.setCurrentBalance(senderCurrentBalance);
				senderTransaction.setStatus(Status.Reversed);
				logger.error("Sender Bill Payment Reversed");
				transactionRepository.save(senderTransaction);
				userApi.updateBalance(senderCurrentBalance, sender);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						sender, senderTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//						senderTransaction, null,MailConstants.CC_MAIL);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
						senderTransaction, null);
			}
		}

		PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (debitSettlementTransaction != null) {
			User debitSettelment = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
			PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
			double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
			String debitSettlementDescription = debitSettlementTransaction.getDescription();
			double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
			debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
			debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
			PQTransaction debitSettlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
				debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
				debitSettlementTransaction1.setDescription(debitSettlementDescription);
				debitSettlementTransaction1.setService(senderService);
				debitSettlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				debitSettlementTransaction1.setDebit(false);
				debitSettlementTransaction1.setAccount(accountDetail);
				debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				debitSettlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 4 success ");
				accountDetail.setBalance(debitSettlementCurrentBalance);
				pqAccountDetailRepository.save(accountDetail);
//				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
				transactionRepository.save(debitSettlementTransaction1);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				receiverTransaction.setStatus(Status.Failed);
				logger.error("Receiver Bill Payment Failed");
				transactionRepository.save(receiverTransaction);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						receiver, receiverTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
//						receiverTransaction, null,MailConstants.CC_MAIL);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
						receiverTransaction, null);
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				commissionTransaction.setStatus(Status.Failed);
				logger.error("Commission Bill Payment Failed");
				transactionRepository.save(commissionTransaction);
			}
		}
	}

	@Override
	public OnePayResponse getTransaction(String transactionRefNo) {
		OnePayResponse result = new OnePayResponse();
		PQTransaction transaction = getTransactionByRefNo(transactionRefNo);
		result = ConvertUtil.get(transaction);
		return result;
	}

	@Override
	public void initiateVisaPayment(double amount, String description, PQService service, String transactionRefNo, String senderUsername, String json) {

		User sender = userApi.findByUserName(senderUsername);
		System.err.println("amount::"+amount);
		PQCommission receiverCommission = commissionApi.findCommissionByServiceAndAmount(service, amount);
		double netCommissionValue = commissionApi.getCommissionValue(receiverCommission, amount);
		double netTransactionAmount = amount;
		double senderCurrentBalance = 0;
		double senderUserBalance = sender.getAccountDetail().getBalance();
		if (receiverCommission.getType().equalsIgnoreCase("POST")) {
			netTransactionAmount = netTransactionAmount - netCommissionValue;
			logger.error("If POST then visa net Transaction with CommissionValue : " + netTransactionAmount);
		}

		System.err.println(netTransactionAmount);
		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction exists = getTransactionByRefNo(transactionRefNo + "D");
		if (exists == null) {
			PQAccountDetail senderAccount = sender.getAccountDetail();
			senderCurrentBalance = senderUserBalance - amount;
			senderTransaction.setCommissionIdentifier(receiverCommission.getIdentifier());
			senderTransaction.setAmount(amount);
			senderTransaction.setDescription(description);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "D");
			senderTransaction.setDebit(true);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setAccount(sender.getAccountDetail());
			senderTransaction.setStatus(Status.Initiated);
			senderTransaction.setRequest(json);
			logger.error("Merchant payment Initiated");
			senderAccount.setBalance(senderCurrentBalance);
			pqAccountDetailRepository.save(senderAccount);
			transactionRepository.save(senderTransaction);
		}

		User settlementAccount = userApi.findByUserName("settlement@vpayqwik.com");
		PQTransaction settlementTransaction = new PQTransaction();
		PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransactionExists == null) {
			double settlementUserBalance = settlementAccount.getAccountDetail().getBalance();
			double settlementCurrentBalance = 0;
			PQAccountDetail settlementAccountDetail = settlementAccount.getAccountDetail();
			settlementCurrentBalance = settlementUserBalance + netTransactionAmount;
			senderTransaction.setCommissionIdentifier(receiverCommission.getIdentifier());
			settlementTransaction.setAmount(netTransactionAmount);
			settlementTransaction.setDescription(description);
			settlementTransaction.setService(service);
			settlementTransaction.setTransactionRefNo(transactionRefNo + "CS");
			settlementTransaction.setDebit(false);
			settlementTransaction.setCurrentBalance(settlementCurrentBalance);
			settlementTransaction.setAccount(settlementAccount.getAccountDetail());
			settlementTransaction.setTransactionType(TransactionType.SETTLEMENT);
			settlementTransaction.setStatus(Status.Success);
			logger.error("Settlement Merchant Payment Success");
			settlementAccountDetail.setBalance(settlementCurrentBalance);
			pqAccountDetailRepository.save(settlementAccountDetail);
			transactionRepository.save(settlementTransaction);
		}

		User visa = userApi.findByUserName(StartupUtil.MVISA);
		PQTransaction visaTransaction = new PQTransaction();
		PQTransaction visaTransactionExists = getTransactionByRefNo(transactionRefNo + "C");
		if (visaTransactionExists == null) {
			double receiverTransactionAmount = netTransactionAmount;
			double receiverCurrentBalance = visa.getAccountDetail().getBalance();
			visaTransaction.setCurrentBalance(receiverCurrentBalance);
			visaTransaction.setAmount(receiverTransactionAmount);
			visaTransaction.setCommissionIdentifier(receiverCommission.getIdentifier());
			visaTransaction.setDescription(description);
			visaTransaction.setService(service);
			visaTransaction.setAccount(visa.getAccountDetail());
			visaTransaction.setTransactionRefNo(transactionRefNo + "C");
			visaTransaction.setDebit(false);
			visaTransaction.setStatus(Status.Initiated);
			logger.error("Receiver VISA Payment Initiated");
			transactionRepository.save(visaTransaction);
		}

		User commissionAccount = userApi.findByUserName("commission@vpayqwik.com");
		PQTransaction commissionTransaction = new PQTransaction();
		PQTransaction commissionTransactionExists = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransactionExists == null) {
			double commissionCurrentBalance = commissionAccount.getAccountDetail().getBalance();
			commissionTransaction.setCurrentBalance(commissionCurrentBalance);
			commissionTransaction.setAmount(netCommissionValue);
			commissionTransaction.setCommissionIdentifier(receiverCommission.getIdentifier());
			commissionTransaction.setDescription(description);
			commissionTransaction.setService(service);
			commissionTransaction.setTransactionRefNo(transactionRefNo + "CC");
			commissionTransaction.setDebit(false);
			commissionTransaction.setTransactionType(TransactionType.COMMISSION);
			commissionTransaction.setAccount(commissionAccount.getAccountDetail());
			commissionTransaction.setStatus(Status.Initiated);
			logger.error("commission Bill Payment Initiated");
			transactionRepository.save(commissionTransaction);
		}

	}

	@Override
	public void successVisaPayment(String transactionRefNo) {

        PQCommission senderCommission = new PQCommission();
        PQService senderService = new PQService();
        String senderUsername = "";
        String receiverUsername = "";
        double netTransactionAmount = 0;
        double netCommissionValue = 0;
        PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
        PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
        if (senderTransaction != null) {
            logger.error("Sender Transaction TransactionRefNo in case of sucess " + senderTransaction);
            User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
            User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
            senderUsername = sender.getUsername();
            receiverUsername = receiver.getUsername();
            senderService = senderTransaction.getService();
            String receiverName = receiver.getUserDetail().getFirstName();
            senderCommission = commissionApi.findCommissionByIdentifier(senderTransaction.getCommissionIdentifier());
            netTransactionAmount = senderTransaction.getAmount();
            netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
            if ((senderTransaction.getStatus().equals(Status.Initiated))) {
                senderTransaction.setStatus(Status.Success);
                logger.error("sender Debit success ");
                PQTransaction t = transactionRepository.save(senderTransaction);
                long accountNumber = senderTransaction.getAccount().getAccountNumber();
                long points = calculatePoints(netTransactionAmount);
                pqAccountDetailRepository.addUserPoints(points, accountNumber);
                if (t != null) {
                    smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL,
                            SMSTemplate.MERCHANT_SENDER,
                            sender, senderTransaction,receiverName);
//                    mailSenderApi.sendTransactionMail("VPayQwik Transaction",
//                            MailTemplate.MERCHANT_SENDER, sender,
//                            senderTransaction,receiverName, MailConstants.CC_MAIL);
                    
                    mailSenderApi.sendTransactionMail("VPayQwik Transaction",
                            MailTemplate.MERCHANT_SENDER, sender,
                            senderTransaction,receiverName);
                }
            }
        }

        PQTransaction settlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
        if (settlementTransaction != null) {
            User settlement = userApi.findByAccountDetail(settlementTransaction.getAccount());
            PQAccountDetail accountDetail = settlementTransaction.getAccount();
            double settlementTransactionAmount = settlementTransaction.getAmount();
            String settlementDescription = settlementTransaction.getDescription();
            double settlementCurrentBalance = settlementTransaction.getCurrentBalance();
            PQTransaction settlementTransaction1 = new PQTransaction();
            PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
            if (settlementTransactionExists == null) {
                settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
                settlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
                settlementTransaction1.setAmount(settlementTransactionAmount);
                settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
                settlementTransaction1.setDescription(settlementDescription);
                settlementTransaction1.setService(senderService);
                settlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
                settlementTransaction1.setDebit(false);
                settlementTransaction1.setAccount(accountDetail);
                settlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
                settlementTransaction1.setStatus(Status.Success);
                logger.error("Debit settlement 2 success ");
                accountDetail.setBalance(settlementCurrentBalance);
                pqAccountDetailRepository.save(accountDetail);
//				userApi.updateBalance(settlementCurrentBalance, settlement);
                transactionRepository.save(settlementTransaction1);
            }
        }

        if (receiverTransaction != null) {
            if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
                User visa = userApi.findByAccountDetail(receiverTransaction.getAccount());
                double receiverCurrentBalance = receiverTransaction.getCurrentBalance();
                double amount = receiverTransaction.getAmount();
                receiverCurrentBalance = receiverCurrentBalance + amount;
                receiverTransaction.setStatus(Status.Success);
                receiverTransaction.setCurrentBalance(receiverCurrentBalance);
                logger.error("Receiver BillPayment success ");
                String serviceCode = receiverTransaction.getService().getCode();
                PQAccountDetail receiverAccount = visa.getAccountDetail();
                receiverAccount.setBalance(receiverCurrentBalance);
                pqAccountDetailRepository.save(receiverAccount);
                PQTransaction t = transactionRepository.save(receiverTransaction);
                if (t != null) {
                    smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL,
                            SMSTemplate.MERCHANT_RECEIVER,
                            visa, receiverTransaction,senderUsername);
//                    mailSenderApi.sendTransactionMail("VPayQwik Transaction",
//                            MailTemplate.MERCHANT_RECEIVER,
//                            visa, receiverTransaction,senderUsername,MailConstants.CC_MAIL);
                    
                    mailSenderApi.sendTransactionMail("VPayQwik Transaction",
                            MailTemplate.MERCHANT_RECEIVER,
                            visa, receiverTransaction,senderUsername);
                }
            }
        }

        PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
        if (commissionTransaction != null) {
            if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
                User commissionAccount = userApi.findByAccountDetail(commissionTransaction.getAccount());
                double commissionCurrentBalance = commissionTransaction.getCurrentBalance();
                commissionCurrentBalance = commissionCurrentBalance + netCommissionValue;
                commissionTransaction.setStatus(Status.Success);
                commissionTransaction.setCurrentBalance(commissionCurrentBalance);
                logger.error("Commission Success");
                PQAccountDetail accountDetail = commissionAccount.getAccountDetail();
                accountDetail.setBalance(commissionCurrentBalance);
                pqAccountDetailRepository.save(accountDetail);
//				userApi.updateBalance(commissionCurrentBalance, commissionAccount);
                transactionRepository.save(commissionTransaction);
            }
        }

    }

	@Override
	public void failedVisaPayment(String transactionRefNo) {
        PQCommission senderCommission = new PQCommission();
        PQService senderService = new PQService();
        PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
        if (senderTransaction != null) {
            User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
            senderService = senderTransaction.getService();
            senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
                    senderTransaction.getAmount());
            double netTransactionAmount = senderTransaction.getAmount();
            double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
            double senderCurrentBalance = 0;
            double senderUserBalance = sender.getAccountDetail().getBalance();
            if (senderCommission.getType().equalsIgnoreCase("POST")) {
                netTransactionAmount = netTransactionAmount + netCommissionValue;
            }
            if ((senderTransaction.getStatus().equals(Status.Initiated))) {
                senderCurrentBalance = senderUserBalance + netTransactionAmount;
                senderTransaction.setCurrentBalance(senderCurrentBalance);
                senderTransaction.setStatus(Status.Reversed);
                logger.error("Sender Bill Payment Reversed");
                transactionRepository.save(senderTransaction);
                userApi.updateBalance(senderCurrentBalance, sender);
                smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
                        sender, senderTransaction, null);
//                mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//                        senderTransaction, null,MailConstants.CC_MAIL);
                
                mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
                      senderTransaction, null);
            }
        }

        PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
        if (debitSettlementTransaction != null) {
            User debitSettelment = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
            PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
            double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
            String debitSettlementDescription = debitSettlementTransaction.getDescription();
            double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
            debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
            debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
            PQTransaction debitSettlementTransaction1 = new PQTransaction();
            PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
            if (settlementTransactionExists == null) {
                debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
                debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
                debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
                debitSettlementTransaction1.setDescription(debitSettlementDescription);
                debitSettlementTransaction1.setService(senderService);
                debitSettlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
                debitSettlementTransaction1.setDebit(false);
                debitSettlementTransaction1.setAccount(accountDetail);
                debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
                debitSettlementTransaction1.setStatus(Status.Success);
                logger.error("Debit settlement 4 success ");
                accountDetail.setBalance(debitSettlementCurrentBalance);
                pqAccountDetailRepository.save(accountDetail);
//				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
                transactionRepository.save(debitSettlementTransaction1);
            }
        }

        PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
        if (receiverTransaction != null) {
            if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
                User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
                receiverTransaction.setStatus(Status.Failed);
                logger.error("Receiver Bill Payment Failed");
                transactionRepository.save(receiverTransaction);
                smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
                        receiver, receiverTransaction, null);
//                mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
//                        receiverTransaction, null,MailConstants.CC_MAIL);
                
                mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
                        receiverTransaction, null);
            }
        }

        PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
        if (commissionTransaction != null) {
            if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
                commissionTransaction.setStatus(Status.Failed);
                logger.error("Commission Bill Payment Failed");
                transactionRepository.save(commissionTransaction);
            }
        }
	}

	private void revertUserSendMoney(String transactionRefNo) {
		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			PQAccountDetail senderAccount = senderTransaction.getAccount();
			User sender = userApi.findByAccountDetail(senderAccount);
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
					senderTransaction.getAmount());
			double netTransactionAmount = senderTransaction.getAmount();
			double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			double senderCurrentBalance = 0;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				netTransactionAmount = netTransactionAmount + netCommissionValue;
			}
			if ((senderTransaction.getStatus().equals(Status.Success))) {
				senderCurrentBalance = senderUserBalance + netTransactionAmount;
				senderTransaction.setCurrentBalance(senderCurrentBalance);
				senderTransaction.setStatus(Status.Reversed);
				logger.error("Sender Fund Transfer Reversed");
				transactionRepository.save(senderTransaction);
				senderAccount.setBalance(senderCurrentBalance);
				pqAccountDetailRepository.save(senderAccount);
//				userApi.updateBalance(senderCurrentBalance, sender);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						sender, senderTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//						senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
						senderTransaction, null);
			}
		}

		PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (debitSettlementTransaction != null) {
			User debitSettelment = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
			PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
			double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
			String debitSettlementDescription = debitSettlementTransaction.getDescription();
			double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
			debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
			debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
			PQTransaction debitSettlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
				debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
				debitSettlementTransaction1.setDescription(debitSettlementDescription);
				debitSettlementTransaction1.setService(senderService);
				debitSettlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				debitSettlementTransaction1.setDebit(false);
				debitSettlementTransaction1.setAccount(accountDetail);
				debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				debitSettlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 4 success ");
				transactionRepository.save(debitSettlementTransaction1);
				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Success))) {
				PQAccountDetail receiverAccount = receiverTransaction.getAccount();
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				double reducedAmount = receiverAccount.getBalance() - receiverTransaction.getAmount();
				receiverTransaction.setStatus(Status.Failed);
				logger.error("Receiver Fund Transfer Failed");
				receiverAccount.setBalance(reducedAmount);
				pqAccountDetailRepository.save(receiverAccount);
				transactionRepository.save(receiverTransaction);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						receiver, receiverTransaction, null);
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				commissionTransaction.setStatus(Status.Failed);
				logger.error("Commission Bill Payment Failed");
				transactionRepository.save(commissionTransaction);
			}
		}

	}

	@Override
	public void initiateBillPayment(double amount, String description, PQService service, String transactionRefNo,
			String senderUsername, String receiverUsername, String json) {

		User sender = userApi.findByUserName(senderUsername);
		PQCommission senderCommission = commissionApi.findCommissionByServiceAndAmount(service, amount);
		double netCommissionValue = commissionApi.getCommissionValue(senderCommission, amount);
		double netTransactionAmount = amount;
		double senderCurrentBalance = 0;
		double senderUserBalance = sender.getAccountDetail().getBalance();
		System.err.print("*************************sender current balance fetched from DB********************************  "+ senderUserBalance);
		if (senderCommission.getType().equalsIgnoreCase("POST")) {
			netTransactionAmount = netTransactionAmount + netCommissionValue;
			logger.error("If POST then net Transaction with CommissionValue : " + netTransactionAmount);
		}

		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction exists = getTransactionByRefNo(transactionRefNo + "D");
		if (exists == null) {
			senderCurrentBalance = senderUserBalance - netTransactionAmount;
			System.err
					.print("*************************net transaction amount in reverse case ********************************  "
							+ netTransactionAmount);
			PQAccountDetail senderAccountDetail = sender.getAccountDetail();
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			senderTransaction.setAmount(netTransactionAmount);
			senderTransaction.setDescription(description);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "D");
			senderTransaction.setDebit(true);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setAccount(sender.getAccountDetail());
			senderTransaction.setStatus(Status.Initiated);
			senderTransaction.setRequest(json);
			logger.error("Sender Bill Payment Initiated");

//			int update = userApi.updateBalance(senderCurrentBalance, sender);
			senderAccountDetail.setBalance(senderCurrentBalance);
			pqAccountDetailRepository.save(senderAccountDetail);
			PQTransaction updatedDetails = transactionRepository.save(senderTransaction);
			System.err.print("*************************sender current balance in initiate case ********************************  " + senderCurrentBalance);

//			System.err.println("*****rows updated*******" + update);
			User temp = userApi.findByUserName(senderUsername);
			System.err.print("*************************sender updated balance in initiate case ********************************  " + temp.getAccountDetail().getBalance());

		}

		User settlementAccount = userApi.findByUserName("settlement@vpayqwik.com");
		PQTransaction settlementTransaction = new PQTransaction();
		PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransactionExists == null) {
			double settlementUserBalance = settlementAccount.getAccountDetail().getBalance();
			double settlementCurrentBalance = 0;
			PQAccountDetail settlementAccountDetail = settlementAccount.getAccountDetail();
			settlementCurrentBalance = settlementUserBalance + netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			settlementTransaction.setAmount(netTransactionAmount);
			settlementTransaction.setDescription(description);
			settlementTransaction.setService(service);
			settlementTransaction.setTransactionRefNo(transactionRefNo + "CS");
			settlementTransaction.setDebit(false);
			settlementTransaction.setCurrentBalance(settlementCurrentBalance);
			settlementTransaction.setAccount(settlementAccount.getAccountDetail());
			settlementTransaction.setTransactionType(TransactionType.SETTLEMENT);
			settlementTransaction.setStatus(Status.Success);
			logger.error("Settlement Bill Payment Success");
			settlementAccountDetail.setBalance(settlementCurrentBalance);
			pqAccountDetailRepository.save(settlementAccountDetail);
//			userApi.updateBalance(settlementCurrentBalance, settlementAccount);
			transactionRepository.save(settlementTransaction);
		}

		User instantpayAccount = userApi.findByUserName(receiverUsername);
		PQTransaction instantpayTransaction = new PQTransaction();
		PQTransaction instantpayTransactionExists = getTransactionByRefNo(transactionRefNo + "C");
		if (instantpayTransactionExists == null) {
			double receiverTransactionAmount = 0;
			if (senderCommission.getType().equalsIgnoreCase("PRE")) {
				receiverTransactionAmount = netTransactionAmount - netCommissionValue;
			}
			
			System.err.println("dfvdwgdwfgedwfgdwfgd  "+instantpayAccount.getAccountDetail().getBalance());
			double receiverCurrentBalance = instantpayAccount.getAccountDetail().getBalance();
			System.err.println("ghgdhwgdhswgdjkshd  "+receiverCurrentBalance);
			instantpayTransaction.setCurrentBalance(receiverCurrentBalance);
			instantpayTransaction.setAmount(receiverTransactionAmount);
			instantpayTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			instantpayTransaction.setDescription(description);
			instantpayTransaction.setService(service);
			instantpayTransaction.setAccount(instantpayAccount.getAccountDetail());
			instantpayTransaction.setTransactionRefNo(transactionRefNo + "C");
			instantpayTransaction.setDebit(false);
			instantpayTransaction.setStatus(Status.Initiated);
			logger.error("Receiver Bill Payment Initiated");
			transactionRepository.save(instantpayTransaction);
		}

		User commissionAccount = userApi.findByUserName("commission@vpayqwik.com");
		PQTransaction commissionTransaction = new PQTransaction();
		PQTransaction commissionTransactionExists = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransactionExists == null) {
			double commissionCurrentBalance = commissionAccount.getAccountDetail().getBalance();
			commissionTransaction.setCurrentBalance(commissionCurrentBalance);
			commissionTransaction.setAmount(netCommissionValue);
			commissionTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			commissionTransaction.setDescription(description);
			commissionTransaction.setService(service);
			commissionTransaction.setTransactionRefNo(transactionRefNo + "CC");
			commissionTransaction.setDebit(false);
			commissionTransaction.setTransactionType(TransactionType.COMMISSION);
			commissionTransaction.setAccount(commissionAccount.getAccountDetail());
			commissionTransaction.setStatus(Status.Initiated);
			logger.error("commission Bill Payment Initiated");
			transactionRepository.save(commissionTransaction);
		}
	}

	@Override
	public void successBillPayment(String transactionRefNo) {

		String senderMobileNumber = null;
		String receiverMobileNumber = null;

		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		double netTransactionAmount = 0;
		double netCommissionValue = 0;
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");

		if (senderTransaction != null) {
			logger.error("SenderTransaction TransactionRefNO in case of success " + senderTransaction);
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderMobileNumber = sender.getUsername();
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByIdentifier(senderTransaction.getCommissionIdentifier());
			netTransactionAmount = senderTransaction.getAmount();
			netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			org.json.JSONObject json = null;
			String mobileNumber = null;
			try {
				json = new org.json.JSONObject(senderTransaction.getRequest());
				receiverMobileNumber = JSONParserUtil.getString(json, "mobileNo");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderTransaction.setStatus(Status.Success);
				logger.error("sender Debit success ");
				PQTransaction t = transactionRepository.save(senderTransaction);
				long accountNumber = senderTransaction.getAccount().getAccountNumber();
				long points = calculatePoints(netTransactionAmount);
				pqAccountDetailRepository.addUserPoints(points, accountNumber);
				if (t != null) {
					smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.BILLPAYMENT_SUCCESS,
							sender, senderTransaction, null);
//					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS, sender,
//							senderTransaction, receiverMobileNumber,null);
					
					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS, sender,
							senderTransaction, receiverMobileNumber);
				}
			}
		}

		PQTransaction settlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransaction != null) {
			User settlement = userApi.findByAccountDetail(settlementTransaction.getAccount());
			PQAccountDetail accountDetail = settlementTransaction.getAccount();
			double settlementTransactionAmount = settlementTransaction.getAmount();
			String settlementDescription = settlementTransaction.getDescription();
			double settlementCurrentBalance = settlementTransaction.getCurrentBalance();
			PQTransaction settlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				PQAccountDetail settlementAccountDetail = settlement.getAccountDetail();
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				settlementTransaction1.setAmount(settlementTransactionAmount);
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setDescription(settlementDescription);
				settlementTransaction1.setService(senderService);
				settlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				settlementTransaction1.setDebit(false);
				settlementTransaction1.setAccount(accountDetail);
				settlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				settlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 2 success ");
				settlementAccountDetail.setBalance(settlementCurrentBalance);
				pqAccountDetailRepository.save(settlementAccountDetail);
//				userApi.updateBalance(settlementCurrentBalance, settlement);
				transactionRepository.save(settlementTransaction1);

			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				PQAccountDetail receiverAccount = receiver.getAccountDetail();
				double receiverCurrentBalance = receiverTransaction.getCurrentBalance();
				receiverCurrentBalance = receiverCurrentBalance + netTransactionAmount - netCommissionValue;
				receiverTransaction.setStatus(Status.Success);
				receiverTransaction.setCurrentBalance(receiverCurrentBalance);
				logger.error("Receiver BillPayment Success");
				receiverAccount.setBalance(receiverCurrentBalance);
				pqAccountDetailRepository.save(receiverAccount);
//				userApi.updateBalance(receiverCurrentBalance, receiver);
				PQTransaction t = transactionRepository.save(receiverTransaction);

				if (t != null) {
					smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.BILLPAYMENT_SUCCESS,
							receiver, receiverTransaction, null);
//					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS,
//							receiver, receiverTransaction, senderMobileNumber,null);
					
					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS,
							receiver, receiverTransaction, senderMobileNumber);
				}
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				User commissionAccount = userApi.findByAccountDetail(commissionTransaction.getAccount());
				PQAccountDetail commissionAccountDetail = commissionAccount.getAccountDetail();

				double commissionCurrentBalance = commissionTransaction.getCurrentBalance();
				commissionCurrentBalance = commissionCurrentBalance + netCommissionValue;
				commissionTransaction.setStatus(Status.Success);
				commissionTransaction.setCurrentBalance(commissionCurrentBalance);
				logger.error("Commission Success");
				commissionAccountDetail.setBalance(commissionCurrentBalance);
				pqAccountDetailRepository.save(commissionAccountDetail);
//				userApi.updateBalance(commissionCurrentBalance, commissionAccount);
				transactionRepository.save(commissionTransaction);

			}
		}
	}

	@Override
	public void revertFailedBillPayment() {

	}

	@Override
	public List<PQTransaction> getTotalTransactions() {
		List<PQTransaction> totalTransactions = transactionRepository.getTotalTransactions();
		List<PQTransaction> filteredTransactions = new ArrayList<>();
		for (PQTransaction t : totalTransactions) {
			if (t.getStatus().equals(Status.Success) && t.isDebit()) {
				if ((t.getService().getServiceType().equals("Bill Payment"))) {
					filteredTransactions.add(t);
				}
			}
		}
		System.err.println(filteredTransactions);
		return totalTransactions;
	}

	@Override
	public void failedBillPayment(String transactionRefNo) {

		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
					senderTransaction.getAmount());
			double netTransactionAmount = senderTransaction.getAmount();
			double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			double senderCurrentBalance = 0;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				netTransactionAmount = netTransactionAmount;
			}

			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				
				List<PQTransaction> lastTransList = transactionRepository.getTotalSuccessTransactions(senderTransaction.getAccount());
				PQTransaction lastTrans = null;
				if (lastTransList != null && !lastTransList.isEmpty()) {
					lastTrans = lastTransList.get(lastTransList.size() - 1);
					senderCurrentBalance =  lastTrans.getCurrentBalance();
					System.err.println("Last Transation current Balance == " + senderCurrentBalance);
				}
				
//				senderCurrentBalance = senderUserBalance + netTransactionAmount;
				System.err
						.print("*************************net transaction amount in reverse case ********************************  "
								+ netTransactionAmount);
				System.err
						.print("*************************sender current balance in reverse case ********************************  "
								+ senderCurrentBalance);
				System.err
						.print("*************************sender user balance in reverse case ********************************  "
								+ senderUserBalance);
				senderTransaction.setCurrentBalance(senderCurrentBalance);
				senderTransaction.setStatus(Status.Reversed);
				logger.error("Sender Bill Payment Reversed");

				PQAccountDetail senderAccount = sender.getAccountDetail();
				senderAccount.setBalance(senderCurrentBalance);
				pqAccountDetailRepository.save(senderAccount);
//				userApi.updateBalance(senderCurrentBalance, sender);
				transactionRepository.save(senderTransaction);
//
// CommonUtil.sleep(15 * 1000);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						sender, senderTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//						senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
						senderTransaction, null);
			}
		}

		PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (debitSettlementTransaction != null) {
			User debitSettelment = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
			PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
			double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
			String debitSettlementDescription = debitSettlementTransaction.getDescription();
			double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
			debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
			debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
			PQTransaction debitSettlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				PQAccountDetail debitSettlementAccount = accountDetail;
				debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
				debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
				debitSettlementTransaction1.setDescription(debitSettlementDescription);
				debitSettlementTransaction1.setService(senderService);
				debitSettlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				debitSettlementTransaction1.setDebit(false);
				debitSettlementTransaction1.setAccount(accountDetail);
				debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				debitSettlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 4 success ");
				debitSettlementAccount.setBalance(debitSettlementCurrentBalance);
				pqAccountDetailRepository.save(debitSettlementAccount);
				transactionRepository.save(debitSettlementTransaction1);
//				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
//				CommonUtil.sleep(15 * 1000);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				receiverTransaction.setStatus(Status.Failed);
				logger.error("Receiver Bill Payment Failed");
				transactionRepository.save(receiverTransaction);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						receiver, receiverTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
//						receiverTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
						receiverTransaction, null);
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				commissionTransaction.setStatus(Status.Failed);
				logger.error("Commission Bill Payment Failed");
				transactionRepository.save(commissionTransaction);
			}
		}
	}

	@Override
	public void initiateLoadMoney(double amount, String description, PQService service, String transactionRefNo,
			String senderUsername, String json) {
		User sender = userApi.findByUserName(senderUsername);
		PQCommission senderCommission = commissionApi.findCommissionByServiceAndAmount(service, amount);
		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction transactionExists = getTransactionByRefNo(transactionRefNo + "C");
		if (transactionExists == null) {
			double transactionAmount = amount;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			double senderCurrentBalance = senderUserBalance;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			senderTransaction.setAmount(transactionAmount);
			senderTransaction.setDescription(EBSConstants.DESCRIPTION + " of Rs." + amount);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "C");
			senderTransaction.setDebit(false);
			senderTransaction.setFavourite(false);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setAccount(sender.getAccountDetail());
			senderTransaction.setStatus(Status.Initiated);
			senderTransaction.setRequest(json);
			logger.error("Load Money Success");
			transactionRepository.save(senderTransaction);
		}
	}

	@Override
	public void successLoadMoney(String transactionRefNo) {
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if ((senderTransaction.getStatus().equals(Status.Initiated))) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			double transactionAmount = senderTransaction.getAmount();
			double senderUserBalance = sender.getAccountDetail().getBalance();
			double senderCurrentBalance = senderUserBalance + transactionAmount;
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setStatus(Status.Success);
			logger.error("Load Money Success And Updated Balance " + senderCurrentBalance);
			PQTransaction t = transactionRepository.save(senderTransaction);
			PQAccountDetail senderAccount = sender.getAccountDetail();
			senderAccount.setBalance(senderCurrentBalance);
			pqAccountDetailRepository.save(senderAccount);
//			int i = userApi.updateBalance(senderCurrentBalance, sender);
//			logger.error("Updated Load money " + i);
			if (t != null) {
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.LOADMONEY_SUCCESS, sender,
						senderTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.LOADMONEY_SUCCESS, sender,
//						senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.LOADMONEY_SUCCESS, sender,
						senderTransaction, null);
			}
		}
	}

	@Override
	public void failedLoadMoney(String transactionRefNo) {
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if ((senderTransaction.getStatus().equals(Status.Initiated))) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderTransaction.setStatus(Status.Failed);
			logger.error("Load Money Initiated");
		}
	}

	@Override
	public void initiateBankTransfer(double amount, String description, PQService service, String transactionRefNo,
			String senderUsername, String receiverUsername, String json) {

		User sender = userApi.findByUserName(senderUsername);
		PQCommission senderCommission = commissionApi.findCommissionByServiceAndAmount(service, amount);
		double netCommissionValue = commissionApi.getCommissionValue(senderCommission, amount);
		double netTransactionAmount = amount;
		double senderCurrentBalance = 0;
		double senderUserBalance = sender.getAccountDetail().getBalance();

		if (senderCommission.getType().equalsIgnoreCase("POST")) {
			netTransactionAmount = netTransactionAmount + netCommissionValue;
			logger.info("If POST then net Transaction with CommissionValue : " + netTransactionAmount);
		}

		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction exists = getTransactionByRefNo(transactionRefNo + "D");
		if (exists == null) {
			senderCurrentBalance = senderUserBalance - netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			senderTransaction.setAmount(netTransactionAmount);
			senderTransaction.setDescription(description);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "D");
			senderTransaction.setDebit(true);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			PQAccountDetail senderAccount = sender.getAccountDetail();
			senderTransaction.setAccount(senderAccount);
			senderTransaction.setStatus(Status.Initiated);
			senderTransaction.setRequest(json);
			logger.error("Sender Bank Transfer Initiated");
			transactionRepository.save(senderTransaction);
			senderAccount.setBalance(senderCurrentBalance);
			pqAccountDetailRepository.save(senderAccount);
//			userApi.updateBalance(senderCurrentBalance, sender);
		}

		User settlementAccount = userApi.findByUserName("settlement@vpayqwik.com");
		PQTransaction settlementTransaction = new PQTransaction();
		PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransactionExists == null) {
			PQAccountDetail settleAccount = settlementAccount.getAccountDetail();
			double settlementUserBalance = settleAccount.getBalance();
			double settlementCurrentBalance = 0;
			settlementCurrentBalance = settlementUserBalance + netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			settlementTransaction.setAmount(netTransactionAmount);
			settlementTransaction.setDescription(description);
			settlementTransaction.setService(service);
			settlementTransaction.setTransactionRefNo(transactionRefNo + "CS");
			settlementTransaction.setDebit(false);
			settlementTransaction.setCurrentBalance(settlementCurrentBalance);
			settlementTransaction.setAccount(settlementAccount.getAccountDetail());
			settlementTransaction.setTransactionType(TransactionType.SETTLEMENT);
			settlementTransaction.setStatus(Status.Success);
			logger.error("Settlement Bill Payment Success");
			transactionRepository.save(settlementTransaction);
			 settleAccount.setBalance(settlementCurrentBalance);
			pqAccountDetailRepository.save(settleAccount);
//			userApi.updateBalance(settlementCurrentBalance, settlementAccount);
		}

		User bankAccount = userApi.findByUserName(receiverUsername);
		PQTransaction bankTransaction = new PQTransaction();
		PQTransaction bankTransactionExists = getTransactionByRefNo(transactionRefNo + "C");
		if (bankTransactionExists == null) {
			double receiverTransactionAmount = 0;
			if (senderCommission.getType().equalsIgnoreCase("PRE")) {
				receiverTransactionAmount = netTransactionAmount - netCommissionValue;
			}
			PQAccountDetail bankAccountDetail = bankAccount.getAccountDetail();
			double receiverCurrentBalance = bankAccount.getAccountDetail().getBalance();
			bankTransaction.setCurrentBalance(receiverCurrentBalance);
			bankTransaction.setAmount(receiverTransactionAmount);
			bankTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			bankTransaction.setDescription(description);
			bankTransaction.setService(service);
			bankTransaction.setAccount(bankAccount.getAccountDetail());
			bankTransaction.setTransactionRefNo(transactionRefNo + "C");
			bankTransaction.setDebit(false);
			bankTransaction.setStatus(Status.Initiated);
			logger.error("Receiver Bill Payment Initiated");
			transactionRepository.save(bankTransaction);
			bankAccountDetail.setBalance(receiverCurrentBalance);

		}

		User commissionAccount = userApi.findByUserName("commission@vpayqwik.com");
		PQTransaction commissionTransaction = new PQTransaction();
		PQTransaction commissionTransactionExists = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransactionExists == null) {
            PQAccountDetail commissionAccountDetail = commissionAccount.getAccountDetail();
			double commissionCurrentBalance = commissionAccountDetail.getBalance();
            commissionTransaction.setCurrentBalance(commissionCurrentBalance);
			commissionTransaction.setAmount(netCommissionValue);
			commissionTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			commissionTransaction.setDescription(description);
			commissionTransaction.setService(service);
			commissionTransaction.setTransactionRefNo(transactionRefNo + "CC");
			commissionTransaction.setDebit(false);
			commissionTransaction.setTransactionType(TransactionType.COMMISSION);
			commissionTransaction.setAccount(commissionAccount.getAccountDetail());
			commissionTransaction.setStatus(Status.Initiated);
			logger.error("commission Bill Payment Initiated");
			transactionRepository.save(commissionTransaction);

		}
	}

	@Override
	public void successBankTransfer(String transactionRefNo) {

		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		double netTransactionAmount = 0;
		double netCommissionValue = 0;
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			logger.info("SenderTransaction TransactionRefNO in case of success " + senderTransaction);
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByIdentifier(senderTransaction.getCommissionIdentifier());
			netTransactionAmount = senderTransaction.getAmount();
			netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderTransaction.setStatus(Status.Success);
				logger.error("sender Debit success ");
				PQTransaction t = transactionRepository.save(senderTransaction);
				long accountNumber = senderTransaction.getAccount().getAccountNumber();
				long points = calculatePoints(netTransactionAmount);
//				pqAccountDetailRepository.addUserPoints(points, accountNumber);
				if (t != null) {
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.BILLPAYMENT_SUCCESS,
						sender, senderTransaction, null);
//		mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS, sender,
//				senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS, sender,
						senderTransaction, null);
				
				
				}
			}
		}

		PQTransaction settlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransaction != null) {
			User settlement = userApi.findByAccountDetail(settlementTransaction.getAccount());
			PQAccountDetail accountDetail = settlementTransaction.getAccount();
			double settlementTransactionAmount = settlementTransaction.getAmount();
			String settlementDescription = settlementTransaction.getDescription();
			double settlementCurrentBalance = settlementTransaction.getCurrentBalance();
			PQTransaction settlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				settlementTransaction1.setAmount(settlementTransactionAmount);
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setDescription(settlementDescription);
				settlementTransaction1.setService(senderService);
				settlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				settlementTransaction1.setDebit(false);
				settlementTransaction1.setAccount(accountDetail);
				settlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				settlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 2 success ");
				transactionRepository.save(settlementTransaction1);
				accountDetail.setBalance(settlementCurrentBalance);
				pqAccountDetailRepository.save(accountDetail);
//				userApi.updateBalance(settlementCurrentBalance, settlement);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				double receiverCurrentBalance = receiverTransaction.getCurrentBalance();
				receiverCurrentBalance = receiverCurrentBalance + netTransactionAmount - netCommissionValue;
				receiverTransaction.setStatus(Status.Success);
				receiverTransaction.setCurrentBalance(receiverCurrentBalance);
				receiverTransaction.setAmount(senderTransaction.getAmount());
				logger.error("Receiver BillPayment Success");
				PQTransaction t = transactionRepository.save(receiverTransaction);
				PQAccountDetail bankAccount = receiver.getAccountDetail();
				bankAccount.setBalance(receiverCurrentBalance);
				pqAccountDetailRepository.save(bankAccount);
//				userApi.updateBalance(receiverCurrentBalance, receiver);
				if (t != null) {
					smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.BILLPAYMENT_SUCCESS,
							receiver, receiverTransaction, null);
//					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS,
//							receiver, receiverTransaction, null,null);
					
					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS,
							receiver, receiverTransaction, null);
				}
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				User commissionAccount = userApi.findByAccountDetail(commissionTransaction.getAccount());
				double commissionCurrentBalance = commissionTransaction.getCurrentBalance();
				commissionCurrentBalance = commissionCurrentBalance + netCommissionValue;
				commissionTransaction.setStatus(Status.Success);
				commissionTransaction.setCurrentBalance(commissionCurrentBalance);
				logger.error("Commission success ");
				transactionRepository.save(commissionTransaction);
				PQAccountDetail commissionAccountDetail = commissionAccount.getAccountDetail();
				pqAccountDetailRepository.save(commissionAccountDetail);
//				userApi.updateBalance(commissionCurrentBalance, commissionAccount);
			}
		}
	}

	@Override
	public void failedBankTransfer(String transactionRefNo) {
		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
					senderTransaction.getAmount());
			double netTransactionAmount = senderTransaction.getAmount();
			double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			double senderCurrentBalance = 0;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				netTransactionAmount = netTransactionAmount + netCommissionValue;
			}
			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderCurrentBalance = senderUserBalance + netTransactionAmount;
				senderTransaction.setCurrentBalance(senderCurrentBalance);
				senderTransaction.setStatus(Status.Reversed);
				logger.error("Sender Bill Payment Reversed");
				transactionRepository.save(senderTransaction);
				userApi.updateBalance(senderCurrentBalance, sender);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						sender, senderTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//						senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
						senderTransaction, null);
			}
		}

		PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (debitSettlementTransaction != null) {
			User debitSettelment = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
			PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
			double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
			String debitSettlementDescription = debitSettlementTransaction.getDescription();
			double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
			debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
			debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
			PQTransaction debitSettlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
				debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
				debitSettlementTransaction1.setDescription(debitSettlementDescription);
				debitSettlementTransaction1.setService(senderService);
				debitSettlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				debitSettlementTransaction1.setDebit(false);
				debitSettlementTransaction1.setAccount(accountDetail);
				debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				debitSettlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 4 success ");
				transactionRepository.save(debitSettlementTransaction1);
				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				receiverTransaction.setStatus(Status.Failed);
				logger.error("Receiver Bill Payment Failed");
				transactionRepository.save(receiverTransaction);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						receiver, receiverTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
//						receiverTransaction, null,null);
				
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
						senderTransaction, null);
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				commissionTransaction.setStatus(Status.Failed);
				logger.error("Commission Bill Payment Failed");
				transactionRepository.save(commissionTransaction);
			}
		}
	}

	@Override
	public void initiatePromoCodeOld(double amount, String description, PQService service, String transactionRefNo,
			String senderUsername, String receiverUsername, String json) {

		User sender = userApi.findByUserName(senderUsername);
		PQCommission senderCommission = commissionApi.findCommissionByServiceAndAmount(service, amount);
		double netCommissionValue = commissionApi.getCommissionValue(senderCommission, amount);
		double netTransactionAmount = amount;
		double senderCurrentBalance = 0;
		double senderUserBalance = sender.getAccountDetail().getBalance();

		if (senderCommission.getType().equalsIgnoreCase("POST")) {
			netTransactionAmount = netTransactionAmount + netCommissionValue;
			logger.info("If POST then net Transaction with CommissionValue : " + netTransactionAmount);
		}

		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction exists = getTransactionByRefNo(transactionRefNo + "D");
		if (exists == null) {
			senderCurrentBalance = senderUserBalance - netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			senderTransaction.setAmount(netTransactionAmount);
			senderTransaction.setDescription(description);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "D");
			senderTransaction.setDebit(true);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setAccount(sender.getAccountDetail());
			senderTransaction.setStatus(Status.Initiated);
			senderTransaction.setRequest(json);
			logger.error("Sender Bill Payment Initiated");
			transactionRepository.save(senderTransaction);
			userApi.updateBalance(senderCurrentBalance, sender);
		}

		User settlementAccount = userApi.findByUserName("settlement@vpayqwik.com");
		PQTransaction settlementTransaction = new PQTransaction();
		PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransactionExists == null) {
			double settlementUserBalance = settlementAccount.getAccountDetail().getBalance();
			double settlementCurrentBalance = 0;
			settlementCurrentBalance = settlementUserBalance + netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			settlementTransaction.setAmount(netTransactionAmount);
			settlementTransaction.setDescription(description);
			settlementTransaction.setService(service);
			settlementTransaction.setTransactionRefNo(transactionRefNo + "CS");
			settlementTransaction.setDebit(false);
			settlementTransaction.setCurrentBalance(settlementCurrentBalance);
			settlementTransaction.setAccount(settlementAccount.getAccountDetail());
			settlementTransaction.setTransactionType(TransactionType.SETTLEMENT);
			settlementTransaction.setStatus(Status.Success);
			logger.error("Settlement Bill Payment Success");
			transactionRepository.save(settlementTransaction);
			userApi.updateBalance(settlementCurrentBalance, settlementAccount);
		}

		User instantpayAccount = userApi.findByUserName(receiverUsername);
		PQTransaction instantpayTransaction = new PQTransaction();
		PQTransaction instantpayTransactionExists = getTransactionByRefNo(transactionRefNo + "C");
		if (instantpayTransactionExists == null) {
			double receiverTransactionAmount = 0;
			if (senderCommission.getType().equalsIgnoreCase("PRE")) {
				receiverTransactionAmount = netTransactionAmount - netCommissionValue;
			}
			double receiverCurrentBalance = instantpayAccount.getAccountDetail().getBalance();
			instantpayTransaction.setCurrentBalance(receiverCurrentBalance);
			instantpayTransaction.setAmount(receiverTransactionAmount);
			instantpayTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			instantpayTransaction.setDescription(description);
			instantpayTransaction.setService(service);
			instantpayTransaction.setAccount(instantpayAccount.getAccountDetail());
			instantpayTransaction.setTransactionRefNo(transactionRefNo + "C");
			instantpayTransaction.setDebit(false);
			instantpayTransaction.setStatus(Status.Initiated);
			logger.error("Receiver Bill Payment Initiated");
			transactionRepository.save(instantpayTransaction);
		}

		User commissionAccount = userApi.findByUserName("commission@vpayqwik.com");
		PQTransaction commissionTransaction = new PQTransaction();
		PQTransaction commissionTransactionExists = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransactionExists == null) {
			double commissionCurrentBalance = commissionAccount.getAccountDetail().getBalance();
			commissionTransaction.setCurrentBalance(commissionCurrentBalance);
			commissionTransaction.setAmount(netCommissionValue);
			commissionTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			commissionTransaction.setDescription(description);
			commissionTransaction.setService(service);
			commissionTransaction.setTransactionRefNo(transactionRefNo + "CC");
			commissionTransaction.setDebit(false);
			commissionTransaction.setTransactionType(TransactionType.COMMISSION);
			commissionTransaction.setAccount(commissionAccount.getAccountDetail());
			commissionTransaction.setStatus(Status.Initiated);
			logger.error("commission Bill Payment Initiated");
			transactionRepository.save(commissionTransaction);
		}
	}

	@Override
	public void successPromoCodeOld(String transactionRefNo) {

		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
					senderTransaction.getAmount());
			double netTransactionAmount = senderTransaction.getAmount();
			double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			double senderCurrentBalance = 0;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				netTransactionAmount = netTransactionAmount + netCommissionValue;
			}
			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderCurrentBalance = senderUserBalance + netTransactionAmount;
				senderTransaction.setCurrentBalance(senderCurrentBalance);
				senderTransaction.setStatus(Status.Reversed);
				logger.error("Sender Bill Payment Reversed");
				transactionRepository.save(senderTransaction);
				userApi.updateBalance(senderCurrentBalance, sender);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						sender, senderTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//						senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
						senderTransaction, null);
			}
		}

		PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (debitSettlementTransaction != null) {
			User debitSettelment = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
			PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
			double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
			String debitSettlementDescription = debitSettlementTransaction.getDescription();
			double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
			debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
			debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
			PQTransaction debitSettlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
				debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
				debitSettlementTransaction1.setDescription(debitSettlementDescription);
				debitSettlementTransaction1.setService(senderService);
				debitSettlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				debitSettlementTransaction1.setDebit(false);
				debitSettlementTransaction1.setAccount(accountDetail);
				debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				debitSettlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 4 success ");
				transactionRepository.save(debitSettlementTransaction1);
				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				receiverTransaction.setStatus(Status.Failed);
				logger.error("Receiver Bill Payment Failed");
				transactionRepository.save(receiverTransaction);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						receiver, receiverTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
//						receiverTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
						senderTransaction, null);
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				commissionTransaction.setStatus(Status.Failed);
				logger.error("Commission Bill Payment Failed");
				transactionRepository.save(commissionTransaction);
			}
		}

	}

	@Override
	public void failedPromoCodeOld(String transactionRefNo) {
		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
					senderTransaction.getAmount());
			double netTransactionAmount = senderTransaction.getAmount();
			double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			double senderCurrentBalance = 0;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				netTransactionAmount = netTransactionAmount + netCommissionValue;
			}
			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderCurrentBalance = senderUserBalance + netTransactionAmount;
				senderTransaction.setCurrentBalance(senderCurrentBalance);
				senderTransaction.setStatus(Status.Reversed);
				logger.error("Sender Bill Payment Reversed");
				transactionRepository.save(senderTransaction);
				userApi.updateBalance(senderCurrentBalance, sender);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						sender, senderTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//						senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
						senderTransaction, null);
			}
		}

		PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (debitSettlementTransaction != null) {
			User debitSettelment = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
			PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
			double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
			String debitSettlementDescription = debitSettlementTransaction.getDescription();
			double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
			debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
			debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
			PQTransaction debitSettlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
				debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
				debitSettlementTransaction1.setDescription(debitSettlementDescription);
				debitSettlementTransaction1.setService(senderService);
				debitSettlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				debitSettlementTransaction1.setDebit(false);
				debitSettlementTransaction1.setAccount(accountDetail);
				debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				debitSettlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 4 success ");
				transactionRepository.save(debitSettlementTransaction1);
				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				receiverTransaction.setStatus(Status.Failed);
				logger.error("Receiver Bill Payment Failed");
				transactionRepository.save(receiverTransaction);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						receiver, receiverTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
//						receiverTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
						senderTransaction, null);
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				commissionTransaction.setStatus(Status.Failed);
				logger.error("Commission Bill Payment Failed");
				transactionRepository.save(commissionTransaction);
			}
		}

	}

	@Override
	public void initiatePromoCode(double amount, String description, String transactionRefNo, PQService service,
			PromoCode promoCode, String senderUsername, String receiverUsername) {
		User sender = userApi.findByUserName(senderUsername);
		User promo = userApi.findByUserName(receiverUsername);
		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction transactionExists = getTransactionByRefNo(transactionRefNo + "C");
		if (transactionExists == null) {
			double transactionAmount = amount;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			double senderCurrentBalance = senderUserBalance;
			senderTransaction.setAmount(transactionAmount);
			senderTransaction.setDescription(description);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "C");
			senderTransaction.setDebit(false);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setAccount(sender.getAccountDetail());
			senderTransaction.setStatus(Status.Initiated);
			PQTransaction saved = transactionRepository.save(senderTransaction);
			System.err.println("sender transaction saved ::"+saved);
			System.err.println("saved transaction ref no ::"+saved.getTransactionRefNo());
		}

		PQTransaction promoCodeTransaction = new PQTransaction();
		PQTransaction exists = getTransactionByRefNo(transactionRefNo+"D");
		if(exists == null){
			PQAccountDetail promoCodeAccount = promo.getAccountDetail();
			double transactionAmount = amount;
			double senderUserBalance = promoCodeAccount.getBalance();
			double senderCurrentBalance = senderUserBalance;
			promoCodeTransaction.setAmount(transactionAmount);
			promoCodeTransaction.setDescription(description);
			promoCodeTransaction.setService(service);
			promoCodeTransaction.setTransactionRefNo(transactionRefNo + "D");
			promoCodeTransaction.setDebit(true);
			promoCodeTransaction.setCurrentBalance(senderCurrentBalance);
			promoCodeTransaction.setAccount(promoCodeAccount);
			promoCodeTransaction.setStatus(Status.Initiated);
			PQTransaction saved = transactionRepository.save(promoCodeTransaction);
		}
	}

	@Override
	public void successPromoCode(String transactionRefNo) {
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "C");
		PQTransaction promoTransaction = getTransactionByRefNo(transactionRefNo+ "D");
		double senderCurrentBalance = 0.0;
		if ((senderTransaction.getStatus().equals(Status.Initiated))) {
			PQAccountDetail accountDetail = senderTransaction.getAccount();
			User sender = userApi.findByAccountDetail(accountDetail);
			PQAccountDetail senderAccount = sender.getAccountDetail();
			double senderUserBalance = accountDetail.getBalance();
			senderCurrentBalance = senderUserBalance;
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setStatus(Status.Success);
			transactionRepository.save(senderTransaction);
			senderAccount.setBalance(senderCurrentBalance);
			pqAccountDetailRepository.save(senderAccount);
			smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.PROMO_SUCCESS, sender,
					senderTransaction, null);
//			mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.PROMO_SUCCESS, sender,
//					senderTransaction, null,null);
			
			mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.PROMO_SUCCESS, sender,
					senderTransaction, null);
		}


		if ((promoTransaction.getStatus().equals(Status.Initiated))) {
			PQAccountDetail accountDetail = promoTransaction.getAccount();
			User promo = userApi.findByAccountDetail(accountDetail);
			PQAccountDetail senderAccount = promo.getAccountDetail();
			double senderUserBalance = accountDetail.getBalance();
			senderCurrentBalance = senderUserBalance;
			promoTransaction.setCurrentBalance(senderCurrentBalance);
			promoTransaction.setStatus(Status.Success);
			transactionRepository.save(promoTransaction);
			senderAccount.setBalance(senderCurrentBalance);
			pqAccountDetailRepository.save(senderAccount);
		}
	}

	@Override
	public void failedPromoCode(String transactionRefNo) {
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if ((senderTransaction.getStatus().equals(Status.Initiated))) {
			PQAccountDetail accountDetail = senderTransaction.getAccount();
			User sender = userApi.findByAccountDetail(accountDetail);
			senderTransaction.setStatus(Status.Failed);
			transactionRepository.save(senderTransaction);
			smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED, sender,
					senderTransaction, null);
//			mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//					senderTransaction, null,null);
			
			
			mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
					senderTransaction, null);
		}
	}

	@Override
	public PQTransaction saveOrUpdate(PQTransaction transaction) {
		return transactionRepository.save(transaction);
	}

	@Override
	public List<PQTransaction> findTransactionByAccount(Date from, Date to, PQAccountDetail account, double amount) {
		List<PQTransaction> trans = transactionRepository.findTransactionByAccount(from, to, account, amount);
		return trans;
	}

	@Override
	public List<PQTransaction> findTransactionByAccount(PQAccountDetail account) {
		List<PQTransaction> trans = transactionRepository.findTransactionByAccount(account);
		return trans;
	}

	@Override
	public Page<PQTransaction> findByAccount(Pageable page,PQAccountDetail account) {
		return transactionRepository.findAllByAccount(page,account);
	}

	@Override
	public Page<PQTransaction> findByService(Pageable page, PQService service) {
		return null;
	}

	@Override
	public List<PQTransaction> findTransactionByAccountAndAmount(Date from, Date to, PQAccountDetail account,
			double amount) {
		List<PQTransaction> trans = transactionRepository.findTransactionByAccountAndAmount(from, to, account, amount);
		return trans;
	}

	@Override
	public List<PQTransaction> getDailyDebitTransaction(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		List<PQTransaction> trans = transactionRepository.getDailyDebitTransaction(now.get(Calendar.YEAR),
				(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), account, true);
		return trans;
	}

	@Override
	public List<PQTransaction> getMonthlyDebitTransaction(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		List<PQTransaction> trans = transactionRepository.getMonthlyDebitTransaction(now.get(Calendar.YEAR),
				(now.get(Calendar.MONTH) + 1), account);
		return trans;
	}

	@Override
	public List<PQTransaction> getDailyCreditAndDebitTransation(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		List<PQTransaction> trans = transactionRepository.getDailyCreditAndDebitTransation(now.get(Calendar.YEAR),
				(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), account);
		return trans;
	}

	@Override
	public List<PQTransaction> getMonthlyCreditAndDebitTransation(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		List<PQTransaction> trans = transactionRepository.getMonthlyCreditAndDebitTransation(now.get(Calendar.YEAR),
				(now.get(Calendar.MONTH) + 1), account);
		return trans;
	}

	@Override
	public List<PQTransaction> getDailyCreditTransation(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		List<PQTransaction> trans = transactionRepository.getDailyCreditTransation(now.get(Calendar.YEAR),
				(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), account);
		return trans;
	}

	@Override
	public List<PQTransaction> getMonthlyCreditTransation(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		List<PQTransaction> trans = transactionRepository.getMonthlyCreditTransation(now.get(Calendar.YEAR),
				(now.get(Calendar.MONTH) + 1), account);
		return trans;
	}

	@Override
	public double getDailyDebitTransactionTotalAmount(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		double amount = 0.0;
		try{
			amount = transactionRepository.getDailyDebitTransactionTotalAmount(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), account);
			String v = String.format("%.2f", amount);
			amount = Double.parseDouble(v);
		}
		catch(NullPointerException e){
			return amount;
		}
		System.err.println("DailyDebitTransactionTotalAmount :: " + amount);
		return amount;
	}

	@Override
	public double getMonthlyDebitTransactionTotalAmount(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		double amount = 0.0;
		try{
			amount = transactionRepository.getMonthlyDebitTransactionTotalAmount(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), account);
			String v = String.format("%.2f", amount);
			amount = Double.parseDouble(v);
		}
		catch(NullPointerException e){
			return amount;
		}
		System.err.println("MonthlyDebitTransactionTotalAmount :: " + amount);
		return amount;
	}

	@Override
	public double getDailyCreditAndDebitTransationTotalAmount(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		double amount = 0.0;
		try{
			amount = transactionRepository.getDailyCreditAndDebitTransationTotalAmount(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), account);
			String v = String.format("%.2f", amount);
			amount = Double.parseDouble(v);
		}
		catch(NullPointerException e){
			return amount;
		}
		return amount;
	}

	@Override
	public double getMonthlyCreditAndDebitTransationTotalAmount(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		double amount = 0.0;
		try{
			amount = transactionRepository.getMonthlyCreditAndDebitTransationTotalAmount(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), account);
			String v = String.format("%.2f", amount);
			amount = Double.parseDouble(v);
		}
		catch(NullPointerException e){
			return amount;
		}
		return amount;
	}

	@Override
	public double getDailyCreditTransationTotalAmount(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		System.err.println("Year is ::"+now.get(Calendar.YEAR));
		System.err.println("Month is ::"+now.get(Calendar.MONTH)+1);
		System.err.println("Day is ::"+now.get(Calendar.DATE));
		double amount = 0.0;
		try{
			amount = transactionRepository.getDailyCreditTransationTotalAmount(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), now.get(Calendar.DATE), account);
			String v = String.format("%.2f", amount);
			amount = Double.parseDouble(v);
		}
		catch(NullPointerException e){
			return amount;
		}
		System.err.println("Ammount :: " + amount);
		return amount;
	}

	@Override
	public double getMonthlyCreditTransationTotalAmount(PQAccountDetail account) {
		Calendar now = Calendar.getInstance();
		double amount = 0.0;
		try{
			amount = transactionRepository.getMonthlyCreditTransationTotalAmount(now.get(Calendar.YEAR),
					(now.get(Calendar.MONTH) + 1), account);
			String v = String.format("%.2f", amount);
			amount = Double.parseDouble(v);
		}
		catch(NullPointerException e){
			return amount;
		}
		System.err.println("Ammount :: " + amount);
		return amount;
	}

	@Override
	public double getLastSuccessTransaction(PQAccountDetail account) {
        List<PQTransaction> lastTransList = transactionRepository.getTotalSuccessTransactions(account);

        PQTransaction lastTrans = null;
        if (lastTransList != null && !lastTransList.isEmpty()) {
            lastTrans = lastTransList.get(lastTransList.size() - 1);
            return lastTrans.getCurrentBalance();
        }
        return 0.0;
    }

	@Override
	public void reverseTransaction(String transactionRefNo) {
		ResponseDTO result = new ResponseDTO();
		String newTransactionRefNo = ""+System.currentTimeMillis();
		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo+"D");
		if (senderTransaction != null) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
					senderTransaction.getAmount());
			double netTransactionAmount = senderTransaction.getAmount();
			double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			double senderCurrentBalance = 0;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				netTransactionAmount = netTransactionAmount + netCommissionValue;
			}
				PQTransaction senderRefundTransaction = getTransactionByRefNo(newTransactionRefNo + "C");

			if ((senderTransaction.getStatus().equals(Status.Success)) && (senderRefundTransaction == null)) {
				PQAccountDetail senderAccount = sender.getAccountDetail();
				senderRefundTransaction = new PQTransaction();
				System.err.println("Sender Bal is "+senderUserBalance);
				System.err.println("Sender Bal is "+senderAccount.getBalance());
				senderCurrentBalance = senderAccount.getBalance() + netTransactionAmount;
				senderRefundTransaction.setCurrentBalance(senderCurrentBalance);
				senderRefundTransaction.setTransactionRefNo(newTransactionRefNo+"C");
				senderRefundTransaction.setDebit(false);
				senderRefundTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
				senderRefundTransaction.setAmount(netTransactionAmount);
				senderRefundTransaction.setAccount(sender.getAccountDetail());
				senderRefundTransaction.setTransactionType(TransactionType.REFUND);
				senderRefundTransaction.setService(senderService);
				senderRefundTransaction.setStatus(Status.Success);
				senderRefundTransaction.setDescription("Refund of Amount "+netTransactionAmount+" on behalf of Transaction ID "+transactionRefNo);
				logger.error("Sender Payment Reversed");
				senderTransaction.setStatus(Status.Refunded);
				transactionRepository.save(senderTransaction);
				transactionRepository.save(senderRefundTransaction);
				senderAccount.setBalance(senderCurrentBalance);
				pqAccountDetailRepository.save(senderAccount);
//				userApi.updateBalance(senderCurrentBalance, sender);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.REFUND_SUCCESS,
						sender, senderTransaction, null);
//				mailSenderApi.sendTransactionMail("Vpayqwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//						senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("Vpayqwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
						senderTransaction, null);
			}

		}
		PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (debitSettlementTransaction != null) {
			User debitSettlement = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
			PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
			double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
			String debitSettlementDescription = debitSettlementTransaction.getDescription();
			double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
			debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
			debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
			PQTransaction debitSettlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
				debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
				debitSettlementTransaction1.setDescription(debitSettlementDescription);
				debitSettlementTransaction1.setService(senderService);
				debitSettlementTransaction1.setTransactionRefNo(newTransactionRefNo + "DS");
				debitSettlementTransaction1.setDebit(true);
				debitSettlementTransaction1.setAccount(accountDetail);
				debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				debitSettlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 4 success ");
				transactionRepository.save(debitSettlementTransaction1);
				accountDetail.setBalance(debitSettlementCurrentBalance);
				pqAccountDetailRepository.save(accountDetail);
//				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		PQTransaction receiverRefundTransaction = getTransactionByRefNo(newTransactionRefNo + "D");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Success)) && (receiverRefundTransaction == null)) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				PQAccountDetail receiverAccount = receiver.getAccountDetail();
				double currentBalance = receiverAccount.getBalance() - receiverTransaction.getAmount();
				receiverRefundTransaction = new PQTransaction();
				receiverRefundTransaction.setAmount(receiverTransaction.getAmount());
				receiverRefundTransaction.setService(receiverTransaction.getService());
				receiverRefundTransaction.setTransactionType(TransactionType.REFUND);
				receiverRefundTransaction.setTransactionRefNo(newTransactionRefNo + "D");
				receiverRefundTransaction.setDebit(true);
				receiverRefundTransaction.setDescription("Refund of Amount "+receiverTransaction.getAmount()+" on behalf of Transaction ID "+transactionRefNo);
				receiverRefundTransaction.setAccount(receiverAccount);
				receiverRefundTransaction.setCommissionIdentifier(receiverTransaction.getCommissionIdentifier());
				receiverRefundTransaction.setStatus(Status.Success);
				receiverRefundTransaction.setCurrentBalance(currentBalance);
				logger.error("Receiver Bill Payment Failed");
				receiverTransaction.setStatus(Status.Refunded);
				transactionRepository.save(receiverTransaction);
				transactionRepository.save(receiverRefundTransaction);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.REFUND_SUCCESS_SENDER	,receiver, receiverTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
//						receiverTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
						receiverTransaction, null);
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		PQTransaction refundCommissionTransaction = getTransactionByRefNo(newTransactionRefNo + "DC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Success)) && (refundCommissionTransaction == null)) {
				refundCommissionTransaction = new PQTransaction();
				User commission = userApi.findByAccountDetail(commissionTransaction.getAccount());
				PQAccountDetail commissionAccount = commission.getAccountDetail();
				double commissionBalance = commissionAccount.getBalance() - commissionTransaction.getAmount() ;

				refundCommissionTransaction.setCommissionIdentifier(commissionTransaction.getCommissionIdentifier());
				refundCommissionTransaction.setAmount(commissionTransaction.getAmount());
				refundCommissionTransaction.setDescription("Commission Debited due to Refund of transaction with ID " + transactionRefNo);
				refundCommissionTransaction.setCurrentBalance(commissionBalance);
				refundCommissionTransaction.setAccount(commissionAccount);
				refundCommissionTransaction.setTransactionRefNo(newTransactionRefNo + "DC");
				refundCommissionTransaction.setDebit(true);
				refundCommissionTransaction.setService(commissionTransaction.getService());
				refundCommissionTransaction.setTransactionType(TransactionType.COMMISSION);
				refundCommissionTransaction.setStatus(Status.Success);
//				commissionTransaction.setStatus(Status.Failed);
				logger.error("Commission Bill Payment Failed");
				commissionTransaction.setStatus(Status.Refunded);
				transactionRepository.save(commissionTransaction);
				transactionRepository.save(refundCommissionTransaction);
				commissionAccount.setBalance(commissionBalance);
				pqAccountDetailRepository.save(commissionAccount);
			}
		}

	}

	public void successBillPaymentNew(String transactionRefNo) {
		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		String senderUsername = "";
		String receiverUsername = "";
		double netTransactionAmount = 0;
		double netCommissionValue = 0;
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (senderTransaction != null) {
			logger.error("SenderTransaction TransactionRefNo in case of sucess " + senderTransaction);
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
			senderUsername = sender.getUsername();
			receiverUsername = receiver.getUsername();
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByIdentifier(senderTransaction.getCommissionIdentifier());
			netTransactionAmount = senderTransaction.getAmount();
			netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);

			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderTransaction.setStatus(Status.Success);
				logger.error("sender Debit success ");
				PQTransaction t = transactionRepository.save(senderTransaction);
				long accountNumber = senderTransaction.getAccount().getAccountNumber();
				// long points = calculatePoints(netTransactionAmount);
				// pqTransactionRepository.addUserPoints(points, accountNumber);
				if (t != null) {
					smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL,
							SMSTemplate.BILLPAYMENT_SUCCESS, sender, senderTransaction, receiverUsername);
//					mailSenderApi.sendTransactionMail("Vpayqwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS,
//							sender, senderTransaction, receiverUsername,null);
					
					
					mailSenderApi.sendTransactionMail("Vpayqwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS,
							sender, senderTransaction, receiverUsername);
				}
			}
		}

		PQTransaction settlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransaction != null) {
			User settlement = userApi.findByAccountDetail(settlementTransaction.getAccount());
			PQAccountDetail accountDetail = settlementTransaction.getAccount();
			double settlementTransactionAmount = settlementTransaction.getAmount();
			String settlementDescription = settlementTransaction.getDescription();
			double settlementCurrentBalance = settlementTransaction.getCurrentBalance();
			PQTransaction settlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				settlementTransaction1.setAmount(settlementTransactionAmount);
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setDescription(settlementDescription);
				settlementTransaction1.setService(senderService);
				settlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				settlementTransaction1.setDebit(false);
				settlementTransaction1.setAccount(accountDetail);
				settlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				settlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 2 success ");
				transactionRepository.save(settlementTransaction1);
				userApi.updateBalance(settlementCurrentBalance, settlement);
			}
		}

		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				double receiverCurrentBalance = receiverTransaction.getCurrentBalance();
				receiverCurrentBalance = receiverCurrentBalance + netTransactionAmount - netCommissionValue;
				receiverTransaction.setStatus(Status.Success);
				receiverTransaction.setCurrentBalance(receiverCurrentBalance);
				logger.error("Receiver BillPayment success ");
				PQTransaction t = transactionRepository.save(receiverTransaction);
				userApi.updateBalance(receiverCurrentBalance, receiver);
				if (t != null) {
					smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.BILLPAYMENT_SUCCESS,
							receiver, receiverTransaction,null);
//					mailSenderApi.sendTransactionMail("VPayqwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS,
//							receiver, receiverTransaction,receiverUsername,null);
					
					mailSenderApi.sendTransactionMail("VPayqwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS,
							receiver, receiverTransaction,receiverUsername);
				}
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				User commissionAccount = userApi.findByAccountDetail(commissionTransaction.getAccount());
				double commissionCurrentBalance = commissionTransaction.getCurrentBalance();
				commissionCurrentBalance = commissionCurrentBalance + netCommissionValue;
				commissionTransaction.setStatus(Status.Success);
				commissionTransaction.setCurrentBalance(commissionCurrentBalance);
				logger.error("Commission success ");
				transactionRepository.save(commissionTransaction);
				userApi.updateBalance(commissionCurrentBalance, commissionAccount);
			}
		}
		
	}

	@Override
	public void failedBillPaymentNew(String transactionRefNo) {
		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderService = senderTransaction.getService();
			senderCommission = commissionApi.findCommissionByServiceAndAmount(senderService,
					senderTransaction.getAmount());
			double netTransactionAmount = senderTransaction.getAmount();
			double netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);
			double senderCurrentBalance = 0;
			double senderUserBalance = sender.getAccountDetail().getBalance();
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				netTransactionAmount = netTransactionAmount + netCommissionValue;
			}
			if ((senderTransaction.getStatus().equals(Status.Initiated))/*||(senderTransaction.getStatus().equals(Status.Success))*/) {
				senderCurrentBalance = senderUserBalance + netTransactionAmount;
				senderTransaction.setCurrentBalance(senderCurrentBalance);
				senderTransaction.setStatus(Status.Reversed);
				logger.error("Sender Bill Payment Reversed");
				transactionRepository.save(senderTransaction);
				userApi.updateBalance(senderCurrentBalance, sender);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						sender, senderTransaction, null);
//				mailSenderApi.sendTransactionMail("Vpayqwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
//						senderTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("Vpayqwik Transaction", MailTemplate.TRANSACTION_FAILED, sender,
						senderTransaction, null);
			}
		
	}
		PQTransaction debitSettlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (debitSettlementTransaction != null) {
			User debitSettelment = userApi.findByAccountDetail(debitSettlementTransaction.getAccount());
			PQAccountDetail accountDetail = debitSettlementTransaction.getAccount();
			double debitSettlementTransactionAmount = debitSettlementTransaction.getAmount();
			String debitSettlementDescription = debitSettlementTransaction.getDescription();
			double debitSettlementCurrentBalance = debitSettlementTransaction.getCurrentBalance();
			debitSettlementCurrentBalance = debitSettlementCurrentBalance - debitSettlementTransactionAmount;
			debitSettlementTransactionAmount = debitSettlementTransactionAmount - debitSettlementTransactionAmount;
			PQTransaction debitSettlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				debitSettlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				debitSettlementTransaction1.setAmount(debitSettlementTransactionAmount);
				debitSettlementTransaction1.setCurrentBalance(debitSettlementCurrentBalance);
				debitSettlementTransaction1.setDescription(debitSettlementDescription);
				debitSettlementTransaction1.setService(senderService);
				debitSettlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				debitSettlementTransaction1.setDebit(false);
				debitSettlementTransaction1.setAccount(accountDetail);
				debitSettlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				debitSettlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 4 success ");
				transactionRepository.save(debitSettlementTransaction1);
				userApi.updateBalance(debitSettlementCurrentBalance, debitSettelment);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				receiverTransaction.setStatus(Status.Failed);
				logger.error("Receiver Bill Payment Failed");
				transactionRepository.save(receiverTransaction);
				smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.TRANSACTION_FAILED,
						receiver, receiverTransaction, null);
//				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
//						receiverTransaction, null,null);
				
				mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.TRANSACTION_FAILED, receiver,
						receiverTransaction, null);
			}
		}

		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				commissionTransaction.setStatus(Status.Failed);
				logger.error("Commission Bill Payment Failed");
				transactionRepository.save(commissionTransaction);
			}
		}
	}

	private long calculatePoints(double amount) {
		long points = 0;
		if (amount >= 100) {
			points = (long) amount / 100;
		}
		return points;

    }
	
	@Override
	public void initiateBusBooking(double amount, String description, PQService service, String transactionRefNo,
			String senderUsername, String receiverUsername, String json) {

		User sender = userApi.findByUserName(senderUsername);
		PQCommission senderCommission = commissionApi.findCommissionByServiceAndAmount(service, amount);
		double netCommissionValue = commissionApi.getCommissionValue(senderCommission, amount);
		double netTransactionAmount = amount;
		double senderCurrentBalance = 0;
		double senderUserBalance = sender.getAccountDetail().getBalance();
		System.err
				.print("*************************sender current balance fetched from DB********************************  "
						+ senderUserBalance);

		if (senderCommission.getType().equalsIgnoreCase("POST")) {
			netTransactionAmount = netTransactionAmount + netCommissionValue;
			System.err.println("net transaction amount  " + netTransactionAmount);
			logger.error("If POST then net Transaction with CommissionValue : " + netTransactionAmount);
		}
		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction exists = getTransactionByRefNo(transactionRefNo + "D");
		System.err.println("existing transaction ref No " + exists);
		if (exists == null) {

			senderCurrentBalance = senderUserBalance - netTransactionAmount;
			System.err.println("sender currentBalance :" + senderCurrentBalance);
			System.err
					.print("*************************net transaction amount in reverse case ********************************  "
							+ netTransactionAmount);
			PQAccountDetail senderAccountDetail = sender.getAccountDetail();
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			senderTransaction.setAmount(amount);
			senderTransaction.setDescription(description);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "D");
			senderTransaction.setDebit(true);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			senderTransaction.setAccount(sender.getAccountDetail());
			senderTransaction.setStatus(Status.Initiated);
			senderTransaction.setRequest(json);
			logger.error("Sender Bill Payment Initiated");
			// userApi.updateBalance(senderCurrentBalance, sender);
			senderAccountDetail.setBalance(senderCurrentBalance);
			pqAccountDetailRepository.save(senderAccountDetail);
			PQTransaction updatedDetails = transactionRepository.save(senderTransaction);
			System.err.print("User's current balance is: " +senderCurrentBalance);

			User temp = userApi.findByUserName(senderUsername);
			System.err.print("User's Updated balance is : " +temp.getAccountDetail().getBalance());
		}

		User settlementAccount = userApi.findByUserName("settlement@vpayqwik.com");
		PQTransaction settlementTransaction = new PQTransaction();
		PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "D");
		System.err.println("settlement Transaction" + settlementTransactionExists);
		if (settlementTransactionExists == null) {
			double settlementUserBalance = settlementAccount.getAccountDetail().getBalance();
			double settlementCurrentBalance = 0;

			PQAccountDetail settlementAccountDetail = settlementAccount.getAccountDetail();
			settlementCurrentBalance = settlementUserBalance + netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			settlementTransaction.setAmount(netTransactionAmount);
			settlementTransaction.setDescription(description);
			settlementTransaction.setService(service);
			settlementTransaction.setTransactionRefNo(transactionRefNo + "CS");
			settlementTransaction.setDebit(false);
			settlementTransaction.setCurrentBalance(settlementCurrentBalance);
			settlementTransaction.setAccount(settlementAccount.getAccountDetail());
			settlementTransaction.setTransactionType(TransactionType.SETTLEMENT);
			settlementTransaction.setStatus(Status.Success);
			logger.error("Settlement Bill Payment Success");
			settlementAccountDetail.setBalance(settlementCurrentBalance);
			pqAccountDetailRepository.save(settlementAccountDetail);
			// userApi.updateBalance(settlementCurrentBalance,
			// settlementAccount);
			transactionRepository.save(settlementTransaction);
		}

		User instantpayAccount = userApi.findByUserName(receiverUsername);
		PQTransaction instantpayTransaction = new PQTransaction();
		PQTransaction instantpayTransactionExists = getTransactionByRefNo(transactionRefNo + "C");
		if (instantpayTransactionExists == null) {
			double receiverTransactionAmount = 0;
			if (senderCommission.getType().equalsIgnoreCase("POST")) {
				receiverTransactionAmount = netTransactionAmount - netCommissionValue;
			}
			double receiverCurrentBalance = instantpayAccount.getAccountDetail().getBalance();

			instantpayTransaction.setCurrentBalance(receiverCurrentBalance);
			instantpayTransaction.setAmount(receiverTransactionAmount);
			instantpayTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			instantpayTransaction.setDescription(description);
			instantpayTransaction.setService(service);
			instantpayTransaction.setAccount(instantpayAccount.getAccountDetail());
			instantpayTransaction.setTransactionRefNo(transactionRefNo + "C");
			instantpayTransaction.setDebit(false);
			instantpayTransaction.setStatus(Status.Initiated);
			logger.error("Receiver Payment Initiated");
			transactionRepository.save(instantpayTransaction);
		}

		User commissionAccount = userApi.findByUserName("commission@vpayqwik.com");
		PQTransaction commissionTransaction = new PQTransaction();
		PQTransaction commissionTransactionExists = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransactionExists == null) {
			double commissionCurrentBalance = commissionAccount.getAccountDetail().getBalance();
			commissionTransaction.setCurrentBalance(commissionCurrentBalance);
			commissionTransaction.setAmount(netCommissionValue);
			commissionTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			commissionTransaction.setDescription(description);
			commissionTransaction.setService(service);
			commissionTransaction.setTransactionRefNo(transactionRefNo + "CC");
			commissionTransaction.setDebit(false);
			commissionTransaction.setTransactionType(TransactionType.COMMISSION);
			commissionTransaction.setAccount(commissionAccount.getAccountDetail());
			commissionTransaction.setStatus(Status.Initiated);
			logger.error("commission Payment Initiated");
			transactionRepository.save(commissionTransaction);
		}
	}
	
	
	@Override
	public void successTravelBus(String transactionRefNo) {

		String senderUsername = null;
		String receiverUsername = null;

		PQCommission senderCommission = new PQCommission();
		PQService senderService = new PQService();

		double netTransactionAmount = 0;
		double netCommissionValue = 0;
		PQTransaction senderTransaction = getTransactionByRefNo(transactionRefNo + "D");
		if (senderTransaction != null) {
			logger.error("SenderTransaction TransactionRefNo in case of sucess " + senderTransaction);
			User sender = userApi.findByAccountDetail(senderTransaction.getAccount());
			senderUsername = sender.getUsername();
			senderService = senderTransaction.getService();

			senderCommission = commissionApi.findCommissionByIdentifier(senderTransaction.getCommissionIdentifier());
			/* netTransactionAmount = senderTransaction.getAmount(); */
			netTransactionAmount = 0.0;
			netCommissionValue = commissionApi.getCommissionValue(senderCommission, netTransactionAmount);

			if ((senderTransaction.getStatus().equals(Status.Initiated))) {
				senderTransaction.setStatus(Status.Success);
				logger.error("sender Debit success ");
				PQTransaction t = transactionRepository.save(senderTransaction);

				if (t != null) {
					smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.BILLPAYMENT_SUCCESS,
							sender, senderTransaction, null);
//					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS, sender,
//							senderTransaction, receiverUsername,null);
					
					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS, sender,
							senderTransaction, receiverUsername);
				}
			}
		}

		PQTransaction settlementTransaction = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransaction != null) {
			User settlement = userApi.findByAccountDetail(settlementTransaction.getAccount());
			PQAccountDetail accountDetail = settlementTransaction.getAccount();
			double settlementTransactionAmount = settlementTransaction.getAmount();
			String settlementDescription = settlementTransaction.getDescription();
			double settlementCurrentBalance = settlementTransaction.getCurrentBalance();
			PQTransaction settlementTransaction1 = new PQTransaction();
			PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "DS");
			if (settlementTransactionExists == null) {
				PQAccountDetail settlementAccountDetail = settlement.getAccountDetail();
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setCommissionIdentifier(senderCommission.getIdentifier());
				settlementTransaction1.setAmount(settlementTransactionAmount);
				settlementTransaction1.setCurrentBalance(settlementCurrentBalance);
				settlementTransaction1.setDescription(settlementDescription);
				settlementTransaction1.setService(senderService);
				settlementTransaction1.setTransactionRefNo(transactionRefNo + "DS");
				settlementTransaction1.setDebit(false);
				settlementTransaction1.setAccount(accountDetail);
				settlementTransaction1.setTransactionType(TransactionType.SETTLEMENT);
				settlementTransaction1.setStatus(Status.Success);
				logger.error("Debit settlement 2 success ");
				settlementAccountDetail.setBalance(settlementCurrentBalance);
				pqAccountDetailRepository.save(settlementAccountDetail);
				transactionRepository.save(settlementTransaction1);
				// userApi.updateBalance(settlementCurrentBalance, settlement);
			}
		}

		PQTransaction receiverTransaction = getTransactionByRefNo(transactionRefNo + "C");
		if (receiverTransaction != null) {
			if ((receiverTransaction.getStatus().equals(Status.Initiated))) {
				User receiver = userApi.findByAccountDetail(receiverTransaction.getAccount());
				receiverUsername = receiver.getUsername();
				PQAccountDetail receiverAccount = receiver.getAccountDetail();
				double receiverCurrentBalance = receiverTransaction.getCurrentBalance();
				receiverCurrentBalance = receiverCurrentBalance + netTransactionAmount - netCommissionValue;
				receiverTransaction.setStatus(Status.Success);
				receiverTransaction.setCurrentBalance(receiverCurrentBalance);
				receiverAccount.setBalance(receiverCurrentBalance);
				pqAccountDetailRepository.save(receiverAccount);
				PQTransaction t = transactionRepository.save(receiverTransaction);
				// userApi.updateBalance(receiverCurrentBalance, receiver);
				logger.error("Receiver BillPayment success ");
				if (t != null) {
					smsSenderApi.sendTransactionSMS(SMSAccount.PAYQWIK_TRANSACTIONAL, SMSTemplate.BILLPAYMENT_SUCCESS,
							receiver, receiverTransaction, null);
//					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS, receiver,
//							receiverTransaction, receiverUsername,null);

					mailSenderApi.sendTransactionMail("VPayQwik Transaction", MailTemplate.BILLPAYMENT_SUCCESS, receiver,
							receiverTransaction, receiverUsername);
				}
			}
		}
		PQTransaction commissionTransaction = getTransactionByRefNo(transactionRefNo + "CC");
		System.err.println("commissionTransaction :: " + commissionTransaction);
		if (commissionTransaction != null) {
			if ((commissionTransaction.getStatus().equals(Status.Initiated))) {
				User commissionAccount = userApi.findByAccountDetail(commissionTransaction.getAccount());
				System.err.println("commissionAccount :: " + commissionAccount);
				PQAccountDetail commissionAccountDetail = commissionAccount.getAccountDetail();
				System.err.println("commissionAccountDetail :: " + commissionAccountDetail.getId());
				double commissionCurrentBalance = commissionTransaction.getCurrentBalance();
				commissionCurrentBalance = commissionCurrentBalance + netCommissionValue;
				commissionTransaction.setStatus(Status.Success);
				commissionTransaction.setCurrentBalance(commissionCurrentBalance);
				logger.error("Commission success ");
				commissionAccountDetail.setBalance(commissionCurrentBalance);
				pqAccountDetailRepository.save(commissionAccountDetail);
				transactionRepository.save(commissionTransaction);
				// userApi.updateBalance(commissionCurrentBalance,
				// commissionAccount);
			}
		}
	}

	@Override
	public void initiateMBankTransfer(double amount, String description, PQService service, String transactionRefNo,
			String senderUsername, String receiverUsername, String json) {

		User sender = userApi.findByUserName(senderUsername);
		PQCommission senderCommission = commissionApi.findCommissionByServiceAndAmount(service, amount);
		double netCommissionValue = commissionApi.getCommissionValue(senderCommission, amount);
		double netTransactionAmount = amount;
		double senderCurrentBalance = 0;
		double senderUserBalance = sender.getAccountDetail().getBalance();

		if (senderCommission.getType().equalsIgnoreCase("POST")) {
			netTransactionAmount = netTransactionAmount + netCommissionValue;
			logger.info("If POST then net Transaction with CommissionValue : " + netTransactionAmount);
		}

		PQTransaction senderTransaction = new PQTransaction();
		PQTransaction exists = getTransactionByRefNo(transactionRefNo + "D");
		if (exists == null) {
			senderCurrentBalance = senderUserBalance - netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			senderTransaction.setAmount(netTransactionAmount);
			senderTransaction.setDescription(description);
			senderTransaction.setService(service);
			senderTransaction.setTransactionRefNo(transactionRefNo + "D");
			senderTransaction.setDebit(true);
			senderTransaction.setCurrentBalance(senderCurrentBalance);
			PQAccountDetail senderAccount = sender.getAccountDetail();
			senderTransaction.setAccount(senderAccount);
			senderTransaction.setStatus(Status.Initiated);
			senderTransaction.setRequest(json);
			logger.error("Merchant Bank Transfer Initiated");
			transactionRepository.save(senderTransaction);
			senderAccount.setBalance(senderCurrentBalance);
			pqAccountDetailRepository.save(senderAccount);
//			userApi.updateBalance(senderCurrentBalance, sender);
		}

		User settlementAccount = userApi.findByUserName("settlement@vpayqwik.com");
		PQTransaction settlementTransaction = new PQTransaction();
		PQTransaction settlementTransactionExists = getTransactionByRefNo(transactionRefNo + "CS");
		if (settlementTransactionExists == null) {
			PQAccountDetail settleAccount = settlementAccount.getAccountDetail();
			double settlementUserBalance = settleAccount.getBalance();
			double settlementCurrentBalance = 0;
			settlementCurrentBalance = settlementUserBalance + netTransactionAmount;
			senderTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			settlementTransaction.setAmount(netTransactionAmount);
			settlementTransaction.setDescription(description);
			settlementTransaction.setService(service);
			settlementTransaction.setTransactionRefNo(transactionRefNo + "CS");
			settlementTransaction.setDebit(false);
			settlementTransaction.setCurrentBalance(settlementCurrentBalance);
			settlementTransaction.setAccount(settlementAccount.getAccountDetail());
			settlementTransaction.setTransactionType(TransactionType.SETTLEMENT);
			settlementTransaction.setStatus(Status.Success);
			logger.error("Settlement Bill Payment Success");
			transactionRepository.save(settlementTransaction);
			 settleAccount.setBalance(settlementCurrentBalance);
			pqAccountDetailRepository.save(settleAccount);
			userApi.updateBalance(settlementCurrentBalance, settlementAccount);
		}

		User bankAccount = userApi.findByUserName(receiverUsername);
		PQTransaction bankTransaction = new PQTransaction();
		PQTransaction bankTransactionExists = getTransactionByRefNo(transactionRefNo + "C");
		if (bankTransactionExists == null) {
			double receiverTransactionAmount = 0;
			if (senderCommission.getType().equalsIgnoreCase("PRE")) {
				receiverTransactionAmount = netTransactionAmount - netCommissionValue;
			}
			PQAccountDetail bankAccountDetail = bankAccount.getAccountDetail();
			double receiverCurrentBalance = bankAccount.getAccountDetail().getBalance();
			bankTransaction.setCurrentBalance(receiverCurrentBalance);
			bankTransaction.setAmount(receiverTransactionAmount);
			bankTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			bankTransaction.setDescription(description);
			bankTransaction.setService(service);
			bankTransaction.setAccount(bankAccount.getAccountDetail());
			bankTransaction.setTransactionRefNo(transactionRefNo + "C");
			bankTransaction.setDebit(false);
			bankTransaction.setStatus(Status.Initiated);
			logger.error("Receiver Bill Payment Initiated");
			transactionRepository.save(bankTransaction);
			receiverCurrentBalance = receiverCurrentBalance + receiverTransactionAmount;
			bankAccountDetail.setBalance(receiverCurrentBalance);
			pqAccountDetailRepository.save(bankAccountDetail);

		}

		User commissionAccount = userApi.findByUserName("commission@vpayqwik.com");
		PQTransaction commissionTransaction = new PQTransaction();
		PQTransaction commissionTransactionExists = getTransactionByRefNo(transactionRefNo + "CC");
		if (commissionTransactionExists == null) {
            PQAccountDetail commissionAccountDetail = commissionAccount.getAccountDetail();
			double commissionCurrentBalance = commissionAccountDetail.getBalance();
            commissionTransaction.setCurrentBalance(commissionCurrentBalance);
			commissionTransaction.setAmount(netCommissionValue);
			commissionTransaction.setCommissionIdentifier(senderCommission.getIdentifier());
			commissionTransaction.setDescription(description);
			commissionTransaction.setService(service);
			commissionTransaction.setTransactionRefNo(transactionRefNo + "CC");
			commissionTransaction.setDebit(false);
			commissionTransaction.setTransactionType(TransactionType.COMMISSION);
			commissionTransaction.setAccount(commissionAccount.getAccountDetail());
			commissionTransaction.setStatus(Status.Initiated);
			logger.error("commission Bill Payment Initiated");
			transactionRepository.save(commissionTransaction);

		}
	
	}

	@Override
	public List<PQTransaction> getTotalTransactionsOfMerchant(String merchantEmail) {
		System.err.println("MErchant Email ::" +merchantEmail );
		User user = userApi.findByUserName(merchantEmail);
		System.err.println("USER ::" + user);
		PQAccountDetail account = user.getAccountDetail();
		System.err.println("Merchant Account ::" + account);
		return transactionRepository.findAllTransactionByMerchant(account);
	}
	
}
	
