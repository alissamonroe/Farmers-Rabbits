package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

/**
 * This class is the model class for the Rabbit Farmer game.It creates a 2D
 * array of Coordinate objects for the game board, and makes use of an ArrayList
 * of Coordinates that is manipulated in order to play the game.
 * 
 * @author alissamonroe, meganlahm
 * @version 05/21/17
 */

public class RabbitFarmModel extends Observable {
	/** 2-D array containing coordinate objects of token moves */
	private Coordinate corArr[][];

	/** ArrayList containing coordinate objects already accessed */
	private ArrayList<Coordinate> accessedCorList;

	/** Queue that holds coordinate objects when checking farmer paths */
	private Queue<Coordinate> corQueue;

	/** Variable that keeps track of each turn */
	private int turn;

	/** Variable for player 1, first bunny */
	private int bunny1;

	/** Variable for player 1, second bunny */
	private int bunny2;

	/** Variable for player 2, farmer */
	private int farmer;

	/**
	 * Variable for the state of the game; false: in progress, true: game over
	 */
	private boolean gameState;

	/**
	 * Constructor - Setting up the board and initializing all other instance
	 * variables
	 */
	public RabbitFarmModel(int size) {
		super();
		corArr = new Coordinate[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				corArr[i][j] = new Coordinate(i, j, -1);
			}
		}
		accessedCorList = new ArrayList<Coordinate>();
		corQueue = new LinkedList<Coordinate>();
		bunny1 = 0;
		bunny2 = 1;
		farmer = 2;
		turn = bunny1;
		gameState = false;
	}

	/**
	 * Checks the surrounding coordinates of the entered coordinate for other
	 * moves - Adds to arrayList the surrounding coordinates (objects)
	 * 
	 * @param x
	 *            the row coordinate of the object
	 * @param y
	 *            the column coordinate of the object
	 * @param token
	 *            the char of the object (bunny or farmer)
	 * @return the arrayList of surrounding coordinates
	 */
	public ArrayList<Coordinate> checkAround(int x, int y, int token) {
		ArrayList<Coordinate> methodCorList = new ArrayList<Coordinate>();
		if (x > 0 && corArr[x - 1][y].getToken() != -1)
			methodCorList.add(corArr[x - 1][y]);
		if (x < (getSize() - 1) && corArr[x + 1][y].getToken() != -1)
			methodCorList.add(corArr[x + 1][y]);
		if (y < (getSize() - 1) && corArr[x][y + 1].getToken() != -1)
			methodCorList.add(corArr[x][y + 1]);
		if (y > 0 && corArr[x][y - 1].getToken() != -1)
			methodCorList.add(corArr[x][y - 1]);
		if (token == farmer) {
			if ((x > 0) && (y > 0) && corArr[x - 1][y - 1].getToken() == 2)
				methodCorList.add(corArr[x - 1][y - 1]);
			if ((y > 0) && (x < getSize() - 1) && corArr[x + 1][y - 1].getToken() == 2)
				methodCorList.add(corArr[x + 1][y - 1]);
			if ((x < getSize() - 1) && (y < getSize() - 1) && corArr[x + 1][y + 1].getToken() == 2)
				methodCorList.add(corArr[x + 1][y + 1]);
			if ((y < getSize() - 1) && (x > 0) && corArr[x - 1][y + 1].getToken() == 2)
				methodCorList.add(corArr[x - 1][y + 1]);
		}
		return methodCorList;
	}

	/**
	 * Checks to see if the move trying to be played is valid to the player,
	 * changes turn if valid. Also checks for bunny and farmer wins.
	 * 
	 * @param x
	 *            the row coordinate attempted
	 * @param y
	 *            the column coordinate attempted
	 */
	public void validMove(int x, int y) throws Exception {
		if (gameState == true) {
			return; // quit method
		} else if (turn == bunny1 || turn == bunny2) { // turn is a bunny
			boolean b1close = false;
			boolean b2close = false;
			if (turn == bunny1 && x == 0) // if north bunny (top row)
				b1close = true;
			if (turn == bunny2 && x == getSize() - 1) // if south bunny (bottom row)
				b2close = true;

			ArrayList<Coordinate> tempList = checkAround(x, y, turn);
			
			if (tempList != null) {
				for (int i = 0; i < tempList.size(); i++) {
					if (tempList.get(i).getToken() == bunny1)
						b1close = true;
					else if (tempList.get(i).getToken() == bunny2)
						b2close = true;
				}
			}
			if (corArr[x][y].getToken() == -1
					&& ((turn == bunny1 && x == 0) || (turn == bunny2 && x == getSize() - 1))) {
				// ^ if north bunny plays on top row or south bunny plays on bottom
				corArr[x][y].setToken(turn);
				// now check if there was a win from that default move
				if ((b1close && turn == bunny1) || (b2close && turn == bunny2)) {
					if (turn == bunny1 && x == getSize() - 1)
						gameState = true;
					else if (turn == bunny2 && x == 0)
						gameState = true;
					else if ((turn == bunny1 && b2close) || (turn == bunny2 && b1close))
						gameState = true;
				}
				turn = (turn + 1) % 3; // change turn
				setChanged();
				notifyObservers(getToken(x, y));
				return;
			}
			// any other moves made by the bunnies
			if (corArr[x][y].getToken() == -1 && ((b1close && turn == bunny1) || (b2close && turn == bunny2))) {
				corArr[x][y].setToken(turn);
				if (turn == bunny1 && x == getSize() - 1)
					gameState = true;
				else if (turn == bunny2 && x == 0)
					gameState = true;
				else if ((turn == bunny1 && b2close) || (turn == bunny2 && b1close))
					gameState = true;
				turn = (turn + 1) % 3; // change turn
				setChanged();
				notifyObservers(getToken(x, y));
			} else {
				throw new Exception("Invalid Bunny Move");
			}
		} else { // turn == farmer
			if (corArr[x][y].getToken() == -1) {
				corArr[x][y].setToken(farmer);
				turn = (turn + 1) % 3; // change turn
				setChanged();
				notifyObservers(getToken(x, y));
				farmerPath();
			} else {
				throw new Exception("Invalid Farmer Move");
			}
		}
	}

	/**
	 * Checks is there is a farmer path against the board, meaning the farmer
	 * has won
	 */
	public void farmerPath() {
		accessedCorList = new ArrayList<Coordinate>();
		for (int i = 0; i < getSize(); i++) {
			if (corArr[i][0].getToken() == farmer) {
				corQueue.add(corArr[i][0]);
				accessedCorList.add(corArr[i][0]);
			}
		}
		while (!corQueue.isEmpty()) {
			Coordinate tempC = corQueue.peek();
			ArrayList<Coordinate> tempAL = checkAround(tempC.getX(), tempC.getY(), tempC.getToken());
			for (int i = 0; i < tempAL.size(); i++) {
				if (!accessedCorList.contains(tempAL.get(i)) && (tempAL.get(i).getToken() == farmer)) {
					corQueue.add(tempAL.get(i));
					if (tempAL.get(i).getY() == getSize() - 1) {
						gameState = true;
						setChanged();
						notifyObservers(getToken(tempAL.get(i).getX(), tempAL.get(i).getY()));
					}
				}
			}
			corQueue.remove();
			if (!accessedCorList.contains(tempC))
				accessedCorList.add(tempC);
		}
	}

	/**
	 * Setter for size
	 */
	public void setSize(int size) {
		corArr = new Coordinate[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				corArr[i][j] = new Coordinate(i, j, -1);
			}
		}
		turn = bunny1;
		setChanged();
		notifyObservers(getSize());
	}

	/**
	 * Getter for size
	 */
	public int getSize() {
		return corArr.length;
	}

	/**
	 * Getter that returns value 0, 1, or 2 depending on the token of the turn;
	 * 0: bunny1, 1: bunny2, 2: farmer
	 */
	public int getToken(int x, int y) {
		return corArr[x][y].getToken();
	}

	/** Getter for the turn variable */
	public int getTurn() {
		return turn;
	}

	/** Getter for the state of the game */
	public boolean getGameState() {
		return gameState;
	}

	/**
	 * Returns a title of "Rabbits vs. Farmer"
	 */
	public String getTitle() {
		return "Rabbits vs. Farmer";
	}

	/**
	 * Clear out all values
	 */
	public void clear() {
		corArr = new Coordinate[getSize()][getSize()];
		accessedCorList = new ArrayList<Coordinate>();
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
				corArr[i][j] = new Coordinate(i, j, -1);
			}
		}
		corQueue = new LinkedList<Coordinate>();
		turn = bunny1;
		gameState = false;
		setChanged();
		notifyObservers(getSize());
	}

	/**
	 * Returns a string of the current condition of the game
	 */
	public String getFeedback() {
		if (gameState == false && turn == bunny1)
			return "North Rabbit's Turn";
		else if (gameState == false && turn == bunny2)
			return "South Rabbit's Turn";
		else if (gameState == false && turn == farmer)
			return "Farmer's Turn";
		if (gameState == true && (turn == farmer || turn == bunny2))
			return "Game Over: Rabbits Win";
		else
			return "Game Over: Farmer Wins";
	}
}