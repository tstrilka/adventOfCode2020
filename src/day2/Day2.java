package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {

    public static void main(String[] args) throws IOException {
        final List<PolicyPassword> items = Files.lines(Path.of("src/day2/input.txt"))
            .map(PolicyPassword::parse)
            .collect(Collectors.toList());
        items.forEach(pp -> {
            System.out.printf("is valid: %s - %s%n", pp.isValid(), pp);
        });
        final long validCount = items.stream()
            .filter(PolicyPassword::isValid)
            .count();
        System.out.println("valid count: " + validCount);
        final long validCountNew = items.stream()
            .filter(PolicyPassword::isValidNew)
            .count();
        System.out.println("valid count new: " + validCountNew);
    }

    private static class PolicyPassword {
        private final Policy policy;
        private final String password;

        private PolicyPassword(Policy policy, String password) {
            this.policy = policy;
            this.password = password;
        }

        public static PolicyPassword parse(String s) {
            return new PolicyPassword(
                Policy.parse(s.substring(0, s.indexOf(":"))),
                s.substring(s.indexOf(": ") + 2)
            );
        }

        @Override
        public String toString() {
            return "PolicyPassword{" +
                "policy=" + policy +
                ", password='" + password + '\'' +
                '}';
        }

        public boolean isValid() {
            final long count = password.chars()
                .filter(ch -> ch == policy.character)
                .count();
            return count >= policy.number1 && count <= policy.number2;
        }

        public boolean isValidNew() {
            final char char1 = password.toCharArray()[policy.number1 - 1];
            final char char2 = password.toCharArray()[policy.number2 - 1];
            return (char1 == policy.character && char2 != policy.character)
                || (char1 != policy.character && char2 == policy.character);
        }
    }

    private static class Policy {
        private final int number1;
        private final int number2;
        private final char character;

        private Policy(int number1, int number2, char character) {
            this.number1 = number1;
            this.number2 = number2;
            this.character = character;
        }

        public static Policy parse(String s) {
            return new Policy(
                Integer.parseInt(s.substring(0, s.indexOf("-"))),
                Integer.parseInt(s.substring(s.indexOf("-") + 1, s.indexOf(" "))),
                s.substring(s.indexOf(" ") + 1).toCharArray()[0]
            );
        }

        @Override
        public String toString() {
            return "Policy{" +
                "number1=" + number1 +
                ", number2=" + number2 +
                ", character='" + character + '\'' +
                '}';
        }
    }
}
