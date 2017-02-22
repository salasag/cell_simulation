package cell;

import javafx.scene.paint.Color;

/**
 * States associated with WaTor Cell type
 * 
 * @author Andreas
 *
 */
public class WaTorState extends State {

	public static final String WATOR_PREDATOR = "predator";
	public static final String WATOR_PREY = "prey";
	public static final Color PREDATOR_COLOR = Color.RED;
	public static final Color PREY_COLOR = Color.YELLOW;

	public WaTorState(String n) {
		super(n);
	}

	/**
	 * return boolean if state is empty state
	 */
	public boolean isEmpty() {
		return this.equals(new WaTorState(EMPTY_STATE));
	}

	/**
	 * 
	 * @return boolean if state is predator type
	 */
	public boolean isPredator() {
		return this.equals(new WaTorState(WATOR_PREDATOR));
	}

	/**
	 * 
	 * @return boolean if state is prey type
	 */
	public boolean isPrey() {
		return this.equals(new WaTorState(WATOR_PREY));
	}

	@Override
	/**
	 * returns color associated with specific state
	 */
	public Color getColor() {
		if (this.equals(new WaTorState(WATOR_PREDATOR))) {
			return PREDATOR_COLOR;
		} else if (this.equals(new WaTorState(WATOR_PREY))) {
			return PREY_COLOR;
		} else {
			return State.EMPTY_COLOR;
		}
	}

}
