package cell;

import javafx.scene.paint.Color;

/**
 * States Associated with a Conway Cell
 * 
 * @author Andreas
 *
 */
public class ConwayState extends State {

	public static final String CONWAY_ALIVE = "alive";
	public static final Color CONWAY_ALIVE_COLOR = Color.BLUE;

	public ConwayState(String n) {
		super(n);
	}

	@Override
	/**
	 * returns if states is empty state
	 */
	public boolean isEmpty() {
		return this.equals(new ConwayState(EMPTY_STATE));
	}

	/**
	 * 
	 * @return true if state is alive state, false otherwise
	 */
	public boolean isAlive() {
		return this.equals(new ConwayState(CONWAY_ALIVE));
	}

	/**
	 * returns the color for the given state
	 */
	public Color getColor() {
		if (this.equals(new ConwayState(CONWAY_ALIVE))) {
			return CONWAY_ALIVE_COLOR;
		} else {
			return State.EMPTY_COLOR;
		}
	}

}