import { Component, Inject} from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {NONE_TYPE} from "@angular/compiler";

export interface DialogData {
  action: string;
  title: string;
  fieldInput: string;
  fieldTitle: string;
}

@Component({
  selector: 'app-dialog-box',
  templateUrl: './dialog-box.component.html',
  styleUrls: ['./dialog-box.component.css']
})
export class DialogBoxComponent {
  boxData: DialogData;

  constructor(
    public dialogRef: MatDialogRef<DialogBoxComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  )
  {
    this.boxData = dialogData;
  }

  doAction(){
    this.dialogRef.close({event:this.boxData.action, input:this.boxData.fieldInput});
  }

  closeDialog(){
    this.dialogRef.close({event:'Cancel', input:NONE_TYPE});
  }

}
