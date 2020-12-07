package servlets;

import java.sql.*;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.file.*;
import java.net.*;
import java.io.*;
import org.json.JSONObject;
import java.util.Iterator;

public class URLMovieServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		String username = "SA";
		String password = "<Echo135Delta>";
		String dataBaseURL = "jdbc:sqlserver://bristed.com:1401;databaseName=awards";
		
		Statement statement = null;
		Connection connection = null;
		
		URL reqURL = new URL(req.getRequestURI());
		Path reqPath = Paths.get(reqURL.getPath()).normalize();
		String reqPathString = reqPath.toString();
		String pathArray[] = reqPathString.split("/");
		
		
		try {
			
			connection = DriverManager.getConnection(dataBaseURL, username, password);
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(BuildSQLSelectStatement(pathArray, connection));
			
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		
	
		
		
	}

	private String BuildSQLSelectStatement(String pathArray[], Connection connection) {
		
		String [] columnHeaderArray= new String[] { "year", "category", "winner", "entity"};
		
		String selectStatement = "SELECT * from awards where ";
		
		String columnName;
		
		int count = 0; 
		
		for(int i = 0; i < pathArray.length;i++) {
			
			columnName = null;
			
			for(int j = 0; j < columnHeaderArray.length; j++) {
				
				if (checkColumn(columnHeaderArray[j], pathArray[i], connection)) {
					
					columnName = columnHeaderArray[j];
				
				}
				
				if (columnName != null) {
					
					selectStatement.concat(columnName + " = " + pathArray[0]);
					
				}
				
				if (pathArray.length > 1 && count < pathArray.length-1) {
				
					selectStatement.concat(" and ");
					count ++;
					
				}
			}
		
		}
		
		return selectStatement+ " for json";
	}
	
	private boolean checkColumn(String column, String pathPiece, Connection connection) {
		
		String sqlTestColumn = "SELECT CASE WHEN EXISTS (SELECT 1 FROM [awards] WHERE " + column + " = ?) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END AS 'result column'";
		
		try {
			
			PreparedStatement statement = connection.prepareStatement(sqlTestColumn);
			statement.setString(1, pathPiece);
			ResultSet results = statement.executeQuery();
			
			return results.getBoolean(1);
			
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
}
