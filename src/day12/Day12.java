package day12;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.Math.abs;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day12 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day12/input.txt"))
            .collect(Collectors.toList());

        final List<Instruction> instructions = items.stream()
            .map(Instruction::new)
            .collect(Collectors.toList());

        task1(instructions);
        task2(instructions);
    }

    private static void task1(List<Instruction> instructions) {
        Position position = new Position();
        for (final Instruction instruction : instructions) {
            position = position.cruise(instruction);
        }

        out.println();
        out.println("Distance: " + position.distance());
        out.println();
    }

    private static void task2(List<Instruction> instructions) {
        Position position = new Position();
        for (final Instruction instruction : instructions) {
            position = position.cruise2(instruction);
        }

        out.println();
        out.println("Distance: " + position.distance());
        out.println();
    }

    private static class Instruction {
        private final Command command;
        private final int argument;

        public Instruction(String s) {
            this.command = Command.valueOf(s.substring(0, 1));
            this.argument = Integer.parseInt(s.substring(1));
        }

        @Override
        public String toString() {
            return "Instruction{" +
                "command=" + command +
                ", argument=" + argument +
                '}';
        }
    }

    public enum Command {
        E, N, W, S, F, L, R
    }

    private static class Position {
        int longitute = 0;
        int latiude = 0;
        int direction = 90;
        Position waypoint;

        public Position cruise(Instruction instruction) {
            if (instruction.command == Command.E) {
                longitute += instruction.argument;
            }
            if (instruction.command == Command.W) {
                longitute -= instruction.argument;
            }
            if (instruction.command == Command.N) {
                latiude += instruction.argument;
            }
            if (instruction.command == Command.S) {
                latiude -= instruction.argument;
            }
            if (instruction.command == Command.R) {
                direction += instruction.argument;
                correctDirection();
            }
            if (instruction.command == Command.L) {
                direction -= instruction.argument;
                correctDirection();
            }
            if (instruction.command == Command.F) {
                if (direction == 90) {
                    longitute += instruction.argument;
                }
                if (direction == 270) {
                    longitute -= instruction.argument;
                }
                if (direction == 0) {
                    latiude += instruction.argument;
                }
                if (direction == 180) {
                    latiude -= instruction.argument;
                }
            }
            return this;
        }

        public Position cruise2(Instruction instruction) {
            if (waypoint == null) {
                waypoint = new Position();
                waypoint.longitute = 10;
                waypoint.latiude = 1;
            }
            if (instruction.command == Command.E) {
                waypoint.longitute += instruction.argument;
            }
            if (instruction.command == Command.W) {
                waypoint.longitute -= instruction.argument;
            }
            if (instruction.command == Command.N) {
                waypoint.latiude += instruction.argument;
            }
            if (instruction.command == Command.S) {
                waypoint.latiude -= instruction.argument;
            }
            if (instruction.command == Command.R) {
                if (instruction.argument == 90) {
                    final int wl = waypoint.latiude;
                    waypoint.latiude = -waypoint.longitute;
                    waypoint.longitute = wl;
                }
                if (instruction.argument == 180) {
                    waypoint.latiude = -waypoint.latiude;
                    waypoint.longitute = -waypoint.longitute;
                }
                if (instruction.argument == 270) {
                    final int wl = -waypoint.latiude;
                    waypoint.latiude = waypoint.longitute;
                    waypoint.longitute = wl;
                }
            }
            if (instruction.command == Command.L) {
                if (instruction.argument == 90) {
                    final int wl = -waypoint.latiude;
                    waypoint.latiude = waypoint.longitute;
                    waypoint.longitute = wl;
                }
                if (instruction.argument == 180) {
                    waypoint.latiude = -waypoint.latiude;
                    waypoint.longitute = -waypoint.longitute;
                }
                if (instruction.argument == 270) {
                    final int wl = waypoint.latiude;
                    waypoint.latiude = -waypoint.longitute;
                    waypoint.longitute = wl;
                }
            }
            if (instruction.command == Command.F) {
                longitute += waypoint.longitute * instruction.argument;
                latiude += waypoint.latiude * instruction.argument;
            }
            return this;
        }

        private void correctDirection() {
            if (direction < 0) {
                direction += 360;
            }
            if (direction >= 360) {
                direction -= 360;
            }
        }

        public int distance() {
            return abs(longitute) + abs(latiude);
        }

        @Override
        public String toString() {
            return "Position{" +
                "longitute=" + longitute +
                ", latiude=" + latiude +
                ", direction=" + direction +
                ", waypoint=" + waypoint +
                '}';
        }
    }
}