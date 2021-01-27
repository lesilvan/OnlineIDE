import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SourceFile } from './source-file';

@Injectable({
  providedIn: 'root',
})
export class SourceFileService {
  private sourceFilesUrl: string;
  private createSourceFileUrl: string;

  constructor(private http: HttpClient) {
    this.sourceFilesUrl = '/sourcefiles/';
    this.createSourceFileUrl = 'create/';
  }

  getSourceFiles(): Observable<SourceFile[]> {
    return this.http.get<SourceFile[]>(this.sourceFilesUrl);
  }

  createSourceFile(file: SourceFile): Observable<SourceFile> {
    return this.http.post<SourceFile>(
      this.sourceFilesUrl + this.createSourceFileUrl,
      file
    );
  }
}
