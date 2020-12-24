package day25;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day25 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day25/input.txt"))
            .collect(Collectors.toList());
        out.println("Lines: " + items);

        task();
    }

    private static void task() {
    }
}