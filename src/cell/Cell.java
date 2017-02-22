package cell;

import java.util.Collection;

import cellsociety_team14.Block;

/**
 * Abstract Cell class
 * 
 * @author Andreas
 *
 */
public abstract class Cell {
	private Block myBlock;
	private Block myNextBlock;
	private State myState;
	private State myNextState;

	Cell(Block block, State s) {
		this.myState = s;
		myBlock = block;
		myNextBlock = block;
		myNextState = s;
	}

	/**
	 * 
	 * @return myState
	 */
	public State getState() {
		return this.myState;
	}

	/**
	 * 
	 * @return myNextState
	 */
	public State getNextState() {
		return this.myNextState;
	}

	/**
	 * 
	 * @param s
	 */
	protected void setState(State s) {
		this.myState = s;
	}

	/**
	 * 
	 * @param s
	 */
	protected void setNextState(State s) {
		this.myNextState = s;
	}

	/**
	 * Returns number of neighbors of specified state
	 * 
	 * @param s
	 * @return
	 */
	protected int countNeighbors(State s, Collection<Block> myAdj) {
		int ret = 0;
		for (Block b : myAdj) {
			if (b.getCell().getState().equals(s)) {
				ret++;
			}
		}
		return ret;
	}

	/**
	 * updates Cell by making myState equal to myNextState
	 */
	public void updateCell() {
		this.myState = this.myNextState;
	}

	/**
	 * updates block by updating cell and block pointers
	 */
	public void updateBlock() {
		this.myBlock.setCell(this.myBlock.getNextCell());
		this.myBlock = this.myNextBlock;
		this.myBlock.setCell(this.myBlock.getNextCell());
	}

	/**
	 * 
	 * @return myBlock
	 */
	public Block getBlock() {
		return this.myBlock;
	}

	/**
	 * 
	 * @param b
	 */
	protected void setBlock(Block b) {
		this.myBlock = b;
	}

	/**
	 * 
	 * @return myNextBlock
	 */
	protected Block getNextBlock() {
		return this.myNextBlock;
	}

	/**
	 * 
	 * @param b
	 */
	protected void setNextBlock(Block b) {
		this.myNextBlock = b;
	}

	/**
	 * Moves Cell c from Block b to Block t and leaves behind Cell leaveBehind
	 * in Block b
	 * 
	 * @param c
	 * @param b
	 * @param t
	 * @param leaveBehind
	 */
	public void moveCell(Cell c, Block b, Block t, Cell leaveBehind) {
		c.setNextBlock(t);
		b.setNextCell(leaveBehind);
		t.setNextCell(c);
		t.setUpdated(true);
	}

	/**
	 * Preserves cell's state and doesn't move cell
	 */
	public void cellEmptyRules() {
		this.setNextState(this.getState());
		this.setNextBlock(this.getBlock());
		this.getBlock().setNextCell(this);
	}

	/**
	 * Changes state of cell to next cell in cycle
	 */
	public void cycleState() {
		for (int i = 0; i < this.getStates().length; i++) {
			if (this.getStates()[i].equals(this.getState())) {
				this.setState(this.getStates()[(i + 1) % this.getStates().length]);
				return;
			}
		}
	}

	public abstract State[] getStates();

	public abstract void rules(Collection<Block> e);
}
