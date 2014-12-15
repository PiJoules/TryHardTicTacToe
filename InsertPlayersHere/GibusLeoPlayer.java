import java.util.*;
import tictactoe.*;

/*
corner | side | corner
----------------------
 side  |center|  side
----------------------
corner | side | corner
*/


public class GibusLeoPlayer extends Player {

	public final int playerNumber;
	private final boolean verbose = true;
	private Random random = new Random();

	private boolean didGetInitials = false;
	private boolean amFirst;
	private int boardSize;
	private Move[] previousMoves;  // previous value returned by getAllMoves method
	private ArrayList<Move> myMoves = new ArrayList<Move>(); // my moves
	private ArrayList<Move> opponentMoves = new ArrayList<Move>(); // opponents recorded moves

	// Will be either 0 (first) or 1 (second)
	public GibusLeoPlayer(int player){
		this.playerNumber = player;
	}

	// Determine which strategy to take
	public Move getMove(TicTacToeState state){
		Move[] possibleMoves = state.getAllMoves(playerNumber);

		// Get info about the board
		if (!didGetInitials){
			int maxSize = possibleMoves[0].getY();
			for (int i = 1; i < possibleMoves.length; i++)
				if (possibleMoves[i].getY() > maxSize)
					maxSize = possibleMoves[i].getY();
			boardSize = maxSize+1;

			amFirst = (this.playerNumber == 0);

			// Initialize all previousMoves as all available moves if going second
			ArrayList<Move> initialAvailableMoves = new ArrayList<>();
			for (int y = 0; y < boardSize; y++){
				for (int x = 0; x < boardSize; x++){
					initialAvailableMoves.add(new Move(x,y,playerNumber));
				}
			}
			previousMoves = initialAvailableMoves.toArray(new Move[initialAvailableMoves.size()]);

			didGetInitials = true;
		}

		if (verbose)
			System.out.println("Analysis:");

		// Make a random move if there is something I have not
		// accounted for
		Move possibleMove = null;

		// First watch for the opponent's move
		if (state.getMoveCount() > 0){
			Move opponentsMove = getOponentsMove(possibleMoves);
			opponentMoves.add(opponentsMove);

			// Check if can finish a line
			possibleMove = finishLine(possibleMoves);
			if (possibleMove != null){
				if (verbose)
					System.out.println("Can finish a line");
				myMoves.add(possibleMove);
				//previousMoves = possibleMoves;
				previousMoves = guessOpponentMovesAfterMove(possibleMove, state);
				return possibleMove;
			}

			// Otherwise, see if you should block
			possibleMove = blockOpponent(possibleMoves);
			if (possibleMove != null){
				if (verbose)
					System.out.println("Should block opponent");
				myMoves.add(possibleMove);
				//previousMoves = possibleMoves;
				previousMoves = guessOpponentMovesAfterMove(possibleMove, state);
				return possibleMove;
			}
		}

		if (boardSize == 3){
			// After checking for openings, look up a strategy and save the move
			possibleMove = size3Strategy(state);
			if (possibleMove != null){
				myMoves.add(possibleMove);
				//previousMoves = possibleMoves;
				previousMoves = guessOpponentMovesAfterMove(possibleMove, state);
				return possibleMove;
			}
		}

		if (verbose)
			System.out.println("Making random move");
		possibleMove = getRandomMove(possibleMoves);
		myMoves.add(possibleMove);
		previousMoves = guessOpponentMovesAfterMove(possibleMove, state);
		return possibleMove;
	}

	private Move getRandomMove(Move[] possibleMoves){
		return possibleMoves[random.nextInt(possibleMoves.length)];
	}

	private Move size3Strategy(TicTacToeState state){
		Move[] possibleMoves = state.getAllMoves(playerNumber);

		// The move the opponent just made, the one he made before his last one,
		// and my last move
		Move opponentsMove = null, opponentsPreviousMove = null, myLastMove = null;

		if (amFirst){
			// Opponent has made at least 1 move
			if (state.getMoveCount() >= 2){
				opponentsMove = opponentMoves.get(opponentMoves.size()-1);
				if (verbose)
					System.out.println("Opponent's move: [" + opponentsMove.getX() + "," + opponentsMove.getY() + "]");
				myLastMove = myMoves.get(myMoves.size()-1);
			}
			// Opponent has made at least 2 moves
			if (state.getMoveCount() >= 4){
				opponentsPreviousMove = opponentMoves.get(opponentMoves.size()-2);
			}

			// Always move in a corner first
			if (state.getMoveCount() == 0){
				if (verbose)
					System.out.println("Starting on random corner");
				Move[] corners = getCorners(possibleMoves);
				return getRandomMove(corners);
			}
			else if (state.getMoveCount() == 2){
				// The opponent moved in the center
				// After moving in the corner, I will need to move onto the opposite corner
				// After which, the opponent will always make a move that makes 2 in a row
				// Either finishLine() or blockOpponent() will take over
				if (moveIsOnCenter(opponentsMove)){
					if (verbose)
						System.out.println("Opponent has moved to center; will move to opposite corner");
					return getOppositeCorner(myLastMove);
				}

				// The opponent is on one of the sides
				// I must move to the adjascent corner on the opposite side of where 
				// the opponent placed his
				// After this, the opponent will have to block, and I can go in the center
				// or the opposite corner depending on where my opponent makes his first move
				//
				// If the opponent's first move is adjascent to mine, then I can go in either
				// the center or opposite corner after this move
				// If the opponent's first move is on a side not adjascent to me, then I must go
				// in the center for my move after this move
				else if (moveIsOnSide(opponentsMove)){
					if (verbose)
						System.out.println("Opponent moved to one of the sides; will move to opposite-adjascent corner");
					return getOppositeAdjascentCorner(possibleMoves, myLastMove, opponentsMove);
				}

				// The opponent is on one of the 3 remaining corners
				// I can move to either of the corners if his piece is on the opposite corner 
				// or one of the sides adjascent to mine and opposite to his if he moved on a 
				// corner that isn't opposite to mine
				else if (moveIsOnCorner(opponentsMove)){
					if (verbose)
						System.out.println("Opponent moved to corner; will move to random corner");
					return getRandomMove(getCorners(possibleMoves));
				}
			}
			else if (state.getMoveCount() == 4){
				// For simplicity, move to center
				// After this move, I will have 2 possible ways to win and finishLine()
				// will take over
				if (moveIsOnSide(opponentsPreviousMove)){
					if (verbose)
						System.out.println("Will setup win by moving to center");
					return getCenter(possibleMoves);
				}

				// For simplicity, I will move on one of the remaining corners
				// After this move, I will have 2 possible ways to win and finishLine()
				// will take over
				else if (moveIsOnCorner(opponentsPreviousMove)){
					if (verbose)
						System.out.println("Will setup win by moving to random corner");
					return getRandomMove(getCorners(possibleMoves));
				}
			}
		}
		else {
			opponentsMove = opponentMoves.get(opponentMoves.size()-1);
			if (verbose)
				System.out.println("Opponent's move: [" + opponentsMove.getX() + "," + opponentsMove.getY() + "]");
			if (state.getMoveCount() > 1){
				myLastMove = myMoves.get(myMoves.size()-1);
			}
			if (state.getMoveCount() >= 3){
				opponentsPreviousMove = opponentMoves.get(opponentMoves.size()-2);
			}


			if (state.getMoveCount() == 1){
				// For simplicity, always move into center if available
				if (!moveIsOnCenter(opponentsMove)){
					if (verbose)
						System.out.println("Start in center");
					return getCenter(possibleMoves);
				}
			}
			else if (state.getMoveCount() == 3){
				// The opponent moved to the corner opposite his first move
				// If the opponent plays the same startegy as above, then he will
				// win if I move to one of the remaining corners, so move to any of
				// the remianing sides
				//
				// All moves after this cause finishLine() or blockOpponent() to take over
				if (moveIsOnCorner(opponentsPreviousMove)){
					Move oppositeCorner = getOppositeCorner(opponentsPreviousMove);
					if (movesAreSame(opponentsMove,oppositeCorner)){
						if (verbose)
							System.out.println("Opponent moved to corner opposite to previous move; will move to random side");
						return getRandomMove(getSides(possibleMoves));
					}
				}
			}
		}

		return null;
	}


	// Same as blockOpponent(), but priority is given over checking for 
	// my moves rather othan my opponents
	private Move finishLine(Move[] possibleMoves){

		// Simulate the board
		char[][] board = new char[boardSize][boardSize];
		for (int y = 0; y < boardSize; y++){
			for (int x = 0; x < boardSize; x++){
				board[y][x] = ' ';
			}
		}
		// x will mark my moves; o for the opponent's
		for (Move move : myMoves){
			board[move.getY()][move.getX()] = 'x';
		}
		for (Move move : opponentMoves){
			board[move.getY()][move.getX()] = 'o';
		}


		int xCounter = 0;
		int oCounter = 0;
		Move possibleMove = null;

		// Check each row
		for (int y = 0; y < boardSize; y++){
			for (int x = 0; x < boardSize; x++){
				if (board[y][x] == 'x')
					xCounter++;
				else if (board[y][x] == 'o')
					oCounter++;
				else
					possibleMove = getMoveFromCoordinates(possibleMoves, x, y);
			}
			if (xCounter == boardSize-1 && oCounter == 0)
				return possibleMove;
			xCounter = 0;
			oCounter = 0;
			possibleMove = null;
		}

		// Check each column
		for (int x = 0; x < boardSize; x++){
			for (int y = 0; y < boardSize; y++){
				if (board[y][x] == 'x')
					xCounter++;
				else if (board[y][x] == 'o')
					oCounter++;
				else
					possibleMove = getMoveFromCoordinates(possibleMoves, x, y);
			}
			if (xCounter == boardSize-1 && oCounter == 0)
				return possibleMove;
			xCounter = 0;
			oCounter = 0;
			possibleMove = null;
		}

		// Diagnols
		for (int x = 0; x < boardSize; x++){
			if (board[x][x] == 'x')
				xCounter++;
			else if (board[x][x] == 'o')
				oCounter++;
			else
				possibleMove = getMoveFromCoordinates(possibleMoves, x, x);
		}
		if (xCounter == boardSize-1 && oCounter == 0)
			return possibleMove;
		xCounter = 0;
		oCounter = 0;
		possibleMove = null;

		for (int x = 0; x < boardSize; x++){
			if (board[boardSize-1-x][x] == 'x')
				xCounter++;
			else if (board[boardSize-1-x][x] == 'o')
				oCounter++;
			else
				possibleMove = getMoveFromCoordinates(possibleMoves, x, boardSize-1-x);
		}
		if (xCounter == boardSize-1 && oCounter == 0)
			return possibleMove;

		return null;
	}


	// Immediately place a move in the same line as the opponent's if 
	// they have at least boardSize-1 in a line
	private Move blockOpponent(Move[] possibleMoves){

		// Simulate the board
		char[][] board = new char[boardSize][boardSize];
		for (int y = 0; y < boardSize; y++){
			for (int x = 0; x < boardSize; x++){
				board[y][x] = ' ';
			}
		}
		// x will mark my moves; o for the opponent's
		for (Move move : myMoves){
			board[move.getY()][move.getX()] = 'x';
		}
		for (Move move : opponentMoves){
			board[move.getY()][move.getX()] = 'o';
		}


		int xCounter = 0;
		int oCounter = 0;
		Move possibleMove = null;

		// Check each row
		for (int y = 0; y < boardSize; y++){
			for (int x = 0; x < boardSize; x++){
				if (board[y][x] == 'x')
					xCounter++;
				else if (board[y][x] == 'o')
					oCounter++;
				else
					possibleMove = getMoveFromCoordinates(possibleMoves, x, y);
			}
			if (oCounter == boardSize-1 && xCounter == 0)
				return possibleMove;
			xCounter = 0;
			oCounter = 0;
			possibleMove = null;
		}

		// Check each column
		for (int x = 0; x < boardSize; x++){
			for (int y = 0; y < boardSize; y++){
				if (board[y][x] == 'x')
					xCounter++;
				else if (board[y][x] == 'o')
					oCounter++;
				else
					possibleMove = getMoveFromCoordinates(possibleMoves, x, y);
			}
			if (oCounter == boardSize-1 && xCounter == 0)
				return possibleMove;
			xCounter = 0;
			oCounter = 0;
			possibleMove = null;
		}

		// Diagnols
		for (int x = 0; x < boardSize; x++){
			if (board[x][x] == 'x')
				xCounter++;
			else if (board[x][x] == 'o')
				oCounter++;
			else
				possibleMove = getMoveFromCoordinates(possibleMoves, x, x);
		}
		if (oCounter == boardSize-1 && xCounter == 0)
			return possibleMove;
		xCounter = 0;
		oCounter = 0;
		possibleMove = null;

		for (int x = 0; x < boardSize; x++){
			if (board[boardSize-1-x][x] == 'x')
				xCounter++;
			else if (board[boardSize-1-x][x] == 'o')
				oCounter++;
			else
				possibleMove = getMoveFromCoordinates(possibleMoves, x, boardSize-1-x);
		}
		if (oCounter == boardSize-1 && xCounter == 0)
			return possibleMove;

		return null;
	}

	private Move getMoveFromCoordinates(Move[] possibleMoves, int x, int y){
		for (Move move : possibleMoves){
			if (move.getX() == x && move.getY() == y)
				return move;
		}
		return null;
	}

	private Move[] getCorners(Move[] possibleMoves){
		ArrayList<Move> corners = new ArrayList<>();
		for (Move move : possibleMoves){
			if (moveIsOnCorner(move))
				corners.add(move);
		}
		return corners.toArray(new Move[corners.size()]);
	}

	private Move[] getSides(Move[] possibleMoves){
		ArrayList<Move> sides = new ArrayList<>();
		for (Move move : possibleMoves){
			if (moveIsOnSide(move))
				sides.add(move);
		}
		return sides.toArray(new Move[sides.size()]);
	}

	private Move getCenter(Move[] possibleMoves){
		for (Move move : possibleMoves){
			if (moveIsOnCenter(move)){
				return move;
			}
		}
		return null;
	}

	private Move getOppositeAdjascentCorner(Move[] possibleMoves, Move referenceCorner, Move opponentsMove){
		if (opponentsMove.getY() == 0){
			int myNextY = boardSize-1;
			int myNextX = referenceCorner.getX();
			return getMoveFromCoordinates(possibleMoves, myNextX, myNextY);
		}
		else if (opponentsMove.getY() == boardSize-1){
			int myNextY = 0;
			int myNextX = referenceCorner.getX();
			return getMoveFromCoordinates(possibleMoves, myNextX, myNextY);
		}
		else if (opponentsMove.getX() == 0){
			int myNextX = boardSize-1;
			int myNextY = referenceCorner.getY();
			return getMoveFromCoordinates(possibleMoves, myNextX, myNextY);
		}
		else if (opponentsMove.getX() == boardSize-1){
			int myNextX = 0;
			int myNextY = referenceCorner.getY();
			return getMoveFromCoordinates(possibleMoves, myNextX, myNextY);
		}

		return null;
	}

	private Move getOppositeCorner(Move cornerMove){
		int x = 0, y = 0;
		if (cornerMove.getX() == 0)
			x = boardSize-1;
		if (cornerMove.getY() == 0)
			y = boardSize-1;
		return new Move(x,y,playerNumber);
	}

	// Get the opponent's last move by checking the difference between the 
	// preveious array of available moves with the current array of available moves
	private Move getOponentsMove(Move[] possibleMoves){
		previous:
			for (Move previousMove : previousMoves){
				// Check if the previousMove is an available move
				// If not, that was the opponent's move
				for (Move possibleMove : possibleMoves){
					if (movesAreSame(possibleMove,previousMove))
						continue previous; // is another possible move
				}
				return previousMove;
			}
		return null;
	}

	private boolean movesAreSame(Move move1, Move move2){
		return (move1.getX() == move2.getX() && move1.getY() == move2.getY());
	}

	// Only checks for directly above, below, left, or right; not diagnols
	private boolean movesAreAdjascent(Move move1, Move move2){
		return ( move1.getX() == move2.getX() && (move1.getY() == move2.getY()-1 || move1.getY() == move2.getY()+1) )
			|| ( move1.getY() == move2.getY() && (move1.getX() == move2.getX()-1 || move1.getX() == move2.getX()+1) );
	}

	private boolean moveIsOnCorner(Move move){
		return (move.getX() == 0 || move.getX() == boardSize-1) && (move.getY() == 0 || move.getY() == boardSize-1);
	}

	private boolean moveIsOnSide(Move move){
		return !moveIsOnCorner(move) && (move.getX() == 0 || move.getX() == boardSize-1 || move.getY() == 0 || move.getY() == boardSize-1);
	}

	private boolean moveIsOnCenter(Move move){
		if (boardSize % 2 == 1){
			int center = (boardSize-1)/2;
			return move.getX() == center && move.getY() == center;
		}
		return false;
	}

	private String printMoves(Move[] moves){
		StringBuilder sb = new StringBuilder();
		for (Move move : moves){
			sb.append("[" + move.getX() + "," + move.getY() + "] ");
		}
		return sb.toString();
	}

	private Move[] guessOpponentMovesAfterMove(Move move, TicTacToeState state){
		TicTacToeState opponentsNextState = state.makeMoveCloning(move);
		int opponentNumber = 0;
		if (playerNumber == 0)
			opponentNumber = 1;
		return state.getAllMoves(opponentNumber);
	}

}