package application;

import java.util.Observable;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**
 * This class is used by the view class (View). It creates a button and adds an
 * observer to that button, so that in the view, each button in an array of
 * buttons has interactions accounted for.
 * 
 * @author alissamonroe, meganlahm
 * @version 05/21/17
 */

public class MoveButtons extends Button implements Observer, EventHandler<ActionEvent> {

	/** Variable for x-coordinate of token */
	private int x;

	/** Variable for y-coordinate of token */
	private int y;

	/** Object for the model */
	private RabbitFarmModel reference;

	/** Constructor */
	public MoveButtons(int newX, int newY, RabbitFarmModel newReference) {
		super();
		x = newX;
		y = newY;
		reference = newReference;
		reference.addObserver(this);
		this.setOnAction(this);
	}

	/** Getter for x-coordinate */
	public int getX() {
		return x;
	}

	/** Getter for y-coordinate */
	public int getY() {
		return y;
	}

	/** The handle method */
	@Override
	public void handle(ActionEvent event) {
		try {
			reference.validMove(getX(), getY());
		} catch (Exception e) {
			Alert invalidAlert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			invalidAlert.showAndWait();
		}
	}

	/** The update method */
	@Override
	public void update(Observable o, Object arg) {
		if (reference.getToken(getX(), getY()) == 0) {
			this.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		} else if (reference.getToken(getX(), getY()) == 1) {
			this.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
		} else if (reference.getToken(getX(), getY()) == 2) {
			this.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
		} else if (reference.getToken(getX(), getY()) == -1) {
			this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
		}
	}
}
