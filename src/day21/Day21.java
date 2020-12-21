package day21;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day21 {

    public static void main(String[] args) throws IOException {
        task("src/day21/test-input.txt");
    }

    private static void task(String path) throws IOException {
        final List<String> items = lines(of(path))
            .collect(Collectors.toList());
        out.println("Lines: " + items);

        final List<Set<String>> ingredients = items.stream()
            .map(line -> Arrays.stream(
                line
                    .split("\\(")[0]
                    .split(" ")
                )
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toSet())
            )
            .collect(Collectors.toList());

        out.println();
        out.println();
        out.println(ingredients);

        final List<Set<String>> allergens = items.stream()
            .map(line -> Arrays.stream(
                line
                    .split("\\(")[1]
                    .replace(")", "")
                    .replace("contains", "")
                    .replace(" ", "")
                    .split(",")
                )
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toSet())
            )
            .collect(Collectors.toList());

        out.println();
        out.println(allergens);
        out.println();


    }
}