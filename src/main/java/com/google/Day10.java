package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Day10 {

    private static final List<Character> OPENING_CHARS = List.of('(', '[', '{', '<');

    public static void main(String[] args) throws IOException {
        var file = "/10/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var illegalCharacters = lines.stream()
                .map(Day10::getIllegalCharacter)
                .toList();

        var sum = illegalCharacters.stream()
                .filter(Objects::nonNull)
                .map(c -> switch (c) {
                    case ')' -> 3;
                    case ']' -> 57;
                    case '}' -> 1197;
                    case '>' -> 25137;
                    default -> 0;
                })
                .reduce(0, Integer::sum);
        System.out.println(sum);
    }

    private static Character getIllegalCharacter(String line) {
        var stack = new Stack<Character>();

        var chars = line.toCharArray();
        for (char c : chars) {
            var expected = getExpectedCharacter(stack);
            if (OPENING_CHARS.contains(c)) {
                stack.push(c);
            } else if (c == expected) {
                stack.pop();
            } else {
                return c;
            }
        }

        return null;
    }

    private static Character getExpectedCharacter(Stack<Character> s) {
        if (s.isEmpty()) {
            return null;
        }
        var character = s.peek();
        return switch (character) {
            case '(' -> ')';
            case '[' -> ']';
            case '{' -> '}';
            case '<' -> '>';
            default -> null;
        };
    }
}
