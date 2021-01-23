import {Component, OnInit} from '@angular/core';
import {Project} from "../project";
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import {DialogBoxComponent} from "../dialog-box/dialog-box.component";

@Component({
  selector: 'app-project-management',
  templateUrl: './project-management.component.html',
  styleUrls: ['./project-management.component.css']
})
export class ProjectManagementComponent implements OnInit {
  newProjectName: any;
  projects: Project[] = [
    {id:1345345345, name: "Great Project"},
    {id:4535345342, name: "Hullapl"}
  ];
  displayedColumns: string[] = ['id', 'name', "action"];
  dataSource = new MatTableDataSource<Project>(this.projects);


  constructor(public dialog: MatDialog) {}

  ngOnInit(): void {
  }

  openDialog(project: Project, action: string): any {
    const dialogRef = this.dialog.open(DialogBoxComponent, {
      width:"250px",
      data: {action: action, project: project}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(result);
      if(result.event == 'Create new'){
        this.addProjectData(result.project);
      }else if(result.event == 'Rename'){
        this.updateProjectData(result.project);
      }else if(result.event == 'Delete'){
        this.deleteProjectData(result.project);
      }
    })
  }
  // Functions initializing the correct dialog
  addProject(): void{
    let project = new Project();
    this.openDialog(project, "Create new");
  }

  editProject(project: Project): void{
    this.openDialog(project, "Rename");
  }

  deleteProject(project: Project): void{
    this.openDialog(project, "Delete");
  }
  // Functions taking care about manipulated data (storage)
  // TODO: Store and Retrieve information from database
  addProjectData(project): void {
    this.projects.push(project);
    this.dataSource.data = this.projects;
  }
  updateProjectData(project): void {
    this.projects.filter((value,key) => {
      if (value.id == project.id) {value.name = project.name;}
    });
    this.dataSource.data = this.projects;
  }
  deleteProjectData(project: Project): void{
    this.projects = this.projects.filter(obj => obj.id !== project.id);
    this.dataSource.data = this.projects;
  }
}
