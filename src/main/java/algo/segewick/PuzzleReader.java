package algo.segewick;

import edu.princeton.cs.algs4.In;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class PuzzleReader {

    private final String puzzleFileName;

    private List<String> readFileNames() {
        File file = new File(puzzleFileName);
        try {
            Scanner scanner = new Scanner(file);
            List<String> list = new ArrayList<>(150);
            while (scanner.hasNext()) {
                list.add("/home/igor/IdeaProjects/SuperProject/src/main/java/segewick/puzzles/" + scanner.nextLine());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int[][] makeTestPuzzle() {
        List<String> files = readFileNames();
        return makePuzzle(files.get(120));
    }

    private  int[][] makePuzzle(String fileName) {
        In in = new In(fileName);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        return tiles;
    }
}
