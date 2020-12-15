package day14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day14 {

    public static void main(String[] args) throws IOException {

        task1();
        task2();
    }

    private static void task1() throws IOException {

        final AtomicReference<String> mask = new AtomicReference<>("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        final Map<Long, Long> memory = new HashMap<>();

        lines(of("src/day14/input.txt"))
            .map(item -> parseInput(mask, item))
            .filter(mem -> !mem.isEmpty())
            .forEach(mem ->
                memory.put(mem.get(0), maskValue1(mem.get(1), mask.get()))
            );

        final long result = memory.values().stream()
            .mapToLong(value -> value).sum();
        out.println("Result: " + result);
    }

    private static List<Long> parseInput(AtomicReference<String> mask, String item) {
        if (item.startsWith("mem")) {
            return Arrays.stream(item.substring(4).split("] = "))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        } else {
            mask.set(item.substring(7));
            return List.<Long>of();
        }
    }

    private static Long maskValue1(Long value, String mask) {
        final char[] chars = mask.toCharArray();
        Long result = value;
        for (int i = 0; i < chars.length; i++) {
            int j = 35 - i;
            if (chars[j] == 'X') {
                // nothing
            } else if (chars[j] == '1') {
                result = result | (1L << i);
            } else if (chars[j] == '0') {
                result = result & ~(1L << i);
            }
        }
        return result;
    }

    private static void task2() throws IOException {
        final AtomicReference<String> mask = new AtomicReference<>("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        final Map<Long, Long> memory = new HashMap<>();

        lines(of("src/day14/input.txt"))
            .map(item -> parseInput(mask, item))
            .filter(mem -> !mem.isEmpty())
            .forEach(mem ->
                memory.putAll(maskValue2(mem.get(0), mem.get(1), mask.get())
            ));

        final long result = memory.values().stream()
            .mapToLong(value -> value).sum();
        out.println("Result: " + result);
    }

    private static Map<Long, Long> maskValue2(Long position, Long value, String mask) {
        final char[] chars = mask.toCharArray();
        List<Long> positions = new ArrayList<>();
        positions.add(position);
        for (int i = 0; i < chars.length; i++) {
            int j = 35 - i;
            if (chars[j] == 'X') {
                final AtomicLong atomicI = new AtomicLong(i);
                positions = positions.stream()
                    .flatMap(pos -> Stream.of(pos | (1L << atomicI.get()), pos & ~(1L << atomicI.get())))
                    .collect(Collectors.toList());
            } else if (chars[j] == '1') {
                final AtomicLong atomicI = new AtomicLong(i);
                positions = positions.stream()
                    .map(pos -> pos | (1L << atomicI.get()))
                    .collect(Collectors.toList());
            } else if (chars[j] == '0') {
                // nothing
            }
        }
        return positions.stream()
            .collect(Collectors.toMap(pos -> pos, pos -> value));
    }
}