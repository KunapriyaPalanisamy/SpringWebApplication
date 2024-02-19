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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Service.YogaServiceImplementations;
import com.example.demo.entity.UserRegister;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.entity.Appointments;
import com.example.demo.entity.CourseData;
import com.example.demo.entity.Courses;
import com.example.demo.entity.InstructorRecommendation;
import com.example.demo.entity.InstructorRegistration;

@RestController
public class CourseController {

	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

	@Autowired
	private YogaServiceImplementations yogaService;

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/addCourse")
	public ResponseEntity<?> addCourse(@RequestHeader("Session") String session, @ModelAttribute CourseData request) {
		try {
			MultipartFile file = request.getFile();
			String data = request.getData();
			ObjectMapper mapper = new ObjectMapper();
			Courses req = mapper.readValue(data, Courses.class);
			if (!yogaService.isValidSession(req.getEmail(), session)) {
				return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
			}
			logger.info("adding course" + data.toString());
			Courses course = yogaService.addCourse(file, req);
			return new ResponseEntity<>(course, HttpStatus.OK);
		} catch (JsonParseException e) {
			return new ResponseEntity<>("Parsing Exception", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getAllCourses/{userId}")
	public ResponseEntity<?> getAllCourses(@RequestHeader("Session") String session,
			@PathVariable("userId") String userEmail) {
		if (!yogaService.isValidSession(userEmail, session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info("Fetching all course details");
		List<Courses> courses = yogaService.getAllCourses();
		return new ResponseEntity<List<Courses>>(courses, HttpStatus.OK);
	}

	/* Recommendation Algorithm */

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getRecommendedCourses/{userId}")
	public ResponseEntity<?> recommendedCourses(@RequestHeader("Session") String session,
			@PathVariable("userId") String userId) {
		if (!yogaService.isValidSession(userId, session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info("Fetching recommended Courses details");
		List<Courses> courses = yogaService.getRecommendedCourses(userId);
		return new ResponseEntity<List<Courses>>(courses, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/instructorRecommendation")
	public ResponseEntity<?> addRecommendedCourse(@RequestHeader("Session") String session,
			@RequestBody InstructorRecommendation request) {
		if (!yogaService.isValidSession(request.getRecommendedBy(), session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info("adding recommendation" + request.toString());
		InstructorRecommendation recommendation = yogaService.recommend(request);
		return new ResponseEntity<>(recommendation, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/getInstructorRecommendedCourses/{userId}")
	public ResponseEntity<?> getInstructorRecommendedCourses(@RequestHeader("Session") String session,
			@PathVariable("userId") String userId) {
		if (!yogaService.isValidSession(userId, session)) {
			return new ResponseEntity<String>("Invalid session", HttpStatus.UNAUTHORIZED);
		}
		logger.info("Fetching instructor recommended Courses details");
		List<Courses> courses = yogaService.getInstructorRecommendedCourses(userId);
		return new ResponseEntity<List<Courses>>(courses, HttpStatus.OK);
	}
	
	

}
