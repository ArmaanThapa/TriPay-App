package com.tripayapp.controller.api.v1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQAccountDetail;
import com.tripayapp.model.SessionDTO;
import com.tripayapp.model.Status;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.PQAccountDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripayapp.api.IMailSenderApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.impl.TopupAndBillPaymentApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserDetail;
import com.tripayapp.mail.util.MailTemplate;
import com.tripayapp.model.MobileTopupDTO;
import com.tripayapp.repositories.PQServiceRepository;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class TestController implements MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private MessageSource messageSource;
	private final IMailSenderApi mailSenderApi;
	private final ITransactionApi transactionApi;
	private final PQServiceRepository pqServiceRepository;
	private final TopupAndBillPaymentApi topupAndBillPaymentApi;
	private final IUserApi userApi;
	public TestController(IMailSenderApi mailSenderApi, ITransactionApi transactionApi,
			PQServiceRepository pqServiceRepository, TopupAndBillPaymentApi topupAndBillPaymentApi,IUserApi userApi) {
		this.mailSenderApi = mailSenderApi;
		this.transactionApi = transactionApi;
		this.pqServiceRepository = pqServiceRepository;
		this.topupAndBillPaymentApi = topupAndBillPaymentApi;
		this.userApi = userApi;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(value = "/TestEmail", method = RequestMethod.GET)
	String authenticateUser(HttpServletRequest request, ModelMap model, HttpServletResponse response,
			HttpSession session) {
		UserDetail userDetailPA = new UserDetail();
		userDetailPA.setEmail("prajun.adhikary@gmail.com");
		User userPA = new User();
		userPA.setUserDetail(userDetailPA);
//		mailSenderApi.sendVijayaBankEmail("Test", MailTemplate.INVITE_FRIEND, userPA, null,null);

		UserDetail userDetailP = new UserDetail();
		userDetailP.setEmail("kumar.pankaj11@gmail.com");
		User userP = new User();
		userP.setUserDetail(userDetailP);
//		mailSenderApi.sendVijayaBankEmail("Test", MailTemplate.INVITE_FRIEND, userP, null,null);
		return "Test";
	}

	@RequestMapping(value = "/GetLastLogin/{username}", method = RequestMethod.GET)
	ResponseEntity<ResponseDTO> getLastLoginByUser(@PathVariable("role") String role, @PathVariable("device") String device, @PathVariable("language") String language,@PathVariable("username") String username,HttpServletRequest request, ModelMap model, HttpServletResponse response,
							HttpSession session) {
			ResponseDTO result = new ResponseDTO();
		User u = userApi.findByUserName(username);
		if(u != null) {
			result.setDetails(userApi.getLastLoginOfUser(u, Status.Success));
			result.setMessage("Login Details of "+u.getUserDetail().getFirstName());
		}
		return new ResponseEntity<ResponseDTO>(result,HttpStatus.OK);
	}

	@RequestMapping(value = "/Refund/{transactionRefNo}", method = RequestMethod.GET)
	ResponseEntity<ResponseDTO> refundTransactionById(@PathVariable("role") String role, @PathVariable("device") String device, @PathVariable("language") String language,@PathVariable("transactionRefNo") String transactionRefNo,HttpServletRequest request, ModelMap model, HttpServletResponse response,
												   HttpSession session) {
		ResponseDTO result = new ResponseDTO();
		transactionApi.reverseTransaction(transactionRefNo);
		return new ResponseEntity<ResponseDTO>(result,HttpStatus.OK);
	}

	@RequestMapping(value = "/TestKYCUpdation/{username}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ResponseDTO> updateKYCAccount(@PathVariable("role") String role, @PathVariable("device") String device, @PathVariable("language") String language, @PathVariable("username") String username,HttpServletRequest request,HttpServletResponse response){
		ResponseDTO result = new ResponseDTO();
		if(role.equalsIgnoreCase("User")) {
			boolean isUpdated = userApi.updateAccountType(username,true);
			User u = userApi.findByUserName(username);
			if(isUpdated) {
				result.setStatus(ResponseStatus.SUCCESS);
				result.setMessage("Account is updated");
				result.setDetails(u.getAccountDetail().getAccountType());
			}
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/TestVisa/{username}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ResponseDTO> testMVisaTransaction(@PathVariable("role") String role, @PathVariable("device") String device, @PathVariable("language") String language, @PathVariable("username") String username,HttpServletRequest request,HttpServletResponse response){
		ResponseDTO result = new ResponseDTO();
		if(role.equalsIgnoreCase("User")) {
			User u = userApi.findByUserName(username);
			if(u != null) {
				PQService visaService = pqServiceRepository.findServiceByCode("MVISA");
				if (visaService != null) {
					String transactionRefNo = String.valueOf(System.currentTimeMillis());
					transactionApi.initiateVisaPayment(10, "mVisa Payment", visaService, transactionRefNo, username, null);
					transactionApi.successVisaPayment(transactionRefNo);
					result.setStatus(ResponseStatus.SUCCESS);
					result.setMessage("Transaction Successful");
					result.setDetails(userApi.getWalletBalance(u));
				}
			}

		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/TestLoadMoneyInitiate/{transactionRefNo}", method = RequestMethod.GET)
	String loadMoneyInitiate(@PathVariable(value = "transactionRefNo") String transactionRefNo,
			HttpServletRequest request, ModelMap model, HttpServletResponse response, HttpSession session) {
		PQService service = pqServiceRepository.findServiceByCode("LMC");
		transactionApi.initiateLoadMoney(50, "Load Money", service, transactionRefNo, "9740116671", "");
		return "Test";
	}


	@RequestMapping(value = "/TestLoadMoneySuccess/{transactionRefNo}", method = RequestMethod.GET)
	String loadMoneySuccess(@PathVariable(value = "transactionRefNo") String transactionRefNo,
			HttpServletRequest request, ModelMap model, HttpServletResponse response, HttpSession session) {
		transactionApi.successLoadMoney(transactionRefNo);
		return "Test";
	}

	@RequestMapping(value = "/TestLoadMoneyFailed/{transactionRefNo}", method = RequestMethod.GET)
	String loadMoneyFailed(@PathVariable(value = "transactionRefNo") String transactionRefNo,
			HttpServletRequest request, ModelMap model, HttpServletResponse response, HttpSession session) {
//		transactionApi.failedLoadMoney(transactionRefNo);
		
		
		return "Test";
	}

	@RequestMapping(value = "/TestBillPaymentInitiate/{transactionRefNo}", method = RequestMethod.GET)
	String billPaymentInitiate(@PathVariable(value = "transactionRefNo") String transactionRefNo,
			HttpServletRequest request, ModelMap model, HttpServletResponse response, HttpSession session) {
		PQService service = pqServiceRepository.findServiceByCode("VATP");
		transactionApi.initiateBillPayment(50, "Bill Payment", service, transactionRefNo, "9461553581",
				"instantpay@payqwik.in", "");
		return "Test";
	}

	@RequestMapping(value = "/TestBillPaymentSuccess/{transactionRefNo}", method = RequestMethod.GET)
	String billPaymentSuccess(@PathVariable(value = "transactionRefNo") String transactionRefNo,
			HttpServletRequest request, ModelMap model, HttpServletResponse response, HttpSession session) {
		transactionApi.successBillPayment(transactionRefNo);
		return "Test";
	}

	@RequestMapping(value = "/TestBillPaymentFailed/{transactionRefNo}", method = RequestMethod.GET)
	String billPaymentFailed(@PathVariable(value = "transactionRefNo") String transactionRefNo,
			HttpServletRequest request, ModelMap model, HttpServletResponse response, HttpSession session) {
		transactionApi.failedBillPayment(transactionRefNo);
		return "Test";
	}

	@RequestMapping(value = "/TestBalanceUpdation/{username}", method = RequestMethod.GET)
	ResponseEntity<ResponseDTO> updateUserBalance(@PathVariable(value = "username") String username,
												  HttpServletRequest request, ModelMap model, HttpServletResponse response, HttpSession session) {
		User user = userApi.findByUserName(username);
		PQAccountDetail userAccount = user.getAccountDetail();
		double currentBalance = userAccount.getBalance();
		System.err.println("--Current Balance of User --"+currentBalance);
		double newBalance = currentBalance +10;
		userAccount.setBalance(newBalance);
		PQAccountDetail updatedDetails = userApi.saveOrUpdateAccount(userAccount);
		System.err.println(" updated balance ---- "+updatedDetails.getBalance());
		return null;
	}

	@RequestMapping(value = "/TestPrepaidTopup/{mobileNumber}/{code}/{amount}", method = RequestMethod.GET)
	String prepaidTopup(@PathVariable(value = "mobileNumber") String mobileNumber,
			@PathVariable(value = "code") String code, @PathVariable(value = "amount") String amount,
			HttpServletRequest request, ModelMap model, HttpServletResponse response, HttpSession session) {
		MobileTopupDTO dto = new MobileTopupDTO();
		dto.setAmount(amount);
		dto.setMobileNo(mobileNumber);
		dto.setServiceProvider(code);

		topupAndBillPaymentApi.prepaidTopup(dto, "9740116671", pqServiceRepository.findServiceByCode("VATP"));
		return "Test";
	}

}
