package cellsociety_team14;

import java.util.Collection;
import cell.Cell;
import cell.State;
import javafx.scene.shape.Shape;

/**
 * Block generic to be extended. Allows for other shapes to be implemented
 * however we did not have time
 * 
 * @author Alex Salas
 */
public abstract class Block {
	public abstract void addAdjacent(Block adjacentBlock);

	public abstract Collection<Block> getAdjacent(State state, String adjCode);

	public abstract Collection<Block> getAdjacent();

	public abstract boolean isEmpty();

	public abstract boolean isNextMovable();

	public abstract boolean isAdjacent(Block block);

	public abstract void setCell(Cell cell);

	public abstract Cell getCell();

	public abstract void setNextCell(Cell cell);

	public abstract Cell getNextCell();

	public abstract int getI();

	public abstract int getJ();

	public abstract Shape getShape();

	public abstract void setShape(Shape shape);

	public abstract boolean isUpdated();

	public abstract void setUpdated(boolean b);
}
