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

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;


@SpringBootApplication
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
        System.out.println(p.getId());
        ProjectSourceFile sf = projectSourceFileController.create(new ProjectSourceFile("hello.c"));
        p = projectController.addSourceFile(sf, p.getId());
        p = projectController.removeSourceFile(sf, p.getId());
//
//        // query project and newly added source file
//        p = projectService.findById(p.getId());
//        Set<ProjectSourceFile> sourceFiles = p.getSourceFiles();
//        log.info("Queried Project # source files: " + p.getSourceFiles().size());
//        log.info("Queried # source files: " + projectSourceFileService.findByProjectId(p.getId()).size());
//        log.info("---------------------------------------");
//
//        // rename project
//        p = projectService.findById(p.getId());
//        p.setName("NEW-PROJECT");
//        projectService.update(p);
//        p = projectService.findById(p.getId());
//        log.info("Project renamed: " + p.getName());
//        log.info("Renamed project # source files: " + projectSourceFileService.findByProject(p).size());
//        log.info("---------------------------------------");
//
//        // rename source file in project
//
//        // delete project and its source file dir
//        projectService.deleteById(p.getId());
//        p.deleteSourceFileDir();
//        log.info("Project deleted.");
//        log.info("# source files: " + projectSourceFileService.findAll().size());
//        log.info("---------------------------------------");
    }
}
