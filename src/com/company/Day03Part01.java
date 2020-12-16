package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day03Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day03.txt"));
        int x = 0, y = 0, treesHit = 0;
        while (y < lines.size()) {
            if (x >= lines.get(y).length())
                x -= lines.get(y).length();
            if (lines.get(y).charAt(x) == '#')
                treesHit++;
            x += 3;
            y += 1;
        }
        System.out.println("trees hit = " + treesHit);
    }
}
