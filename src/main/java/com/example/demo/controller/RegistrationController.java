package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.YogaServiceImplementations;
import com.example.demo.entity.InstructorRegistration;
import com.example.demo.entity.UserRegister;


@RestController
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private YogaServiceImplementations yogaService;

    @CrossOrigin(origins="http://localhost:4200")
    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody UserRegister user) {
    	logger.info("Inside Registration");
        return yogaService.registerUser(user);
    }
    
    @CrossOrigin(origins="http://localhost:4200")
    @PostMapping("/registerInstructor")
    public ResponseEntity<?> registerUser(@RequestBody InstructorRegistration instructor) {
    	logger.info("Inside Instructor Registration");
        return yogaService.registerInstructor(instructor);
    }
}
