import tictactoe.Player;
import tictactoe.Move;
import tictactoe.TicTacToeState;

import java.util.Random;

public class RandomPlayer extends Player {

	public final int playerNumber;
	private static Random random = new Random();

	public RandomPlayer(int player){
		this.playerNumber = player;
	}

	public Move getMove(TicTacToeState state){
		Move[] possibleMoves = state.getAllMoves();
		int choice = random.nextInt(possibleMoves.length);
		return possibleMoves[choice];
	}
}