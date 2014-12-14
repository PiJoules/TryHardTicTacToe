import java.util.Random;
import tictactoe.*;

/**
 * A random tic tac toe player
 */
public class FirstPlayer extends Player {

	public final int playerNumber;
	private static Random random = new Random();

	public FirstPlayer(int player){
		this.playerNumber = player;
	}

	public Move getMove(TicTacToeState state){
		Move[] possibleMoves = state.getAllMoves(playerNumber);
		int choice = random.nextInt(possibleMoves.length);
		return possibleMoves[choice];
	}
}