package com.example.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="instructor_recommendation")
public class InstructorRecommendation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sno")
	private String sno;

	@Column(name="user_email")
	private String email;
	
	@Column(name="course_id")
	private int courseId;

	@Column(name="recommended_by")
	private String recommendedBy;
	
	public InstructorRecommendation() {
		// TODO Auto-generated constructor stub
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	public String getRecommendedBy() {
		return recommendedBy;
	}

	public void setRecommendedBy(String recommendedBy) {
		this.recommendedBy = recommendedBy;
	}

	@Override
	public String toString() {
		return "InstructorRecommendation [sno=" + sno + ", email=" + email + ", courseId=" + courseId
				+ ", recommendedBy=" + recommendedBy + "]";
	}

	public InstructorRecommendation(String sno, String email, int courseId, String recommendedBy) {
		super();
		this.sno = sno;
		this.email = email;
		this.courseId = courseId;
		this.recommendedBy = recommendedBy;
	}


	
	
	
	
}
