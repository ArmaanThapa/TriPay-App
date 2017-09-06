package com.tripayapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TravelUserDetail extends AbstractEntity<Long> {

	private static final long serialVersionUID=1L;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String age;
	
	@Column(nullable=false)
	private String mobileNo;
	
	@Column(nullable=false)
	private String postalCode;
	
	@Column(nullable=false)
	private String address;
	
	@Column(nullable=false)
	private boolean partialCancellationAllowed;

	@Column(nullable=false)
	private String emailId;
	
	@Column(nullable=false)
	private String gender;
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isPartialCancellationAllowed() {
		return partialCancellationAllowed;
	}

	public void setPartialCancellationAllowed(boolean partialCancellationAllowed) {
		this.partialCancellationAllowed = partialCancellationAllowed;
	}
	
}
