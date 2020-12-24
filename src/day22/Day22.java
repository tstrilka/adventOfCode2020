package day22;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day22 {

    public static void main(String[] args) throws IOException {
        task1("src/day22/input.txt");
        task2("src/day22/input.txt");
    }

    private static void task1(String path) throws IOException {
        final LinkedList<Integer> deck1 = new LinkedList<>();
        final LinkedList<Integer> deck2 = new LinkedList<>();
        fillDecks(path, deck1, deck2);
        List<Integer> deck = deck2;

        if (playGameAndIsWinner1(deck1, deck2, false)) {
            deck = deck1;
        }

        printResult(deck, "Task 1: ");
    }

    private static void task2(String path) throws IOException {
        final LinkedList<Integer> deck1 = new LinkedList<>();
        final LinkedList<Integer> deck2 = new LinkedList<>();
        fillDecks(path, deck1, deck2);
        List<Integer> deck = deck2;

        if (playGameAndIsWinner1(deck1, deck2, true)) {
            deck = deck1;
        }

        printResult(deck, "Task 2: ");
    }

    private static void fillDecks(String path, List<Integer> deck1, LinkedList<Integer> deck2) throws IOException {
        final List<String> items = lines(of(path))
            .collect(Collectors.toList());
        out.println("Lines: " + items);

        List<Integer> deck = deck1;
        for (final String line : items) {
            if (line.isBlank()) {
                deck = deck2;
                continue;
            }
            if (line.contains("Player")) {
                continue;
            }
            deck.add(Integer.parseInt(line));
        }
    }

    private static void printResult(List<Integer> deck, String s) {
        long result = 0;
        for (int i = 0; i < deck.size(); i++) {
            result += deck.get((deck.size() - i - 1)) * (i + 1);
        }

        out.println();
        out.println(s + result);
        out.println();
    }

    private static boolean playGameAndIsWinner1(LinkedList<Integer> deck1, LinkedList<Integer> deck2, boolean withSubGames) {
        boolean recursion = false;
        final Set<Long> deck1HashCode = new HashSet<>();
        final Set<Long> deck2HashCode = new HashSet<>();
        while (!recursion && (!deck1.isEmpty() && !deck2.isEmpty())) {
            deck1HashCode.add(getHashCode(deck1));
            deck2HashCode.add(getHashCode(deck2));
            final Integer card1 = deck1.pop();
            final Integer card2 = deck2.pop();
            if (withSubGames && card1 <= deck1.size() && card2 <= deck2.size()) {
                final LinkedList<Integer> deck1Sublist = new LinkedList<>(deck1.subList(0, card1));
                final LinkedList<Integer> deck2Sublist = new LinkedList<>(deck2.subList(0, card2));
                if (playGameAndIsWinner1(deck1Sublist, deck2Sublist, true)) {
                    deck1.add(card1);
                    deck1.add(card2);
                } else {
                    deck2.add(card2);
                    deck2.add(card1);
                }
            } else {
                if (card1 > card2) {
                    deck1.add(card1);
                    deck1.add(card2);
                } else {
                    deck2.add(card2);
                    deck2.add(card1);
                }
            }
            if (deck1HashCode.contains(getHashCode(deck1)) && deck2HashCode.contains(getHashCode(deck2))) {
                recursion = true;
            }
        }
        return deck2.isEmpty() || recursion;
    }

    private static long getHashCode(List<Integer> list) {
        final long prime = 3;
        long result = 1;
        for (Integer i : list) {
            result = result * prime + (long) i.hashCode();
        }
        return result;
    }
}