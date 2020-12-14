package day13;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day13 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day13/input.txt"))
            .collect(Collectors.toList());

        task1(items);
        task2(items);
    }

    private static void task1(List<String> items) {
        final Integer earliestTimestamp = Integer.parseInt(items.get(0));
        final List<Integer> minBus = Arrays.stream(items.get(1).split(","))
            .filter(s -> !s.equals("x"))
            .map(Integer::parseInt)
            .map(bus -> List.of(bus, getDeparture(earliestTimestamp, bus)))
            .min(Comparator.comparing(o -> o.get(1)))
            .orElseThrow();

        out.println(earliestTimestamp + " -> " + minBus);
        out.println( "Result -> " + minBus.get(0) * (minBus.get(1) - earliestTimestamp));
    }

    private static Integer getDeparture(Integer earliestTimestamp, Integer bus) {
        return ((Integer) (earliestTimestamp / bus)) * bus + bus;
    }

    private static void task2(List<String> items) {
        final List<Integer> busNumbers  = Arrays.stream(items.get(1).split(","))
            .map(s -> s.equals("x") ? 1 : Integer.parseInt(s))
            .collect(Collectors.toList());

        out.println("Result: " + getDepartureFast(busNumbers));
    }

    private static long getDepartureFast(List<Integer> busNumbers) {
        final AtomicInteger index = new AtomicInteger(-1);
        final List<List<Integer>> busNumbersWithIndex = busNumbers.stream()
            .map(bus -> List.of(bus, index.addAndGet(1)))
            .sorted(Comparator.comparing(o -> -o.get(0)))
            .filter(bus -> !bus.get(0).equals(1))
            .collect(Collectors.toList());

        long departure = -busNumbersWithIndex.get(0).get(1);
        boolean allMatching = false;
        long step = busNumbersWithIndex.get(0).get(0);
        int start = 1;
        while (!allMatching) {
            departure += step;
            boolean matching = true;
            for (int i = start; i < busNumbersWithIndex.size(); i++) {
                final long modulo = (departure + busNumbersWithIndex.get(i).get(1)) % busNumbersWithIndex.get(i).get(0);
                if (modulo != 0) {
                    matching = false;
                    break;
                } else {
                    step = step * busNumbersWithIndex.get(i).get(0);
                    start++;
                }
            }
            if (matching) {
                allMatching = true;
            }
        }
        return departure;
    }
}