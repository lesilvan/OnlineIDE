package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.ProjectSourceFile;
import edu.tum.ase.project.service.ProjectSourceFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("sourcefiles")
public class ProjectSourceFileController {

    @Autowired
    private ProjectSourceFileService service;

    @GetMapping("/")
    public List<ProjectSourceFile> index() {
        return service.findAll();
    }

    // create and read
    @PostMapping("create")
    public ProjectSourceFile create(@RequestBody ProjectSourceFile sourceFile) {
        return service.create(sourceFile);
    }

    // @GetMapping("{id}")
    // public ProjectSourceFile read(@PathVariable(name = "id") String id) {
    //     return service.fineddById(id);
    // }

    @GetMapping(value = "{id}/sourcecode", produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String getSourceCode(@PathVariable(name = "id") String id) {
        ProjectSourceFile sf = service.findById(id);
        return service.getSourceCode(sf);
    }

    // updates
    @PostMapping("{id}/update-sourcecode")
    public ProjectSourceFile updateSourceCode(@RequestBody String sourceCode, @PathVariable(name = "id") String id) {
        ProjectSourceFile sf = service.findById(id);
        sf = service.updateSourceCode(sf, sourceCode);
        return sf;
    }

    @PostMapping("{id}/rename")
    public ProjectSourceFile rename(@RequestBody String name, @PathVariable(name = "id") String id) {
        ProjectSourceFile sf = service.findById(id);
        sf = service.updateName(sf, name);
        return sf;
    }
}
