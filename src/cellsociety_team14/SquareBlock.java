// This entire file is part of my masterpiece.
// Alex Salas
package cellsociety_team14;

import java.util.ArrayList;
import java.util.Collection;
import cell.Cell;
import cell.State;
import javafx.scene.shape.Shape;

/**
 * Block Class Alex Salas Connects to adjacent blocks in the grid. Contains a
 * cell object
 * @author Alex Salas
 */
public class SquareBlock extends Block {
	private ArrayList<Block> myAdjacent;
	private Cell myCell;
	private Cell myNextCell;
	private int myI;
	private int myJ;
	private Shape myRectangle;
	private boolean updated;

	/**
	 * Creates new squareblock at position
	 * 
	 * @param i
	 * @param j
	 */
	public SquareBlock(int i, int j) {
		myAdjacent = new ArrayList<Block>();
		myI = i;
		myJ = j;
	}
	/**
	 * Adds block to adjacents and corresponding sub adjacent group
	 * 
	 * @param adjacentBlock
	 */
	public void addAdjacent(Block adjacentBlock) {
		if (adjacentBlock == null || myAdjacent.contains(adjacentBlock)) {
			return;
		}
		myAdjacent.add(adjacentBlock);
	}
	/**
	 * Returns true if it is adjacent
	 */
	public boolean isAdjacent(Block block) {
		return myAdjacent.contains(block);
	}
	/**
	 * Returns true if empty
	 */
	public boolean isEmpty() {
		return (this.getCell().getState().isEmpty());
	}
	/**
	 * Returns true if valid movable position
	 */
	public boolean isNextMovable() {
		return (this.getNextCell().getState().isEmpty());
	}
	/**
	 * Gets Adjacent blocks based on a String code of ints
	 * This removes lots of unnecessary code elsewhere!
	 */
	public Collection<Block> getAdjacent(State state, String adjCode) {
		Collection<Block> states = new ArrayList<Block>();
		for(int i = 0; i < adjCode.length(); i++){
			int j;
			try{
				j = Integer.parseInt(""+adjCode.charAt(i));
			}
			catch(NumberFormatException e){
				continue;
			}
			if(j >= myAdjacent.size()){continue;}
			states.add(myAdjacent.get(j));
		}
		return states;
	}
	/**
	 * Getter
	 */
	public Collection<Block> getAdjacent() {
		return myAdjacent;
	}
	/**
	 * Setter
	 */
	public void setCell(Cell cell) {
		myCell = cell;
	}
	/**
	 * Getter
	 */
	public Cell getCell() {
		return myCell;
	}
	/**
	 * Setter
	 */
	public void setNextCell(Cell cell) {
		this.myNextCell = cell;
	}
	/**
	 * Getter
	 */
	public Cell getNextCell() {
		return this.myNextCell;
	}

	/**
	 * Getter
	 */
	public int getI() {
		return myI;
	}

	/**
	 * Getter
	 */
	public int getJ() {
		return myJ;
	}

	/**
	 * Getter
	 */
	@Override
	public Shape getShape() {
		return myRectangle;
	}

	/**
	 * Setter
	 */
	@Override
	public void setShape(Shape rect) {
		myRectangle = rect;
	}

	/**
	 * Returns true if updated
	 */
	public boolean isUpdated() {
		return this.updated;
	}

	/**
	 * Setter
	 */
	public void setUpdated(boolean b) {
		this.updated = b;
	}
}