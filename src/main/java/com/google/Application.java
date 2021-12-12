package com.google;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream(new File("/home/stefan/Desktop/advent-of-code/1/input"));
        String data = readFromInputStream(inputStream);

        List<Long> measurements = Arrays.stream(data.split("\n"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        long counter = 0;
        long previous = Long.MAX_VALUE;
        for (var m : measurements) {
            if (m > previous) {
                counter++;
            }
            previous = m;
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
