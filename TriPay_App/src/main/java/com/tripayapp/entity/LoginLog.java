package com.tripayapp.entity;

import javax.persistence.*;

import com.tripayapp.model.Status;

@Entity
public class LoginLog extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.EAGER)
	private User user;

	@Column
	private String remoteAddress;

	@Column
	private String serverIp;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;



	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
