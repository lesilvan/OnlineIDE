package edu.tum.ase.project.controller;

import edu.tum.ase.project.ProjectApplication;
import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.ProjectSourceFile;
import edu.tum.ase.project.model.User;
import edu.tum.ase.project.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("projects")
public class ProjectController {
    private static final Logger log = LoggerFactory.getLogger(ProjectApplication.class);

    @Autowired
    private ProjectService service;

    @Autowired
    private OAuth2RestOperations restTemplate;

    @Autowired
    private ProjectSourceFileController sourceFileController;

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_USER')")
    @PostFilter("filterObject.userIds.contains(authentication.principal)")
    @GetMapping("/")
    public List<Project> index() {
        return service.findAll();
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_USER')")
    @PostMapping("/create")
    public Project create(@RequestBody Project project) {
        Principal creator = SecurityContextHolder.getContext().getAuthentication();
        if (creator != null) {
            project.addUser(creator.getName());  //add username
            log.info("Created by:" + creator.getName());
        } else {
            log.info("No creator!");
        }
        return service.create(project);
    }

    @PostAuthorize("returnObject.userIds.contains(authentication.principal)")
    @GetMapping("/{id}")
    public Project read(@PathVariable("id") String id) throws IllegalAccessError {
        return service.findById(id);
    }

    // internal authorization
    @PostMapping("/{id}/rename")
    public Project rename(@RequestBody String name, @PathVariable(name = "id") String id) {
        Project p = service.findById(id);
        System.out.println(p.getUserIds());
        return service.updateName(p, name);
    }

    // internal authorization
    @PostMapping("/{id}/add-sourcefile")
    public Project addSourceFile(@RequestBody ProjectSourceFile sourceFile, @PathVariable(name = "id") String id) {
        Project p = service.findById(id);
        return service.addSourceFile(p, sourceFile);
    }

    // internal authorization
    @PostMapping("/{id}/remove-sourcefile")
    public Project removeSourceFile(@RequestBody ProjectSourceFile sourceFile, @PathVariable(name = "id") String id) {
        Project p = service.findById(id);
        return service.removeSourceFile(p, sourceFile);
    }

    // internal authorization
    @DeleteMapping("/{id}/delete")
    public Boolean delete(@PathVariable(name = "id") String id) {
        Project p = service.findById(id);
        var isRemoved = service.delete(p);
        return isRemoved;
    }

    // internal authorization
    @PostMapping("/{id}/share")
    public Project share(@RequestBody String username, @PathVariable(name="id") String id) {
        // String url = "https://gitlab.lrz.de/api/v4/search?scope=users&search=Heilmann"; search e-mail or name
        // String url = "https://gitlab.lrz.de/api/v4/users?username=silvan11"; search via username
        // Try to get userData corresponding to username
        String searchUrl = "https://gitlab.lrz.de/api/v4/users?username=" + username;
        User[] users = restTemplate.getForObject(searchUrl, User[].class);
        if (users.length == 1) {
            for (User user: users) {
                // Add user to project database
                Project project = service.findById(id);
                project.addUser(user.getUsername());
                service.update(project);

                // Add user to sourcefile database (each file individually)
                Set<ProjectSourceFile> sourceFiles = project.getSourceFiles();
                for (ProjectSourceFile sourceFile: sourceFiles) {
                    this.sourceFileController.share(sourceFile.getId(), user.getUsername());
                }

                // Some messages for debugging
                // TODO: Remove
                System.out.println("Project shared with:");
                System.out.println(user.getId());
                System.out.println(user.getName());
                System.out.println(user.getUsername());
                System.out.println("end of user");
            }
        }
        // Return updated (or not in case of error) project
        return service.findById(id);
    }
}