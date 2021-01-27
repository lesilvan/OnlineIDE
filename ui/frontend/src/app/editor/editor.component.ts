import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { SourceFile } from '../source-file';
import { SourceFileService } from '../source-file.service';
import {ProjectListService} from "../project-list.service";
import {Project} from "../project";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css'],
})
export class EditorComponent implements OnInit {
  editorOptions = { theme: 'vs-dark', language: 'javascript' };
  code: string = 'function x() {\nconsole.log("Hello world!");\n}';
  project_id: number;
  project: Project;

  constructor(
    private route: ActivatedRoute,
    private sourceFileService: SourceFileService,
    private projectListService: ProjectListService
  ) {}

  ngOnInit(): void {
    // Gets the current project_id from the routing
    this.route.params.subscribe(params => {
      this.project_id = params['id'];
    })
    // Get project information
    this.getProject(this.project_id);
  }

  createNewFile() {}

  shareProject() {}

  saveProject() {}

  compile() {}

  // Functions for database interaction
  getProject(id: number): void {
    this.projectListService.getProject(id).subscribe((project) => {
      (this.project = project, console.log("Project loaded:", project));
    });
  }

  openSourceFileInEditor(id: number) {

  }
}
