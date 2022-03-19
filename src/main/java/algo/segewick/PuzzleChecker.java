package algo.segewick;

import edu.princeton.cs.algs4.StdOut;

public class PuzzleChecker {

    private final Solver solver;
    private final String filename;

    public PuzzleChecker(Solver solver, String filename) {
        this.solver = solver;
        this.filename = filename;

        printAns();
    }

    public void printAns() {
        if (solver.isSolvable()) {
            for (Board board : solver.solution()) {
                System.out.println(board);
            }
            StdOut.println(filename + ": " + solver.moves());
        }
        else {
            StdOut.println(filename + " UNSOLVABLE");
        }
    }
}
