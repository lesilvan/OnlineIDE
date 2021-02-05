package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.ProjectSourceFile;
import edu.tum.ase.project.repository.ProjectRepository;
import edu.tum.ase.project.repository.ProjectSourceFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectSourceFileRepository sourceFileRepository;

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    @PreAuthorize("#project.userIds.contains(authentication.principal)")
    public Project update(Project project) {
        return projectRepository.save(project);
    }

    @PreAuthorize("#project.userIds.contains(authentication.principal)")
    public Project updateName(Project project, String name) {
        project.setName(name);
        return projectRepository.save(project);
    }

    @PreAuthorize("#project.userIds.contains(authentication.principal)")
    public Project addSourceFile(Project project, ProjectSourceFile sourceFile) {
        sourceFile = sourceFileRepository.save(sourceFile);
        project.addSourceFile(sourceFile);

        return projectRepository.save(project);
    }

    @PreAuthorize("#project.userIds.contains(authentication.principal)")
    public Project removeSourceFile(Project project, ProjectSourceFile sourceFile) {
        project.removeSourceFile(sourceFile);
        project = projectRepository.save(project);
        sourceFileRepository.delete(sourceFile);

        return project;
    }

    public Project findById(String id) throws IllegalAccessError{
        Optional<Project> project = projectRepository.findById(id);

        if (project.isPresent()) {
            return project.get();
        } else {
            throw new IllegalAccessError("Project with id "+id+" could not be found");
        }
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @PreAuthorize("#project.userIds.contains(authentication.principal)")
    public boolean delete(Project project) {
        try {
            projectRepository.delete(project);
            for (ProjectSourceFile sf : project.getSourceFiles()) {
                sf.deleteSourceFile();
            }
            return true;
        }
        catch (Exception e){
            // Project could not be safely deleted
            return false;
        }
    }
}
