package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

// too slow, doesn't complete for days
public class Day13Part02 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day13.txt"));
        Map<Long, Integer> orderToBusIdMap = new HashMap<>();

        String[] idList = lines.get(1).split(",");
        int largestId = -1;
        int largestIdIndex = -1;
        for (int i = 0; i < idList.length; i++) {
            if (!idList[i].equals("x")) {
                int value = Integer.parseInt(idList[i]);
                orderToBusIdMap.put((long) i, value);
                if (value > largestId) {
                    largestId = value;
                    largestIdIndex = i;
                }
            }
        }
        long startingIdx = largestId;
        System.out.println(startingIdx);
        long earliestTimestamp = -1;
        for (long i = startingIdx; i < Long.MAX_VALUE; i += largestId) {
            System.out.println(i);
            long firstIdx = i - largestIdIndex;
            Iterator<Map.Entry<Long, Integer>> iterator = orderToBusIdMap.entrySet().iterator();
            boolean matchFound = true;
            while (iterator.hasNext()) {
                Map.Entry<Long, Integer> entry = iterator.next();
                long requiredPosition = firstIdx + entry.getKey();
                int orderId = entry.getValue();
                if (requiredPosition % orderId != 0) { // requiredPosition % orderId
                    matchFound = false;
                    break;
                }
            }
            if (matchFound) {
                earliestTimestamp = firstIdx;
                break;
            }
        }
        System.out.println(earliestTimestamp);
    }
}
