package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Appointments;

public interface AppointmentRepository extends JpaRepository<Appointments, Integer> {

	List<Appointments> findByUserEmail(String userEmail);

	List<Appointments> findByUserEmailAndStatusIn(String userEmail, List<String> statuses);

	Appointments findBySno(int sno);

	List<Appointments> findByInstructorEmail(String instructorEmail);

	List<Appointments> findByInstructorEmailAndStatusIn(String userEmail, List<String> statuses);

}
