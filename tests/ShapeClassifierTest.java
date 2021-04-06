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

class ShapeClassifierTest {

    @TestFactory
    Collection<DynamicTest> evaluateTriples() {
        return generateTestCases("./recources/blackBox/triples.csv");
    }

    @TestFactory
    Collection<DynamicTest> evaluatePairs() {
        return generateTestCases("./recources/blackBox/pairs.csv");
    }

    @TestFactory
    Collection<DynamicTest> evaluateSingles() {
        return generateTestCases("./recources/blackBox/singles.csv");
    }

    private Collection<DynamicTest> generateTestCases(String filename) {
        Collection<DynamicTest> testCollection = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                ShapeClassifier classifier = new ShapeClassifier();
                String input = generateInput(line);
                String expectedOutput = line.split(",")[14];
                testCollection.add(DynamicTest.dynamicTest(input + " - " + expectedOutput, () -> Assertions.assertEquals(expectedOutput, classifier.evaluateGuess(input))));
                line = reader.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return testCollection;
    }
    private String generateInput(String input) {
        String[] params = input.split(",");
        String shape = params[0];
        String size = params[1];
        String isDescriptionEven = params[2];
        int amountOfSides = Integer.parseInt(params[3]);
        int amountOfUnique = Integer.parseInt(params[4]);
        boolean isPerimeterMoreHundred = params[5].equals(">100");
        boolean isProductEven = params[6].equals("Even");
        return shape + "," + size + "," + isDescriptionEven + generateParams(amountOfSides, amountOfUnique, isPerimeterMoreHundred, isProductEven);

    }
    private String generateParams(int amountOfSides, int amountOfUnique, boolean isPerimeterMoreHundred, boolean isProductEven) {
        Random random = new Random(10);
        int base;
        if (isPerimeterMoreHundred) {
            base = 500;
        } else {
            base = 0;
        }
        final int[] ints = random.ints(1, 15).distinct().limit(6).toArray();


        int[] sides = new int[amountOfSides];

        for (int i = 0; i < amountOfSides; i++) {
            sides[i] = base + ints[i];
        }

        if (amountOfUnique == 1) {
            for (int i = 0; i < sides.length; i++) {
                sides[i] = sides[0];
            }
        }

        if (amountOfUnique == 2) {
            for (int i = 1; i < sides.length; i++) {
                sides[i] = sides[1];
            }
        }

        if (amountOfUnique == 3) {
            for (int i = 2; i < sides.length; i++) {
                sides[i] = sides[2];
            }
        }

        if (isProductEven) {
            if (sides[0] % 2 != 0) {
                sides[0] += 1;
            }
        } else {
            for (int i = 0; i < amountOfSides; i++) {
                if (sides[i] % 2 == 0) {
                    sides[i] += 1;
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < amountOfSides; i++) {
            result.append(",").append(sides[i]);
        }
        return result.toString();
    }
}