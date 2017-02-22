package cell;

import javafx.scene.paint.Color;

/**
 * State class for states associated with Segregation Cell
 * 
 * @author Andreas
 *
 */
public class SegregationState extends State {

	public static final String SPECIES1 = "species1";
	public static final String SPECIES2 = "species2";
	public static final Color SPECIES1_COLOR = Color.RED;
	public static final Color SPECIES2_COLOR = Color.BLUE;

	public SegregationState(String n) {
		super(n);
	}

	/**
	 * returns boolean if state is empty state
	 */
	public boolean isEmpty() {
		return this.equals(new WaTorState(EMPTY_STATE));
	}

	/**
	 * 
	 * @return boolean if state is species 1 state
	 */
	public boolean isSpecies1() {
		return this.equals(new SegregationState(SPECIES1));
	}

	/**
	 * 
	 * @return boolean if state is species 2 state
	 */
	public boolean isSpecies2() {
		return this.equals(new SegregationState(SPECIES2));
	}

	@Override
	/**
	 * returns color associated with the given state
	 */
	public Color getColor() {
		if (this.equals(new SegregationState(SPECIES1))) {
			return SPECIES1_COLOR;
		} else if (this.equals(new SegregationState(SPECIES2))) {
			return SPECIES2_COLOR;
		} else {
			return State.EMPTY_COLOR;
		}
	}

}
