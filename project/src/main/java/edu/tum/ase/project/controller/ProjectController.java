package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.ProjectSourceFile;
import edu.tum.ase.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("projects")
public class ProjectController {
    @Autowired
    private ProjectService service;

    @GetMapping("/")
    public List<Project> index() {
        return service.findAll();
    }

    // create and read
    @PostMapping("create")
    public Project create(@RequestBody Project project) {
        return service.create(project);
    }

    @GetMapping("{id}")
    public Project read(@PathVariable("id") String id) {
        return service.findById(id);
    }

    // updates
    @PostMapping("{id}/rename")
    public Project rename(@RequestBody String name, @PathVariable(name = "id") String id) {
        Project p = service.findById(id);
        return service.updateName(p, name);
    }

    @PostMapping("{id}/add-sourcefile")
    public Project addSourceFile(@RequestBody ProjectSourceFile sourceFile, @PathVariable(name ="id") String id) {
        Project p = service.findById(id);
        return service.addSourceFile(p, sourceFile);
    }

    @PostMapping("{id}/remove-sourcefile")
    public Project removeSourceFile(@RequestBody ProjectSourceFile sourceFile, @PathVariable(name ="id") String id) {
        Project p = service.findById(id);
        return service.removeSourceFile(p, sourceFile);
    }

    // delete
    @DeleteMapping("{id}/delete")
    public Boolean delete(@PathVariable(name="id") String id) {
        Project p = service.findById(id);
        var isRemoved = service.delete(p);
        return isRemoved;
    }
}