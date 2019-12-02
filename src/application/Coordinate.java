package application;

/**
 * This class is used by the model class (RabbitFarmModel) to create an
 * object that contains the coordinates of a placed turn and the token/player
 * who made the turn.
 * 
 * @author alissamonroe, meganlahm
 * @version 05/21/17
 */

public class Coordinate {

	/** Variable for x-coordinate of token */
	private int x;

	/** Variable for y-coordinate of token */
	private int y;

	/** Variable for character of token (b1, b2, farmer) */
	private int token;

	/** Constructor */
	public Coordinate(int newX, int newY, int newToken) {
		x = newX;
		y = newY;
		token = newToken;
	}

	/** Getter for x-coordinate */
	public int getX() {
		return x;
	}

	/** Getter for y-coordinate */
	public int getY() {
		return y;
	}

	/** Setter for token */
	public void setToken(int newToken) {
		token = newToken;
	}

	/** Getter for token */
	public int getToken() {
		return token;
	}
}




