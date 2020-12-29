package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Faster solution to https://adventofcode.com/2020/day/23 part 2
//
// Implementation of Winston's array linked list solution.
//
// If the circle is:
//   2 8 4 5 7 3 9 6 1
// then the data is stored as
// data:  X 2 8 9 5 7 1 3 4 6
// idx :  0 1 2 3 4 5 6 7 8 9

public class Day23Part02b {

    public static final int HIGHEST_VALUE = 1_000_000;
    public static final int MOVES = 10_000_000;

    public static void main(String[] args) throws IOException {
        int[] cups = initializeCups("inputs/day23.txt");

        int nextCup = cups[HIGHEST_VALUE]; // Start with the first cup (which cups[HIGHEST_VALUE] points to)
        for (int m = 0; m < MOVES; m++) {
            int cup = nextCup;

            int firstCupToMove = cups[cup];
            int midCupToMove = cups[firstCupToMove];
            int lastCupToMove = cups[midCupToMove];

            // Determine destination for cups to move
            int dest = cup - 1;
            if (dest < 1) dest = HIGHEST_VALUE;
            while (dest == firstCupToMove || dest == midCupToMove || dest == lastCupToMove) {
                dest--;
                if (dest < 1) dest = HIGHEST_VALUE;
            }

            // Get pointer from destination (destination's original next cup)
            int destinationNextCup = cups[dest];

            // First point the current cup to the one after the group that is moving
            cups[cup] = cups[lastCupToMove];
            // Then insert the cups at the destination
            cups[lastCupToMove] = destinationNextCup;
            cups[dest] = firstCupToMove;

            nextCup = cups[cup];
        }

        System.out.println((long)cups[1] * (long)cups[cups[1]]);
    }

    private static int[] initializeCups(String file) throws IOException {
        String input = Files.readAllLines(Path.of(file)).get(0);

        // Add data from input file
        // Each entry in the array holds the index of the next entry
        int[] cups = new int[HIGHEST_VALUE + 1];
        int firstCup = Integer.parseInt(input.substring(0, 1)); // Save first cup to point the last cup to
        int nextIdx = firstCup; // Store the next entry at the value of the first cup
        for (int i = 1; i < input.length(); i++) {
            int val = Integer.parseInt(input.substring(i, i + 1));
            cups[nextIdx] = val;
            nextIdx = val;
        }

        // Add numbers 10 through 1 million
        for (int n = 10; n <= HIGHEST_VALUE; n++) {
            cups[nextIdx] = n;
            nextIdx = n;
        }
        // At this point cups[999,999] -> 1,000,000
        // nextIdx = 1,000,000
        cups[nextIdx] = firstCup; // Complete the circle by pointing back to the beginning
        return cups;
    }

    private static void printCups(int[] cups) {
        StringBuilder sb = new StringBuilder();
        int next = 1;
        for (int i = 1; i < cups.length; i++) {
            sb.append(cups[next]).append(" ");
            next = cups[next];
        }
        System.out.println(sb);
    }

}
