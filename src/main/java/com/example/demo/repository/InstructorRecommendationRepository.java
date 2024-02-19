package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.InstructorLogin;
import com.example.demo.entity.InstructorRecommendation;

public interface InstructorRecommendationRepository extends JpaRepository<InstructorRecommendation, Integer> {

	List<InstructorRecommendation> findByEmail(String userId);

}
