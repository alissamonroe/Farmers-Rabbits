package application;

import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This is the view class, which which provides a view for a model class (namely
 * RabbitFarmModel), allowing user interaction and results to show accordingly (GUI).
 * 
 * @author alissamonroe, meganlahm
 * @version 05/21/17
 */

public class View extends Application implements Observer, EventHandler<ActionEvent> {

	/** Object for the game */
	private RabbitFarmModel board;

	/** ComboBoc containing board size options */
	private ComboBox<Integer> boardOptionsCB;

	/** Buttons (Squares) for each slot on the board */
	private MoveButtons[][] slots;

	/** Clear button */
	private Button clearB;

	/** Feedback label */
	private Label feedbackL;

	/** The middle grid pane containing the rectangle slots */
	private GridPane midGP;

	/** The bottom grid pane containg the feedback label */
	private GridPane bottomGP;

	/** Rectangles for red, blue, and orange borders */
	private FlowPane north;
	private FlowPane east;
	private FlowPane south;
	private FlowPane west;

	@Override
	public void start(Stage primaryStage) {
		try {
			board = new RabbitFarmModel(3);
			board.addObserver(this);

			GridPane GP = new GridPane();
			GP.setAlignment(Pos.CENTER);
			Scene scene = new Scene(GP, 700, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle(board.getTitle());

			north = new FlowPane();
			north.setPrefWidth(board.getSize() * 50 + 100);
			north.setPrefHeight(50);
			north.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));

			west = new FlowPane();
			west.setPrefWidth(50);
			west.setPrefHeight(board.getSize() * 50);
			west.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));

			east = new FlowPane();
			east.setPrefWidth(50);
			east.setPrefHeight(board.getSize() * 50);
			east.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));

			south = new FlowPane();
			south.setPrefWidth(board.getSize() * 50 + 100);
			south.setPrefHeight(50);
			south.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));

			BorderPane BP = new BorderPane();
			BP.setTop(north);
			BP.setCenter(createMiddlePane());
			BP.setLeft(west);
			BP.setRight(east);
			BP.setBottom(south);

			GridPane topGP = new GridPane();
			topGP.add(createTopPane(), 0, 0);
			topGP.add(BP, 0, 1);

			bottomGP = new GridPane();
			feedbackL = new Label(board.getFeedback());
			feedbackL.setTextFill(Color.RED);
			feedbackL.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
			bottomGP.add(feedbackL, 0, board.getSize() + 1);
			bottomGP.setAlignment(Pos.CENTER);

			GP.add(topGP, 0, 0);
			GP.add(bottomGP, 0, 1);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The top pane which has the ComboBox for board size options
	 */
	private GridPane createTopPane() {
		GridPane topGP = new GridPane();

		Label option = new Label("Choose a board size: ");
		topGP.add(option, 0, 0);

		// Adding combo box options
		boardOptionsCB = new ComboBox<Integer>();
		boardOptionsCB.getItems().addAll(5, 8, 10, 13, 15);
		boardOptionsCB.setOnAction(this);
		topGP.add(boardOptionsCB, 1, 0);

		clearB = new Button("Reset");
		clearB.setOnAction(this);
		topGP.add(clearB, 2, 0);

		return topGP;
	}

	/**
	 * The middle pane which contains the board
	 */
	private GridPane createMiddlePane() {
		midGP = new GridPane();
		midGP.setGridLinesVisible(true);
		slots = new MoveButtons[board.getSize()][board.getSize()];
		for (int i = 0; i < board.getSize(); i++) {
			for (int j = 0; j < board.getSize(); j++) {
				slots[j][i] = new MoveButtons(i, j, board);
				slots[j][i].setPrefSize(50, 50);
				slots[j][i].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
				midGP.add(slots[j][i], j + 1, i + 1);
			}
		}
		return midGP;
	}

	/** The handle method */
	@Override
	public void handle(ActionEvent event) {
		// When the clear square is pressed
		if (event.getSource() == clearB) {
			board.clear();
			board.addObserver(this);
		}

		// When a choice is made on the combo box
		if (boardOptionsCB.getValue() != null && boardOptionsCB.getValue() != board.getSize()) {
			int value = boardOptionsCB.getSelectionModel().getSelectedItem();
			board.deleteObservers();
			board.addObserver(this);
			board.setSize(value);
			board.clear();
		} else {
			feedbackL.setText(board.getFeedback());
		}
	}

	/** The update method */
	@Override
	public void update(Observable o, Object arg) {
		// Updating the square according to the ComboBox
		if (slots.length != board.getSize()) {
			slots = new MoveButtons[board.getSize()][board.getSize()];
			midGP.getChildren().clear();
			midGP.setGridLinesVisible(false);
			midGP.setGridLinesVisible(true);
			for (int i = 0; i < board.getSize(); i++) {
				for (int j = 0; j < board.getSize(); j++) {
					slots[j][i] = new MoveButtons(i, j, board);
					slots[j][i].setPrefSize(50, 50);
					midGP.add(slots[j][i], j + 1, i + 1);
				}
			}

			// Updating the colored border according to board size
			north.setPrefWidth(board.getSize() * 50 + 100);
			north.setPrefHeight(50);
			north.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));

			west.setPrefWidth(50);
			west.setPrefHeight(board.getSize() * 50);
			west.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));

			east.setPrefWidth(50);
			east.setPrefHeight(board.getSize() * 50);
			east.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));

			south.setPrefWidth(board.getSize() * 50 + 100);
			south.setPrefHeight(50);
			south.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
		}

		// Updating of feedback
		feedbackL.setText("");
		if (board.getGameState() == true) {
			if (board.getTurn() == 0) {
				feedbackL = new Label(board.getFeedback());
				feedbackL.setTextFill(Color.ORANGE);
				feedbackL.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
			} else if (board.getTurn() == 1) {
				feedbackL = new Label(board.getFeedback());
				feedbackL.setTextFill(Color.RED);
				feedbackL.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
			} else {
				feedbackL = new Label(board.getFeedback());
				feedbackL.setTextFill(Color.BLUE);
				feedbackL.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
			}
		} else {
			if (board.getTurn() == 0) {
				feedbackL = new Label(board.getFeedback());
				feedbackL.setTextFill(Color.RED);
				feedbackL.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
			} else if (board.getTurn() == 1) {
				feedbackL = new Label(board.getFeedback());
				feedbackL.setTextFill(Color.BLUE);
				feedbackL.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
			} else {
				feedbackL = new Label(board.getFeedback());
				feedbackL.setTextFill(Color.ORANGE);
				feedbackL.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
			}
		}
		bottomGP.add(feedbackL, 0, 1);
		bottomGP.setAlignment(Pos.CENTER);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
