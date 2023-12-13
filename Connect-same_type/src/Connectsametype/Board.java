package Connectsametype;


import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;


/**
 *
 * @author RAWANH 
 */
public class Board {

    private int[][] grid;
    private int[] topPieceIndex;
    private int width;
    private int height;
    private int numOfPiecesToWin;
    private int fills;
    private int lastColumnPlayed = -1;
    

    public Board(int width, int height, int numOfPiecesToWin) {
        fills = 0;
        this.width = width;
        this.height = height;
        this.numOfPiecesToWin = numOfPiecesToWin;
        grid = new int[height][width];
        topPieceIndex = new int[width];
        for (int i = 0; i < topPieceIndex.length; i++) {
            topPieceIndex[i] = height;
        }
        for (int[] grid1 : grid) {
            for (int j = 0; j < grid1.length; j++) {
                grid1[j] = -1;
            }
        }
    }

    public Board(Board board) {
        grid = new int[board.height][board.width];
        topPieceIndex = new int[board.width];
        System.arraycopy(board.topPieceIndex, 0, topPieceIndex, 0, this.topPieceIndex.length);
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(board.grid[i], 0, this.grid[i], 0, board.width);
        }
        this.fills = board.fills;
        this.height = board.height;
        this.width = board.width;
        this.numOfPiecesToWin = board.numOfPiecesToWin;
    }

    public int getBoardWidth() {
        return width;
    }

    public int getBoardHeight() {
        return height;
    }

    public List<Board> getPossibleNextBoards(int nextPlayer) {
        List<Board> nextBoards = new LinkedList<>();
        for (int i = 0; i < width; i++) {
            if (topPieceIndex[i] != 0) {
                Board nextBoard = new Board(this);
                nextBoard.play(nextPlayer , i);
                nextBoards.add(nextBoard);
            }
        }
        return nextBoards;
    }

    public boolean play(int player, int col) {
        if (topPieceIndex[col] != 0) {
            topPieceIndex[col] -= 1;
            grid[topPieceIndex[col]][col] = player;
            fills++;
            lastColumnPlayed = col;
            return true;
        }
        return false;
    }

    /**
     * how good is the board for this player?
     *
     * @param player
     * @return
     */
    //         ************** YOUR CODE HERE ************            \\
    public int evaluate(char player){
        if(isWin('1')){
            return Integer.MAX_VALUE;
        }
        else if (isWin(otherPlayer(player)) ){
            return Integer.MIN_VALUE;
        }
        return 0;
    }
//    public int evaluate(char player) {
//        char opponent;
//        if(player == '0')
//            opponent = '1';
//        else
//            opponent = '0';
//
//        // Check if the current player has won
//        if (isWin(player)) {
//            return Integer.MAX_VALUE; // Max value indicates a win
//        }
//
//        // Check if the opponent has won
//        if (isWin(opponent)) {
//            return Integer.MIN_VALUE; // Min value indicates a loss
//        }
//
//        if(isWithdraw()){
//            return 0;
//        }
//
//        // Evaluate based on the number of pieces in a row for the current player
//        int playerScore = evaluatePlayer(player);
//
//        // Evaluate based on the number of pieces in a row for the opponent
//        int opponentScore = evaluatePlayer(opponent);
//
//        // Return the difference in scores
//        return playerScore - opponentScore;
//    }

    public int evaluatePlayer(char player) {
        int temp_score, col_score = 0, row_score = 0;
        // columns
        for (int col=0; col<width; col++){
            temp_score = scoreInColumn(player, col);
            col_score = max(col_score, temp_score);
        }
        // rows
        for(int row=0; row<height; row++){
            temp_score = scoreInRow(player, row);
            row_score = max(row_score, temp_score);
        }

        int score = col_score + row_score;

        return score;
    }

    private int scoreInColumn(char player, int col) {
        int row = topPieceIndex[col];
        //row is empty
        if (row == height) {
            return 0;
        }
        // the cell itself
        if (!same_type(player,grid[row][col])) {
            return 0;
        }
        int count = 1;
        // check cells below
        try {
            for (int i = row + 1; i < height; i++) {
                if (same_type(player,grid[i][col])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return count;
    }

    private int scoreInRow(char player, int row) {
        // column is empty
        if (row >= height || row < 0) {
            return 0;
        }

        // the cell itself
        if (!same_type(player, grid[row][0])) {
            return 0;
        }

        int count = 1;
        // check cells to the right
        try {
            for (int j = 1; j < width; j++) {
                if (same_type(player, grid[row][j])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        return count;
    }


    /**
     * checks if the game is withdraw
     *
     * @return
     */
    public boolean isWithdraw() {
        return (fills == width * height);
    }

    /**
     * checks if player putting last piece makes him win (connect four pieces)
     *
     * @param player
     * @return true if win
     */
    public boolean isPlayerWinWithLastPiece(char player) {
        int col = this.lastColumnPlayed;
        return (isWinInColumn(player, col) || isWinInRow(player, col)
                || isWinInDiagonal_1(player, col) || isWinInDiagonal_2(player, col));
    }

    /**
     * checks if player is a winner (searching all the board not just last
     * piece)
     *
     * @param player
     * @return true if win
     */
    public boolean isWin(char player) {
        for (int col = 0; col < width; col++) {
            if (isWinInColumn(player, col)) {
                return true;
            } else if (isWinInRow(player, col)) {
                return true;
            } else if (isWinInDiagonal_1(player, col)) {
                return true;
            } else if (isWinInDiagonal_2(player, col)) {
                return true;
            }
        }
        return false;
    }
    
    

    /**
     * checks if the game is win of withdraw for any player
     *
     * @return
     */
    public boolean isFinished() {
        return (isWin('1') || isWin('0') || isWithdraw());
    }
    
    private boolean same_type(char player,int num){
        if (num!= -1 && num %2==Character.getNumericValue(player))
            return true;
        else
            return false;
    }

    /**
     * checks if player putting piece in col makes him win (connect four pieces)
     * in this col
     *
     * @param player
     * @param col the column the player played in
     * @return true if win
     */
    
  
    private boolean isWinInColumn(char player, int col) {
        int row = topPieceIndex[col];
        //row is empty
        if (row == height) {
            return false;
        }
        // the cell itself
        
        if (!same_type(player,grid[row][col])) {
            return false;
        }
        int count = 1;
        // check cells below
        try {
            for (int i = row + 1; i < height; i++) {
                if (same_type(player,grid[i][col])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return (count >= numOfPiecesToWin);
    }

    /**
     * checks if player putting piece in col makes him win (connect four pieces)
     * in the row containing piece
     *
     * @param player
     * @param col the column the player played in
     * @return true if win
     */
    private boolean isWinInRow(char player, int col) {
        //collect row
        int row = topPieceIndex[col];
        //row is empty
        if (row == height) {
            return false;
        }
        // the cell itself
        if (!same_type(player,grid[row][col])) {
            return false;
        }
        int count = 1;
        // cells befor
        try {
            for (int i = col - 1; i >= 0; i--) {
                if (same_type(player,grid[row][i])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        // cells after
        try {
            for (int i = col + 1; i < width; i++) {
                if (grid[row][i] %2==Character.getNumericValue(player)) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return (count >= numOfPiecesToWin);

    }

    /**
     * checks if player putting piece in col makes him win (connect four pieces)
     * in the first diagonal containing this piece
     *
     * @param player
     * @param col the column the player played in
     * @return true if win
     */
    private boolean isWinInDiagonal_1(char player, int col) {
        //collect diagonal
        int row = topPieceIndex[col];
        //row is empty
        if (row == height) {
            return false;
        }
        // the cell itself
        if (!same_type(player,grid[row][col])) {
            return false;
        }
        int count = 1;
        // cells befor
        try {
            for (int i = 1;; i++) {
                if ( same_type(player,grid[row - i][col - i])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        // cells after
        try {
            for (int i = 1;; i++) {
                if (same_type(player,grid[row + i][col + i])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return (count >= numOfPiecesToWin);

    }

    /**
     * checks if player putting piece in col makes him win (connect four pieces)
     * in the second diagonal containing this piece
     *
     * @param player
     * @param col the column the player played in
     * @return true if win
     */
    private boolean isWinInDiagonal_2(char player, int col) {
        //collect diagonal
        int row = topPieceIndex[col];
        //row is empty
        if (row == height) {
            return false;
        }
        // the cell itself
        if (!same_type(player,grid[row][col])) {
            return false;
        }
        int count = 1;
        // cells befor
        try {
            for (int i = 1;; i++) {
                if (same_type(player,grid[row - i][col + i] )) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        // cells after
        try {
            for (int i = 1;; i++) {
                if (same_type(player,grid[row + i][col - i] )) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return (count >= numOfPiecesToWin);

    }

    private char otherPlayer(char player) {
        if (player == '0') {
            return '1';
        }
        return '0';
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            sb.append(" | ");
            for (int j = 0; j < width; j++) {
                if (grid[i][j]!=-1){
                    sb.append((char)(grid[i][j] + '0'));
                    sb.append(" | ");
                }
                else {
                    sb.append(" ");
                    sb.append(" | ");
                            }
            }
//            sb.delete(sb.length() - 2, sb.length() - 1);
            sb.append('\n');
        }
        sb.append(" ");
        for (int i = 1; i < height; i++) {
            sb.append("-----");
        }
        sb.append("\n | ");
        for (int i = 1; i <= height; i++) {
            sb.append(i);
            sb.append(" | ");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Board board = new Board(4, 4, 3);

        board.play(2, 1);
        board.play(5, 1);
        board.play(4, 2);
        board.play(7, 2);
        board.play(3, 3);
        board.play(1, 3);

        System.out.println("board:");
        System.out.println(board);
        System.out.println("****************");
        System.out.println("is win for odd? " + board.isWin('1'));
        System.out.println("****************");

        List<Board> next = board.getPossibleNextBoards(3);
        int i = 1;
        for (Board b : next) {
            System.out.println(i + ": (" + b.evaluate('1') + ")");
            System.out.println(b);
            System.out.println("is win for odd? " + b.isPlayerWinWithLastPiece('1'));
            i++;
        }

    }
}
