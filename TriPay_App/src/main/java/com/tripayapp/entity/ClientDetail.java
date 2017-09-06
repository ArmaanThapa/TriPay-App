package com.tripayapp.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.tripayapp.util.Status;

@Entity
public class ClientDetail extends AbstractEntity<Long> {

	
	private static final long serialVersionUID=1L;
	
	@Column(nullable=false)
	private String clientName;
	
	@Column(nullable=false)
	private String ipAddress;
	
	@Column(unique=true)
	private String token;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Status clientStatus;
	
	//OneToMany(mappedBy = "clientDetailID",cascade=CascadeType.REMOVE)
	
	@OneToMany(mappedBy ="clientDetailID",cascade=CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<PQService> serviceID;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Status getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(Status clientStatus) {
		this.clientStatus = clientStatus;
	}

	}
	
	
	

