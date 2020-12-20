package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day20Part01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day20.txt"));
        List<Tile> tiles = parseTiles(lines);

        int rowColSize = (int) Math.sqrt(tiles.size());
        List<List<Tile>> tileArrangement = null;
        while (tileArrangement == null) {
            for (int i = 0; i < tiles.size() && tileArrangement == null; i++) {
                Tile startingTile = tiles.get(i);
                for (int j = 0; j < startingTile.numberOfOrientations && tileArrangement == null; j++) {
                    startingTile.selectedOrientation = j;
                    tileArrangement = findValidArrangement(tiles, rowColSize, startingTile, 0);
                    tiles.forEach(Tile::reset);
                }
            }
            shuffleTiles(tiles);
        }

        System.out.println("solution: " + (tileArrangement.get(0).get(0).id *
                tileArrangement.get(0).get(rowColSize - 1).id *
                tileArrangement.get(rowColSize - 1).get(0).id *
                tileArrangement.get(rowColSize - 1).get(rowColSize - 1).id));

    }

    private static void shuffleTiles(List<Tile> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            int randomIndex = (int)(Math.random() * tiles.size());
            Collections.swap(tiles, i, randomIndex);
        }
    }

    private static List<List<Tile>> findValidArrangement(List<Tile> tiles, int rowColSize, Tile startingTile, int firstTileToCheck) {
        startingTile.setPositionFound();

        List<List<Tile>> tileArrangement = new ArrayList<>();
        List<Tile> firstRow = new ArrayList<>();
        firstRow.add(startingTile);

        Tile tileA = startingTile;
        int currentRow = 0;
        int rowPosition = 0;

        for (int j = firstTileToCheck; j < tiles.size() && rowPosition < rowColSize; j++) {
            Tile tileB = tiles.get(j);
            if (tileB.isPositionFound()) continue;

            if (Tiles.rightOfAMatchesAnyBSide(tileA, tileB)) {
                firstRow.add(tileB);
                tileB.setPositionFound();
                tileA = tileB;
                rowPosition++;
                j = 0;
            }
        }

        if (rowPosition != rowColSize - 1) {
//            System.out.println("no valid first row arrangement found for starting tile " + startingTile.id);
            return null;
        }
        tileArrangement.add(firstRow);

        // other rows
        while (currentRow < rowColSize - 1) {
            currentRow++;
            rowPosition = 0;
            List<Tile> row = new ArrayList<>();
            tileArrangement.add(row);
            for (int j = 0; j < tiles.size() && rowPosition < rowColSize; j++) {
                tileA = tileArrangement.get(currentRow - 1).get(rowPosition); // tileA is the tile above the one being checked
                Tile tileB = tiles.get(j);
                if (tileB.isPositionFound()) continue;

                if (Tiles.bottomOfAMatchesAnyBSide(tileA, tileB)) {
                    if (rowPosition > 0)
                        if (!Tiles.rightOfAMatchesLeftOfB(row.get(rowPosition - 1), tileB)) continue;

                    row.add(tileB);
                    tileB.setPositionFound();
                    rowPosition++;
                    j = 0;
                }
            }

            if (rowPosition != rowColSize) {
//                if (currentRow > 9)
//                    System.out.println("no valid row " + currentRow + " arrangement found for starting tile " + startingTile.id);
                return null;
            }
        }

        return tileArrangement;
    }

    private static List<Tile> parseTiles(List<String> lines) {
        List<Tile> tiles = new ArrayList<>();
        int currentTileId = -1;
        List<String> currentImage = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("Tile")) {
                currentTileId = Integer.parseInt(line.substring(line.indexOf(" ") + 1, line.length() - 1));
            } else if (line.isEmpty()) {
                tiles.add(new Tile(currentTileId, currentImage));
                currentTileId = -1;
                currentImage = new ArrayList<>();
            } else {
                currentImage.add(line);
            }
        }
        tiles.add(new Tile(currentTileId, currentImage)); // add last entry
        return tiles;
    }

    private static class Tiles {
        public static boolean rightOfAMatchesAnyBSide(Tile a, Tile b) {
            for (int bOrientation = 0; bOrientation < b.numberOfOrientations; bOrientation++) {
                if (rightOfAMatchesLeftOfB(a, b)) return true;
                b.shift();
            }
            return false;
        }

        public static boolean rightOfAMatchesLeftOfB(Tile a, Tile b) {
            List<String> aImage = a.currentState();
            List<String> bImage = b.currentState();
            int lastCharIdx = a.original.size() - 1;

            for (int i = 0; i < aImage.size(); i++)
                if (aImage.get(i).charAt(lastCharIdx) != bImage.get(i).charAt(0))
                    return false;

            return true;
        }

        public static boolean bottomOfAMatchesAnyBSide(Tile a, Tile b) {
            for (int bOrientation = 0; bOrientation < b.numberOfOrientations; bOrientation++) {
                if (bottomOfAMatchesTopOfB(a, b)) return true;
                b.shift();
            }
            return false;
        }

        public static boolean bottomOfAMatchesTopOfB(Tile a, Tile b) {
            String bottomLineOfA = a.currentState().get(a.currentState().size() - 1);
            String topLineOfB = b.currentState().get(0);
            return topLineOfB.equals(bottomLineOfA);
        }

    }

    private static class Tile {
        final long id;
        final List<String> original;
        final List<List<String>> allOrientations = new ArrayList<>();
        final int numberOfOrientations;

        int selectedOrientation = 0;
        boolean positionFound = false;

        public Tile(int tileId, List<String> image) {
            this.id = tileId;
            this.original = image;
            allOrientations.add(original);
            allOrientations.add(rotate(allOrientations.get(0)));
            allOrientations.add(rotate(allOrientations.get(1)));
            allOrientations.add(rotate(allOrientations.get(2)));
            allOrientations.add(flip(allOrientations.get(0)));
            allOrientations.add(flip(allOrientations.get(1)));
            allOrientations.add(flip(allOrientations.get(2)));
            allOrientations.add(flip(allOrientations.get(3)));
            numberOfOrientations = allOrientations.size();
//            if (this.id == 3187) {
//                for (List<String> orientation : allOrientations) {
//                    orientation.forEach(System.out::println);
//                    System.out.println();
//                }
//            }
        }

        public void shift() {
            selectedOrientation = (selectedOrientation + 1) % allOrientations.size();
//            return currentState();
        }

        public boolean isPositionFound() {
            return positionFound;
        }

        public void setPositionFound() {
            positionFound = true;
        }

        ;

        public void reset() {
            selectedOrientation = 0;
            positionFound = false;
        }

        public List<String> currentState() {
            return allOrientations.get(selectedOrientation);
        }

        public List<String> rotate(List<String> image) {
            List<String> newImageLines = new ArrayList<>();
            for (int i = 0; i < image.size(); i++) {
                StringBuilder rotatedLine = new StringBuilder();
                for (int j = image.size() - 1; j >= 0; j--) {
                    rotatedLine.append(image.get(j).charAt(i));
                }
                newImageLines.add(rotatedLine.toString());
            }
            return newImageLines;
        }

        public List<String> flip(List<String> image) {
            List<String> newImageLines = new ArrayList<>();
            for (int i = image.size() - 1; i >= 0; i--) {
                newImageLines.add(image.get(i));
            }
            return newImageLines;
        }

    }
}
