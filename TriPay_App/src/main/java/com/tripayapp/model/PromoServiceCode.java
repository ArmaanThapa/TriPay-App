package com.tripayapp.model;

public enum PromoServiceCode {
	
	PTPS("PTPS"), PBPS("PBPS"), PECS("PECS"), PLMS(
			"PLMS"), PGAS("PGAS"), PINS("PINS");

	private final String value;

	private PromoServiceCode(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String getValue() {
		return value;
	}

	public static PromoServiceCode getEnum(String value) {
		if (value == null)
			throw new IllegalArgumentException();
		for (PromoServiceCode serviceType : values())
			if (value.equalsIgnoreCase(serviceType.getValue()))
				return serviceType;
		throw new IllegalArgumentException();
	}

}
