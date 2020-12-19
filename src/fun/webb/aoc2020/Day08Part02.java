package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day08Part02 {
    public static void main(String[] args) throws IOException {
        int acc = 0;
        int jmpToChange = 0;
        boolean repeatFound = true;

        while (repeatFound) {
            jmpToChange++;
            int jmpCount = 0;
            List<String> lines = Files.readAllLines(Path.of("inputs/day08.txt"));
            acc = 0;
            repeatFound = false;

            for (int i = 0; i < lines.size(); ) {
                String line = lines.get(i);
                int moveBy = 1;
                if (line == null) {
                    repeatFound = true;
                    break;
                } else if (line.startsWith("jmp")) {
                    jmpCount++;
                    if (jmpCount != jmpToChange) { // change to nop by ignoring jmp command
                        moveBy = Integer.parseInt(line.substring(4));
                    }
                } else if (line.startsWith("acc")) {
                    acc += Integer.parseInt(line.substring(4));
                }
                lines.set(i, null);
                i += moveBy;
            }

        }
        System.out.println(acc);
    }
}
