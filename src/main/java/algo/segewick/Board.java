package algo.segewick;

import java.util.LinkedList;

public class Board {

    private final int[][] table;
    private final int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    public Board(int[][] tiles) {
        dimension = tiles.length;
        table = new int[dimension][dimension];

        for (int i = 0; i < dimension; ++i)
            for (int j = 0; j < dimension; ++j)
                table[i][j] = tiles[i][j];
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension() + "\n");

        for (int i = 0; i < dimension(); ++i) {
            for (int j = 0; j < dimension; ++j) {
                sb.append(" " + String.valueOf(table[i][j]) + " ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int k = 0;

        for (int i = 0; i < dimension(); ++i) {
            for (int j = 0; j < dimension(); ++j) {
                if (table[i][j] == 0) continue;
                if (table[i][j] != findRightPlace(i, j)) ++k;
            }
        }

        return k;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // row and column is [0:dimension * dimension)
        int k = 0;
        for (int i = 0; i < dimension(); ++i) {
            for (int j = 0; j < dimension(); ++j) {
                if (table[i][j] == 0) continue;
                int num = table[i][j];
                int row = (num % dimension() == 0)
                          ? (num / dimension() - 1)
                          : (num / dimension());
                int column = num - row * dimension() - 1;
                k += Math.abs(i - row);
                k += Math.abs(j - column);
            }
        }

        return k;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj == this) return true;

        if (obj.getClass() != this.getClass()) return false;

        Board other = (Board) obj;

        if (this.dimension() != other.dimension()) return false;

        for (int i = 0; i < this.dimension(); ++i) {
            for (int j = 0; j < this.dimension(); ++j) {
                if (this.table[i][j] != other.table[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        int zeroRow = -1;
        int zeroCol = -1;
        boolean found = false;

        for (int i = 0; i < dimension() && !found; ++i) {
            for (int j = 0; j < dimension(); ++j) {
                if (table[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    found = true;
                    break;
                }
            }
        }

        int[][] directions = {
                { zeroRow + 1, zeroCol },
                { zeroRow, zeroCol + 1 },
                { zeroRow - 1, zeroCol },
                { zeroRow, zeroCol - 1 }
        };

        LinkedList<Board> neighbors = new LinkedList<Board>();

        for (int[] direction : directions) {
            int x = direction[0];
            int y = direction[1];
            if (validateMatrixIndex(x, y)) {
                int[][] neighbourTable = new int[dimension()][dimension()];
                for (int i = 0; i < dimension(); ++i) {
                    for (int j = 0; j < dimension(); ++j) {
                        neighbourTable[i][j] = table[i][j];
                    }
                }
                neighbourTable[zeroRow][zeroCol] = neighbourTable[x][y];
                neighbourTable[x][y] = 0;
                neighbors.add(new Board(neighbourTable));
            }
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board board = new Board(this.table);
        if (board.table[0][0] != 0) {
            if (board.table[1][0] != 0) {
                int temp = board.table[0][0];
                board.table[0][0] = board.table[1][0];
                board.table[1][0] = temp;
            }
            else {
                int temp = board.table[0][0];
                board.table[0][0] = board.table[0][1];
                board.table[0][1] = temp;
            }
        }
        else {
            int temp = board.table[1][0];
            board.table[1][0] = board.table[0][1];
            board.table[0][1] = temp;
        }

        return board;
    }

    private int findRightPlace(int row, int column) {
        // row, column is [0:dimension * dimension]
        return dimension() * row + column + 1;
    }

    private boolean validateMatrixIndex(int r, int c) {
        if (r > dimension() - 1 || r < 0) {
            return false;
        }
        if (c > dimension() - 1 || c < 0) {
            return false;
        }
        return true;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        /* empty */
    }

}

// { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } }
// { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } }
