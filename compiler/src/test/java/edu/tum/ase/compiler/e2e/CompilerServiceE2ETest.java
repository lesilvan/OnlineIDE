package edu.tum.ase.compiler.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tum.ase.compiler.model.SourceCode;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore
public class CompilerServiceE2ETest {
    private final String URL = "/compile";

    @Autowired
    private MockMvc system;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void JavaCompilationSuccess() throws Exception {
        SourceCode sc = new SourceCode();
        sc.setFileName("HelloWorld.java");
        String code = "class HelloWorld { public static void main(String args[]) { System.out.println(\"hello world.\"); } }";
        sc.setCode(code);

        ResultActions result = system.perform(post(URL).content(mapper.writeValueAsString(sc)).contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.compilable").value(true));
    }

    @Test
    public void JavaCompilationFail() throws Exception {
        SourceCode sc = new SourceCode();
        sc.setFileName("HelloWorld.java");
        String code = "class HelloWorld { public static void main(String args[]) { System.out.println(\"hello world.\") } }";
        sc.setCode(code);

        ResultActions result = system.perform(post(URL).content(mapper.writeValueAsString(sc)).contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.compilable").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stderr").isString());
    }

    @Test
    public void CCompilationSuccess() throws Exception {
        SourceCode sc = new SourceCode();
        sc.setFileName("helloworld.c");
        String code = "#include <stdio.h>\n\tint main() { printf(\"hello world.\"); }";
        sc.setCode(code);

        ResultActions result = system.perform(post(URL).content(mapper.writeValueAsString(sc)).contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.compilable").value(true));
    }

    @Test
    public void CCompilationFail() throws Exception {
        SourceCode sc = new SourceCode();
        sc.setFileName("helloworld.c");
        String code = "#include <stdio.h>\n\tint main() { printf(\"hello world.\") }";
        sc.setCode(code);

        ResultActions result = system.perform(post(URL).content(mapper.writeValueAsString(sc)).contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.compilable").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stderr").isString());
    }
}

