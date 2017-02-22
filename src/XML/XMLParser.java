package XML;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class is an adapted version of a class that handles parsing XML files
 * and returning a completed object.
 * 
 * @author Jack Bloomfeld
 * @author Rhondu Smithwick
 * @author Robert C. Duvall
 */
public class XMLParser {
	public static final String TYPE_ATTRIBUTE = "type";

	private static final DocumentBuilder DOCUMENT_BUILDER = getDocumentBuilder();

	/**
	 * This method is passed an XML file and extracts the data, or returns an
	 * error if the data has a problem
	 * 
	 * @param dataFile
	 * @return
	 */
	public FileData getFileData(File dataFile) {
		Element root = getRootElement(dataFile);
		if (!isValidFile(root, FileData.DATA_TYPE)) {
			throw new XMLException("XML file does not represent %s", FileData.DATA_TYPE);
		}
		Map<String, String> results = new HashMap<>();
		for (String field : FileData.DATA_FIELDS) {
			results.put(field, getTextValue(root, field));
		}
		checkCompatibility(results);
		String formattedInitialState = createInitialState(results);
		results.put("initialstate", formattedInitialState);
		return new FileData(results);
	}

	/**
	 * This method returns a specific element from an XML file
	 * 
	 * @param xmlFile
	 * @return
	 */
	private Element getRootElement(File xmlFile) {
		try {
			DOCUMENT_BUILDER.reset();
			Document xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
			return xmlDocument.getDocumentElement();
		} catch (SAXException | IOException e) {
			throw new XMLException(e);
		}
	}

	/**
	 * this method ensures the file is valid
	 * 
	 * @param root
	 * @param type
	 * @return
	 */
	private boolean isValidFile(Element root, String type) {
		return getAttribute(root, TYPE_ATTRIBUTE).equals(type);
	}

	/**
	 * this method returns a specific attribute from an element
	 * 
	 * @param e
	 * @param attributeName
	 * @return
	 */
	private String getAttribute(Element e, String attributeName) {
		return e.getAttribute(attributeName);
	}

	/**
	 * this method gets the value of an element as a String
	 * 
	 * @param e
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element e, String tagName) {
		NodeList nodeList = e.getElementsByTagName(tagName);
		if (nodeList != null && nodeList.getLength() > 0) {
			return nodeList.item(0).getTextContent();
		} else {
			throw new XMLException("No input value for %s", tagName);
		}
	}

	/**
	 * This method checks the data in the XML file to make sure it is valid for
	 * a simulation
	 * 
	 * @param inputMap
	 */
	private void checkCompatibility(Map<String, String> inputMap) {
		checkParameterValues(inputMap);
		checkInitialState(inputMap);
	}

	/**
	 * Checks the parameter values for validity
	 * 
	 * @param inputMap
	 */
	public void checkParameterValues(Map<String, String> inputMap) {
		int speed = Integer.parseInt(inputMap.get("speed"));
		if (speed <= 0 | speed >= 100) {
			throw new XMLException("Invalid speed value");
		}
		int rows = Integer.parseInt(inputMap.get("gridrows"));
		if (rows <= 0 | rows >= 200) {
			throw new XMLException("Invalid rows value");
		}
		int cols = Integer.parseInt(inputMap.get("gridcolumns"));
		if (cols <= 0 | cols >= 200) {
			throw new XMLException("Invalid columns value");
		}
		String edge = inputMap.get("edgetype");
		if (!edge.equals("wrapped") && !edge.equals("finite")) {
			throw new XMLException("Invalid edge setting");
		}
		String gridoutline = inputMap.get("gridoutlined");
		if (!gridoutline.equals("true") && !gridoutline.equals("false")) {
			throw new XMLException("Invalid gridoutline setting");
		}
	}

	/**
	 * checks the formatting of the initial state input for validity
	 * 
	 * @param inputMap
	 */
	public void checkInitialState(Map<String, String> inputMap) {
		int rows = Integer.parseInt(inputMap.get("gridrows"));
		int cols = Integer.parseInt(inputMap.get("gridcolumns"));
		String inp = inputMap.get("initialstate");
		String temp;
		if (inp.contains("(")) {
			for (int i = 1; i < rows + 1; i++) {
				for (int j = 1; j < cols + 1; j++) {
					temp = "(" + i + "," + j + ",";
					if (inp.contains(temp)) {
						if (i < 1 || i > rows) {
							throw new XMLException("Invalid initial state x coordinate");
						}
						if (j < 1 || j > cols) {
							throw new XMLException("Invalid initial state y coordinate");
						}
					}
				}
			}

		} else if (inp.contains("number of state 1 cells:")) {
			String[] lines = inp.split("\\n");
			String[] tokens = lines[0].split(":");
			int num1states = Integer.parseInt(tokens[1]);
			int num2states = 0;
			if (lines.length > 1) {
				String[] tokens2 = lines[1].split(":");
				num2states = Integer.parseInt(tokens2[1]);
			}
			if (num1states + num2states > rows * cols) {
				throw new XMLException("Invalid number of initial states");
			}
		} else {
			inp = inp.replaceAll("(\\r|\\n|\\s)", "");
			if (rows * cols != inp.length()) {
				System.out.println("Dimensions and initial state incompatible");
				throw new XMLException("Grid dimensions and initial state are not compatible");
			}
		}
	}

	/**
	 * Turns the initial state data into the correct data structure
	 * 
	 * @param inputMap
	 * @return
	 */
	private String createInitialState(Map<String, String> inputMap) {
		String inp = inputMap.get("initialstate");
		int rows = Integer.parseInt(inputMap.get("gridrows"));
		int cols = Integer.parseInt(inputMap.get("gridcolumns"));
		int index;
		String temp;
		StringBuilder stateString = new StringBuilder();
		if (inp.contains("|")) {
			String[] tokens = inp.split("|");
			for (String str : tokens) {
				String[] nums = str.split(",");
				for (String s : nums) {
					System.out.println(s);
				}
			}
		}
		if (inp.contains("(")) {
			for (int i = 1; i < rows + 1; i++) {
				for (int j = 1; j < cols + 1; j++) {
					temp = "(" + i + "," + j + ",";
					if (inp.contains(temp)) {
						index = inp.indexOf(temp) + temp.length();
						stateString.append(inp.substring(index, index + 1));
					} else {
						stateString.append("0");
					}
				}
			}
			temp = stateString.toString();
		} else if (inp.contains("number of state 1 cells:")) {
			String[] lines = inp.split("\\n");
			String[] tokens = lines[0].split(":");
			int num1states = Integer.parseInt(tokens[1]);
			int num2states = 0;
			if (lines.length > 1) {
				String[] tokens2 = lines[1].split(":");
				num2states = Integer.parseInt(tokens2[1]);
			}
			ArrayList<String> cellSet = new ArrayList<String>();
			for (int i = 0; i < num1states; i++) {
				cellSet.add("1");
			}
			for (int i = 0; i < num2states; i++) {
				cellSet.add("2");
			}
			for (int i = 0; i < rows * cols - num1states - num2states; i++) {
				cellSet.add("0");
			}
			Random rand = new Random();
			for (int i = 1; i < rows + 1; i++) {
				for (int j = 1; j < cols + 1; j++) {
					index = rand.nextInt(cellSet.size());
					stateString.append(cellSet.get(index));
					cellSet.remove(cellSet.get(index));
				}
			}
			temp = stateString.toString();
		} else {
			stateString.append(inputMap.get("initialstate"));
			temp = stateString.toString();
			temp = temp.replaceAll("(\\r|\\n|\\s)", "");
		}
		return temp;
	}

	/**
	 * creates a new document builder object
	 * 
	 * @return
	 */
	private static DocumentBuilder getDocumentBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XMLException(e);
		}
	}
}
