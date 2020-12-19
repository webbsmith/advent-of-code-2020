package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day11Part01 {
    public static void main(String[] args) throws IOException {
        List<String> currentState = Files.readAllLines(Path.of("inputs/day11.txt"));
        List<String> nextState = createEmptyState(currentState);

        while (true) {
            for (int y = 0, len = currentState.size(); y < len; y++) {
                for (int x = 0, lineLen = currentState.get(y).length(); x < lineLen; x++) {
                    updateSeatState(currentState, nextState, x, y);
                }
            }
            if (currentState.equals(nextState)) break;
            currentState = List.copyOf(nextState);
        }
        System.out.println(countOccupiedSeats(nextState));
    }

    private static List<String> createEmptyState(List<String> currentState) {
        List<String> emptyState = new ArrayList<>();
        for (String line : currentState)
            emptyState.add(new String(new char[line.length()]));

        return emptyState;
    }


    private static int countOccupiedSeats(List<String> nextState) {
        return nextState.stream()
                .mapToInt(line -> line.chars()
                        .map(c -> (c == '#') ? 1 : 0).sum())
                .sum();
    }

    private static void updateSeatState(List<String> currentState, List<String> nextState, int x, int y) {
        String curLine = currentState.get(y);
        char spot = curLine.charAt(x);
        if (seatIsEmpty(spot) && adjacentSeatsAreEmpty(x, y, currentState)) {
            occupySeat(x, y, nextState);
        } else if (seatIsOccupied(spot) && atLeast4AdjacentAreOccupied(x, y, currentState)) {
            emptySeat(x, y, nextState);
        } else {
            mimicCurrentState(spot, x, y, nextState);
        }
    }

    private static void mimicCurrentState(char curVal, int x, int y, List<String> nextState) {
        setSeatState(x, y, nextState, curVal);
    }

    private static void emptySeat(int x, int y, List<String> nextState) {
        setSeatState(x, y, nextState, 'L');
    }

    private static boolean atLeast4AdjacentAreOccupied(int x, int y, List<String> currentState) {
        return countSurroundingOccupants(x, y, currentState) > 3;
    }

    private static boolean seatIsOccupied(char spot) {
        return spot == '#';
    }

    private static void occupySeat(int x, int y, List<String> nextState) {
        setSeatState(x, y, nextState, '#');
    }

    private static void setSeatState(int x, int y, List<String> nextState, char value) {
        char[] mutableString = nextState.get(y).toCharArray();
        mutableString[x] = value;
        nextState.set(y, new String(mutableString));
    }

    private static boolean adjacentSeatsAreEmpty(int x, int y, List<String> currentState) {
        return countSurroundingOccupants(x, y, currentState) == 0;
    }

    private static int countSurroundingOccupants(int x, int y, List<String> currentState) {

        String thisLine = currentState.get(y);
        String prevLine = (y - 1) < 0 ? null : currentState.get(y - 1);
        String nextLine = (y + 1) >= currentState.size() ? null : currentState.get(y + 1);

        return countOccupantsOnOtherRow(x, prevLine)
                + countOccupantsOnOtherRow(x, nextLine)
                + countOccupantsOnThisRow(x, thisLine);
    }

    private static int countOccupantsOnThisRow(int x, String row) {
        int count = 0;
        for (int x1 = x - 1; x1 <= x + 1; x1 += 2) { // skips current position
            if (x1 >= 0 && x1 < row.length() && row.charAt(x1) == '#') count++;
        }
        return count;
    }

    private static int countOccupantsOnOtherRow(int x, String row) {
        int count = 0;
        if (row != null) {
            for (int x1 = x - 1; x1 <= x + 1; x1++) {
                if (x1 >= 0 && x1 < row.length() && row.charAt(x1) == '#') count++;
            }
        }
        return count;
    }

    private static boolean seatIsEmpty(char spot) {
        return spot == 'L';
    }
}
