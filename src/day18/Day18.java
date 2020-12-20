package day18;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day18 {

    public static void main(String[] args) throws IOException {
        task1();
        task2();
    }

    private static void task1() throws IOException {
        out.println("Result 1: " + lines(of("src/day18/input.txt"))
            .map(item -> new Expression(item, false))
            .map(Expression::evaluate)
            .mapToLong(Long::longValue)
            .sum());
    }

    private static void task2() throws IOException {
        out.println("Result 2: " + lines(of("src/day18/input.txt"))
            .map(item -> new Expression(item, true))
            .map(Expression::evaluate)
            .mapToLong(Long::longValue)
            .sum());
    }

    private static class Expression {
        private final boolean secondTask;
        private List<Long> numbers = new ArrayList<>();
        private List<String> operators = new ArrayList<>();

        public Expression(String item, boolean secondTask) {
            this.secondTask = secondTask;
            item = item.replace(" ", "");
            char[] chars = item.toCharArray();
            int innerExpressionCount = 0;
            List<Character> innerExpression = new ArrayList<>();
            for (char ch : chars) {
                if (innerExpressionCount == 0 && ch >= '0' && ch <= '9') {
                    numbers.add(Long.parseLong(String.valueOf(ch)));
                }
                if (innerExpressionCount == 0 && (ch == '+' || ch == '*')) {
                    operators.add(String.valueOf(ch));
                }
                if (ch == '(') {
                    innerExpressionCount++;
                    if (innerExpressionCount == 1) {
                        continue;
                    }
                }
                if (ch == ')') {
                    innerExpressionCount--;
                    if (innerExpressionCount == 0) {
                        final Expression innerExpressionObject = new Expression(
                            innerExpression.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining()), secondTask
                        );
                        numbers.add(Long.parseLong(String.valueOf(secondTask ? innerExpressionObject.evaluate2() : innerExpressionObject.evaluate1())));
                        innerExpression = new LinkedList<>();
                    }
                }
                if (innerExpressionCount > 0) {
                    innerExpression.add(ch);
                }
            }
        }

        public Long evaluate1() {
            Long result = numbers.get(0);
            for (int i = 0; i < operators.size(); i++) {
                if ("+".equals(operators.get(i))) {
                    result += numbers.get(i + 1);
                } else if ("*".equals(operators.get(i))) {
                    result *= numbers.get(i + 1);
                }
            }
            return result;
        }

        public Long evaluate2() {
            while (operators.contains("+")) {
                for (int i = 0; i < operators.size(); i++) {
                    if ("+".equals(operators.get(i))) {
                        final long sum = numbers.get(i) + numbers.get(i + 1);
                        final List<String> operatorsCopy = new ArrayList<>(operators);
                        operators = new ArrayList<>();
                        operators.addAll(operatorsCopy.subList(0, i));
                        operators.addAll(operatorsCopy.subList(i + 1, operatorsCopy.size()));
                        final List<Long> numbersCopy = new ArrayList<>(numbers);
                        numbers = new ArrayList<>();
                        numbers.addAll(numbersCopy.subList(0, i));
                        numbers.add(sum);
                        numbers.addAll(numbersCopy.subList(i + 2, numbersCopy.size()));
                        break;
                    }
                }
            }
            return evaluate1();
        }

        public Long evaluate() {
            return this.secondTask ? evaluate2() : evaluate1();
        }
    }
}