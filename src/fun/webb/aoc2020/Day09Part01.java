package fun.webb.aoc2020;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day09Part01 {
    static final int PREVIOUS_TO_CHECK = 25;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day09.txt"));

        HashSet<Integer> recent25 = new HashSet<>();
        int oldestIdx = 0;
        Integer oldest = Integer.parseInt(lines.get(oldestIdx));
        recent25.add(oldest);

        for (int i = 1; i < PREVIOUS_TO_CHECK; i++) {
            recent25.add(Integer.parseInt(lines.get(i)));
        }

        for (int i = PREVIOUS_TO_CHECK; i < lines.size(); i++) {
            final int current = Integer.parseInt(lines.get(i));
            if (recent25.stream().noneMatch(e -> recent25.contains(current - e))) {
                System.out.println("invalid: " + current);
                break;
            }
            recent25.remove(Integer.parseInt(lines.get(oldestIdx++)));
            recent25.add(current);
        }

    }
}
