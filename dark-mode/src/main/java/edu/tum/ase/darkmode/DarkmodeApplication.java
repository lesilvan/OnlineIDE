package edu.tum.ase.darkmode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.LocalDateTime;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class DarkmodeApplication {
    private static final Logger logger = LoggerFactory.getLogger(DarkmodeApplication.class);

    private boolean darkModeEnabled = false;
    private LocalDateTime lastToggled;

    public static void main(String[] args) {
        SpringApplication.run(DarkmodeApplication.class, args);
    }

    @RequestMapping(path = "/dark-mode/toggle", method = RequestMethod.GET)
    public void toggleDarkMode() {
        if (!isToggleOnCoolDown()) {
            darkModeEnabled = !darkModeEnabled;
            lastToggled = LocalDateTime.now();
            logger.debug("Toggled dark mode.");  // TODO JH: logging not working
        } else {
            logger.debug("Dark mode toggle on cool down.");
        }
    }

    @RequestMapping(path = "/dark-mode", method = RequestMethod.GET)
    public boolean darkMode() {
        return darkModeEnabled;
    }

    private boolean isToggleOnCoolDown() {
        if (lastToggled == null) {
            lastToggled = LocalDateTime.now();

            return false;
        } else {
            LocalDateTime now = LocalDateTime.now();
            long diff = SECONDS.between(lastToggled, now);

            return diff < 3;
        }
    }
}