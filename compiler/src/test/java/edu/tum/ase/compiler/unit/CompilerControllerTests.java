package edu.tum.ase.compiler.unit;

import edu.tum.ase.compiler.controller.CompilerController;
import edu.tum.ase.compiler.model.SourceCode;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class CompilerControllerTests {

    @Autowired
    private CompilerController controller;

    @Test
    public void CCompilationSuccess() {
        SourceCode sc = new SourceCode();
        sc.setFileName("helloworld.c");
        String code = "#include <stdio.h>\n\tint main() { printf(\"hello world.\"); }";
        sc.setCode(code);

        try {
            controller.compile(sc);
        } catch(IOException e) {
            fail("Should not throw exception.");
            return;
        }

        assertEquals(sc.getCompilable(), true);
    }

    @Test
    public void CCompilationFail() {
        SourceCode sc = new SourceCode();
        sc.setFileName("helloworld.c");
        String semicolonMissing = "#include <stdio.h>\n\tint main() { printf(\"hello world.\") }";
        sc.setCode(semicolonMissing);

        try {
            controller.compile(sc);
        } catch(IOException e) {
            fail("Should not throw exception.");
            return;
        }

        assertEquals(sc.getCompilable(), false);
        assertNotEquals(sc.getStderr(), null);
    }

    @Test
    public void JavaCompilationSuccess() {
        SourceCode sc = new SourceCode();
        sc.setFileName("HelloWorld.java");
        String code = "class HelloWorld { public static void main(String args[]) { System.out.println(\"hello world.\"); } }";
        sc.setCode(code);

        try {
            controller.compile(sc);
        } catch(IOException e) {
            fail("Should not throw exception.");
            return;
        }

        assertEquals(sc.getCompilable(), true);
    }

    @Test
    public void JavaCompilationFail() {
        SourceCode sc = new SourceCode();
        sc.setFileName("HelloWorld.java");
        String semicolonMissing = "class HelloWorld { public static void main(String args[]) { System.out.println(\"hello world.\") } }";
        sc.setCode(semicolonMissing);

        try {
            controller.compile(sc);
        } catch(IOException e) {
            fail("Should not throw exception.");
            return;
        }

        assertEquals(sc.getCompilable(), false);
        assertNotEquals(sc.getStderr(), null);
    }

    @Test
    public void UnknownFileExtension() {
        SourceCode sc = new SourceCode();
        sc.setFileName("wrong-extension.abc");

        // should throw error
        try {
            controller.compile(sc);
            fail("Should have thrown IllegalArgumentException.");
        } catch(Exception e) {
            assert(e instanceof IllegalArgumentException);
        }
    }
}
