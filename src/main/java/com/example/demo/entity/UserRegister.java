package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "user_details")
public class UserRegister {
	public UserRegister() {

	}

	@Id
	@Column(name = "email")
	private String email;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "age")
	private int age;

	@Column(name = "frequency_of_yoga_practice")
	private String frequency;

	@Column(name = "gender")
	private String gender;

	@Column(name = "goals_of_practicing_yoga")
	private String goals;

	@Column(name = "medical_condition")
	private String medicalCondition;

	@Transient
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getMedicalCondition() {
		return medicalCondition;
	}

	public void setMedicalCondition(String medicalCondition) {
		this.medicalCondition = medicalCondition;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserRegister [email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age
				+ ", frequency=" + frequency + ", gender=" + gender + ", goals=" + goals + ", medicalCondition="
				+ medicalCondition + ", password=" + password + "]";
	}

	public UserRegister(String email, String firstName, String lastName, int age, String frequency, String gender,
			String goals, String medicalCondition, String password) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.frequency = frequency;
		this.gender = gender;
		this.goals = goals;
		this.medicalCondition = medicalCondition;
		this.password = password;
	}

}
