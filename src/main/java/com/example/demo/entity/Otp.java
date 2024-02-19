package com.example.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="otp")
public class Otp {
	
	@Id
	@Column(name="email")
	private String email;

	@Column(name="otp")
	private String otp;

	@Transient
	private String password;

	public Otp(String email, String otp, String password) {
		super();
		this.email = email;
		this.otp = otp;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "Otp [email=" + email + ", otp=" + otp + ", password=" + password + "]";
	}

	public Otp() {
	
	}
	
	
	
	
	
}
