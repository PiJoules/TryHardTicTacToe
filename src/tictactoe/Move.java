package tictactoe;

public class Move {
	private int xPos, yPos;
	private char player; //0 for x, 1 for o

	public Move(int x, int y, int player){
		this.xPos = x;
		this.yPos = y;
		if(player == 0)
		{
			this.player = 'x';
		}
		else
		{
			this.player = 'o';
		}
	}
	public int getX(){
		return this.xPos;
	}
	public int getY(){
		return this.yPos;
	}
	public char getPlayer()
	{
		return player;
	}

	/**
	 * Makind our own toString
	 */
	@Override 
	public String toString()
	{
		return "Row: " + yPos + " Column: " + xPos; 
	}
}