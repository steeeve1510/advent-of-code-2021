package com.google;

import com.google.util.ReadFileUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 {

    public static void main(String[] args) throws IOException {
        var file = "/home/stefan/Desktop/advent-of-code/6/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var lanternFishes = parse(lines.get(0));

        var days = 80;

        for (int i = 0; i < days; i++) {
            lanternFishes = lanternFishes.stream()
                    .flatMap(f -> f.breed().stream())
                    .collect(Collectors.toList());
        }

        System.out.println(lanternFishes.size());
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

        public List<LanternFish> breed() {
            if (timer > 0) {
                timer--;
                return List.of(this);
            }

            timer = 6;
            return List.of(this, new LanternFish(8));
        }
    }
}
