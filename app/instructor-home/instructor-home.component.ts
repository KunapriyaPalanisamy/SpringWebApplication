import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';



@Component({
  selector: 'app-instructor-home',
  templateUrl: './instructor-home.component.html',
  styleUrls: ['./instructor-home.component.css']
})
export class InstructorHomeComponent {
  showDropdown = false;
  selectedTab: string = '';
  currentTab: string = '';    
  private baseUrl = 'http://localhost:8080';

  courseForm: FormGroup;
  showCourseForm = false;
  
  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
      this.courseForm = this.fb.group({
          courseName: ['', Validators.required],
          description: ['', [Validators.required, Validators.maxLength(400)]],
          minAge: ['', [Validators.required, Validators.min(0)]],
          maxAge: ['', [Validators.required, Validators.min(0)]],
          goals: ['fitness', Validators.required],
          goodFor: ['', Validators.required],
          badFor: ['']
      });
  }
    ngOnInit(): void {
    console.log(localStorage.getItem('email'));
    this.navigateTo('users'); 
    this.currentTab = 'instructors';
  }

  navigateTo(tab: string) {
    this.selectedTab = tab; 
    this.currentTab = tab;
    if (tab === 'users') {
      this.fetchUsers();
    }
    else if (tab === 'appointments') {
      this.fetchAppointments('ALL'); 
  }else if(tab === 'courses' && this.courses.length === 0) {
    this.fetchCourses();
}
  }
  
  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  goToProfile() {
    this.router.navigate(['/instructor-Profile']);
  }

  logout() {
    const email = localStorage.getItem('email');
    const session = localStorage.getItem('session');
    const headers = {
      'Session': session ? session : ''
    };
    
    this.http.get(`${this.baseUrl}/logout/${email}`, { headers: headers, responseType: 'text' }).subscribe(
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
  selectedUser: any=null;
  users: any[] = [];
  fetchUsers() {
    const email = localStorage.getItem('email');
    const session = localStorage.getItem('session');
    const headers = {
      'Session': session ? session : ''
    };    
    this.http.get<any[]>(`${this.baseUrl}/getUsers/${email}`, { headers: headers }).subscribe(
      (data) => {
        this.users = data;
        console.log("Fetched Users:", this.users);
      },
      (error) => {
        console.error('Error fetching Users:', error);
      }
    );
  }

  showUserDetails(user: any) {
    this.selectedUser = user;
}

closeUserDetails() {
  this.selectedUser = null;
}
appointments: any[] = [];
appointmentFilter: string = 'ALL';

onFilterChange() {
  this.fetchAppointments(this.appointmentFilter);
}

public getStatusClass(status: string): string {
  switch(status) {
      case 'CONFIRMED':
          return 'confirmed';
      case 'CANCELLED':
          return 'cancelled';
      case 'PENDING':
          return 'pending';
      case 'COMPLETED':
          return 'completed';
      default:
          return '';
  }
}
shouldShowCancel(status: string): boolean {
  return ['CONFIRMED', 'PENDING'].includes(status);
}
shouldShowConfirm(status: string): boolean {
  return ['PENDING'].includes(status);
}
shouldShowComplete(status: string): boolean {
  return ['CONFIRMED'].includes(status);
}

cancelAppointment(appointment: any) {
  const userEmail = localStorage.getItem('email');
  const sessionValue = localStorage.getItem('session');
  const headers = new HttpHeaders({
    'Session': sessionValue ? sessionValue : ''
  });

  const body = {
    userEmail: userEmail,
    sno: appointment.sno  
  };

  this.http.post('http://localhost:8080/cancelAppointment', body, { headers: headers }).subscribe(
    (response) => {
      console.log('Appointment successfully cancelled:', response);
      this.navigateTo(this.currentTab);
    },
    (error) => {
      console.error('Error cancelling the appointment:', error);
    }
  );
}

confirmAppointment(appointment: any) {
  const userEmail = localStorage.getItem('email');
  const sessionValue = localStorage.getItem('session');
  const headers = new HttpHeaders({
    'Session': sessionValue ? sessionValue : ''
  });

  const body = {
    userEmail: userEmail,
    sno: appointment.sno  
  };

  this.http.post('http://localhost:8080/confirmAppointment', body, { headers: headers }).subscribe(
    (response) => {
      console.log('Appointment successfully confirmed:', response);
      this.navigateTo(this.currentTab);
    },
    (error) => {
      console.error('Error confirming the appointment:', error);
    }
  );
}

completeAppointment(appointment: any) {
  const userEmail = localStorage.getItem('email');
  const sessionValue = localStorage.getItem('session');
  const headers = new HttpHeaders({
    'Session': sessionValue ? sessionValue : ''
  });

  const body = {
    userEmail: userEmail,
    sno: appointment.sno  
  };

  this.http.post('http://localhost:8080/markCompleted', body, { headers: headers }).subscribe(
    (response) => {
      console.log('Appointment successfully completed:', response);
      this.navigateTo(this.currentTab);
    },
    (error) => {
      console.error('Error completing the appointment:', error);
    }
  );
}
  fetchAppointments(filter: string){
    const userEmail = localStorage.getItem('email');
    const sessionValue = localStorage.getItem('session');
    const headers = new HttpHeaders({
        'Session': sessionValue ? sessionValue : ''
    });

    if (filter === 'ALL') {
      this.http.get<any[]>(`http://localhost:8080/getInstructorappointments/${userEmail}`, { headers: headers }).subscribe(
        (data) => {
          this.appointments = data;
        },
        (error) => {
          console.error('Error:', error);
        }
      );
    } else if (filter === 'ACTIVE') {
      this.http.get<any[]>(`http://localhost:8080/getInstructorActiveappointments/${userEmail}`, { headers: headers }).subscribe(
        (data) => {
          this.appointments = data;
        },
        (error) => {
          console.error('Error:', error);
        }
      );
        }

  }

  courses: any[] = [];

  fetchCourses() {
    const email = localStorage.getItem('email');
    const session = localStorage.getItem('session');
    const headers = {
      'Session': session ? session : ''
    };  
    this.http.get<any[]>(`${this.baseUrl}/getAllCourses/${email}`, { headers: headers }).subscribe(
      (data) => {
                this.courses = data;
    }, error => {
        console.error('Error fetching courses:', error);
    });
}

selectedFile: File | null = null;

onFileSelected(event: Event) {
  const eventCasted = event as InputEvent;
  const fileInput = eventCasted.target as HTMLInputElement;
  this.selectedFile = fileInput.files ? fileInput.files[0] : null;
}

successMessage: string = '';

onSubmit() {
  const email = localStorage.getItem('email');
  const session = localStorage.getItem('session');
  const headers = new HttpHeaders({
      'Session': session ? session : ''
  });

  const formData = new FormData();
  if (this.selectedFile) {
    formData.append('file', this.selectedFile, this.selectedFile.name);
  }
  formData.append('data', JSON.stringify({ ...this.courseForm.value, email }));

  this.http.post('http://localhost:8080/addCourse', formData, { headers: headers }).subscribe(
      (response) => {
          console.log('Course successfully uploaded:', response);
          this.showCourseForm = false;
          // Show success message for 3 seconds
          setTimeout(() => {
              this.successMessage = '';
          }, 3000);
          this.successMessage = 'Upload successful, now you can view the course in course tab';
      },
      (error) => {
          console.error('Error uploading the course:', error);
      }
  );
}

toggleCourseForm() {
  this.showCourseForm = !this.showCourseForm;
}

recommendCourse(course: any) {
  this.selectedCourse = course;
  const email = localStorage.getItem('email');
  const session = localStorage.getItem('session');
  const headers = {
      'Session': session ? session : ''
  };

  this.http.get<any[]>(`${this.baseUrl}/getUsers/${email}`, { headers: headers }).subscribe(
      (data) => {
          this.users = data;
          console.log("Fetched Users:", this.users);
          // Show the user selection card
          this.showUserSelection = true;
      },
      (error) => {
          console.error('Error fetching Users:', error);
      }
  );
}

showUserSelection = false;
selectedCourse: any = null;
recommendToUser(user: any) {
  const instructorEmail = localStorage.getItem('email');
  const session = localStorage.getItem('session');

  const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Session': session ? session : ''
  });

  const body = {
      email: user.email,
      courseId: this.selectedCourse.sno,
      recommendedBy: instructorEmail
  };

  this.http.post(`${this.baseUrl}/instructorRecommendation`, body, { headers: headers }).subscribe(
      (response) => {
          console.log('Successfully recommended:', response);
          this.showUserSelection = false;
          alert('Successfully recommended');
      },
      (error) => {
          console.error('Error recommending the course:', error);
      }
  );
}
closeOverlay() {
  this.showUserSelection = false;
}

}
