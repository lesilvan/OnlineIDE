package edu.tum.ase.project.service;

import edu.tum.ase.project.model.ProjectSourceFile;
import edu.tum.ase.project.repository.ProjectSourceFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectSourceFileService {
    @Autowired
    private ProjectSourceFileRepository repo;

    public ProjectSourceFile create(ProjectSourceFile sourceFile) {
        return repo.save(sourceFile);
    }

    @PreAuthorize("#sourceFile.userIds.contains(authentication.principal)")
    public ProjectSourceFile updateSourceCode(ProjectSourceFile sourceFile, String sourceCode) {
        sourceFile.writeSourceFile(sourceCode);
        return repo.save(sourceFile);
    }

    @PreAuthorize("#sourceFile.userIds.contains(authentication.principal)")
    public ProjectSourceFile updateName(ProjectSourceFile sourceFile, String name) {
        sourceFile.setName(name);
        return repo.save(sourceFile);
    }

    public ProjectSourceFile findById(String id) throws IllegalAccessError {
        Optional<ProjectSourceFile> sf = repo.findById(id);

        if (sf.isPresent()) {
            return sf.get();
        } else {
            throw new IllegalAccessError();
        }
    }

    @PreAuthorize("#sourceFile.userIds.contains(authentication.principal)")
    public String getSourceCode(ProjectSourceFile sourceFile) {
        return sourceFile.provideSourceCode();
    }

    public List<ProjectSourceFile> findAll() {
        return repo.findAll();
    }

    @PreAuthorize("#sourceFile.userIds.contains(authentication.principal)")
    public ProjectSourceFile share(ProjectSourceFile sourceFile, String username) {
        //ProjectSourceFile sourceFile = this.findById(id);
        sourceFile.addUser(username);
        return repo.save(sourceFile);
    }
}