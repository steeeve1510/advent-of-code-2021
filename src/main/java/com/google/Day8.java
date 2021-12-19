package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day8 {

    private static final List<String> characters = List.of("a", "b", "c", "d", "e", "f", "g");

    public static void main(String[] args) throws IOException {
        var file = "/8/input";

        var lcds = ReadFileUtil.readLinesFrom(file).stream()
                .map(Day8::parse)
                .toList();

        var assignments = getAllAssignments().stream()
                .map(Assignment::new)
                .toList();

        var counter = 0;
        for (var lcd : lcds) {
            var validAssignments = assignments.stream()
                    .filter(a -> lcd.all().stream().allMatch(a::isValidNumber))
                    .toList();
            if (validAssignments.size() > 1) {
                throw new RuntimeException("Dafuq");
            }
            var assignment = validAssignments.get(0);
            var correctNumbers = lcd.output().stream()
                    .map(assignment::getNumber)
                    .filter(n -> List.of(1, 4, 7, 8).contains(n))
                    .count();
            counter += correctNumbers;
        }
        System.out.println(counter);
    }

    private static LCD parse(String line) {
        var parts = line.split(" [|] ");
        var input = Arrays.stream(parts[0].split(" "))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        var output = Arrays.stream(parts[1].split(" "))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        return new LCD(input, output);
    }

    private static List<String> getAllAssignments() {
        return product("", characters);
    }

    private static List<String> product(String prefix, List<String> available) {
        if (available.isEmpty()) {
            return List.of(prefix);
        }
        return available.stream()
                .flatMap(a -> {
                    var without = available.stream()
                            .filter(b -> !b.equals(a))
                            .toList();
                    return product(prefix + a, without).stream();
                })
                .toList();
    }

    private record LCD(List<String> input, List<String> output) {
        public List<String> all() {
            return Stream.of(input, output)
                    .flatMap(Collection::stream)
                    .toList();
        }
    }

    private record Assignment(String assignment) {

        public boolean isValidNumber(String turnedOn) {
            return getNumber(turnedOn) != null;
        }

        public Integer getNumber(String turnedOn) {
            var chars = turnedOn.toCharArray();
            if (isZero(chars)) {
                return 0;
            } else if (isOne(chars)) {
                return 1;
            } else if (isTwo(chars)) {
                return 2;
            } else if (isThree(chars)) {
                return 3;
            } else if (isFour(chars)) {
                return 4;
            } else if (isFive(chars)) {
                return 5;
            } else if (isSix(chars)) {
                return 6;
            } else if (isSeven(chars)) {
                return 7;
            } else if (isEight(chars)) {
                return 8;
            } else if (isNine(chars)) {
                return 9;
            } else {
                return null;
            }
        }

        public boolean isZero(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(0, 1, 2, 4, 5, 6).equals(positions);
        }

        public boolean isOne(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(2, 5).equals(positions);
        }

        public boolean isTwo(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(0, 2, 3, 4, 6).equals(positions);
        }

        public boolean isThree(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(0, 2, 3, 5, 6).equals(positions);
        }

        public boolean isFour(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(1, 2, 3, 5).equals(positions);
        }

        public boolean isFive(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(0, 1, 3, 5, 6).equals(positions);
        }

        public boolean isSix(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(0, 1, 3, 4, 5, 6).equals(positions);
        }

        public boolean isSeven(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(0, 2, 5).equals(positions);
        }

        public boolean isEight(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(0, 1, 2, 3, 4, 5, 6).equals(positions);
        }

        public boolean isNine(char[] turnedOn) {
            var positions = getPositions(turnedOn);
            return Set.of(0, 1, 2, 3, 5, 6).equals(positions);
        }

        private Set<Integer> getPositions(char[] turnedOn) {
            return IntStream.range(0, turnedOn.length)
                    .mapToObj(i -> turnedOn[i])
                    .map(this::get)
                    .collect(Collectors.toSet());
        }

        private int get(char i) {
            return assignment.indexOf(i);
        }
    }
}
