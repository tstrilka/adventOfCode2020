package day24;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day23 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day24/test-input.txt"))
            .collect(Collectors.toList());
        out.println("Lines: " + items);

        task(items);
    }

    private static void task(List<String> items) {
        final Tile startTile = new Tile(0, 0, 0);
        final Set<Tile> tiles = new HashSet<>();
        tiles.add(startTile);
        out.println("Tiles: " + tiles);

        final List<ArrayList<Object>> instructions = items.stream()
            .map(line -> line.chars()
                .mapToObj(Character::toString)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll)
            )
            .collect(Collectors.toList());

        out.println(instructions);
    }

    private static class Tile {
        private final int e;
        private final int se;
        private final int sw;
        private String side;

        public Tile(int e, int se, int sw) {
            this.e = e;
            this.se = se;
            this.sw = sw;
            this.side = "W";
        }

        private void flip() {
            this.side = side.equals("W") ? "B" : "W";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Tile tile = (Tile) o;

            if (e != tile.e) {
                return false;
            }
            if (se != tile.se) {
                return false;
            }
            return sw == tile.sw;
        }

        @Override
        public int hashCode() {
            int result = e;
            result = 31 * result + se;
            result = 31 * result + sw;
            return result;
        }

        @Override
        public String toString() {
            return "e=" + e + ",se=" + se + ",sw=" + se + " " + side;
        }
    }
}