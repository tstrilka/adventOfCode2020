package day21;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static java.lang.System.out;
import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;

public class Day21 {

    public static void main(String[] args) throws IOException {
        task("src/day21/input.txt");
    }

    private static void task(String path) throws IOException {
        final List<String> items = lines(of(path))
            .collect(Collectors.toList());
        out.println("Lines: " + items);

        final List<Set<String>> ingredients = items.stream()
            .map(line -> Arrays.stream(
                line
                    .split("\\(")[0]
                    .split(" ")
                )
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toSet())
            )
            .collect(Collectors.toList());

        final List<Set<String>> allergens = items.stream()
            .map(line -> Arrays.stream(
                line
                    .split("\\(")[1]
                    .replace(")", "")
                    .replace("contains", "")
                    .replace(" ", "")
                    .split(",")
                )
                    .filter(s -> !s.isBlank())
                    .collect(Collectors.toSet())
            )
            .collect(Collectors.toList());

        final Map<String, List<String>> ingredientToAllergens = new HashMap<>();
        for (int i = 0; i < ingredients.size(); i++) {
            final Set<String> possibleAllergens = allergens.get(i);
            ingredients.get(i).forEach(ingredient -> {
                final List<String> ingredientAllergens = ingredientToAllergens.getOrDefault(ingredient, new ArrayList<>());
                ingredientAllergens.addAll(possibleAllergens);
                ingredientToAllergens.put(ingredient, ingredientAllergens);
            });
        }

        final Map<String, List<String>> allergenToIngredients = allergens.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toSet()).stream()
            .collect(Collectors.toMap(
                allergen -> allergen,
                allergen ->
                    ingredientToAllergens.entrySet().stream()
                        .filter(e -> e.getValue().contains(allergen))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList())
                )
            );

        final Map<String, List<Long>> allergenToIngredientCount = allergenToIngredients.entrySet().stream()
            .map(allergenIngredients ->
                new AbstractMap.SimpleEntry<>(
                    allergenIngredients.getKey(),
                    allergenIngredients.getValue().stream()
                        .map(ingredient -> {
                            long count = 0;
                            for (int j = 0; j < allergens.size(); j++) {
                                if (allergens.get(j).contains(allergenIngredients.getKey()) && ingredients.get(j).contains(ingredient)) {
                                    count++;
                                }
                            }
                            return count;
                        })
                        .collect(Collectors.toList())
                )
            ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        final Map<String, Long> allergenToMax = allergenToIngredientCount.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream().max(Long::compareTo).orElseThrow()
            ));

        final Map<String, List<String>> allergenToIngredientsReduced = allergenToIngredients.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
               e -> {
                   List<String> result = new ArrayList<>();
                   for (int i = 0; i < e.getValue().size(); i++) {
                       if (allergenToIngredientCount.get(e.getKey()).get(i).equals(allergenToMax.get(e.getKey()))) {
                           result.add(allergenToIngredients.get(e.getKey()).get(i));
                       }
                   }
                   return result;
               }
            ));

        Map<String, String> allergenToIngredient = new HashMap<>();
        while (allergenToIngredient.size() != allergenToIngredientsReduced.size()) {
            final Map<String, String> allergenIngredient = allergenToIngredientsReduced.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(
                    e.getKey(),
                    e.getValue().stream()
                        .filter(i -> !allergenToIngredient.containsValue(i))
                        .collect(Collectors.toList())
                    )
                )
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, e -> e.getValue().get(0)));
            allergenToIngredient.putAll(allergenIngredient);
            out.println();
            out.println(allergenToIngredient);
        }

        final long countUnusedIngredients = ingredients.stream()
            .flatMap(Collection::stream)
            .filter(ingredient -> !allergenToIngredient.containsValue(ingredient))
            .count();

        out.println();
        out.println("Task 1: " + countUnusedIngredients);
        out.println();

        final String orderedIngredients = allergenToIngredient.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(Map.Entry::getValue)
            .collect(Collectors.joining(","));

        out.println();
        out.println("Task 2: " + orderedIngredients);
        out.println();
    }
}