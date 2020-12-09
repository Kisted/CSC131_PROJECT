package initialization;

//Insert data from CSV file to database scanner code

import java.io.*;
import java.sql.*;

import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseBool;
//import org.supercsv.cellprocessor.Optional;
//import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;



public class ImportCSV {
	
public static void main(String[] args) {
 
	String username = "SA";
	String password = "<Echo135Delta>";
	String dataBaseURL = "jdbc:sqlserver://bristed.com:1401;databaseName=awards";
	 
	
	String csvFilePath = "./lib/datahubio_oscar_data_csv.csv";
	
	
	
	Connection connection = null;
	
	ICsvBeanReader beanReader = null;
	CellProcessor[] processor = new CellProcessor[] {
	new ParseInt(), // Year
	new NotNull(), // Category
	new ParseBool(), // Winner
	new NotNull(), // Entity

	};

	try {

		connection = DriverManager.getConnection(dataBaseURL, username, password);
		connection.setAutoCommit(false);
		
		String insert = "INSERT INTO awards (year, category, winner, entity) VALUES(?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(insert);
		
		beanReader = new CsvBeanReader(new FileReader(csvFilePath), CsvPreference.STANDARD_PREFERENCE);
		
		beanReader.getHeader(true); // header line skipped
		
		String[] header = {"year", "category", "winner", "entity"};
		Awards bean = null;
		
		int count = 0;
		int size = 20;
		
		while ((bean = beanReader.read(Awards.class, header, processor)) != null) {
			
			
			int year = bean.getYear();
			String category = bean.getCategory();
			boolean winner = bean.getWinner();
			String entity = bean.getEntity();

			statement.setInt(1, year);
			statement.setString(2, category);
			statement.setBoolean(3, winner);
			statement.setString(4, entity);

			statement.addBatch();
			count++;

			if (count % size == 0) {
				
				statement.executeBatch();
				System.out.println(count/20);
			}
		}

		beanReader.close();

		// run remaining queries
		statement.executeBatch();
	
		connection.commit();
		connection.close();
	
	   } catch (IOException ex) {
		   System.err.println(ex);
	   } catch (SQLException ex) {
		   ex.printStackTrace();
	
	       try {
	           connection.rollback();
	       } catch (SQLException e) {
	           e.printStackTrace();
	       }
	   }
	}
}
