package XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class is an adapted version of a data structure that contains values
 * read in from an XML file
 * 
 * @author Jack Bloomfeld
 * @author Robert C. Duvall
 */
public class FileData {
	public static final String DATA_TYPE = "FileData";
	public static final List<String> DATA_FIELDS = Arrays
			.asList(new String[] { "type", "param1", "param2", "title", "author", "gridrows", "gridcolumns",
					"gridshape", "edgetype", "gridoutlined", "cellcolor", "cellshape", "speed", "initialstate" });

	private Map<String, String> myDataValues;

	public FileData(Map<String, String> dataValues) {
		myDataValues = dataValues;
	}

	public String getSimulationType() {
		return myDataValues.get(DATA_FIELDS.get(0));
	}

	public int getParam1() {
		return Integer.parseInt(myDataValues.get(DATA_FIELDS.get(1)));
	}

	public int getParam2() {
		return Integer.parseInt(myDataValues.get(DATA_FIELDS.get(2)));
	}

	public String getTitle() {
		return myDataValues.get(DATA_FIELDS.get(3));
	}

	public String getAuthor() {
		return myDataValues.get(DATA_FIELDS.get(4));
	}

	public int getGridRows() {
		return Integer.parseInt(myDataValues.get(DATA_FIELDS.get(5)));
	}

	public int getGridColumns() {
		return Integer.parseInt(myDataValues.get(DATA_FIELDS.get(6)));
	}

	public String getGridShape() {
		return myDataValues.get(DATA_FIELDS.get(7));
	}

	public String getEdgeType() {
		return myDataValues.get(DATA_FIELDS.get(8));
	}

	public String getGridOutlined() {
		return myDataValues.get(DATA_FIELDS.get(9));
	}

	public String getCellColor() {
		return myDataValues.get(DATA_FIELDS.get(10));
	}

	public String getCellShape() {
		return myDataValues.get(DATA_FIELDS.get(11));
	}

	public int getSpeed() {
		return Integer.parseInt(myDataValues.get(DATA_FIELDS.get(12)));
	}

	/**
	 * This method formats the initial state data to make sure it is appropriate
	 * and then returns a collection
	 * 
	 * @return
	 */
	public Collection<Integer> getInitialState() {
		String config = myDataValues.get(DATA_FIELDS.get(13));
		config = config.replaceAll("(\\r|\\n|\\s)", "");
		Collection<Integer> cellStates = new ArrayList<Integer>();
		int temp;
		for (int i = 0; i < getGridRows() * getGridColumns(); i++) {
			temp = Integer.parseInt(config.substring(i, i + 1));
			if (temp < 0 | temp > 7) {
				throw new XMLException("Invalid state input");
			}
			cellStates.add(temp);
		}
		return cellStates;
	}

	/**
	 * this method overrides the toString method so that a FileData structure
	 * can be printed and print all data stored
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Specifications {\n");
		for (Map.Entry<String, String> e : myDataValues.entrySet()) {
			result.append("  " + e.getKey() + "='" + e.getValue() + "',\n");
		}
		result.append("}\n");
		return result.toString();
	}

}
