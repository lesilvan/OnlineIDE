import { Component, Inject, Optional } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {Project} from "../project";
import {NONE_TYPE} from "@angular/compiler";

export interface DialogData {
  project: Project;
  action: string;
}

@Component({
  selector: 'app-dialog-box',
  templateUrl: './dialog-box.component.html',
  styleUrls: ['./dialog-box.component.css']
})
export class DialogBoxComponent {
  action:string;
  project: Project;

  constructor(
    public dialogRef: MatDialogRef<DialogBoxComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  )
  {
    this.project = dialogData.project;
    this.action = dialogData.action;
  }

  doAction(){
    this.dialogRef.close({event:this.action, project:this.project});
  }

  closeDialog(){
    this.dialogRef.close({event:'Cancel', project:NONE_TYPE});
      //{event:'Cancel', Project: this.dialogData.project});
  }

}
