package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day14.txt"));
        Map<Integer, Long> mem = new HashMap<>();
        String mask = "";
        for (String line : lines) {
            String[] words = line.split(" ");
            if (words[0].equals("mask")) {
                mask = words[2];
            } else {
                int memAddr = Integer.parseInt(words[0].substring(4, words[0].length() - 1));
                long originalValue = Long.parseLong(words[2]);
                long maskedValue = 0;
                for (int i = 0; i < 36; i++) {
                    maskedValue = maskedValue << 1;
                    char maskChar = mask.charAt(i);
                    if (maskChar == '1') {
                        maskedValue += 1;
                    } else if (maskChar == '0') {
                        maskedValue += 0;
                    } else {
                        long bitMask = (long)Math.pow(2, (35-i)); // to extract the current bit
                        long originalBitValue = (originalValue & bitMask);
                        long originalBit = originalBitValue >> (35 - i);
                        maskedValue += originalBit;
                    }
                }
                mem.put(memAddr, maskedValue);
            }
        }
//        System.out.println(mem);
        long sum = mem.values().stream().mapToLong(Long::longValue).sum();
        System.out.println(sum);
    }
}
