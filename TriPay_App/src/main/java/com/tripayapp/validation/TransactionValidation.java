package com.tripayapp.validation;

import com.tripayapp.api.ICommissionApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQAccountType;
import com.tripayapp.entity.PQCommission;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.PQTransaction;
import com.tripayapp.entity.User;
import com.tripayapp.model.SharePointDTO;
import com.tripayapp.model.Status;
import com.tripayapp.model.TransactionDTO;
import com.tripayapp.model.error.TransactionError;

import java.util.Date;

public class TransactionValidation {

	private final IUserApi userApi;
	private final ITransactionApi transactionApi;
	private final ICommissionApi commissionApi;

	public TransactionValidation(IUserApi userApi, ITransactionApi transactionApi, ICommissionApi commissionApi) {
		this.userApi = userApi;
		this.transactionApi = transactionApi;
		this.commissionApi = commissionApi;
	}

	public TransactionError validateMerchantTransaction(String amount, String senderUsername, PQService service) {
		TransactionError error = new TransactionError();
		boolean valid = true;
		double transactionAmount = Double.parseDouble(amount);
		User senderUser = userApi.findByUserName(senderUsername);
		if (senderUser.getMobileStatus().equals(Status.Inactive)) {
			error.setMessage(
					"Please verify your mobile " + senderUser.getUserDetail().getContactNo() + " before any transaction");
			error.setValid(false);
			error.setCode("F01");
			return error;
		}
		double dailyTransactionLimit = senderUser.getAccountDetail().getAccountType().getDailyLimit();
		double monthlyTransactionLimit = senderUser.getAccountDetail().getAccountType().getMonthlyLimit();
		double totalDailyTransaction = userApi.dailyTransactionTotal(senderUser);
		double totalMonthlyTransaction = userApi.monthlyTransactionTotal(senderUser);
		double totalDebitMonthly = transactionApi.getMonthlyDebitTransactionTotalAmount(senderUser.getAccountDetail());
		double totalDebitDaily = transactionApi.getDailyDebitTransactionTotalAmount(senderUser.getAccountDetail());
		if (totalDailyTransaction < 0 || totalMonthlyTransaction < 0) {
			error.setMessage("Opps!! Unable to process transaction.");
			error.setValid(false);
			error.setCode("F02");
			return error;
		}
		PQCommission commission = commissionApi.findCommissionByServiceAndAmount(service, transactionAmount);
		double netCommissionValue = commissionApi.getCommissionValue(commission, transactionAmount);
		if (commission.getType().equalsIgnoreCase("POST")) {
			transactionAmount = transactionAmount + netCommissionValue;
		}
		if (!CommonValidation.balanceCheck(senderUser.getAccountDetail().getBalance(), transactionAmount)) {
			if (commission.getType().equalsIgnoreCase("POST")) {
				error.setMessage("Insufficient Balance. Your current balance is Rs."
						+ senderUser.getAccountDetail().getBalance() + " and service charges is Rs." + netCommissionValue);
			} else {
				error.setMessage("Insufficient Balance. Your current balance is Rs."
						+ senderUser.getAccountDetail().getBalance());
			}
			valid = false;
			error.setCode("F04");
			error.setValid(valid);
			return error;
		}else if (!CommonValidation.dailyLimitCheck(dailyTransactionLimit, totalDebitDaily, transactionAmount)) {
			error.setMessage("Transaction Limit Exceeded. Your daily transaction limit is Rs. "
					+ dailyTransactionLimit + " and you already made total transaction of Rs." + totalDebitDaily);
			valid = false;
			error.setCode("F06");
			error.setValid(valid);
			return error;
		}else if (!CommonValidation.monthlyLimitCheck(monthlyTransactionLimit, totalDebitMonthly,
				transactionAmount)) {
			error.setMessage("Transaction Limit Exceeded. Your monthly transaction limit is Rs."
					+ monthlyTransactionLimit + " and you already made total transaction of Rs."
					+ totalDebitMonthly);
			valid = false;
			error.setCode("F05");
			error.setValid(valid);
			return error;
		}
		Date date = transactionApi.getLastTranasactionTimeStamp(senderUser.getAccountDetail());
		if (date != null) {
			long lastTransactionTimeStamp = date.getTime();
			long currentTimeInMillis = System.currentTimeMillis();
			if ((currentTimeInMillis - lastTransactionTimeStamp) < 1000 * 60 * 1) {
				error.setMessage("Please wait for 1 min");
				error.setValid(false);
				return error;
			}
		}
		Date reverseTransactionDate = transactionApi.getLastTranasactionTimeStampByStatus(senderUser.getAccountDetail(),Status.Reversed);
		if(reverseTransactionDate != null){
			long timestamp = reverseTransactionDate.getTime();
			long currentTime = System.currentTimeMillis();
			if((currentTime - timestamp) < 1000 * 60 * 15){
				error.setMessage("Your last transaction was reversed. Please try again after 15 mins");
				error.setValid(false);
				return error;
			}
		}

		double currentBalance = senderUser.getAccountDetail().getBalance();
		double lastTransactionBalance = transactionApi.getLastSuccessTransaction(senderUser.getAccountDetail());
		if(currentBalance >= 0 && lastTransactionBalance >= 0) {
			if (currentBalance != lastTransactionBalance) {
				error.setMessage("Transaction is not validated");
				error.setValid(false);
				return error;
			}
		}

		error.setValid(valid);
		return error;
	}

	public TransactionError validateP2PTransaction(String amount, String senderUsername, String receiverUsername,
			PQService service) {
		TransactionError error = new TransactionError();
		boolean valid = true;
		double totalCreditMonthlyReceiver = 0;
		double monthlyCreditLimitReceiver = 0;
		User senderUser = userApi.findByUserName(senderUsername);
		if (senderUser.getMobileStatus().equals(Status.Inactive)) {
			error.setMessage("Please verify your mobile " + senderUser.getUserDetail().getContactNo() + " before any transaction");
			error.setValid(false);
			return error;
		}
		User receiverUser = userApi.findByUserName(receiverUsername);
//		if (receiverUser.getEmailStatus().equals(Status.Inactive)
//				&& receiverUser.getMobileStatus().equals(Status.Active)) {
//			error.setMessage("Receiver email is not verified. Please verify " + receiverUser.getUserDetail().getEmail() + " before any transaction");
//			error.setValid(false);
//			return error;
//		}

		double balanceLimit = 0;
		if (receiverUser != null) {
			balanceLimit = receiverUser.getAccountDetail().getAccountType().getBalanceLimit();
			totalCreditMonthlyReceiver = transactionApi.getMonthlyCreditTransationTotalAmount(receiverUser.getAccountDetail());
			monthlyCreditLimitReceiver = receiverUser.getAccountDetail().getAccountType().getMonthlyLimit();
		}
		double transactionAmount = Double.parseDouble(amount);
		double dailyTransactionLimit = senderUser.getAccountDetail().getAccountType().getDailyLimit();
		double monthlyTransactionLimit = senderUser.getAccountDetail().getAccountType().getMonthlyLimit();

		double totalDailyTransaction = userApi.dailyTransactionTotal(senderUser);
		double totalMonthlyTransaction = userApi.monthlyTransactionTotal(senderUser);
		double totalMonthlyDebit = transactionApi.getMonthlyDebitTransactionTotalAmount(senderUser.getAccountDetail());
		double totalDailyDebit = transactionApi.getDailyDebitTransactionTotalAmount(senderUser.getAccountDetail());
		if (totalDailyTransaction < 0 || totalMonthlyTransaction < 0) {
			error.setMessage("Opps!! Unable to process transaction.");
			error.setValid(false);
			return error;
		}

		PQCommission commission = commissionApi.findCommissionByServiceAndAmount(service, transactionAmount);
		double netCommissionValue = commissionApi.getCommissionValue(commission, transactionAmount);
		if (commission.getType().equalsIgnoreCase("POST")) {
			transactionAmount = transactionAmount + netCommissionValue;
		}

		if (!CommonValidation.balanceCheck(senderUser.getAccountDetail().getBalance(), transactionAmount)) {
			if (commission.getType().equalsIgnoreCase("POST")) {
				error.setMessage("Insufficient Balance. Your current balance is Rs."
						+ senderUser.getAccountDetail().getBalance() + " and service charges is Rs." + netCommissionValue);
			} else {
				error.setMessage("Insufficient Balance. Your current balance is Rs."
						+ senderUser.getAccountDetail().getBalance());
			}
			valid = false;
			error.setValid(valid);
			return error;
		} else if (!CommonValidation.dailyLimitCheck(dailyTransactionLimit, totalDailyDebit, transactionAmount)) {
			error.setMessage("Transaction Limit Exceeded. Your daily transaction limit is Rs. "
					+ dailyTransactionLimit + " and you already made total transaction of Rs." + totalDailyDebit);
			valid = false;
			error.setValid(valid);
			return error;
		} else if ((receiverUser != null) && !(CommonValidation.receiverBalanceLimit(balanceLimit,
				receiverUser.getAccountDetail().getBalance(), transactionAmount))) {
			error.setMessage(
					"Balance Limit Exceeded. " + receiverUser.getUsername() + " balance limit is Rs." + balanceLimit);
			valid = false;
			error.setValid(valid);
			return error;
		}else if (!CommonValidation.monthlyLimitCheck(monthlyTransactionLimit, totalMonthlyDebit,
				transactionAmount)) {
			error.setMessage("Transaction Limit Exceeded. Your monthly transaction limit is Rs."
					+ monthlyTransactionLimit + " and you already made total transaction of Rs."
					+ totalMonthlyDebit);
			valid = false;
			error.setValid(valid);
			return error;
		}else if (!CommonValidation.monthlyCreditLimitCheck(monthlyCreditLimitReceiver, totalCreditMonthlyReceiver,
				transactionAmount)) {
			error.setMessage("Receiver Transaction Limit Exceeded.");
			valid = false;
			error.setValid(valid);
			return error;
		}

		Date date = transactionApi.getLastTranasactionTimeStamp(senderUser.getAccountDetail());
		if (date != null) {
			long lastTransactionTimeStamp = date.getTime();
			long currentTimeInMillis = System.currentTimeMillis();
			if ((currentTimeInMillis - lastTransactionTimeStamp) < 1000 * 60 * 1) {
				error.setMessage("Please wait for 1 min");
				error.setValid(false);
				return error;
			}
		}

		Date reverseTransactionDate = transactionApi.getLastTranasactionTimeStampByStatus(senderUser.getAccountDetail(),Status.Reversed);
		if(reverseTransactionDate != null){
			long timestamp = reverseTransactionDate.getTime();
			long currentTime = System.currentTimeMillis();
			if((currentTime - timestamp) < 1000 * 60 * 15){
				error.setMessage("Your last transaction was reversed. Please try again after 15 mins");
				error.setValid(false);
				return error;
			}
		}

		double currentBalance = senderUser.getAccountDetail().getBalance();
		double lastTransactionBalance = transactionApi.getLastSuccessTransaction(senderUser.getAccountDetail());
		if(currentBalance >= 0 && lastTransactionBalance >= 0) {
			if (currentBalance != lastTransactionBalance) {
				error.setMessage("Transaction is not validated");
				error.setValid(false);
				return error;
			}
		}

		error.setValid(valid);
		return error;
	}


	public TransactionError validateOfflinePayment(double amount,User merchant,User user,PQService service){
		TransactionError error = new TransactionError();
		boolean valid = true;
		if(merchant.getEmailStatus().equals(Status.Inactive)) {
			error.setMessage("Please verify your email "+merchant.getUserDetail().getEmail()+" before any transaction");
			error.setValid(false);
			return error;
		}

		if(user.getMobileStatus().equals(Status.Inactive)) {
			error.setMessage("User "+user.getUsername()+ " mobile number is not verified, hence not able to do transaction");
			error.setValid(false);
			return error;
		}

		double 	totalCreditMonthlyReceiver = transactionApi.getMonthlyCreditTransationTotalAmount(merchant.getAccountDetail());
		double 	monthlyCreditLimitReceiver = merchant.getAccountDetail().getAccountType().getMonthlyLimit();
        double transactionAmount = amount;
		double dailyTransactionLimit = user.getAccountDetail().getAccountType().getDailyLimit();
		double monthlyTransactionLimit = user.getAccountDetail().getAccountType().getMonthlyLimit();

		double totalDailyTransaction = userApi.dailyTransactionTotal(user);
		double totalMonthlyTransaction = userApi.monthlyTransactionTotal(user);
		double totalMonthlyDebit = transactionApi.getMonthlyDebitTransactionTotalAmount(user.getAccountDetail());
		double totalDailyDebit = transactionApi.getDailyDebitTransactionTotalAmount(user.getAccountDetail());

		if (totalDailyTransaction < 0 || totalMonthlyTransaction < 0) {
			error.setMessage("Opps!! Unable to process transaction.");
			error.setValid(false);
			return error;
		}

		PQCommission commission = commissionApi.findCommissionByServiceAndAmount(service, transactionAmount);
		double netCommissionValue = commissionApi.getCommissionValue(commission, transactionAmount);
		if (commission.getType().equalsIgnoreCase("POST")) {
			transactionAmount = transactionAmount + netCommissionValue;
		}

		if (!CommonValidation.balanceCheck(user.getAccountDetail().getBalance(), transactionAmount)) {
			if (commission.getType().equalsIgnoreCase("POST")) {
				error.setMessage("Insufficient Balance of Wallet . "+user.getUsername()+" current balance is Rs."
						+ user.getAccountDetail().getBalance() + " and service charges is Rs." + netCommissionValue);
			} else {
				error.setMessage("Insufficient Balance. of Wallet. "+user.getUsername()+" current balance is Rs."
						+ user.getAccountDetail().getBalance());
			}
			valid = false;
			error.setValid(valid);
			return error;
		} else if (!CommonValidation.dailyLimitCheck(dailyTransactionLimit, totalDailyDebit, transactionAmount)) {
			error.setMessage("Transaction Limit Exceeded of Wallet User "+user.getUsername()+". Daily transaction limit is Rs. "
					+ dailyTransactionLimit);
			valid = false;
			error.setValid(valid);
			return error;
		}else if (!CommonValidation.monthlyLimitCheck(monthlyTransactionLimit, totalMonthlyDebit,
				transactionAmount)) {
			error.setMessage("Transaction Limit Exceeded of Wallet User "+user.getUsername()+". Monthly transaction limit is Rs."
					+ monthlyTransactionLimit);
			valid = false;
			error.setValid(valid);
			return error;
		}else if (!CommonValidation.monthlyCreditLimitCheck(monthlyCreditLimitReceiver, totalCreditMonthlyReceiver,
				transactionAmount)) {
			error.setMessage("Your Transaction Limit Exceeded. Your monthly transaction limit is "+monthlyCreditLimitReceiver+" and your account already credited with Rs."+totalCreditMonthlyReceiver);
			valid = false;
			error.setValid(valid);
			return error;
		}
		double currentBalance = user.getAccountDetail().getBalance();
		double lastTransactionBalance = transactionApi.getLastSuccessTransaction(user.getAccountDetail());
		if(currentBalance >= 0 && lastTransactionBalance >= 0) {
			if (currentBalance != lastTransactionBalance) {
				error.setMessage("Transaction is not validated");
				error.setValid(false);
				return error;
			}
		}

		Date dateOfUser = transactionApi.getLastTranasactionTimeStamp(user.getAccountDetail());
		if (dateOfUser != null) {
			long lastTransactionTimeStamp = dateOfUser.getTime();
			long currentTimeInMillis = System.currentTimeMillis();
			if ((currentTimeInMillis - lastTransactionTimeStamp) < 1000 * 60 * 1) {
				error.setMessage("Please wait for 1 min");
				error.setValid(false);
				return error;
			}
		}



		Date reverseTransactionDate = transactionApi.getLastTranasactionTimeStampByStatus(user.getAccountDetail(),Status.Reversed);
		if(reverseTransactionDate != null){
			long timestamp = reverseTransactionDate.getTime();
			long currentTime = System.currentTimeMillis();
			if((currentTime - timestamp) < 1000 * 60 * 15) {
				error.setMessage("Wallet User "+user.getUsername()+" last transaction was reversed. Please try again after 15 mins");
				error.setValid(false);
				return error;
			}
		}

		error.setValid(valid);
		return error;

	}
	public TransactionError validateSharePoints(SharePointDTO dto) {
		TransactionError error = new TransactionError();
		boolean valid = true;

		User senderUser = userApi.findByUserName(dto.getSenderUsername());
		if (senderUser.getMobileStatus().equals(Status.Inactive)) {
			error.setMessage("Please verify your contactNo " + senderUser.getUserDetail().getContactNo() + " before any transaction");
			error.setValid(false);
			return error;
		}

		if (senderUser.getAccountDetail().getPoints() < dto.getPoints()) {
			error.setMessage("You don't have sufficient points to do that transaction");
			error.setValid(false);
			return error;
		}


		User receiverUser = userApi.findByUserName(dto.getReceiverUsername());
//		if (receiverUser.getEmailStatus().equals(Status.Inactive)
//				&& receiverUser.getMobileStatus().equals(Status.Active)) {
//			error.setMessage("Receiver email is not verified. Please verify " + senderUser.getUserDetail().getEmail()
//					+ " before any transaction");
//			error.setValid(false);
//			return error;
//		}



		error.setValid(valid);
		return error;
	}

	public TransactionError validateBillPayment(String amount, String senderUsername, PQService service) {
		TransactionError error = new TransactionError();
		boolean valid = true;

		User senderUser = userApi.findByUserName(senderUsername);
		if (senderUser.getMobileStatus().equals(Status.Inactive)) {
			error.setMessage(
					"Please verify your mobile " + senderUser.getUserDetail().getContactNo()+ " before any transaction");
			error.setValid(false);
			return error;
		}

		double transactionAmount = Double.parseDouble(amount);
		double dailyTransactionLimit = senderUser.getAccountDetail().getAccountType().getDailyLimit();
		double monthlyTransactionLimit = senderUser.getAccountDetail().getAccountType().getMonthlyLimit();
		double totalDailyTransaction = userApi.dailyTransactionTotal(senderUser);
		double totalMonthlyTransaction = userApi.monthlyTransactionTotal(senderUser);
		double totalMonthlyDebit = transactionApi.getMonthlyDebitTransactionTotalAmount(senderUser.getAccountDetail());
		if (totalDailyTransaction < 0 || totalMonthlyTransaction < 0) {
			error.setMessage("Opps!! Unable to process transaction.");
			error.setValid(false);
			return error;
		}
		PQCommission commission = commissionApi.findCommissionByServiceAndAmount(service, transactionAmount);
		double netCommissionValue = commissionApi.getCommissionValue(commission, transactionAmount);
		if (commission.getType().equalsIgnoreCase("POST")) {
			transactionAmount = transactionAmount + netCommissionValue;
		}
		if (!CommonValidation.balanceCheck(senderUser.getAccountDetail().getBalance(), transactionAmount)) {
			if (commission.getType().equalsIgnoreCase("POST")) {
				error.setMessage("Insufficient Balance. Your current balance is Rs."
						+ senderUser.getAccountDetail().getBalance() + " and service charges is Rs." + netCommissionValue);

			} else {
				error.setMessage("Insufficient Balance. Your current balance is Rs."
						+ senderUser.getAccountDetail().getBalance());
			}
			valid = false;
			error.setValid(valid);
			return error;
		}else if (!CommonValidation.monthlyLimitCheck(monthlyTransactionLimit, totalMonthlyDebit,
				transactionAmount)) {
			error.setMessage("Transaction Limit Exceeded. Your monthly transaction limit is Rs."
					+ monthlyTransactionLimit + " and you already made total transaction of Rs."
					+ totalMonthlyDebit);
			valid = false;
			error.setValid(valid);
			return error;
		}

		Date date = transactionApi.getLastTranasactionTimeStamp(senderUser.getAccountDetail());
		if (date != null) {
			long lastTransactionTimeStamp = date.getTime();
			long currentTimeInMillis = System.currentTimeMillis();
			if ((currentTimeInMillis - lastTransactionTimeStamp) < 1000 * 60 * 1) {
				error.setMessage("Please wait for 1 min");
				error.setValid(false);
				return error;
			}
		}
		Date reverseTransactionDate = transactionApi.getLastTranasactionTimeStampByStatus(senderUser.getAccountDetail(),Status.Reversed);
		if(reverseTransactionDate != null){
			long timestamp = reverseTransactionDate.getTime();
			long currentTime = System.currentTimeMillis();
			if((currentTime - timestamp) < 1000 * 60 * 15){
				error.setMessage("Your last transaction was reversed. Please try again after 15 mins");
				error.setValid(false);
				return error;
			}
		}

		double currentBalance = senderUser.getAccountDetail().getBalance();
		double lastTransactionBalance = transactionApi.getLastSuccessTransaction(senderUser.getAccountDetail());
		if(currentBalance >= 0 && lastTransactionBalance >= 0) {
			if (currentBalance != lastTransactionBalance) {
				error.setMessage("Transaction is not validated");
				error.setValid(false);
				return error;
			}
		}
		error.setValid(valid);
		return error;
	}

	public TransactionError validateLoadMoneyTransaction(String amount, String senderUsername, PQService service) {
		TransactionError error = new TransactionError();
		boolean valid = true;
		User senderUser = userApi.findByUserName(senderUsername);
		if (senderUser.getMobileStatus().equals(Status.Inactive)) {
			error.setMessage(
					"Please verify your mobile " + senderUser.getUserDetail().getContactNo() + " before any transaction");
			error.setValid(false);
			return error;
		}
		double transactionAmount = Double.parseDouble(amount);
		double currentWalletBalance = senderUser.getAccountDetail().getBalance();
		double balanceLimit = senderUser.getAccountDetail().getAccountType().getBalanceLimit();
		double monthlyTransactionLimit = senderUser.getAccountDetail().getAccountType().getMonthlyLimit();
		double totalMonthlyTransaction = userApi.monthlyLoadMoneyTransactionTotal(senderUser);
		double dailyTransactionLimit = senderUser.getAccountDetail().getAccountType().getDailyLimit();
		double totalCreditMonthly = transactionApi.getMonthlyCreditTransationTotalAmount(senderUser.getAccountDetail());
		if (totalMonthlyTransaction < 0) {
			error.setMessage("Opps!! Unable to process transaction.");
			error.setValid(false);
			return error;
		}

		if (senderUser.getMobileStatus().equals(Status.Inactive)) {
			error.setMessage(
					"Please verify your mobile " + senderUser.getUserDetail().getContactNo() + " before any transaction");
			valid = false;
			error.setValid(valid);
			return error;
		}else if (!CommonValidation.monthlyCreditLimitCheck(monthlyTransactionLimit, totalCreditMonthly,
				transactionAmount)) {
			error.setMessage("Monthly Credit Limit Exceeded. Your monthly limit is Rs." + monthlyTransactionLimit
					+ " and you've already credited total of Rs." + totalCreditMonthly);
			valid = false;
			error.setValid(valid);
			return error;
		} else if (!CommonValidation.receiverBalanceLimit(balanceLimit, currentWalletBalance, transactionAmount)) {
			error.setMessage(
					"Balance Limit Exceeded. " + senderUser.getUsername() + " balance limit is Rs." + balanceLimit);
			valid = false;
			error.setValid(valid);
			return error;
		}

		Date date = transactionApi.getLastTranasactionTimeStamp(senderUser.getAccountDetail());
		if (date != null) {
			long lastTransactionTimeStamp = date.getTime();
			long currentTimeInMillis = System.currentTimeMillis();
			if ((currentTimeInMillis - lastTransactionTimeStamp) < 1000 * 60 * 1) {
				error.setMessage("Please wait for 1 min");
				error.setValid(false);
				return error;
			}
		}
		Date reverseTransactionDate = transactionApi.getLastTranasactionTimeStampByStatus(senderUser.getAccountDetail(),Status.Reversed);
		if(reverseTransactionDate != null){
			long timestamp = reverseTransactionDate.getTime();
			long currentTime = System.currentTimeMillis();
			if((currentTime - timestamp) < 1000 * 60 * 15){
				error.setMessage("Your last transaction was reversed. Please try again after 15 mins");
				error.setValid(false);
				return error;
			}
		}
		error.setValid(valid);
		return error;
	}


    public TransactionError validateGenericTransaction(TransactionDTO dto) {
        TransactionError error = new TransactionError();
        boolean valid = true;

        User senderUser = userApi.findByUserName(dto.getSenderUsername());
        if (senderUser.getMobileStatus().equals(Status.Inactive)) {
            error.setMessage(
                    "Please verify your mobile " + senderUser.getUserDetail().getContactNo() + " before any transaction");
            error.setValid(false);
			error.setCode("F01");
            return error;
        }

        double transactionAmount = dto.getAmount();
        System.out.println("Amount : "+transactionAmount);
        double currentWalletBalance = senderUser.getAccountDetail().getBalance();
        System.out.println("Current Wallet Balance : " +currentWalletBalance);
        PQAccountType accountType = senderUser.getAccountDetail().getAccountType();
        System.out.println("Account Type : " +accountType.getDescription());
        double balanceLimit = senderUser.getAccountDetail().getAccountType().getBalanceLimit();
        System.out.println("Balance limit : " +balanceLimit);
        double dailyTransactionLimit = senderUser.getAccountDetail().getAccountType().getDailyLimit();
        System.out.println("Daily transaction limit : " +dailyTransactionLimit);
        double monthlyTransactionLimit = senderUser.getAccountDetail().getAccountType().getMonthlyLimit();
        System.out.println("Monthly transaction limit : " +monthlyTransactionLimit);
        double totalMonthlyTransaction = userApi.monthlyLoadMoneyTransactionTotal(senderUser);
        System.out.println("Total monthly transaction : " +totalMonthlyTransaction);
		double totalCreditMonthly = transactionApi.getMonthlyCreditTransationTotalAmount(senderUser.getAccountDetail());
		System.out.println("Total monthly credit : " +totalCreditMonthly);
		double totalDebitMonthly = transactionApi.getMonthlyDebitTransactionTotalAmount(senderUser.getAccountDetail());
		System.out.println("Total monthly debit : " +totalDebitMonthly);
		double totalCreditDaily = transactionApi.getDailyCreditTransationTotalAmount(senderUser.getAccountDetail());
		System.out.println("Total credit daily :" +totalCreditDaily);
		double totalDebitDaily = transactionApi.getDailyDebitTransactionTotalAmount(senderUser.getAccountDetail());
		System.out.println("Total debit daily : " +totalDebitDaily);
		
        if (totalMonthlyTransaction < 0) {
            error.setMessage("Opps!! Unable to process transaction.");
            error.setValid(false);
			error.setCode("F02");
            return error;
        }
        if (senderUser.getMobileStatus().equals(Status.Inactive)) {
            error.setMessage(
                    "Please verify your mobile " + senderUser.getUserDetail().getContactNo() + " before any transaction");
            valid = false;
			error.setCode("F04");
			error.setValid(valid);
			return error;
        }if (!CommonValidation.balanceCheck(senderUser.getAccountDetail().getBalance(), transactionAmount)) {
				error.setMessage("Insufficient Balance. Your current balance is Rs."
						+ senderUser.getAccountDetail().getBalance());
			valid = false;
			error.setValid(valid);
			return error;
		}else if (!CommonValidation.monthlyDebitLimitCheck(monthlyTransactionLimit, totalDebitMonthly,
                transactionAmount)) {
            error.setMessage("Monthly Debit Limit Exceeded. Your monthly debit limit is Rs." + monthlyTransactionLimit
                    + " and you've already debited Rs." + totalDebitMonthly);
            valid = false;
			error.setCode("F05");
			error.setValid(valid);
			return error;
        }
		Date date = transactionApi.getLastTranasactionTimeStamp(senderUser.getAccountDetail());
		if (date != null) {
			long lastTransactionTimeStamp = date.getTime();
			long currentTimeInMillis = System.currentTimeMillis();
			if ((currentTimeInMillis - lastTransactionTimeStamp) < 1000 * 60 * 1) {
				error.setMessage("Please wait for 1 min");
				error.setValid(false);
				return error;
			}
		}
		Date reverseTransactionDate = transactionApi.getLastTranasactionTimeStampByStatus(senderUser.getAccountDetail(),Status.Reversed);
		if(reverseTransactionDate != null){
			long timestamp = reverseTransactionDate.getTime();
			long currentTime = System.currentTimeMillis();
			if((currentTime - timestamp) < 1000 * 60 * 15){
				error.setMessage("Your last transaction was reversed. Please try again after 15 mins");
				error.setValid(false);
				return error;
			}
		}
		double currentBalance = senderUser.getAccountDetail().getBalance();
		double lastTransactionBalance = transactionApi.getLastSuccessTransaction(senderUser.getAccountDetail());
		if(currentBalance >= 0 && lastTransactionBalance >= 0) {
			if (currentBalance != lastTransactionBalance) {
				error.setMessage("Transaction is not validated");
				error.setValid(false);
				return error;
			}
		}
		error.setValid(valid);
        return error;
    }

	public TransactionError validateGenericTransactionForAPI(TransactionDTO dto) {
		TransactionError error = new TransactionError();
		boolean valid = true;

		User senderUser = userApi.findByUserName(dto.getSenderUsername());
		if (senderUser.getMobileStatus().equals(Status.Inactive)) {
			error.setMessage(
					"Please verify your mobile " + senderUser.getUserDetail().getContactNo() + " before any transaction");
			error.setValid(false);
			error.setCode("F01");
			return error;
		}
		double transactionAmount = dto.getAmount();
		double currentWalletBalance = senderUser.getAccountDetail().getBalance();
		double balanceLimit = senderUser.getAccountDetail().getAccountType().getBalanceLimit();
		double dailyTransactionLimit = senderUser.getAccountDetail().getAccountType().getDailyLimit();
		double monthlyTransactionLimit = senderUser.getAccountDetail().getAccountType().getMonthlyLimit();
		double totalMonthlyTransaction = userApi.monthlyLoadMoneyTransactionTotal(senderUser);
		double totalCreditMonthly = transactionApi.getMonthlyCreditTransationTotalAmount(senderUser.getAccountDetail());
		double totalDebitMonthly = transactionApi.getMonthlyDebitTransactionTotalAmount(senderUser.getAccountDetail());
		double totalCreditDaily = transactionApi.getDailyCreditTransationTotalAmount(senderUser.getAccountDetail());
		double totalDebitDaily = transactionApi.getDailyDebitTransactionTotalAmount(senderUser.getAccountDetail());
		System.err.print("Total Credit Monthly ::"+totalCreditMonthly);
		System.err.println("Total Debit Monthly ::"+totalDebitMonthly);
		System.err.println("Total Credit Daily ::"+totalCreditDaily);
		System.err.println("Total Debit Daily ::"+totalDebitDaily);
		if (totalMonthlyTransaction < 0) {
			error.setMessage("Opps!! Unable to process transaction.");
			error.setValid(false);
			error.setCode("F02");
			return error;
		} else if (!CommonValidation.monthlyCreditLimitCheck(monthlyTransactionLimit, totalCreditMonthly,
				transactionAmount)) {
			error.setMessage("Monthly Credit Limit Exceeded. Your monthly limit is Rs." + monthlyTransactionLimit
					+ " and you've already by credited Rs." + totalCreditMonthly);
			valid = false;
			error.setCode("F05");
			error.setValid(valid);
			return error;
		} else if (!CommonValidation.receiverBalanceLimit(balanceLimit, currentWalletBalance, transactionAmount)) {
			error.setMessage(
					"Balance Limit Exceeded. " + senderUser.getUsername() + " balance limit is Rs." + balanceLimit);
			valid = false;
			error.setCode("F06");
			error.setValid(valid);
			return error;
		}
		Date date = transactionApi.getLastTranasactionTimeStamp(senderUser.getAccountDetail());
		if (date != null) {
			long lastTransactionTimeStamp = date.getTime();
			long currentTimeInMillis = System.currentTimeMillis();
			if ((currentTimeInMillis - lastTransactionTimeStamp) < 1000 * 60 * 1) {
				error.setMessage("Please wait for 1 min");
				error.setValid(false);
				return error;
			}
		}
		Date reverseTransactionDate = transactionApi.getLastTranasactionTimeStampByStatus(senderUser.getAccountDetail(),Status.Reversed);
		if(reverseTransactionDate != null){
			long timestamp = reverseTransactionDate.getTime();
			long currentTime = System.currentTimeMillis();
			if((currentTime - timestamp) < 1000 * 60 * 15){
				error.setMessage("Your last transaction was reversed. Please try again after 15 mins");
				error.setValid(false);
				return error;
			}
		}
		double currentBalance = senderUser.getAccountDetail().getBalance();
		double lastTransactionBalance = transactionApi.getLastSuccessTransaction(senderUser.getAccountDetail());
		if(currentBalance >= 0 && lastTransactionBalance >= 0) {
			if (currentBalance != lastTransactionBalance) {
				error.setMessage("Transaction is not validated");
				error.setValid(false);
				return error;
			}
		}
		error.setValid(valid);
		return error;
	}
}
