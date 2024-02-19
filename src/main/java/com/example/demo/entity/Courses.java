package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Courses {
	public Courses() {

	}

	@Id
	@Column(name = "sno")
	private int sno;

	@Column(name = "email")
	private String email;
	
	@Column(name = "course_name")
	private String courseName;

	@Column(name = "description")
	private String description;

	@Column(name = "min_age_category")
	private String minAge;

	@Column(name = "max_age_category")
	private String maxAge;

	@Column(name = "goals")
	private String goals;

	@Column(name = "good_for_health_condition")
	private String goodFor;

	@Column(name = "bad_for_health_condition")
	private String badFor;

	@Column(name = "path")
	private String path;

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getGoodFor() {
		return goodFor;
	}

	public void setGoodFor(String goodFor) {
		this.goodFor = goodFor;
	}

	public String getBadFor() {
		return badFor;
	}

	public void setBadFor(String badFor) {
		this.badFor = badFor;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Courses [sno=" + sno + ", email=" + email + ", courseName=" + courseName + ", description="
				+ description + ", minAge=" + minAge + ", maxAge=" + maxAge + ", goals=" + goals + ", goodFor="
				+ goodFor + ", badFor=" + badFor + ", path=" + path + "]";
	}

	public Courses(int sno, String email, String courseName, String description, String minAge, String maxAge,
			String goals, String goodFor, String badFor, String path) {
		super();
		this.sno = sno;
		this.email = email;
		this.courseName = courseName;
		this.description = description;
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.goals = goals;
		this.goodFor = goodFor;
		this.badFor = badFor;
		this.path = path;
	}
	

}
