package com.google.util;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class Day4 {

    public static void main(String[] args) throws IOException {
        var file = "/home/stefan/Desktop/advent-of-code/4/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var guesses = getGuesses(lines);
        var players = getPlayers(lines);

        for (var guess : guesses) {
            players.forEach(p -> p.mark(guess));

            var winners = players.stream()
                    .filter(Player::isBingo)
                    .toList();

            if (winners.size() > 1) {
                throw new RuntimeException("Dafuq " + winners.size());
            }
            if (winners.size() == 1) {
                var winner = winners.get(0);
                var sum = winner.getSum();
                System.out.println(sum * guess);
                break;
            }
        }
    }

    private static List<Integer> getGuesses(List<String> lines) {
        var first = lines.get(0);
        return Arrays.stream(first.split(","))
                .map(Integer::parseInt)
                .toList();
    }

    private static List<Player> getPlayers(List<String> lines) {
        List<Player> players = new ArrayList<>();
        List<List<Integer>> field = new ArrayList<>();
        for (var i = 2; i < lines.size(); i++) {
            var line = lines.get(i);

            if (line.isBlank()) {
                players.add(new Player(field));
                field = new ArrayList<>();
                continue;
            }

            var row = Arrays.stream(line.split(" "))
                    .filter(s -> !s.isBlank())
                    .map(Integer::parseInt)
                    .toList();
            field.add(new ArrayList<>(row));
        }

        players.add(new Player(field));
        return players;
    }

    private record Player(List<List<Integer>> field) {

        public void mark(int number) {
            for (List<Integer> row : field) {
                for (var x = 0; x < row.size(); x++) {
                    var cell = row.get(x);
                    if (cell == number) {
                        row.set(x, -1);
                    }
                }
            }
        }

        public int getSum() {
            return field.stream()
                    .flatMap(Collection::stream)
                    .filter(i -> i >= 0)
                    .reduce(0, Integer::sum);
        }

        public boolean isBingo() {
            var rows = field.size();
            var columns = field.get(0).size();

            var isRowBingo = IntStream.range(0, rows)
                    .anyMatch(this::isRowBingo);

            var isColumnBingo = IntStream.range(0, columns)
                    .anyMatch(this::isColumnBingo);

            return isRowBingo || isColumnBingo;
        }

        private boolean isRowBingo(int y) {
            return field.get(y).stream()
                    .noneMatch(i -> i >= 0);
        }

        private boolean isColumnBingo(int x) {
            return field.stream()
                    .map(r -> r.get(x))
                    .noneMatch(i -> i >= 0);
        }
    }
}
