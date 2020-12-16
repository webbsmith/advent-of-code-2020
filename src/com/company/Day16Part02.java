package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Day16Part02 {

    private static int LIMIT = 1000;

    private static final List<List<String>> validValues = new ArrayList<>(LIMIT);

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day16.txt"));

        for (int i = 0; i < LIMIT; i++) {
            validValues.add(new ArrayList<>());
        }

        List<String> allFieldNames = new ArrayList<>();

        Iterator<String> lineIterator = lines.iterator();
        String line = lineIterator.next();

        while (!line.isEmpty()) {
            String fieldName = line.substring(0, line.indexOf(':'));
            allFieldNames.add(fieldName);
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

        // filter valid tickets
        List<List<Integer>> validTickets = ticketList.stream()
                .filter(Day16Part02::isValidTicket).collect(Collectors.toList());

        // create possible order list each containing all fields initially
        List<List<String>> possibleFieldOrder = new ArrayList<>();
        for (int i = 0; i < allFieldNames.size(); i++) {
            possibleFieldOrder.add(new ArrayList<>(List.copyOf(allFieldNames)));
        }

        // remove invalid field order possibilities
        for (List<Integer> ticket : validTickets) {
            for (int i = 0; i < ticket.size(); i++) {
                int value = ticket.get(i);
                List<String> validFields = validValues.get(value);
                possibleFieldOrder.get(i).removeIf(field -> !validFields.contains(field));
            }
        }

        List<String> determinedFieldOrder = new ArrayList<>(allFieldNames.size());
        // initialization is required since fields are not determined in order
        for (int i = 0; i < allFieldNames.size(); i++) {
            determinedFieldOrder.add(null);
        }

        for (int determinedFieldCount = 0; determinedFieldCount < allFieldNames.size(); ) {
            for (int i = 0; i < possibleFieldOrder.size(); i++) {
                List<String> fieldOptions = possibleFieldOrder.get(i);
                if (fieldOptions.size() == 1) {
                    String determinedField = fieldOptions.get(0);
                    determinedFieldOrder.set(i, determinedField);
                    for (List<String> otherFields : possibleFieldOrder) {
                        otherFields.removeIf(determinedField::equals);
                    }
                    determinedFieldCount++;
                }
            }
        }

        long solution = 1;
        for (int i = 0; i < determinedFieldOrder.size(); i++) {
            String fieldName = determinedFieldOrder.get(i);
            if (fieldName.startsWith("departure")) {
                solution *= myTicket.get(i);
            }
        }
        System.out.println(solution);
    }

    private static boolean isValidTicket(List<Integer> ticket) {
        for (Integer value : ticket) {
            if (validValues.get(value).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> getTicketValues(String line) {
        return Arrays.stream(line.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }
}
