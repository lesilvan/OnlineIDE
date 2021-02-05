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
public class ProjectSourceFileApplication {
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
}
