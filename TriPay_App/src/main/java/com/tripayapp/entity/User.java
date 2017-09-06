package com.tripayapp.entity;

import javax.persistence.*;

import com.tripayapp.model.Status;
import com.tripayapp.model.UserType;

@Entity
public class User extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private UserType userType;

	@Column(nullable = false)
	private String authority;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status emailStatus;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status mobileStatus;

	@OneToOne(fetch = FetchType.EAGER)
	private UserDetail userDetail;

	@OneToOne(fetch = FetchType.EAGER)
	private PQAccountDetail accountDetail;

	@Column
	private String emailToken;

	@Column
	private String mobileToken;

	@Lob
	private String gcmId;

	public String getGcmId() {
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	public String getUsername() {
		return username;
	}

	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public String getMobileToken() {
		return mobileToken;
	}

	public void setMobileToken(String mobileToken) {
		this.mobileToken = mobileToken;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Status getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(Status emailStatus) {
		this.emailStatus = emailStatus;
	}

	public Status getMobileStatus() {
		return mobileStatus;
	}

	public void setMobileStatus(Status mobileStatus) {
		this.mobileStatus = mobileStatus;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public PQAccountDetail getAccountDetail() {
		return accountDetail;
	}

	public void setAccountDetail(PQAccountDetail accountDetail) {
		this.accountDetail = accountDetail;
	}

}
