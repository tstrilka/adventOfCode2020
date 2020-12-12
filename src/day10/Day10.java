package day10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day10 {

    public static void main(String[] args) throws IOException {
        final List<Long> items = lines(of("src/day10/input.txt"))
            .map(Long::parseLong)
            .sorted()
            .collect(Collectors.toList());

        out.println();
        out.println("Adapters: " + items);
        out.println();

        task1(items);
        task2(items);
    }

    private static void task1(List<Long> adapters) {
        final Map<Long, Long> counts = new HashMap<>();
        for (int i = 0; i < adapters.size() - 1; i++) {
            final long dif = adapters.get(i + 1) - adapters.get(i);
            counts.put(dif, counts.getOrDefault(dif, 1L) + 1);
        }

        final long result = counts.get(1L) * counts.get(3L);
        out.println();
        out.println("Result: " + result);
        out.println();
    }

    private static void task2(List<Long> items) {
        final List<List<Long>> adapters = new ArrayList<>();
        adapters.add(new ArrayList<>(List.of(0L, 0L)));
        adapters.addAll(items.stream()
            .map(a -> new ArrayList<>(List.of(a, 0L)))
            .collect(Collectors.toList()));

        adapters.get(adapters.size() - 1).set(1, 1L);
        for (int i = adapters.size() - 2; i >= 0; i--) {
            long count = 0;
            if (i + 1 < adapters.size() && (adapters.get(i + 1).get(0) - adapters.get(i).get(0)) <= 3) {
                count++;
                count += adapters.get(i + 1).get(1) - 1;
            }
            if (i + 2 < adapters.size() && (adapters.get(i + 2).get(0) - adapters.get(i).get(0)) <= 3) {
                count++;
                count += adapters.get(i + 2).get(1) - 1;
            }
            if (i + 3 < adapters.size() && (adapters.get(i + 3).get(0) - adapters.get(i).get(0)) <= 3) {
                count++;
                count += adapters.get(i + 3).get(1) - 1;
            }
            adapters.get(i).set(1, count);
        }


        out.println();
        out.println("Count: " + adapters.get(0).get(1));
        out.println();
    }

}