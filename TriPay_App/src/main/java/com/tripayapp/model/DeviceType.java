package com.tripayapp.model;

public enum DeviceType {

	Andriod("Android"),Browser("Browser"),Windows("Windows"),IOS("IOS");
	
	private final String value;
	
	private DeviceType(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return value;
	}

	public String getValue() {
		return value;
	}

	public static DeviceType getEnum(String value) {
		if (value == null)
			throw new IllegalArgumentException();
		for (DeviceType v : values())
			if (value.equalsIgnoreCase(v.getValue()))
				return v;
		throw new IllegalArgumentException();
	}
}
