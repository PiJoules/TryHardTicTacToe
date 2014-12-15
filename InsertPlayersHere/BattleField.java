import tictactoe.*;
import java.lang.reflect.Constructor;
import java.io.*;

public class BattleField {
    public static void main(String[] args) throws Exception {

        TicTacToeState board = new TicTacToeState(Integer.parseInt(args[2])); // Third arg is board size

        Class<?> clazz = Class.forName(args[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Player p1 = (Player) ctor.newInstance(new Object[]{0});
        Class<?> clazz2 = Class.forName(args[1]);
        Constructor<?> ctor2 = clazz2.getConstructors()[0];
        Player p2 = (Player) ctor2.newInstance(new Object[]{1});

        Move playerMove;

        boolean playing = true;

        while(playing){
            System.out.println("Turn: " + (board.getMoveCount()+1));

            playerMove= p1.getMove(board);
            board = board.makeMove(playerMove);
            board.display();
            if(board.gameOver())
            {
                System.out.println("Winner: " + board.getWinner());
                playing = false;
                break;
            }

            System.out.println("______________");
            System.out.println("Turn: " + (board.getMoveCount()+1));

            playerMove = p2.getMove(board);
            board = board.makeMove(playerMove);
            board.display();
            if(board.gameOver())
            {
                System.out.println("Winner: " + board.getWinner());
                playing = false;
                break;
            }

            System.out.println("______________");
        }

    }
}