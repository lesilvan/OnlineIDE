package edu.tum.ase.project.controller;

import edu.tum.ase.project.ProjectApplication;
import edu.tum.ase.project.model.ProjectSourceFile;
import edu.tum.ase.project.service.ProjectSourceFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("sourcefiles")
public class ProjectSourceFileController {
    private static final Logger log = LoggerFactory.getLogger(ProjectApplication.class);

    @Autowired
    private ProjectSourceFileService service;

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_USER')")
    @PostFilter("filterObject.userIds.contains(authentication.principal)")
    @GetMapping("/")
    public List<ProjectSourceFile> index() {
        return service.findAll();
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_USER')")
    @PostMapping("create")
    public ProjectSourceFile create(@RequestBody ProjectSourceFile sourceFile) {
        Principal creator = SecurityContextHolder.getContext().getAuthentication();
        if (creator != null) {
            sourceFile.addUser(creator.getName());  //add username
            log.info("Created by:" + creator.getName());
        } else {
            log.info("No creator!");
        }
        return service.create(sourceFile);
    }

    @PostAuthorize("returnObject.userIds.contains(authentication.principal)")
    @GetMapping("{id}")
    public ProjectSourceFile read(@PathVariable(name = "id") String id) throws IllegalAccessError {
        return service.findById(id);
    }

    // Internal Authorization
    @GetMapping(value = "{id}/sourcecode", produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String getSourceCode(@PathVariable(name = "id") String id) {
        ProjectSourceFile sf = service.findById(id);
        return service.getSourceCode(sf);
    }

    // Internal Authorization
    @PostMapping("{id}/update-sourcecode")
    public ProjectSourceFile updateSourceCode(@RequestBody String sourceCode, @PathVariable(name = "id") String id) {
        ProjectSourceFile sf = service.findById(id);
        sf = service.updateSourceCode(sf, sourceCode);
        return sf;
    }

    // Internal Authorization
    @PostMapping("{id}/rename")
    public ProjectSourceFile rename(@RequestBody String name, @PathVariable(name = "id") String id) {
        ProjectSourceFile sf = service.findById(id);
        sf = service.updateName(sf, name);
        log.info(sf.getUserIds().toString());
        return sf;
    }

    public ProjectSourceFile share(String id, String username) {
        ProjectSourceFile sf = service.findById(id);
        return service.share(sf, username);
    }
}
