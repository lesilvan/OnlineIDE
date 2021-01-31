package edu.tum.ase.project;

import edu.tum.ase.project.controller.ProjectController;
import edu.tum.ase.project.controller.ProjectSourceFileController;
import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.ProjectSourceFile;
import edu.tum.ase.project.service.ProjectService;
import edu.tum.ase.project.service.ProjectSourceFileService;
import org.apache.tomcat.jni.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;


@SpringBootApplication
@EnableEurekaClient
public class ProjectSourceFileApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ProjectApplication.class);

    @Autowired
    DataSource dataSource;

    @Autowired
    private ProjectSourceFileController projectSourceFileController;

    @Autowired
    private ProjectController projectController;

    public static void main(String[] args) {
        SpringApplication.run(ProjectSourceFileApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // TODO: only runs tests, to be removed
        log.debug("DataSource: " + dataSource);

        // create new project and set its source file dir
        Project p = projectController.create(new Project("new-project", Collections.emptySet()));
        p = projectController.read(p.getId());
        log.info("Queried Project: '" + p.getId() + "'");
        log.info("---------------------------------------");

        // add source file to project
        ProjectSourceFile sf = projectSourceFileController.create(new ProjectSourceFile("hello.c"));
        p = projectController.addSourceFile(sf, p.getId());
        Set<ProjectSourceFile> sfs = p.getSourceFiles();

        for(ProjectSourceFile sourceFile : sfs) {
            projectSourceFileController.updateSourceCode("#include <stdio.h>\n\tint main() { printf(\"Hello World!\"); }", sf.getId());
            projectSourceFileController.rename("new.c", sf.getId());
            String sourceCode = projectSourceFileController.getSourceCode(sf.getId());
        }
    }
}
