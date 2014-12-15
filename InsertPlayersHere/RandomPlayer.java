import java.util.Random;
import tictactoe.*;

/**
 * A random tic tac toe player
 */
public class RandomPlayer extends Player {

	private static Random random = new Random();

	public RandomPlayer(int player){
		super(player);
	}

	public Move getMove(TicTacToeState state){
		Move[] possibleMoves = state.getAllMoves(playerNumber);
		int choice = random.nextInt(possibleMoves.length);
		return possibleMoves[choice];
	}
}