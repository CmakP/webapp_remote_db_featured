/**
 * Project: COMP3613_A00977249Assignment02
 * File: HTMLManager.java
 * Date: Oct 15, 2016
 * Time: 2:17:50 PM
 */
package a00977249.assignment.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import a00977249.assignment.data.product.Product;

/**
 * @author Siamak Pourian
 *
 * Manages the generated result to be presented in the body section of 
 * the html/jsp page
 *  
 * HTMLManager Class
 */
public class HTMLManager {
	
	private StringBuffer outputData;
	
	public HTMLManager() {
		outputData = new StringBuffer();
	}
	
	/**
	 * Appends the attributes of the scoped variable to the current table row
	 * 
	 * @param product to be added to the current table row
	 * @param resourceBundle the corresponding language bundle for the presentation language
	 */
	public void addRowToOutputData(Product product) {
		synchronized(product) {
			outputData.append("<form class='center' action='CRUD' method='post' onSubmit='return submitIt(this)'>\n");
			outputData.append("<input type='hidden' name='update' value='update'>\n");
			outputData.append("<tr>\n");
			outputData.append("<td><input type='text' name='Id' size='3' value='" + product.getId() + "' readonly></td>\n");
			outputData.append("<td><input type='text' name='Name' value='" + product.getName() + "'></td>\n");
			outputData.append("<td><input type='text' name='Price' size='12' value='" + product.getPrice() +"'></td>\n");
			outputData.append("<td><input type='text' name='Weight' size='12' value='" + product.getWeight() + "'></td>\n");
			outputData.append("<td><input type='text' name='Code' size='12' value='" + product.getCode() + "'></td>\n");
			outputData.append("<td><input type='text' name='Manufacturer' size='12' value='" + product.getManufacturer() + "'></td>\n");
			outputData.append("<td><input type='text' name='MadeIn' size='12' value='" + product.getMadeIn() + "'></td>\n");
			outputData.append("<td><input type='text' name='Description' size='40' value='" + product.getDescription() + "'></td>\n");
			outputData.append("<td><input type='submit' value='Update' class='button buttonTb'></td>\n");
			outputData.append("</form>\n");
			outputData.append("<form class='center' action='CRUD' method='post'>\n");
			outputData.append("<td>\n");
			outputData.append("<input type='hidden' name='id' value=" + product.getId() + ">\n");
			outputData.append("<input type='hidden' name='delete' value='delete'>\n");
			outputData.append("<input type='submit' value='Delete' class='button buttonTb'>\n");
			outputData.append("</td>\n");
			outputData.append("</form>\n");
			outputData.append("</tr>\n");
		}
	}
	
	/**
	 * Closes the table tag and returns the HTML body
	 * 
	 * @return the completed buffer as HTML body
	 */
	public StringBuffer getOutputData() {
		return outputData;		  
	}
	
	/**
	 * Creates a table of SQL commands and corresponding execution date
	 * 
	 * @param sqlCommands SQL commands collection
	 * @param commandCreationTime date and time of the execution of each SQL command
	 * @param resourceBundle the corresponding language bundle for the presentation language
	 * @return an HTML table of SQL commands and execution date
	 */
	public StringBuffer getSQLCommandsTable(ArrayList<String> sqlCommands, ArrayList<Date> commandCreationTime, ResourceBundle resourceBundle) {
		if (sqlCommands != null && sqlCommands.size() > 0) {
			synchronized(sqlCommands) {
			    outputData.append("<CENTER><TABLE>\n");
		        outputData.append("<TR>\n");
		        outputData.append("  <TH>" + resourceBundle.getString("SQL_Command_Header") + "</TH>\n");
		        outputData.append("  <TH>" + resourceBundle.getString("Execution_Time_Header") + "</TH></TR>\n");
		        for(int i=0; i < sqlCommands.size(); i++) {
		          outputData.append("<TR><TD>" + sqlCommands.get(i) + "</TD>\n");
		          outputData.append("<TD>" + commandCreationTime.get(i) + "</TD></TR>\n");
		        }
		        outputData.append("</TABLE></CENTER>\n");
			    return outputData;
		   }
		} else {
			return outputData.append("<h2>" + resourceBundle.getString("No_CRUD_Commands") + "</h2>\n");
		}
	}
}
