package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Day5 {

    public static void main(String[] args) throws IOException {
        var file = "/home/stefan/Desktop/advent-of-code/5/input";

        var lines = ReadFileUtil.readLinesFrom(file).stream()
                .map(Day5::parse)
                .toList();

        Map<Point, Integer> field = new HashMap<>();

        lines.forEach(l -> {
            var drawnLine = getDrawnLine(l);
            drawnLine.forEach(p -> {
                field.compute(p, (key, value) -> {
                    if (value == null) {
                        return 1;
                    }
                    return value + 1;
                });
            });
        });

        var count = field.values().stream()
                .filter(v -> v > 1)
                .count();

        System.out.println(count);
    }

    private static List<Point> getDrawnLine(Line line) {
        var a = line.a();
        var b = line.b();

        var xDiff = b.x() - a.x();
        var yDiff = b.y() - a.y();
        if (xDiff == 0) {
            var x = a.x();
            return getIntStream(a.y(), b.y())
                    .mapToObj(y -> new Point(x, y))
                    .toList();
        } else if (yDiff == 0) {
            var y = a.y();
            return getIntStream(a.x(), b.x())
                    .mapToObj(x -> new Point(x, y))
                    .toList();
        } else {
            var xStream = getIntStream(a.x(), b.x()).boxed().toList();
            var yStream = getIntStream(a.y(), b.y()).boxed().toList();

            return IntStream.range(0, xStream.size())
                    .mapToObj(i -> new Point(xStream.get(i), yStream.get(i)))
                    .toList();
        }
    }

    private static IntStream getIntStream(int a, int b) {
        if (a < b) {
            return IntStream.rangeClosed(a, b);
        } else {
            return IntStream.rangeClosed(b, a)
                    .map(i -> b - i + a);
        }
    }

    private static Line parse(String line) {
        var parts = line.split(" -> ");
        var a = Arrays.stream(parts[0].split(","))
                .map(Integer::parseInt)
                .toList();
        var b = Arrays.stream(parts[1].split(","))
                .map(Integer::parseInt)
                .toList();

        return new Line(
                new Point(a.get(0), a.get(1)),
                new Point(b.get(0), b.get(1))
        );
    }

    private record Line(Point a, Point b) {
    }

    private record Point(int x, int y) {
    }
}
