package com.google.util;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ReadFileUtil {

    public static List<String> readLinesFrom(String file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        String data = readFromInputStream(inputStream);

        return Arrays.asList(data.split("\n"));
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
