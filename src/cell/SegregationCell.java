package cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import cellsociety_team14.Block;

/**
 * Segregation type Cell Class
 * 
 * @author Andreas
 *
 */
public class SegregationCell extends Cell {
	private static final SegregationState species1 = new SegregationState(SegregationState.SPECIES1);
	private static final SegregationState species2 = new SegregationState(SegregationState.SPECIES2);
	private static final State empty = new SegregationState(State.EMPTY_STATE);
	private static final State states[] = { empty, species1, species2 };
	private double happyThresh;
	private Random r = new Random();
	private Collection<Block> empties;

	public SegregationCell(Block block, State s, int h) {
		super(block, s);
		this.happyThresh = h / 100.0;
	}

	@Override
	/**
	 * implements rules for given cell states
	 */
	public void rules(Collection<Block> e) {
		empties = e;
		if (this.getState().isEmpty()) {
			cellEmptyRules();
		} else {
			speciesRules(this.getState());
		}
	}

	/**
	 * implements the rules for the species of the given type
	 * 
	 * @param s
	 */
	private void speciesRules(State s) {
		double totNonEmpty = this.countNeighbors(species1, this.getBlock().getAdjacent())
				+ this.countNeighbors(species2, this.getBlock().getAdjacent());
		double likeNeighbors = this.countNeighbors(s, this.getBlock().getAdjacent());
		double ratio = likeNeighbors / totNonEmpty;
		if (ratio >= happyThresh) {
			cellEmptyRules();
			return;
		}
		moveToOpen(new SegregationCell(this.getBlock(), new SegregationState(SegregationState.EMPTY_STATE),
				(int) this.happyThresh * 100));
	}

	/**
	 * moves this cell to an open cell, and specifies what type of cell to leave
	 * behind
	 * 
	 * @param leaveBehind
	 */
	private void moveToOpen(Cell leaveBehind) {
		ArrayList<Block> EmptyNeighbors = (ArrayList<Block>) empties;
		while (EmptyNeighbors.size() > 0) {
			int choose = r.nextInt(EmptyNeighbors.size());
			Block b = this.getBlock();
			Block t = EmptyNeighbors.get(choose);
			if (t.getNextCell().getNextState().isEmpty()) {
				moveCell(this, b, t, leaveBehind);
				return;
			} else {
				EmptyNeighbors.remove(choose);
			}
		}
		cellEmptyRules();
	}

	@Override
	/**
	 * returns array of possible states for this simulation
	 */
	public State[] getStates() {
		return states;
	}
}