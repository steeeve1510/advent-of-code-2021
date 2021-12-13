package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day3 {

    private static int numberOfDigits = 12;

    public static void main(String[] args) throws IOException {
        var file = "/3/input2";

        List<String> lines = ReadFileUtil.readLinesFrom(file);

        List<BinaryNumber> numbers = lines.stream()
                .map(BinaryNumber::parse)
                .toList();

        var oxygen = oxygen(numbers);
        var co2 = co2(numbers);

        System.out.println(oxygen * co2);
    }

    private static int oxygen(List<BinaryNumber> numbers) {
        for (var i = 0; i < numberOfDigits; i++) {
            var threshold = numbers.size() / 2f;
            var count = count(numbers, i);
            if (count >= threshold) {
                numbers = filter(numbers, 1, i);
            } else {
                numbers = filter(numbers, 0, i);
            }
            if (numbers.size() <= 1) {
                break;
            }
        }
        if (numbers.size() > 1) {
            throw new RuntimeException("Dafuq " + numbers.size());
        }
        var text = numbers.get(0).digits.stream()
                .map(String::valueOf)
                .reduce("", (a, b) -> a + b);
        return Integer.parseInt(text, 2);
    }

    private static int co2(List<BinaryNumber> numbers) {
        for (var i = 0; i < numberOfDigits; i++) {
            var threshold = numbers.size()  / 2f;
            var count = count(numbers, i);
            if (count < threshold) {
                numbers = filter(numbers, 1, i);
            } else {
                numbers = filter(numbers, 0, i);
            }
            if (numbers.size() <= 1) {
                break;
            }
        }
        if (numbers.size() > 1) {
            throw new RuntimeException("Dafuq " + numbers.size());
        }
        var text = numbers.get(0).digits.stream()
                .map(String::valueOf)
                .reduce("", (a, b) -> a + b);
        return Integer.parseInt(text, 2);
    }

    private static int count(List<BinaryNumber> numbers, int position) {
        return numbers.stream()
                .map(n -> n.get(position))
                .reduce(0, Integer::sum);
    }

    private static List<BinaryNumber> filter(List<BinaryNumber> numbers, int criteria, int position) {
        return numbers.stream()
                .filter(n -> n.get(position) == criteria)
                .toList();
    }

    private record BinaryNumber(List<Integer> digits) {

        public Integer get(int index) {
            return digits.get(index);
        }

        public static BinaryNumber parse(String text) {
            var values = new ArrayList<Integer>();
            var characters = text.toCharArray();
            for (var c : characters) {
                if ('0' == c) {
                    values.add(0);
                }
                if ('1' == c) {
                    values.add(1);
                }
            }
            return new BinaryNumber(values);
        }
    }
}
