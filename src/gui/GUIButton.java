package gui;

import java.util.Collection;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * Better improved button specific to our code. Made by during refactoring
 * 
 * @author Alex Salas
 */
public class GUIButton {
	private Button myButton;

	public GUIButton(String label, int row, int col, EventHandler<ActionEvent> buttonPress) {
		myButton = new Button();
		GridPane.setConstraints(myButton, col, row);
		myButton.setText(label);
		myButton.setOnAction(buttonPress);
	}

	/**
	 * Adds button collection to a Gridpane
	 */
	public static void addButtonsTo(Collection<GUIButton> buttons, GridPane gridPane) {
		for (GUIButton button : buttons) {
			gridPane.getChildren().add(button.getButton());
		}
	}

	/**
	 * getter
	 * 
	 * @return
	 */
	public Button getButton() {
		return myButton;
	}
}
