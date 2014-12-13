import java.util.ArrayList;

// The current state of the board
public class TicTacToeState {



	// Return all moves that can be made ()
	public Moves[] getAllMoves(Player player){
		//return allMoves.toArray(new Move[allMoves.size()]);

	}

	public TicTacToeState makeMove(Move move){
		
	}

	public TicTacToeState makeMoveCloning(Move move){

	}

	// Game over if all (9) spaces are taken or 3 in a row
	public boolean gameOver(){



		return (allMoves.size() >= 9);
	}
}