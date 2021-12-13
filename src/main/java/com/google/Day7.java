package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7 {

    public static void main(String[] args) throws IOException {
        var file = "/home/stefan/Desktop/advent-of-code/7/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var crabs = parse(lines.get(0));
        var positions = crabs.positions();
        var min = positions.get(0);
        var max = positions.get(positions.size() - 1);

        var minFuel = IntStream.rangeClosed(min, max)
                .map(crabs::getFuel)
                .min()
                .orElse(Integer.MAX_VALUE);

        System.out.println(minFuel);
    }

    private static Crabs parse(String line) {
        var positions = Arrays.stream(line.split(","))
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
        return new Crabs(positions);
    }

    private record Crabs(List<Integer> positions) {
        public int getFuel(int alignment) {
            return positions.stream()
                    .map(p -> Math.abs(p - alignment))
                    .reduce(0, Integer::sum);
        }
    }
}
