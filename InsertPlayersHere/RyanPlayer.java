import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import tictactoe.*;

/**
 * A random tic tac toe player
 */
public class RyanPlayer extends Player {

    private int player;
    private int opponent;
    private boolean maximize;
    private static Random random = new Random();

    public RyanPlayer(int player){
        super(player);
        this.player = player;
        if(player == 1)
        {
            maximize = false; //We want a -1 game state, O win. 
            opponent = 0;
        }
        else
        {
            maximize = true; //We want a 1 game state, X win.
            opponent = 1;
        }
    }

    /**
     * This will run a minimax search for finding the best move to play from the 
     * current state for the player. It uses alpha beta pruning
     *
     */
    private Move minimaxAlphaBetaPruning(TicTacToeState state)
    {
        //Get all of the possible moves
        ArrayList<Move> moves = new ArrayList<Move>(Arrays.asList(state.getAllMoves(playerNumber)));

        if(moves.size() == 0)
        {
            return null;
        }

        TicTacToeState temp = state.makeMoveCloning(moves.get(0));
        int bestScore = alphaBetaMinimax(temp, Integer.MIN_VALUE, Integer.MAX_VALUE, maximize);
        int bestIndex = 0;

        for (int i = 1; i < moves.size(); i++) {
            temp = state.makeMoveCloning(moves.get(i));
            int tempScore = alphaBetaMinimax(temp, Integer.MIN_VALUE, Integer.MAX_VALUE, maximize);

            if (tempScore > bestScore && maximize) {
                bestScore = tempScore;
                bestIndex = i;
            } else if(tempScore < bestScore && !maximize)
            {
                bestScore = tempScore;
                bestIndex = i;
            }
        }

        return moves.get(bestIndex);
    }

    /**
     * Run the recursive alphaBetaMinimax search
     */
    private int alphaBetaMinimax(TicTacToeState state, int alpha, int beta, boolean max) {
        
        ArrayList<Move> moves;
        
        if(max)
        {
            moves = new ArrayList<Move>(Arrays.asList(state.getAllMoves(0))); 
        }
        else
        {
            moves = new ArrayList<Move>(Arrays.asList(state.getAllMoves(1))); 
        }

        //Base case: Check if we reached the end.
        if (moves.isEmpty() || state.gameOver()) {
            return state.getWinner(); //Need better heuristic 
        }

        // Recursive calls

        if (max) {
            // Try to maximize and update alpha

            for (Move move : moves) {
                TicTacToeState temp = state.makeMoveCloning(move); // Get the
                                                                    // state
                int tempScore = alphaBetaMinimax(temp, alpha, beta, !max);
                if (tempScore > alpha) {
                    alpha = tempScore;
                }
                if (beta <= alpha) {
                    break;
                }
            }

            return alpha;
        } else {
            // Try to minimize and update beta

            for (Move move : moves) {
                TicTacToeState temp = state.makeMoveCloning(move); // Get the
                                                                    // state
                int tempScore = alphaBetaMinimax(temp, alpha, beta, !max);
                if (tempScore < beta) {
                    beta = tempScore;
                }
                if (beta <= alpha) {
                    break;
                }
            }
            return beta;
        }
    }

    public Move getMove(TicTacToeState state){
        
        return minimaxAlphaBetaPruning(state);
    }
}