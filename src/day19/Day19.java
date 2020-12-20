package day19;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day19 {

    private static Map<Integer, Set<String>> knownCombinations = new HashMap<>();

    public static void main(String[] args) throws IOException {
        task("src/day19/input.txt");
        task("src/day19/input2.txt");
    }

    private static void task(String file) throws IOException {
        final List<String> items = lines(of(file))
            .collect(Collectors.toList());

        final List<Rule> rules = items.stream()
            .filter(s -> s.contains(":"))
            .map(Rule::new)
            .collect(Collectors.toList());

        final List<String> messages = items.stream()
            .filter(s -> !s.isEmpty())
            .filter(s -> !s.contains(":"))
            .collect(Collectors.toList());

        final Map<Integer, Rule> ruleMap = rules.stream()
            .collect(Collectors.toMap(r -> r.index, r -> r));

        final Rule rule0 = ruleMap.get(0);
        final Set<String> allCombinations = rule0.possibleCombinations(ruleMap, messages);

        final long result = messages.stream()
            .filter(allCombinations::contains)
            .count();

        out.println("Result: " + result);
    }

    private static class Rule {
        private final int index;
        private final List<Integer> rules1 = new ArrayList<>();
        private final List<Integer> rules2 = new ArrayList<>();
        private final String textRule;
        private boolean recursionAlert = false;
        int recursionCount = 0;
        boolean alreadyCalculated = false;

        public Rule(String text) {
            this.index = Integer.parseInt(text.split(":")[0]);
            final String rule = text.split(":")[1];
            if (rule.contains("\"")) {
                textRule = rule.split("\"")[1];
            } else {
                textRule = null;
                if (rule.contains("|")) {
                    addRules(rule.split("\\|")[0], rules1);
                    addRules(rule.split("\\|")[1], rules2);
                } else {
                    addRules(rule, rules1);
                }
            }
        }

        private void addRules(String rule, List<Integer> rules) {
            String[] indexes = rule.split(" ");
            for (int i = 1; i < indexes.length; i++) {
                rules.add(Integer.valueOf(indexes[i]));
            }
        }

        public Set<String> possibleCombinations(Map<Integer, Rule> ruleMap, List<String> messages) {
            if (alreadyCalculated) {
                return knownCombinations.get(index);
            }
            if (index == 8 || index == 11) {
                recursionAlert = true;
                recursionCount++;
            }
            if (textRule != null) {
                return Set.of(textRule);
            }
            final Set<String> allCombinations = new HashSet<>();
            if (!recursionAlert || recursionCount < 7) {
                addCombinations(ruleMap, allCombinations, rules1, messages);
                addCombinations(ruleMap, allCombinations, rules2, messages);
            } else {
                recursionCount = 0;
                recursionAlert = false;
            }
            if (index == 31 || index == 42) {
                knownCombinations.put(index, allCombinations);
                alreadyCalculated = true;
            }
            return allCombinations;
        }

        private void addCombinations(Map<Integer, Rule> ruleMap, Set<String> allCombinations, List<Integer> rules, List<String> messages) {
            if (!rules.isEmpty()) {
                Set<String> set = ruleMap.get(rules.get(0))
                    .possibleCombinations(ruleMap, messages)
                    .stream()
                    .filter(s -> messages.stream().anyMatch(m -> m.contains(s)))
                    .collect(Collectors.toSet());
                if (rules.size() > 1) {
                    final Set<String> set2 = ruleMap.get(rules.get(1)).possibleCombinations(ruleMap, messages);
                    set = set.stream()
                        .flatMap(s -> set2.stream().map(s2 -> s + s2))
                        .filter(s -> messages.stream().anyMatch(m -> m.contains(s)))
                        .collect(Collectors.toSet());
                } else {
                    allCombinations.addAll(set);
                    return;
                }
                if (rules.size() > 2) {
                    final Set<String> set3 = ruleMap.get(rules.get(2)).possibleCombinations(ruleMap, messages);
                    set = set.stream()
                        .flatMap(s -> set3.stream().map(s2 -> s + s2))
                        .filter(s -> messages.stream().anyMatch(m -> m.contains(s)))
                        .collect(Collectors.toSet());
                }
                allCombinations.addAll(set);
            }
        }
    }
}