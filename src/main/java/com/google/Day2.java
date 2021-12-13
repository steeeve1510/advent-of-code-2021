package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.List;

public class Day2 {

    public static void main(String[] args) throws IOException {
        var file = "/2/input2";

        List<DirectionChange> changes = ReadFileUtil.readLinesFrom(file).stream()
                .map(DirectionChange::parse)
                .toList();

        long horizontal = 0;
        long depth = 0;
        long aim = 0;

        for (var c : changes) {
            switch (c.direction()) {
                case up -> aim -= c.change();
                case down -> aim += c.change();
                case forward -> {
                    horizontal += c.change();
                    depth += aim * c.change();
                }
            }
        }

        System.out.println(horizontal * depth);
    }

    private record DirectionChange(Direction direction, int change) {
        public static DirectionChange parse(String text) {
            var parts = text.split(" ");
            var direction = Direction.valueOf(parts[0]);
            var change = Integer.parseInt(parts[1]);
            return new DirectionChange(direction, change);
        }
    }

    private enum Direction {
        forward,
        down,
        up;
    }
}
