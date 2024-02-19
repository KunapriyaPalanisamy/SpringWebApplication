import { Component, OnInit } from '@angular/core'; 
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';


@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.css']
})
export class UserHomeComponent {

  instructors: any[] = [];
  selectedInstructor: any = null;
  
  private baseUrl = 'http://localhost:8080';

  bookingAppointment = false;
  appointmentName = '';
  appointmentEmail = localStorage.getItem('email') || '';  
  appointmentDate = '';
  appointmentTime = '';
  appointmentNotes = '';
  dateError: string = '';
  availableTimes: string[] = [];
  maxDate: string = '';

  

  constructor(private http: HttpClient, private router: Router) {
    for (let i = 9; i <= 18; i++) {
      this.availableTimes.push(`${i}:00`);
    }
  }

  

  ngOnInit(): void {
    console.log(localStorage.getItem('email'));
    this.navigateTo('instructors'); 
    this.currentTab = 'instructors';

    this.maxDate = this.calculateMaxDate(7);

  }

  calculateMaxDate(daysToAdd: number): string {
    const currentDate = new Date();
    let daysAdded = 0;
    while (daysAdded < daysToAdd) {
        currentDate.setDate(currentDate.getDate() + 1);
        if (currentDate.getDay() !== 6 && currentDate.getDay() !== 0) { 
            daysAdded++;
        }
    }
    return currentDate.toISOString().split('T')[0];
}
  showDropdown = false;

  toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }

  goToProfile() {
    this.router.navigate(['/user-Profile']);
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

  courses: any[] = [];
  recommended:any[]=[];

  navigateTo(tab: string) {
    this.currentTab = tab;
    if (tab === 'instructors') {
      this.fetchInstructors();
    }
    else if (tab === 'appointments') {
      this.fetchAppointments('ALL'); 
  }else if(tab === 'courses' && this.courses.length === 0) {
    this.fetchCourses();
}
else if(tab === 'recommended' && this.recommended.length === 0) {
  this.fetchRecommendedCourses();
}

    // ... Other tab navigation logic ...
  }

  fetchInstructors() {
    const email = localStorage.getItem('email');
    const session = localStorage.getItem('session');
    const headers = {
      'Session': session ? session : ''
    };    
    this.http.get<any[]>(`${this.baseUrl}/getInstructors/${email}`, { headers: headers }).subscribe(
      (data) => {
        this.instructors = data;
      },
      (error) => {
        console.error('Error fetching instructors:', error);
      }
    );
  }

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

fetchRecommendedCourses() {
  const email = localStorage.getItem('email');
  const session = localStorage.getItem('session');
  const headers = {
      'Session': session ? session : ''
  };  
  
  this.http.get<any[]>(`${this.baseUrl}/getRecommendedCourses/${email}`, { headers: headers }).subscribe(
      (data) => {
          this.recommended = data;
          this.fetchInstructorRecommendedCourses();
      }, 
      error => {
          console.error('Error fetching courses:', error);
      }
  );
}

instructorRecommended: any[] = [];
fetchInstructorRecommendedCourses() {
  const email = localStorage.getItem('email');
  const session = localStorage.getItem('session');
  const headers = {
      'Session': session ? session : ''
  };
  
  this.http.get<any[]>(`${this.baseUrl}/getInstructorRecommendedCourses/${email}`, { headers: headers }).subscribe(
      (data) => {
          this.instructorRecommended = data;
      }, 
      error => {
          console.error('Error fetching instructor recommended courses:', error);
      }
  );
}


  showInstructorDetails(instructor: any) {
    this.selectedInstructor = instructor;
}

  closeInstructorDetails() {
    this.selectedInstructor = null;
  }

  showBookingAppointment(instructor: any) {
    this.selectedInstructor = instructor; 
    this.bookingAppointment = true;
}

cancelBooking() {
  this.bookingAppointment = false;
  this.dateError = ''; 
}
todaysDate: string = new Date().toISOString().split('T')[0];

validateDate() {
  const today = new Date();
  const selectedDate = new Date(this.appointmentDate + ' ' + this.appointmentTime + ':00'); 

  if (selectedDate.getTime() <= today.getTime()) {
      this.dateError = 'Please select a future date and time.';
      return;
  }
  
  const dayOfWeek = selectedDate.getDay();
  
  if (dayOfWeek === 6 || dayOfWeek === 0) {
      this.dateError = 'Please select any weekday.';
      return;
  }

  if (selectedDate > new Date(this.maxDate)) {
      this.dateError = 'Please select a date within the allowed range.';
  } else {
      this.dateError = '';
  }
}


  
  successMessage: string = '';
  showMessage: boolean = false;
  

  submitForReview() {
    if (!this.dateError && this.appointmentName && this.appointmentDate && this.appointmentTime) {
      const requestBody = {
        userEmail: this.appointmentEmail,
        userName: this.appointmentName,
        instructorEmail: this.selectedInstructor.email, 
        instructorName: `${this.selectedInstructor.firstName}${this.selectedInstructor.lastName ? ' ' + this.selectedInstructor.lastName : ''}`,
        timings: `${this.appointmentDate} ${this.appointmentTime}`,
        userNotes: this.appointmentNotes
      };
  
      const session = localStorage.getItem('session');
      const headers = new HttpHeaders({
        'Session': session ? session : ''
      });
  
      this.http.post(`${this.baseUrl}/fixAppointment`, requestBody, { headers: headers }).subscribe(
        (response) => {
          console.log('Appointment successfully submitted:', response);
          this.successMessage = 'Appointment booked! Instructor will review your booking.';
        this.showMessage = true;
        setTimeout(() => {
          this.showMessage = false;
        }, 3000); 

        this.cancelBooking();
      },
        (error) => {
          console.error('Error submitting appointment:', error);
        }
      );
    }
  }
// appointment section
    currentTab: string = '';    
    appointments: any[] = [];
    appointmentFilter: string = 'ALL';

    onFilterChange() {
      this.fetchAppointments(this.appointmentFilter);
  }
  
  
  fetchAppointments(filter: string) {
    const userEmail = localStorage.getItem('email');
    const sessionValue = localStorage.getItem('session');
    const headers = new HttpHeaders({
        'Session': sessionValue ? sessionValue : ''
    });

    if (filter === 'ALL') {
      this.http.get<any[]>(`http://localhost:8080/getMyappointments/${userEmail}`, { headers: headers }).subscribe(
        (data) => {
          this.appointments = data;
        },
        (error) => {
          console.error('Error:', error);
        }
      );
    } else if (filter === 'ACTIVE') {
      this.http.get<any[]>(`http://localhost:8080/getMyActiveappointments/${userEmail}`, { headers: headers }).subscribe(
        (data) => {
          this.appointments = data;
        },
        (error) => {
          console.error('Error:', error);
        }
      );
        }
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


  
  
}

