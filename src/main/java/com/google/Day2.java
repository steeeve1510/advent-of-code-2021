package com.google;

import com.google.util.ReadFileUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {

    public static void main(String[] args) throws IOException {
        var file = "/home/stefan/Desktop/advent-of-code/2/input";

        List<DirectionChange> changes = ReadFileUtil.readLinesFrom(file).stream()
                .map(DirectionChange::parse)
                .collect(Collectors.toList());

        long horizontal = 0;
        long depth = 0;

        for (var c : changes) {
            switch (c.getDirection()) {
                case up:
                    depth -= c.getChange();
                    break;
                case down:
                    depth += c.getChange();
                    break;
                case forward:
                    horizontal += c.getChange();
                    break;
            }
        }

        System.out.println(horizontal * depth);
    }

    @RequiredArgsConstructor
    @Getter
    private static class DirectionChange {
        private final Direction direction;
        private final int change;

        public static DirectionChange parse(String text) {
            var parts = text.split(" ");
            var direction = Direction.valueOf(parts[0]);
            var change = Integer.valueOf(parts[1]);
            return new DirectionChange(direction, change);
        }
    }

    private enum Direction {
        forward,
        down,
        up;
    }
}
