import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SourceCode } from './source-code';
import { SourceFile } from './source-file';

@Injectable({
  providedIn: 'root',
})
export class CompilerService {
  private readonly compilerUrl: string;

  constructor(private http: HttpClient) {
    this.compilerUrl = environment.apiUrlCompiler + '/compile';
  }

  /** POST compile sourcecode */
  compile(sourceFile: SourceFile, code: string): Observable<SourceCode> {
    // Build sourceCode-File
    let sourceCode = new SourceCode();
    sourceCode.code = code;
    sourceCode.fileName = sourceFile.name;
    console.log('sourceCode:', sourceCode);
    return this.http.post<any>(this.compilerUrl, sourceCode, {
      responseType: 'json',
    });
  }
}
