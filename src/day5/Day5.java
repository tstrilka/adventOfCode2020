package day5;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day5 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day5/input.txt"))
            .collect(Collectors.toList());

        out.println();
        items.forEach(pass -> out.println("Board pass: " + pass));
        out.println();

        final Optional<Integer> max = items.stream()
            .map(Day5::decode)
            .max(Comparator.comparingInt(o -> o));

        out.println();
        out.println("Max: " + max);
        out.println();

        final List<Integer> passes = items.stream()
            .map(Day5::decode)
            .sorted()
            .collect(Collectors.toList());
        out.println();
        passes.forEach(pass -> out.println("Board pass: " + pass));
        out.println();

        for (int i = 0; i < passes.size(); i++) {
            int d = 40;
            if (!passes.get(i).equals(i+d)) {
                out.println();
                out.println("missing " + (i+d));
                return;
            }
        }
    }

    public static Integer decode(String pass) {

        String rowPass = pass.substring(0, 7);
        rowPass = rowPass.replace('F', '0');
        rowPass = rowPass.replace('B', '1');
        final int row = Integer.parseInt(rowPass, 2);
        out.println(rowPass + " -> " + row);

        String colPass = pass.substring(7, 10);
        colPass = colPass.replace('L', '0');
        colPass = colPass.replace('R', '1');
        final int col = Integer.parseInt(colPass, 2);
        out.println(colPass + " -> " + col);

        final Integer result = row * 8 + col;
        out.println(pass + " -> " + result);
        return result;
    }
}
