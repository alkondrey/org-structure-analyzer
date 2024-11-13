package com.swissre;

import com.swissre.exception.CannotFindCeoException;
import com.swissre.exception.CannotParseFileException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void throwExceptionWhenFilePathIsNotPassedIntoCommandLine() {
        assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{}));
    }

    @Test
    public void process1Employee() {
        runTestCase("cases/process-1-employee");
    }

    @Test
    public void process1000Employees() {
        runTestCase("cases/process-1000-employees");
    }

    @Test
    public void processEmployeesWithSameId() {
        // NOTE: Takes the first employee from the file and ignores others with the same ID.
        runTestCase("cases/process-employees-with-same-id");
    }

    @Test
    public void processGeneralCase() {
        runTestCase("cases/process-general-case");
    }

    @Test
    public void processMultipleCeos() {
        // NOTE: Takes the first CEO from the file and ignores others.
        runTestCase("cases/process-multiple-ceos");
    }

    @Test
    public void throwExceptionWhenEmployeesDoesntHaveCeo() {
        runTestCase("cases/throw-exception-when-employees-doesnt-have-ceo", CannotFindCeoException.class);
    }

    @Test
    public void throwExceptionWhenFileDoesntHaveEnoughFields() {
        runTestCase("cases/throw-exception-when-file-doesnt-have-enough-fields", CannotParseFileException.class);
    }

    @Test
    public void throwExceptionWhenFileHasInvalidHeader() {
        runTestCase("cases/throw-exception-when-file-has-invalid-header", CannotParseFileException.class);
    }

    @Test
    public void throwExceptionWhenFileIsEmpty() {
        runTestCase("cases/throw-exception-when-file-is-empty", CannotParseFileException.class);
    }

    @Test
    public void throwExceptionWhenLoadingTooMuchEmployees() {
        runTestCase("cases/throw-exception-when-loading-too-much-employees", CannotParseFileException.class);
    }

    private void runTestCase(String caseDirPath) {
        runTestCase(caseDirPath, null);
    }

    private void runTestCase(String caseDirPath, Class<? extends Throwable> expectedException) {
        String inputFilePath = Objects.requireNonNull(getClass().getClassLoader().getResource(caseDirPath + "/input.csv")).getPath();
        if (expectedException != null) {
            assertThrows(expectedException, () -> Main.main(new String[]{inputFilePath}));
            return;
        }

        Main.main(new String[]{inputFilePath});

        String expectedOutputFilePath = Objects.requireNonNull(getClass().getClassLoader().getResource(caseDirPath + "/expected-output.txt")).getPath();
        try {
            List<String> lines = Files.readAllLines(Paths.get(expectedOutputFilePath));
            assertEquals(String.join("\n", lines), outContent.toString().trim());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
