package cellsociety_team14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import cell.ConwayState;
import cell.FireState;
import cell.SegregationState;
import cell.State;
import cell.WaTorState;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;
import structure.RectangularGrid;

/**
 * This class contains parameters that are read in from the XML file. It formats
 * some of the data and then allows other classes to access the data with
 * getters and setters
 * 
 * @author Jack
 *
 */
public class Control {
	private int rows;
	private int columns;
	private String simulationType;
	private int param1;
	private int param2;
	private String stateTitle;
	private String author;
	private String edgeType;
	private String gridOutlined;
	private int speed;
	private Collection<String> initialState;
	private RectangularGrid myGrid;
	private Stage myStage;
	public static final String RESOURCE_LOCATION = "resources/English";
	public static final double STAGE_WIDTH = 1000;
	public static final double STAGE_HEIGHT = 800;
	public static final String CONWAY_GAME = "conway";
	public static final String FIRE_GAME = "fire";
	public static final String WATOR_GAME = "wator";
	public static final String SEGREGATION_GAME = "segregation";
	public static final String LANGTON_GAME = "langton";

	private State[] myStates;
	private HashMap<State, Series<Number, Number>> mySeries;
	private NumberAxis myXAxis;

	/**
	 * Stores many variables that are needed by classes and acts as an
	 * intermediary from the frontend to the backend
	 */
	public Control(int rowsIn, int columnsIn, String typeIn, int param1In, int param2In, String staTitle, String autIn,
			String gridsIn, String edgeIn, String gridOutlinedIn, String colorIn, String shapeIn, int speedIn,
			Collection<Integer> inState) {
		rows = rowsIn;
		columns = columnsIn;
		simulationType = typeIn;
		param1 = param1In;
		param2 = param2In;
		stateTitle = staTitle;
		author = autIn;
		edgeType = edgeIn;
		gridOutlined = gridOutlinedIn;
		speed = speedIn;
		initialState = setStates(inState);
		myGrid = new RectangularGrid(rows, columns, initialState, STAGE_HEIGHT, simulationType, param1, param2,
				edgeType);
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public String getType() {
		return simulationType;
	}

	public int getParam1() {
		return param1;
	}

	public int getParam2() {
		return param2;
	}

	public boolean getGridOutlined() {
		return gridOutlined.equals("true");
	}

	public String getStateTitle() {
		return stateTitle;
	}

	public String getAuthor() {
		return author;
	}

	public int getSpeed() {
		return speed;
	}

	public Collection<String> getInitialState() {
		return initialState;
	}

	public RectangularGrid getGrid() {
		return myGrid;
	}

	public Stage getStage() {
		return myStage;
	}

	public void setStage(Stage stageIn) {
		myStage = stageIn;
		myStage.setTitle(stateTitle);
	}

	public void setXAxis(NumberAxis xAxis) {
		myXAxis = xAxis;
	}

	/**
	 * Adds the graph and initializes
	 */
	public void setData(LineChart<Number, Number> graph) {
		myStates = myGrid.getBlock(0, 0).getCell().getStates();
		mySeries = new HashMap<State, Series<Number, Number>>();
		for (int i = 0; i < myStates.length; i++) {
			Series<Number, Number> stateSeries = new Series<Number, Number>();
			stateSeries.setName(myStates[i].getName());
			mySeries.put(myStates[i], stateSeries);
			graph.getData().add(stateSeries);
		}
	}

	/**
	 * Adds updates to the graph
	 */
	public void addGraphData(int frames, int[] frequencies) {
		int dataPoints = 50;
		for (int i = 0; i < myStates.length; i++) {
			Series<Number, Number> currentSeries = mySeries.get(myStates[i]);
			int num = frequencies[i];
			currentSeries.getData().add(new Data<Number, Number>(frames, num));
			if (currentSeries.getData().size() > dataPoints) {
				currentSeries.getData().remove(0, currentSeries.getData().size() - dataPoints);
			}
		}
		myXAxis.setLowerBound(frames - dataPoints);
		myXAxis.setUpperBound(frames - 1);

	}

	private Collection<String> setStates(Collection<Integer> inState) {
		Collection<String> states = new ArrayList<String>();
		for (int a : inState) {
			if (a == 0) {
				states.add(State.EMPTY_STATE);
			} else if (a == 1) {
				if (simulationType.equals(CONWAY_GAME)) {
					states.add(ConwayState.CONWAY_ALIVE);
				} else if (simulationType.equals(FIRE_GAME)) {
					states.add(FireState.TREE);
				} else if (simulationType.equals(WATOR_GAME)) {
					states.add(WaTorState.WATOR_PREY);
				} else if (simulationType.equals(SEGREGATION_GAME)) {
					states.add(SegregationState.SPECIES1);
				}
			} else if (a == 2) {
				if (simulationType.equals(FIRE_GAME)) {
					states.add(FireState.BURNING_TREE);
				} else if (simulationType.equals(WATOR_GAME)) {
					states.add(WaTorState.WATOR_PREDATOR);
				} else if (simulationType.equals(SEGREGATION_GAME)) {
					states.add(SegregationState.SPECIES2);
				}
			}
		}
		return states;
	}
}