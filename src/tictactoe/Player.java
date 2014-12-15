package tictactoe;

public abstract class Player {
	protected final int playerNumber;
	public Player(int player){
		this.playerNumber = player;
	}
	public abstract Move getMove(TicTacToeState state);
}