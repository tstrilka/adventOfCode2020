package day13;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day13 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day13/input.txt"))
            .collect(Collectors.toList());

        task1(items);
        task2(items);
    }

    private static void task1(List<String> instructions) {
    }

    private static void task2(List<String> instructions) {
    }
}