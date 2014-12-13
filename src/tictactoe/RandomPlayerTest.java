package tictactoe;

/**
 * This is a test case for the tic tac toe game with two random players. 
 *
 */

public class RandomPlayerTest
{
    public static void main(String[] args)
    {
        TicTacToeState board = new TicTacToeState(3);
        Player p1 = new RandomPlayer(0);
        Player p2 = new RandomPlayer(1);
        Move playerMove;

        boolean playing = true;

        while(playing)
        {
            playerMove= p1.getMove(board);
            board = board.makeMove(playerMove);
            if(board.gameOver())
            {
                playing = false;
                break;
            }

            board.display();

            playerMove = p2.getMove(board);
            board = board.makeMove(playerMove);
            if(board.gameOver())
            {
                playing = false;
                break;
            }

            board.display();
        }

    }
}