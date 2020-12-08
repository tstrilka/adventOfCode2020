package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Day4 {

    private static Set<String> keys = Set.of(
        "byr",
        "iyr",
        "eyr",
        "hgt",
        "hcl",
        "ecl",
        "pid"
//        "cid"
    );

    public static void main(String[] args) throws IOException {
        final List<String> items = Files.lines(Path.of("src/day4/input.txt"))
            .collect(Collectors.toList());

        final AtomicBoolean newGroup = new AtomicBoolean(true);
        final Map<String, String> passport = new HashMap<>();
        final List<Map<String, String>> passports = new ArrayList<>();

        items.forEach(line -> {
            if (line.isBlank()) {
                System.out.println();
                newGroup.set(true);
                passports.add(new HashMap<>(passport));
                passport.clear();
            } else {
                newGroup.set(false);;

                final List<String> blocks = Arrays.stream(
                    line
                        .split(" "))
                        .collect(Collectors.toList());
                System.out.println(blocks);

                blocks.forEach(block -> {
                    final String key = block.substring(0, block.indexOf(':'));
                    final String value = block.substring(block.indexOf(':') + 1);
                    passport.put(key, value);
                });
            }
        });

        System.out.println();
        System.out.println(passports);
        System.out.println();

        final long count = passports.stream()
            .filter(p -> {
                final Set<String> presentKeys = p.keySet();
                return presentKeys.containsAll(keys);
            })
            .count();


        System.out.println("Count: " + count);
        System.out.println();

        final long count2 = passports.stream()
            .filter(p -> {
                final Set<String> presentKeys = p.keySet();
                return presentKeys.containsAll(keys) && (
                    (Long.parseLong(p.get("byr")) >= 1920L && Long.parseLong(p.get("byr")) <= 2002L)
                    && (Long.parseLong(p.get("iyr")) >= 2010L && Long.parseLong(p.get("iyr")) <= 2020L)
                    && (Long.parseLong(p.get("eyr")) >= 2020L && Long.parseLong(p.get("eyr")) <= 2030L)
                    && validateHeight(p)
                    && validateColor(p)
                    && validateEye(p)
                    && validateId(p)
                );
            })
            .count();

        System.out.println("Count 2: " + count2);
    }

    private static boolean validateEye(Map<String, String> p) {
        final String hcl = p.get("ecl");
        return hcl.equals("amb")
            || hcl.equals("blu")
            || hcl.equals("brn")
            || hcl.equals("gry")
            || hcl.equals("grn")
            || hcl.equals("hzl")
            || hcl.equals("oth")
            ;
    }

    private static boolean validateColor(Map<String, String> p) {
        final String hcl = p.get("hcl");
        return hcl.startsWith("#") && hcl.length() == 7
            && hcl.substring(1, 7).chars().allMatch(c -> (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f'));
    }

    private static boolean validateId(Map<String, String> p) {
        final String hcl = p.get("pid");
        return hcl.length() == 9
            && hcl.chars().allMatch(c -> (c >= '0' && c <= '9'));
    }

    private static boolean validateHeight(Map<String, String> p) {
        final String hgt = p.get("hgt");
        if (hgt.endsWith("cm")) {
            long l = Long.parseLong(hgt.substring(0, hgt.length() - 2));
            return l >= 150L && l <= 193L;
        }
        if (hgt.endsWith("in")) {
            long l = Long.parseLong(hgt.substring(0, hgt.length() - 2));
            return l >= 59L && l <= 76L;
        }
        return false;
    }
}
