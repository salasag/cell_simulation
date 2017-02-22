package cell;

import java.util.Collection;

import java.util.Random;

import cellsociety_team14.Block;

/**
 * Conway type Cell class
 * 
 * @author Andreas
 *
 */
public class FireCell extends Cell {

	private double probCatch;
	private static final FireState burning = new FireState(FireState.BURNING_TREE);
	private static final FireState tree = new FireState(FireState.TREE);
	private static final State empty = new FireState(State.EMPTY_STATE);
	private static final State states[] = { empty, tree, burning };

	public FireCell(Block b, State s, int probability) {
		super(b, s);
		probCatch = probability / 100.0;
	}

	@Override
	/**
	 * calls rules for given states
	 */
	public void rules(Collection<Block> e) {
		if (this.getState().isEmpty()) {
			cellEmptyRules();
		}
		if (((FireState) this.getState()).isTree()) {
			cellTreeRules();
		} else {
			cellBurningRules();
		}
	}

	/**
	 * calls rules for Tree states
	 */
	private void cellTreeRules() {
		int burningNeighbors = countNeighbors(burning, this.getBlock().getAdjacent(burning, "2457"));
		if (burningNeighbors > 0) {
			Random r = new Random();
			double result = r.nextDouble();

			if (result <= this.probCatch) {
				this.setNextState(new FireState(FireState.BURNING_TREE));
			} else {
				this.setNextState(this.getState());
			}
		} else {
			this.setNextState(this.getState());
		}
		this.setNextBlock(this.getBlock());
		this.getBlock().setNextCell(this);
	}

	/**
	 * calls rules for burning states
	 */
	private void cellBurningRules() {
		this.setNextState(new FireState(FireState.EMPTY_STATE));
		this.setNextBlock(this.getBlock());
		this.getBlock().setNextCell(this);
	}

	@Override
	/**
	 * returns array of possible states for this simulation
	 */
	public State[] getStates() {
		return states;
	}

}