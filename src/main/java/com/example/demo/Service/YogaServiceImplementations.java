package com.example.demo.Service;

import java.io.InputStream;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Exception.CustomException;
import com.example.demo.entity.Appointments;
import com.example.demo.entity.Courses;
import com.example.demo.entity.InstructorLogin;
import com.example.demo.entity.InstructorRecommendation;
import com.example.demo.entity.InstructorRegistration;
import com.example.demo.entity.LoginUser;
import com.example.demo.entity.Otp;
import com.example.demo.entity.UserRegister;
import com.example.demo.entity.SessionEntity;
import com.example.demo.repository.AdminLoginRepository;
import com.example.demo.repository.InstructorLoginRepository;
import com.example.demo.repository.InstructorRecommendationRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.OtpRepository;
import com.example.demo.repository.UserLoginRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserSessionRepository;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.CourseRepository;

import jakarta.transaction.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class YogaServiceImplementations {

	private static final Logger logger = LoggerFactory.getLogger(YogaServiceImplementations.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private S3Presigner s3Presigner;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Autowired
	private AdminLoginRepository adminLoginRepository;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private InstructorRepository instructorRepository;

	@Autowired
	private InstructorLoginRepository instructorLoginRepository;

	@Autowired
	private UserSessionRepository sessionRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	private static final String BUCKET_NAME = "yogaapplicationbucket";
	private static final String FOLDER_NAME = "courses";

	private final S3Client s3Client;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private InstructorRecommendationRepository instructorRecommendationRepository;

	public YogaServiceImplementations() {
		s3Client = S3Client.builder().region(Region.EU_NORTH_1).build();
	}

	@Transactional
	public ResponseEntity<?> registerUser(UserRegister user) {
		if(instructorRepository.findById(user.getEmail()).isPresent()) {
		    throw new CustomException("601", "User registered already as instructor");
		}
		if(userRepository.findById(user.getEmail()).isPresent()) {
		    throw new CustomException("602", "User registered already ");
		}

		logger.info("Registering user now");

		// Hashing the password using BCrypt
		String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashedPassword);

		UserRegister registeredUser = userRepository.save(user);

		LoginUser saveObj = new LoginUser();
		saveObj.setEmail(user.getEmail());
		saveObj.setPassword(hashedPassword);
		userLoginRepository.save(saveObj);
		logger.info("saving login user now");
		logger.info(user.getEmail());
		sendMail(user.getEmail(), "User Registration- YogaNow", "You have successfully registered.");
		logger.info("sent mail");
		return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
	}

	public ResponseEntity<?> loginUser(LoginUser user) {
		logger.info("Authenticating user");

		LoginUser storedUser = userLoginRepository.findById(user.getEmail()).orElse(null);

		if (storedUser == null) {
			InstructorLogin storedadmin = adminLoginRepository.findById(user.getEmail()).orElse(null);

			if (storedadmin == null) {
				return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
			}
			if (BCrypt.checkpw(user.getPassword(), storedadmin.getPassword())) {

				String session = UUID.randomUUID().toString();
				storedadmin.setSession(session);
				storedadmin.setRole("admin");
				SessionEntity saveObj = new SessionEntity();
				saveObj.setEmail(user.getEmail());
				saveObj.setSession(session);
				userSessionRepository.save(saveObj);
				return new ResponseEntity<>(storedadmin, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
			}
		} else {
			if (BCrypt.checkpw(user.getPassword(), storedUser.getPassword())) {

				String session = UUID.randomUUID().toString();
				storedUser.setSession(session);
				storedUser.setRole("user");

				SessionEntity saveObj = new SessionEntity();
				saveObj.setEmail(user.getEmail());
				saveObj.setSession(session);
				userSessionRepository.save(saveObj);
				return new ResponseEntity<>(storedUser, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
			}

		}

	}

//	public ResponseEntity<?> adminSignin(InstructorLogin user) {
//		logger.info("Authenticating user");
//
//		InstructorLogin storedUser = adminLoginRepository.findById(user.getEmail()).orElse(null);
//
//		if (storedUser == null) {
//			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
//		}
//
//		if (BCrypt.checkpw(user.getPassword(), storedUser.getPassword())) {
//
//			String session = UUID.randomUUID().toString();
//			storedUser.setSession(session);
//
//			SessionEntity saveObj = new SessionEntity();
//			saveObj.setEmail(user.getEmail());
//			saveObj.setSession(session);
//			userSessionRepository.save(saveObj);
//			return new ResponseEntity<>(storedUser, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
//		}
//	}

	public void sendMail(String to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		mailSender.send(message);
	}

	public String generateOtp() {
		Random random = new Random();
		return String.format("%06d", random.nextInt(1000000));
	}

	public Otp sendOtp(Otp request) {
		Otp response = new Otp();
		String otp = generateOtp();
		sendMail(request.getEmail(), "Your OTP Code", "Your OTP code is: " + otp);
		request.setOtp(otp);
		response.setEmail(request.getEmail());

		otpRepository.save(request);

		return response;
	}

	public boolean verifyOtp(String email, String providedOtp) {
		Otp storedOtpEntity = otpRepository.findByEmail(email);
		if (storedOtpEntity != null && storedOtpEntity.getOtp().equals(providedOtp)) {
			otpRepository.delete(storedOtpEntity);
			return true;
		}
		return false;
	}

	@Transactional
	public ResponseEntity<?> registerInstructor(InstructorRegistration instructor) {
		if(instructorRepository.findById(instructor.getEmail()).isPresent()) {
		    throw new CustomException("601", "instructor registered already ");
		}
		if(userRepository.findById(instructor.getEmail()).isPresent()) {
		    throw new CustomException("602", "instructor registered already as user ");
		}
		logger.info("Registering instructor now");

		// Hashing the password using BCrypt
		String hashedPassword = BCrypt.hashpw(instructor.getPassword(), BCrypt.gensalt());
		instructor.setPassword(hashedPassword);

		InstructorRegistration registeredInstructor = instructorRepository.save(instructor);

		InstructorLogin saveObj = new InstructorLogin();
		saveObj.setEmail(instructor.getEmail());
		saveObj.setPassword(hashedPassword);
		instructorLoginRepository.save(saveObj);
		sendMail(instructor.getEmail(), "instructor Registration- YogaNow", "You have successfully registered.");
		return new ResponseEntity<>(registeredInstructor, HttpStatus.CREATED);
	}

	public boolean isValidSession(String userId, String session) {
		Optional<SessionEntity> sessionObj = Optional.ofNullable(new SessionEntity());
		sessionObj = sessionRepository.findById(userId);
		if (sessionObj.get().getSession().equals(session)) {
			return true;
		} else {
			return false;
		}
	}

	public List<InstructorRegistration> getInstructors() {
		return instructorRepository.findAll();
	}

	public List<Appointments> getAllappointments(String userEmail) {

		return appointmentRepository.findByUserEmail(userEmail);
	}

	public List<Appointments> getAllActiveAppointments(String userEmail) {
		List<String> statuses = Arrays.asList("PENDING", "CONFIRMED");
		return appointmentRepository.findByUserEmailAndStatusIn(userEmail, statuses);
	}

	public Appointments fixAppointment(Appointments request) {
		request.setStatus("PENDING");
		appointmentRepository.save(request);
		return request;
	}

	public ResponseEntity<String> logOut(String userId) {
		try {
			userSessionRepository.deleteById(userId);
			return new ResponseEntity<>("User logged out successfully.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error while logging out.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public Appointments cancelAppointment(Appointments request) {
		Appointments cancelObj = new Appointments();
		cancelObj = appointmentRepository.findBySno(request.getSno());
		cancelObj.setStatus("CANCELLED");
		appointmentRepository.save(cancelObj);
		return cancelObj;
	}

	public Optional<UserRegister> getUserRegister(String userEmail) {
		return userRepository.findById(userEmail);
	}

	public List<UserRegister> getUsers() {
		return userRepository.findAll();
	}

	public List<Appointments> getAllInstructorappointments(String instructorEmail) {

		return appointmentRepository.findByInstructorEmail(instructorEmail);
	}

	public List<Appointments> getAllInstructorActiveAppointments(String userEmail) {
		List<String> statuses = Arrays.asList("PENDING", "CONFIRMED");
		return appointmentRepository.findByInstructorEmailAndStatusIn(userEmail, statuses);
	}

	public Appointments confirmAppointment(Appointments request) {
		Appointments confirmObj = new Appointments();
		confirmObj = appointmentRepository.findBySno(request.getSno());
		confirmObj.setStatus("CONFIRMED");
		confirmObj.setInstructorNotes(request.getInstructorNotes());
		appointmentRepository.save(confirmObj);
		return confirmObj;
	}

	public Appointments appointmentCompletion(Appointments request) {
		Appointments completeObj = new Appointments();
		completeObj = appointmentRepository.findBySno(request.getSno());
		completeObj.setStatus("COMPLETED");
		appointmentRepository.save(completeObj);
		return completeObj;
	}

	public Optional<InstructorRegistration> getInstructorRegister(String userEmail) {
		return instructorRepository.findById(userEmail);
	}

	@Transactional
	public Courses addCourse(MultipartFile file, Courses data) {
		String generatedFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

		try {
			InputStream fileInputStream = file.getInputStream();
			RequestBody requestBody = RequestBody.fromInputStream(fileInputStream, file.getSize());

			PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(BUCKET_NAME)
					.key(FOLDER_NAME + "/" + generatedFileName).build();

			PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);

			data.setPath(
					"https://s3.us-west-2.amazonaws.com/" + BUCKET_NAME + "/" + FOLDER_NAME + "/" + generatedFileName);
			
			courseRepository.save(data);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	public List<Courses> getAllCourses() {
		List<Courses> courses = courseRepository.findAll();
		for (Courses course : courses) {
			String objectKey = course.getPath().replace("https://s3.us-west-2.amazonaws.com/" + BUCKET_NAME + "/", "");
			String presignedUrl = generatePresignedUrl(objectKey);
			course.setPath(presignedUrl);
		}
		return courses;
	}

	private String generatePresignedUrl(String objectKey) {
		try {
			ZonedDateTime expirationTime = ZonedDateTime.now().plusHours(1);

			GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
					.signatureDuration(Duration.ofHours(1))
					.getObjectRequest(GetObjectRequest.builder().bucket(BUCKET_NAME).key(objectKey).build()).build();

			PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
			return presignedGetObjectRequest.url().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Recommendation Algorithm
	public List<Courses> getRecommendedCourses(String userId) {
		UserRegister user = userRepository.findByEmail(userId);

		// To keep a count of courses that match filters
		Map<Courses, Integer> courseMatchCount = new HashMap<>();

		List<Courses> allCourses = courseRepository.findAll();

		for (Courses course : allCourses) {

			// Age filter
			if (user.getAge() >= Integer.parseInt(course.getMinAge())
					&& user.getAge() <= Integer.parseInt(course.getMaxAge())) {
				courseMatchCount.merge(course, 1, Integer::sum);
			}

			// Goals filter
			if (course.getGoals().equalsIgnoreCase(user.getGoals())) {
				courseMatchCount.merge(course, 1, Integer::sum);
			}

			// Frequency in Description filter
			if (course.getDescription().contains(user.getFrequency())) {
				courseMatchCount.merge(course, 1, Integer::sum);
			}

			// Medical Condition (Good For) filter
			if (course.getGoodFor().contains(user.getMedicalCondition())) {
				courseMatchCount.merge(course, 1, Integer::sum);
			}

			// Medical Condition (Bad For) filter (Exclude)
			if (!course.getBadFor().contains(user.getMedicalCondition())) {
				courseMatchCount.merge(course, 1, Integer::sum);
			}
		}

		// Sorting courses by match count in descending order based on number of match
		// and limiting to top 3
		List<Courses> recommendedCourses = courseMatchCount.entrySet().stream()
				.sorted(Map.Entry.<Courses, Integer>comparingByValue().reversed()).limit(3).map(Map.Entry::getKey)
				.collect(Collectors.toList());

		for (Courses course : recommendedCourses) {
			String objectKey = course.getPath().replace("https://s3.us-west-2.amazonaws.com/" + BUCKET_NAME + "/", "");
			String presignedUrl = generatePresignedUrl(objectKey);
			course.setPath(presignedUrl);
		}

		return recommendedCourses;
	}

	public InstructorRecommendation recommend(InstructorRecommendation request) {
		instructorRecommendationRepository.save(request);

		return request;
	}

	public List<Courses> getInstructorRecommendedCourses(String userId) {
		logger.info("1");
		List<InstructorRecommendation> recommendationsObj = instructorRecommendationRepository.findByEmail(userId);
		logger.info("2");

		List<Integer> courseIds = recommendationsObj.stream().map(InstructorRecommendation::getCourseId)
				.collect(Collectors.toList());
		logger.info("3");

		List<Courses> courses = courseRepository.findAllById(courseIds);
		logger.info("4");

		for (Courses course : courses) {
			String objectKey = course.getPath().replace("https://s3.us-west-2.amazonaws.com/" + BUCKET_NAME + "/", "");
			String presignedUrl = generatePresignedUrl(objectKey);
			logger.info("5");

			course.setPath(presignedUrl);
		}
		logger.info("6");

		return courses;
	}

}
