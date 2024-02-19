package com.example.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="session")
public class SessionEntity {
	
	@Id
	@Column(name="email")
	private String email;
	
	@Column(name="session")
	private String session;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public SessionEntity(String email, String session) {
		super();
		this.email = email;
		this.session = session;
	}

	@Override
	public String toString() {
		return "session [email=" + email + ", session=" + session + "]";
	}

	public SessionEntity() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
