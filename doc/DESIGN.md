Group 14- DESIGN

[https://coursework.cs.duke.edu/CompSci308_2017Spring/cellsociety_team14](https://coursework.cs.duke.edu/CompSci308_2017Spring/cellsociety_team14)

Introduction

* Program that implements a Cellular Automata

    * Is compatible with any set of rules specified

* Graphics implementation

* Management 

    * (e.g. keeps track of cells)

* Cells 

    * have state and NextState 

        * also keep a pointer to myBlock and myNextBlock

        * carry other information relevant to specific simulation

* Update states continuously

  * Animation class runs a Timeline and updates each cell
  
  * Do this by updating NextStates and NextBlocks all at once

  * Then update CurrentStates and CurrentBlocks using 'next' information

* Allow user to manage the game parameters and rules from XML files

  * Read in the type of simulation, initial state, and any relevant parameters


Overview

<table>
  <tr>
    <td>Class</td>
    <td>Methods</td>
  </tr>
  <tr>
    <td>Abstract Cell(Block b, State s)
    ConwayCell(Block b, State s)
    FireCell(Block b, State s, int probCatch)
    WaTorCell(Block b, State s, int ageToReproduce, int BaseEnergy)
    SegregationCell(Block b, State s, int happyThreshold)
    has myBlock, myNextBlock, myCell, myNextCell
</td>
    <td>SetState()
getState()
setNextState
getNextState()
setBlock()
getBlock()
setNextBlock()
getNextBlock()
countNeighbors()
abstract rules()
updateCell()
updateBlock()
moveCell()
</td>
  </tr>
  <tr>
    <td>Abstract Block(int i, int j)
    has myAdjacent, myCell, myNextCell, myRectangle, myI, myJ</td>
    <td>getAdjacents()
    isEmpty()
    setCell()
    getCell()
    setNextCell()
    getNextCell()
    getI()
    getJ()
    isUpdated()
</td>
  </tr>
  <tr>
    <td>Abstract State(String name)</td>
    <td> isEmpty(), isPredator(), etc.
    getColor()
    </td>
  </tr>
  <tr>
    <td>GridBuilder(int rows, int columns)</td>
    <td>buildMenu()
buildGrid(rows, columns, shape)
populateCells();
updateCells();
displayCells();</td>
  </tr>
  <tr>
    <td>FileReader()</td>
    <td>readFile()
     fileChooser() 
     showError()</td>
  </tr>
  <tr>
    <td>Main()</td>
    <td></td>
  </tr>
</table>


Each cell will be instantiated as a cell object that holds pointers to a current and next Block, a state, and a next state. We think it’s better to have each cell as an object that can move around and is independent from a location on the grid because that way it’s easier to keep track of individual attributes nonspecific to location. It’s necessary to have a nextState private object in addition to the currentState so that you can calculate the update for the grid cell by cell and then update the grid all at once.

Each type of cell can implement its own rules based on its states, blocks, and neighbors, calling rules allows a cell to update its next cell and next block, along with any other necessary information associated with the cell. Then another 'control' class can call updateCells which makes cells update their current blocks and states based on the values of their next cells and next states. Once blocks are updated as well with pointers to their current cells, they can update the color of their associated shapes based on their current cell's state (which can return the associated color).

We have a separate GridBuilder class so that the grid can be built with any specifications of size and shape, and then populated with cell objects. The cell objects will be kept in a private ArrayList. 

User Interface

* Allow the user to choose the simulation type and parameters (from XML) including: initial state, simulation speed, grid size
  and buttons including start, stop, and step in an overlay that borders the main graphical display.

* Main graphical interface 

* Customizable grid layout

Design Details

* Graphics/UI package "GridBuilder"

    * Builds the GUI 

    * Grid- sets up empty grid for the CA

        * Size_x

        * Size_y

        * Shape

    * Controls

        * Methods to update cells, display cells, populate cells

        * Build menus, buttons/controls etc.

* Game Management "Rules"

    * Cell and State will be superclasses, with each different CA simulation (e.g. Conways Game of Life, WATOR, Segregation, etc) being subclasses. Based on the info they hold about themselves, they can specify when a cell should update it’s nextState, and what the nextState should update to.

    * Will have a method that returns a state that a cell’s next state should be, based the rules for that specific game.

* "Cell"

    * This class represents a single "cell" in a CA game.

    * Each cell has current and next Block pointers, a current state, and a nextState

    * From a cell, you can get and set its current and next state, and position

    * There will also be a method that counts the number of neighboring cells of a certain state (to help implement rules)

    * Cells should carry any other relevant information about themselves (e.g. number of cycles a cell has been alive).

* "State"

    * Specifies the different states that a cell can be in 

    * State should have a String name, and Color c (which is how the state will be represented on the grid)

* "Main"

    * Initializes the grid builder

* "FileReader"

    * Reads file into a data structure called Control that contains the initial state and every relevant paramtere

* "Grid"
	* Encapulated container of cells
	* Is heavily connected with the GUI to change the states of the cells

* "SquareBlock"
	
	* Holds a cell and rectangle and keeps pointers to ```myAdjacents```. Never moves so ```myAdjacents``` is relatively constant

* "SimulationBuilder"
	
	* Creates the main GUI and listens for user actions to change the scene

Use Cases:

* Apply the rule to a middle cell

    * if(cell.getNumberOfNeighbors(state Alive) > 3 || cell.getNumberOfNeighbors(state Alive < 2){

		cell.setNextState(state Dead)

	}

* Apply the rule to an edge cell

    * if(cell.getNumberOfNeighbors(state Alive) > 3 || cell.getNumberOfNeighbors(state Alive < 2){

		cell.setNextState(state dead)

	}

* Move to the next generation

    * GridBuilder.updateCells();

    * GridBuilder.displayCells();

* Set a simulation parameter

    * Cell fireCell = new FireCell(Block b, State s, int probCatch);

    * int probCatch = FileReader.getNum();

    * fireRule.setProbCatch(probCatch);

* Switch simulations

    * SplashScreen.resetSimulation(myStage);

    * GridBuilder.buildGrid(Wator);

    * GridBuilder.populateCells();

    * GridBuilder.displayCells();

Design Considerations

* What kind of information should be stored by the cells or able to be calculated by the cells?

    * The number and neighbors and state of the neighbors can be calculated, so it isn’t necessary to store.

    * The current state can’t be the only information stored, because if a cell’s state is changed as you’re checking the grid it will influence whether or not the later cells need to be changed. As a result the cells will store current state and next state (as well as current Block and nextBlock) and then update at appropriate times.

* What data structure to use to keep track of the cells in the grid?

    * Depends on whether we want to dynamically move the cell objects to new locations in the grid, or leave the cells in one location and simply change the state of the cell as necessary.

* How do we want the project displayed

    * The user could change things during the simulation or alternatively we could have them change it in a menu.

    * Do we want the simulation displayed 

Team Responsibilities

* Reading files into Data Structures- Jack

* Rule Implementation- Andreas

* GUI/Data structures- Alex

