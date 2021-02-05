package edu.tum.ase.project.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

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

    @ElementCollection
    @CollectionTable(name="source_file_project_users", joinColumns = @JoinColumn(name="id"))
    @Column(name = "usersIds")
    private Set<String> userIds = new HashSet<>();

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

            byte[] bytes = sourceCode.getBytes(StandardCharsets.UTF_8);
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

    public String provideSourceCode() {
        String sourceCode;
        // Try reading in, if not, then file hasn't been created yet => sourcecode is empty
        try {
            sourceCode = new String(Files.readAllBytes(Paths.get(getFilePath())));
            return sourceCode;
        } catch(IOException e) {
            e.printStackTrace();
            sourceCode = "";
        }

        return sourceCode;
    }

    public Set<String> getUserIds() {
        return userIds;
    }

    public void addUser(String user) {
        this.userIds.add(user);
    }

    public void setUserIds(Set<String> userIds) {
        this.userIds = userIds;
    }
}
