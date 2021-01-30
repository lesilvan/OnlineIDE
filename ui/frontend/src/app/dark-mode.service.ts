import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DarkModeService {
  darkModeUrl: string;

  constructor(private http: HttpClient) {
    this.darkModeUrl = environment.apiUrlDarkMode + '/dark-mode/';
  }

  /** GET dark mode status */
  getDarkModeStatus(): Observable<boolean> {
    return this.http.get<boolean>(this.darkModeUrl, {
      observe: 'body',
      responseType: 'json',
    });
  }
}
