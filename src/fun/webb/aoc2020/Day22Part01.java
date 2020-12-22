package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day22Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day22.txt"));
        Queue<Integer> deck1 = new LinkedList<>();
        int i;
        for (i = 1; ; i++) {
            if (lines.get(i).isEmpty()) break;
            deck1.add(Integer.parseInt(lines.get(i)));
        }
        Queue<Integer> deck2 = new LinkedList<>();
        for (i = i + 2; i < lines.size(); i++) {
            deck2.add(Integer.parseInt(lines.get(i)));
        }
        System.out.println(deck1);
        System.out.println(deck2);
        for (int round = 1; !deck1.isEmpty() && !deck2.isEmpty(); round++) {
            int deck1Card = deck1.remove();
            int deck2Card = deck2.remove();
            if (deck1Card > deck2Card) {
                System.out.println("Round " + round + " result: Player 1 wins");
                deck1.add(deck1Card);
                deck1.add(deck2Card);
            } else {
                System.out.println("Round " + round + " result: Player 2 wins");
                deck2.add(deck2Card);
                deck2.add(deck1Card);
            }
        }
        long score;
        if (deck1.isEmpty()) {
            System.out.println("Player 2 wins the game!");
            score = getScore(deck2);
        } else {
            System.out.println("Player 1 wins the game!");
            score = getScore(deck1);
        }


        System.out.println("Score: " + score);
    }

    private static long getScore(Queue<Integer> deck2) {
        long score = 0;
        for (int cardMultiple = deck2.size(); cardMultiple > 0; cardMultiple--) {
            score += (long) deck2.remove() * cardMultiple;
        }
        return score;
    }
}
