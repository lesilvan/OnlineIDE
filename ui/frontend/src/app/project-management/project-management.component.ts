import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import {DialogBoxComponent, DialogData} from '../dialog-box/dialog-box.component';
import { Project } from '../project';
import { ProjectListService } from '../project-list.service';
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-project-management',
  templateUrl: './project-management.component.html',
  styleUrls: ['./project-management.component.css'],
})
export class ProjectManagementComponent implements OnInit {
  newProjectName: any;
  projects: Project[];
  displayedColumns: string[] = ['id', 'name', 'action'];
  dataSource: MatTableDataSource<Project>;

  constructor(
    public dialog: MatDialog,
    private projectListService: ProjectListService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.getProjectList();
    this.dataSource = new MatTableDataSource<Project>(this.projects);
  }

  openDialog(project: Project, dialogData: DialogData): any {
    const dialogRef = this.dialog.open(DialogBoxComponent, {width:'250px', data:dialogData});
    dialogRef.afterClosed().subscribe((result) => {
      if (result.event == 'Create') {
        project.name = result.input;
        console.log("Updated Project:", project);
        this.addProjectData(project);
      } else if (result.event == 'Rename') {
        project.name = result.input;
        console.log("Updated Project:", project);
        this.updateProjectData(project);
      } else if (result.event == 'Delete') {
        this.deleteProjectData(project);
      }
    });
  }
  // Functions initializing dialog correctly
  addProject(): void {
    let dialogData: DialogData = {
      title: "Create new project",
      fieldTitle: "Project name",
      fieldInput: "",
      action: "Create"
    }
    this.openDialog(new Project(), dialogData);
  }

  editProject(project: Project): void {
    let dialogData: DialogData = {
      title: "Rename project",
      fieldTitle: "New project name",
      fieldInput: project.name,
      action: "Rename"
    }
    this.openDialog(project, dialogData);
  }

  deleteProject(project: Project): void {
    let dialogData: DialogData = {
      title: "Delete project",
      fieldTitle: "",
      fieldInput: project.name,
      action: "Delete"
    }
    this.openDialog(project, dialogData);
  }

  // Functions taking care about manipulated data (storage)
  addProjectData(project): void {
    this.projectListService
      .addProject(project)
      .subscribe((project: Project) => {
        console.log(project);
        this.getProjectList();
      });
  }

  updateProjectData(project): void {
    /*this.projects.filter((value,key) => {
      if (value.id == project.id) {value.name = project.name;}
    });
    this.dataSource.data = this.projects;*/
    this.projectListService
      .renameProject(project)
      .subscribe((project: Project) => {
        console.log(project);
        this.getProjectList();
      });
  }

  deleteProjectData(project: Project): void {
    this.projects = this.projects.filter((obj) => obj.id !== project.id);
    this.dataSource.data = this.projects;
    this.projectListService.deleteProject(project).subscribe((isDeleted) => {
      console.log(isDeleted);
      this.getProjectList();
    });
  }

  // Functions for database interaction
  getProjectList(): void {
    this.projectListService.getProjectList().subscribe((projects) => {
      this.projects = projects;
      this.dataSource.data = projects;
    });
  }
}
