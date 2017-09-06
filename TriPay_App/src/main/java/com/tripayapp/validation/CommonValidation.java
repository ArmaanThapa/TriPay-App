package com.tripayapp.validation;

import com.tripayapp.entity.User;
import com.tripayapp.util.CommonUtil;

public class CommonValidation {

	/**
	 * Checks String with length greater than 6
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean checkLength6(String str) {

		String temp = str.trim();
		if (temp.length() >= 6) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean checkValidMpin(String str) {
		String temp = str.trim();
		if (temp.length() == 4) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks String with length equal to 4
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean checkLength4(String str) {

		String temp = str.trim();
		if (temp.length() == 4) {
			return true;
		} else {
			return false;
		}

	}
	
	

	/**
	 * Checks whether a String is alphanumeric
	 * 
	 * @param str
	 * @return boolean
	 * 
	 */
	public static boolean isAlphanumeric(String str) {
		String temp = str.trim();
		if (temp.matches("[A-Za-z0-9]+")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * checks whether the string is completely numeric
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNumeric(String str) {
		String temp = str.trim();
		boolean isNumber = false;
		if (temp.matches("[0-9]+")) {
			isNumber = true;
		}
		return isNumber;
	}

	public static boolean isValidLoadMoneyTransaction(String str) {
		int amount = Integer.parseInt(str);
		if ((amount >= 1) && (amount <= 10000)) {
			return true;
		}
		return false;
	}

	/**
	 * checks the length of string is equal to 10
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkLength10(String str) {
		String temp = str.trim();
		int length = temp.length();
		boolean isValid = false;
		if (length == 10) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * for email validation
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmail(String str) {

		return false;
	}

	/**
	 * checks the number must be greater than 10
	 * 
	 * @param number
	 * @return boolean
	 */

	public static boolean isGreaterthan10(double number) {
		if (number >= 10)
			return true;
		else
			return false;
	}

	/**
	 * checks the validity of email
	 * 
	 * @param str
	 * @return boolean
	 * 
	 */

	public static boolean isValidMail(String str) {
		boolean isValid = false;
		if (str.contains("@") || str.contains(".")) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * checks whether String is null or not
	 * 
	 * @param str
	 * @return boolean
	 * 
	 */
	public static boolean isNull(String str) {
		if (str == null || str.isEmpty() || str == "") {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * checks whether String contains only alphabets
	 * 
	 * @param str
	 * @return boolean
	 * 
	 */

	public static boolean containsAlphabets(String str) {
		if (str.matches("[A-Za-z]+")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * checks whether String contains valid image extension
	 * 
	 * @param str
	 * @return boolean
	 * 
	 * 
	 */
	public static boolean isValidImageExtension(String str) {
		String temp = str.trim();
		if (temp.contains(".jpg") || temp.contains(".png") || temp.contains(".tiff") || temp.contains(".gif"))
			return true;
		else
			return false;
	}

	/**
	 * Checks transaction amount is less than or equal to user balance
	 * 
	 * @param userBalance
	 * @param transactionAmount
	 * @return
	 */
	public static boolean balanceCheck(double userBalance, double transactionAmount) {
		if (transactionAmount <= userBalance) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if user daily transaction limit is to mark or not if the
	 * transaction is carried out
	 * 
	 * @param dailyTransactionLimit
	 * @param totalDailyTransaction
	 * @param transactionAmount
	 * @return
	 */
	public static boolean dailyLimitCheck(double dailyTransactionLimit, double totalDailyTransaction,
			double transactionAmount) {
		if (dailyTransactionLimit >= (totalDailyTransaction + transactionAmount)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if user daily debit transaction limit is to mark or not if the
	 * transaction is carried out
	 * 
	 * @param dailyTransactionLimit
	 * @param totalDailyDebitTransactionAmount
	 * @return
	 */
	public static boolean dailyDebitLimitCheck(double dailyTransactionLimit, 
			double totalDailyDebitTransactionAmount, double transactionAmount) {
		if (dailyTransactionLimit >= (totalDailyDebitTransactionAmount + transactionAmount)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if user daily credit transaction limit is to mark or not if the
	 * transaction is carried out
	 * @param transactionAmount
	 * @param dailyTransactionLimit
	 * @param totalDailyCreditTransactionAmount
	 * @return
	 */
	public static boolean dailyCreditLimitCheck(double dailyTransactionLimit, 
			double totalDailyCreditTransactionAmount, double transactionAmount) {
		if (dailyTransactionLimit >= (totalDailyCreditTransactionAmount + transactionAmount)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if user monthly transaction limit is to mark or not if the
	 * transaction is carried out
	 * 
	 * @param monthlyTransactionLimit
	 * @param totalMonthlyTransaction
	 * @param transactionAmount
	 * @return
	 */
	public static boolean monthlyLimitCheck(double monthlyTransactionLimit, double totalMonthlyTransaction,
			double transactionAmount) {
		if (monthlyTransactionLimit >= (totalMonthlyTransaction + transactionAmount)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if user monthly debit transaction limit is to mark or not if the
	 * transaction is carried out
	 * 
	 * @param monthlyTransactionLimit
	 * @param totalMonthlyDebitTransactionAmount
	 * @return
	 */
	public static boolean monthlyDebitLimitCheck(double monthlyTransactionLimit, 
			double totalMonthlyDebitTransactionAmount, double transactionAmount) {
		if (monthlyTransactionLimit >= (totalMonthlyDebitTransactionAmount + transactionAmount)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if user monthly credit transaction limit is to mark or not if the
	 * transaction is carried out
	 * 
	 * @param monthlyTransactionLimit
	 * @param totalMonthlyCreditTransactionAmount
	 * @return
	 */
	public static boolean monthlyCreditLimitCheck(double monthlyTransactionLimit, 
			double totalMonthlyCreditTransactionAmount, double transactionAmount) {
		if (monthlyTransactionLimit >= (totalMonthlyCreditTransactionAmount + transactionAmount)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean monthlyLoadMoneyLimitCheck(double monthlyTransactionLimit, double totalMonthlyTransaction,
			double transactionAmount) {
		if (monthlyTransactionLimit >= (totalMonthlyTransaction + transactionAmount)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if receiver balance limit is to mark or not if the transaction is
	 * carried out
	 * 
	 * @param balanceLimit
	 * @param receiverBalance
	 * @param transactionAmount
	 * @return
	 */
	public static boolean receiverBalanceLimit(double balanceLimit, double receiverBalance, double transactionAmount) {
		if (balanceLimit >= (receiverBalance + transactionAmount)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isAmountInMinMaxRange(double minAmount, double maxAmount, String strAmount) {
		boolean valid = true;
		try {
			double amount = Double.parseDouble(strAmount);
			if (amount >= minAmount && amount <= maxAmount) {
				valid = true;
			} else {
				valid = false;
			}
		} catch (Exception e) {
			valid = false;
		}
		return valid;
	}
	
	public static String getOTP(String genOtp, User u) {
		String existOTP = u.getMobileToken();
		if (!CommonValidation.isNull(existOTP)) {
			if (!genOtp.equals(existOTP)) {
				return genOtp;
			} else {
				genOtp = CommonUtil.generateSixDigitNumericString();
				String getOpt = CommonValidation.getOTP(genOtp, u);
				return getOpt;
			}
		}
		return null;
	}

}
