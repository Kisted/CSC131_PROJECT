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
import org.json.JSONArray;
import java.util.Iterator;

public class URLMovieServlet extends HttpServlet {

	public static void main(String[] args) {
		String username = "SA";
		String password = "<Echo135Delta>";
		String dataBaseURL = "jdbc:sqlserver://bristed.com:1401;databaseName=awards";
		Statement statement = null;
		Connection connection = null;
		
		
		
		
		//URL reqURL = new URL(req .getRequestURI());
		//URL reqURL;
		try {
			
			URL reqURL = new URL("http://localhost:8080/best+picture/1990/losers");
			
			String[] pathArray = sanitizeEndPoint(reqURL);
			
			connection = DriverManager.getConnection(dataBaseURL, username, password);
			connection.setAutoCommit(false);
			
			String selectStatement = BuildSQLSelectStatement(pathArray);
			System.out.println(selectStatement);
			
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(selectStatement);
			
			
			rs.next();
			
			String resultString = (rs.getObject(1).toString());
			resultString = formatJSON(resultString);
			System.out.println(resultString);
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		} catch (SQLException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();	
		}
			
	}
		
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	
	}
	
	private static String[] sanitizeEndPoint(URL url) {
		
		Path reqPath = Paths.get(url.getPath()).normalize();
		String reqPathString = reqPath.toString().replaceFirst("\\\\","");
		
		reqPathString = reqPathString.replace("winners", "1");
		reqPathString = reqPathString.replace("winner", "1");
		reqPathString = reqPathString.replace("losers", "0");
		
		reqPathString = reqPathString.replace("+"," ");
		
		String pathArray[] = reqPathString.split("\\\\");
		
		return pathArray;
	}

	private static String BuildSQLSelectStatement(String pathArray[]) {
		
		String column1 = "year";
		String column2 = "category";
		String column3 = "winner";
		int count = 1;
		
		String selectStatement = "select * from awards where ";
		
		for (int i = 0; i< pathArray.length; i++) {
			
			//check if pathArray[i] can be cast as an integer
			if(isInt(pathArray[i])) {
				
				
				//check if integer it is 0 or one
				if(isBinary(pathArray[i])) {
					selectStatement = selectStatement.concat(column3 + " = " + pathArray[i]);
					
				//if is not binary, lookup from year column	
				}else {
					selectStatement = selectStatement.concat(column1 + " = " + pathArray[i]);
				}
				//if it is not cast-able as an integer, lookup from 'category' column
			}else {
				selectStatement = selectStatement.concat(column2 + " like '" + pathArray[i] + "%'");
			}
			if(count < pathArray.length) {
				selectStatement = selectStatement.concat(" and ");
				count++;
			}
		}
		
		selectStatement = selectStatement.concat(" ORDER BY year ASC, Entity ASC, category ASC FOR JSON AUTO");
		
		return selectStatement;
	}
	private static boolean isInt(String s) {
		
		try {
			Integer.parseInt(s);
		}catch(NumberFormatException e) {
			return false;
		}
		
		return true;
		
	}
	
	private static boolean isBinary(String s) {
		
		if (Integer.parseInt(s) == 0 || Integer.parseInt(s) == 1) {
			return true;
		}else {
			return false;
		}
	}
	
	private static String formatJSON(String s) {
		
		s = s.replace("[{", "[{\n");
		s = s.replace("},{", "},\n{");
		s = s.replace(",\"", ",\n\"");
		s = s.replace("{", "{\n");
		s = s.replace("}", "\n}");
		s = s.replace("}]", "\n}]");
		
		return s;
		
	}
	
}
