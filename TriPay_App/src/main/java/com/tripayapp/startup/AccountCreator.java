package com.tripayapp.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.api.ICommissionApi;
import com.tripayapp.entity.PQCommission;
import com.tripayapp.entity.PQOperator;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.PQServiceType;
import com.tripayapp.model.Status;
import com.tripayapp.repositories.PQOperatorRepository;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.PQServiceTypeRepository;

public class AccountCreator {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PQServiceTypeRepository pqServiceTypeRepository;
	private final PQOperatorRepository pqOperatorRepository;
	private final PQServiceRepository pqServiceRepository;
	private final ICommissionApi commissionApi;

	public AccountCreator(PQServiceTypeRepository pqServiceTypeRepository, PQOperatorRepository pqOperatorRepository,
			PQServiceRepository pqServiceRepository, ICommissionApi commissionApi) {
		this.pqServiceTypeRepository = pqServiceTypeRepository;
		this.pqOperatorRepository = pqOperatorRepository;
		this.pqServiceRepository = pqServiceRepository;
		this.commissionApi = commissionApi;
	}

	public void create() {
		PQOperator instantPay = pqOperatorRepository.findOperatorByName("InstantPay");
		if (instantPay == null) {
			instantPay = new PQOperator();
			instantPay.setName("InstantPay");
			instantPay.setStatus(Status.Active);
			pqOperatorRepository.save(instantPay);
		}

		PQOperator i2Space = pqOperatorRepository.findOperatorByName("I2Space");
		if (i2Space == null) {
			i2Space = new PQOperator();
			i2Space.setName("I2Space");
			i2Space.setStatus(Status.Active);
			pqOperatorRepository.save(i2Space);
		}

		PQOperator vpayqwik = pqOperatorRepository.findOperatorByName("VPayQwik");
		if (vpayqwik == null) {
			vpayqwik = new PQOperator();
			vpayqwik.setName("VPayQwik");
			vpayqwik.setStatus(Status.Active);
			pqOperatorRepository.save(vpayqwik);
		}

		PQOperator vijayaBank = pqOperatorRepository.findOperatorByName("Vijaya Bank");
		if (vijayaBank == null) {
			vijayaBank = new PQOperator();
			vijayaBank.setName("Vijaya Bank");
			vijayaBank.setStatus(Status.Active);
			pqOperatorRepository.save(vijayaBank);
		}



		PQServiceType serviceLoadMoney = pqServiceTypeRepository.findServiceTypeByName("Load Money");
		if (serviceLoadMoney == null) {
			serviceLoadMoney = new PQServiceType();
			serviceLoadMoney.setName("Load Money");
			serviceLoadMoney.setDescription("Load Money in VPayQwik.");
			serviceLoadMoney = pqServiceTypeRepository.save(serviceLoadMoney);
		}

		PQServiceType serviceMerchantPayment = pqServiceTypeRepository.findServiceTypeByName("Merchant Payment");
		if (serviceMerchantPayment == null) {
			serviceMerchantPayment = new PQServiceType();
			serviceMerchantPayment.setName("Merchant Payment");
			serviceMerchantPayment.setDescription("Pay to merchant.");
			serviceMerchantPayment = pqServiceTypeRepository.save(serviceMerchantPayment);
		}

		PQServiceType serviceFundTransfer = pqServiceTypeRepository.findServiceTypeByName("Fund Transfer");
		if (serviceFundTransfer == null) {
			serviceFundTransfer = new PQServiceType();
			serviceFundTransfer.setName("Fund Transfer");
			serviceFundTransfer.setDescription("Transfer fund to other users.");
			serviceFundTransfer = pqServiceTypeRepository.save(serviceFundTransfer);
		}

		PQServiceType serviceFundTransferBank = pqServiceTypeRepository.findServiceTypeByName("Fund Transfer Bank");
		if (serviceFundTransferBank == null) {
			serviceFundTransferBank = new PQServiceType();
			serviceFundTransferBank.setName("Fund Transfer Bank");
			serviceFundTransferBank.setDescription("Transfer fund to other users through Bank");
			serviceFundTransferBank = pqServiceTypeRepository.save(serviceFundTransferBank);
		}

		PQServiceType inviteFriends = pqServiceTypeRepository.findServiceTypeByName("Invite Friends");
		if (inviteFriends == null) {
			inviteFriends = new PQServiceType();
			inviteFriends.setName("Invite Friends");
			inviteFriends.setDescription("Invite Friends Through VPayQwik");
			inviteFriends = pqServiceTypeRepository.save(inviteFriends);
		}

		PQServiceType travel = pqServiceTypeRepository.findServiceTypeByName("I2Space Travel");
		if (travel == null) {
			travel = new PQServiceType();
			travel.setName("I2Space Travel");
			travel.setDescription("Travel Through VPayQwik");
			travel = pqServiceTypeRepository.save(travel);
		}


		PQServiceType walletReg = pqServiceTypeRepository.findServiceTypeByName("Wallet Registration");
		if (walletReg == null) {
			walletReg = new PQServiceType();
			walletReg.setName("Wallet Registration");
			walletReg.setDescription("Registration Through VPayQwik");
			walletReg = pqServiceTypeRepository.save(walletReg);
		}

		PQServiceType ppfAccount = pqServiceTypeRepository.findServiceTypeByName("Promotional Activity");
		if (ppfAccount == null) {
			ppfAccount = new PQServiceType();
			ppfAccount.setName("Promotional Activity");
			ppfAccount.setDescription("Promo Code in VPayQwik");
			ppfAccount = pqServiceTypeRepository.save(ppfAccount);
		}


		PQServiceType sharePoints = pqServiceTypeRepository.findServiceTypeByName("Share Points");
		if (sharePoints == null) {
			sharePoints = new PQServiceType();
			sharePoints.setName("Share Points");
			sharePoints.setDescription("Share Points in VPayQwik");
			sharePoints = pqServiceTypeRepository.save(sharePoints);
		}

		PQServiceType mVisaPayment = pqServiceTypeRepository.findServiceTypeByName("mVisa Payment");
		if (mVisaPayment == null) {
			mVisaPayment = new PQServiceType();
			mVisaPayment.setName("mVisa Payment");
			mVisaPayment.setDescription("mVisa Payment in VPayQwik");
			mVisaPayment = pqServiceTypeRepository.save(mVisaPayment);
		}



		PQService busTravel = pqServiceRepository.findServiceByCode("VBUS");
		if (busTravel == null) {
			busTravel = new PQService();
			busTravel.setName("I2Space BUS");
			busTravel.setDescription("Bus Travel in VPayQwik");
			busTravel.setMinAmount(10);
			busTravel.setMaxAmount(10000000);
			busTravel.setCode("VBUS");
			busTravel.setOperator(i2Space);
			busTravel.setOperatorCode("BUS");
			busTravel.setStatus(Status.Active);
			busTravel.setServiceType(travel);
			pqServiceRepository.save(busTravel);
		}

		PQCommission busCommission = new PQCommission();
		busCommission.setMinAmount(1);
		busCommission.setMaxAmount(10000);
		busCommission.setType("POST");
		busCommission.setValue(0);
		busCommission.setFixed(true);
		busCommission.setService(busTravel);
		busCommission.setIdentifier(commissionApi.createCommissionIdentifier(busCommission));
		if (commissionApi.findCommissionByIdentifier(busCommission.getIdentifier()) == null) {
			commissionApi.save(busCommission);
		}



		PQServiceType serviceTax = pqServiceTypeRepository.findServiceTypeByName("Service Tax");
		if (serviceTax == null) {
			serviceTax = new PQServiceType();
			serviceTax.setName("Service Tax");
			serviceTax.setDescription("Service Tax in VPayQwik");
			serviceTax = pqServiceTypeRepository.save(serviceTax);
		}

		PQService pointsService = pqServiceRepository.findServiceByCode("SPU");
		if (pointsService == null) {
			pointsService = new PQService();
			pointsService.setName("Share Points");
			pointsService.setDescription("Share Points in VPayQwik");
			pointsService.setMinAmount(10);
			pointsService.setMaxAmount(10000000);
			pointsService.setCode("SPU");
			pointsService.setOperator(vpayqwik);
			pointsService.setOperatorCode("SPU");
			pointsService.setStatus(Status.Active);
			pointsService.setServiceType(sharePoints);
			pqServiceRepository.save(pointsService);
		}

		PQCommission sharePointsCommission = new PQCommission();
		sharePointsCommission.setMinAmount(1);
		sharePointsCommission.setMaxAmount(10000);
		sharePointsCommission.setType("POST");
		sharePointsCommission.setValue(0);
		sharePointsCommission.setFixed(true);
		sharePointsCommission.setService(pointsService);
		sharePointsCommission.setIdentifier(commissionApi.createCommissionIdentifier(sharePointsCommission));
		if (commissionApi.findCommissionByIdentifier(sharePointsCommission.getIdentifier()) == null) {
			commissionApi.save(sharePointsCommission);
		}


		PQService vatService = pqServiceRepository.findServiceByCode("VAT");
		if (vatService == null) {
			vatService = new PQService();
			vatService.setName("VAT");
			vatService.setDescription("VAT in VPayQwik");
			vatService.setMinAmount(0);
			vatService.setMaxAmount(10000000);
			vatService.setCode("VAT");
			vatService.setOperator(vpayqwik);
			vatService.setOperatorCode("VAT");
			vatService.setStatus(Status.Active);
			vatService.setServiceType(serviceTax);
			pqServiceRepository.save(vatService);
		}

		PQCommission vatCommission = new PQCommission();
		vatCommission.setMinAmount(0);
		vatCommission.setMaxAmount(10000);
		vatCommission.setType("POST");
		vatCommission.setValue(14);
		vatCommission.setFixed(false);
		vatCommission.setService(vatService);
		vatCommission.setIdentifier(commissionApi.createCommissionIdentifier(vatCommission));
		if (commissionApi.findCommissionByIdentifier(vatCommission.getIdentifier()) == null) {
			commissionApi.save(vatCommission);
		}


		PQService krishiCess = pqServiceRepository.findServiceByCode("KKC");
		if (krishiCess == null) {
			krishiCess = new PQService();
			krishiCess.setName("Krishi Kalyan");
			krishiCess.setDescription("Krishi Kalyan Cess in VPayQwik");
			krishiCess.setMinAmount(0);
			krishiCess.setMaxAmount(100000);
			krishiCess.setCode("KKC");
			krishiCess.setOperator(vpayqwik);
			krishiCess.setOperatorCode("KKC");
			krishiCess.setStatus(Status.Active);
			krishiCess.setServiceType(serviceTax);
			pqServiceRepository.save(krishiCess);
		}

		PQCommission kkCommission = new PQCommission();
		kkCommission.setMinAmount(1);
		kkCommission.setMaxAmount(10000);
		kkCommission.setType("POST");
		kkCommission.setValue(0.5);
		kkCommission.setFixed(false);
		kkCommission.setService(krishiCess);
		kkCommission.setIdentifier(commissionApi.createCommissionIdentifier(kkCommission));
		if (commissionApi.findCommissionByIdentifier(kkCommission.getIdentifier()) == null) {
			commissionApi.save(kkCommission);
		}

		PQService sbCess = pqServiceRepository.findServiceByCode("SBC");
		if (sbCess == null) {
			sbCess = new PQService();
			sbCess.setName("Swacch Bharat Cess");
			sbCess.setDescription("Swacch Bharat Cess in VPayQwik");
			sbCess.setMinAmount(0);
			sbCess.setMaxAmount(100000);
			sbCess.setCode("SBC");
			sbCess.setOperator(vpayqwik);
			sbCess.setOperatorCode("SBC");
			sbCess.setStatus(Status.Active);
			sbCess.setServiceType(serviceTax);
			pqServiceRepository.save(sbCess);
		}

		PQCommission sbCommission = new PQCommission();
		sbCommission.setMinAmount(0);
		sbCommission.setMaxAmount(10000);
		sbCommission.setType("POST");
		sbCommission.setValue(0.5);
		sbCommission.setFixed(false);
		sbCommission.setService(sbCess);
		sbCommission.setIdentifier(commissionApi.createCommissionIdentifier(sbCommission));
		if (commissionApi.findCommissionByIdentifier(sbCommission.getIdentifier()) == null) {
			commissionApi.save(sbCommission);
		}

		PQService visaService = pqServiceRepository.findServiceByCode("MVISA");
		if (visaService == null) {
			visaService = new PQService();
			visaService.setName("mVisa Service");
			visaService.setDescription("mVisa in VPayQwik");
			visaService.setMinAmount(10);
			visaService.setMaxAmount(100000);
			visaService.setCode("MVISA");
			visaService.setOperator(vpayqwik);
			visaService.setOperatorCode("MVISA");
			visaService.setStatus(Status.Active);
			visaService.setServiceType(mVisaPayment);
			pqServiceRepository.save(visaService);
		}

		PQCommission visaCommission = new PQCommission();
		visaCommission.setMinAmount(1);
		visaCommission.setMaxAmount(100000);
		visaCommission.setType("POST");
		visaCommission.setValue(1);
		visaCommission.setFixed(true);
		visaCommission.setService(visaService);
		visaCommission.setIdentifier(commissionApi.createCommissionIdentifier(visaCommission));
		if (commissionApi.findCommissionByIdentifier(visaCommission.getIdentifier()) == null) {
			commissionApi.save(visaCommission);
		}

		PQService promotionalService = pqServiceRepository.findServiceByCode("PPS");
		if (promotionalService == null) {
			promotionalService = new PQService();
			promotionalService.setName("Promotional Activity");
			promotionalService.setDescription("Send Promo Codes");
			promotionalService.setMinAmount(10);
			promotionalService.setMaxAmount(10000);
			promotionalService.setCode("PPS");
			promotionalService.setOperator(vpayqwik);
			promotionalService.setOperatorCode("PPS");
			promotionalService.setStatus(Status.Active);
			promotionalService.setServiceType(ppfAccount);
			pqServiceRepository.save(promotionalService);
		}

		PQService registrationService = pqServiceRepository.findServiceByCode("RVP");
		if (registrationService == null) {
			registrationService = new PQService();
			registrationService.setName("Registration");
			registrationService.setDescription("Registration Through VPayQwik");
			registrationService.setMinAmount(0);
			registrationService.setMaxAmount(1);
			registrationService.setCode("RVP");
			registrationService.setOperator(vpayqwik);
			registrationService.setOperatorCode("RVP");
			registrationService.setStatus(Status.Active);
			registrationService.setServiceType(walletReg);
			pqServiceRepository.save(registrationService);
		}


		PQService invitationService = pqServiceRepository.findServiceByCode("IVP");
		if (invitationService == null) {
			invitationService = new PQService();
			invitationService.setName("Invite Friends");
			invitationService.setDescription("Invite Friends Through VPayQwik");
			invitationService.setMinAmount(10);
			invitationService.setMaxAmount(100);
			invitationService.setCode("IVP");
			invitationService.setOperator(vpayqwik);
			invitationService.setOperatorCode("IVP");
			invitationService.setStatus(Status.Active);
			invitationService.setServiceType(inviteFriends);
			pqServiceRepository.save(invitationService);
		}


		PQService loadMoneyService = pqServiceRepository.findServiceByOperatorCode("LMC");
		if (loadMoneyService == null) {
			loadMoneyService = new PQService();
			loadMoneyService.setName("Load Money");
			loadMoneyService.setDescription("Load Money User");
			loadMoneyService.setMinAmount(1);
			loadMoneyService.setMaxAmount(10000);
			loadMoneyService.setCode("LMC");
			loadMoneyService.setOperatorCode("LMC");
			loadMoneyService.setServiceType(serviceLoadMoney);
			loadMoneyService.setOperator(vpayqwik);
			loadMoneyService.setStatus(Status.Active);
			pqServiceRepository.save(loadMoneyService);
		}

		PQCommission commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(loadMoneyService);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService loadMoneyVijayaBank = pqServiceRepository.findServiceByOperatorCode("LMB");
		if (loadMoneyVijayaBank == null) {
			loadMoneyVijayaBank = new PQService();
			loadMoneyVijayaBank.setName("Load Money VNet");
			loadMoneyVijayaBank.setDescription("Load Money By VNet Banking");
			loadMoneyVijayaBank.setMinAmount(1);
			loadMoneyVijayaBank.setMaxAmount(10000);
			loadMoneyVijayaBank.setCode("LMB");
			loadMoneyVijayaBank.setOperatorCode("LMB");
			loadMoneyVijayaBank.setServiceType(serviceLoadMoney);
			loadMoneyVijayaBank.setOperator(vijayaBank);
			loadMoneyVijayaBank.setStatus(Status.Active);
			pqServiceRepository.save(loadMoneyVijayaBank);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(loadMoneyVijayaBank);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService payAtStoreService = pqServiceRepository.findServiceByOperatorCode("PAS");
		if (payAtStoreService == null) {
			payAtStoreService = new PQService();
			payAtStoreService.setName("Pay At Store");
			payAtStoreService.setDescription("Pay At Store");
			payAtStoreService.setMinAmount(1);
			payAtStoreService.setMaxAmount(10000);
			payAtStoreService.setCode("PAS");
			payAtStoreService.setOperatorCode("PAS");
			payAtStoreService.setServiceType(serviceFundTransfer);
			payAtStoreService.setOperator(vpayqwik);
			payAtStoreService.setStatus(Status.Active);
			pqServiceRepository.save(payAtStoreService);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(payAtStoreService);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}


		PQService refundMoney = pqServiceRepository.findServiceByOperatorCode("RMU");
		if (refundMoney == null) {
			refundMoney = new PQService();
			refundMoney.setName("Refund Money");
			refundMoney.setDescription("Refund Money from User to Account");
			refundMoney.setMinAmount(1);
			refundMoney.setMaxAmount(100000);
			refundMoney.setCode("RMU");
			refundMoney.setOperatorCode("RMU");
			refundMoney.setServiceType(serviceFundTransfer);
			refundMoney.setOperator(vpayqwik);
			refundMoney.setStatus(Status.Active);
			pqServiceRepository.save(refundMoney);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(refundMoney);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}


		PQService sendMoneyRegisteredUser = pqServiceRepository.findServiceByOperatorCode("SMR");
		if (sendMoneyRegisteredUser == null) {
			sendMoneyRegisteredUser = new PQService();
			sendMoneyRegisteredUser.setName("Send Money");
			sendMoneyRegisteredUser.setDescription("Send Money Registered User");
			sendMoneyRegisteredUser.setMinAmount(1);
			sendMoneyRegisteredUser.setMaxAmount(50000);
			sendMoneyRegisteredUser.setCode("SMR");
			sendMoneyRegisteredUser.setOperatorCode("SMR");
			sendMoneyRegisteredUser.setServiceType(serviceFundTransfer);
			sendMoneyRegisteredUser.setOperator(vpayqwik);
			sendMoneyRegisteredUser.setStatus(Status.Active);
			pqServiceRepository.save(sendMoneyRegisteredUser);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(50000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(sendMoneyRegisteredUser);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}


		PQService sendMoneyToBank = pqServiceRepository.findServiceByOperatorCode("SMB");

		if (sendMoneyToBank == null) {
			sendMoneyToBank = new PQService();
			sendMoneyToBank.setName("Send Money Bank");
			sendMoneyToBank.setDescription("Send Money to Bank Account");
			sendMoneyToBank.setMinAmount(500);
			sendMoneyToBank.setMaxAmount(49999);
			sendMoneyToBank.setCode("SMB");
			sendMoneyToBank.setOperatorCode("SMB");
			sendMoneyToBank.setServiceType(serviceFundTransferBank);
			sendMoneyToBank.setOperator(vpayqwik);
			sendMoneyToBank.setStatus(Status.Active);
			pqServiceRepository.save(sendMoneyToBank);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(49999);
		commission.setType("POST");
		commission.setValue(2);
		commission.setFixed(true);
		commission.setService(sendMoneyToBank);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}
		
		
		PQService sendMoneyToMBank = pqServiceRepository.findServiceByOperatorCode("SMMB");

		if (sendMoneyToMBank == null) {
			sendMoneyToMBank = new PQService();
			sendMoneyToMBank.setName("NEFT Merchant");
			sendMoneyToMBank.setDescription("NEFT to Merchant Bank A/C");
			sendMoneyToMBank.setMinAmount(500);
			sendMoneyToMBank.setMaxAmount(5000000);
			sendMoneyToMBank.setCode("SMMB");
			sendMoneyToMBank.setOperatorCode("SMMB");
			sendMoneyToMBank.setServiceType(serviceFundTransferBank);
			sendMoneyToMBank.setOperator(vijayaBank);
			sendMoneyToMBank.setStatus(Status.Active);
			pqServiceRepository.save(sendMoneyToMBank);
		}

		commission = new PQCommission();
		commission.setMinAmount(500);
		commission.setMaxAmount(5000000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(sendMoneyToMBank);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		

		PQService sendMoneyUnregisteredUser = pqServiceRepository.findServiceByOperatorCode("SMU");
		if (sendMoneyUnregisteredUser == null) {
			sendMoneyUnregisteredUser = new PQService();
			sendMoneyUnregisteredUser.setName("Send Money");
			sendMoneyUnregisteredUser.setDescription("Send Money Unregistered User");
			sendMoneyUnregisteredUser.setMinAmount(1);
			sendMoneyUnregisteredUser.setMaxAmount(50000);
			sendMoneyUnregisteredUser.setCode("SMU");
			sendMoneyUnregisteredUser.setOperatorCode("SMU");
			sendMoneyUnregisteredUser.setServiceType(serviceFundTransfer);
			sendMoneyUnregisteredUser.setOperator(vpayqwik);
			sendMoneyUnregisteredUser.setStatus(Status.Active);
			pqServiceRepository.save(sendMoneyUnregisteredUser);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(50000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(sendMoneyUnregisteredUser);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQServiceType serviceBillPayment = pqServiceTypeRepository.findServiceTypeByName("Bill Payment");
		if (serviceBillPayment == null) {
			serviceBillPayment = new PQServiceType();
			serviceBillPayment.setName("Bill Payment");
			serviceBillPayment.setDescription("Pay mobile and other utility bills.");
			serviceBillPayment = pqServiceTypeRepository.save(serviceBillPayment);
		}

		PQService airtelPrepaidTopup = pqServiceRepository.findServiceByOperatorCode("ATP");
		if (airtelPrepaidTopup == null) {
			airtelPrepaidTopup = new PQService();
			airtelPrepaidTopup.setName("Airtel");
			airtelPrepaidTopup.setDescription("Airtel Prepaid Topup");
			airtelPrepaidTopup.setMinAmount(1);
			airtelPrepaidTopup.setMaxAmount(10000);
			airtelPrepaidTopup.setCode("VATP");
			airtelPrepaidTopup.setOperatorCode("ATP");
			airtelPrepaidTopup.setServiceType(serviceBillPayment);
			airtelPrepaidTopup.setOperator(vpayqwik);
			airtelPrepaidTopup.setStatus(Status.Active);
			pqServiceRepository.save(airtelPrepaidTopup);
		}

		PQService videoconD2H = pqServiceRepository.findServiceByOperatorCode("VTK");
		if (videoconD2H == null) {
			videoconD2H = new PQService();
			videoconD2H.setName("VideoconD2H");
			videoconD2H.setDescription("VideoconD2H Connection");
			videoconD2H.setMinAmount(1000);
			videoconD2H.setMaxAmount(50000);
			videoconD2H.setCode("VVTK");
			videoconD2H.setOperatorCode("VTK");
			videoconD2H.setServiceType(serviceBillPayment);
			videoconD2H.setOperator(vpayqwik);
			videoconD2H.setStatus(Status.Active);
			pqServiceRepository.save(videoconD2H);
		}

		PQService TataSky = pqServiceRepository.findServiceByOperatorCode("TTK");
		if (TataSky == null) {
			TataSky = new PQService();
			TataSky.setName("Tata Sky");
			TataSky.setDescription("TataSky Connection");
			TataSky.setMinAmount(1000);
			TataSky.setMaxAmount(50000);
			TataSky.setCode("VTTK");
			TataSky.setOperatorCode("TTK");
			TataSky.setServiceType(serviceBillPayment);
			TataSky.setOperator(vpayqwik);
			TataSky.setStatus(Status.Active);
			pqServiceRepository.save(TataSky);
		}

		PQService SunDirect = pqServiceRepository.findServiceByOperatorCode("STK");
		if (SunDirect == null) {
			SunDirect = new PQService();
			SunDirect.setName("SunDirect");
			SunDirect.setDescription("SunDirect Connection");
			SunDirect.setMinAmount(1000);
			SunDirect.setMaxAmount(50000);
			SunDirect.setCode("VSTK");
			SunDirect.setOperatorCode("STK");
			SunDirect.setServiceType(serviceBillPayment);
			SunDirect.setOperator(vpayqwik);
			SunDirect.setStatus(Status.Active);
			pqServiceRepository.save(SunDirect);
		}

		PQService DishTV = pqServiceRepository.findServiceByOperatorCode("DTK");
		if (DishTV == null) {
			DishTV = new PQService();
			DishTV.setName("Dish TV");
			DishTV.setDescription("DishTV Connection");
			DishTV.setMinAmount(1000);
			DishTV.setMaxAmount(50000);
			DishTV.setCode("VDTK");
			DishTV.setOperatorCode("DTK");
			DishTV.setServiceType(serviceBillPayment);
			DishTV.setOperator(vpayqwik);
			DishTV.setStatus(Status.Active);
			pqServiceRepository.save(DishTV);
		}

		PQService AirtelDigitalTV = pqServiceRepository.findServiceByOperatorCode("ATK");
		if (AirtelDigitalTV == null) {
			AirtelDigitalTV = new PQService();
			AirtelDigitalTV.setName("AirtelDigital TV");
			AirtelDigitalTV.setDescription("AirtelDigital TV Connection");
			AirtelDigitalTV.setMinAmount(1000);
			AirtelDigitalTV.setMaxAmount(50000);
			AirtelDigitalTV.setCode("VATK");
			AirtelDigitalTV.setOperatorCode("ATK");
			AirtelDigitalTV.setServiceType(serviceBillPayment);
			AirtelDigitalTV.setOperator(vpayqwik);
			AirtelDigitalTV.setStatus(Status.Active);
			pqServiceRepository.save(AirtelDigitalTV);
		}

		PQService VideoconD2H = pqServiceRepository.findServiceByOperatorCode("VTV");
		if (VideoconD2H == null) {
			VideoconD2H = new PQService();
			VideoconD2H.setName("VideoconD2H");
			VideoconD2H.setDescription("VideoconD2H DTH-Connection");
			VideoconD2H.setMinAmount(50);
			VideoconD2H.setMaxAmount(65000);
			VideoconD2H.setCode("VVTV");
			VideoconD2H.setOperatorCode("VTV");
			VideoconD2H.setServiceType(serviceBillPayment);
			VideoconD2H.setOperator(instantPay);
			VideoconD2H.setStatus(Status.Active);
			pqServiceRepository.save(VideoconD2H);
		}

		commission = new PQCommission();
		commission.setMinAmount(50);
		commission.setMaxAmount(65000);
		commission.setType("PRE");
		commission.setValue(3.60);
		commission.setFixed(false);
		commission.setService(VideoconD2H);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService TataSkyOnline = pqServiceRepository.findServiceByOperatorCode("OTV");
		if (TataSkyOnline == null) {
			TataSkyOnline = new PQService();
			TataSkyOnline.setName("TatakyOnline");
			TataSkyOnline.setDescription("TataSkyOnline DTH-Connection");
			TataSkyOnline.setMinAmount(8);
			TataSkyOnline.setMaxAmount(30000);
			TataSkyOnline.setCode("VOTV");
			TataSkyOnline.setOperatorCode("OTV");
			TataSkyOnline.setServiceType(serviceBillPayment);
			TataSkyOnline.setOperator(instantPay);
			TataSkyOnline.setStatus(Status.Active);
			pqServiceRepository.save(TataSkyOnline);
		}

		commission = new PQCommission();
		commission.setMinAmount(8);
		commission.setMaxAmount(30000);
		commission.setType("PRE");
		commission.setValue(1.60);
		commission.setFixed(false);
		commission.setService(TataSkyOnline);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tataSky = pqServiceRepository.findServiceByOperatorCode("TTV");
		if (tataSky == null) {
			tataSky = new PQService();
			tataSky.setName("TataSkyDTH");
			tataSky.setDescription("TataSky DTH-Connection");
			tataSky.setMinAmount(8);
			tataSky.setMaxAmount(30000);
			tataSky.setCode("VTTV");
			tataSky.setOperatorCode("TTV");
			tataSky.setServiceType(serviceBillPayment);
			tataSky.setOperator(instantPay);
			tataSky.setStatus(Status.Active);
			pqServiceRepository.save(tataSky);
		}

		commission = new PQCommission();
		commission.setMinAmount(8);
		commission.setMaxAmount(30000);
		commission.setType("PRE");
		commission.setValue(3.60);
		commission.setFixed(false);
		commission.setService(tataSky);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}
		PQService sunDirect = pqServiceRepository.findServiceByOperatorCode("STV");
		if (sunDirect == null) {
			sunDirect = new PQService();
			sunDirect.setName("SunDirect DTH");
			sunDirect.setDescription("sunDirect DTH-Connection");
			sunDirect.setMinAmount(10);
			sunDirect.setMaxAmount(10000);
			sunDirect.setCode("VSTV");
			sunDirect.setOperatorCode("STV");
			sunDirect.setServiceType(serviceBillPayment);
			sunDirect.setOperator(instantPay);
			sunDirect.setStatus(Status.Active);
			pqServiceRepository.save(sunDirect);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(3.70);
		commission.setFixed(false);
		commission.setService(sunDirect);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService RelianceDigitalTV = pqServiceRepository.findServiceByOperatorCode("RTV");
		if (RelianceDigitalTV == null) {
			RelianceDigitalTV = new PQService();
			RelianceDigitalTV.setName("RelianceDigitalTV DTH");
			RelianceDigitalTV.setDescription("RelianceDigitalTV DTH-Connection");
			RelianceDigitalTV.setMinAmount(10);
			RelianceDigitalTV.setMaxAmount(10000);
			RelianceDigitalTV.setCode("VRTV");
			RelianceDigitalTV.setOperatorCode("RTV");
			RelianceDigitalTV.setServiceType(serviceBillPayment);
			RelianceDigitalTV.setOperator(instantPay);
			RelianceDigitalTV.setStatus(Status.Active);
			pqServiceRepository.save(RelianceDigitalTV);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(3.60);
		commission.setFixed(false);
		commission.setService(RelianceDigitalTV);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService dishTV = pqServiceRepository.findServiceByOperatorCode("DTV");
		if (dishTV == null) {
			dishTV = new PQService();
			dishTV.setName("DishTV");
			dishTV.setDescription("DishTV DTH-Connection");
			dishTV.setMinAmount(10);
			dishTV.setMaxAmount(64490);
			dishTV.setCode("VDTV");
			dishTV.setOperatorCode("DTV");
			dishTV.setServiceType(serviceBillPayment);
			dishTV.setOperator(instantPay);
			dishTV.setStatus(Status.Active);
			pqServiceRepository.save(dishTV);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(64490);
		commission.setType("PRE");
		commission.setValue(2.50);
		commission.setFixed(false);
		commission.setService(dishTV);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService airtelDigitaltvDTH = pqServiceRepository.findServiceByOperatorCode("ATV");
		if (airtelDigitaltvDTH == null) {
			airtelDigitaltvDTH = new PQService();
			airtelDigitaltvDTH.setName("AirtelDigitalTVDTH");
			airtelDigitaltvDTH.setDescription("AirtelDigital DTH-Connection");
			airtelDigitaltvDTH.setMinAmount(100);
			airtelDigitaltvDTH.setMaxAmount(15000);
			airtelDigitaltvDTH.setCode("VATV");
			airtelDigitaltvDTH.setOperatorCode("ATV");
			airtelDigitaltvDTH.setServiceType(serviceBillPayment);
			airtelDigitaltvDTH.setOperator(instantPay);
			airtelDigitaltvDTH.setStatus(Status.Active);
			pqServiceRepository.save(airtelDigitaltvDTH);
		}

		commission = new PQCommission();
		commission.setMinAmount(100);
		commission.setMaxAmount(15000);
		commission.setType("PRE");
		commission.setValue(0.80);
		commission.setFixed(false);
		commission.setService(airtelDigitaltvDTH);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tseclTripura = pqServiceRepository.findServiceByOperatorCode("TTE");
		if (tseclTripura == null) {
			tseclTripura = new PQService();
			tseclTripura.setName("TseclTripura");
			tseclTripura.setStatus(Status.Active);
			tseclTripura.setDescription("TseclTripura Electricity Board");
			tseclTripura.setMinAmount(1);
			tseclTripura.setMaxAmount(100000);
			tseclTripura.setCode("VTTE");
			tseclTripura.setOperatorCode("TTE");
			tseclTripura.setServiceType(serviceBillPayment);
			tseclTripura.setOperator(vpayqwik);
			tseclTripura.setStatus(Status.Active);
			pqServiceRepository.save(tseclTripura);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(tseclTripura);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService torrentPower = pqServiceRepository.findServiceByOperatorCode("TPE");
		if (torrentPower == null) {
			torrentPower = new PQService();
			torrentPower.setName("TorrentPower");
			torrentPower.setDescription("TorrentPower Electricity Board");
			torrentPower.setMinAmount(1);
			torrentPower.setMaxAmount(100000);
			torrentPower.setCode("VTPE");
			torrentPower.setOperatorCode("TPE");
			torrentPower.setServiceType(serviceBillPayment);
			torrentPower.setOperator(vpayqwik);
			torrentPower.setStatus(Status.Active);
			pqServiceRepository.save(torrentPower);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(torrentPower);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tataPowerDelhi = pqServiceRepository.findServiceByOperatorCode("NDE");
		if (tataPowerDelhi == null) {
			tataPowerDelhi = new PQService();
			tataPowerDelhi.setName("TataPowerDelhi");
			tataPowerDelhi.setDescription("TataPowerDelhi Electricity Board");
			tataPowerDelhi.setMinAmount(1);
			tataPowerDelhi.setMaxAmount(100000);
			tataPowerDelhi.setCode("VNDE");
			tataPowerDelhi.setOperatorCode("NDE");
			tataPowerDelhi.setServiceType(serviceBillPayment);
			tataPowerDelhi.setOperator(vpayqwik);
			tataPowerDelhi.setStatus(Status.Active);
			pqServiceRepository.save(tataPowerDelhi);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(tataPowerDelhi);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService southernPowerTELANGANA = pqServiceRepository.findServiceByOperatorCode("STE");
		if (southernPowerTELANGANA == null) {
			southernPowerTELANGANA = new PQService();
			southernPowerTELANGANA.setName("SouthernPowerTELANGANA");
			southernPowerTELANGANA.setDescription("Southern Power TELANGANA Electricity Board");
			southernPowerTELANGANA.setMinAmount(1);
			southernPowerTELANGANA.setMaxAmount(100000);
			southernPowerTELANGANA.setCode("VSTE");
			southernPowerTELANGANA.setOperatorCode("STE");
			southernPowerTELANGANA.setServiceType(serviceBillPayment);
			southernPowerTELANGANA.setOperator(vpayqwik);
			southernPowerTELANGANA.setStatus(Status.Active);
			pqServiceRepository.save(southernPowerTELANGANA);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(southernPowerTELANGANA);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService southernPowerAndraPradesh = pqServiceRepository.findServiceByOperatorCode("SAE");
		if (southernPowerAndraPradesh == null) {
			southernPowerAndraPradesh = new PQService();
			southernPowerAndraPradesh.setName("SouthernPowerAndraPradesh");
			southernPowerAndraPradesh.setDescription("Southern Power AndraPradesh Electricity Board");
			southernPowerAndraPradesh.setMinAmount(1);
			southernPowerAndraPradesh.setMaxAmount(100000);
			southernPowerAndraPradesh.setCode("VSAE");
			southernPowerAndraPradesh.setOperatorCode("SAE");
			southernPowerAndraPradesh.setServiceType(serviceBillPayment);
			southernPowerAndraPradesh.setOperator(vpayqwik);
			southernPowerAndraPradesh.setStatus(Status.Active);
			pqServiceRepository.save(southernPowerAndraPradesh);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(southernPowerAndraPradesh);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService relianceEnergyMumbai = pqServiceRepository.findServiceByOperatorCode("REE");
		if (relianceEnergyMumbai == null) {
			relianceEnergyMumbai = new PQService();
			relianceEnergyMumbai.setName("RelianceEnergyMumbai");
			relianceEnergyMumbai.setDescription("Reliance Energy Mumbai Electricity Board");
			relianceEnergyMumbai.setMinAmount(1);
			relianceEnergyMumbai.setMaxAmount(100000);
			relianceEnergyMumbai.setCode("VREE");
			relianceEnergyMumbai.setOperatorCode("REE");
			relianceEnergyMumbai.setServiceType(serviceBillPayment);
			relianceEnergyMumbai.setOperator(vpayqwik);
			relianceEnergyMumbai.setStatus(Status.Active);
			pqServiceRepository.save(relianceEnergyMumbai);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0.0);
		commission.setFixed(true);
		commission.setService(relianceEnergyMumbai);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService paschimKshetraVitaranMADHYAPRADESH = pqServiceRepository.findServiceByOperatorCode("MPE");
		if (paschimKshetraVitaranMADHYAPRADESH == null) {
			paschimKshetraVitaranMADHYAPRADESH = new PQService();
			paschimKshetraVitaranMADHYAPRADESH.setName("PaschimKshetraVitaranMADHYAPRADESH");
			paschimKshetraVitaranMADHYAPRADESH
					.setDescription("Paschim Kshetra Vitaran MADHYAPRADESH Electricity Board");
			paschimKshetraVitaranMADHYAPRADESH.setMinAmount(1);
			paschimKshetraVitaranMADHYAPRADESH.setMaxAmount(100000);
			paschimKshetraVitaranMADHYAPRADESH.setCode("VMPE");
			paschimKshetraVitaranMADHYAPRADESH.setOperatorCode("MPE");
			paschimKshetraVitaranMADHYAPRADESH.setServiceType(serviceBillPayment);
			paschimKshetraVitaranMADHYAPRADESH.setOperator(vpayqwik);
			paschimKshetraVitaranMADHYAPRADESH.setStatus(Status.Active);
			pqServiceRepository.save(paschimKshetraVitaranMADHYAPRADESH);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(paschimKshetraVitaranMADHYAPRADESH);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService odishaDiscomsODISHA = pqServiceRepository.findServiceByOperatorCode("DOE");
		if (odishaDiscomsODISHA == null) {
			odishaDiscomsODISHA = new PQService();
			odishaDiscomsODISHA.setName("Odisha Discoms ODISHA");
			odishaDiscomsODISHA.setDescription("Odisha Discoms ODISHA Electricity Board");
			odishaDiscomsODISHA.setMinAmount(1);
			odishaDiscomsODISHA.setMaxAmount(100000);
			odishaDiscomsODISHA.setCode("VDOE");
			odishaDiscomsODISHA.setOperatorCode("DOE");
			odishaDiscomsODISHA.setServiceType(serviceBillPayment);
			odishaDiscomsODISHA.setOperator(vpayqwik);
			odishaDiscomsODISHA.setStatus(Status.Active);
			pqServiceRepository.save(odishaDiscomsODISHA);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(odishaDiscomsODISHA);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService noidaPowerNOIDA = pqServiceRepository.findServiceByOperatorCode("NUE");
		if (noidaPowerNOIDA == null) {
			noidaPowerNOIDA = new PQService();
			noidaPowerNOIDA.setName("NoidaPowerNOIDA");
			noidaPowerNOIDA.setDescription("Noida Power NOIDA Electricity ");
			noidaPowerNOIDA.setMinAmount(1);
			noidaPowerNOIDA.setMaxAmount(100000);
			noidaPowerNOIDA.setCode("VNUE");
			noidaPowerNOIDA.setOperatorCode("NUE");
			noidaPowerNOIDA.setServiceType(serviceBillPayment);
			noidaPowerNOIDA.setOperator(vpayqwik);
			noidaPowerNOIDA.setStatus(Status.Active);
			pqServiceRepository.save(noidaPowerNOIDA);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(noidaPowerNOIDA);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService msedcMaharashtra = pqServiceRepository.findServiceByOperatorCode("MDE");
		if (msedcMaharashtra == null) {
			msedcMaharashtra = new PQService();
			msedcMaharashtra.setName("MSEDC MAHARASHTRA");
			msedcMaharashtra.setDescription("MSEDC MAHARASHTRA Electricity ");
			msedcMaharashtra.setMinAmount(1);
			msedcMaharashtra.setMaxAmount(100000);
			msedcMaharashtra.setCode("VMDE");
			msedcMaharashtra.setOperatorCode("MDE");
			msedcMaharashtra.setServiceType(serviceBillPayment);
			msedcMaharashtra.setOperator(vpayqwik);
			msedcMaharashtra.setStatus(Status.Active);
			pqServiceRepository.save(msedcMaharashtra);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(msedcMaharashtra);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService madhyaKshetraVitaranMADHYAPRADESH = pqServiceRepository.findServiceByOperatorCode("MEE");
		if (madhyaKshetraVitaranMADHYAPRADESH == null) {
			madhyaKshetraVitaranMADHYAPRADESH = new PQService();
			madhyaKshetraVitaranMADHYAPRADESH.setName("Madhya Kshetra Vitaran MADHYAPRADESH");
			madhyaKshetraVitaranMADHYAPRADESH.setDescription("Madhya Kshetra Vitaran MADHYAPRADESH Electricity ");
			madhyaKshetraVitaranMADHYAPRADESH.setMinAmount(1);
			madhyaKshetraVitaranMADHYAPRADESH.setMaxAmount(100000);
			madhyaKshetraVitaranMADHYAPRADESH.setCode("VMEE");
			madhyaKshetraVitaranMADHYAPRADESH.setOperatorCode("MEE");
			madhyaKshetraVitaranMADHYAPRADESH.setServiceType(serviceBillPayment);
			madhyaKshetraVitaranMADHYAPRADESH.setOperator(vpayqwik);
			madhyaKshetraVitaranMADHYAPRADESH.setStatus(Status.Active);
			pqServiceRepository.save(madhyaKshetraVitaranMADHYAPRADESH);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(madhyaKshetraVitaranMADHYAPRADESH);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService jodhpurVidyutVitranNigamRAJASTHAN = pqServiceRepository.findServiceByOperatorCode("DRE");
		if (jodhpurVidyutVitranNigamRAJASTHAN == null) {
			jodhpurVidyutVitranNigamRAJASTHAN = new PQService();
			jodhpurVidyutVitranNigamRAJASTHAN.setName("Jodhpur VidyutVitran Nigam RAJASTHAN");
			jodhpurVidyutVitranNigamRAJASTHAN.setDescription("Jodhpur VidyutVitran Nigam RAJASTHAN Electricity ");
			jodhpurVidyutVitranNigamRAJASTHAN.setMinAmount(1);
			jodhpurVidyutVitranNigamRAJASTHAN.setMaxAmount(100000);
			jodhpurVidyutVitranNigamRAJASTHAN.setCode("VDRE");
			jodhpurVidyutVitranNigamRAJASTHAN.setOperatorCode("DRE");
			jodhpurVidyutVitranNigamRAJASTHAN.setServiceType(serviceBillPayment);
			jodhpurVidyutVitranNigamRAJASTHAN.setOperator(vpayqwik);
			jodhpurVidyutVitranNigamRAJASTHAN.setStatus(Status.Active);
			pqServiceRepository.save(jodhpurVidyutVitranNigamRAJASTHAN);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(jodhpurVidyutVitranNigamRAJASTHAN);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService jamshedpurUtilitiesAndServices = pqServiceRepository.findServiceByOperatorCode("JUE");
		if (jamshedpurUtilitiesAndServices == null) {
			jamshedpurUtilitiesAndServices = new PQService();
			jamshedpurUtilitiesAndServices.setName("Jamshedpur Utilities & Services");
			jamshedpurUtilitiesAndServices.setDescription("Jamshedpur Utilities & Services Electricity ");
			jamshedpurUtilitiesAndServices.setMinAmount(1);
			jamshedpurUtilitiesAndServices.setMaxAmount(100000);
			jamshedpurUtilitiesAndServices.setCode("VJUE");
			jamshedpurUtilitiesAndServices.setOperatorCode("JUE");
			jamshedpurUtilitiesAndServices.setServiceType(serviceBillPayment);
			jamshedpurUtilitiesAndServices.setOperator(vpayqwik);
			jamshedpurUtilitiesAndServices.setStatus(Status.Active);
			pqServiceRepository.save(jamshedpurUtilitiesAndServices);
		}
		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(jamshedpurUtilitiesAndServices);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService jaipurVidyutVitranNigamRAJASTHAN = pqServiceRepository.findServiceByOperatorCode("JRE");
		if (jaipurVidyutVitranNigamRAJASTHAN == null) {
			jaipurVidyutVitranNigamRAJASTHAN = new PQService();
			jaipurVidyutVitranNigamRAJASTHAN.setName("Jaipur Vidyut Vitran Nigam RAJASTHAN");
			jaipurVidyutVitranNigamRAJASTHAN.setDescription("Jaipur Vidyut Vitran Nigam RAJASTHAN Electricity ");
			jaipurVidyutVitranNigamRAJASTHAN.setMinAmount(1);
			jaipurVidyutVitranNigamRAJASTHAN.setMaxAmount(100000);
			jaipurVidyutVitranNigamRAJASTHAN.setCode("VJRE");
			jaipurVidyutVitranNigamRAJASTHAN.setOperatorCode("JRE");
			jaipurVidyutVitranNigamRAJASTHAN.setServiceType(serviceBillPayment);
			jaipurVidyutVitranNigamRAJASTHAN.setOperator(vpayqwik);
			jaipurVidyutVitranNigamRAJASTHAN.setStatus(Status.Active);
			pqServiceRepository.save(jaipurVidyutVitranNigamRAJASTHAN);
		}
		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(jaipurVidyutVitranNigamRAJASTHAN);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService indiaPower = pqServiceRepository.findServiceByOperatorCode("IPE");
		if (indiaPower == null) {
			indiaPower = new PQService();
			indiaPower.setName("IndiaPower");
			indiaPower.setDescription("India Power Electricity ");
			indiaPower.setMinAmount(1);
			indiaPower.setMaxAmount(100000);
			indiaPower.setCode("VIPE");
			indiaPower.setOperatorCode("IPE");
			indiaPower.setServiceType(serviceBillPayment);
			indiaPower.setOperator(vpayqwik);
			indiaPower.setStatus(Status.Active);
			pqServiceRepository.save(indiaPower);
		}
		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(indiaPower);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService dnhpdclDADRAAndNAGARHAVELI = pqServiceRepository.findServiceByOperatorCode("DNE");
		if (dnhpdclDADRAAndNAGARHAVELI == null) {
			dnhpdclDADRAAndNAGARHAVELI = new PQService();
			dnhpdclDADRAAndNAGARHAVELI.setName("DNHPDCL DADRA & NAGAR HAVELI");
			dnhpdclDADRAAndNAGARHAVELI.setDescription("DNHPDCL DADRA & NAGAR HAVELI Electricity ");
			dnhpdclDADRAAndNAGARHAVELI.setMinAmount(1);
			dnhpdclDADRAAndNAGARHAVELI.setMaxAmount(100000);
			dnhpdclDADRAAndNAGARHAVELI.setCode("VDNE");
			dnhpdclDADRAAndNAGARHAVELI.setOperatorCode("DNE");
			dnhpdclDADRAAndNAGARHAVELI.setServiceType(serviceBillPayment);
			dnhpdclDADRAAndNAGARHAVELI.setOperator(vpayqwik);
			dnhpdclDADRAAndNAGARHAVELI.setStatus(Status.Active);
			pqServiceRepository.save(dnhpdclDADRAAndNAGARHAVELI);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(dnhpdclDADRAAndNAGARHAVELI);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService dhbvnHaryana = pqServiceRepository.findServiceByOperatorCode("DHE");
		if (dhbvnHaryana == null) {
			dhbvnHaryana = new PQService();
			dhbvnHaryana.setName("DHBVN HARYANA");
			dhbvnHaryana.setDescription("DHBVN HARYANA Electricity ");
			dhbvnHaryana.setMinAmount(1);
			dhbvnHaryana.setMaxAmount(100000);
			dhbvnHaryana.setCode("VDHE");
			dhbvnHaryana.setOperatorCode("DHE");
			dhbvnHaryana.setServiceType(serviceBillPayment);
			dhbvnHaryana.setOperator(vpayqwik);
			dhbvnHaryana.setStatus(Status.Active);
			pqServiceRepository.save(dhbvnHaryana);
		}
		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(dhbvnHaryana);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService csebChhattisgarh = pqServiceRepository.findServiceByOperatorCode("CCE");
		if (csebChhattisgarh == null) {
			csebChhattisgarh = new PQService();
			csebChhattisgarh.setName("CSEB CHHATTISGARH");
			csebChhattisgarh.setDescription("CSEB CHHATTISGARH Electricity ");
			csebChhattisgarh.setMinAmount(1);
			csebChhattisgarh.setMaxAmount(100000);
			csebChhattisgarh.setCode("VCCE");
			csebChhattisgarh.setOperatorCode("CCE");
			csebChhattisgarh.setServiceType(serviceBillPayment);
			csebChhattisgarh.setOperator(vpayqwik);
			csebChhattisgarh.setStatus(Status.Active);
			pqServiceRepository.save(csebChhattisgarh);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(csebChhattisgarh);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService cescWestBengal = pqServiceRepository.findServiceByOperatorCode("CWE");
		if (cescWestBengal == null) {
			cescWestBengal = new PQService();
			cescWestBengal.setName("CESC WESTBENGAL");
			cescWestBengal.setDescription("CESC WEST BENGAL Electricity ");
			cescWestBengal.setMinAmount(1);
			cescWestBengal.setMaxAmount(100000);
			cescWestBengal.setCode("VCWE");
			cescWestBengal.setOperatorCode("CWE");
			cescWestBengal.setServiceType(serviceBillPayment);
			cescWestBengal.setOperator(vpayqwik);
			cescWestBengal.setStatus(Status.Active);
			pqServiceRepository.save(cescWestBengal);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(cescWestBengal);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService bsesYamunaDELHI = pqServiceRepository.findServiceByOperatorCode("BYE");
		if (bsesYamunaDELHI == null) {
			bsesYamunaDELHI = new PQService();
			bsesYamunaDELHI.setName("BSES Yamuna DELHI");
			bsesYamunaDELHI.setDescription("BSES Yamuna DELHI Electricity ");
			bsesYamunaDELHI.setMinAmount(1);
			bsesYamunaDELHI.setMaxAmount(100000);
			bsesYamunaDELHI.setCode("VBYE");
			bsesYamunaDELHI.setOperatorCode("BYE");
			bsesYamunaDELHI.setServiceType(serviceBillPayment);
			bsesYamunaDELHI.setOperator(vpayqwik);
			bsesYamunaDELHI.setStatus(Status.Active);
			pqServiceRepository.save(bsesYamunaDELHI);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0.0);
		commission.setFixed(true);
		commission.setService(bsesYamunaDELHI);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService bsesRajdhaniDELHI = pqServiceRepository.findServiceByOperatorCode("BRE");
		if (bsesRajdhaniDELHI == null) {
			bsesRajdhaniDELHI = new PQService();
			bsesRajdhaniDELHI.setName("BSES Rajdhani DELHI");
			bsesRajdhaniDELHI.setDescription("BSES Rajdhani DELHI Electricity ");
			bsesRajdhaniDELHI.setMinAmount(1);
			bsesRajdhaniDELHI.setMaxAmount(100000);
			bsesRajdhaniDELHI.setCode("VBRE");
			bsesRajdhaniDELHI.setOperatorCode("BRE");
			bsesRajdhaniDELHI.setServiceType(serviceBillPayment);
			bsesRajdhaniDELHI.setOperator(vpayqwik);
			bsesRajdhaniDELHI.setStatus(Status.Active);
			pqServiceRepository.save(bsesRajdhaniDELHI);
		}
		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0.0);
		commission.setFixed(true);
		commission.setService(bsesRajdhaniDELHI);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService bestUndertakingMUMBAI = pqServiceRepository.findServiceByOperatorCode("BME");
		if (bestUndertakingMUMBAI == null) {
			bestUndertakingMUMBAI = new PQService();
			bestUndertakingMUMBAI.setName("BEST Undertaking MUMBAI");
			bestUndertakingMUMBAI.setDescription("BEST Undertaking MUMBAI Electricity ");
			bestUndertakingMUMBAI.setMinAmount(1);
			bestUndertakingMUMBAI.setMaxAmount(100000);
			bestUndertakingMUMBAI.setCode("VBME");
			bestUndertakingMUMBAI.setOperatorCode("BME");
			bestUndertakingMUMBAI.setServiceType(serviceBillPayment);
			bestUndertakingMUMBAI.setOperator(vpayqwik);
			bestUndertakingMUMBAI.setStatus(Status.Active);
			pqServiceRepository.save(bestUndertakingMUMBAI);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(bestUndertakingMUMBAI);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService bescomBengaluru = pqServiceRepository.findServiceByOperatorCode("BBE");
		if (bescomBengaluru == null) {
			bescomBengaluru = new PQService();
			bescomBengaluru.setName("BESCOM BENGALURU");
			bescomBengaluru.setDescription("BESCOM BENGALURU Electricity ");
			bescomBengaluru.setMinAmount(1);
			bescomBengaluru.setMaxAmount(100000);
			bescomBengaluru.setCode("VBBE");
			bescomBengaluru.setOperatorCode("BBE");
			bescomBengaluru.setServiceType(serviceBillPayment);
			bescomBengaluru.setOperator(vpayqwik);
			bescomBengaluru.setStatus(Status.Active);
			pqServiceRepository.save(bescomBengaluru);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(bescomBengaluru);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService apdclAssam = pqServiceRepository.findServiceByOperatorCode("AAE");
		if (apdclAssam == null) {
			apdclAssam = new PQService();
			apdclAssam.setName("APDCL ASSAM");
			apdclAssam.setDescription("APDCL ASSAM Electricity ");
			apdclAssam.setMinAmount(1);
			apdclAssam.setMaxAmount(100000);
			apdclAssam.setCode("VAAE");
			apdclAssam.setOperatorCode("AAE");
			apdclAssam.setServiceType(serviceBillPayment);
			apdclAssam.setOperator(vpayqwik);
			apdclAssam.setStatus(Status.Active);
			pqServiceRepository.save(apdclAssam);
		}
		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(apdclAssam);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService ajmerVidyutVitranNigamRAJASTHAN = pqServiceRepository.findServiceByOperatorCode("ARE");
		if (ajmerVidyutVitranNigamRAJASTHAN == null) {
			ajmerVidyutVitranNigamRAJASTHAN = new PQService();
			ajmerVidyutVitranNigamRAJASTHAN.setName("Ajmer Vidyut Vitran Nigam RAJASTHAN");
			ajmerVidyutVitranNigamRAJASTHAN.setDescription("BESCOM BENGALURU Electricity ");
			ajmerVidyutVitranNigamRAJASTHAN.setMinAmount(1);
			ajmerVidyutVitranNigamRAJASTHAN.setMaxAmount(100000);
			ajmerVidyutVitranNigamRAJASTHAN.setCode("VARE");
			ajmerVidyutVitranNigamRAJASTHAN.setOperatorCode("ARE");
			ajmerVidyutVitranNigamRAJASTHAN.setServiceType(serviceBillPayment);
			ajmerVidyutVitranNigamRAJASTHAN.setOperator(vpayqwik);
			ajmerVidyutVitranNigamRAJASTHAN.setStatus(Status.Active);
			pqServiceRepository.save(ajmerVidyutVitranNigamRAJASTHAN);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(ajmerVidyutVitranNigamRAJASTHAN);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService mahanagarGas = pqServiceRepository.findServiceByOperatorCode("MMG");
		if (mahanagarGas == null) {
			mahanagarGas = new PQService();
			mahanagarGas.setName("Mahanagar Gas");
			mahanagarGas.setDescription("Mahanagar Gas");
			mahanagarGas.setMinAmount(1);
			mahanagarGas.setMaxAmount(100000);
			mahanagarGas.setCode("VMMG");
			mahanagarGas.setOperatorCode("MMG");
			mahanagarGas.setServiceType(serviceBillPayment);
			mahanagarGas.setOperator(vpayqwik);
			mahanagarGas.setStatus(Status.Active);
			pqServiceRepository.save(mahanagarGas);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0);
		commission.setFixed(true);
		commission.setService(mahanagarGas);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService indraprasthaGas = pqServiceRepository.findServiceByOperatorCode("IPG");
		if (indraprasthaGas == null) {
			indraprasthaGas = new PQService();
			indraprasthaGas.setName("Indraprastha Gas");
			indraprasthaGas.setDescription("Indraprastha Gas ");
			indraprasthaGas.setMinAmount(1);
			indraprasthaGas.setMaxAmount(100000);
			indraprasthaGas.setCode("VIPG");
			indraprasthaGas.setOperatorCode("IPG");
			indraprasthaGas.setServiceType(serviceBillPayment);
			indraprasthaGas.setOperator(vpayqwik);
			indraprasthaGas.setStatus(Status.Active);
			pqServiceRepository.save(indraprasthaGas);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0.0);
		commission.setFixed(true);
		commission.setService(indraprasthaGas);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService gujaratGas = pqServiceRepository.findServiceByOperatorCode("GJG");
		if (gujaratGas == null) {
			gujaratGas = new PQService();
			gujaratGas.setName("Gujarat Gas");
			gujaratGas.setDescription("Gujarat Gas");
			gujaratGas.setMinAmount(1);
			gujaratGas.setMaxAmount(100000);
			gujaratGas.setCode("VGJG");
			gujaratGas.setOperatorCode("GJG");
			gujaratGas.setServiceType(serviceBillPayment);
			gujaratGas.setOperator(vpayqwik);
			gujaratGas.setStatus(Status.Active);
			pqServiceRepository.save(gujaratGas);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(2.0);
		commission.setFixed(true);
		commission.setService(gujaratGas);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService gspcGas = pqServiceRepository.findServiceByOperatorCode("GSG");
		if (gspcGas == null) {
			gspcGas = new PQService();
			gspcGas.setName("GSPC Gas");
			gspcGas.setDescription("GSPC Gas");
			gspcGas.setMinAmount(1);
			gspcGas.setMaxAmount(100000);
			gspcGas.setCode("VGSG");
			gspcGas.setOperatorCode("GSG");
			gspcGas.setServiceType(serviceBillPayment);
			gspcGas.setOperator(vpayqwik);
			gspcGas.setStatus(Status.Active);
			pqServiceRepository.save(gspcGas);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(2.0);
		commission.setFixed(true);
		commission.setService(gspcGas);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService adaniGas = pqServiceRepository.findServiceByOperatorCode("ADG");
		if (adaniGas == null) {
			adaniGas = new PQService();
			adaniGas.setName("Adani Gas");
			adaniGas.setDescription("Adani Gas");
			adaniGas.setMinAmount(1);
			adaniGas.setMaxAmount(100000);
			adaniGas.setCode("VADG");
			adaniGas.setOperatorCode("ADG");
			adaniGas.setServiceType(serviceBillPayment);
			adaniGas.setOperator(vpayqwik);
			adaniGas.setStatus(Status.Active);
			pqServiceRepository.save(adaniGas);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(2.0);
		commission.setFixed(true);
		commission.setService(adaniGas);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tataAIALifeInsurance = pqServiceRepository.findServiceByOperatorCode("TAI");
		if (tataAIALifeInsurance == null) {
			tataAIALifeInsurance = new PQService();
			tataAIALifeInsurance.setName("Tata AIA Life Insurance");
			tataAIALifeInsurance.setDescription("Tata AIA Life Insurance");
			tataAIALifeInsurance.setMinAmount(1);
			tataAIALifeInsurance.setMaxAmount(100000);
			tataAIALifeInsurance.setCode("VTAI");
			tataAIALifeInsurance.setOperatorCode("TAI");
			tataAIALifeInsurance.setServiceType(serviceBillPayment);
			tataAIALifeInsurance.setOperator(vpayqwik);
			tataAIALifeInsurance.setStatus(Status.Active);
			pqServiceRepository.save(tataAIALifeInsurance);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0.0);
		commission.setFixed(true);
		commission.setService(tataAIALifeInsurance);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService indiaFirstLifeInsurance = pqServiceRepository.findServiceByOperatorCode("ILI");
		if (indiaFirstLifeInsurance == null) {
			indiaFirstLifeInsurance = new PQService();
			indiaFirstLifeInsurance.setName("India First Life Insurance");
			indiaFirstLifeInsurance.setDescription("India First Life Insurance");
			indiaFirstLifeInsurance.setMinAmount(1);
			indiaFirstLifeInsurance.setMaxAmount(100000);
			indiaFirstLifeInsurance.setCode("VILI");
			indiaFirstLifeInsurance.setOperatorCode("ILI");
			indiaFirstLifeInsurance.setServiceType(serviceBillPayment);
			indiaFirstLifeInsurance.setOperator(vpayqwik);
			indiaFirstLifeInsurance.setStatus(Status.Active);
			pqServiceRepository.save(indiaFirstLifeInsurance);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(2.0);
		commission.setFixed(true);
		commission.setService(indiaFirstLifeInsurance);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService iciciPrudentialLifeInsurance = pqServiceRepository.findServiceByOperatorCode("IPI");
		if (iciciPrudentialLifeInsurance == null) {
			iciciPrudentialLifeInsurance = new PQService();
			iciciPrudentialLifeInsurance.setName("ICICI Prudential Life Insurance");
			iciciPrudentialLifeInsurance.setDescription("ICICI Prudential Life Insurance");
			iciciPrudentialLifeInsurance.setMinAmount(1);
			iciciPrudentialLifeInsurance.setMaxAmount(100000);
			iciciPrudentialLifeInsurance.setCode("VIPI");
			iciciPrudentialLifeInsurance.setOperatorCode("IPI");
			iciciPrudentialLifeInsurance.setServiceType(serviceBillPayment);
			iciciPrudentialLifeInsurance.setOperator(vpayqwik);
			iciciPrudentialLifeInsurance.setStatus(Status.Active);
			pqServiceRepository.save(iciciPrudentialLifeInsurance);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(0.0);
		commission.setFixed(true);
		commission.setService(iciciPrudentialLifeInsurance);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService bhartiAXALifeInsurance = pqServiceRepository.findServiceByOperatorCode("BAI");
		if (bhartiAXALifeInsurance == null) {
			bhartiAXALifeInsurance = new PQService();
			bhartiAXALifeInsurance.setName("Bharti AXA Life Insurance");
			bhartiAXALifeInsurance.setDescription("Bharti AXA Life Insurance");
			bhartiAXALifeInsurance.setMinAmount(1);
			bhartiAXALifeInsurance.setMaxAmount(100000);
			bhartiAXALifeInsurance.setCode("VBAI");
			bhartiAXALifeInsurance.setOperatorCode("BAI");
			bhartiAXALifeInsurance.setServiceType(serviceBillPayment);
			bhartiAXALifeInsurance.setOperator(vpayqwik);
			bhartiAXALifeInsurance.setStatus(Status.Active);
			pqServiceRepository.save(bhartiAXALifeInsurance);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(100000);
		commission.setType("POST");
		commission.setValue(2.0);
		commission.setFixed(true);
		commission.setService(bhartiAXALifeInsurance);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tikonaISPPostPaid = pqServiceRepository.findServiceByOperatorCode("TDB");
		if (tikonaISPPostPaid == null) {
			tikonaISPPostPaid = new PQService();
			tikonaISPPostPaid.setName("Tikona");
			tikonaISPPostPaid.setDescription("Tikona ISPPostPaid");
			tikonaISPPostPaid.setMinAmount(1);
			tikonaISPPostPaid.setMaxAmount(99999);
			tikonaISPPostPaid.setCode("VTDB");
			tikonaISPPostPaid.setOperatorCode("TDB");
			tikonaISPPostPaid.setServiceType(serviceBillPayment);
			tikonaISPPostPaid.setOperator(vpayqwik);
			tikonaISPPostPaid.setStatus(Status.Active);
			pqServiceRepository.save(tikonaISPPostPaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(99999);
		commission.setType("PRE");
		commission.setValue(0.90);
		commission.setFixed(false);
		commission.setService(tikonaISPPostPaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tataDocomoPostPaid = pqServiceRepository.findServiceByOperatorCode("TCL");
		if (tataDocomoPostPaid == null) {
			tataDocomoPostPaid = new PQService();
			tataDocomoPostPaid.setName("TataDocomo");
			tataDocomoPostPaid.setDescription("TataDocomo PostPaid");
			tataDocomoPostPaid.setMinAmount(1);
			tataDocomoPostPaid.setMaxAmount(10000);
			tataDocomoPostPaid.setCode("VTCL");
			tataDocomoPostPaid.setOperatorCode("TCL");
			tataDocomoPostPaid.setServiceType(serviceBillPayment);
			tataDocomoPostPaid.setOperator(vpayqwik);
			tataDocomoPostPaid.setStatus(Status.Active);
			pqServiceRepository.save(tataDocomoPostPaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.50);
		commission.setFixed(false);
		commission.setService(tataDocomoPostPaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService relianceLandline = pqServiceRepository.findServiceByOperatorCode("RGL");
		if (relianceLandline == null) {
			relianceLandline = new PQService();
			relianceLandline.setName("Reliance");
			relianceLandline.setDescription("Reliance LandLine");
			relianceLandline.setMinAmount(50);
			relianceLandline.setMaxAmount(10000);
			relianceLandline.setCode("VRGL");
			relianceLandline.setOperatorCode("RGL");
			relianceLandline.setServiceType(serviceBillPayment);
			relianceLandline.setOperator(vpayqwik);
			relianceLandline.setStatus(Status.Active);
			pqServiceRepository.save(relianceLandline);
		}

		commission = new PQCommission();
		commission.setMinAmount(50);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.50);
		commission.setFixed(false);
		commission.setService(relianceLandline);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService mtnlDelhi = pqServiceRepository.findServiceByOperatorCode("MDL");
		if (mtnlDelhi == null) {
			mtnlDelhi = new PQService();
			mtnlDelhi.setName("MTNLDelhi");
			mtnlDelhi.setDescription("MTNLDelhi LandLine");
			mtnlDelhi.setMinAmount(10);
			mtnlDelhi.setMaxAmount(10000);
			mtnlDelhi.setCode("VMDL");
			mtnlDelhi.setOperatorCode("MDL");
			mtnlDelhi.setServiceType(serviceBillPayment);
			mtnlDelhi.setOperator(vpayqwik);
			mtnlDelhi.setStatus(Status.Active);
			pqServiceRepository.save(mtnlDelhi);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.0);
		commission.setFixed(true);
		commission.setService(mtnlDelhi);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService bsnlLandline = pqServiceRepository.findServiceByOperatorCode("BGL");
		if (bsnlLandline == null) {
			bsnlLandline = new PQService();
			bsnlLandline.setName("BSNL");
			bsnlLandline.setDescription("BSNL LandLine");
			bsnlLandline.setMinAmount(1);
			bsnlLandline.setMaxAmount(50000);
			bsnlLandline.setCode("VBGL");
			bsnlLandline.setOperatorCode("BGL");
			bsnlLandline.setServiceType(serviceBillPayment);
			bsnlLandline.setOperator(vpayqwik);
			bsnlLandline.setStatus(Status.Active);
			pqServiceRepository.save(bsnlLandline);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(50000);
		commission.setType("PRE");
		commission.setValue(0.0);
		commission.setFixed(false);
		commission.setService(bsnlLandline);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService airtelLandline = pqServiceRepository.findServiceByOperatorCode("ATL");
		if (airtelLandline == null) {
			airtelLandline = new PQService();
			airtelLandline.setName("Airtel");
			airtelLandline.setDescription("Airtel LandLine");
			airtelLandline.setMinAmount(1);
			airtelLandline.setMaxAmount(50000);
			airtelLandline.setCode("VATL");
			airtelLandline.setOperatorCode("ATL");
			airtelLandline.setServiceType(serviceBillPayment);
			airtelLandline.setOperator(vpayqwik);
			airtelLandline.setStatus(Status.Active);
			pqServiceRepository.save(airtelLandline);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.45);
		commission.setFixed(false);
		commission.setService(airtelLandline);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService vodafone = pqServiceRepository.findServiceByOperatorCode("VFC");
		if (vodafone == null) {
			vodafone = new PQService();
			vodafone.setName("Vodafone");
			vodafone.setDescription("Vodafone PostPaid");
			vodafone.setMinAmount(10);
			vodafone.setMaxAmount(10000);
			vodafone.setCode("VVFC");
			vodafone.setOperatorCode("VFC");
			vodafone.setServiceType(serviceBillPayment);
			vodafone.setOperator(vpayqwik);
			vodafone.setStatus(Status.Active);
			pqServiceRepository.save(vodafone);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.65);
		commission.setFixed(true);
		commission.setService(vodafone);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tataDocomoPost = pqServiceRepository.findServiceByOperatorCode("TDC");
		if (tataDocomoPost == null) {
			tataDocomoPost = new PQService();
			tataDocomoPost.setName("TataDocomo");
			tataDocomoPost.setDescription("TataDocomo PostPaid");
			tataDocomoPost.setMinAmount(10);
			tataDocomoPost.setMaxAmount(10000);
			tataDocomoPost.setCode("VTDC");
			tataDocomoPost.setOperatorCode("TDC");
			tataDocomoPost.setServiceType(serviceBillPayment);
			tataDocomoPost.setOperator(vpayqwik);
			tataDocomoPost.setStatus(Status.Active);
			pqServiceRepository.save(tataDocomoPost);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.5);
		commission.setFixed(false);
		commission.setService(tataDocomoPost);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService reliancePostPaid = pqServiceRepository.findServiceByOperatorCode("RGC");
		if (reliancePostPaid == null) {
			reliancePostPaid = new PQService();
			reliancePostPaid.setName("ReliancePostPaid");
			reliancePostPaid.setDescription("Reliance PostPaid");
			reliancePostPaid.setMinAmount(50);
			reliancePostPaid.setMaxAmount(10000);
			reliancePostPaid.setCode("VRGC");
			reliancePostPaid.setOperatorCode("RGC");
			reliancePostPaid.setServiceType(serviceBillPayment);
			reliancePostPaid.setOperator(vpayqwik);
			reliancePostPaid.setStatus(Status.Active);
			pqServiceRepository.save(reliancePostPaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(50);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.5);
		commission.setFixed(false);
		commission.setService(reliancePostPaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService mtsPostPaid = pqServiceRepository.findServiceByOperatorCode("MTC");
		if (mtsPostPaid == null) {
			mtsPostPaid = new PQService();
			mtsPostPaid.setName("MTS");
			mtsPostPaid.setDescription("MTS PostPaid");
			mtsPostPaid.setMinAmount(10);
			mtsPostPaid.setMaxAmount(10000);
			mtsPostPaid.setCode("VMTC");
			mtsPostPaid.setOperatorCode("MTC");
			mtsPostPaid.setServiceType(serviceBillPayment);
			mtsPostPaid.setOperator(vpayqwik);
			mtsPostPaid.setStatus(Status.Active);
			pqServiceRepository.save(mtsPostPaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.5);
		commission.setFixed(false);
		commission.setService(mtsPostPaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService ideaPostPaid = pqServiceRepository.findServiceByOperatorCode("IDC");
		if (ideaPostPaid == null) {
			ideaPostPaid = new PQService();
			ideaPostPaid.setName("IDEA");
			ideaPostPaid.setDescription("IDEA PostPaid");
			ideaPostPaid.setMinAmount(10);
			ideaPostPaid.setMaxAmount(10800);
			ideaPostPaid.setCode("VIDC");
			ideaPostPaid.setOperatorCode("IDC");
			ideaPostPaid.setServiceType(serviceBillPayment);
			ideaPostPaid.setOperator(vpayqwik);
			ideaPostPaid.setStatus(Status.Active);
			pqServiceRepository.save(ideaPostPaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10800);
		commission.setType("PRE");
		commission.setValue(0.3);
		commission.setFixed(false);
		commission.setService(ideaPostPaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService bsnlPostPaid = pqServiceRepository.findServiceByOperatorCode("BGC");
		if (bsnlPostPaid == null) {
			bsnlPostPaid = new PQService();
			bsnlPostPaid.setName("BSNL");
			bsnlPostPaid.setDescription("BSNL PostPaid");
			bsnlPostPaid.setMinAmount(1);
			bsnlPostPaid.setMaxAmount(10000);
			bsnlPostPaid.setCode("VBGC");
			bsnlPostPaid.setOperatorCode("BGC");
			bsnlPostPaid.setServiceType(serviceBillPayment);
			bsnlPostPaid.setOperator(vpayqwik);
			bsnlPostPaid.setStatus(Status.Active);
			pqServiceRepository.save(bsnlPostPaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(1.00);
		commission.setFixed(false);
		commission.setService(bsnlPostPaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService airtelPostPaid = pqServiceRepository.findServiceByOperatorCode("ATC");
		if (airtelPostPaid == null) {
			airtelPostPaid = new PQService();
			airtelPostPaid.setName("AIRTEL");
			airtelPostPaid.setDescription("Airtel PostPaid");
			airtelPostPaid.setMinAmount(10);
			airtelPostPaid.setMaxAmount(10000);
			airtelPostPaid.setCode("VATC");
			airtelPostPaid.setOperatorCode("ATC");
			airtelPostPaid.setServiceType(serviceBillPayment);
			airtelPostPaid.setOperator(vpayqwik);
			airtelPostPaid.setStatus(Status.Active);
			pqServiceRepository.save(airtelPostPaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.45);
		commission.setFixed(false);
		commission.setService(airtelPostPaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService aircelPostPaid = pqServiceRepository.findServiceByOperatorCode("ACC");
		if (aircelPostPaid == null) {
			aircelPostPaid = new PQService();
			aircelPostPaid.setName("AIRCEL");
			aircelPostPaid.setDescription("Aircel PostPaid");
			aircelPostPaid.setMinAmount(10);
			aircelPostPaid.setMaxAmount(10000);
			aircelPostPaid.setCode("VACC");
			aircelPostPaid.setOperatorCode("ACC");
			aircelPostPaid.setServiceType(serviceBillPayment);
			aircelPostPaid.setOperator(vpayqwik);
			aircelPostPaid.setStatus(Status.Active);
			pqServiceRepository.save(aircelPostPaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.80);
		commission.setFixed(false);
		commission.setService(aircelPostPaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService vodafonePrePaid = pqServiceRepository.findServiceByOperatorCode("VFP");
		if (vodafonePrePaid == null) {
			vodafonePrePaid = new PQService();
			vodafonePrePaid.setName("Vodafone");
			vodafonePrePaid.setDescription("Vodafone PrePaid");
			vodafonePrePaid.setMinAmount(1);
			vodafonePrePaid.setMaxAmount(10000);
			vodafonePrePaid.setCode("VVFP");
			vodafonePrePaid.setOperatorCode("VFP");
			vodafonePrePaid.setServiceType(serviceBillPayment);
			vodafonePrePaid.setOperator(vpayqwik);
			vodafonePrePaid.setStatus(Status.Active);
			pqServiceRepository.save(vodafonePrePaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(1.20);
		commission.setFixed(false);
		commission.setService(vodafonePrePaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService videoconTalktime = pqServiceRepository.findServiceByOperatorCode("VGP");
		if (videoconTalktime == null) {
			videoconTalktime = new PQService();
			videoconTalktime.setName("VideoconTalktime");
			videoconTalktime.setDescription("VideoconTalktime PrePaid");
			videoconTalktime.setMinAmount(1);
			videoconTalktime.setMaxAmount(10000);
			videoconTalktime.setCode("VVGP");
			videoconTalktime.setOperatorCode("VGP");
			videoconTalktime.setServiceType(serviceBillPayment);
			videoconTalktime.setOperator(vpayqwik);
			videoconTalktime.setStatus(Status.Active);
			pqServiceRepository.save(videoconTalktime);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(4.0);
		commission.setFixed(false);
		commission.setService(videoconTalktime);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService videoconSpecialTariff = pqServiceRepository.findServiceByOperatorCode("VSP");
		if (videoconSpecialTariff == null) {
			videoconSpecialTariff = new PQService();
			videoconSpecialTariff.setName("VideoconSpecialTariff");
			videoconSpecialTariff.setDescription("VideoconSpecialTariff PrePaid");
			videoconSpecialTariff.setMinAmount(1);
			videoconSpecialTariff.setMaxAmount(10000);
			videoconSpecialTariff.setCode("VVSP");
			videoconSpecialTariff.setOperatorCode("VSP");
			videoconSpecialTariff.setServiceType(serviceBillPayment);
			videoconSpecialTariff.setOperator(vpayqwik);
			videoconSpecialTariff.setStatus(Status.Active);
			pqServiceRepository.save(videoconSpecialTariff);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(4.0);
		commission.setFixed(false);
		commission.setService(videoconSpecialTariff);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService telenorTalktime = pqServiceRepository.findServiceByOperatorCode("UGP");
		if (telenorTalktime == null) {
			telenorTalktime = new PQService();
			telenorTalktime.setName("TelenorTalktime");
			telenorTalktime.setDescription("TelenorTalktimePrePaid");
			telenorTalktime.setMinAmount(1);
			telenorTalktime.setMaxAmount(10000);
			telenorTalktime.setCode("VUGP");
			telenorTalktime.setOperatorCode("UGP");
			telenorTalktime.setServiceType(serviceBillPayment);
			telenorTalktime.setOperator(vpayqwik);
			telenorTalktime.setStatus(Status.Active);
			pqServiceRepository.save(telenorTalktime);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(3.10);
		commission.setFixed(false);
		commission.setService(telenorTalktime);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService telenorSpecialTariff = pqServiceRepository.findServiceByOperatorCode("USP");
		if (telenorSpecialTariff == null) {
			telenorSpecialTariff = new PQService();
			telenorSpecialTariff.setName("Telenor Special Tariff");
			telenorSpecialTariff.setDescription("Telenor Special Tariff PrePaid");
			telenorSpecialTariff.setMinAmount(1);
			telenorSpecialTariff.setMaxAmount(10000);
			telenorSpecialTariff.setCode("VUSP");
			telenorSpecialTariff.setOperatorCode("USP");
			telenorSpecialTariff.setServiceType(serviceBillPayment);
			telenorSpecialTariff.setOperator(vpayqwik);
			telenorSpecialTariff.setStatus(Status.Active);
			pqServiceRepository.save(telenorSpecialTariff);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(3.10);
		commission.setFixed(false);
		commission.setService(telenorSpecialTariff);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tataDocomoGSMTalktime = pqServiceRepository.findServiceByOperatorCode("TGP");
		if (tataDocomoGSMTalktime == null) {
			tataDocomoGSMTalktime = new PQService();
			tataDocomoGSMTalktime.setName("TataDocomo GSM Talktime");
			tataDocomoGSMTalktime.setDescription("TataDocomo GSM Talktime PrePaid");
			tataDocomoGSMTalktime.setMinAmount(1);
			tataDocomoGSMTalktime.setMaxAmount(10000);
			tataDocomoGSMTalktime.setCode("VTGP");
			tataDocomoGSMTalktime.setOperatorCode("TGP");
			tataDocomoGSMTalktime.setServiceType(serviceBillPayment);
			tataDocomoGSMTalktime.setOperator(vpayqwik);
			tataDocomoGSMTalktime.setStatus(Status.Active);
			pqServiceRepository.save(tataDocomoGSMTalktime);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(1.60);
		commission.setFixed(false);
		commission.setService(tataDocomoGSMTalktime);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tataDocomoGSMSpecialTariff = pqServiceRepository.findServiceByOperatorCode("TSP");
		if (tataDocomoGSMSpecialTariff == null) {
			tataDocomoGSMSpecialTariff = new PQService();
			tataDocomoGSMSpecialTariff.setName("TataDocomo GSM Special Talktime");
			tataDocomoGSMSpecialTariff.setDescription("TataDocomo GSM Special Talktime PrePaid");
			tataDocomoGSMSpecialTariff.setMinAmount(1);
			tataDocomoGSMSpecialTariff.setMaxAmount(10000);
			tataDocomoGSMSpecialTariff.setCode("VTSP");
			tataDocomoGSMSpecialTariff.setOperatorCode("TSP");
			tataDocomoGSMSpecialTariff.setServiceType(serviceBillPayment);
			tataDocomoGSMSpecialTariff.setOperator(vpayqwik);
			tataDocomoGSMSpecialTariff.setStatus(Status.Active);
			pqServiceRepository.save(tataDocomoGSMSpecialTariff);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(1.60);
		commission.setFixed(false);
		commission.setService(tataDocomoGSMSpecialTariff);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService tataDocomoCDMA = pqServiceRepository.findServiceByOperatorCode("TCP");
		if (tataDocomoCDMA == null) {
			tataDocomoCDMA = new PQService();
			tataDocomoCDMA.setName("TataDocomoCDMA");
			tataDocomoCDMA.setDescription("TataDocomo CDMA PrePaid");
			tataDocomoCDMA.setMinAmount(1);
			tataDocomoCDMA.setMaxAmount(10000);
			tataDocomoCDMA.setCode("VTCP");
			tataDocomoCDMA.setOperatorCode("TCP");
			tataDocomoCDMA.setServiceType(serviceBillPayment);
			tataDocomoCDMA.setOperator(vpayqwik);
			tataDocomoCDMA.setStatus(Status.Active);
			pqServiceRepository.save(tataDocomoCDMA);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(1.60);
		commission.setFixed(false);
		commission.setService(tataDocomoCDMA);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService t24MobileTalktime = pqServiceRepository.findServiceByOperatorCode("TMP");
		if (t24MobileTalktime == null) {
			t24MobileTalktime = new PQService();
			t24MobileTalktime.setName("T24MobileTalktime");
			t24MobileTalktime.setDescription("T24 Mobile Talktime PrePaid");
			t24MobileTalktime.setMinAmount(1);
			t24MobileTalktime.setMaxAmount(10000);
			t24MobileTalktime.setCode("VTMP");
			t24MobileTalktime.setOperatorCode("TMP");
			t24MobileTalktime.setServiceType(serviceBillPayment);
			t24MobileTalktime.setOperator(vpayqwik);
			t24MobileTalktime.setStatus(Status.Active);
			pqServiceRepository.save(t24MobileTalktime);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(1.60);
		commission.setFixed(false);
		commission.setService(t24MobileTalktime);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService t24MobileSpecialTariff = pqServiceRepository.findServiceByOperatorCode("TVP");
		if (t24MobileSpecialTariff == null) {
			t24MobileSpecialTariff = new PQService();
			t24MobileSpecialTariff.setName("T24 Mobile Special Tariff");
			t24MobileSpecialTariff.setDescription("T24 Mobile Special Talktime PrePaid");
			t24MobileSpecialTariff.setMinAmount(1);
			t24MobileSpecialTariff.setMaxAmount(10000);
			t24MobileSpecialTariff.setCode("VTVP");
			t24MobileSpecialTariff.setOperatorCode("TVP");
			t24MobileSpecialTariff.setServiceType(serviceBillPayment);
			t24MobileSpecialTariff.setOperator(vpayqwik);
			t24MobileSpecialTariff.setStatus(Status.Active);
			pqServiceRepository.save(t24MobileSpecialTariff);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(1.60);
		commission.setFixed(false);
		commission.setService(t24MobileSpecialTariff);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService reliancePrePaid = pqServiceRepository.findServiceByOperatorCode("RGP");
		if (reliancePrePaid == null) {
			reliancePrePaid = new PQService();
			reliancePrePaid.setName("Reliance");
			reliancePrePaid.setDescription("Reliance PrePaid");
			reliancePrePaid.setMinAmount(1);
			reliancePrePaid.setMaxAmount(10000);
			reliancePrePaid.setCode("VRGP");
			reliancePrePaid.setOperatorCode("RGP");
			reliancePrePaid.setServiceType(serviceBillPayment);
			reliancePrePaid.setOperator(vpayqwik);
			reliancePrePaid.setStatus(Status.Active);
			pqServiceRepository.save(reliancePrePaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(2.60);
		commission.setFixed(false);
		commission.setService(reliancePrePaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService mtsPrePaid = pqServiceRepository.findServiceByOperatorCode("MTP");
		if (mtsPrePaid == null) {
			mtsPrePaid = new PQService();
			mtsPrePaid.setName("MTS");
			mtsPrePaid.setDescription("MTS PrePaid");
			mtsPrePaid.setMinAmount(1);
			mtsPrePaid.setMaxAmount(10000);
			mtsPrePaid.setCode("VMTP");
			mtsPrePaid.setOperatorCode("MTP");
			mtsPrePaid.setServiceType(serviceBillPayment);
			mtsPrePaid.setOperator(vpayqwik);
			mtsPrePaid.setStatus(Status.Active);
			pqServiceRepository.save(mtsPrePaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(3.60);
		commission.setFixed(false);
		commission.setService(mtsPrePaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService mtnlTalktime = pqServiceRepository.findServiceByOperatorCode("MMP");
		if (mtnlTalktime == null) {
			mtnlTalktime = new PQService();
			mtnlTalktime.setName("MTNL");
			mtnlTalktime.setDescription("MTNL Talktime PrePaid");
			mtnlTalktime.setMinAmount(1);
			mtnlTalktime.setMaxAmount(10000);
			mtnlTalktime.setCode("VMMP");
			mtnlTalktime.setOperatorCode("MMP");
			mtnlTalktime.setServiceType(serviceBillPayment);
			mtnlTalktime.setOperator(vpayqwik);
			mtnlTalktime.setStatus(Status.Active);
			pqServiceRepository.save(mtnlTalktime);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(3.70);
		commission.setFixed(false);
		commission.setService(mtnlTalktime);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService mtnlSpecialTariff = pqServiceRepository.findServiceByOperatorCode("MSP");
		if (mtnlSpecialTariff == null) {
			mtnlSpecialTariff = new PQService();
			mtnlSpecialTariff.setName("MTNLSpecialTariff");
			mtnlSpecialTariff.setDescription("MTNL Special Tariff PrePaid");
			mtnlSpecialTariff.setMinAmount(1);
			mtnlSpecialTariff.setMaxAmount(10000);
			mtnlSpecialTariff.setCode("VMSP");
			mtnlSpecialTariff.setOperatorCode("MSP");
			mtnlSpecialTariff.setServiceType(serviceBillPayment);
			mtnlSpecialTariff.setOperator(vpayqwik);
			mtnlSpecialTariff.setStatus(Status.Active);
			pqServiceRepository.save(mtnlSpecialTariff);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(3.70);
		commission.setFixed(false);
		commission.setService(mtnlSpecialTariff);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService ideaPrePaid = pqServiceRepository.findServiceByOperatorCode("IDP");
		if (ideaPrePaid == null) {
			ideaPrePaid = new PQService();
			ideaPrePaid.setName("Idea");
			ideaPrePaid.setDescription("Idea PrePaid");
			ideaPrePaid.setMinAmount(1);
			ideaPrePaid.setMaxAmount(10000);
			ideaPrePaid.setCode("VIDP");
			ideaPrePaid.setOperatorCode("IDP");
			ideaPrePaid.setServiceType(serviceBillPayment);
			ideaPrePaid.setOperator(vpayqwik);
			ideaPrePaid.setStatus(Status.Active);
			pqServiceRepository.save(ideaPrePaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(1.20);
		commission.setFixed(false);
		commission.setService(ideaPrePaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService bsnlTalktime = pqServiceRepository.findServiceByOperatorCode("BGP");
		if (bsnlTalktime == null) {
			bsnlTalktime = new PQService();
			bsnlTalktime.setName("BSNLTalktime");
			bsnlTalktime.setDescription("BSNL TalkTime PrePaid");
			bsnlTalktime.setMinAmount(1);
			bsnlTalktime.setMaxAmount(10000);
			bsnlTalktime.setCode("VBGP");
			bsnlTalktime.setOperatorCode("BGP");
			bsnlTalktime.setServiceType(serviceBillPayment);
			bsnlTalktime.setOperator(vpayqwik);
			bsnlTalktime.setStatus(Status.Active);
			pqServiceRepository.save(bsnlTalktime);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(3.50);
		commission.setFixed(false);
		commission.setService(bsnlTalktime);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService bsnlSpecialTariff = pqServiceRepository.findServiceByOperatorCode("BVP");
		if (bsnlSpecialTariff == null) {
			bsnlSpecialTariff = new PQService();
			bsnlSpecialTariff.setName("BSNLSpecialTariff");
			bsnlSpecialTariff.setDescription("BSNL Specail Tariff PrePaid");
			bsnlSpecialTariff.setMinAmount(1);
			bsnlSpecialTariff.setMaxAmount(10000);
			bsnlSpecialTariff.setCode("VBVP");
			bsnlSpecialTariff.setOperatorCode("BVP");
			bsnlSpecialTariff.setServiceType(serviceBillPayment);
			bsnlSpecialTariff.setOperator(vpayqwik);
			bsnlSpecialTariff.setStatus(Status.Active);
			pqServiceRepository.save(bsnlSpecialTariff);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(3.50);
		commission.setFixed(false);
		commission.setService(bsnlSpecialTariff);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService airtelPrepaid = pqServiceRepository.findServiceByOperatorCode("ATP");
		if (airtelPrepaid == null) {
			airtelPrepaid = new PQService();
			airtelPrepaid.setName("Airtel");
			airtelPrepaid.setDescription("Airtel PrePaid");
			airtelPrepaid.setMinAmount(1);
			airtelPrepaid.setMaxAmount(10000);
			airtelPrepaid.setCode("VATP");
			airtelPrepaid.setOperatorCode("ATP");
			airtelPrepaid.setServiceType(serviceBillPayment);
			airtelPrepaid.setOperator(vpayqwik);
			airtelPrepaid.setStatus(Status.Active);
			pqServiceRepository.save(airtelPrepaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(10);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(0.45);
		commission.setFixed(false);
		commission.setService(airtelPrepaidTopup);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

		PQService aircelPrepaid = pqServiceRepository.findServiceByOperatorCode("ACP");
		if (aircelPrepaid == null) {
			aircelPrepaid = new PQService();
			aircelPrepaid.setName("Aircel");
			aircelPrepaid.setDescription("Aircel PrePaid");
			aircelPrepaid.setMinAmount(1);
			aircelPrepaid.setMaxAmount(10000);
			aircelPrepaid.setCode("VACP");
			aircelPrepaid.setOperatorCode("ACP");
			aircelPrepaid.setServiceType(serviceBillPayment);
			aircelPrepaid.setOperator(vpayqwik);
			aircelPrepaid.setStatus(Status.Active);
			pqServiceRepository.save(aircelPrepaid);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(10000);
		commission.setType("PRE");
		commission.setValue(4.10);
		commission.setFixed(false);
		commission.setService(aircelPrepaid);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

	/*	PQService bus = pqServiceRepository.findServiceByOperatorCode("BUS");
=======
		/*PQService bus = pqServiceRepository.findServiceByOperatorCode("BUS");
>>>>>>> origin/as_1.0
		if (bus == null) {
			bus = new PQService();
			bus.setName("Bus");
			bus.setDescription("Travel");
			bus.setMinAmount(1);
			bus.setMaxAmount(99999);
			bus.setCode("VBUS");
			bus.setOperatorCode("BUS");
			bus.setServiceType(serviceBillPayment);
			bus.setOperator(vpayqwik);
			bus.setStatus(Status.Active);
			pqServiceRepository.save(bus);
		}

		commission = new PQCommission();
		commission.setMinAmount(1);
		commission.setMaxAmount(99999);
		commission.setType("PRE");
		commission.setValue(4.50);
		commission.setFixed(false);
		commission.setService(bus);
		commission.setIdentifier(commissionApi.createCommissionIdentifier(commission));
		if (commissionApi.findCommissionByIdentifier(commission.getIdentifier()) == null) {
			commissionApi.save(commission);
		}

	}*/
	}
}
