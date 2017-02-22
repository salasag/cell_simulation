package XML;

import java.io.File;
import java.util.ArrayList;

import XML.XMLParser;
import cellsociety_team14.Control;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * This class is an adapted version of a class that reads in an XML file from a
 * directory folder, and parses the data from it
 * 
 * @author Jack Bloomfeld
 * @author Robert C. Duvall
 */
public class FileReader {

	private static final String DATA_FILE_EXTENSION = "*.xml";
	private FileChooser myChooser = makeChooser(DATA_FILE_EXTENSION);

	/**
	 * This method is given the stage and opens a selection window. It then
	 * reads the XML file and saves the data
	 * 
	 * @param primaryStage
	 * @return
	 * @throws Exception
	 */
	public Control readFile(Stage primaryStage) throws Exception {
		File dataFile = myChooser.showOpenDialog(primaryStage);
		if (dataFile != null) {
			try {
				XMLParser temp = new XMLParser();
				FileData input = temp.getFileData(dataFile);
				int rows = input.getGridRows();
				int cols = input.getGridColumns();
				String simType = input.getSimulationType();
				int param1 = input.getParam1();
				int param2 = input.getParam2();
				String stateTitle = input.getTitle();
				String author = input.getAuthor();
				String gridShape = input.getGridShape();
				String edgeType = input.getEdgeType();
				String gridOutlined = input.getGridOutlined();
				String cellColor = input.getCellColor();
				String cellShape = input.getCellShape();
				int speed = input.getSpeed();
				ArrayList<Integer> inState = (ArrayList<Integer>) input.getInitialState();
				return new Control(rows, cols, simType, param1, param2, stateTitle, author, gridShape, edgeType,
						gridOutlined, cellColor, cellShape, speed, inState);
			} catch (XMLException e) {
				showError(String.format(e.getMessage()));
			}
		} else {
			Platform.exit();
		}
		return null;
	}

	/**
	 * This method sets the parameters for the window that allows you to select
	 * an XML file
	 * 
	 * @param extensionAccepted
	 * @return
	 */
	private FileChooser makeChooser(String extensionAccepted) {
		FileChooser result = new FileChooser();
		result.setTitle("Open Data File");
		result.setInitialDirectory(new File(System.getProperty("user.dir")));
		result.getExtensionFilters().setAll(new ExtensionFilter("Text Files", extensionAccepted));
		return result;
	}

	/**
	 * This method displays an error when called with a specified message
	 * 
	 * @param message
	 */
	private void showError(String message) {
		Alert a = new Alert(AlertType.ERROR);
		a.setContentText(message);
		a.showAndWait();
	}

}
