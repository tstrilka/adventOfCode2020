package day8;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day8 {

    public static void main(String[] args) throws IOException {
        final List<String> items = lines(of("src/day8/input.txt"))
            .collect(Collectors.toList());

        out.println();
        items.forEach(answer -> out.println("Line: " + answer));
        out.println();

//        task1(items);
        task2(items);
    }

    private static void task1(List<String> items) {
        final List<Instruction> instructions = parseInstructions(items);

        long count = executeInstructions(instructions).get(0).intValue();

        out.println();
        out.println("Count: " + count);
        out.println();
    }

    private static void task2(List<String> items) {
        final List<Instruction> instructions = parseInstructions(items);

        List<Number> result;
        int indexOfChange = 0;
        while (true) {
            out.println();
            List<Instruction> instructionCopy = instructions.stream()
                .map(i -> new Instruction(i.type, i.argument))
                .collect(Collectors.toList());
//            if (instructionCopy.get(indexOfChange).type == Type.nop) {
//                instructionCopy.get(indexOfChange).type = Type.jmp;
//            }
            if (instructionCopy.get(indexOfChange).type == Type.jmp) {
                instructionCopy.get(indexOfChange).type = Type.nop;
            }
            result = executeInstructions(instructionCopy);
            out.println();
            int pointer = result.get(1).intValue();
            if (pointer >= instructions.size()) {
                break;
            }
            indexOfChange++;
        }

        out.println();
        out.println("Count: " + result.get(0));
        out.println();
    }

    private static List<Instruction> parseInstructions(List<String> items) {
        final List<Instruction> instructions = items.stream()
            .map(line -> new Instruction(line.substring(0, 3), line.substring(4)))
            .collect(Collectors.toList());

        out.println();
        instructions.forEach(instruction -> out.println("Instruction: " + instruction));
        out.println();
        return instructions;
    }

    private static List<Number> executeInstructions(List<Instruction> instructions) {
        int pointer = 0;
        long count = 0;
        Instruction instruction;
        while (true) {
            instruction = instructions.get(pointer);
            out.println("Pointer: " + pointer + ",  Instruction: " + instruction);
            if (instruction.callCount == 1) {
                break;
            }
            if (instruction.type == Type.nop) {
                pointer++;
            }
            if (instruction.type == Type.acc) {
                count += instruction.argument;
                pointer++;
            }
            if (instruction.type == Type.jmp) {
                pointer += instruction.argument;
            }
            if (pointer >= instructions.size()) {
                break;
            }
            instruction.callCount += 1;
        }
        return List.of(count, pointer);
    }

    private static class Instruction {
        Type type;
        final int argument;
        long callCount;

        public Instruction(String instruction, String argument) {
            this.type = parse(instruction);
            this.argument = Integer.parseInt(argument);
            this.callCount = 0;
        }

        public Instruction(Type type, int argument) {
            this.type = type;
            this.argument = argument;
        }

        private Type parse(String instruction) {
            if (instruction.equals("acc")) {
                return Type.acc;
            }
            if (instruction.equals("nop")) {
                return Type.nop;
            }
            if (instruction.equals("jmp")) {
                return Type.jmp;
            }
            return null;
        }

        @Override
        public String toString() {
            return "Instruction{" +
                "type=" + type +
                ", argument=" + argument +
                '}';
        }
    }
}
