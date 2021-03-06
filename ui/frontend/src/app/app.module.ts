import { ObserversModule } from '@angular/cdk/observers';
import { HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { MonacoEditorModule } from 'ngx-monaco-editor';
import { AppComponent } from './app.component';
import { AuthGuard } from './auth.guard';
import { DialogBoxComponent } from './dialog-box/dialog-box.component';
import { EditorComponent } from './editor/editor.component';
import { HomeComponent } from './home/home.component';
import { ProjectManagementComponent } from './project-management/project-management.component';

const appRoutes: Routes = [
  { path: 'home', component: HomeComponent },
  {
    path: 'manage-projects',
    component: ProjectManagementComponent,
    canActivate: [AuthGuard],
  },
  { path: 'ide/:id', component: EditorComponent, canActivate: [AuthGuard] },
  {
    path: '',
    redirectTo: '/manage-projects',
    pathMatch: 'full',
  },
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ProjectManagementComponent,
    EditorComponent,
    DialogBoxComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    HttpClientXsrfModule.withOptions({ cookieName: 'XSRF-TOKEN' }),
    BrowserAnimationsModule,
    MatButtonModule,
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // debugging purposes only
    ),
    MonacoEditorModule.forRoot(),
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatDialogModule,
    MatGridListModule,
    MatListModule,
    MatIconModule,
    MatSortModule,
    ObserversModule,
    MatInputModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
