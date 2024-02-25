package game2048;

import java.util.*;
import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;
        this.board.setViewingPerspective(side);
        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        //b.tile(col, row)
        for (int col = 0; col < this.board.size(); col++) {
            if (colOperator(col)){
                changed = true;
            };
        }
        checkGameOver();
        if (changed) {
            setChanged();
        }
        this.board.setViewingPerspective(Side.NORTH);
        return changed;
    }

    public ArrayList<Integer> nullMove(int col) {
        ArrayList<Integer> nullMoveDistance = new ArrayList<>();
        int count = 0;
        for (int row = this.board.size() - 1; row >= 0 ; row--) {
            if (!isNulltile(col, row)) {
                nullMoveDistance.addLast(count);
            } else {
                nullMoveDistance.addLast(0);
                count += 1;
            }
        }
        return nullMoveDistance;
    }
    public ArrayList<Integer> mergeMove(int col) {
        ArrayList<Integer> mergeMoveDistance = new ArrayList<>();
        ArrayList<Integer> numberQueue = new ArrayList<>();
        ArrayList<Boolean> isMergedQueue = new ArrayList<>();
        for (int row = this.board.size() - 1; row >= 0 ; row--) {
            isMergedQueue.addLast(false);
            if (!isNulltile(col, row)) {
                numberQueue.addLast(this.board.tile(col, row).value());
            }
            else {
                numberQueue.addLast(0);
            }
        }
        int count = 0;
        while(!numberQueue.isEmpty() && !isMergedQueue.isEmpty()) {
            int currentNumber = numberQueue.removeFirst();
            boolean currentIsMergedFlag = isMergedQueue.removeFirst();
            ArrayList<Integer> temNum = new ArrayList<>();
            for (int n : numberQueue) {
                temNum.addLast(n);
            }
            if (currentNumber == 0) {
                mergeMoveDistance.addLast(0);
            } else {
                if (!currentIsMergedFlag) {
                    ArrayList<Boolean> temFlag = new ArrayList<>();
                    boolean mergeFlag = false;
                    while(!temNum.isEmpty()) {
                        int Num = temNum.removeFirst();
                        temFlag.addLast(isMergedQueue.removeFirst());
                        if (Num != 0 && currentNumber != Num) {break;}
                        else if (currentNumber == Num) {
                            temFlag.removeLast();
                            temFlag.addLast(true);
                            mergeFlag = true;
                            break;
                        }
                    }
                    while(!temFlag.isEmpty()) {
                        isMergedQueue.addFirst(temFlag.removeLast());
                    }
                    if (mergeFlag) {
                        mergeMoveDistance.addLast(count);
                        count += 1;
                        this.score = this.score + currentNumber * 2;
                    } else {
                        mergeMoveDistance.addLast(count);
                    }
                }
                else {
                    mergeMoveDistance.addLast(count);
                }
            }
        }
        return mergeMoveDistance;
    }
    public boolean colOperator(int col){
/*        int[] distance = new int[this.board.size()];
        boolean changed = false;
        ArrayList<Integer> nullMoveDistance = nullMove(col);
        ArrayList<Integer> mergeMoveDistance = mergeMove(col);
        int count = this.board.size() - 1;
        while(!nullMoveDistance.isEmpty() && !mergeMoveDistance.isEmpty()) {
            int n = nullMoveDistance.removeFirst() + mergeMoveDistance.removeFirst();
            distance[count] = n;
            if (n != 0) {
                changed = true;
                this.board.move(col, count + n, this.board.tile(col, count));

            }
            count -= 1;
        }
        return changed;*/
        int[] distance = new int[this.board.size()];
        boolean changed = false;
        distance = getDistance(col, distance);
        for (int row = this.board.size() - 1; row >= 0 ; row--) {
            if (distance[row] != 0 && !isNulltile(col, row)){
                changed = true;
                if (isMerge(col, row + distance[row], this.board.tile(col, row))) {
                    this.score = this.score + this.board.tile(col, row).value() * 2;
                }
                this.board.move(col, row + distance[row], this.board.tile(col, row));
            }
        }
        return changed;
    }

    public boolean isMerge(int target_col, int target_row, Tile tile){
        if (!isNulltile(target_col, target_row) && this.board.tile(target_col, target_row).value() == tile.value()) {
            return true;
        }
        return false;
    }
    public boolean isNulltile(int col, int row){
        return this.board.tile(col, row) == null;
    }

    public int[] getDistance(int col, int[] distance){
        for (int row = this.board.size() - 1; row >= 0 ; row--) {
            if (isNulltile(col, row)) {
                distance[row] = 0;
            } else {
                distance[row] = nullMove(col, row) + mergeMove(col, row);
            }
        }
        return distance;
    }

    public int nullMove(int col, int row){
        if(row == this.board.size() - 1){
            return 0;
        }
        else if(isNulltile(col, row + 1)){
            return 1 + nullMove(col, row + 1);
        }
        else {
            return nullMove(col, row + 1);
        }
    }

    public int mergeMove(int col, int row){
        if (row == this.board.size() - 1) {
            return 0;
        } else if(canMerge(col, row)){
            return 1 + mergeMove(col, row + 1);
        }
        return mergeMove(col, row + 1);
    }

    public boolean canMerge(int col, int row){
        if(isNulltile(col, row)){
            return false;
        }
        Object[] result = hasNextEqual(col, row);
        if((boolean) result[0]){
            if (!canMerge(col, row + (int) result[1])){
                return true;
            }
        }
        return false;
    }

    public Object[] hasNextEqual(int col, int row){
        int current_value = this.board.tile(col, row).value();
        Object[] result = new Object[]{false, 0};
        for (int i = 1; i <= this.board.size() - row - 1 ; i++) {
            if (! isNulltile(col, row + i)) {
                if (this.board.tile(col, row + i).value() == current_value) {
                    result[0] = true;
                    result[1] = i;
                    break;
                } else if (this.board.tile(col, row + i).value() != current_value) {
                    result[0] = false;
                    result[1] = 0;
                    break;
                }
            }
        }
        return result;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        int length = b.size();
        for (int i = 0; i <= length - 1; i += 1){
            for (int j = 0; j <= length - 1; j += 1){
                if (b.tile(i ,j) == null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        int length = b.size();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (b.tile(i ,j) != null && b.tile(i, j).value() == MAX_PIECE) {
                        return true;
                    }
                }
            }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        int length = b.size();
        if (emptySpaceExists(b)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int current_value = b.tile(i, j).value();
                if(i - 1 >= 0){
                    if(b.tile(i - 1, j).value() == current_value){
                        return true;
                    }
                }
                if(i + 1 <= length - 1){
                    if(b.tile(i + 1, j).value() == current_value){
                        return true;
                    }
                }
                if(j - 1 >= 0){
                    if(b.tile(i, j - 1).value() == current_value){
                        return true;
                    }
                }
                if(j + 1 <= length - 1){
                    if(b.tile(i, j + 1).value() == current_value){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
