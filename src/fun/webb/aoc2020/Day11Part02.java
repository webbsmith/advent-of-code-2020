package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day11Part02 {
    public static void main(String[] args) throws IOException {
        List<String> currentState = Files.readAllLines(Path.of("inputs/day11.txt"));
        List<String> nextState = createEmptyState(currentState);

        int iterCount = 0;
        while (true) {
            iterCount++;
            for (int y = 0, len = currentState.size(); y < len; y++) {
                for (int x = 0, lineLen = currentState.get(y).length(); x < lineLen; x++) {
                    updateSeatState(currentState, nextState, x, y);
                }
            }
            if (currentState.equals(nextState)) break;
            System.out.println(iterCount);
            currentState.forEach(System.out::println);

            currentState = List.copyOf(nextState);
        }

        System.out.println(iterCount);
        currentState.forEach(System.out::println);
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
        } else if (seatIsOccupied(spot) && atLeast5AdjacentAreOccupied(x, y, currentState)) {
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

    private static boolean atLeast5AdjacentAreOccupied(int x, int y, List<String> currentState) {
        return countSurroundingOccupants(x, y, currentState) > 4;
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

        int count = countOccupantsBelow(x, y, currentState)
                + countOccupantsAbove(x, y, currentState)
                + countOccupantsOnThisRow(x, thisLine);

        return count;
    }

    private static int countOccupantsOnThisRow(int x, String row) {
        int count = 0;
        // To the left
        for (int x1 = x - 1; x1 >= 0 && x1 > x - 9; x1--) {
            char c = row.charAt(x1);
            if (seatIsOccupied(c)) {
                count++;
                break;
            } else if (seatIsEmpty(c)) {
                break;
            }
        }
        // To the right
        for (int x1 = x + 1; x1 < row.length() && x1 < x + 9; x1++) {
            char c = row.charAt(x1);
            if (seatIsOccupied(c)) {
                count++;
                break;
            } else if (seatIsEmpty(c)) {
                break;
            }
        }
        return count;
    }

    private static int countOccupantsBelow(int x, int y, List<String> currentState) {
        int limit = 8;
        int count = 0;
        // Check left diagonal
        for (int y1 = y + 1, x1 = x - 1; y1 < currentState.size() && y1 < y + 1 + limit && x1 >= 0 && x1 > x - 1 - limit; y1++, x1--) {
            String row = currentState.get(y1);
            char c = row.charAt(x1);
            if (seatIsOccupied(c)) {
                count++;
                break;
            } else if (seatIsEmpty(c)) {
                break;
            }
        }
        // Check right diagonal
        for (int y1 = y + 1, x1 = x + 1; y1 < currentState.size() && y1 < y + 1 + limit && x1 < currentState.get(y1).length() && x1 < x + 1 + limit; y1++, x1++) {
            String row = currentState.get(y1);
            char c = row.charAt(x1);
            if (seatIsOccupied(c)) {
                count++;
                break;
            } else if (seatIsEmpty(c)) {
                break;
            }
        }
        // Check below
        for (int y1 = y + 1, i = 0; y1 < currentState.size() && y1 < y + 1 + limit && i < limit; y1++, i++) {
            String row = currentState.get(y1);
            char c = row.charAt(x);
            if (seatIsOccupied(c)) {
                count++;
                break;
            }else if (seatIsEmpty(c)) {
                break;
            }
        }
        return count;
    }

    private static int countOccupantsAbove(int x, int y, List<String> currentState) {
        int limit = 8;
        int count = 0;
        // Check left diagonal
        for (int y1 = y - 1, x1 = x - 1; y1 >= 0 && y1 > y - 1 - limit && x1 >= 0 && x1 > x - 1 - limit; y1--, x1--) {
            String row = currentState.get(y1);
            char c = row.charAt(x1);
            if (seatIsOccupied(c)) {
                count++;
                break;
            } else if (seatIsEmpty(c)) {
                break;
            }
        }
        // Check right diagonal
        for (int y1 = y - 1, x1 = x + 1; y1 >= 0 && y1 > y - 1 - limit && x1 < currentState.get(y1).length() && x1 < x + 1 + limit; y1--, x1++) {
            String row = currentState.get(y1);
            char c = row.charAt(x1);
            if (seatIsOccupied(c)) {
                count++;
                break;
            } else if (seatIsEmpty(c)) {
                break;
            }
        }
        // Check above
        for (int y1 = y - 1, i = 0; y1 >= 0 && y1 > y - 1 - limit && i < limit; y1--, i++) {
            String row = currentState.get(y1);
            char c = row.charAt(x);
            if (seatIsOccupied(c)) {
                count++;
                break;
            } else if (seatIsEmpty(c)) {
                break;
            }
        }
        return count;
    }

    private static boolean seatIsEmpty(char spot) {
        return spot == 'L';
    }
}
