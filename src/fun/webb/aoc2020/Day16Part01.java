package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Day16Part01 {

    private static int LIMIT = 1000;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day16.txt"));

        List<List<String>> validValues = new ArrayList<>(LIMIT);
        for (int i = 0; i < LIMIT; i++) {
            validValues.add(new ArrayList<>());
        }

        Iterator<String> lineIterator = lines.iterator();
        String line = lineIterator.next();

        while (!line.isEmpty()) {
            String fieldName = line.substring(0, line.indexOf(':'));
            String[] firstRange = line.substring(line.indexOf(':') + 2, line.indexOf(" or ", - 1)).split("-");
            String[] secondRange = line.substring(line.indexOf(" or ") + 4).split("-");

            int firstRangeStart = parseInt(firstRange[0]);
            int firstRangeEnd = parseInt(firstRange[1]);
            int secondRangeStart = parseInt(secondRange[0]);
            int secondRangeEnd = parseInt(secondRange[1]);

            for (int i = firstRangeStart; i <= firstRangeEnd; i++) {
                validValues.get(i).add(fieldName);
            }

            for (int i = secondRangeStart; i <= secondRangeEnd; i++) {
                validValues.get(i).add(fieldName);
            }

            line = lineIterator.next();
        }

        lineIterator.next();  // skip "your ticket:" line
        List<Integer> myTicket = getTicketValues(lineIterator.next());

        // skip blank and "nearby tickets:" lines
        lineIterator.next();
        lineIterator.next();

        List<List<Integer>> ticketList = new ArrayList<>();
        while (lineIterator.hasNext()) {
            ticketList.add(getTicketValues(lineIterator.next()));
        }

        // parse each ticket for invalid values
        List<Integer> invalidValues = new ArrayList<>();
        for (List<Integer> ticket : ticketList) {
            for (Integer value : ticket) {
                if (validValues.get(value).isEmpty()) {
                    invalidValues.add(value);
                    break;
                }
            }
        }
        System.out.println(invalidValues.stream().mapToInt(Integer::intValue).sum());
    }

    private static List<Integer> getTicketValues(String line) {
        return Arrays.stream(line.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }
}
