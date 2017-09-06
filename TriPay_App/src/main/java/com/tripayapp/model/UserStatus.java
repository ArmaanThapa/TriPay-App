package com.tripayapp.model;

public enum UserStatus {
	ALL("All"), ONLINE("Online"), ACTIVE("Active"), BLOCKED("Blocked"), INACTIVE("Inactive"),LOCKED("Locked");
	private final String value;

	private UserStatus(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String getValue() {
		return value;
	}

	public static UserStatus getEnum(String value) {
		if (value == null)
			throw new IllegalArgumentException();
		for (UserStatus v : values())
			if (value.equalsIgnoreCase(v.getValue()))
				return v;
		throw new IllegalArgumentException();
	}

}
