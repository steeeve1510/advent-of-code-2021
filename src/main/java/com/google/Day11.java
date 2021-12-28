package com.google;

import com.google.util.ReadFileUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day11 {

    public static void main(String[] args) throws IOException {
        var file = "/11/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var grid = parse(lines);
        var size = grid.size();

        var i = 0L;
        while (true) {
            i++;
            var flashes = grid.energize();
            if (size == flashes) {
                System.out.println(i);
                break;
            }
        }
    }

    private static Grid parse(List<String> lines) {
        var octopi = lines.stream()
                .map(Day11::parse)
                .map(es -> es.stream()
                        .map(Octopus::new)
                        .toList()
                )
                .toList();

        var height = octopi.size();
        var width = octopi.get(0).size();

        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                var current = get(octopi, x, y).orElseThrow();
                current.setUp(get(octopi, x, y - 1));
                current.setUpRight(get(octopi, x + 1, y - 1));
                current.setRight(get(octopi, x + 1, y));
                current.setDownRight(get(octopi, x + 1, y + 1));
                current.setDown(get(octopi, x, y + 1));
                current.setDownLeft(get(octopi, x - 1, y + 1));
                current.setLeft(get(octopi, x - 1, y));
                current.setUpLeft(get(octopi, x - 1, y - 1));
            }
        }

        return new Grid(octopi);
    }

    private static Optional<Octopus> get(List<List<Octopus>> octopi, int x, int y) {
        if (y < 0 || y >= octopi.size()) {
            return Optional.empty();
        }
        var row = octopi.get(y);
        if (x < 0 || x >= row.size()) {
            return Optional.empty();
        }
        return Optional.of(row.get(x));
    }

    private static List<Integer> parse(String line) {
        var chars = line.toCharArray();
        return IntStream.range(0, line.length())
                .mapToObj(i -> chars[i] + "")
                .map(Integer::parseInt)
                .toList();
    }

    private record Grid(List<List<Octopus>> octopi) {
        public long energize() {
            var octopiAsList = octopi.stream()
                    .flatMap(Collection::stream)
                    .toList();
            octopiAsList.forEach(Octopus::energize);

            var count = 0L;
            var currentFlashes = -1L;
            while (currentFlashes != 0) {
                currentFlashes = octopiAsList.stream()
                        .map(Octopus::ignite)
                        .filter(i -> i)
                        .count();
                count += currentFlashes;
            }
            octopiAsList.forEach(Octopus::reset);

            return count;
        }

        public String toString() {
            return octopi.stream()
                    .map(os -> os.stream()
                            .map(Octopus::getEnergy)
                            .map(String::valueOf)
                            .collect(Collectors.joining(""))
                    )
                    .collect(Collectors.joining("\n"));
        }

        public long size() {
            return octopi.stream()
                    .mapToLong(Collection::size)
                    .sum();
        }
    }

    @RequiredArgsConstructor
    @Getter
    @Setter
    private static class Octopus {

        @NonNull
        private int energy;

        private Optional<Octopus> up;
        private Optional<Octopus> upRight;
        private Optional<Octopus> right;
        private Optional<Octopus> downRight;
        private Optional<Octopus> down;
        private Optional<Octopus> downLeft;
        private Optional<Octopus> left;
        private Optional<Octopus> upLeft;

        private boolean ignited = false;

        public void energize() {
            energy++;
        }

        public boolean ignite() {
            if (energy <= 9 || ignited) {
                return false;
            }

            neighbors().forEach(Octopus::energize);
            ignited = true;
            return true;
        }

        public void reset() {
            if (!ignited) {
                return;
            }
            energy = 0;
            ignited = false;
        }

        private List<Octopus> neighbors() {
            return Stream.of(up, upRight, right, downRight, down, downLeft, left, upLeft)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }
    }
}
