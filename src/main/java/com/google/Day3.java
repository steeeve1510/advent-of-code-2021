package com.google;

import com.google.util.ReadFileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day3 {

    public static void main(String[] args) throws IOException {
        var file = "/home/stefan/Desktop/advent-of-code/3/input";

        List<String> number = ReadFileUtil.readLinesFrom(file);

        Map<Integer, Integer> occurrences = new HashMap<>();
        long size = number.size();

        for (var n : number) {
            var characters = n.toCharArray();
            for (var i = 0; i < characters.length; i++) {
                var c = characters[i];
                var occurrence = occurrences.getOrDefault(i, 0);
                if ('1' == c) {
                    occurrence++;
                }
                occurrences.put(i, occurrence);
            }
        }

        occurrences.forEach((k, v) -> {
            if (v > size/2) {
                System.out.println(k + ":1");
            } else {
                System.out.println(k + ":0");
            }
        });

        // 111011110011 -> 3827
        // 000100001100 -> 268

        System.out.println(3827 * 268);
    }
}
