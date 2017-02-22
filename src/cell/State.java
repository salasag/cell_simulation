package cell;

import javafx.scene.paint.Color;

/**
 * Abstract state class
 * 
 * @author Andreas
 *
 */
public abstract class State {

	public static final String EMPTY_STATE = "empty";
	public static final Color EMPTY_COLOR = Color.WHITE;

	private String name;

	public abstract Color getColor();

	public State(String n) {
		this.name = n;
	}

	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	public abstract boolean isEmpty();

	@Override
	public boolean equals(Object s) {
		if (!(s instanceof State)) {
			return false;
		}
		return this.getName().equals(((State) s).getName());
	}

	@Override
	public String toString() {
		return this.name;
	}
}