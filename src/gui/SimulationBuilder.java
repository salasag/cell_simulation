package gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import cellsociety_team14.Animation;
import cellsociety_team14.Block;
import cellsociety_team14.Control;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import structure.RectangularGrid;

/**
 * Builds the simulation scene with various parts
 * 
 * @author Alex
 *
 */
public class SimulationBuilder {
	private double SCENE_WIDTH = Control.STAGE_WIDTH;
	private double SCENE_HEIGHT = Control.STAGE_HEIGHT;
	private float SLIDER_DEFAULT_VALUE = 50;
	private Control controlObj;
	private Animation myAnimation;
	private ResourceBundle myResources;

	/**
	 * Creates new simulation builder with control object
	 * 
	 * @param controlIn
	 */
	public SimulationBuilder(Control controlIn) {
		controlObj = controlIn;
		myAnimation = new Animation(controlIn);
		myResources = ResourceBundle.getBundle(Control.RESOURCE_LOCATION);
	}

	/**
	 * Returns the simulator scene
	 * 
	 * @return
	 */
	public Scene buildSimulator() {
		RectangularGrid myBlocks = controlObj.getGrid();
		ArrayList<Shape> rectangleArray = new ArrayList<Shape>();
		for (Block block : myBlocks) {
			Shape curRectangle = block.getShape();
			rectangleArray.add(curRectangle);
		}
		GridPane myRoot = createGridPane(0, 0, new Insets(0));
		myRoot.setAlignment(Pos.CENTER);
		RowConstraints menuRow = new RowConstraints();
		menuRow.setPercentHeight(Integer.parseInt(myResources.getString("MenuRowPercent")));
		menuRow.setValignment(VPos.TOP);
		RowConstraints controlRow = new RowConstraints();
		controlRow.setPercentHeight(Integer.parseInt(myResources.getString("ControlRowPercent")));
		ColumnConstraints controlColumn = new ColumnConstraints();
		controlColumn.setPercentWidth(Integer.parseInt(myResources.getString("ControlColumnPercent")));
		ColumnConstraints cellColumn = new ColumnConstraints();
		cellColumn.setPercentWidth(Integer.parseInt(myResources.getString("CellColumnPercent")));
		myRoot.getRowConstraints().addAll(menuRow, controlRow);
		myRoot.getColumnConstraints().addAll(controlColumn, cellColumn);

		createCellGridFrame(myRoot, myBlocks, rectangleArray, controlObj.getRows(), controlObj.getColumns());
		createControlPanel(myRoot);
		createSeparator(myRoot);
		createMenuBar(myRoot);

		Scene mySimulator = new Scene(myRoot, SCENE_WIDTH, SCENE_HEIGHT, Color.BURLYWOOD);
		return mySimulator;
	}

	/**
	 * Creates the frame for the cells
	 * 
	 * @param myRoot
	 * @param grid
	 * @param rects
	 * @param rows
	 * @param cols
	 */
	private void createCellGridFrame(Pane myRoot, RectangularGrid grid, ArrayList<Shape> rects, int rows, int cols) {
		GridPane cellGrid = grid.getVisualGrid();
		createCellGrid(cellGrid, grid, rects, rows, cols);
		cellGrid.setTranslateY(-7);
		GridPane.setConstraints(cellGrid, 1, 1);
		myRoot.getChildren().addAll(cellGrid);
		ScrollBar scrollHorizontal = new ScrollBar();
		GridPane.setConstraints(scrollHorizontal, 1, 2);
		scrollHorizontal.setMin(-rects.get(0).getBoundsInLocal().getWidth() * grid.getMaxRows());
		scrollHorizontal.setMax(rects.get(0).getBoundsInLocal().getWidth() * grid.getMaxCols());
		scrollHorizontal.setOrientation(Orientation.HORIZONTAL);
		scrollHorizontal.setTranslateY(-14);
		scrollHorizontal.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				cellGrid.setTranslateX(-new_val.doubleValue());
			}
		});
		ScrollBar scrollVertical = new ScrollBar();
		GridPane.setConstraints(scrollVertical, 2, 1);
		scrollVertical.setMin(-rects.get(0).getBoundsInLocal().getHeight() * rows);
		scrollVertical.setMax(rects.get(0).getBoundsInLocal().getHeight() * rows);
		scrollVertical.setOrientation(Orientation.VERTICAL);
		scrollVertical.setTranslateX(-8);
		scrollVertical.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				cellGrid.setTranslateY(-new_val.doubleValue() - 7);
			}
		});
		myRoot.getChildren().addAll(scrollVertical, scrollHorizontal);
	}

	/**
	 * Creates the grid for the cells
	 * 
	 * @param cellGrid
	 * @param grid
	 * @param rects
	 * @param rows
	 * @param cols
	 */
	private void createCellGrid(GridPane cellGrid, RectangularGrid grid, ArrayList<Shape> rects, int rows, int cols) {
		int rowBounds = rows;
		int colBounds = cols;
		if (grid.getGridType().equals("infinite")) {
			rowBounds = grid.getMaxRows();
			colBounds = grid.getMaxCols();
		}
		for (int i = 0; i < rowBounds; i++) {
			RowConstraints rowConstraint = new RowConstraints();
			rowConstraint.setPercentHeight(100);
			cellGrid.getRowConstraints().add(rowConstraint);
		}
		for (int i = 0; i < colBounds; i++) {
			ColumnConstraints colConstraint = new ColumnConstraints();
			colConstraint.setPercentWidth(100);
			cellGrid.getColumnConstraints().add(colConstraint);
		}
		if (controlObj.getGridOutlined()) {
			cellGrid.setGridLinesVisible(true);
		} else {
			cellGrid.setGridLinesVisible(false);
		}
		GridPane.setHalignment(cellGrid, HPos.CENTER);
		GridPane.setValignment(cellGrid, VPos.CENTER);
	}

	/**
	 * Creates the menubar
	 * 
	 * @param myRoot
	 */
	private void createMenuBar(Pane myRoot) {
		MenuBar menuBar = new MenuBar();
		Menu simulationMenu = new Menu(myResources.getString("DropDownLabel"));
		MenuItem newSim = new MenuItem("New Simulation");
		newSim.setOnAction(e -> {
			SplashScreen resetter = new SplashScreen();
			resetter.resetSimulation(controlObj.getStage());
		});
		simulationMenu.getItems().add(newSim);
		menuBar.getMenus().addAll(simulationMenu);
		GridPane.setConstraints(menuBar, 0, 0);
		GridPane.setColumnSpan(menuBar, 3);
		myRoot.getChildren().add(menuBar);
	}

	/**
	 * Creates the control panel and graph
	 * 
	 * @param myRoot
	 */
	private void createControlPanel(Pane myRoot) {
		GridPane controlGrid = createGridPane(10, 30, new Insets(0, 10, 0, 10));
		createTitle(controlGrid, myResources.getString("ControlPanelTitle"), 0);
		createCaptionSlider(controlGrid, myResources.getString("SpeedSliderLabel"), 1);
		createStartStepStopButtons(controlGrid, 3);
		createPopulationGraph(controlGrid, 4);
		GridPane.setConstraints(controlGrid, 0, 1, 1, 1);
		Rectangle background = new Rectangle(SCENE_WIDTH * .2, SCENE_HEIGHT, Color.ROYALBLUE);
		GridPane.setConstraints(background, 0, 0, 1, 2);
		controlGrid.getChildren().add(background);
		myRoot.getChildren().addAll(background, controlGrid);
	}

	/**
	 * Creates graph
	 * 
	 * @param grid
	 * @param row
	 */
	private void createPopulationGraph(GridPane grid, int row) {
		NumberAxis xAxis = new NumberAxis();
		xAxis.setAutoRanging(false);
		xAxis.setForceZeroInRange(false);
		NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(true);
		LineChart<Number, Number> graph = new LineChart<Number, Number>(xAxis, yAxis);
		graph.setAnimated(false);
		GridPane.setConstraints(graph, 0, row, 3, 1);
		graph.setTitle("Species Populations");
		grid.getChildren().add(graph);
		controlObj.setData(graph);
		controlObj.setXAxis(xAxis);
	}

	/**
	 * Creates buttons for control
	 * 
	 * @param sliderGrid
	 * @param row
	 */
	private void createStartStepStopButtons(GridPane sliderGrid, int row) {
		String[] buttonLabels = { "StartButtonLabel", "StepButtonLabel", "StopButtonLabel" };
		EventHandler<ActionEvent> start = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				myAnimation.startAnimation(controlObj.getSpeed());
			}
		};
		EventHandler<ActionEvent> step = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				myAnimation.pause();
				myAnimation.updateCells();
			}
		};
		EventHandler<ActionEvent> stop = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				myAnimation.pause();
			}
		};
		ArrayList<EventHandler<ActionEvent>> events = new ArrayList<EventHandler<ActionEvent>>();
		events.add(start);
		events.add(step);
		events.add(stop);
		Collection<GUIButton> buttons = new ArrayList<GUIButton>();
		for (int i = 0; i < buttonLabels.length; i++) {
			GUIButton newButton = new GUIButton(myResources.getString(buttonLabels[i]), row, i, events.get(i));
			buttons.add(newButton);
		}
		GUIButton.addButtonsTo(buttons, sliderGrid);
	}

	/**
	 * Creates a gridpane with specifications
	 * 
	 * @param hGap
	 * @param vGap
	 * @param insets
	 * @return
	 */
	private GridPane createGridPane(int hGap, int vGap, Insets insets) {
		GridPane grid = new GridPane();
		grid.setPadding(insets);
		grid.setHgap(hGap);
		grid.setVgap(vGap);
		return grid;
	}

	/**
	 * Creates bar of separation
	 * 
	 * @param grid
	 */
	private void createSeparator(GridPane grid) {
		Rectangle separator = new Rectangle(10, SCENE_HEIGHT);
		GridPane.setConstraints(separator, 1, 0);
		GridPane.setRowSpan(separator, 3);
		grid.getChildren().add(separator);
	}

	/**
	 * Creates the Text title
	 * 
	 * @param group
	 * @param title
	 * @param row
	 */
	private void createTitle(Pane group, String title, int row) {
		Text titleText = new Text(title);
		titleText.setFont(new Font(24));
		GridPane.setConstraints(titleText, 0, row);
		GridPane.setColumnSpan(titleText, 3);
		group.getChildren().add(titleText);
	}

	/**
	 * Creates a slider with specifications
	 * 
	 * @param group
	 * @param caption
	 * @param row
	 */
	private void createCaptionSlider(Pane group, String caption, int row) {
		Text sliderCaption = createCaption(caption, row);
		Label sliderLabel = createValueLabel(row);
		Slider slider = createSlider(row + 1, sliderLabel, new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				sliderLabel.setText(String.format("%.2f", new_val));
				myAnimation.changeSpeed(new_val.doubleValue());
			}
		});
		group.getChildren().addAll(sliderCaption, sliderLabel, slider);
	}

	/**
	 * Creates label for slider
	 * 
	 * @param row
	 * @return
	 */
	private Label createValueLabel(int row) {
		Label sliderLabel = new Label(String.format("%.2f", SLIDER_DEFAULT_VALUE));
		sliderLabel.setAlignment(Pos.TOP_RIGHT);
		GridPane.setConstraints(sliderLabel, 1, row);
		return sliderLabel;
	}

	/**
	 * Creates slider
	 * 
	 * @param row
	 * @param sliderValue
	 * @param e
	 * @return
	 */
	private Slider createSlider(int row, Label sliderValue, ChangeListener<Number> e) {
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(100);
		slider.setValue(SLIDER_DEFAULT_VALUE);
		slider.setBlockIncrement(10);
		GridPane.setConstraints(slider, 0, row);
		GridPane.setColumnSpan(slider, 3);
		slider.valueProperty().addListener(e);
		return slider;
	}

	/**
	 * Creates a caption
	 * 
	 * @param s
	 * @param row
	 * @return
	 */
	private Text createCaption(String s, int row) {
		Text captionText = new Text(s);
		GridPane.setConstraints(captionText, 0, row);
		return captionText;
	}

}
