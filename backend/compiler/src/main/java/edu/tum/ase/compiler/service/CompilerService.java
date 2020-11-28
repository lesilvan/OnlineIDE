package edu.tum.ase.compiler.service;

import edu.tum.ase.compiler.model.SourceCode;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

@Service
public class CompilerService {
    public SourceCode compile(SourceCode sourceCode) throws IOException {
        return switch (sourceCode.getFileEnding()) {
            case "java" -> compileJavaCode(sourceCode);
            case "c" -> compileCCode(sourceCode);
            default -> throw new IllegalArgumentException("Programming language not supported.");
        };
    }

    private SourceCode compileJavaCode(SourceCode sourceCode) throws IOException {
        File sourceFile = createSourceFile(sourceCode);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        // get units to compile
        Iterable<? extends JavaFileObject> units;
        units = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(sourceFile));

        // run compilation
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, units);
        boolean compilationSuccessful = task.call();


        if (compilationSuccessful) {
            sourceCode.setCompilable(true);
        } else {
            // if compilation failed, extract error message
            sourceCode.setCompilable(false);

            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                String message = "%s%n %s: %s%n".formatted(diagnostic.getSource(), diagnostic.getCode(), diagnostic.getMessage(null));
                sourceCode.setStderr(sourceCode.getStderr() + message);
            }
        }

        fileManager.close();
        cleanUpFiles(sourceFile);

        return sourceCode;
    }

    private SourceCode compileCCode(SourceCode sourceCode) throws IOException {
        return sourceCode;
    }

    private File createSourceFile(SourceCode sourceCode) throws IOException {
        File sourceFile = new File("tmp/%s".formatted(sourceCode.getFileName()));

        // create temporary folder to store source file and compilation results
        if (!sourceFile.getParentFile().exists()) {
            sourceFile.getParentFile().mkdirs();
        }

        // write code contained in sourceCode to a file
        Writer writer = new FileWriter(sourceFile);
        writer.write(sourceCode.getCode());
        writer.close();

        return sourceFile;
    }

    private void cleanUpFiles(File sourceFile) throws IOException {
        FileUtils.deleteDirectory(sourceFile.getParentFile());
    }
}