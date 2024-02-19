import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  isUserLogin = true;
  userEmail = '';
  userPassword = '';
  instructorEmail = '';
  instructorPassword = '';
  userLoginForm: any;
  instructorLoginForm: any;
  errorMessage: string = '';


  constructor(private http: HttpClient, private router: Router, private fb: FormBuilder) {}

  ngOnInit() {
    this.userLoginForm = this.fb.group({
      userEmail: ['', [Validators.required, Validators.email]],
      userPassword: ['', Validators.required]
    });

    this.instructorLoginForm = this.fb.group({
      instructorEmail: ['', [Validators.required, Validators.email]],
      instructorPassword: ['', Validators.required]
    });
  }


  login() {
    if (this.userLoginForm.valid) { 
        const endpoint = 'http://localhost:8080/loginUser';  
  
        const loginData = {
            email: this.userLoginForm.get('userEmail').value,
            password: this.userLoginForm.get('userPassword').value
        };
  
        this.http.post(endpoint, loginData).subscribe(
            (response: any) => {
                localStorage.setItem('session', response.session);
                localStorage.setItem('email', response.email);
                
                if (response.role === 'admin') {
                    this.router.navigate(['/instructor-home']);
                } else if (response.role === 'user') {
                    this.router.navigate(['/user-home']);
                } else {
                    console.error('Unknown role received:', response.role);
                    this.errorMessage = "Login failed due to unknown role!";
                }
            },
            (error) => {
                console.error('Error during login:', error);
                this.errorMessage = "Incorrect username or password! try again";
            }
        );
    } else {
        console.log("Form is invalid!");
    }
}


  
}
