package day20;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day20 {

    public static void main(String[] args) throws IOException {
        task1("src/day20/test-input.txt");
    }

    private static void task1(String file) throws IOException {
        final List<String> items = lines(of(file))
            .collect(Collectors.toList());
        out.println(items);
    }
}