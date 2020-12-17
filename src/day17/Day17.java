package day17;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day17 {

    public static void main(String[] args) throws IOException {
        task1();
        task2();
    }

    private static void task1() throws IOException {
        final List<String> items = lines(of("src/day17/input.txt"))
            .collect(Collectors.toList());
        out.println(items);

        Map<Coordinates, Boolean> cells = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            char[] line = items.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                cells.put(new Coordinates(j, i, 0), line[j] == '#');
            }
        }

        for (int i = 1; i <= 6; i++) {
            cells = iterate3D(cells);
        }

        final long result = cells.values()
            .stream()
            .filter(v -> v)
            .count();

        out.println("Result: " + result);
    }

    private static Map<Coordinates, Boolean> iterate3D(Map<Coordinates, Boolean> inputCells) {
        final Map<Coordinates, Boolean> outputCells = new HashMap<>();
        final int xMin = getMin(inputCells, p -> p.x);
        final int xMax = getMax(inputCells, p -> p.x);
        final int yMin = getMin(inputCells, p -> p.y);
        final int yMax = getMax(inputCells, p -> p.y);
        final int zMin = getMin(inputCells, p -> p.z);
        final int zMax = getMax(inputCells, p -> p.z);
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    outputCells.put(new Coordinates(x, y, z), getCellState3D(inputCells, x, y, z));
                }
            }
        }
        return outputCells;
    }

    private static int getMin(Map<Coordinates, Boolean> inputCells, ToIntFunction<Coordinates> p) {
        return inputCells.keySet().stream()
            .map(p::applyAsInt)
            .min(Integer::compare).orElseThrow() - 1;
    }

    private static int getMax(Map<Coordinates, Boolean> inputCells, ToIntFunction<Coordinates> p) {
        return inputCells.keySet().stream()
            .map(p::applyAsInt)
            .max(Integer::compare).orElseThrow() + 1;
    }

    private static Boolean getCellState3D(Map<Coordinates, Boolean> cells, int xi, int yi, int zi) {
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (z == 0 && y == 0 && x == 0) {
                        continue;
                    }
                    if (cells.getOrDefault(new Coordinates(xi + x, yi + y, zi + z), false)) {
                        count++;
                    }
                }
            }
        }
        boolean cellStatus = cells.getOrDefault(new Coordinates(xi, yi, zi), false);
        if (cellStatus && (count == 2 || count == 3)) {
            return true;
        }
        return !cellStatus && (count == 3);
    }

    private static void task2() throws IOException {
        final List<String> items = lines(of("src/day17/input.txt"))
            .collect(Collectors.toList());
        out.println(items);

        Map<Coordinates, Boolean> cells = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            char[] line = items.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                cells.put(new Coordinates(j, i, 0, 0), line[j] == '#');
            }
        }

        for (int i = 1; i <= 6; i++) {
            cells = iterate4D(cells);
        }

        final long result = cells.values()
            .stream()
            .filter(v -> v)
            .count();

        out.println("Result: " + result);
    }

    private static Map<Coordinates, Boolean> iterate4D(Map<Coordinates, Boolean> inputCells) {
        Map<Coordinates, Boolean> outputCells = new HashMap<>();
        final int xMin = getMin(inputCells, p -> p.x);
        final int xMax = getMax(inputCells, p -> p.x);
        final int yMin = getMin(inputCells, p -> p.y);
        final int yMax = getMax(inputCells, p -> p.y);
        final int zMin = getMin(inputCells, p -> p.z);
        final int zMax = getMax(inputCells, p -> p.z);
        final int wMin = getMin(inputCells, p -> p.w);
        final int wMax = getMax(inputCells, p -> p.w);
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    for (int w = wMin; w <= wMax; w++) {
                        outputCells.put(new Coordinates(x, y, z, w), getCellState4D(inputCells, x, y, z, w));
                    }
                }
            }
        }
        return outputCells;
    }

    private static Boolean getCellState4D(Map<Coordinates, Boolean> cells, int xi, int yi, int zi, int wi) {
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    for (int w = -1; w <= 1; w++) {
                        if (z == 0 && y == 0 && x == 0 && w == 0) {
                            continue;
                        }
                        if (cells.getOrDefault(new Coordinates(xi + x, yi + y, zi + z, wi + w), false)) {
                            count++;
                        }
                    }
                }
            }
        }
        boolean cellStatus = cells.getOrDefault(new Coordinates(xi, yi, zi, wi), false);
        if (cellStatus && (count == 2 || count == 3)) {
            return true;
        }
        return !cellStatus && (count == 3);
    }

    private static class Coordinates {
        private final int x;
        private final int y;
        private final int z;
        private final int w;

        public Coordinates(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = 0;
        }

        public Coordinates(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + ", " + z + ", " + w + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Coordinates that = (Coordinates) o;

            if (x != that.x) {
                return false;
            }
            if (y != that.y) {
                return false;
            }
            if (z != that.z) {
                return false;
            }
            return w == that.w;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            result = 31 * result + w;
            return result;
        }
    }
}