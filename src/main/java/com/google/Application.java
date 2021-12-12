package com.google;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream(new File("/home/stefan/Desktop/advent-of-code/1/input2"));
        String data = readFromInputStream(inputStream);

        List<Long> measurements = Arrays.stream(data.split("\n"))
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

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
