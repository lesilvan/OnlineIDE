import { Injectable } from '@angular/core';
import {ReplaySubject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

const LOGIN_PATH = '/login';
const LOGOUT_PATH = '/logout';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authenticated$: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
  private notAuthenticated$: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);

  public get authenticated(): ReplaySubject<boolean> {
    return this.authenticated$;
  }

  public get notAuthenticated(): ReplaySubject<boolean> {
    return this.notAuthenticated$;
  }

  constructor(private httpClient: HttpClient,
              private router: Router) {
    this.checkAuthentication();
  }

  public checkAuthentication(): void {
    this.httpClient.get<boolean>('/authenticated')
      .subscribe((authenticated) => {
        this.authenticated$.next(authenticated);
        this.notAuthenticated$.next(!authenticated);
      }, (err) => {
        this.authenticated$.next(false);
        this.notAuthenticated$.next(true);
      });
  }

  public login(): void {
    window.location.href = LOGIN_PATH;
  }

  public logout(): void {
    this.httpClient.post(LOGOUT_PATH, {})
      .subscribe(() => {
        this.router.navigateByUrl('/home');
        this.checkAuthentication();
      });
  }
}
