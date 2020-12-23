package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Takes hours to run!
public class Day23Part02 {
    public static void main(String[] args) throws IOException {
        String input = Files.readAllLines(Path.of("inputs/day23.txt")).get(0);
        int moves = 10_000_000;
        List<Integer> cups = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            cups.add(Integer.parseInt(input.substring(i, i + 1)));
        }

        int highestValue = 1_000_000;
        for (int n = 10; n <= 1_000_000; n++) {
            cups.add(n);
        }

        int size = highestValue;
        for (int m = 0, currentIdx = 0; m < moves; m++, currentIdx = (currentIdx + 1) % size) {
            System.out.println(m);
            int cup = cups.get(currentIdx);
            int pickupIdx = (currentIdx + 1) % cups.size();
            List<Integer> pickedUp = new ArrayList<>();
            pickedUp.add(cups.remove(pickupIdx));
            if (pickupIdx != 0) pickupIdx %= cups.size();
            pickedUp.add(cups.remove(pickupIdx));
            if (pickupIdx != 0) pickupIdx %= cups.size();
            pickedUp.add(cups.remove(pickupIdx));

            int cupToInsertAfter = cup;
            boolean positionFound = false;
            while (!positionFound) {
                cupToInsertAfter--;
                if (cupToInsertAfter < 1) {
                    cupToInsertAfter = highestValue;
                }
                if (pickedUp.contains(cupToInsertAfter)) {
                    continue;
                }

                for (int i = 0; i < cups.size(); i++) {
                    if (cups.get(i) == cupToInsertAfter) {
                        cups.addAll(i + 1, pickedUp);
                        positionFound = true;
                        if (i < currentIdx) {
                            // if cups were inserted in front, continue with the next cup
                            if (currentIdx < size - 3) {
                                currentIdx += 3;
                            } else if (currentIdx == size - 3) {
                                currentIdx += 2;
                            } else if (currentIdx == size - 2) {
                                currentIdx += 1;
                            }
                        }
                        break;
                    }
                }
            }
        }

        long product = 1;
        int idxOf1 = cups.indexOf(1);
        if (idxOf1 == highestValue - 1) {
            idxOf1 = -1;
        }
        for (int i = idxOf1 + 1; i < idxOf1 + 3; i = (i + 1) % size) {
            product *= cups.get(i);
        }
        System.out.println(product);
    }
}
