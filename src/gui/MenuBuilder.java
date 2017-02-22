package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Menubuilder creates the splash screen
 * 
 * @author Jack Bloomfeld
 */
public class MenuBuilder {
	/**
	 * This method takes in a string and returns an HBox with the appropriate
	 * label
	 * 
	 * @param labelText
	 * @return
	 */
	public HBox buildHBox(String labelText) {
		Label label1 = new Label(labelText);
		HBox hb = new HBox();
		hb.getChildren().addAll(label1);
		hb.setAlignment(Pos.CENTER);
		return hb;
	}

	/**
	 * This method takes in specifications for text and returns the appropriate
	 * text object
	 * 
	 * @param stringin
	 * @param colorin
	 * @param font
	 * @param fontsize
	 * @return
	 */
	public Text buildText(String stringin, Color colorin, String font, int fontsize) {
		final Text header = new Text();
		header.setText(stringin);
		header.setFill(colorin);
		header.setFont(Font.font(font, FontWeight.BOLD, fontsize));
		InnerShadow is = new InnerShadow();
		header.setEffect(is);
		return header;
	}
}