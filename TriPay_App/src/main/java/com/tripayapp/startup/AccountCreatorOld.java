package com.tripayapp.startup;

import com.tripayapp.entity.PQCommission;
import com.tripayapp.entity.PQOperator;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.PQServiceType;
import com.tripayapp.model.Status;
import com.tripayapp.repositories.PQCommissionRepository;
import com.tripayapp.repositories.PQOperatorRepository;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.PQServiceTypeRepository;

public class AccountCreatorOld {

	private final PQServiceTypeRepository pqServiceTypeRepository;
	private final PQOperatorRepository pqOperatorRepository;
	private final PQServiceRepository pqServiceRepository;
	private final PQCommissionRepository pqCommissionRepository;

	public AccountCreatorOld(PQServiceTypeRepository pqServiceTypeRepository, PQOperatorRepository pqOperatorRepository,
			PQServiceRepository pqServiceRepository, PQCommissionRepository pqCommissionRepository) {
		this.pqServiceTypeRepository = pqServiceTypeRepository;
		this.pqOperatorRepository = pqOperatorRepository;
		this.pqServiceRepository = pqServiceRepository;
		this.pqCommissionRepository = pqCommissionRepository;
	}
	
	public void create() {

		PQOperator instantPay = pqOperatorRepository.findOperatorByName("InstantPay");
		if (instantPay == null) {
			instantPay = new PQOperator();
			instantPay.setName("InstantPay");
			instantPay.setStatus(Status.Active);
		}
		
		PQOperator ebs = pqOperatorRepository.findOperatorByName("EBS");
		if (ebs == null) {
			ebs = new PQOperator();
			ebs.setName("EBS");
			ebs.setStatus(Status.Active);
		}

		PQServiceType serviceLoadMoney = pqServiceTypeRepository.findServiceTypeByName("Load Money");
		if (serviceLoadMoney == null) {
			serviceLoadMoney = new PQServiceType();
			serviceLoadMoney.setName("Load Money");
			serviceLoadMoney.setDescription("Load Money in VPayQwik.");
			pqServiceTypeRepository.save(serviceLoadMoney);
		}

		PQServiceType serviceMerchantPayment = pqServiceTypeRepository.findServiceTypeByName("Merchant Payment");
		if (serviceMerchantPayment == null) {
			serviceMerchantPayment = new PQServiceType();
			serviceMerchantPayment.setName("Merchant Payment");
			serviceMerchantPayment.setDescription("Pay to merchant.");
			pqServiceTypeRepository.save(serviceMerchantPayment);
		}

		PQServiceType serviceFundTransfer = pqServiceTypeRepository.findServiceTypeByName("Fund Transfer");
		if (serviceFundTransfer == null) {
			serviceFundTransfer = new PQServiceType();
			serviceFundTransfer.setName("Fund Transfer");
			serviceFundTransfer.setDescription("Transfer fund to other users.");
			pqServiceTypeRepository.save(serviceFundTransfer);
		}

		PQServiceType serviceBillPayment = pqServiceTypeRepository.findServiceTypeByName("Bill Payment");
		if (serviceBillPayment == null) {
			serviceBillPayment = new PQServiceType();
			serviceBillPayment.setName("Bill Payment");
			serviceBillPayment.setDescription("Pay mobile and other utility bills.");
			pqServiceTypeRepository.save(serviceBillPayment);
		}

		PQService ebsLoadMoney = pqServiceRepository.findServiceByOperatorCode("LMC");
		if (ebsLoadMoney == null) {
			ebsLoadMoney = new PQService();
			ebsLoadMoney.setName("Load Money");
			ebsLoadMoney.setDescription("EBS Load Money");
			ebsLoadMoney.setMinAmount(1);
			ebsLoadMoney.setMaxAmount(10000);
			ebsLoadMoney.setCode("LMC");
			ebsLoadMoney.setOperatorCode("LMC");
			ebsLoadMoney.setServiceType(serviceLoadMoney);
			ebsLoadMoney.setOperator(ebs);
			pqServiceRepository.save(ebsLoadMoney);
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
			airtelPrepaidTopup.setOperator(instantPay);
			pqServiceRepository.save(airtelPrepaidTopup);
		}

		PQCommission commissionAirtel = pqCommissionRepository
				.findCommissionByIdentifier("com1");
		if (commissionAirtel == null) {
			commissionAirtel = new PQCommission();
			commissionAirtel.setMinAmount(10);
			commissionAirtel.setMaxAmount(10000);
			commissionAirtel.setType("PRE");
			commissionAirtel.setValue(1);
			commissionAirtel.setFixed(false);
			commissionAirtel.setIdentifier("com1");
			pqCommissionRepository.save(commissionAirtel);
		}

	}

}
