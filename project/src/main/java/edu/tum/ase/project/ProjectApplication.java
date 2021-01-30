package edu.tum.ase.project;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ProjectApplication.class);

    @Autowired
    DataSource dataSource;

    @Autowired
    private ProjectService projectService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Override
    public void run(String... args) {
    }
}
