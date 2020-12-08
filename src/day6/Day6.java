package day6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day6 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day6/input.txt"))
            .collect(Collectors.toList());

        out.println();
        items.forEach(answer -> out.println("Answer: " + answer));
        out.println();

        task1(items);
        task2(items);
    }

    private static void task1(List<String> items) {
        final AtomicBoolean newGroup = new AtomicBoolean(true);
        final Set<String> groupAnswers = new HashSet<>();
        final List<Set<String>> answers = new ArrayList<>();

        items.forEach(line -> {
            if (line.isBlank()) {
                System.out.println();
                newGroup.set(true);
                answers.add(new HashSet<>(groupAnswers));
                groupAnswers.clear();
            } else {
                newGroup.set(false);
                final Set<String> personAnswers = line.chars()
                    .mapToObj(Character::toString)
                    .collect(Collectors.toSet());
                groupAnswers.addAll(personAnswers);
            }
        });

        System.out.println();
        System.out.println("Group Answers: " + answers);
        System.out.println();

        int count = answers.stream()
            .map(Set::size)
            .mapToInt(i -> i)
            .sum();
        System.out.println();
        System.out.println("Count: " + count);
        System.out.println();
    }

    private static void task2(List<String> items) {
        final AtomicBoolean newGroup = new AtomicBoolean(true);
        final Set<String> groupAnswers = new HashSet<>();
        final List<Set<String>> answers = new ArrayList<>();

        items.forEach(line -> {
            if (line.isBlank()) {
                System.out.println();
                newGroup.set(true);
                answers.add(new HashSet<>(groupAnswers));
                groupAnswers.clear();
            } else {
                final Set<String> personAnswers = line.chars()
                    .mapToObj(Character::toString)
                    .collect(Collectors.toSet());
                final Set<String> personAnswersInvers = Stream.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")
                    .filter(c -> !personAnswers.contains(c))
                    .collect(Collectors.toSet());
                System.out.println();
                System.out.println("Group Answers: " + groupAnswers);
                System.out.println("Person Answers: " + personAnswers);
                System.out.println("Person Answers Inverted: " + personAnswersInvers);
                if (newGroup.get()) {
                    groupAnswers.addAll(personAnswers);
                }
                newGroup.set(false);
                groupAnswers.removeAll(personAnswersInvers);
                System.out.println("Group Answers: " + groupAnswers);
                System.out.println();
            }
        });

        System.out.println();
        System.out.println("Group Answers: " + answers);
        System.out.println();

        int count = answers.stream()
            .map(Set::size)
            .mapToInt(i -> i)
            .sum();
        System.out.println();
        System.out.println("Count: " + count);
        System.out.println();
    }
}
