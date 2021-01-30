package edu.tum.ase.compiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompilerApplication {
    private static final Logger logger = LoggerFactory.getLogger(CompilerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CompilerApplication.class, args);
    }

}
