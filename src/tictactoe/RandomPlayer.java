package tictactoe;


import java.util.Random;

/**
 * A random tic tac toe player
 */
public class RandomPlayer extends Player {

	public final int playerNumber;
	private static Random random = new Random();

	public RandomPlayer(int player){
		this.playerNumber = player;
	}

	public Move getMove(TicTacToeState state){
		Move[] possibleMoves = state.getAllMoves(playerNumber);
		int choice = random.nextInt(possibleMoves.length);
		return possibleMoves[choice];
	}
}