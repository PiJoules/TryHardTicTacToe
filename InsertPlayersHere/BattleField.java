import tictactoe.*;
import java.lang.reflect.Constructor;
import java.io.*;

public class BattleField {

    private static String firstPlayer;
    private static String secondPlayer;
    private static int firstPlayerWins = 0;
    private static int secondPlayerWins = 0;
    private static int ties = 0;
    private static int playCount = 0;
    private static boolean switched = false;

    public static void main(String[] args) throws Exception {

        firstPlayer = args[0];
        secondPlayer = args[1];
        int boardSize = Integer.parseInt(args[2]);
        int plays = Integer.parseInt(args[3]);
        boolean playUntilLoss = Boolean.parseBoolean(args[4]);


        while(isPlaying(plays, playUntilLoss)){
            System.out.println("\n\nGame " + (playCount+1) + ":");

            TicTacToeState board = new TicTacToeState(boardSize);

            // Switch whoever starts every game
            if (playCount > 0){
                String nameHolder = firstPlayer;
                firstPlayer = secondPlayer;
                secondPlayer = nameHolder;
                switched = !switched;
            }
            System.out.println("x - " + firstPlayer);
            System.out.println("o - " + secondPlayer);

            Class<?> clazz = Class.forName(firstPlayer);
            Class<?> clazz2 = Class.forName(secondPlayer);
            Constructor<?> ctor = clazz.getConstructors()[0];
            Constructor<?> ctor2 = clazz2.getConstructors()[0];
            Player p1 = (Player) ctor.newInstance(new Object[]{0});
            Player p2 = (Player) ctor2.newInstance(new Object[]{1});

            Move playerMove;

            System.out.println("______________");
            while(true){
                System.out.println("Turn: " + (board.getMoveCount()+1));

                playerMove= p1.getMove(board);
                System.out.println(playerMove.toString());
                board = board.makeMove(playerMove);
                board.display();
                if (isGameOver(board))
                    break;

                System.out.println("______________");
                System.out.println("Turn: " + (board.getMoveCount()+1));

                playerMove = p2.getMove(board);
                board = board.makeMove(playerMove);
                board.display();
                if (isGameOver(board))
                    break;

                System.out.println("______________");
            }
        }

        System.out.println("______________");
        if (playCount % 2 == 0){
            String nameHolder = firstPlayer;
            firstPlayer = secondPlayer;
            secondPlayer = nameHolder;
        }
        System.out.println("Results after " + plays + " plays:");
        System.out.println(firstPlayer + " wins: " + firstPlayerWins);
        System.out.println(secondPlayer + " wins: " + secondPlayerWins);
        System.out.println("Ties: " + ties);

    }

    // Either play until player1 loses or number of plays is up
    private static boolean isPlaying(int plays, boolean playUntilLoss){
        if (playUntilLoss)
            return (secondPlayerWins == 0);
        return (playCount < plays);
    }

    private static boolean isGameOver(TicTacToeState board){
        if(board.gameOver()){
            playCount++;
            int winner = board.getWinner();

            if (winner == 1){
                System.out.println("Winner: " + firstPlayer);
                if (switched)
                    secondPlayerWins++;
                else
                    firstPlayerWins++;
            }
            else if (winner == -1){
                System.out.println("Winner: " + secondPlayer);
                if (switched)
                    firstPlayerWins++;
                else
                    secondPlayerWins++;
            }
            else if (winner == 0){
                System.out.println("Tie");
                ties++;
            }
        }
        return board.gameOver();
    }
}