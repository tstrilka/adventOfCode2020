package day12;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day12 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day12/input.txt"))
            .collect(Collectors.toList());

        task1(items);
        task2(items);
    }

    private static void task1(List<String> items) {
    }

    private static void task2(List<String> items) {
    }
}