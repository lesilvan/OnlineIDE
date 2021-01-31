import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SourceFile } from './source-file';

@Injectable({
  providedIn: 'root',
})
export class SourceFileService {
  private sourceFilesUrl: string;

  constructor(private http: HttpClient) {
    this.sourceFilesUrl = environment.apiUrl + '/project/sourcefiles/';
  }

  /** POST create sourceFile */
  createSourceFile(sourceFile: SourceFile): Observable<SourceFile> {
    return this.http.post<SourceFile>(
      this.sourceFilesUrl + '/create',
      sourceFile
    );
  }

  /** GET load sourceFile */
  loadSourceFile(id: number): Observable<SourceFile> {
    return this.http.get<SourceFile>(this.sourceFilesUrl + String(id), {});
  }

  /** POST rename sourceFile */
  updateSourceFile(sourceFile: SourceFile): Observable<SourceFile> {
    return this.http.post<SourceFile>(
      this.sourceFilesUrl + String(sourceFile.id) + '/rename',
      sourceFile.name
    );
  }

  /** GET get sourcecode */
  getSourceCode(sourceFile: SourceFile) {
    return this.http.get(
      this.sourceFilesUrl + String(sourceFile.id) + '/sourcecode',
      { responseType: 'text' }
    );
  }

  /** POST update sourcecode */
  saveSourceCode(
    sourceFile: SourceFile,
    sourceCode: string
  ): Observable<SourceFile> {
    return this.http.post<SourceFile>(
      this.sourceFilesUrl + String(sourceFile.id) + '/update-sourcecode',
      sourceCode,
      { responseType: 'json' }
    );
  }
}
