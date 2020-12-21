package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day20Part02 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day20.txt"));
        List<Tile> tiles = parseTiles(lines);

        int rowColSize = (int) Math.sqrt(tiles.size());
        List<List<Tile>> tileArrangement = null;
        while (tileArrangement == null) {
            shuffleTiles(tiles);
            for (int i = 0; i < tiles.size() && tileArrangement == null; i++) {
                Tile startingTile = tiles.get(i);
                for (int j = 0; j < startingTile.numberOfOrientations && tileArrangement == null; j++) {
                    tiles.forEach(Tile::reset);
                    startingTile.selectedOrientation = j;
                    tileArrangement = findValidArrangement(tiles, rowColSize, startingTile, 0);
                }
            }
        }

        Tile combinedTile = Tiles.combine(tileArrangement);
        long seaMonsters = 0;
        for (int i = 0; i < combinedTile.numberOfOrientations && seaMonsters == 0; i++) {
            combinedTile.shift();
            seaMonsters = countSeaMonsters(combinedTile.currentState());
        }
        System.out.println("sea monsters: " + seaMonsters);
        long poundCount = String.join("", combinedTile.currentState()).chars().filter(c -> c == '#').count();
        long waterRoughness = poundCount - (seaMonsters * 15); // each sea monster accounts for 15 '#'s
        System.out.println("water roughness: " + waterRoughness);

//        markSeaMonsters(combinedTile.currentState());
//        combinedTile.currentState().forEach(System.out::println);
    }

    private static long countSeaMonsters(List<String> currentState) {
        long count = 0;
        int rowLength = currentState.get(0).length();
        for (int rowIndex = 1; rowIndex < currentState.size() - 1; rowIndex++) {
            for (int colIndex = 0; colIndex < rowLength - 21; colIndex++) {
                // check for middle row then above and below
                if (currentState.get(rowIndex).substring(colIndex, colIndex + 20).matches("#....##....##....###") &&
                        currentState.get(rowIndex - 1).charAt(colIndex + 18) == '#' &&
                        currentState.get(rowIndex + 1).substring(colIndex, colIndex + 20).matches(".#..#..#..#..#..#...")) {
                    count++;
                }
            }
        }
        return count;
    }

    private static long markSeaMonsters(List<String> currentState) {
        long count = 0;
        int rowLength = currentState.get(0).length();
        for (int rowIndex = 1; rowIndex < currentState.size() - 1; rowIndex++) {
            for (int colIndex = 0; colIndex < rowLength - 21; colIndex++) {
                // check for middle row then above and below
                if (currentState.get(rowIndex).substring(colIndex, colIndex + 20).matches("[#O]....[#O][#O]....[#O][#O]....[#O][#O][#O]") &&
                        currentState.get(rowIndex - 1).charAt(colIndex + 18) == '#' || currentState.get(rowIndex - 1).charAt(colIndex + 18) == 'O' &&
                        currentState.get(rowIndex + 1).substring(colIndex, colIndex + 20).matches(".[#O]..[#O]..[#O]..[#O]..[#O]..[#O]...")) {
                    int topRowOffset = 18;
                    int[] middleRowOffsets = new int[] {0, 5, 6, 11, 12, 17, 18, 19};
                    int[] bottomRowOffsets = new int[] {1, 4, 7, 10, 13, 16};

                    char[] topRow = currentState.get(rowIndex - 1).toCharArray();
                    topRow[topRowOffset] = 'O';
                    currentState.set(rowIndex - 1, new String(topRow));

                    char[] middleRow = currentState.get(rowIndex).toCharArray();
                    for (int middleRowOffset : middleRowOffsets) {
                        middleRow[middleRowOffset] = 'O';
                    }
                    currentState.set(rowIndex, new String(middleRow));

                    char[] bottomRow = currentState.get(rowIndex + 1).toCharArray();
                    for (int bottomRowOffset : bottomRowOffsets) {
                        bottomRow[bottomRowOffset] = 'O';
                    }
                    currentState.set(rowIndex + 1, new String(bottomRow));
                }
            }
        }
        return count;
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

        public static Tile combine(List<List<Tile>> tileArrangement) {
            List<String> combinedImage = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < tileArrangement.size(); rowIndex++) {
                List<Tile> row = tileArrangement.get(rowIndex);
                combinedImage.addAll(row.get(0).withoutBorders()); // add first image in row
                // append the rest of the row
                for (int colIndex = 1; colIndex < tileArrangement.size(); colIndex++) {
                    List<String> image = row.get(colIndex).withoutBorders();
                    for (int i = 0; i < image.size(); i++) {
                        int combinedIndex = i + (image.size() * rowIndex);
                        combinedImage.set(combinedIndex, combinedImage.get(combinedIndex) + image.get(i));
                    }
                }
            }

            return new Tile(0, combinedImage);
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
        }

        public void shift() {
            selectedOrientation = (selectedOrientation + 1) % allOrientations.size();
        }

        public boolean isPositionFound() {
            return positionFound;
        }

        public void setPositionFound() {
            positionFound = true;
        }

        public void reset() {
            selectedOrientation = 0;
            positionFound = false;
        }

        public List<String> currentState() {
            return allOrientations.get(selectedOrientation);
        }

        private List<String> rotate(List<String> image) {
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

        private List<String> flip(List<String> image) {
            List<String> newImageLines = new ArrayList<>();
            for (int i = image.size() - 1; i >= 0; i--) {
                newImageLines.add(image.get(i));
            }
            return newImageLines;
        }

        public List<String> withoutBorders() {
            List<String> oldImage = currentState();
            List<String> newImage = new ArrayList<>();
            for (int i = 1; i < oldImage.size() - 1; i++) {
                String s = oldImage.get(i);
                newImage.add(s.substring(1, s.length() - 1));
            }
            return newImage;
        }
    }
}
