package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

    public static void main(String[] args) throws IOException {
        final List<Long> items = Files.lines(Path.of("src/day1/input.txt"))
            .map(Long::parseLong)
            .collect(Collectors.toList());
        System.out.println(items);
        items.forEach(a -> {
            items.forEach(b -> {
                final Long sum = a + b;
                if (sum.equals(2020L)) {
                    System.out.printf("%s, %s, %s%n", a, b, a*b);
                }
            });
        });
        items.forEach(a -> {
            items.forEach(b -> {
                items.forEach(c -> {
                    final Long sum = a + b + c;
                    if (sum.equals(2020L)) {
                        System.out.printf("%s, %s, %s, %s%n", a, b, c, a*b*c);
                    }
                });
            });
        });
    }
}
