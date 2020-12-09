package day9;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day9 {

    private static List<Long> numbers;

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day9/input.txt"))
            .collect(Collectors.toList());

        out.println();
        items.forEach(answer -> out.println("Line: " + answer));
        out.println();

        numbers = items.stream()
            .map(Long::parseLong)
            .collect(Collectors.toList());

        task1(items);
        task2(items);
    }

    private static void task1(List<String> items) {

        final int size = 25;
        final Keys keys = new Keys(numbers, size);

        final Optional<Long> result = numbers.subList(size, numbers.size()).stream()
            .filter(number -> {
                final boolean testResult = testNumber(number, keys);
                if (testResult) {
                    return true;
                }
                keys.push(number);
                return false;
            })
            .findFirst();

        out.println();
        out.println("Result: " + result);
        out.println();
    }

    private static boolean testNumber(Long number, Keys keys) {
        final List<List<Long>> pairs = keys.getPairs();
        return pairs.stream()
            .noneMatch(pair -> number.equals(pair.get(0) + pair.get(1)));
    }

    private static void task2(List<String> items) {
//        final long invalidNumber = 127; // 15
        final long invalidNumber = 258585477; // 594
//        final int invalidNumberIndex = 14;
        final int invalidNumberIndex = 593;

        final int size = invalidNumberIndex;
        final Keys keys = new Keys(numbers, size);

        final List<List<Long>> combinations = keys.getBursts().stream()
            .filter(burst -> invalidNumber == burst.stream().mapToLong(n -> n).sum())
            .collect(Collectors.toList());

        out.println();
        out.println("Combinations: " + combinations);
        combinations.forEach(burst -> out.println("Sum: "
            + (burst.stream().min(Long::compareTo).orElseThrow() + burst.stream().max(Long::compareTo).orElseThrow())));
        out.println();
    }

    private static class Keys {
        private final LinkedList<Long> keys = new LinkedList<>();

        public Keys(List<Long> items, int size) {
            keys.addAll(items.subList(0, size));
        }

        @Override
        public String toString() {
            return "Keys{" +
                "keys=" + keys +
                '}';
        }

        public void push(Long number) {
            keys.remove();
            keys.add(number);
        }

        public List<List<Long>> getPairs() {
            final List<List<Long>> pairs = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                for (int j = i + 1; j < keys.size(); j++) {
                    pairs.add(List.of(keys.get(i), keys.get(j)));
                }
            }
            return pairs;
        }

        public List<List<Long>> getBursts() {
            final List<List<Long>> burst = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                for (int j = i + 1; j < keys.size(); j++) {
                    burst.add(keys.subList(i, j + 1));
                }
            }
            return burst;
        }
    }
}
