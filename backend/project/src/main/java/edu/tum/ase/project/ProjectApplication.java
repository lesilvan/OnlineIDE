package edu.tum.ase.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProjectApplication.class);
    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("DataSource = " + dataSource);
    }
}