import { Injectable } from '@angular/core';
import {Project} from "./project";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";


@Injectable({
  providedIn: 'root'
})
export class ProjectListService {
  private projectsUrl: string;
  private projectCreationUrl: string;
  private projectRenameUrl: string;
  private projectDeleteUrl: string;
  private projectUrl: string


    constructor(private http: HttpClient) {
    this.projectCreationUrl = '/projects/create';
    this.projectsUrl = '/projects/';
  }

  /** GET project list from the server */
  getProjectList(): Observable<Project[]> {
    return this.http.get<Project[]>(
      this.projectsUrl,
      {observe:'body', responseType:'json'}
      );
  }

  /** POST new project to server */
  addProject(project: Project): Observable<Project> {
    return this.http.post<Project>(
      this.projectCreationUrl,
      project,
      {}
      );
  }

  /** POST rename project */
  renameProject(project: Project): Observable<Project> {
    console.log(project.id);
    this.projectRenameUrl = this.projectsUrl + String(project.id) + '/rename';
    console.log(this.projectRenameUrl);
    return this.http.post<Project>(
      this.projectRenameUrl,
      project.name,
      {}
      );
  }

  /** DELETE delete project */
  deleteProject(project: Project): Observable<boolean>{
    this.projectDeleteUrl = this.projectsUrl + String(project.id) + '/delete';
    console.log(this.projectDeleteUrl);
    return this.http.delete<boolean>(
      this.projectDeleteUrl,
      {}
    );
  }

  /** GET project information by id */
  getProject(id: number): Observable<Project>{
    this.projectUrl = this.projectsUrl + String(id);
    console.log(this.projectUrl);
    return this.http.get<Project>(
      this.projectUrl,
      {observe:'body', responseType:'json'}
    );
  }
}
