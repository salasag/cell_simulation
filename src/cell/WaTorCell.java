package cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import cellsociety_team14.Block;

/**
 * WaTor cell type class
 * 
 * @author Andreas
 *
 */
public class WaTorCell extends Cell {

	private int baseEnergy;
	private int energy;
	private int age;
	private int ageToReproduce;
	private Random r = new Random();
	private static final WaTorState predator = new WaTorState(WaTorState.WATOR_PREDATOR);
	private static final WaTorState prey = new WaTorState(WaTorState.WATOR_PREY);
	private static final State empty = new WaTorState(State.EMPTY_STATE);
	private static final State states[] = { empty, prey, predator };

	public WaTorCell(Block block, State s, int a, int e) {
		super(block, s);
		this.age = 0;
		this.energy = e;
		this.ageToReproduce = a;
		this.baseEnergy = e;
	}

	@Override
	/**
	 * implements rules for given state
	 */
	public void rules(Collection<Block> e) {
		if (((WaTorState) this.getState()).isPredator()) {
			cellPredatorRules();
		} else if (((WaTorState) this.getState()).isPrey()) {
			cellPreyRules(new WaTorState(WaTorState.WATOR_PREY));
		} else {
			cellEmptyRules();
		}
	}

	/**
	 * implements rules for prey type, passes State s of the type of state to
	 * reproduce if necessary
	 * 
	 * @param s
	 */
	private void cellPreyRules(State s) {
		if (!incrementAge(s)) {
			moveToOpen(new WaTorCell(this.getBlock(), new WaTorState(State.EMPTY_STATE), this.age, this.baseEnergy));
		}
	}

	/**
	 * increment age, and reproduce if necessary
	 * 
	 * @param s
	 * @return boolean if reproduction occurred
	 */
	private boolean incrementAge(State s) {
		this.age++;
		if (this.age >= this.ageToReproduce) {
			this.age = 0;
			reproduce(s);
			return true;
		}
		return false;
	}

	/**
	 * reproduces with offspring of type s reproduces by moving to open space
	 * and leaving behind offspring in old location
	 * 
	 * @param s
	 */
	private void reproduce(State s) {
		moveToOpen(new WaTorCell(this.getBlock(), s, this.ageToReproduce, this.baseEnergy));
	}

	/**
	 * rules for predator type
	 */
	private void cellPredatorRules() {
		if (this.energy <= 0) { // Issue with energy
			this.setNextState(new WaTorState(WaTorState.EMPTY_STATE));
			this.setNextBlock(this.getBlock());
			this.getBlock().setNextCell(this);
			return;
		}
		this.energy--;
		int food = this.getBlock().getAdjacent(prey, "2457").size();
		if (food > 0) {
			if (!incrementAge(new WaTorState(WaTorState.WATOR_PREDATOR))) {
				eat();
			}
		} else {
			cellPreyRules(new WaTorState(WaTorState.WATOR_PREDATOR));
		}
	}

	/**
	 * implements a predator eating prey
	 */
	private void eat() {
		this.energy = this.baseEnergy;
		ArrayList<Block> foodNeighbors = (ArrayList<Block>)this.getBlock().getAdjacent(prey, "2457");
		while (foodNeighbors.size() > 0) {
			int randomNeighbor = r.nextInt(foodNeighbors.size());
			Block b = this.getBlock();
			Block t = foodNeighbors.get(randomNeighbor);
			if (!t.getNextCell().getNextState().equals(predator)) {
				moveCell(this, b, t, new WaTorCell(this.getBlock(), new WaTorState(State.EMPTY_STATE),
						this.ageToReproduce, this.baseEnergy));
				return;
			}
			foodNeighbors.remove(t);
		}
		cellPreyRules(new WaTorState(WaTorState.WATOR_PREDATOR));
	}

	/**
	 * moves this to open adjacent cell and leaves behind Cell leaveBehind
	 * 
	 * @param leaveBehind
	 */
	private void moveToOpen(Cell leaveBehind) {
		ArrayList<Block> EmptyNeighbors = (ArrayList<Block>)this.getBlock().getAdjacent(empty, "2457");
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
