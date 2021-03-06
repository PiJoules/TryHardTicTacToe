package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;

// The current state of the board
public class TicTacToeState {

	private char[][] board;
	private int moveCount;
	private final int size;
	private int winner;

	// The current player making the move
	// Players are indicated with x and o
	// The first player is always 'x' since it's the first to get placed on the board; second is 'o'
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
				if (j != size-1)
					System.out.print("|");
			}
			if (i != size-1){
				System.out.println("");
				for (int j = 0; j < 2*size-1; j++){
					System.out.print("-");
				}
			}
			System.out.println("");
		}
	}


	// Clone the state then make move
	public TicTacToeState makeMoveCloning(Move move){
		char[][] clonedBoard = new char[size][size];
		//Clone the board
		for(int i = 0; i < size; i ++)
		{
			for(int j = 0; j < size; j ++)
			{
				clonedBoard[i][j] = this.board[i][j];
			}
		}
		//Clone the state
		TicTacToeState clone = new TicTacToeState(clonedBoard, this.moveCount, this.size);
		clone = clone.makeMove(move);
		return clone;
	}



	// playerChar is either 'x' pr 'o'
	public boolean playerDidWin(char playerChar){
		// Check each row
		row:
			for (int y = 0; y < size; y++){
				for (int x = 0; x < size; x++){
					if (board[y][x] != playerChar)
						continue row;
				}
				return true;
			}

		// Check each col
		col:
			for (int x = 0; x < size; x++){
				for (int y = 0; y < size; y++){
					if (board[y][x] != playerChar)
						continue col;
				}
				return true;
			}

		// The 2 diagnols
		for (int x = 0; x < size; x++){
			if (x == size-1 && board[x][x] == playerChar)
				return true;
			if (board[x][x] != playerChar)
				break;
		}
		for (int x = 0; x < size; x++){
			if (x == size-1 && board[size-1-x][x] == playerChar)
				return true;
			if (board[size-1-x][x] != playerChar)
				break;
		}

		return false;
	}

	public int getWinner(){
		return this.winner;
	}

	public int getMoveCount(){
		return this.moveCount;
	}

	// Game over if all spaces are taken or [size] in a row
	public boolean gameOver(){

		if (playerDidWin('x')){
			this.winner = 1;
			return true;
		}
		else if (playerDidWin('o')){
			this.winner = -1;
			return true;
		}

		//return (moveCount >= size*size);
		if (moveCount >= size*size){
			this.winner = 0; // tie
			return true;
		}

		return false;
	}
}