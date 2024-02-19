package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.YogaServiceImplementations;
import com.example.demo.entity.LoginUser;
import com.example.demo.entity.InstructorLogin;
import com.example.demo.entity.InstructorRegistration;

@RestController
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private YogaServiceImplementations yogaService;

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/loginUser")
	public ResponseEntity<?> loginUser(@RequestBody LoginUser user) {
		logger.info("Inside Login");
		return yogaService.loginUser(user);
	}

//	@CrossOrigin(origins = "http://localhost:4200")
//	@PostMapping("/adminLogin")
//	public ResponseEntity<?> adminLogin(@RequestBody InstructorLogin user) {
//		logger.info("Inside Admin Login");
//		return yogaService.adminSignin(user);
//	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/logout/{userId}")
	public ResponseEntity<?> logout(@RequestHeader("Session") String session, @PathVariable("userId") String userId) {
	    if (!yogaService.isValidSession(userId, session)) {
	        return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
	    }
	    logger.info("loggingOut");
	    return yogaService.logOut(userId);
	}

}
