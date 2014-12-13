// The move is the tile the player will place their
// piece on (an array of size 2 where the first element
// is x pos and second is y pos).

// The center of the board is the origin (0,0) and
// the board is laid out like so
/*


(-1,-1)  |	(0,-1)	 |	(1,-1)
_________|___________|________
		 |			 |
(-1,0)	 |	(0,0)	 |	(1,0)
_________|___________|_________
		 |			 |
(-1,1)	 |	(0,1)	 |	(1,1)

*/

public class Move {
	private static int xPos, yPos;
	public Move(Player player, int x, int y){
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