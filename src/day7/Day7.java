package day7;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day7 {

    static Map<String, Set<Couple>> rules;

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day7/input.txt"))
            .collect(Collectors.toList());

        out.println();
        items.forEach(answer -> out.println("Rule: " + answer));
        out.println();

        task1(items);
        task2(items);
    }

    private static void task1(List<String> items) {
         rules = items.stream()
            .collect(Collectors.toMap(
                line -> line.substring(0, line.indexOf("contain") - 2),
                line -> Arrays.stream(
                    line.substring(line.indexOf("contain") + 8).split(",", 0))
                    .map(Couple::new)
                    .collect(Collectors.toSet())));
        out.println();
        rules.entrySet().forEach(rule -> out.println("Rule: " + rule));
        out.println();

        long count = rules.values().stream()
            .filter(Day7::isRuleCapable)
            .count();
        out.println();
        out.println(count);
        out.println();    }

    private static boolean isRuleCapable(Set<Couple> values) {
        if (values.stream().anyMatch(v -> v.bag.contains("shiny gold bag"))) {
            return true;
        }
        final Set<String> keys = values.stream()
            .filter(v -> !v.bag.contains("no other bag"))
            .map(v -> rules.keySet().stream()
                .filter(k -> v.bag.contains(k))
                .findFirst()
                .orElseThrow()
            )
            .collect(Collectors.toSet());
        out.println(keys);
        return keys.stream()
            .anyMatch(k -> isRuleCapable(rules.get(k)));
    }

    private static void task2(List<String> items) {
        rules = items.stream()
            .collect(Collectors.toMap(
                line -> line.substring(0, line.indexOf("contain") - 2),
                line -> Arrays.stream(
                    line.substring(line.indexOf("contain") + 8).split(",", 0))
                    .map(Couple::new)
                    .collect(Collectors.toSet())));
        out.println();
        out.println("Sum " + sumSiblings("shiny gold bag"));
        out.println();

    }

    private static long sumSiblings(String key) {
        final Map.Entry<String, Set<Couple>> rule = rules.entrySet().stream()
            .filter(e -> key.contains(e.getKey()))
            .findFirst()
            .orElseThrow();
        out.println("rule: " + rule);
        final List<Long> siblingSums = rule.getValue().stream()
            .filter(v -> !v.bag.contains("no other bag"))
            .map(sibling -> sibling.count * sumSiblings(rules.keySet().stream()
                .filter(k -> sibling.bag.contains(k))
                .findFirst()
                .orElseThrow())
            )
            .collect(Collectors.toList());
        long result = 1 + siblingSums.stream().reduce(0L, Long::sum);
        out.println("result for key " + key + " -> " + result);
        return result;
    }

    private static class Couple {
        String bag;
        int count;

        public Couple(String s) {
            bag = s.stripLeading();
            if (bag.contains("no other bag")) {
                count = 0;
            } else {
                count = Integer.parseInt(bag.substring(0, bag.indexOf(' ')));
            }
        }

        @Override
        public String toString() {
            return "Couple{" +
                "bag='" + bag + '\'' +
                ", count=" + count +
                '}';
        }
    }
}
