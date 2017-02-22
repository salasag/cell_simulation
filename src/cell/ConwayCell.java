package cell;

import java.util.Collection;

import cellsociety_team14.Block;

/**
 * Conway type Cell class
 * 
 * @author Andreas
 *
 */
public class ConwayCell extends Cell {

	private static final ConwayState alive = new ConwayState(ConwayState.CONWAY_ALIVE);
	private static final State empty = new ConwayState(State.EMPTY_STATE);
	private static final State states[] = { empty, alive };

	public ConwayCell(Block block, State s) {
		super(block, s);
	}

	@Override
	/**
	 * calls rules on different cell types
	 */
	public void rules(Collection<Block> e) {
		if (((ConwayState) this.getState()).isEmpty()) {
			cellDeadRules();
		} else {
			cellAliveRules();
		}
	}

	/**
	 * rules for cells in alive state
	 */
	private void cellAliveRules() {
		int liveNeighbors = countNeighbors(alive, this.getBlock().getAdjacent());
		if (liveNeighbors < 2 || liveNeighbors > 3) {
			this.setNextState(new ConwayState(ConwayState.EMPTY_STATE));
		} else {
			this.setNextState(this.getState());
		}
		this.setNextBlock(this.getBlock());
		this.getBlock().setNextCell(this);
	}

	/**
	 * rules for cells in dead state
	 */
	private void cellDeadRules() {
		int liveNeighbors = countNeighbors(alive, this.getBlock().getAdjacent());
		if (liveNeighbors == 3) {
			this.setNextState(new ConwayState(ConwayState.CONWAY_ALIVE));
		} else {
			this.setNextState(this.getState());
		}
		this.setNextBlock(this.getBlock());
		this.getBlock().setNextCell(this);
	}

	@Override
	/**
	 * returns array of states in this simulation
	 */
	public State[] getStates() {
		return states;
	}

}