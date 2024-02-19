package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Otp;

public interface OtpRepository extends JpaRepository<Otp, String> {

	Otp findByEmail(String email);

}
