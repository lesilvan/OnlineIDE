import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SourceFile } from '../source-file';
import { SourceFileService } from '../source-file.service';
import {ProjectListService} from "../project-list.service";
import {Project} from "../project";
import {DialogBoxFileComponent} from "../dialog-box-file/dialog-box-file.component";
import {MatDialog} from "@angular/material/dialog";
import {CompilerService} from "../compiler.service";
import {SourceCode} from "../source-code";
import {timer, Subscription} from "rxjs";
import {DarkModeService} from "../dark-mode.service";

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css'],
})
export class EditorComponent implements OnInit {
  editorOptions = { theme: 'vs'};
  project_id: number;
  project: Project = new Project();

  visibilityEditor: string;
  loadedSourceFile: SourceFile = new(SourceFile);
  compilingDisabled: boolean = true;
  savingDisabled: boolean = true;
  sourceCode: SourceCode = new(SourceCode);
  displayedCompilerMessage: string;
  private timerSubscription: Subscription;

  constructor(
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private sourceFileService: SourceFileService,
    private projectListService: ProjectListService,
    private compilerService: CompilerService,
    private darkModeService: DarkModeService
  ) {
    this.project.sourceFiles = [];
    this.loadedSourceFile.id = 0;
  }

  ngOnInit(): void {
    // no file is loaded -> make editor invisible
    this.visibilityEditor = 'hidden'; // 'visible'
    // Get the current project_id from the routing
    this.route.params.subscribe(params => {
      this.project_id = params['id'];
    })
    // Get interval timer to fetch dark mode button
    this.timerSubscription = timer(0, 3000).subscribe(
      (value) => {
        console.log(value, new Date());
        this.darkModeService.getDarkModeStatus().subscribe(
          (darkModeEnabled) => {
            if (darkModeEnabled) {
              monaco.editor.setTheme("vs-dark");
            } else {
              monaco.editor.setTheme("vs");
            }
            console.log(darkModeEnabled);
          }
        );
      }
    );
    // Get project information
    this.getProject(this.project_id);
  }

  ngOnDestroy() {
    this.timerSubscription.unsubscribe();
  }

  /* Ignitable methods from user perspective */
  createNewFile() {
    this.openFileDialog(new SourceFile(), "Create new");
  }

  renameFile(id: number){
    // Load current version of sourceFile
    this.sourceFileService.loadSourceFile(id)
      .subscribe((sourceFile) => {
        // Try to rename it
        this.openFileDialog(sourceFile, "Rename");
      });
  }

  deleteFile(id: number){
    // Load current version of sourceFile (check if still existent)
    this.sourceFileService.loadSourceFile(id)
      .subscribe((sourceFile) => {
        // Try to delete it
        this.openFileDialog(sourceFile, "Delete");
      });
  }

  shareProject() {}

  saveFile() {
    this.sourceFileService.saveSourceCode(
      this.loadedSourceFile,
      this.sourceCode.code).subscribe((sourceFile) => {
        this.compilingDisabled = false;
      }
    )
  }

  compile() {
    this.compilerService.compile(
      this.loadedSourceFile,
      this.sourceCode.code
    ).subscribe((result) => {
      if (result.compilable == true) {
        this.displayedCompilerMessage = "Successfully compiled...";
      }
      else {
        this.displayedCompilerMessage = result.stderr;
        // TODO: Improve display of error message
      }
      this.sourceCode.stderr = result.stderr;
      this.sourceCode.stdout = result.stdout;
      this.sourceCode.compilable = result.compilable;
      console.log(result);
    });
  }

  openSourceFile(id: number) {
    console.log("Loading content of sourceFile with id", id, "in editor started");
    this.visibilityEditor = 'visible';
    // Try to load latest version of file
    this.sourceFileService.loadSourceFile(id)
      .subscribe((sourceFile) => {
        // Save initially loaded version of sourceFile
        this.loadedSourceFile = sourceFile;
        // Try to get sourceCode
        this.getSourceCode(sourceFile);
      });
  }


  /* Dialog Handler (as mediator between user wish and ignition of intended methodology */
  openFileDialog(sourceFile: SourceFile, action: string): any {
    const dialogRef = this.dialog.open(DialogBoxFileComponent, {
      width: '250px',
      data: { action: action, sourceFile: sourceFile},
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result.event == 'Create new') {
        this.createSourceFile(result.sourceFile);
      } else if (result.event == 'Rename') {
        this.renameSourceFile(result.sourceFile);
      } else if (result.event == 'Delete') {
        this.deleteSourceFile(result.sourceFile);
      }});
  }

  /** Function comparing sourceFiles for consistent sorting */
  compareSourceFiles(file1: SourceFile, file2: SourceFile) {
    if (file1.id < file2.id) {return -1;}
    if (file1.id > file2.id) {return 1;}
    return 0;
  }


  /* Functions for database interaction */
  // Project database
  private getProject(id: number): void {
    this.projectListService.getProject(id).subscribe((project) => {
      this.project = project;
    });
  }

  private addSourceFileToProject(sourceFile: SourceFile): void {
    this.projectListService.addSourceFile(this.project, sourceFile)
      .subscribe((project) => {
        this.project = project;
      });
  }

  private renameSourceFile(sourceFile: SourceFile) {
    this.sourceFileService.updateSourceFile(sourceFile)
      .subscribe((sourceFile) => {
        // Update loadedSourceFile Metadata (i.e. for potential compiling)
        if (this.loadedSourceFile.id == sourceFile.id) {
          this.loadedSourceFile = sourceFile;
        }
        // Update project to display latest information
        this.getProject(this.project_id);
      });
  }

  private deleteSourceFile(sourceFile) {
    this.projectListService.deleteSourceFile(this.project, sourceFile)
      .subscribe((project) => {
        // Make editor invisible if deleted file was the working file
        if (this.loadedSourceFile.id == sourceFile.id) {
          this.visibilityEditor = 'hidden';
          this.savingDisabled = true;
          this.compilingDisabled = true;
          this.displayedCompilerMessage = "";
        }
        // Update Project
        this.getProject(this.project_id);
      });
  }

  // SourceFile Database
  private createSourceFile(sourceFile: SourceFile): void {
    // Create source file (in sourceFile database)
    this.sourceFileService.createSourceFile(sourceFile)
      .subscribe((sourceFile) => {
        // Add sourceFile to project (in project DB)
        this.addSourceFileToProject(sourceFile);
      });
  }

  private getSourceCode(sourceFile: SourceFile): void {
    this.sourceFileService.getSourceCode(sourceFile)
      .subscribe((code) => {
        this.sourceCode.code = code;
        this.sourceCode.fileName = sourceFile.name;
        // Select correct language highlighting
        this.setLanguageHighlighting(sourceFile);
        this.savingDisabled = false;
      });
  }

  sourceCodeChanged() {
    //Disable Compile button
    this.compilingDisabled = true;
  }

  getFileExtension(filename: string): string {
    return filename.substring(filename.lastIndexOf('.')+1, filename.length) || filename;
  }

  private setLanguageHighlighting(sourceFile: SourceFile) {
    var fileExtension = this.getFileExtension(sourceFile.name);
    if (fileExtension == "c") {
      monaco.editor.setModelLanguage(window.monaco.editor.getModels()[0],"c");
    } else if (fileExtension == "java") {
      monaco.editor.setModelLanguage(window.monaco.editor.getModels()[0],"java");
    }
  }
}
