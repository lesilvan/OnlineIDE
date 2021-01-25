import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './home/home.component';
import {MatButtonModule} from '@angular/material/button';
import {Route, RouterModule, Routes} from "@angular/router";
import { ProjectManagementComponent } from './project-management/project-management.component';
import { StartupComponent } from './startup/startup.component';
import {FormsModule} from "@angular/forms";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatGridListModule} from '@angular/material/grid-list';
import {MatDialogModule} from "@angular/material/dialog";
import { DialogBoxComponent } from './dialog-box/dialog-box.component';
import { EditorComponent } from './editor/editor.component';
import { MonacoEditorModule} from "ngx-monaco-editor";
import {HttpClientModule} from "@angular/common/http";

const appRoutes: Routes = [
  { path: 'startup', component: StartupComponent},
  { path: 'home', component: HomeComponent},
  { path: 'manage-projects', component: ProjectManagementComponent},
  { path: 'ide/:id', component: EditorComponent},
  { path: '', redirectTo: '/startup', pathMatch: 'full'}
]
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ProjectManagementComponent,
    StartupComponent,
    DialogBoxComponent,
    EditorComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule,
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: true} // debugging purposes only
    ),
    MonacoEditorModule.forRoot(),
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatDialogModule,
    MatGridListModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
