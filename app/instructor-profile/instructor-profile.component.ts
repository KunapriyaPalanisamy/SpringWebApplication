import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
@Component({
  selector: 'app-instructor-profile',
  templateUrl: './instructor-profile.component.html',
  styleUrls: ['./instructor-profile.component.css']
})
export class InstructorProfileComponent {

  
  intructorDetails: any;

  constructor(private http: HttpClient,private router: Router) { }

  ngOnInit(): void {
    const userEmail = localStorage.getItem('email');
    const sessionValue = localStorage.getItem('session');

    // Ensure that sessionValue exists, else assign a default value
    const headers = new HttpHeaders({
        'Session': sessionValue ? sessionValue : 'default-session-value'
    });

    this.http.get(`http://localhost:8080/getInstructorProfileDetails/${userEmail}`, {
      headers: headers
    }).subscribe(data => {
      this.intructorDetails = data;
    },
    error => {
      console.error("Error fetching profile details:", error);
    });
  }

  logout() {
    const email = localStorage.getItem('email');
    const session = localStorage.getItem('session');
    const headers = {
      'Session': session ? session : ''
    };
    
    this.http.get(`http://localhost:8080/logout/${email}`, { headers: headers, responseType: 'text' }).subscribe(
      (response) => {
        console.log('Logged out successfully:', response);
        localStorage.removeItem('email');  
        localStorage.removeItem('session'); 
        this.router.navigate(['/login']);   
      },
      (error) => {
        console.error('Error during logout:', error);
      }
    );
  }
}
