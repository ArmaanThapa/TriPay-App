package com.tripayapp.model;

public enum ServiceType {

	Registration("Registration"), TopUp("TopUp"), BillPay("BillPay"), SendMoney("SendMoney"), LoadMoney(
			"LoadMoney"), InviteFriend("InviteFriend"), Redeem("Redeem");

	private final String value;

	private ServiceType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String getValue() {
		return value;
	}

	public static ServiceType getEnum(String value) {
		if (value == null)
			throw new IllegalArgumentException();
		for (ServiceType serviceType : values())
			if (value.equalsIgnoreCase(serviceType.getValue()))
				return serviceType;
		throw new IllegalArgumentException();
	}
}
