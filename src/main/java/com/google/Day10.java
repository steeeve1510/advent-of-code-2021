package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static java.util.function.Predicate.not;

public class Day10 {

    private static final List<Character> OPENING_CHARS = List.of('(', '[', '{', '<');

    public static void main(String[] args) throws IOException {
        var file = "/10/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var incompleteLines = lines.stream()
                .filter(not(l -> buildWithError(l).getValue()))
                .toList();

        var remainders = incompleteLines.stream()
                .map(Day10::buildWithError)
                .filter(not(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .map(Day10::getRemaining)
                .toList();

        var scores = remainders.stream()
                .map(Day10::calculateScore)
                .sorted()
                .toList();

        var output = scores.get(scores.size() / 2);
        System.out.println(output);
    }

    private static Long calculateScore(List<Character> characters) {
        return characters.stream()
                .map(c -> switch (c) {
                    case ')' -> 1L;
                    case ']' -> 2L;
                    case '}' -> 3L;
                    case '>' -> 4L;
                    default -> 0L;
                })
                .reduce(0L, (v1, v2) -> v1 * 5L + v2);
    }

    private static Map.Entry<Stack<Character>, Boolean> buildWithError(String line) {
        var stack = new Stack<Character>();

        var chars = line.toCharArray();
        for (char c : chars) {
            var expected = getExpectedCharacter(stack);
            if (OPENING_CHARS.contains(c)) {
                stack.push(c);
            } else if (expected != null && c == expected) {
                stack.pop();
            } else {
                return Map.entry(stack, true);
            }
        }

        return Map.entry(stack, false);
    }

    private static List<Character> getRemaining(Stack<Character> s) {
        List<Character> closing = new ArrayList<>();
        while (!s.isEmpty()) {
            var next = s.pop();
            var c = getClosingCharacter(next);
            closing.add(c);
        }
        return closing;
    }

    private static Character getExpectedCharacter(Stack<Character> s) {
        if (s.isEmpty()) {
            return null;
        }
        var character = s.peek();
        return getClosingCharacter(character);
    }

    private static Character getClosingCharacter(Character c) {
        if (c == null) {
            return null;
        }
        return switch (c) {
            case '(' -> ')';
            case '[' -> ']';
            case '{' -> '}';
            case '<' -> '>';
            default -> null;
        };
    }
}
