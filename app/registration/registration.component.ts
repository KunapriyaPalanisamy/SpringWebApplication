import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidatorFn } from '@angular/forms';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  isUserRegistration = true;
  otpSent = false;
  registrationForm!: FormGroup;
  registrationSuccess = false;
  
  user = {
    email: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    age: 18, 
    gender: 'Male', 
    frequency: 'daily',
    goals: 'fitness',
    medicalCondition: '',
    otp: ''
  };
  

  ages = Array(100).fill(0).map((x,i)=>i+1); 
  
  constructor(private http: HttpClient, private router: Router, private fb: FormBuilder) {}
  errorMessage: string = '';


  ngOnInit(): void {
    this.registrationForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.pattern(/^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[A-Z]).{8,}$/),
        ],
      ],
      confirmPassword: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      age: [18, Validators.required],
      gender: ['Male', Validators.required],
      frequency: ['daily', Validators.required],
      goals: ['fitness', Validators.required],
      medicalCondition: [''],
      otp: ['']
    }, {
      validator: this.passwordMatchValidator
    });
    this.instructorRegistrationForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.pattern(/^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[A-Z]).{8,}$/),
        ],
      ],
      confirmPassword: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      age: [18, Validators.required],
      gender: ['Male', Validators.required],
      experience: ['', Validators.required],
      specialization: ['', Validators.required],
      about: ['', Validators.required],
      otp: ['']
  }, {
      validator: this.passwordMatchValidator
  });
  }

 

  passwordMatchValidator(formGroup: FormGroup) {
    const passwordControl = formGroup.get('password');
    const confirmPasswordControl = formGroup.get('confirmPassword');
    
    if (!passwordControl || !confirmPasswordControl) {
        return { 'mismatch': true };
    }

    return passwordControl.value === confirmPasswordControl.value ? null : { 'mismatch': true };
}



  toggleRegistration() {
    const container = document.querySelector('.container');
    if (container) {
        container.classList.toggle('clicked');
        this.isUserRegistration = !this.isUserRegistration;
    }
}



generateOtp() {
  if (this.registrationForm.hasError('mismatch')) {
    alert("Passwords don't match!");
    return;
  }
  this.http.post('http://localhost:8080/otp', { email: this.registrationForm.get('email')?.value }).subscribe(response => {
    this.otpSent = true;
  }, error => {
    console.error(error);
    this.errorMessage = error.error.message || 'Error during registration!';

  });
}
navigateToLogin(): void {
  this.router.navigate(['/login']);
}



verifyOtp() {
  const email = this.registrationForm.get('email')?.value;
  const otp = this.registrationForm.get('otp')?.value;
  this.http.post('http://localhost:8080/verifyOtp', { email: email, otp: otp }).subscribe(response => {
    
    this.user.email = this.registrationForm.get('email')?.value;
    this.user.password = this.registrationForm.get('password')?.value;
    this.user.firstName = this.registrationForm.get('firstName')?.value;
    this.user.lastName = this.registrationForm.get('lastName')?.value;
    this.user.age = this.registrationForm.get('age')?.value;
    this.user.gender = this.registrationForm.get('gender')?.value;
    this.user.frequency = this.registrationForm.get('frequency')?.value;
    this.user.goals = this.registrationForm.get('goals')?.value;
    this.user.medicalCondition = this.registrationForm.get('medicalCondition')?.value;
    this.registerUser();
    

  }, error => {
    console.error(error);
    this.errorMessage = error.error.message || 'Error during registration!';

  });
}



  registerUser() {
    const payload = {
      email: this.user.email,
      password: this.user.password,
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      age: this.user.age,
      gender: this.user.gender,
      frequency: this.user.frequency,
      goals: this.user.goals,
      medicalCondition: this.user.medicalCondition
    };
    this.http.post('http://localhost:8080/registerUser', payload).subscribe(response => {
      this.registrationSuccess = true;
      setTimeout(() => {
        this.registrationSuccess = false;
        this.router.navigate(['/login']);
    }, 3000);      
    }, error => {
      console.error(error);
      this.errorMessage = error.error.message || 'Error during registration!';
  
    });
}
instructorRegistrationForm!: FormGroup;
instructorRegistrationSuccess = false;

instructor = {
  email: '',
  password: '',
  confirmPassword: '',
  firstName: '',
  lastName: '',
  age: 18, 
  gender: 'Male', 
  experience: '', 
  specialization: '', 
  about: '',
  otp: ''
};
instructorOtpSent = false;


generateInstructorOtp() {
  if (this.instructorRegistrationForm.hasError('mismatch')) {
      alert("Passwords don't match!");
      return;
  }
  this.http.post('http://localhost:8080/otp', { email: this.instructorRegistrationForm.get('email')?.value }).subscribe(response => {
      this.instructorOtpSent = true;
  }, error => {
      console.error(error);
      this.errorMessage = error.error.message || 'Error during registration!';

  });
}

verifyInstructorOtp() {
  const email = this.instructorRegistrationForm.get('email')?.value;
  const otp = this.instructorRegistrationForm.get('otp')?.value;
  this.http.post('http://localhost:8080/verifyOtp', { email: email, otp: otp }).subscribe(response => {
      this.instructor.email = this.instructorRegistrationForm.get('email')?.value;
      this.instructor.password = this.instructorRegistrationForm.get('password')?.value;
      this.instructor.firstName = this.instructorRegistrationForm.get('firstName')?.value;
      this.instructor.lastName = this.instructorRegistrationForm.get('lastName')?.value;
      this.instructor.age = this.instructorRegistrationForm.get('age')?.value;
      this.instructor.gender = this.instructorRegistrationForm.get('gender')?.value;
      this.instructor.experience = this.instructorRegistrationForm.get('experience')?.value;
      this.instructor.specialization = this.instructorRegistrationForm.get('specialization')?.value;
      this.instructor.about = this.instructorRegistrationForm.get('about')?.value;
            this.registerInstructor();
  }, error => {
      console.error(error);
      this.errorMessage = error.error.message || 'Error during registration!';

  });
}

registerInstructor() {
  const payload = {
      email: this.instructor.email,
      password: this.instructor.password,
      firstName: this.instructor.firstName,
      lastName: this.instructor.lastName,
      age: this.instructor.age, 
      gender: this.instructor.gender, 
      experience: this.instructor.experience, 
      specialization: this.instructor.specialization, 
      about: this.instructor.about,
    };
  this.http.post('http://localhost:8080/registerInstructor', payload).subscribe(response => {
      this.instructorRegistrationSuccess = true;
      setTimeout(() => {
          this.instructorRegistrationSuccess = false;
          this.router.navigate(['/login']);
      }, 3000);      
  }, error => {
    console.error(error);
    this.errorMessage = error.error.message || 'Error during registration!';

});
}
}
