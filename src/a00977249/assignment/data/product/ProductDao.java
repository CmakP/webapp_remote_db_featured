/**
 * Project: COMP3613_A00977249Assignment02
 * File: DataBaseConnection.java
 * Date: Oct 12, 2016
 * Time: 8:04:58 PM
 */
package a00977249.assignment.data.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Siamak Pourian
 *
 * Handles the CRUD statements
 * 
 * DataBaseConnection Class
 */
public class ProductDao {
	
	public static final String TABLE_NAME = "A00977249_Products";
	
	private Statement stmt = null;
	
	private int size;

	/**
	 * Overloaded constructor
	 * 
	 * @param connection to the remote db
	 * @throws SQLException 
	 */
	public ProductDao(Statement stmt) throws SQLException {
		this.stmt = stmt;
	}

	/**
	 * Loads the table from the remote db
	 * 
	 * @param list data manager reference
	 * @throws SQLException
	 */
	public void loadData(ArrayList<Product> list) throws SQLException {
		ResultSet results = null;
		Product product = null;
		results = stmt.executeQuery(String.format("SELECT * FROM %s", TABLE_NAME));
		
		while (results.next()) {
		    product = new Product();   
		    product.setId(results.getInt("Id"));
		    product.setName(results.getString("Name"));
		    product.setPrice(results.getDouble("Price"));
		    product.setWeight(results.getDouble("Weight"));
		    product.setCode(results.getInt("Code"));
		    product.setManufacturer(results.getString("Manufacturer"));
			product.setMadeIn(results.getString("MadeIn"));
			product.setDescription(results.getString("Description"));
			list.add(product);
		}
	}
	
	/**
	 * Inserts a new row to the remote db
	 * 
	 * @param product record to be added to the db
	 * @throws SQLException
	 * @return SQL command
	 */
	public String insert(Product product) throws SQLException {
	    String strInsert = String.format("INSERT INTO %s VALUES('%s', %.2f, %.2f, %d, '%s', '%s', '%s')", TABLE_NAME,
				           product.getName(), product.getPrice(), product.getWeight(), product.getCode(), product.getManufacturer(), product.getMadeIn(), product.getDescription());
	    stmt.executeUpdate(strInsert);
	    return strInsert;
	}
	
	/**
	 * Deletes the specified row from db
	 * 
	 * @param id of the row which is going to be deleted
	 * @throws SQLException 
 	 * @return SQL command
	 */
	public String delete(int id) throws SQLException {
		String strDelete = String.format("DELETE from %s WHERE Id=%d ", TABLE_NAME, id);
		stmt.executeUpdate(strDelete);
		return strDelete;
	}
	
	/**
	 * Updates the product in the db
	 * 
	 * @param product to be updated
	 * @throws SQLException 
	 * @return SQL command
	 */
	public String update(Product product) throws SQLException {
		String strUpdate = String.format("UPDATE %s SET Name='%s', Price=%.2f, Weight=%.2f, Code=%d, MadeIn='%s', Manufacturer='%s', Description='%s' WHERE Id=%d", TABLE_NAME,
				           product.getName(), product.getPrice(), product.getWeight(), product.getCode(), product.getMadeIn(), product.getManufacturer(), product.getDescription(), product.getId());
		stmt.executeUpdate(strUpdate);	
		return strUpdate;
	}
	
	/**
	 * @return the size
	 * @throws SQLException 
	 */
	public int getSize() throws SQLException {
		ResultSet results = null;
		results = stmt.executeQuery(String.format("SELECT * FROM %s", TABLE_NAME));
		results.last();
		size = results.getRow();
		results.beforeFirst();
	
		return size;
	}

	/**
	 * Checks the table for code duplication
	 * 
	 * @param code to be verified
	 * @return true if the code is a duplicate and false otherwise
	 * @throws SQLException
	 */
	public boolean validateCode(int code) throws SQLException {
		ResultSet results = null;
		results = stmt.executeQuery(String.format("SELECT * FROM %s WHERE Code=%d", TABLE_NAME, code));
		return results.next();
	}
}
