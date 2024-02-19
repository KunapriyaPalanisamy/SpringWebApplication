package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.LoginUser;

public interface UserLoginRepository extends JpaRepository<LoginUser, String> {

}
