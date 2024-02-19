package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.InstructorLogin;

public interface AdminLoginRepository extends JpaRepository<InstructorLogin, String> {

}
