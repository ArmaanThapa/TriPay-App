package com.tripayapp.entity;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.*;

import com.tripayapp.model.Gender;

@Entity
public class UserDetail extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String firstName;

	@Column
	private String middleName;

	@Column(nullable = false)
	private String lastName;

	@Column
	private String address;

	@Column(nullable = false)
	private String contactNo;

	@Column(nullable = false)
	private String email;

	@Column
	private String image;

	@Column
	private String mpin;

	@Column
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;

	@Column
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@OneToOne(fetch = FetchType.EAGER)
	private LocationDetails location;


	public LocationDetails getLocation() {
		return location;
	}

	public void setLocation(LocationDetails location) {
		this.location = location;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	@Transient
	public String getName() {
		StringBuilder name = new StringBuilder();

		name.append(firstName);
		name.append(" ");
		name.append(lastName);

		return name.toString();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getMpin() {
		return mpin;
	}

	public void setMpin(String mpin) {
		this.mpin = mpin;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

}
