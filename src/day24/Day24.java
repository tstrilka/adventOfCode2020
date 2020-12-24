package day24;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day24 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day24/input.txt"))
            .collect(Collectors.toList());
        out.println("Lines: " + items);

        task(items);
    }

    private static void task(List<String> items) {
        Tile startTile = new Tile(0, 0);
        final Set<Tile> tiles = new HashSet<>();

        final List<List<String>> instructions = items.stream()
            .map(line -> line.chars()
                .mapToObj(Character::toString)
                .collect(new InstructionCollector())
            )
            .collect(Collectors.toList());

        final List<Tile> destinationTiles = instructions.stream()
            .map(list -> new Tile(startTile.e, startTile.se).move(list))
            .collect(Collectors.toList());

        destinationTiles.forEach(tile -> {
            if (tiles.contains(tile)) {
                findTile(tiles, tile).flip();
            } else {
                tiles.add(tile);
                tile.flip();
            }
        });

        out.println("Task 1: " + tiles.stream()
            .filter(t -> t.side.equals("B"))
            .count());

        Set<Tile> blackTilesAfterStep = tiles.stream()
            .filter(t -> t.side.equals("B"))
            .collect(Collectors.toSet());
        for (int i = 0; i < 100; i++) {
            blackTilesAfterStep = flipAllTiles(blackTilesAfterStep);
        }
        out.println("Task 2: " + blackTilesAfterStep.size());
    }

    private static Tile findTile(Set<Tile> tiles, Tile tile) {
        return tiles.stream()
            .filter(t -> t.equals(tile))
            .findFirst()
            .orElseThrow();
    }

    private static Set<Tile> flipAllTiles(Set<Tile> tiles) {
        HashSet<Tile> flippedTiles = new HashSet<>();
        tiles.forEach(t -> {
            final List<Tile> tilesToTest = new ArrayList<>();
            final Tile middleTile = t.copy();
            tilesToTest.add(middleTile);
            tilesToTest.add(findOrCreateTileCopy(tiles, middleTile.e + 1, middleTile.se));
            tilesToTest.add(findOrCreateTileCopy(tiles, middleTile.e - 1, middleTile.se));
            tilesToTest.add(findOrCreateTileCopy(tiles, middleTile.e, middleTile.se + 1));
            tilesToTest.add(findOrCreateTileCopy(tiles, middleTile.e, middleTile.se - 1));
            tilesToTest.add(findOrCreateTileCopy(tiles, middleTile.e + 1, middleTile.se - 1));
            tilesToTest.add(findOrCreateTileCopy(tiles, middleTile.e - 1, middleTile.se + 1));
            flippedTiles.addAll(flipTilesToTest(tilesToTest, tiles));
        });
        return flippedTiles;
    }

    private static Set<Tile> flipTilesToTest(List<Tile> tilesToTest, Set<Tile> tiles) {
        return tilesToTest.stream()
            .map(t -> t.applyConditions(tiles))
            .filter(t -> t.side.equals("B"))
            .collect(Collectors.toSet());
    }

    private static Tile findOrCreateTileCopy(Set<Tile> tiles, int e, int se) {
        final Tile tile = new Tile(e, se);
        return tiles.contains(tile) ? findTile(tiles, tile).copy() : tile.copy();
    }

    private static class Tile {
        private int e;
        private int se;
        private String side;

        public Tile(int e, int se) {
            this.e = e;
            this.se = se;
            this.side = "W";
        }

        public Tile(int e, int se, String side) {
            this.e = e;
            this.se = se;
            this.side = side;
        }

        private Tile flip() {
            this.side = side.equals("W") ? "B" : "W";
            return this;
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
            return se == tile.se;
        }

        @Override
        public int hashCode() {
            int result = e;
            result = 31 * result + se;
            return result;
        }

        @Override
        public String toString() {
            return "e=" + e + ",se=" + se + " " + side;
        }

        public Tile move(List<String> instructions) {
            instructions.forEach(i -> {
                if (i.equals("e")) {
                    this.e++;
                } else if (i.equals("w")) {
                    this.e--;
                } else if (i.equals("se")) {
                    this.se++;
                } else if (i.equals("nw")) {
                    this.se--;
                } else if (i.equals("sw")) {
                    this.se++;
                    this.e--;
                } else if (i.equals("ne")) {
                    this.se--;
                    this.e++;
                }
            });
            return this;
        }

        public Tile copy() {
            return new Tile(this.e, this.se, this.side);
        }

        public Tile applyConditions(Set<Tile> tiles) {
            int blackCount = 0;
            if (isTileSideBlack(tiles, new Tile(this.e + 1, this.se))) {
                blackCount++;
            }
            if (isTileSideBlack(tiles, new Tile(this.e - 1, this.se))) {
                blackCount++;
            }
            if (isTileSideBlack(tiles, new Tile(this.e, this.se + 1))) {
                blackCount++;
            }
            if (isTileSideBlack(tiles, new Tile(this.e, this.se - 1))) {
                blackCount++;
            }
            if (isTileSideBlack(tiles, new Tile(this.e + 1, this.se - 1))) {
                blackCount++;
            }
            if (isTileSideBlack(tiles, new Tile(this.e - 1, this.se + 1))) {
                blackCount++;
            }
            if ((this.side.equals("B") && (blackCount == 0 || blackCount > 2)) || (this.side.equals("W") && blackCount == 2)) {
                return this.flip();
            }
            return this;
        }

        private boolean isTileSideBlack(Set<Tile> tiles, Tile tile) {
            if (tiles.contains(tile)) {
                return findTile(tiles, tile).side.equals("B");
            }
            return false;
        }
    }

    private static class InstructionCollector implements Collector<String, List<String>, List<String>> {
        @Override
        public Supplier<List<String>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<String>, String> accumulator() {
            return (list, s) -> {
                if (list.isEmpty()) {
                    list.add(s);
                } else if (s.equals("e") && list.get(list.size() - 1).equals("s")) {
                    list.set(list.size() - 1, "se");
                } else if (s.equals("w") && list.get(list.size() - 1).equals("s")) {
                    list.set(list.size() - 1, "sw");
                } else if (s.equals("e") && list.get(list.size() - 1).equals("n")) {
                    list.set(list.size() - 1, "ne");
                } else if (s.equals("w") && list.get(list.size() - 1).equals("n")) {
                    list.set(list.size() - 1, "nw");
                } else {
                    list.add(s);
                }
            };
        }

        @Override
        public BinaryOperator<List<String>> combiner() {
            return (list, list2) -> {
                list.addAll(list2);
                return list;
            };
        }

        @Override
        public Function<List<String>, List<String>> finisher() {
            return null;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.IDENTITY_FINISH);
        }
    }
}