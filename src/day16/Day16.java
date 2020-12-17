package day16;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day16 {

    public static void main(String[] args) throws IOException {
        task1();
        task2();
    }

    private static void task1() throws IOException {
        final List<String> items = lines(of("src/day16/input.txt"))
            .collect(Collectors.toList());

        final List<List<Integer>> rules = getRules(items);
        final List<List<Integer>> tickets = getTickets(items);

        final long invalidRate = getTickets(items).subList(1, tickets.size()).stream()
            .flatMap(Collection::stream)
            .filter(value -> rules.stream()
                .noneMatch(rule -> ((value >= rule.get(0) && value <= rule.get(1)) || (value >= rule.get(2) && value <= rule.get(3))))
            )
            .mapToInt(Integer::intValue)
            .sum();

        out.println("Result 1: " + invalidRate);
    }

    private static List<List<Integer>> getTickets(List<String> items) {
        return items.stream()
            .filter(s -> !s.contains(":"))
            .filter(s -> !s.strip().isEmpty())
            .map(s -> Arrays.stream(s.split(","))
                .map(String::strip)
                .map(Integer::parseInt)
                .collect(Collectors.toList()))
            .collect(Collectors.toList());
    }

    private static List<List<Integer>> getRules(List<String> items) {
        return items.stream()
            .filter(s -> s.contains(" or "))
            .map(s -> Arrays.stream(s.split(":")[1].split("-|or"))
                .map(String::strip)
                .map(Integer::parseInt)
                .collect(Collectors.toList()))
            .collect(Collectors.toList());
    }

    private static void task2() throws IOException {
        final List<String> items = lines(of("src/day16/input.txt"))
            .collect(Collectors.toList());

        final LinkedList<List<Integer>> rules = new LinkedList<>(getRules(items));
        final List<List<Integer>> tickets = getTickets(items);

        final List<List<Integer>> validTickets = tickets.stream()
            .filter(ticket -> ticket.stream()
                .noneMatch(value -> rules.stream()
                    .noneMatch(rule -> value >= rule.get(0) && value <= rule.get(1) || value >= rule.get(2) && value <= rule.get(3))
                )
            )
            .collect(Collectors.toList());

        final Map<Integer, List<Integer>> ticketValueToRules = new HashMap<>();
        for (int i = 0; i < tickets.get(0).size(); i++) {
            final int finalI = i;

            final List<Integer> values = validTickets.stream()
                .map(ticket -> ticket.get(finalI))
                .collect(Collectors.toList());

            final List<List<Integer>> rulesFound = rules.stream()
                .filter(rule -> values.stream()
                    .allMatch(value -> value >= rule.get(0) && value <= rule.get(1) || value >= rule.get(2) && value <= rule.get(3))
                )
                .collect(Collectors.toList());

            ticketValueToRules.put(i, rulesFound.stream()
                .map(rules::indexOf)
                .collect(Collectors.toList())
            );
        }

        final List<Map.Entry<Integer, List<Integer>>> ticketValueToRulesSorted = ticketValueToRules.entrySet().stream()
            .sorted(Comparator.comparingInt(o -> o.getValue().size()))
            .collect(Collectors.toList());
        final Collection<Integer> resolvedRules = new ArrayList<>();
        resolvedRules.add(ticketValueToRulesSorted.get(0).getValue().get(0));

        for (int i = 1; i < ticketValueToRulesSorted.size(); i++) {
            ticketValueToRulesSorted.get(i).getValue().removeAll(resolvedRules);
            resolvedRules.add(ticketValueToRulesSorted.get(i).getValue().get(0));
        }

        final Map<Integer, Integer> ruleToPosition = ticketValueToRulesSorted.stream()
            .collect(Collectors.toMap(e -> e.getValue().get(0), Map.Entry::getKey));

        long result = 1;
        for (int i = 0; i < 6; i++) {
            final Integer value = tickets.get(0).get(ruleToPosition.get(i));
            result *= value;
        }

        out.println("Result 2: " + result);

    }
}