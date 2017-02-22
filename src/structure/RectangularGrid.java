package structure;
/**
 * Alex Salas 
 * Rectangular grid object holds and connects bricks and holds them in an Array 
 * 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cell.*;
import cellsociety_team14.Block;
import cellsociety_team14.SquareBlock;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Grid that manages the visual Gridpane and the Collection of blocks
 * 
 * @author Alex Salas
 *
 */
public class RectangularGrid extends Grid implements Iterable<Block> {
	private ArrayList<Block> myGrid;
	private double rectangleWidth;
	private double rectangleHeight;
	private String simulationType;
	private int myMaxRows;
	private int myMaxCols;
	private int myRows;
	private int myCols;
	private int myInitialRows;
	private int myInitialCols;
	private int myNewTopRows;
	private int myNewLeftCols;
	private int adjacentCoordinates[];
	private int param1;
	private int param2;
	private GridPane myVisualGrid = new GridPane();
	private String gridType;

	/**
	 * Creates constructors
	 * 
	 * @param rows
	 * @param columns
	 * @param initialState
	 * @param SIZE
	 * @param type
	 * @param param1In
	 * @param param2In
	 * @param gridTypeIn
	 */
	public RectangularGrid(int rows, int columns, Collection<String> initialState, double SIZE, String type,
			int param1In, int param2In, String gridTypeIn) {
		myGrid = new ArrayList<Block>();
		myRows = rows;
		myCols = columns;
		simulationType = type;
		myMaxRows = rows;
		myMaxCols = columns;
		myInitialRows = rows;
		myInitialCols = columns;
		param1 = param1In;
		param2 = param2In;
		gridType = gridTypeIn;
		if (gridType.equals("infinite")) {
			myMaxRows = 1000;
			myMaxCols = 1000;
		}
		rectangleWidth = (SIZE * .85) / columns;
		rectangleHeight = (SIZE * .75) / rows;
		int initialAdjacents[] = { -myCols - 1, -myCols, -myCols + 1, -1, 0, +1, +myCols - 1, +myCols, +myCols + 1, };
		adjacentCoordinates = initialAdjacents;
		setInitialState(initialState);
		connectGrid();
	}

	/**
	 * Initializes the grid connections
	 * 
	 * @param initialState
	 */
	private void setInitialState(Collection<String> initialState) {
		for (int i = 0; i < myRows * myCols; i++) {
			myGrid.add(new SquareBlock(getI(i), getJ(i)));
		}
		Iterator<Block> iterBlock = myGrid.iterator();
		Iterator<String> iterState = initialState.iterator();
		while (iterBlock.hasNext() && iterState.hasNext()) {
			Block block = iterBlock.next();
			String initial = iterState.next();
			initializeBlock(block, initial);
		}
	}

	/**
	 * Getter
	 */
	public int getI(Block block) {
		return myGrid.indexOf(block) / myCols;
	}

	/**
	 * Getter
	 */
	public int getJ(Block block) {
		return myGrid.indexOf(block) % myCols;
	}

	/**
	 * Getter
	 */
	public int getI(int i) {
		return i / myCols;
	}

	/**
	 * Getter
	 */
	public int getJ(int j) {
		return j % myCols;
	}

	/**
	 * Getter
	 */
	public String getGridType() {
		return gridType;
	}

	/**
	 * Getter
	 */
	public int getMaxRows() {
		return myMaxRows;
	}

	/**
	 * Getter
	 */
	public int getMaxCols() {
		return myMaxCols;
	}

	/**
	 * Initializes block based on state string
	 * 
	 * @param block
	 * @param initial
	 */
	private void initializeBlock(Block block, String initial) {
		Cell cell;
		cell = getCell(block, initial);
		block.setCell(cell);
		block.setNextCell(cell);
		Rectangle rect = new Rectangle(rectangleWidth, rectangleHeight);
		rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				block.getCell().cycleState();
				rect.setFill(block.getCell().getState().getColor());
			}
		});
		rect.setStroke(Color.BLACK);
		rect.setFill(cell.getState().getColor());
		GridPane.setConstraints(rect, (myMaxCols - myInitialCols) / 2 - myNewLeftCols + getJ(block),
				(myMaxRows - myInitialRows) / 2 - myNewTopRows + getI(block));
		myVisualGrid.getChildren().add(rect);
		block.setShape(rect);
	}

	/**
	 * gets a new cell based on initial conditions
	 * 
	 * @param block
	 * @param initial
	 * @return
	 */
	private Cell getCell(Block block, String initial) {
		Cell cell = null;
		if (simulationType.equals("conway")) {
			cell = new ConwayCell(block, new ConwayState(initial));
		} else if (simulationType.equals("fire")) {
			// change the fire prob chance to be read in instead of hard coded
			// :)
			cell = new FireCell(block, new FireState(initial), param1);
		} else if (simulationType.equals("wator")) {
			cell = new WaTorCell(block, new WaTorState(initial), param1, param2);
		} else if (simulationType.equals("segregation")) {
			cell = new SegregationCell(block, new SegregationState(initial), param1);
		} else {
			System.out.println("Invalid cell types");
		}
		return cell;
	}

	/**
	 * Connects neighbors
	 */
	private void connectGrid() {
		Block currentBlock;
		for (int i = 0; i < myGrid.size(); i++) {
			currentBlock = myGrid.get(i);
			for (int j = 0; j < adjacentCoordinates.length; j++) {
				if (j != 4) {
					if (gridType.equals("finite")) {
						connectFinite(currentBlock, i, j);
					} else if (gridType.equals("wrapped")) {
						connectWrapped(currentBlock, i, j);
					} else if (gridType.equals("infinite")) {
						connectInfinite(currentBlock, i, j);
					}
				}
			}
		}
	}

	/**
	 * Connects for finite grids
	 * 
	 * @param currentBlock
	 * @param i
	 * @param j
	 */
	private void connectFinite(Block currentBlock, int i, int j) {
		if (j != 4 && i + adjacentCoordinates[j] < myGrid.size() && i + adjacentCoordinates[j] >= 0
				&& !(i % myCols == 0 && j % 3 == 0) && !(i % myCols == myCols - 1 && j % 3 == 2)) {
			currentBlock.addAdjacent(myGrid.get(i + adjacentCoordinates[j]));
		}
	}

	/**
	 * Connects for wrapped grids
	 * 
	 * @param currentBlock
	 * @param i
	 * @param j
	 */
	private void connectWrapped(Block currentBlock, int i, int j) {
		int toAdd = i + adjacentCoordinates[j];
		toAdd += myGrid.size();
		toAdd %= myGrid.size(); // Deals with high and low indices (I'm clever)
		currentBlock.addAdjacent(myGrid.get(toAdd));
	}

	/**
	 * Connects for infinite grids
	 * 
	 * @param currentBlock
	 * @param i
	 * @param j
	 */
	private void connectInfinite(Block currentBlock, int i, int j) {
		if (j != 4 && i + adjacentCoordinates[j] < myGrid.size() && i + adjacentCoordinates[j] >= 0
				&& !(i % myCols == 0 && j % 3 == 0) && !(i % myCols == myCols - 1 && j % 3 == 2)) {
			currentBlock.addAdjacent(myGrid.get(i + adjacentCoordinates[j]));
		}
	}

	/**
	 * Getter
	 * 
	 * @return
	 */
	public int getMyRows() {
		return myRows;
	}

	/**
	 * Getter
	 * 
	 * @return
	 */
	public int getMyCols() {
		return myCols;
	}

	/**
	 * Implements Iterable
	 */
	@Override
	public Iterator<Block> iterator() {
		return myGrid.iterator();
	}

	/**
	 * returns a block based on i j conversion
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public Block getBlock(int i, int j) {
		if (i >= myRows || i < 0 || j >= myCols || j < 0) {
			return null;
		}
		return myGrid.get(i * myCols + j);
	}

	/**
	 * Unused but successfully adds column for the infinite grid
	 * 
	 * @param column
	 */
	@SuppressWarnings(value = { "unused" })
	private void addColumn(int column) {
		if (column != 0 && column != myCols || myCols >= myMaxCols || !gridType.equals("infinite")) {
			return;
		}
		if (column == 0) {
			myNewLeftCols++;
		}
		incrementColumns();
		for (int i = 0; i < myRows; i++) { // Blocks made
			int index = i * myCols + column;
			Block currentBlock = new SquareBlock(i, column);
			myGrid.add(index, currentBlock);
			initializeBlock(currentBlock, "empty");
		}
		int firstColumn = column;
		if (column != 0) {
			firstColumn -= 1;
		}
		for (int j = firstColumn; j < firstColumn + 1; j++) {
			for (int i = 0; i < myRows; i++) {
				Block currentBlock = getBlock(i, j);
				for (int k = 0; k < adjacentCoordinates.length; k++) {
					if (k != 4) {
						int index = i * myCols + j + adjacentCoordinates[k];
						if (index < 0 || index >= myGrid.size()) {
							continue;
						}
						currentBlock.addAdjacent(myGrid.get(index));
					}
				}
			}
		}
		// Update adjacent coordinates when
		// Reset rows
		// connectColumn(1-3);

	}

	/**
	 * Used in infinite grid to properly increase column value
	 */
	private void incrementColumns() {
		myCols++;
		for (int i = 0; i < adjacentCoordinates.length; i++) {
			if (i < adjacentCoordinates.length / 2) {
				adjacentCoordinates[i] -= 1;
			} else if (i == adjacentCoordinates.length / 2) {
			} else if (i > adjacentCoordinates.length / 2) {
				adjacentCoordinates[i] += 1;
			}
		}
	}

	/**
	 * Adds a row for the infinite grid. Not used but would be similar to
	 * addColumn
	 */
	@SuppressWarnings("unused")
	private void addRow() {

	}

	/**
	 * getter for the gridpane
	 */
	public GridPane getVisualGrid() {
		return myVisualGrid;
	}
}
