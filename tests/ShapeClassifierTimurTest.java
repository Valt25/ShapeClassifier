import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

class ShapeClassifierTimurTest {

    @TestFactory
    Collection<DynamicTest> evaluateTriples() {
        return generateTestCases("./recources/blackBox/timur.csv");
    }

    private Collection<DynamicTest> generateTestCases(String filename) {
        Collection<DynamicTest> testCollection = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                ShapeClassifier classifier = new ShapeClassifier();
                String[] params = line.split(",");
                StringBuilder input = new StringBuilder();
                for (int i = 0; i < params.length - 1; i++) {
                    input.append(params[i]).append(",");
                }
                String expectedOutput = params[params.length - 1];
                testCollection.add(DynamicTest.dynamicTest(input.toString().substring(0, input.toString().length() - 1), () -> Assertions.assertEquals(expectedOutput, classifier.evaluateGuess(input.toString().substring(0, input.toString().length() - 1)))));
                line = reader.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return testCollection;
    }
}