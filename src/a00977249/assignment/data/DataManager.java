/**
 * Project: COMP3613_A00977249Assignment02
 * File: DataManager.java
 * Date: Oct 14, 2016
 * Time: 9:45:29 PM
 */
package a00977249.assignment.data;

import java.util.ArrayList;
import java.util.Date;

import a00977249.assignment.data.product.Product;

/**
 * @author Siamak Pourian
 *
 * Stores an arraylist of the records in the remote table
 *
 * DataManager Class
 */
public class DataManager {
	
	private ArrayList<Product> productList;
	private ArrayList<String> sqlCollection;
	private ArrayList<Date> commandCreationTime;
 	
	/**
	 * Default constructor
	 */
	public DataManager() {}
	
	/**
	 * @return the sqlCollection
	 */
	public ArrayList<String> getSqlCollection() {
		return sqlCollection;
	}

	/**
	 * @param sqlCollection the sqlCollection to set
	 */
	public void setSqlCollection(ArrayList<String> sqlCollection) {
		this.sqlCollection = sqlCollection;
	}

	/**
	 * Adds the SQL command to the collection
	 * 
	 * @param sqlCommand the SLQ command to be added to the list
	 */
    public void addSQLCommand(String sqlCommand) {
    	sqlCollection.add(sqlCommand);
    }
	
	/**
	 * Adds a product to the list
	 * 
	 * @param product to be added to the list
	 */
	public void addProduct(Product product) {
		productList.add(product);
	}

	/**
	 * @return the productList
	 */
	public ArrayList<Product> getProductList() {
		return productList;
	}

	/**
	 * @param productList the productList to set
	 */
	public void setProductList(ArrayList<Product> productList) {
		this.productList = productList;
	}

	/**
	 * @return the commandCreationTime
	 */
	public ArrayList<Date> getCommandCreationTime() {
		return commandCreationTime;
	}
	
	/**
	 * @param commandCreationTime the commandCreationTime to set
	 */
	public void setCommandCreationTime(ArrayList<Date> commandCreationTime) {
		this.commandCreationTime = commandCreationTime;
	}

	/**
	 * Adds the date of creation to the list
	 * 
	 * @param date time of creation
	 */
	public void addDate(Date date) {
		commandCreationTime.add(date);
	}
}
