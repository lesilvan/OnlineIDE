import { Injectable } from '@angular/core';
import {Project} from "./project";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {SourceFile} from "./source-file";


@Injectable({
  providedIn: 'root'
})
export class ProjectListService {
  private readonly projectsUrl: string;

    constructor(private http: HttpClient) {
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
      this.projectsUrl + 'create',
      project,
      {}
      );
  }

  /** POST rename project */
  renameProject(project: Project): Observable<Project> {
    return this.http.post<Project>(
      this.projectsUrl + String(project.id) + '/rename',
      project.name,
      {}
      );
  }

  /** DELETE delete project */
  deleteProject(project: Project): Observable<boolean>{
    return this.http.delete<boolean>(
      this.projectsUrl + String(project.id) + '/delete',
      {}
    );
  }

  /** GET project information by id */
  getProject(id: number): Observable<Project>{
    return this.http.get<Project>(
      this.projectsUrl + String(id),
      {observe:'body', responseType:'json'}
    );
  }

  /** POST add sourceFile to project */
  addSourceFile(project: Project, sourceFile: SourceFile): Observable<Project> {
    return this.http.post<Project>(
      this.projectsUrl + String(project.id) + '/add-sourcefile',
      sourceFile,
      {responseType: "json"}
    );
  }

  /** POST remove sourceFile from project and entirely */
  deleteSourceFile(project: Project, sourceFile: SourceFile): Observable<Project> {
    console.log("Project where file should be removed:", project);
    console.log("sourceFile to be deleted:", sourceFile);
    console.log("URL:", this.projectsUrl + String(project.id) + '/remove-sourcefile');
    return this.http.post<Project>(
      this.projectsUrl + String(project.id) + '/remove-sourcefile',
      sourceFile,
      {observe: "body",
        responseType: "json"}
    );
  }
}
