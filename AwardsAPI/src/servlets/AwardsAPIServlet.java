package servlets;

import java.sql.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.*;
import org.json.*;

public class AwardsAPIServlet extends HttpServlet {
	
	private static final long serialVersionUID= 1L;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	
		String username = "SA";
		String password = "<Echo135Delta>";
		String dataBaseURL = "jdbc:sqlserver://bristed.com:1401;databaseName=awards";
		Statement statement = null;
		Connection connection = null;
		
		try {
			
			URL reqURL = new URL(req.getRequestURI());
			
			String[] pathArray = APIHelper.sanitizeEndPoint(reqURL);
			
			connection = DriverManager.getConnection(dataBaseURL, username, password);
			connection.setAutoCommit(false);
			
			String selectStatement = APIHelper.BuildSQLSelectStatement(pathArray);
			//System.out.println(selectStatement);
			
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(selectStatement);
			
			
			rs.next();
			
			String resultString = (rs.getObject(1).toString());
			JSONArray jsonResultArray = new JSONArray(resultString);
			if(jsonResultArray.length()  == 1) {
				
				JSONObject jsonResultObject = new JSONObject(resultString.replace("]", "").replace("[", ""));
				System.out.println(jsonResultObject.toString(4));
				
			}else {
				
				System.out.println(jsonResultArray.toString(4));
				
			}
			
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		} catch (SQLException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();	
		}
			
	}
	
}
