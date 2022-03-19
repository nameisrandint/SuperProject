package algo.segewick;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

public class Solver {

    private boolean solvable;
    private int minMoves;
    private volatile boolean isAnybodyFinished;
    private volatile List<Board> traceList;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        solve(initial);
    }

    private void solve(Board initial) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Result> init = executorService.submit(new Search(initial));
        executorService.submit(new Search(initial.twin()));

        try {
            Result result = init.get();
            if (result instanceof Result.Sucsess) {
                solvable = true;
                minMoves = ((Result.Sucsess) result).getMoves();
                traceList = ((Result.Sucsess) result).getTraceList();
            } else {
                solvable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (!isSolvable())
            return -1;
        else return minMoves;
    }

    public Iterable<Board> solution() {
        return isSolvable() ? traceList : null;
    }

    private class Node implements Comparable<Node> {
        private final Node previous;
        private final int moves;
        private final Board board;

        Node(Board board, int moves, Node previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        public int priority() {
            return board.manhattan() + moves;
        }

        public int getMoves() {
            return moves;
        }

        public int compareTo(Node other) {
            return this.priority() - other.priority();
        }

        public boolean isGoal() {
            return board.isGoal();
        }

        public Board getBoard() {
            return board;
        }

        public Node getPrevious() {
            return this.previous;
        }
    }

    private class Search implements Callable<Result> {

        private MinPQ<Node> queue = new MinPQ<>();

        public Search(Board initial) {
            Node startNode = new Node(initial, 0, null);
            queue.insert(startNode);
        }

        @Override
        public Result call() {
            while (!queue.min().isGoal() && !isAnybodyFinished) {
                Node min = queue.delMin();
                for (Board board : min.getBoard().neighbors()) {
                    if (min.getPrevious() != null && board.equals(min.getPrevious().getBoard())) {
                        continue;
                    }
                    queue.insert(new Node(board, min.getMoves() + 1, min));
                }
            }

            if (!isAnybodyFinished) {
                isAnybodyFinished = true;
                Node current = queue.min();
                int moves = current.getMoves();
                List<Board> list = makeTraceList(current);
                queue = null;
                return new Result.Sucsess(moves, list);
            } else {
                queue = null;
                return new Result.Fail();
            }
        }

        private List<Board> makeTraceList(Node current) {
            List<Board> list = new ArrayList<>(current.getMoves());
            while (current != null) {
                list.add(current.getBoard());
                current = current.getPrevious();
            }
            Collections.reverse(list);
            return list;
        }
    }

    abstract static class Result {
        public static class Sucsess extends Result {

            private final int moves;
            private final List<Board> list;

            Sucsess(int moves, List<Board> list) {
                this.moves = moves;
                this.list = list;
            }

            public int getMoves() {
                return moves;
            }

            public List<Board> getTraceList() {
                return list;
            }
        }

        public static class Fail extends Result {
            //empty
        }
    }
}


