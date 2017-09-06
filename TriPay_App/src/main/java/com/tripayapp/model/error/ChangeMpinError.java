package com.tripayapp.model.error;

public class ChangeMpinError {

	private boolean valid;
	private String username;
	private String oldMpin;
	private String newMpin;
	private String confirmMpin;

	public String getUsername() {
		return username;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldMpin() {
		return oldMpin;
	}

	public void setOldMpin(String oldMpin) {
		this.oldMpin = oldMpin;
	}

	public String getNewMpin() {
		return newMpin;
	}

	public void setNewMpin(String newMpin) {
		this.newMpin = newMpin;
	}

	public String getConfirmMpin() {
		return confirmMpin;
	}

	public void setConfirmMpin(String confirmMpin) {
		this.confirmMpin = confirmMpin;
	}

}
