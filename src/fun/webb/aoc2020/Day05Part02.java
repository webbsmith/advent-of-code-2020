package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day05Part02 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day05.txt"));
        List<Integer> takenSeats = new ArrayList<>();
        for (String l : lines) {
            String binaryString = l.replaceAll("F|L", "0").replaceAll("B|R", "1");
            int row = Integer.parseInt(binaryString.substring(0, 7), 2);
            int column = Integer.parseInt(binaryString.substring(7, 10), 2);
            takenSeats.add(row * 8 + column);
        }
        Collections.sort(takenSeats);
        for (int i = 0; i < takenSeats.size() - 1; i++) {
            if (takenSeats.get(i) != takenSeats.get(i+1) - 1) {
                System.out.println(takenSeats.get(i) + "..." + takenSeats.get(i+1));
                return;
            }
        }

    }
}
