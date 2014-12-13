package TicTacToe

/**
 * This is a test case for the tic tac toe game with two random players. 
 *
 */

public class RandomPlayerTest
{
    public static void main(String[] args)
    {
        TicTacToeState board = new TicTacToeState(3);
        Player p1 = new RandomPlayer();
        Player p2 = new RandomPlayer();
    }
}