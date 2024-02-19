package com.example.demo.controller;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.YogaServiceImplementations;
import com.example.demo.entity.Otp;

@RestController
public class OtpController {

	private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

	@Autowired
	private YogaServiceImplementations yogaService;

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/otp")
	public Otp sendOtp(@RequestBody Otp request) {
		logger.info("Inside otp service");
		return yogaService.sendOtp(request);
	}

	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/verifyOtp")
	public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Otp request) {
	    boolean isValid = yogaService.verifyOtp(request.getEmail(), request.getOtp());

	    if (isValid) {
	        logger.info("verification successful");
	        return ResponseEntity.ok(Collections.singletonMap("message", "Verification successful"));
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Invalid OTP"));
	    }
	}

}
