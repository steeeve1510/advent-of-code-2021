package com.google;

import com.google.util.ReadFileUtil;
import lombok.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {

    private static final String START = "start";
    private static final String END = "end";

    public static void main(String[] args) throws IOException {
        var file = "/12/input";

        var lines = ReadFileUtil.readLinesFrom(file);

        var caves = parse(lines);

        var start = caves.get(START);
        var paths = List.of(
                new Path(List.of(start))
        );

        while (true) {
            var oldSize = paths.size();
            paths = paths.stream()
                    .map(Path::step)
                    .flatMap(Collection::stream)
                    .toList();
            var newSize = paths.size();
            if (oldSize == newSize) {
                break;
            }
        }

        System.out.println(paths.size());
    }

    private static Map<String, Cave> parse(List<String> lines) {
        var caves = lines.stream()
                .flatMap(Day12::parse)
                .collect(Collectors.toMap(
                        Cave::getId,
                        Function.identity(),
                        (c1, c2) -> {
                            var adjacent = Stream.of(c1, c2)
                                    .map(Cave::getAdjacent)
                                    .flatMap(Collection::stream)
                                    .distinct()
                                    .toList();
                            return new Cave(c1.getId(), c1.isSmall(), adjacent);
                        }
                ));
        caves.values().forEach(c -> {
            var fixed = c.getAdjacent().stream()
                    .map(a -> caves.get(a.getId()))
                    .toList();
            c.setAdjacent(fixed);
        });
        return caves;
    }

    private static Stream<Cave> parse(String line) {
        var parts = line.split("-");
        var fromId = parts[0];
        var toId = parts[1];
        var from = new Cave(fromId, fromId.equals(fromId.toLowerCase()));
        var to = new Cave(toId, toId.equals(toId.toLowerCase()));
        from.getAdjacent().add(to);
        to.getAdjacent().add(from);
        return Stream.of(from, to);
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @ToString(onlyExplicitlyIncluded = true)
    private static class Cave {
        @EqualsAndHashCode.Include
        @ToString.Include
        private final String id;
        @ToString.Include
        private final boolean isSmall;
        @Setter
        private List<Cave> adjacent = new ArrayList<>();
    }

    private record Path(List<Cave> caves) {

        public List<Path> step() {
            if (containsEnd()) {
                return List.of(this);
            }
            var last = caves.get(caves.size() - 1);
            return last.getAdjacent().stream()
                    .map(n -> Stream.of(caves, List.of(n))
                            .flatMap(Collection::stream)
                            .toList()
                    )
                    .map(Path::new)
                    .filter(Path::isValid)
                    .toList();
        }

        private boolean containsEnd() {
            var last = caves.get(caves.size() - 1);
            return END.equals(last.getId());
        }

        private boolean isValid() {
            var smallCaveVisits = caves.stream()
                    .filter(Cave::isSmall)
                    .collect(Collectors.toMap(
                            Cave::getId,
                            (c) -> 1,
                            Integer::sum
                    ));

            var twiceVisits = smallCaveVisits.values().stream()
                    .filter(v -> v == 2)
                    .count();

            var largerVisits = smallCaveVisits.values().stream()
                    .filter(v -> v > 2)
                    .count();

            var startVisits = smallCaveVisits.get(START);
            var endVisits = smallCaveVisits.getOrDefault(END, 1);

            return startVisits == 1 && endVisits == 1 && twiceVisits <= 1 && largerVisits == 0;
        }
    }
}
