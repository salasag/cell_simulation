package gui;

import java.io.IOException;
import java.util.ResourceBundle;

import XML.FileReader;
import cellsociety_team14.Control;
import gui.MenuBuilder;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * This is the main method for the program. It sets up the splash screens and
 * displays them.
 * 
 * @author Jack Bloomfeld
 * 
 */
public class SplashScreen extends Application {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	private ResourceBundle myResources;
	private Stage myStage;

	/**
	 * Runs the program by setting up a scene and then setting the stage
	 */
	public void start(Stage s) throws IOException {
		myStage = s;
		s.setResizable(false);
		myResources = ResourceBundle.getBundle(Control.RESOURCE_LOCATION);
		MenuBuilder builder = new MenuBuilder();
		Scene scene = setupSplash(builder);
		s.setScene(scene);
		s.setTitle(myResources.getString("TitleText"));
		s.show();
	}

	/**
	 * Sets up the colors, button, and text on the splash screen
	 * 
	 * @param builder
	 * @return returns a scene of the primary menu
	 */
	private Scene setupSplash(MenuBuilder builder) {
		VBox grid = new VBox();
		Text temp = builder.buildText(myResources.getString("TitleText"), Color.LIGHTGREEN, "Arial", 100);
		temp.setTextAlignment(TextAlignment.CENTER);
		grid.getChildren().add(temp);
		Button myButton = new Button(myResources.getString("GameOption1"));
		myButton.setOnAction(e -> startSimulation());
		myButton.setAlignment(Pos.BOTTOM_CENTER);
		grid.getChildren().add(myButton);
		grid.setAlignment(Pos.CENTER);
		grid.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
		Scene splash = new Scene(grid, WIDTH, HEIGHT);
		return splash;
	}

	/**
	 * This method opens up a file reader to read an XML file and then builds
	 * the appropriate scene, and sets the stage.
	 */
	public void startSimulation() {
		FileReader filer = new FileReader();
		Control controlObj = null;
		try {
			controlObj = filer.readFile(myStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		controlObj.setStage(myStage);
		SimulationBuilder gui = new SimulationBuilder(controlObj);
		Scene a = gui.buildSimulator();
		myStage.setScene(a);
	}

	/**
	 * This method is called to set a new simulation when the new simulation
	 * button on the menu is clicked
	 * 
	 * @param s
	 */
	public void resetSimulation(Stage s) {
		myStage = s;
		startSimulation();
	}

	public static void main(String[] args) {
		launch(args);
	}
}