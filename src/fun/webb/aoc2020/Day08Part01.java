package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day08Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day08.txt"));
        int acc = 0;
        for (int i = 0; ;) {
            String line = lines.get(i);
            int moveBy = 1;
            if (line == null) {
                break;
            } else if (line.startsWith("jmp")) {
                moveBy = Integer.parseInt(line.substring(4));
            } else if (line.startsWith("acc")) {
                acc += Integer.parseInt(line.substring(4));
            }
            lines.set(i, null);
            i += moveBy;
        }
        System.out.println(acc);
    }
}
