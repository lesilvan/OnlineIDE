package edu.tum.ase.compiler.service;

import edu.tum.ase.compiler.CompilerApplication;
import edu.tum.ase.compiler.controller.CompilerController;
import edu.tum.ase.compiler.model.SourceCode;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.tools.*;
import javax.xml.transform.Source;
import java.io.*;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CompilerService {
    Logger logger = LoggerFactory.getLogger(CompilerApplication.class);
    // C compilation constants
    String tmpDir = "./tmp";
    String inDir = tmpDir + "/in";
    String outDir = tmpDir + "/out";

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
        cleanUpSourceFile(sourceFile);

        return sourceCode;
    }

    private SourceCode compileCCode(SourceCode sourceCode) throws IOException {
        // create source file and input and output dirs
        File sourceFile = createSourceFile(sourceCode);
        createInOutDirs(); //TODO: Evaluate if necessary

        // get runtime process for compilation with GCC
        String cmd = buildGCCCommand(sourceFile);
        Process proc = Runtime.getRuntime().exec(cmd);

        // define writers that listen to proc's i/o
        BufferedReader stderrReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        // try catch, could be that proc gets interrupted
        try {
            int exitCode = proc.waitFor();

            // check if compilation was successful
            if (exitCode == 0) {
                sourceCode.setCompilable(true);
            } else {
                sourceCode.setCompilable(false);

                String stderr = stderrReader.lines().collect(Collectors.joining("\n"));
                sourceCode.setStderr(stderr);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stderrReader.close();
        cleanUpSourceFile(sourceFile);

        return sourceCode;
    }

    private String buildGCCCommand(File sourceFile) {
        String outputPath = outDir + "/" + sourceFile.getName().substring(0, sourceFile.getName().length() - 2);
        logger.info("outputPath: "+ outputPath);
        String cmd = String.format("gcc %s -o %s", sourceFile.getAbsolutePath(), outputPath);
        logger.info("cmd command: " + cmd);
        return cmd;
    }

    private void createInOutDirs() {
        File inputDir = new File(inDir);
        File outputDir = new File(outDir);

        if (!inputDir.exists()) {
            inputDir.mkdirs();
        }
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
    }

    private File createSourceFile(SourceCode sourceCode) throws IOException {
        File sourceFile = new File("%s/%s".formatted(inDir, sourceCode.getFileName()));

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

    private void cleanUpSourceFile(File sourceFile) throws IOException {
        FileUtils.deleteDirectory(sourceFile.getParentFile());
    }
}