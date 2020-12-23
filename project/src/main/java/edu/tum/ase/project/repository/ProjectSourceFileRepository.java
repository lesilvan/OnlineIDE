package edu.tum.ase.project.repository;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.ProjectSourceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSourceFileRepository extends JpaRepository<ProjectSourceFile, String> {
}
