import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Project } from './project';

@Injectable({
  providedIn: 'root',
})
export class ProjectListService {
  private projectsUrl: string;
  private projectCreationUrl: string;
  private projectRenameUrl: string;
  private projectDeleteUrl: string;

  constructor(private http: HttpClient) {
    this.projectCreationUrl = 'http://localhost:8081/projects/create';
    this.projectsUrl = 'http://localhost:8081/projects/';
  }

  /** GET project list from the server */
  getProjectList(): Observable<Project[]> {
    return this.http.get<Project[]>(this.projectsUrl, {
      observe: 'body',
      responseType: 'json',
    });
  }

  /** POST new project to server */
  addProject(project: Project): Observable<Project> {
    return this.http.post<Project>(this.projectCreationUrl, project, {});
  }

  /** POST rename project */
  renameProject(project: Project): Observable<Project> {
    console.log(project.id);
    this.projectRenameUrl = this.projectsUrl + String(project.id) + '/rename';
    console.log(this.projectRenameUrl);
    return this.http.post<Project>(this.projectRenameUrl, project.name, {});
  }

  /** DELETE delete project */
  deleteProject(project: Project): Observable<boolean> {
    this.projectDeleteUrl = this.projectsUrl + String(project.id) + '/delete';
    console.log(this.projectDeleteUrl);
    return this.http.delete<boolean>(this.projectDeleteUrl, {});
  }
}
