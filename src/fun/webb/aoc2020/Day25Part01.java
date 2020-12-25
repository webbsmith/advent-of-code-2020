package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day25Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day25.txt"));
        long cardPublicKey = Long.parseLong(lines.get(0));
        long doorPublicKey = Long.parseLong(lines.get(1));
        long value = 1;
        long subjectNumber = 7;

        long cardLoopSize = 0;
        while (value != cardPublicKey) {
            cardLoopSize++;
            value *= subjectNumber;
            value %= 20201227;
        }
        System.out.println(cardLoopSize);

        long doorLoopSize = 0;
        value = 1;
        while (value != doorPublicKey) {
            doorLoopSize++;
            value *= subjectNumber;
            value %= 20201227;
        }
        System.out.println(doorLoopSize);

        long encryptionLoopSize = 0;
        value = 1;
        subjectNumber = cardPublicKey;
        while (encryptionLoopSize < doorLoopSize) {
            encryptionLoopSize++;
            value *= subjectNumber;
            value %= 20201227;
        }
        System.out.println(value);
    }
}
