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
public class CompilerUnitTests {

    @Autowired
    private CompilerController systemUnderTest;

    @Test
    public void should_ThrowIllegalArgumentException_When_UnsupportedFileEnding() {
        //given
        SourceCode sc = new SourceCode();
        sc.setFileName("file.abc");

        // then
        try {
            systemUnderTest.compile(sc);
            fail("Should have thrown IllegalArgumentException.");
        } catch(Exception e) {
            // then
            assert(e instanceof IllegalArgumentException);
        }
    }
}
