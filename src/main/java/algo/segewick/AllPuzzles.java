package algo.segewick;

import edu.princeton.cs.algs4.In;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AllPuzzles {

    public AllPuzzles() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        List<String> list = readFileNames();
        for (String fileName : list) {
            executorService.execute(new TableTask(fileName));
        }
        executorService.shutdown();
    }

    private List<String> readFileNames() {
        File file = new File("fileslist.txt");
        try {
            Scanner scanner = new Scanner(file);
            List<String> list = new ArrayList<>(150);
            while (scanner.hasNext()) {
                list.add("puzzles/" + scanner.nextLine());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        AllPuzzles a = new AllPuzzles();
    }
}

class TableTask implements Runnable {

    private final String fileName;

    TableTask(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        Board initial = new Board(makeTable());
        Solver solver = new Solver(initial);

        if (solver.isSolvable()) {
            System.out.println(fileName + " CALCULATED");
        } else {
            System.out.println(fileName + ": UNSOLVABLE");
        }
    }

    private int[][] makeTable() {
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
