package com.example.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="instructor_login")
public class InstructorLogin {
	
	@Id
	@Column(name="email")
	private String email;

	@Column(name="password")
	private String password;
	
	@Transient
	private String session;
	
	@Transient
	private String role;

	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	@Override
	public String toString() {
		return "LoginUser [email=" + email + ", password=" + password + ", session=" + session + "]";
	}

	public InstructorLogin(String email, String password, String session) {
		super();
		this.email = email;
		this.password = password;
		this.session = session;
	}

	public InstructorLogin() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
