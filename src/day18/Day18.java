package day18;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day18 {

    public static void main(String[] args) throws IOException {
        task1();
        task2();
    }

    private static void task1() throws IOException {
        final List<String> items = lines(of("src/day18/test-input.txt"))
            .collect(Collectors.toList());
        out.println(items);
    }

    private static void task2() throws IOException {
    }
}