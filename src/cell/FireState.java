package cell;

import javafx.scene.paint.Color;

/**
 * States associated with a Fire Cell
 * 
 * @author Andreas
 *
 */
public class FireState extends State {

	public static final String BURNING_TREE = "treeBurning";
	public static final String TREE = "tree";
	public static final Color TREE_COLOR = Color.GREEN;
	public static final Color BURNING_TREE_COLOR = Color.RED;

	public FireState(String n) {
		super(n);
	}

	@Override
	/**
	 * checks if states is empty state
	 */
	public boolean isEmpty() {
		return this.equals(new FireState(EMPTY_STATE));
	}

	/**
	 * 
	 * @return boolean if state is Tree state
	 */
	public boolean isTree() {
		return this.equals(new FireState(TREE));
	}

	/**
	 * 
	 * @return boolean if state is Burning state
	 */
	public boolean isBurning() {
		return this.equals(new FireState(BURNING_TREE));
	}

	/**
	 * returns color associated with state
	 */
	public Color getColor() {
		if (this.equals(new FireState(BURNING_TREE))) {
			return BURNING_TREE_COLOR;
		} else if (this.equals(new FireState(TREE))) {
			return TREE_COLOR;
		} else {
			return State.EMPTY_COLOR;
		}
	}
}
