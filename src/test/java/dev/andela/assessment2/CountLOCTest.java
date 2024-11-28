package dev.andela.assessment2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CountLOCTest {

    @Test
    public void shouldWorkOnYourCustomTest1() throws IOException {
        String path = "./src/test/java/countloc/fixtures/Example1.java";
        String code = new String(Files.readAllBytes(Paths.get(path)));

        assertEquals(7, CountLOC.count(code));
    }

    @Test
    public void shouldWorkOnYourCustomTest2() throws IOException {
        String path = "./src/test/java/countloc/fixtures/Example2.java";
        String code = new String(Files.readAllBytes(Paths.get(path)));

        assertEquals(5, CountLOC.count(code));
    }

    @Test
    public void shouldWorkOnYourCustomTest3() throws IOException {
        // TODO need to be fixed
        String path = "./src/test/java/countloc/fixtures/Example3.java";
        String code = new String(Files.readAllBytes(Paths.get(path)));

        assertEquals(5, CountLOC.count(code));
    }
}