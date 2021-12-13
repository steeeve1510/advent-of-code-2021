package com.google;

import com.google.util.ReadFileUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day6 {

    public static void main(String[] args) throws IOException {
        var file = "/home/stefan/Desktop/advent-of-code/6/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var lanternFishes = parse(lines.get(0));

        var days = 256;

        var simulation = simulate(days);

        long count = lanternFishes.stream()
                .map(l -> simulation.get(days - l.timer))
                .reduce(0L, Long::sum);

        System.out.println(count);
    }

    private static Map<Integer, Long> simulate(int days) {
        Map<Integer, Long> simulation = new HashMap<>();
        var lanternFishes = List.of(new LanternFish(0));

        for (int i = 0; i < days / 2; i++) {
            simulation.put(i, (long) lanternFishes.size());
            lanternFishes = lanternFishes.stream()
                    .flatMap(LanternFish::breed)
                    .toList();
        }

        for (int i = 0; i < days / 2; i++) {
            int finalI = i;
            long count = lanternFishes.stream()
                    .map(l -> simulation.getOrDefault(finalI - l.timer, 1L))
                    .reduce(0L, Long::sum);
            simulation.put(days / 2 + i, count);
        }

        return simulation;
    }

    private static List<LanternFish> parse(String line) {
        return Arrays.stream(line.split(","))
                .map(Integer::parseInt)
                .map(LanternFish::new)
                .toList();
    }

    @Data
    @AllArgsConstructor
    private static class LanternFish {

        private int timer;

        public Stream<LanternFish> breed() {
            if (timer > 0) {
                timer--;
                return Stream.of(this);
            }

            timer = 6;
            return Stream.of(this, new LanternFish(8));
        }
    }
}
