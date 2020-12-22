package fun.webb.aoc2020;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day13Part02b {
    // Implementation of the Chinese Remainder Theorem (https://youtu.be/ru7mWZJlRQg)
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day13.txt"));
        String[] tokens = lines.get(1).split(",");
        long[] numbers = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String s = tokens[i];
            if (s.equals("x")) numbers[i] = 0;
            else numbers[i] = Long.parseLong(s);
        }

        BigInteger[] chineseRemainderStep1 = new BigInteger[tokens.length];
        BigInteger productOfAll = BigInteger.ONE;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] == 0) continue;
            productOfAll = productOfAll.multiply(big(numbers[i]));
            long productOfOthers = 1;
            for (int j = 0; j < numbers.length; j++) {
                if (i == j || numbers[j] == 0) continue;
                productOfOthers *= numbers[j];
            }

            BigInteger thisModNumberEqualsI = big(productOfOthers);
            long lookingForModResult = (numbers[i] - i) % numbers[i];
            if (lookingForModResult < 0) {
                lookingForModResult += numbers[i];
            }
            for (BigInteger increment = big(productOfOthers); ;) {
                BigInteger num = big(numbers[i]);
                int remainder = thisModNumberEqualsI.mod(num).intValueExact();
                if (remainder == lookingForModResult) break;
                thisModNumberEqualsI = thisModNumberEqualsI.add(increment);
            }
            chineseRemainderStep1[i] = thisModNumberEqualsI;
        }

        BigInteger chineseRemainderStep1Sum = BigInteger.ZERO;
        for (BigInteger num : chineseRemainderStep1) {
            if (num == null) continue;
            chineseRemainderStep1Sum = chineseRemainderStep1Sum.add(num);
        }

        BigInteger result = chineseRemainderStep1Sum;
        do {
            result = result.subtract(productOfAll);
        } while (result.compareTo(BigInteger.ZERO) > 0);
        result = result.add(productOfAll);

        System.out.println(result);
    }

    private static BigInteger big(long number) {
        return BigInteger.valueOf(number);
    }
}
