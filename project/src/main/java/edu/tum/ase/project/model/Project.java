package edu.tum.ase.project.model;

import edu.tum.ase.project.service.ProjectSourceFileService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "project_id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<ProjectSourceFile> sourceFiles = Collections.emptySet();

    protected Project() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public Project(String name, Set <ProjectSourceFile> sourceFiles) {
        this.name = name;
        this.sourceFiles = sourceFiles;
    }

    public void addSourceFile(ProjectSourceFile sourceFile) {
        boolean isAlready = false;
        for (ProjectSourceFile sf : getSourceFiles()) {
            if (sf.getId().equals(sourceFile.getId())) {
                isAlready = true;
            }
        }
        if (!isAlready) {
            sourceFiles.add(sourceFile);
        }
    }

    public void removeSourceFile(ProjectSourceFile sourceFile) {
        for (ProjectSourceFile sf : getSourceFiles()) {
            if (sf.getId().equals(sourceFile.getId())) {
                sourceFiles.remove(sf);
                sf.deleteSourceFile();
            }
        }
    }

    public Set<ProjectSourceFile> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(Set<ProjectSourceFile> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
