package day23;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day23 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day23/input.txt"))
            .collect(Collectors.toList());
        out.println("Lines: " + items);

        List<Integer> cups = items.get(0)
            .chars()
            .mapToObj(ch -> Integer.parseInt(Character.toString(ch)))
            .collect(Collectors.toList());

        task(cups, cups.size(), 100);
        task(cups, 1000000, 10000000);
    }

    private static void task(List<Integer> cups, int totalSize, int steps) {
        for (int i = cups.size(); i < totalSize; i++) {
            cups.add(i + 1);
        }

        final Map<Integer, Cup> cupsMap = new HashMap<>();
        cups.forEach(cup -> cupsMap.put(cup, new Cup(cup)));
        for (int i = 0; i < cups.size() - 1; i++) {
            cupsMap.get(cups.get(i)).setNext(cupsMap.get(cups.get(i + 1)));
        }
        cupsMap.get(cups.get(cups.size() - 1)).setNext(cupsMap.get(cups.get(0)));

        Cup currentCup = cupsMap.get(cups.get(0));
        for (int i = 0; i < steps; i++) {
            currentCup = doMove(cupsMap, currentCup);
        }

        out.print("Task 1: ");
        Cup cup = cupsMap.get(1);
        for (int i = 0; i < Math.min(cupsMap.size() - 1, 50); i++) {
            cup = cup.nextCup;
            out.print(cup.cup);
        }
        out.println();
        out.println("Task 2: " + (Long.valueOf(cupsMap.get(1).nextCup.cup) * cupsMap.get(1).nextCup.nextCup.cup));
    }

    private static Cup doMove(Map<Integer, Cup> cupsMap, Cup currentCup) {
        final Map<Integer, Cup> sublist = new HashMap<>();
        final Cup sublistFirstCup = currentCup.nextCup;
        sublist.put(sublistFirstCup.cup, sublistFirstCup);
        sublist.put(sublistFirstCup.nextCup.cup, sublistFirstCup.nextCup);
        final Cup sublistLastCup = sublistFirstCup.nextCup.nextCup;
        sublist.put(sublistLastCup.cup, sublistLastCup);

        final Cup cupAfterSublist = sublistLastCup.nextCup;
        currentCup.setNext(cupAfterSublist);

        int i = 1;
        if (currentCup.cup == 1) {
            i = 1 - cupsMap.size();
        }
        Cup destinationCup = cupsMap.get(currentCup.cup - i);
        while ((currentCup.cup - i) >= 1 && sublist.containsKey(destinationCup.cup)) {
            i++;
            destinationCup = cupsMap.get(currentCup.cup - i);
        }
        if (destinationCup == null) {
            destinationCup = cupsMap.get(cupsMap.keySet().stream().filter(v -> !sublist.containsKey(v)).max(Integer::compareTo).orElseThrow());
        }

        final Cup destinationNextCup = destinationCup.nextCup;
        destinationCup.setNext(sublistFirstCup);
        sublistLastCup.setNext(destinationNextCup);

        return currentCup.nextCup;
    }

    private static class Cup {
        private final Integer cup;
        private Cup nextCup;

        public Cup(Integer cup) {
            this.cup = cup;
            this.nextCup = null;
        }

        public void setNext(Cup nextCup) {
            this.nextCup = nextCup;
        }

        @Override
        public String toString() {
            return cup + ">" + (nextCup == null ? "" : nextCup.cup);
        }
    }
}