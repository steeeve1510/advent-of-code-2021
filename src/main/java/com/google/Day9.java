package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day9 {

    private static final List<String> characters = List.of("a", "b", "c", "d", "e", "f", "g");

    public static void main(String[] args) throws IOException {
        var file = "/9/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var map = parse(lines);

        List<Integer> lowPoints = new ArrayList<>();
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                var current = map.get(x, y);
                var up = map.getUp(x, y);
                var right = map.getRight(x, y);
                var down = map.getDown(x, y);
                var left = map.getLeft(x, y);

                if (current == null) {
                    continue;
                }
                if (up != null && up <= current) {
                    continue;
                }
                if (right != null && right <= current) {
                    continue;
                }
                if (down != null && down <= current) {
                    continue;
                }
                if (left != null && left <= current) {
                    continue;
                }
                lowPoints.add(current);
            }
        }

        var risk = lowPoints.stream()
                .map(p -> p + 1)
                .reduce(0, Integer::sum);
        System.out.println(risk);
    }

    private static Map parse(List<String> lines) {
        var field = lines.stream()
                .map(l -> {
                    var chars = l.toCharArray();
                    return IntStream.range(0, chars.length)
                            .mapToObj(i -> chars[i])
                            .map(c -> c + "")
                            .map(Integer::parseInt)
                            .toList();
                })
                .toList();
        return new Map(field);
    }

    private record Map(List<List<Integer>> field) {

        public int getWidth() {
            return field.get(0).size();
        }

        public int getHeight() {
            return field.size();
        }

        public Integer get(int x, int y) {
            if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
                return null;
            }
            return field.get(y).get(x);
        }

        public Integer getUp(int x, int y) {
            return get(x, y - 1);
        }

        public Integer getRight(int x, int y) {
            return get(x + 1, y);
        }

        public Integer getDown(int x, int y) {
            return get(x, y + 1);
        }

        public Integer getLeft(int x, int y) {
            return get(x - 1, y);
        }
    }
}
