package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.ProjectSourceFile;
import edu.tum.ase.project.repository.ProjectRepository;
import edu.tum.ase.project.repository.ProjectSourceFileRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectSourceFileRepository sourceFileRepository;

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    public Project update(Project project) {
        return projectRepository.save(project);
    }

    public Project updateName(Project project, String name) {
        project.setName(name);
        return projectRepository.save(project);
    }

    public Project addSourceFile(Project project, ProjectSourceFile sourceFile) {
        sourceFile = sourceFileRepository.save(sourceFile);
        project.addSourceFile(sourceFile);

        return projectRepository.save(project);
    }

    public Project removeSourceFile(Project project, ProjectSourceFile sourceFile) {
        project.removeSourceFile(sourceFile);
        project = projectRepository.save(project);
        sourceFileRepository.delete(sourceFile);

        return project;
    }

    public Project findById(String id) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isPresent()) {
            return project.get();
        } else {
            throw new IllegalAccessError();
        }
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

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
