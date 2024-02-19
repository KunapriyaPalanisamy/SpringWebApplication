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
public class InstructorController {

	private static final Logger logger = LoggerFactory.getLogger(InstructorController.class);

	@Autowired
	private YogaServiceImplementations yogaService;

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getUsers/{userId}")
	public ResponseEntity<?> getUsers(@RequestHeader("Session") String session, @PathVariable("userId") String userId) {
		if (!yogaService.isValidSession(userId, session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info("Fetching Users ");
		List<UserRegister> users = yogaService.getUsers();
		return new ResponseEntity<List<UserRegister>>(users, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getInstructorappointments/{userId}")
	public ResponseEntity<?> getInstructorAppointments(@RequestHeader("Session") String session,
			@PathVariable("userId") String userEmail) {
		if (!yogaService.isValidSession(userEmail, session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info("Fetching all appointment details");
		List<Appointments> appointments = yogaService.getAllInstructorappointments(userEmail);
		return new ResponseEntity<List<Appointments>>(appointments, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getInstructorActiveappointments/{userId}")
	public ResponseEntity<?> getInstructorActiveAppointments(@RequestHeader("Session") String session,
			@PathVariable("userId") String userEmail) {
		if (!yogaService.isValidSession(userEmail, session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info("Fetching all active appointment details");
		List<Appointments> appointments = yogaService.getAllInstructorActiveAppointments(userEmail);
		return new ResponseEntity<List<Appointments>>(appointments, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/confirmAppointment")
	public ResponseEntity<?> confirmAppointment(@RequestHeader("Session") String session,
			@RequestBody Appointments request) {
		if (!yogaService.isValidSession(request.getUserEmail(), session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info("Confirming appointment" + request.toString());
		Appointments appointments = yogaService.confirmAppointment(request);
		return new ResponseEntity<>(appointments, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/markCompleted")
	public ResponseEntity<?> appointmentComplete(@RequestHeader("Session") String session,
			@RequestBody Appointments request) {
		if (!yogaService.isValidSession(request.getUserEmail(), session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info(" appointment completed" + request.toString());
		Appointments appointments = yogaService.appointmentCompletion(request);
		return new ResponseEntity<>(appointments, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getInstructorProfileDetails/{userId}")
	public ResponseEntity<?> getInstructorProfileDetails(@RequestHeader("Session") String session,
			@PathVariable("userId") String userEmail) {
		if (!yogaService.isValidSession(userEmail, session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info("Fetching instructor profile details details");
		Optional<InstructorRegistration> response = yogaService.getInstructorRegister(userEmail);
		if (response.isPresent()) {
			return new ResponseEntity<InstructorRegistration>(response.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
		}
	}

}
