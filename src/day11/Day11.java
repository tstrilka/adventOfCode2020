package day11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day11 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day11/input.txt"))
            .collect(Collectors.toList());

        task1(items);
        task2(items);
    }

    private static void task1(List<String> seats) {
        Plan plan = Plan.parse(seats);
        String oldPlan = "";
        while (!plan.toString().equals(oldPlan)) {
            oldPlan = plan.toString();
            plan = plan.iterate();
        }
        out.println();
        out.println("Result task 1: " + plan.occupiedCount());
    }

    private static void task2(List<String> seats) {
        Plan plan = Plan.parse(seats);
        String oldPlan = "";
        while (!plan.toString().equals(oldPlan)) {
            oldPlan = plan.toString();
            plan = plan.iterate2();
        }
        out.println();
        out.println("Result task 2: " + plan.occupiedCount());
    }

    private static class Plan {

        private final List<List<Boolean>> seatPlan;

        public Plan(List<String> seats) {
            this.seatPlan = new ArrayList<>();
            for (final String seat : seats) {
                this.seatPlan.add(seat.chars()
                    .mapToObj(ch -> {
                        if (ch == 'L') {
                            return Boolean.FALSE;
                        }
                        if (ch == '#') {
                            return Boolean.TRUE;
                        }
                        return null;
                    })
                    .collect(Collectors.toList()));
            }
        }

        public Plan(List<List<Boolean>> map, int i) {
            this.seatPlan = map;
        }

        public static Plan parse(List<String> seats) {
            return new Plan(seats);
        }

        public Plan iterate() {
            final List<List<Boolean>> map = new ArrayList<>();
            for (int i = 0; i < seatPlan.size(); i++) {
                map.add(new ArrayList<>());
                for (int j = 0; j < seatPlan.get(i).size(); j++) {
                    final Boolean seat = seatPlan.get(i).get(j);
                    final Boolean value;
                    if (seat != null) {
                        if (seat) {
                            int count = 0;
                            if (i > 0 && j > 0 && seatPlan.get(i - 1).get(j - 1) != null && seatPlan.get(i - 1).get(j - 1)) {
                                count++;
                            }
                            if (j > 0 && seatPlan.get(i).get(j - 1) != null && seatPlan.get(i).get(j - 1)) {
                                count++;
                            }
                            if (i < seatPlan.size() - 1 && j > 0 && seatPlan.get(i + 1).get(j - 1) != null && seatPlan.get(i + 1).get(j - 1)) {
                                count++;
                            }
                            if (i > 0 && seatPlan.get(i - 1).get(j) != null && seatPlan.get(i - 1).get(j)) {
                                count++;
                            }
                            if (i < seatPlan.size() - 1 && seatPlan.get(i + 1).get(j) != null && seatPlan.get(i + 1).get(j)) {
                                count++;
                            }
                            if (i > 0 && j < seatPlan.get(i).size() - 1 && seatPlan.get(i - 1).get(j + 1) != null && seatPlan.get(i - 1).get(j + 1)) {
                                count++;
                            }
                            if (j < seatPlan.get(i).size() - 1 && seatPlan.get(i).get(j + 1) != null && seatPlan.get(i).get(j + 1)) {
                                count++;
                            }
                            if (i < seatPlan.size() - 1 && j < seatPlan.get(i).size() - 1 && seatPlan.get(i + 1).get(j + 1) != null && seatPlan.get(i + 1).get(j + 1)) {
                                count++;
                            }
                            value = count < 4;
                        } else {
                            value = !(i > 0 && j > 0 && seatPlan.get(i - 1).get(j - 1) != null && seatPlan.get(i - 1).get(j - 1))
                                && !(j > 0 && seatPlan.get(i).get(j - 1) != null && seatPlan.get(i).get(j - 1))
                                && !(i < seatPlan.size() - 1 && j > 0 && seatPlan.get(i + 1).get(j - 1) != null && seatPlan.get(i + 1).get(j - 1))
                                && !(i > 0 && seatPlan.get(i - 1).get(j) != null && seatPlan.get(i - 1).get(j))
                                && !(i < seatPlan.size() - 1 && seatPlan.get(i + 1).get(j) != null && seatPlan.get(i + 1).get(j))
                                && !(i > 0 && j < seatPlan.get(i).size() - 1 && seatPlan.get(i - 1).get(j + 1) != null && seatPlan.get(i - 1).get(j + 1))
                                && !(j < seatPlan.get(i).size() - 1 && seatPlan.get(i).get(j + 1) != null && seatPlan.get(i).get(j + 1))
                                && !(i < seatPlan.size() - 1 && j < seatPlan.get(i).size() - 1 && seatPlan.get(i + 1).get(j + 1) != null && seatPlan.get(i + 1).get(j + 1));
                        }
                    } else {
                        value = null;
                    }
                    map.get(i).add(value);
                }
            }
            return new Plan(map, 0);
        }

        public Plan iterate2() {
            final List<List<Boolean>> map = new ArrayList<>();
            for (int i = 0; i < seatPlan.size(); i++) {
                map.add(new ArrayList<>());
                for (int j = 0; j < seatPlan.get(i).size(); j++) {
                    final Boolean seat = seatPlan.get(i).get(j);
                    final Boolean value;
                    if (seat != null) {
                        if (seat) {
                            int count = 0;
                            if (isOccupiedLeftDown(i, j)) {
                                count++;
                            }
                            if (isOccupiedDown(i, j)) {
                                count++;
                            }
                            if (isOccupiedRightDown(i, j)) {
                                count++;
                            }
                            if (isOccupiedLeft(i, j)) {
                                count++;
                            }
                            if (isOccupiedRight(i, j)) {
                                count++;
                            }
                            if (isOccupiedLeftUp(i, j)) {
                                count++;
                            }
                            if (isOccupiedUp(i, j)) {
                                count++;
                            }
                            if (isOccupiedRightUp(i, j)) {
                                count++;
                            }
                            value = count < 5;
                        } else {
                            value = !(isOccupiedLeftDown(i, j))
                                && !(isOccupiedDown(i, j))
                                && !(isOccupiedRightDown(i, j))
                                && !(isOccupiedLeft(i, j))
                                && !(isOccupiedRight(i, j))
                                && !(isOccupiedLeftUp(i, j))
                                && !(isOccupiedUp(i, j))
                                && !(isOccupiedRightUp(i, j));
                        }
                    } else {
                        value = null;
                    }
                    map.get(i).add(value);
                }
            }
            return new Plan(map, 0);
        }

        private boolean isOccupiedRightUp(int i, int j) {
            return i < seatPlan.size() - 1 && j < seatPlan.get(i).size() - 1 && (seatPlan.get(i + 1).get(j + 1) != null ? seatPlan.get(i + 1).get(j + 1) : isOccupiedRightUp(i + 1, j + 1));
        }

        private boolean isOccupiedUp(int i, int j) {
            return j < seatPlan.get(i).size() - 1 && (seatPlan.get(i).get(j + 1) != null ? seatPlan.get(i).get(j + 1) : isOccupiedUp(i, j + 1));
        }

        private boolean isOccupiedLeftUp(int i, int j) {
            return i > 0 && j < seatPlan.get(i).size() - 1 && (seatPlan.get(i - 1).get(j + 1) != null ? seatPlan.get(i - 1).get(j + 1) : isOccupiedLeftUp(i - 1, j + 1));
        }

        private boolean isOccupiedRight(int i, int j) {
            return i < seatPlan.size() - 1 && (seatPlan.get(i + 1).get(j) != null ? seatPlan.get(i + 1).get(j) : isOccupiedRight(i + 1, j));
        }

        private boolean isOccupiedLeft(int i, int j) {
            return i > 0 && (seatPlan.get(i - 1).get(j) != null ? seatPlan.get(i - 1).get(j) : isOccupiedLeft(i - 1, j));
        }

        private boolean isOccupiedRightDown(int i, int j) {
            return i < seatPlan.size() - 1 && j > 0 && (seatPlan.get(i + 1).get(j - 1) != null ? seatPlan.get(i + 1).get(j - 1) : isOccupiedRightDown(i + 1, j - 1));
        }

        private boolean isOccupiedDown(int i, int j) {
            return j > 0 && (seatPlan.get(i).get(j - 1) != null ? seatPlan.get(i).get(j - 1) : isOccupiedDown(i, j - 1));
        }

        private boolean isOccupiedLeftDown(int i, int j) {
            return i > 0 && j > 0 && (seatPlan.get(i - 1).get(j - 1) != null ? seatPlan.get(i - 1).get(j - 1) : isOccupiedLeftDown(i - 1, j - 1));
        }

        @Override
        public String toString() {
            return "Plan:\n"
                + seatPlan.stream()
                .map(l -> l.stream().map(s -> s == null ? "." : s ? "#" : "L").collect(Collectors.joining()) + "\n")
                .collect(Collectors.joining());
        }

        public long occupiedCount() {
            return seatPlan.stream()
                .flatMap(Collection::stream)
                .filter(s -> s != null && s)
                .count();
        }
    }
}