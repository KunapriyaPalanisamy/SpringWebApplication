package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

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
import com.example.demo.entity.UserRegister;
import com.example.demo.entity.Appointments;
import com.example.demo.entity.InstructorRegistration;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private YogaServiceImplementations yogaService;

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getInstructors/{userId}")
	public ResponseEntity<?> getInstructors(@RequestHeader("Session") String session, @PathVariable("userId") String userId) {
	    if(!yogaService.isValidSession(userId, session)) {
	        return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
	    }
	    logger.info("Fetching Instructor details");
	    List<InstructorRegistration> instructors = yogaService.getInstructors();
	    return new ResponseEntity<List<InstructorRegistration>>(instructors, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getMyappointments/{userId}")
	public ResponseEntity<?> getAppointments(@RequestHeader("Session") String session, @PathVariable("userId") String userEmail) {
	    if(!yogaService.isValidSession(userEmail, session)) {
	        return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
	    }
	    logger.info("Fetching all appointment details");
	    List<Appointments> appointments = yogaService.getAllappointments(userEmail);
	    return new ResponseEntity<List<Appointments>>(appointments, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getMyActiveappointments/{userId}")
	public ResponseEntity<?> getActiveAppointments(@RequestHeader("Session") String session, @PathVariable("userId") String userEmail) {
	    if(!yogaService.isValidSession(userEmail, session)) {
	        return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
	    }
	    logger.info("Fetching all active appointment details");
	    List<Appointments> appointments = yogaService.getAllActiveAppointments(userEmail);
	    return new ResponseEntity<List<Appointments>>(appointments, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/fixAppointment")
	public ResponseEntity<?> fixAppointment(@RequestHeader("Session") String session, @RequestBody Appointments request) {
	    if(!yogaService.isValidSession(request.getUserEmail(), session)) {
	        return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
	    }
	    logger.info("fixing appointment"+request.toString());
	    Appointments appointments = yogaService.fixAppointment(request);
	    return new ResponseEntity<>(appointments, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/cancelAppointment")
	public ResponseEntity<?> cancelAppointment(@RequestHeader("Session") String session, @RequestBody Appointments request) {
	    if(!yogaService.isValidSession(request.getUserEmail(), session)) {
	        return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
	    }
	    logger.info("Cancelling appointment"+request.toString());
	    Appointments appointments = yogaService.cancelAppointment(request);
	    return new ResponseEntity<>(appointments, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getProfileDetails/{userId}")
	public ResponseEntity<?> getProfileDetails(@RequestHeader("Session") String session, @PathVariable("userId") String userEmail) {
	    if(!yogaService.isValidSession(userEmail, session)) {
	        return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
	    }
	    logger.info("Fetching user details");
	    Optional<UserRegister> response = yogaService.getUserRegister(userEmail);
	    if(response.isPresent()) {
	        return new ResponseEntity<UserRegister>(response.get(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
	    }
	}

	


}
