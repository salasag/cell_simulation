package cellsociety_team14;

import java.util.ArrayList;
import java.util.Collection;

import cell.State;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import structure.RectangularGrid;
/**
 * This class runs the animation of the simulations. It uses a Timeline and a step method to update cells.
 * @author Jack
 *
 */
public class Animation {

	private Control controlObj;
	private Timeline animation;
	private boolean running = false;
	private boolean paused = false;
	private Collection<Block> myEmpties;
	private int framesPassed;
	/**
	 *	Creates the animation that runs the scenes with a timeline 
	 */
	public Animation(Control obj){
		controlObj = obj;
		framesPassed = 0;
	}
	/**
	 *	Starts the animation with a set speed 
	 */
	public void startAnimation(double fps) {
		if(!running){
			running = true;
			animation = new Timeline();
			animation.setCycleCount(Timeline.INDEFINITE);
			double mildelay = 1000/fps;
			double sdelay = 1/fps;
			KeyFrame frame = new KeyFrame(Duration.millis(mildelay),
					e -> step(sdelay));
			animation.getKeyFrames().add(frame);
			animation.play();
		}
		if(paused){
			animation.play();
		}
	}
	/**
	 *	Pauses simulation 
	 */
	public void pause(){
		if(running){
			animation.pause();
		}
		paused = true;
	}
	/**
	 *	Increases/Decreases speed
	 *	Uses exponent to have a better range of speed
	 */
	public void changeSpeed(double fps){
		if(running){
			animation.pause();
			animation.setRate(Math.exp((fps-50)/16.66));
			animation.play();
		}
	}
	/**
	 *	Step for animation 
	 */
	public void step (double elapsedTime) {
		updateCells();
	}
	/**
	 *	The work behind step 
	 */
	public void updateCells(){
		updateEmpties();
		updateNextStates();
		updateBlocks();
		updateStates();
		updateColors();
		resetUpdated();
		updateGraph();
	}
	/**
	 * Updates graph by finding population totals
	 */
	private void updateGraph() {
		framesPassed++;
		RectangularGrid blocks = controlObj.getGrid();
		State[] states = blocks.getBlock(0,0).getCell().getStates();
		int[] frequencies = new int[states.length];
		for(Block block: blocks){
			State currentState = block.getCell().getState();
			for(int i = 0; i < states.length; i++){
				if(states[i].equals(currentState)){
					frequencies[i]++;
				}
			}
		}
		controlObj.addGraphData(framesPassed,frequencies);
	}
	/**
	 *	Gets the total of empties for certain simulations 
	 */
	private void updateEmpties() {
		Collection<Block> empties = new ArrayList<Block>();
		for(Block b : controlObj.getGrid()){
			if(b.isEmpty()){
				empties.add(b);
			}
		}
		myEmpties = empties;
	}
	/**
	 *	Has each cell play out its rules 
	 */
	public void updateNextStates(){
		for(Block b : controlObj.getGrid()){
			if(!b.isUpdated()){
				b.getCell().rules(myEmpties);
			}
		}
	}
	/**
	 *	Moves a cells state to the next one to update it 
	 */
	public void updateStates(){
		for(Block b : controlObj.getGrid()){
			b.getCell().updateCell();
		}
	}
	/**
	 *	Updates block pointers 
	 */
	public void updateBlocks(){
		for(Block b: controlObj.getGrid()){
			b.getCell().updateBlock();
		}
	}
	/**
	 *	Sets block color to the next color 
	 */
	public void updateColors(){
		for(Block b : controlObj.getGrid()){
			b.getShape().setFill(b.getCell().getState().getColor());
		}
	}
	/**
	 *	Resets a marker on the block 
	 */
	public void resetUpdated(){
		for(Block b: controlObj.getGrid()){
			b.setUpdated(false);
		}
	}
}
