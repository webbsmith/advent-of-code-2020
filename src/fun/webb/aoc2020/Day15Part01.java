package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day15Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day15.txt"));
        List<Integer> numbers = Arrays.stream(lines.get(0).split(",")).map(Integer::valueOf).collect(Collectors.toList());

        Map<Integer, Integer> mostRecentIndex = new HashMap<>();
        for (int i = 0; i < numbers.size(); i++) {
            mostRecentIndex.put(numbers.get(i), i);
        }

        int num = numbers.get(numbers.size()-1);
        for (int i = numbers.size() - 1; ; i++) {
            if (i == 2019) { // 2019 because i'm counting 0
                System.out.println("2020th number: " + num);
                break;
            }
            Integer lastIndex = mostRecentIndex.getOrDefault(num, i);
            int nextNum = i - lastIndex; // 0 if first occurrence
            mostRecentIndex.put(num, i);
            num = nextNum;
        }

    }
}
