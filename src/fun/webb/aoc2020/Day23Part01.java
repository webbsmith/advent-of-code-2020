package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day23Part01 {
    public static void main(String[] args) throws IOException {
        String input = Files.readAllLines(Path.of("inputs/day23.txt")).get(0);
        int moves = 100;
        List<Integer> cups = new ArrayList<>();
        int size = input.length();
        for (int i = 0; i < size; i++) {
            cups.add(Integer.parseInt(input.substring(i, i + 1)));
        }

        for (int m = 0, currentIdx = 0; m < moves; m++, currentIdx = (currentIdx + 1) % size) {
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
                    cupToInsertAfter = 9;
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

        StringBuilder sb = new StringBuilder();
        int idxOf1 = cups.indexOf(1);
        if (idxOf1 == 8) {
            idxOf1 = -1;
        }
        for (int i = idxOf1 + 1; sb.length() < 8; i = (i + 1) % size) {
            sb.append(cups.get(i));
        }
        System.out.println(sb.toString());
    }
}
