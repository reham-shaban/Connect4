package Connectsametype;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.random;
import java.util.Random;
import java.util.Scanner;
import java.util.List;

/**
 *
 * @author RAWANH
 */
public class Game {
    char computer = '1';
    char human = '0';
    Board board = new Board(5, 5, 5);
    Random random = new Random();
    int even;

    public void play() {
        System.out.println(board);
        while (true) {
            humanTurn();
            System.out.println(board);

            if (board.isWin(human)) {
                System.out.println("Congratulations you won :'(");
                break;
            }
            if (board.isWithdraw()) {
                System.out.println("We played a draw -_-");
                break;
            }
            computerTurn();
            System.out.println("_____The role of the computer ______");
            System.out.println(board);
            if (board.isWin(computer)) {
                System.out.println("I won :D I'm smarter than you, you stupid person!");
                break;
            }
            if (board.isWithdraw()) {
                System.out.println("We played a draw -_-");
                break;
            }
        }

    }

    //         ************** YOUR CODE HERE ************            \\
    private void computerTurn() {
        System.out.println("Computer's turn:");

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        int width = board.getBoardWidth();

        for(int move = 0; move < width; move++){
            Board copyBoard = new Board(board);  // Create a copy of the board to simulate the move
            if(copyBoard.play(computer, move)){
                int score = minmax(copyBoard, 8, Integer.MIN_VALUE, Integer.MAX_VALUE, human);
                if (score >= bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        if (bestMove != -1) {
            board.play(1, bestMove);
            System.out.println("Computer played in column " + (bestMove + 1));
        }

    }


    /**
     * Human plays
     *
     * @return the column the human played in
     */
    private void humanTurn() {
        Scanner s = new Scanner(System.in);
        int col;
        while (true) {
            System.out.print("Enter even number: ");
            even = s.nextInt();
            while (even % 2 != 0) {
                System.out.println("The number is  " + even + " is odd !, try again");
                System.out.print("Enter even number: ");
                even = s.nextInt();
                System.out.println();
            }

            System.out.print("Enter column: ");
            col = s.nextInt();
            System.out.println();
            if ((col > 0) && (col - 1 < board.getBoardWidth())) {
                if (board.play(even, col - 1)) {
                    return;
                }
                System.out.println("Invalid Column: Column " + col + " is full!, try again");
            }
            System.out.println("Invalid Column: out of range " + board.getBoardWidth() + ", try again");
        }
    }

    //         ************** YOUR CODE HERE ************            \\
    private char opp_type(char player){
       if(player == '0')    return '1';
       return '0';
    }
    //         ************** YOUR CODE HERE ************            \\
    private  int minmax(Board b, int depth, int alpha, int beta, char player){
        if(depth == 0 || b.isFinished()){
            return b.evaluate(player);
        }
        int eval, maxEval, minEval;
        List<Board> boards = b.getPossibleNextBoards(player);
        if(player == '1'){
            maxEval = Integer.MIN_VALUE;
            for(Board board : boards){
                eval = minmax(board, depth - 1, alpha, beta, opp_type(player));
                maxEval = max(eval, maxEval);
                alpha = max(alpha, maxEval);
                if(beta <= alpha)
                    break;
            }


            return maxEval;
        }
        else {
            minEval = Integer.MAX_VALUE;
            for(Board board : boards){
                eval = minmax(board, depth - 1, alpha, beta, opp_type(player));
                minEval = min(eval, minEval);
                beta = min(beta, minEval);
                if(beta <= alpha)
                    break;
            }
            return minEval;
        }
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.play();
    }

}
