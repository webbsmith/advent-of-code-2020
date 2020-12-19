package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day03Part02 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day03.txt"));
        List<Long> treesHit = new ArrayList<>();
        treesHit.add(calcTreesHit(lines, 1, 1));
        treesHit.add(calcTreesHit(lines, 3, 1));
        treesHit.add(calcTreesHit(lines, 5, 1));
        treesHit.add(calcTreesHit(lines, 7, 1));
        treesHit.add(calcTreesHit(lines, 1, 2));
        treesHit.forEach(System.out::println);
        long product = treesHit.stream().reduce(1L, (subtotal, next) ->  subtotal * next);
        System.out.println("result = " + product);
    }

    private static long calcTreesHit(List<String> lines, int xSlope, int ySlope) {
        int x = 0, y = 0, treesHit = 0;
        while (y < lines.size()) {
            if (x >= lines.get(y).length())
                x -= lines.get(y).length();
            if (lines.get(y).charAt(x) == '#')
                treesHit++;
            x += xSlope;
            y += ySlope;
        }
        return treesHit;
    }
}
