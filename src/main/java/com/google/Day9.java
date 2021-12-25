package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day9 {

    public static void main(String[] args) throws IOException {
        var file = "/9/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var map = parse(lines);

        var basins = new HashMap<Field, Integer>();
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                var field = new Field(x, y);
                var elevation = map.getElevation(field);
                if (elevation >= 9) {
                    continue;
                }
                var lowest = floatDown(field, map);
                basins.putIfAbsent(lowest, 0);
                basins.computeIfPresent(lowest, (k, v) -> v + 1);
            }
        }

        Comparator<java.util.Map.Entry<Field, Integer>> comparator = java.util.Map.Entry.comparingByValue();
        var sum = basins.entrySet().stream()
                .sorted(comparator.reversed())
                .limit(3)
                .map(java.util.Map.Entry::getValue)
                .reduce(1, (v1, v2) -> v1 * v2);
        System.out.println(sum);
    }

    private static Field floatDown(Field field, Map map) {
        Field current = field;
        Field prev = null;

        while (!current.equals(prev)) {
            prev = current;
            current = floatStep(current, map);
        }
        return current;
    }

    private static Field floatStep(Field field, Map map) {
        var heightMap = Stream.of(
                        field,
                        field.getUp(),
                        field.getRight(),
                        field.getDown(),
                        field.getLeft()
                )
                .filter(map::isInBounds)
                .collect(Collectors.toMap(Function.identity(), map::getElevation));

        return heightMap.entrySet().stream()
                .min(Comparator.comparingInt(java.util.Map.Entry::getValue))
                .map(java.util.Map.Entry::getKey)
                .orElse(null);
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

    private record Map(List<List<Integer>> fields) {

        public int getWidth() {
            return fields.get(0).size();
        }

        public int getHeight() {
            return fields.size();
        }

        public boolean isInBounds(Field field) {
            var x = field.x();
            var y = field.y();
            return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
        }

        public Integer getElevation(Field field) {
            return getElevation(field.x(), field.y());
        }

        public Integer getElevation(int x, int y) {
            if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
                return null;
            }
            return fields.get(y).get(x);
        }
    }

    private record Field(int x, int y) {
        public Field getUp() {
            return new Field(x, y - 1);
        }

        public Field getRight() {
            return new Field(x + 1, y);
        }

        public Field getDown() {
            return new Field(x, y + 1);
        }

        public Field getLeft() {
            return new Field(x - 1, y);
        }
    }
}
