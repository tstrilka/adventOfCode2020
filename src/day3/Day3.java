package day3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day3 {

    public static void main(String[] args) throws IOException {
        final List<Row> items = lines(of("src/day3/input.txt"))
            .map(Row::parse)
            .collect(Collectors.toList());

        printRows(items);
        final long count1 = getCount(items, 1, 1);
        final long count3 = getCount(items, 3, 1);
        final long count5 = getCount(items, 5, 1);
        final long count7 = getCount(items, 7, 1);

        final long count1_2 = getCount(items, 1, 2);
        out.println();
        out.println("Count for (1, 1): " + count1);
        out.println("Count for (3, 1): " + count3);
        out.println("Count for (5, 1): " + count5);
        out.println("Count for (7, 1): " + count7);
        out.println("Count for (1, 2): " + count1_2);
        out.println();
        out.println("Multiply: " + count1 * count3 * count5 * count7 * count1_2);
    }

    private static void printRows(List<Row> items) {
        items.stream()
            .map(item -> "row: " + item)
            .forEach(out::println);
    }

    private static long getCount(List<Row> items, int right, int down) {
        long count = 0;
        for (int i = 0; i < items.size(); i = i + down) {
            count += items.get(i).isColliding(right, i / down) ? 1 : 0;
        }
        return count;
    }

    private static class Row {

        private final List<Boolean> row;

        private Row(List<Boolean> row) {
            this.row = row;
        }

        private List<Boolean> getRow() {
            return new ArrayList<>(row);
        }

        public static Row parse(String s) {

            final List<Boolean> row = s.chars()
                .mapToObj(ch -> ch == '#')
                .collect(Collectors.toList());

            return new Row(row);
        }

        @Override
        public String toString() {
            return row.stream()
                .map(ch -> ch ? "#" : ".")
                .collect(Collectors.joining());
        }

        public boolean isColliding(int right, int index) {
            int reducedIndex = index * right % row.size();
            return row.get(reducedIndex);
        }
    }
}
