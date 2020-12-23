package edu.tum.ase.project.model;

import edu.tum.ase.project.model.Project;

import edu.tum.ase.project.service.ProjectSourceFileService;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

@Entity
@Table(name = "project_source_files")
public class ProjectSourceFile {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "project_source_file_id")
    private String id;

    @Column(name = "source_file_name", nullable = false)
    private String name;

    protected ProjectSourceFile() {
    }

    public ProjectSourceFile(String name) {
        this.name = name;
    }

    public void writeSourceFile(String sourceCode) {
        try {
            String filePath = getFilePath();
            File f = new File(filePath);
            f.getParentFile().mkdirs();
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f, false);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            byte[] bytes = sourceCode.getBytes(StandardCharsets.UTF_16);
            bos.write(bytes);

            bos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSourceFile() {
        File sourceFile = new File(getFilePath());
        sourceFile.delete();
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

    public String getFilePath() {
        return "./tmp/" + id;
    }
}
