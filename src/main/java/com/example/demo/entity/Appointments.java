package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "appointments")
public class Appointments {
	public Appointments() {

	}

	@Id
	@Column(name = "sno")
	private int sno;

	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "instructor_email")
	private String instructorEmail;

	@Column(name = "instructor_name")
	private String instructorName;

	@Column(name = "timings")
	private String timings;

	@Column(name = "user_notes")
	private String userNotes;

	@Column(name = "instructor_notes")
	private String instructorNotes;

	@Column(name = "status")
	private String status;

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getInstructorEmail() {
		return instructorEmail;
	}

	public void setInstructorEmail(String instructorEmail) {
		this.instructorEmail = instructorEmail;
	}

	public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public String getTimings() {
		return timings;
	}

	public void setTimings(String timings) {
		this.timings = timings;
	}

	public String getUserNotes() {
		return userNotes;
	}

	public void setUserNotes(String userNotes) {
		this.userNotes = userNotes;
	}

	public String getInstructorNotes() {
		return instructorNotes;
	}

	public void setInstructorNotes(String instructorNotes) {
		this.instructorNotes = instructorNotes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Appointments [sno=" + sno + ", userEmail=" + userEmail + ", userName=" + userName + ", instructorEmail="
				+ instructorEmail + ", instructorName=" + instructorName + ", timings=" + timings + ", userNotes="
				+ userNotes + ", instructorNotes=" + instructorNotes + ", status=" + status + "]";
	}

	public Appointments(int sno, String userEmail, String userName, String instructorEmail, String instructorName,
			String timings, String userNotes, String instructorNotes, String status) {
		super();
		this.sno = sno;
		this.userEmail = userEmail;
		this.userName = userName;
		this.instructorEmail = instructorEmail;
		this.instructorName = instructorName;
		this.timings = timings;
		this.userNotes = userNotes;
		this.instructorNotes = instructorNotes;
		this.status = status;
	}
	
	
	

}
