package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day1 {

    public static void main(String[] args) throws IOException {
        var file = "/home/stefan/Desktop/advent-of-code/1/input2";

        List<Long> measurements = ReadFileUtil.readLinesFrom(file).stream()
                .map(Long::parseLong)
                .toList();

        List<Long> sums = new ArrayList<>();
        for (var i = 0; i < measurements.size() - 2; i++) {
            var first = measurements.get(i);
            var second = measurements.get(i + 1);
            var third = measurements.get(i + 2);

            var sum = first + second + third;
            sums.add(sum);
        }

        long counter = 0;
        long previous = Long.MAX_VALUE;
        for (var s : sums) {
            if (s > previous) {
                counter++;
            }
            previous = s;
        }

        System.out.println(counter);
    }

}
