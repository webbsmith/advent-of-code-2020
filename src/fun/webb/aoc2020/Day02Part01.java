package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day02Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day02.txt"));
        AtomicInteger meetsRequirements = new AtomicInteger();

        lines.forEach(line -> {
            String[] split = line.split(" ");
            String countRange = split[0];
            char letter = split[1].charAt(0);
            String password = split[2];

            String[] minToMax = countRange.split("-");
            int min = Integer.parseInt(minToMax[0]);
            int max = Integer.parseInt(minToMax[1]);

            int letterOccurrences = 0;
            for (char c : password.toCharArray())
                if (c == letter)
                    letterOccurrences++;

            if (letterOccurrences >= min && letterOccurrences <= max)
                meetsRequirements.incrementAndGet();
        });

        System.out.println("Passwords meeting requirements = " + meetsRequirements.get());
    }
}
