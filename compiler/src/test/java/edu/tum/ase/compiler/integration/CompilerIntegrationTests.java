package edu.tum.ase.compiler.integration;

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
public class CompilerIntegrationTests {
    /** Testing integration of Compiler with os/filesystem under the hood */

    @Autowired
    private CompilerController systemUnderTest;

    @Test
    public void should_ReturnCompilableTrue_When_CCodeCompilable() {
        SourceCode sc = new SourceCode();
        sc.setFileName("helloworld.c");
        String code = "#include <stdio.h>\n\tint main() { printf(\"hello world.\"); }";
        sc.setCode(code);

        try {
            systemUnderTest.compile(sc);
        } catch(IOException e) {
            fail("Should not throw exception.");
            return;
        }

        assertTrue(sc.getCompilable());
    }

    @Test
    public void should_ReturnCompilableFalseAndErrorMessage_When_CCodeNotCompilable() {
        SourceCode sc = new SourceCode();
        sc.setFileName("helloworld.c");
        String semicolonMissing = "#include <stdio.h>\n\tint main() { printf(\"hello world.\") }";
        sc.setCode(semicolonMissing);

        try {
            systemUnderTest.compile(sc);
        } catch(IOException e) {
            fail("Should not throw exception.");
            return;
        }

        assertFalse(sc.getCompilable());
        assertNotEquals(sc.getStderr(), null);
    }

    @Test
    public void should_ReturnCompilableTrue_When_JavaCodeCompilable() {
        SourceCode sc = new SourceCode();
        sc.setFileName("HelloWorld.java");
        String code = "class HelloWorld { public static void main(String args[]) { System.out.println(\"hello world.\"); } }";
        sc.setCode(code);

        try {
            systemUnderTest.compile(sc);
        } catch(IOException e) {
            fail("Should not throw exception.");
            return;
        }

        assertTrue(sc.getCompilable());
    }

    @Test
    public void should_ReturnCompilableFalseAndErrorMessage_When_JavaCodeNotCompilable() {
        SourceCode sc = new SourceCode();
        sc.setFileName("HelloWorld.java");
        String semicolonMissing = "class HelloWorld { public static void main(String args[]) { System.out.println(\"hello world.\") } }";
        sc.setCode(semicolonMissing);

        try {
            systemUnderTest.compile(sc);
        } catch(IOException e) {
            fail("Should not throw exception.");
            return;
        }

        assertFalse(sc.getCompilable());
        assertNotEquals(sc.getStderr(), null);
    }
}
