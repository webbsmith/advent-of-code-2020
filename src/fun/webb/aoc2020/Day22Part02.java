package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day22Part02 {
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

        combat(deck1, deck2);

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

    // Return 1 if Player 1 wins, 2 otherwise
    private static int combat(Queue<Integer> deck1, Queue<Integer> deck2) {
        List<DeckStates> pastDeckStates = new ArrayList<>(); // Keep track of previous deck states
        for (int round = 1; !deck1.isEmpty() && !deck2.isEmpty(); round++) {
            // To prevent infinite games, end the game if the current state is equal to a previous state
            DeckStates deckStates = new DeckStates(deck1, deck2);
            if (pastDeckStates.contains(deckStates)) {
                return 1; // Player 1 wins
            } else {
                pastDeckStates.add(deckStates);
            }

            int deck1Card = deck1.remove();
            int deck2Card = deck2.remove();

            int roundWinner;
            if (deck1Card <= deck1.size() && deck2Card <= deck2.size()) {
                // If both players have a high enough card to play a subgame, call this method again
                Queue<Integer> subDeck1 = new LinkedList<>();
                Queue<Integer> subDeck2 = new LinkedList<>();

                copyCardsToSubdeck(deck1, deck1Card, subDeck1);
                copyCardsToSubdeck(deck2, deck2Card, subDeck2);

                roundWinner = combat(subDeck1, subDeck2);
            } else if (deck1Card > deck2Card) {
                roundWinner = 1;
            } else {
                roundWinner = 2;
            }

            if (roundWinner == 1) {
                System.out.println("Round " + round + " result: Player 1 wins");
                deck1.add(deck1Card);
                deck1.add(deck2Card);
            } else {
                System.out.println("Round " + round + " result: Player 2 wins");
                deck2.add(deck2Card);
                deck2.add(deck1Card);
            }
        }

        return deck2.isEmpty() ? 1 : 2;
    }

    private static void copyCardsToSubdeck(Queue<Integer> deck, int count, Queue<Integer> subDeck) {
        Iterator<Integer> iterator = deck.iterator();
        for (int i = 0; i < count; i++) {
            Integer card = iterator.next();
            subDeck.add(card);
        }
    }

    private static long getScore(Queue<Integer> deck2) {
        long score = 0;
        for (int cardMultiple = deck2.size(); cardMultiple > 0; cardMultiple--) {
            score += (long) deck2.remove() * cardMultiple;
        }
        return score;
    }

    private static class DeckStates {
        private final Queue<Integer> deck1;
        private final Queue<Integer> deck2;

        public DeckStates(Queue<Integer> deck1, Queue<Integer> deck2) {
            this.deck1 = new LinkedList<>(deck1);
            this.deck2 = new LinkedList<>(deck2);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DeckStates that = (DeckStates) o;
            return deck1.equals(that.deck1) && deck2.equals(that.deck2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(deck1, deck2);
        }
    }
}
