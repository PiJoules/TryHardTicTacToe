package tictactoe;

public class Move {
	private int xPos, yPos;
	public Move(int x, int y){
		this.xPos = x;
		this.yPos = y;
	}
	public int getX(){
		return this.xPos;
	}
	public int getY(){
		return this.yPos;
	}
}