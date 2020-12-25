package day25;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day25 {

    public static final long DIVIDER = 20201227;

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day25/input.txt"))
            .collect(Collectors.toList());
        out.println("Lines: " + items);

        task(items);
    }

    private static void task(List<String> keys) {
        long publicKey1 = Long.parseLong(keys.get(0));
        final long loopSize1 = getLoopSize(publicKey1);
        long publicKey2 = Long.parseLong(keys.get(1));
        final long loopSize2 = getLoopSize(publicKey2);
        out.println();
        out.println("loopSize1: " + loopSize1);
        out.println("loopSize2: " + loopSize2);
        out.println();

        long number = 1;
        for (int j = 1; j <= loopSize2; j++) {
            number = number * publicKey1;
            number = number % DIVIDER;
        }

        out.println("encryption key: " + number);
    }

    private static long getLoopSize(long publicKey) {
        long number = 1;
        long subjectNumber = 7;
        long i = 0;
        while (number != publicKey) {
            i++;
            number = number * subjectNumber;
            number = number % DIVIDER;
        }
        return i;
    }
}