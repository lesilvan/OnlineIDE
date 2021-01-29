import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class DarkModeService {
  darkModeUrl: string;

  constructor(private http: HttpClient) {
    this.darkModeUrl = "/dark-mode/";
  }

  /** GET dark mode status */
  getDarkModeStatus(): Observable<boolean>{
    return this.http.get<boolean>(
      this.darkModeUrl,
      {observe:'body', responseType:'json'}
    );
  }
}
