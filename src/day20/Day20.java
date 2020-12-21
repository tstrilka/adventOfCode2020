package day20;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day20 {

    public static void main(String[] args) throws IOException {
        task1("src/day20/input.txt");
        task2("src/day20/input.txt");
    }

    private static void task2(String file) throws IOException {
        final Map<Integer, Tile> tiles = parseInputToTiles(file);

        final Tile cornerTile = tiles.values().stream()
            .filter(tile -> tile.isCorner(tiles))
            .findFirst()
            .orElseThrow();

        final List<Tile> tilesOrdered = new ArrayList<>(Collections.nCopies(tiles.size(), null));
        tilesOrdered.set(0, cornerTile);
        final int sideSize = Long.valueOf(round(sqrt(tiles.size()))).intValue();
        final List<String> oppositeSideCW = new ArrayList<>();

        for (int i = 0; i < sideSize - 1; i++) {
            if (i == 0) {
                final Tile tileInTest = tilesOrdered.get(0);
                final List<Tile> tilesMatched =
                    tileInTest.bordersCW.stream()
                        .map(border ->
                            tiles.values().stream()
                                .filter(tile -> tile.index != tileInTest.index)
                                .filter(tile -> tile.matchBorder(border))
                                .findFirst()
                                .orElse(null)
                        )
                        .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                tilesOrdered.set(1, tilesMatched.get(0));
                oppositeSideCW.add(tilesMatched.get(0).oppositeSideBorderCW(tileInTest));
                tilesOrdered.set(sideSize, tilesMatched.get(1));
                oppositeSideCW.add(tilesMatched.get(1).oppositeSideBorderCW(tileInTest));
            } else {
                final Tile tileInTest1 = tilesOrdered.get(i);
                final Tile tileMatched1 = tiles.values().stream()
                    .filter(tile -> tile.index != tileInTest1.index)
                    .filter(tile -> tile.matchBorder(oppositeSideCW.get(0)))
                    .findFirst()
                    .orElseThrow();
                tilesOrdered.set(i + 1, tileMatched1);
                oppositeSideCW.set(0, tileMatched1.oppositeSideBorderCW(tileInTest1));

                final Tile tileInTest2 = tilesOrdered.get(i * sideSize);
                final Tile tileMatched2 = tiles.values().stream()
                    .filter(tile -> tile.index != tileInTest2.index)
                    .filter(tile -> tile.matchBorder(oppositeSideCW.get(1)))
                    .findFirst()
                    .orElseThrow();
                tilesOrdered.set((i + 1) * sideSize, tileMatched2);
                oppositeSideCW.set(1, tileMatched2.oppositeSideBorderCW(tileInTest2));
            }
        }

        for (int i = 0; i < tiles.size(); i++) {
            if (i > sideSize && (i % sideSize != 0)) {
                final Tile tileInTest1 = tilesOrdered.get(i - 1);
                final Tile tileInTest2 = tilesOrdered.get(i - sideSize);
                final Set<Integer> placedTiles = tilesOrdered.stream()
                    .filter(Objects::nonNull)
                    .map(tile -> tile.index)
                    .collect(Collectors.toSet());
                final List<Tile> tilesMatched = tiles.values().stream()
                    .filter(tile -> !placedTiles.contains(tile.index))
                    .filter(tile -> tileInTest1.bordersCW.stream().anyMatch(tile::matchBorder))
                    .filter(tile -> tileInTest2.bordersCW.stream().anyMatch(tile::matchBorder))
                    .collect(Collectors.toList());
                if (tilesMatched.size() != 1) {
                    throw new RemoteException("error 2");
                }
                tilesOrdered.set(i, tilesMatched.get(0));
            }
        }

        for (int i = 0; i < tilesOrdered.size(); i++) {
            if (i == 0) {
                tilesOrdered.get(0).reorientCorner(tilesOrdered.get(1), tilesOrdered.get(sideSize));
            } else if (i < sideSize) {
                tilesOrdered.get(i).reorientTop(tilesOrdered.get(i - 1), tilesOrdered.get(i + sideSize));
            } else if (i % sideSize == 0) {
                tilesOrdered.get(i).reorientLeft(tilesOrdered.get(i - sideSize), tilesOrdered.get(i + 1));
            } else {
                tilesOrdered.get(i).reorient(tilesOrdered.get(i - sideSize), tilesOrdered.get(i - 1));
            }
        }

        final List<String> mergedMaps = new ArrayList<>();
        for (int i = 0; i < sideSize; i++) {
            for (int j = 0; j < sideSize; j++) {
                for (int k = 1; k < 9; k++) {
                    int indexInOrderedTiles = i * sideSize + j;
                    if (j == 0) {
                        mergedMaps.add(tilesOrdered.get(indexInOrderedTiles).lines.get(k).substring(1, 9));
                    } else {
                        int index = i * 8 + k - 1;
                        mergedMaps.set(index, mergedMaps.get(index) + tilesOrdered.get(indexInOrderedTiles).lines.get(k).substring(1, 9));
                    }
                }
            }
        }

        final List<String> monster = new ArrayList<>();
        monster.add("                  # ");
        monster.add("#    ##    ##    ###");
        monster.add(" #  #  #  #  #  #   ");

        final ArrayList<Long> counts = new ArrayList<>();
        counts.add(findMonster(mergedMaps, monster));
        counts.add(findMonster(flipHorizontally(mergedMaps), monster));
        counts.add(findMonster(rotate(mergedMaps, 1), monster));
        counts.add(findMonster(flipHorizontally(rotate(mergedMaps, 1)), monster));
        counts.add(findMonster(rotate(mergedMaps, 2), monster));
        counts.add(findMonster(flipHorizontally(rotate(mergedMaps, 2)), monster));
        counts.add(findMonster(rotate(mergedMaps, 3), monster));
        counts.add(findMonster(flipHorizontally(rotate(mergedMaps, 3)), monster));

        final long monsterCount = counts.stream()
            .max(Long::compareTo)
            .orElseThrow();

        final long totalObstacleCount = mergedMaps.stream()
            .flatMap(line -> line.chars()
                .mapToObj(Character::toString)
            )
            .filter(ch -> ch.equals("#"))
            .count();

        out.println("Obstacle Count: " + (totalObstacleCount - monsterCount * 15));
    }

    private static List<String> rotate(List<String> mergedMaps, int steps) {
        List<String> map = new ArrayList<>(mergedMaps);
        for (int i = 0; i < steps; i++) {
            map = rotateCW(map);
        }
        return map;
    }

    private static List<String> rotateCW(List<String> map) {
        final List<String> transformedLines = new ArrayList<>();
        for (int i = map.size() - 1; i >= 0; i--) {
            char[] chars = map.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (i == map.size() - 1) {
                    transformedLines.add(String.valueOf(chars[j]));
                } else {
                    transformedLines.set(j, transformedLines.get(j) + chars[j]);
                }
            }
        }
        return transformedLines;
    }


    private static long findMonster(List<String> mergedMaps, List<String> monster) {
        return generateRegions(mergedMaps, 20, 3).stream()
            .filter(region -> isMonster(region, monster))
            .count();
    }

    private static boolean isMonster(List<String> region, List<String> monster) {
        return monsterMatch(region, monster);
    }

    private static List<String> flipHorizontally(List<String> map) {
        return map.stream()
            .map(line -> new StringBuilder(line).reverse().toString())
            .collect(Collectors.toList());
    }

    private static boolean monsterMatch(List<String> region, List<String> monster) {
        for (int i = 0; i < region.size(); i++) {
            if (!lineMatch(region.get(i), monster.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean lineMatch(String region, String monster) {
        final char[] regionChars = region.toCharArray();
        final char[] monsterChars = monster.toCharArray();
        for (int i = 0; i < monsterChars.length; i++) {
            if (monsterChars[i] == '#' && regionChars[i] != '#') {
                return false;
            }
        }
        return true;
    }

    private static List<List<String>> generateRegions(List<String> mergedMaps, int length, int height) {
        final List<List<String>> regions = new ArrayList<>();
        for (int i = 0; i < mergedMaps.size() - height; i++) {
            for (int j = 0; j <= mergedMaps.get(0).length() - length; j++) {
                final List<String> region = new ArrayList<>();
                for (int k = 0; k < height; k++) {
                    final String row = mergedMaps.get(i + k);
                    region.add(row.substring(j, j + length));
                }
                regions.add(region);
            }
        }
        return regions;
    }

    private static Map<Integer, Tile> parseInputToTiles(String file) throws IOException {
        final List<String> items = lines(of(file))
            .collect(Collectors.toList());

        final Map<Integer, Tile> tiles = new HashMap<>();
        final AtomicInteger tileNumber = new AtomicInteger(0);
        items.forEach(line -> {
            if (line.contains(":")) {
                tileNumber.set(Integer.parseInt(line.substring(5, 9)));
                tiles.put(tileNumber.get(), new Tile(tileNumber.get()));
                return;
            }
            if (!line.isEmpty()) {
                tiles.get(tileNumber.get()).add(line);
            }
        });
        tiles.values().forEach(Tile::fillBorders);
        return tiles;
    }

    private static void task1(String file) throws IOException {
        final Map<Integer, Tile> tiles = parseInputToTiles(file);

        final List<Integer> cornerTiles = tiles.values().stream()
            .filter(tile -> tile.isCorner(tiles))
            .map(tile -> tile.index)
            .collect(Collectors.toList());

        out.println("Result 1: " + Long.valueOf(cornerTiles.get(0)) * cornerTiles.get(1) * cornerTiles.get(2) * cornerTiles.get(3));
    }

    private static class Tile {
        private final int index;
        private List<String> lines = new ArrayList<>();
        private final List<String> bordersCW = new ArrayList<>();
        private final List<String> bordersACW = new ArrayList<>();

        public Tile(int tileNumber) {
            this.index = tileNumber;
        }

        public void add(String line) {
            this.lines.add(line);
        }

        public void fillBorders() {
            bordersCW.add(lines.get(0));
            bordersACW.add(revert(bordersCW.get(0)));
            bordersCW.add(lines.stream()
                .map(line -> String.valueOf(line.charAt(9)))
                .collect(Collectors.joining())
            );
            bordersACW.add(revert(bordersCW.get(1)));
            bordersACW.add(lines.get(9));
            bordersCW.add(revert(bordersACW.get(2)));
            bordersACW.add(lines.stream()
                .map(line -> String.valueOf(line.charAt(0)))
                .collect(Collectors.joining())
            );
            bordersCW.add(revert(bordersACW.get(3)));
        }

        private String revert(String s) {
            return new StringBuilder(s).reverse().toString();
        }

        public boolean isCorner(Map<Integer, Tile> tiles) {
            final AtomicInteger count = new AtomicInteger(0);
            bordersCW.forEach(border -> {
                final boolean matching = tiles.values().stream()
                    .filter(tile -> tile.index != this.index)
                    .anyMatch(tile -> tile.matchBorder(border));
                if (matching) {
                    count.getAndAdd(1);
                }
            });
            return count.get() == 2;
        }

        private boolean matchBorder(String border) {
            return bordersACW.stream()
                .anyMatch(b -> b.equals(border))
                || bordersCW.stream()
                .anyMatch(b -> b.equals(border));
        }

        @Override
        public String toString() {
            return String.valueOf(index);
        }

        public String oppositeSideBorderCW(Tile tile) {
            if (tile.matchBorder(bordersCW.get(0))) {
                return bordersCW.get(2);
            }
            if (tile.matchBorder(bordersCW.get(1))) {
                return bordersCW.get(3);
            }
            if (tile.matchBorder(bordersCW.get(2))) {
                return bordersCW.get(0);
            }
            if (tile.matchBorder(bordersCW.get(3))) {
                return bordersCW.get(1);
            }
            throw new RuntimeException("error");
        }

        public void reorientCorner(Tile tileRight, Tile tileBottom) {
            int rightMatchIndex = -1;
            for (int i = 0; i < bordersCW.size(); i++) {
                if (tileRight.matchBorder(bordersCW.get(i))) {
                    rightMatchIndex = i;
                    break;
                }
            }
            if (rightMatchIndex == 0) {
                this.rotateCW();
            }
            if (rightMatchIndex == 2) {
                this.rotateACW();
            }
            if (rightMatchIndex == 3) {
                this.rotateCW();
                this.rotateCW();
            }
            if (!tileBottom.matchBorder(bordersCW.get(rightMatchIndex == 3 ? 0 : rightMatchIndex + 1))) {
                this.flipVertically();
            }
        }

        public void reorientTop(Tile tileLeft, Tile tileBottom) {
            int leftMatchIndex = -1;
            for (int i = 0; i < bordersCW.size(); i++) {
                if (tileLeft.matchBorder(bordersCW.get(i))) {
                    leftMatchIndex = i;
                    break;
                }
            }
            if (leftMatchIndex == 0) {
                this.rotateACW();
            }
            if (leftMatchIndex == 1) {
                this.rotateCW();
                this.rotateCW();
            }
            if (leftMatchIndex == 2) {
                this.rotateCW();
            }
            if (!tileBottom.matchBorder(bordersCW.get(leftMatchIndex == 0 ? 3 : leftMatchIndex - 1))) {
                this.flipVertically();
            }
        }

        public void reorientLeft(Tile tileTop, Tile tileRight) {
            int rightMatchIndex = -1;
            for (int i = 0; i < bordersCW.size(); i++) {
                if (tileRight.matchBorder(bordersCW.get(i))) {
                    rightMatchIndex = i;
                    break;
                }
            }
            if (rightMatchIndex == 0) {
                this.rotateCW();
            }
            if (rightMatchIndex == 2) {
                this.rotateACW();
            }
            if (rightMatchIndex == 3) {
                this.rotateCW();
                this.rotateCW();
            }
            if (!tileTop.matchBorder(bordersCW.get(rightMatchIndex == 0 ? 3 : rightMatchIndex - 1))) {
                this.flipVertically();
            }
        }

        public void reorient(Tile tileTop, Tile tileLeft) {
            int leftMatchIndex = -1;
            for (int i = 0; i < bordersCW.size(); i++) {
                if (tileLeft.matchBorder(bordersCW.get(i))) {
                    leftMatchIndex = i;
                    break;
                }
            }
            if (leftMatchIndex == 0) {
                this.rotateACW();
            }
            if (leftMatchIndex == 1) {
                this.rotateCW();
                this.rotateCW();
            }
            if (leftMatchIndex == 2) {
                this.rotateCW();
            }
            if (!tileTop.matchBorder(bordersCW.get(leftMatchIndex == 3 ? 0 : leftMatchIndex + 1))) {
                this.flipVertically();
            }
        }

        private void flipVertically() {
            Collections.reverse(lines);
        }

        private void rotateACW() {
            final List<String> transformedLines = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                char[] chars = lines.get(i).toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    if (i == 0) {
                        transformedLines.add(String.valueOf(chars[chars.length - 1 - j]));
                    } else {
                        transformedLines.set(j, transformedLines.get(j) + chars[chars.length - 1 - j]);
                    }
                }
            }
            lines = transformedLines;
        }

        private void rotateCW() {
            lines = Day20.rotateCW(lines);
        }

    }
}