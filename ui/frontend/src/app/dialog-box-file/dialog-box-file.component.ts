import { Component, Inject, Optional } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {Project} from "../project";
import {NONE_TYPE} from "@angular/compiler";
import {SourceFile} from "../source-file";

export interface DialogDataFile {
  sourceFile: SourceFile;
  action: string;
}

@Component({
  selector: 'app-dialog-box',
  templateUrl: './dialog-box-file.component.html',
  styleUrls: ['./dialog-box-file.component.css']
})
export class DialogBoxFileComponent {
  action:string;
  sourceFile: SourceFile;

  constructor(
    public dialogRef: MatDialogRef<DialogBoxFileComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogDataFile
  )
  {
    this.sourceFile = dialogData.sourceFile;
    this.action = dialogData.action;
  }

  doAction(){
    this.dialogRef.close({event:this.action, sourceFile:this.sourceFile});
  }

  closeDialog(){
    this.dialogRef.close({event:'Cancel', sourceFile:NONE_TYPE});
  }

}
