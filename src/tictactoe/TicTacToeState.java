package tictactoe;

import java.util.ArrayList;

// The current state of the board
public class TicTacToeState {

	private char[][] board;
	private int moveCount;
	private final int size;

	// The current player making the move
	// Players are indicated with x and o
	private char nextPlayer = 'x';


	public TicTacToeState(int size){
		this.size = size;
		board = new char[size][size];

		//Initialize the board to be all spaces
		for(int i = 0; i < size; i ++)
		{
			for(int j = 0; j < size; j ++)
			{
				board[i][j] = ' ';
			}
		}
		//Initialize the move count
		moveCount = 0;
	}

	// Copy constructor
	private TicTacToeState(char[][] board, int moveCount, int size){
		this.board = board;
		this.moveCount = moveCount;
		this.size = size;
	}

	// Return all moves that can be made ()
	public Move[] getAllMoves(int player){
		ArrayList<Move> availableMoves = new ArrayList<>();
		for (int y = 0; y < size; y++){
			for (int x = 0; x < size; x++){
				if (board[y][x] == ' '){
					availableMoves.add(new Move(x,y,player));
				}
			}
		}
		return availableMoves.toArray(new Move[availableMoves.size()]);
	}


	// Update the state
	public TicTacToeState makeMove(Move move){
		moveCount++;

		int x = move.getX();
		int y = move.getY();
		board[y][x] = move.getPlayer();

		// Swicth players after move is made
		if (nextPlayer == 'x')
			nextPlayer = 'o';
		else if (nextPlayer == 'o')
			nextPlayer = 'x';
		return this;
	}

	//Display the board
	public void display()
	{
		for(int i = 0; i < size; i ++)
		{
			for(int j = 0; j < size; j ++)
			{
				System.out.print(board[j][i]);
			}
			System.out.println("");
		}
	}


	// Clone the state then make move
	public TicTacToeState makeMoveCloning(Move move){
		TicTacToeState clone = new TicTacToeState(this.board, this.moveCount, this.size);
		clone = clone.makeMove(move);
		return clone;
	}


	// Game over if all spaces are taken or [size] in a row
	public boolean gameOver(){

		// Check each row
		row:
			for (int y = 0; y < size; y++){
				char startingPlayer = board[y][0];
				if (startingPlayer == ' ')
					continue;
				for (int x = 1; x < size; x++){
					if (board[y][x] != startingPlayer)
						continue row;
				}
				return true;
			}

		// Check each col
		col:
			for (int x = 0; x < size; x++){
				char startingPlayer = board[0][x];
				if (startingPlayer == ' ')
					continue;
				for (int y = 1; y < size; y++){
					if (board[y][x] != startingPlayer)
						continue col;
				}
				return true;
			}

		// The 2 diagnols
		char startingPlayer = board[0][0];
		if (startingPlayer != ' '){
			for (int x = 1; x < size; x++){
				if (x == size-1 && board[x][x] == startingPlayer)
					return true;
				if (board[x][x] != startingPlayer)
					break;
			}
		}
		startingPlayer = board[size-1][0];
		if (startingPlayer != ' '){
			for (int x = 1; x < size; x++){
				if (x == size-1 && board[size-1-x][x] == startingPlayer)
					return true;
				if (board[size-1-x][x] != startingPlayer)
					break;
			}
		}

		return (moveCount >= size*size);
	}
}