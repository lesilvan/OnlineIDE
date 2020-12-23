package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.ProjectSourceFile;
import edu.tum.ase.project.repository.ProjectRepository;
import edu.tum.ase.project.repository.ProjectSourceFileRepository;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectSourceFileService {
    @Autowired
    private ProjectSourceFileRepository repo;

    public ProjectSourceFile create(ProjectSourceFile sourceFile) {
        return repo.save(sourceFile);
    }

    public ProjectSourceFile updateSourceCode(ProjectSourceFile sourceFile, String sourceCode) {
        sourceFile.writeSourceFile(sourceCode);
        return repo.save(sourceFile);
    }

    public ProjectSourceFile updateName(ProjectSourceFile sourceFile, String name) {
        sourceFile.setName(name);
        return repo.save(sourceFile);
    }

    public ProjectSourceFile findById(String id) {
        Optional<ProjectSourceFile> sf = repo.findById(id);

        if (sf.isPresent()) {
            return sf.get();
        } else {
            throw new IllegalAccessError();
        }
    }

    public List<ProjectSourceFile> findAll() {
        return repo.findAll();
    }
}