package fun.webb.aoc2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day17Part01 {
    public static void main(String[] args) throws IOException {
        new Day17Part01(Files.readAllLines(Path.of("inputs/day17.txt"))).run(6);
    }

    private final List<String> lines;

    public Day17Part01(List<String> fileLines) {
        this.lines = fileLines;
    }

    public void run(int cycleCount) {
        int xySize = lines.size();
        int zSize = 1;
        Map<Cube, CubeState> cubeStates = initializeCubeStates(xySize, zSize);
        printCubeStates(cubeStates);

        for (int cycle = 0; cycle < cycleCount; cycle++) {
            xySize += 2;
            zSize += 2;
            cubeStates = updateCubeStates(cubeStates, xySize, zSize);
            System.out.println("\n After cycle " + cycle + ": \n");
            printCubeStates(cubeStates);
        }

        long activeCount = cubeStates.values().stream().filter(CubeState::isActive).count();
        System.out.println("active cubes: " + activeCount);
    }

    private void printCubeStates(Map<Cube, CubeState> cubeStates) {
        int x = 0;
        int y = 0;
        int z = 0;
        CubeState cubeState = cubeStates.get(new Cube(x, y, z));
        while (cubeState != null) {
            System.out.println("\nz=" + z);
            while (cubeState != null) {
                while (true) {
                    cubeState = cubeStates.get(new Cube(x++, y, z));
                    if (cubeState == null) {
                        break;
                    } else if (cubeState.isActive()) {
                        System.out.print('#');
                    } else {
                        System.out.print('.');
                    }
                }
                x = 0;
                y++;
                System.out.println();
                cubeState = cubeStates.get(new Cube(x, y, z));
            }
            y = 0;
            z++;
            cubeState = cubeStates.get(new Cube(x, y, z));
        }
    }

    private Map<Cube, CubeState> initializeCubeStates(int xySize, int zSize) {
        Map<Cube, CubeState> cubeStates = new HashMap<>();
        for (int z = 0; z < zSize; z++) {
            for (int y = 0; y < xySize; y++) {
                String line = lines.get(y);
                for (int x = 0; x < xySize; x++) {
                    boolean active = line.charAt(x) == '#';
                    cubeStates.put(new Cube(x, y, z), new CubeState(active));
                }
            }
        }
        return cubeStates;
    }

    private Map<Cube, CubeState> updateCubeStates(Map<Cube, CubeState> previousStates, int xySize, int zSize) {
        Map<Cube, CubeState> nextStates = new HashMap<>();
        for (int z = 0; z < zSize; z++) {
            int previousZ = z - 1;
            for (int y = 0; y < xySize; y++) {
                int previousY = y - 1;
                for (int x = 0; x < xySize; x++) {
                    int previousX = x - 1;
                    Cube previousCube = new Cube(previousX, previousY, previousZ);
                    CubeState previousState = previousStates.getOrDefault(previousCube, new CubeState(false));
                    CubeState newState;
                    if (previousState.isActive() && activeNeighborCountWithin(previousCube, previousStates, 2, 3)) {
                        newState = previousState;
                    } else if (!previousState.isActive() && activeNeighborCountWithin(previousCube, previousStates, 3, 3)) {
                        newState = new CubeState(true);
                    } else {
                        newState = new CubeState(false);
                    }
                    nextStates.put(new Cube(x, y, z), newState);
                }
            }
        }
        return nextStates;
    }

    private boolean activeNeighborCountWithin(Cube previousCube, Map<Cube, CubeState> previousStates, int min, int max) {
        int activeCount = 0;
        for (int z = -1; z <= 1; z++) {
            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    if (x == 0 && y == 0 && z == 0) continue; // skip this cube
                    Cube neighbor = new Cube(previousCube.x + x, previousCube.y + y, previousCube.z + z);
                    if (previousStates.getOrDefault(neighbor, new CubeState(false)).isActive()) {
                        activeCount++;
                    }
                }
            }
        }

        return activeCount >= min && activeCount <= max;
    }

    private static class Cube {
        private final int x;
        private final int y;
        private final int z;

        public Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cube cube = (Cube) o;
            return x == cube.x && y == cube.y && z == cube.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "Cube{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    private static class CubeState {
        private final boolean active;

        public CubeState(boolean active) {
            this.active = active;
        }

        public boolean isActive() {
            return active;
        }

        @Override
        public String toString() {
            return isActive() ? "active" : "inactive";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CubeState cubeState = (CubeState) o;
            return active == cubeState.active;
        }

        @Override
        public int hashCode() {
            return Objects.hash(active);
        }
    }
}
