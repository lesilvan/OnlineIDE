import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { SourceFile } from '../source-file';
import { SourceFileService } from '../source-file.service';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css'],
})
export class EditorComponent implements OnInit {
  editorOptions = { theme: 'vs-dark', language: 'javascript' };
  code: string = 'function x() {\nconsole.log("Hello world!");\n}';

  files$: Observable<SourceFile[]>;

  constructor(
    private route: ActivatedRoute,
    private sourceFileService: SourceFileService
  ) {}

  ngOnInit(): void {
    this.files$ = this.route.paramMap.pipe(
      switchMap((params) => {
        return this.sourceFileService.getSourceFiles();
      })
    );
  }

  createNewFile() {}

  shareProject() {}

  saveProject() {}

  compile() {}
}
