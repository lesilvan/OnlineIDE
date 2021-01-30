import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { DialogBoxProjectComponent } from '../dialog-box-project/dialog-box-project.component';
import { Project } from '../project';
import { ProjectListService } from '../project-list.service';

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
    private projectListService: ProjectListService
  ) {}

  ngOnInit(): void {
    this.getProjectList();
    this.dataSource = new MatTableDataSource<Project>(this.projects);
  }

  openDialog(project: Project, action: string): any {
    const dialogRef = this.dialog.open(DialogBoxProjectComponent, {
      width: '250px',
      data: { action: action, project: project },
    });
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result.event == 'Create new') {
        this.addProjectData(result.project);
      } else if (result.event == 'Rename') {
        this.updateProjectData(result.project);
      } else if (result.event == 'Delete') {
        this.deleteProjectData(result.project);
      }
    });
  }
  // Functions initializing dialog correctly
  addProject(): void {
    let project = new Project();
    this.openDialog(project, 'Create new');
  }

  editProject(project: Project): void {
    this.openDialog(project, 'Rename');
  }

  deleteProject(project: Project): void {
    this.openDialog(project, 'Delete');
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
