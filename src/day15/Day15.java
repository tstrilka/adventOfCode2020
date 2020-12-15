package day15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day15 {

    public static void main(String[] args) throws IOException {
        task(2020);
        task(30000000);
    }

    private static void task(int position) throws IOException {
        final List<Integer> numbers = Arrays.stream(lines(of("src/day15/input.txt"))
            .collect(Collectors.toList())
            .get(0)
            .split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        out.println(numbers);

        final Map<Integer, List<Integer>> numberToIndexes = new HashMap<>();
        for (int i = 0; i < numbers.size(); i++) {
            numberToIndexes.put(numbers.get(i), new ArrayList<>(List.of(i + 1)));
        }
        int number = numbers.get(numbers.size() - 1);
        int index = numbers.size();
        while (index < position) {
            index++;
            final List<Integer> lastNumberIndexes = numberToIndexes.get(number);
            if (lastNumberIndexes == null || lastNumberIndexes.size() == 1) {
                number = 0;
            } else {
                number = lastNumberIndexes.get(1) - lastNumberIndexes.get(0);
            }
            final List<Integer> currentNumberIndexes = numberToIndexes.get(number);
            if (currentNumberIndexes == null) {
                numberToIndexes.put(number, new ArrayList<>(List.of(index)));
            } else if (currentNumberIndexes.size() == 1) {
                numberToIndexes.put(number, new ArrayList<>(List.of(currentNumberIndexes.get(0), index)));
            } else {
                currentNumberIndexes.set(0, currentNumberIndexes.get(1));
                currentNumberIndexes.set(1, index);
            }
        }
        out.println("last number: " + number);
    }
}