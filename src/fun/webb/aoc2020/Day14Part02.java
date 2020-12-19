package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14Part02 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day14.txt"));
        Map<Long, Long> mem = new HashMap<>();
        String mask = "";
        for (String line : lines) {
            String[] words = line.split(" ");
            if (words[0].equals("mask")) {
                mask = words[2];
            } else {
                long originalMemAddr = Long.parseLong(words[0].substring(4, words[0].length() - 1));
                long valueToWrite = Long.parseLong(words[2]);
                StringBuilder maskedAddrBuilder = new StringBuilder();
                int xCount = 0;
                for (int i = 0; i < 36; i++) {
                    char maskedBit = mask.charAt(i);
                    if (maskedBit == '1') {
                        maskedAddrBuilder.append(maskedBit);
                    } else if (maskedBit == 'X') {
                        maskedAddrBuilder.append(maskedBit);
                        xCount++;
                    } else {
                        maskedAddrBuilder.append((originalMemAddr & (long)Math.pow(2, 35 - i)) >> 35-i); // Extract one bit
                    }
                }
                String maskedAddr = maskedAddrBuilder.toString();
                long addresses = (long) Math.pow(2, xCount);
                for (long floatVal = 0; floatVal < addresses; floatVal++) {
                    int xRemaining = xCount;
                    long memAddr = 0;
                    for (int i = 0; i < 36; i++) {
                        memAddr = memAddr << 1;
                        char c = maskedAddr.charAt(i);
                        if (c == '1') {
                            memAddr += 1;
                        } else if (c == '0') {
                            memAddr += 0;
                        } else {
                            memAddr += (floatVal >> --xRemaining) & 1;
                        }
                    }
                    mem.put(memAddr, valueToWrite);
                }
            }
        }
//        System.out.println(mem);
        long sum = mem.values().stream().mapToLong(Long::longValue).sum();
        System.out.println(sum);
    }
}
