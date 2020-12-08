package servlets;

//imports
import java.sql.*;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.*;
import java.nio.file.*;

public class AwardsAPIServlet extends HttpServlet {
	
	//HttpServlet is serializable, assign UID
	private static final long serialVersionUID= 1L;				
	
	//servlet run by server when a http-style 'get' request is initiated
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
	
		//PrintWriter is used to display the JSON to the web page
		PrintWriter out = resp.getWriter();
		
		//Login information for the SQL database
		String username = "SA";
		String password = "<Echo135Delta>";
		String dataBaseURL = "jdbc:sqlserver://bristed.com:1401;databaseName=awards";
		Statement statement = null;
		Connection connection = null;
			
		try {
			//Grabs path from the HttpServletRequest; contains everything after the host and port
			Path reqPath = Paths.get(req.getRequestURI());
			
			//cleans the path (removes redundancies) and splits it into an array based on the backslash between each term
			String[] pathArray = APIHelper.sanitizeEndPoint(reqPath);
			
			//establishes connection to the database, sets auto-commit mode to false
			connection = DriverManager.getConnection(dataBaseURL, username, password);
			connection.setAutoCommit(false);
			
			//selectStatement is the string that will eventually be passed to the SQL server as a query
			String selectStatement;
			
			//Logic to allow for search functionality
			//if the path only has one element, and therefore the array made from the path likewise has one element, we check to 
			//see if the element begins with 'search'. If it does, we use a different helper method to build our select Statement.
			if(pathArray.length ==1 && pathArray[0].startsWith("search")) {
				
				selectStatement = APIHelper.BuildSearchSQLSelectStatement(req, resp);
			
			//if the path array has more than one element or it does not begin with the string 'search', we use the default helper method.
			}else {
				
			 selectStatement = APIHelper.BuildSQLSelectStatement(pathArray);
			
			}
			//prepares to execute the select statement on the SQL server
			statement = connection.createStatement();
			
			//executes selectStatement, and returns the result of that query into a ResultSet object. 
			//Note that this ResultSet will always contain one 'object'; a JSON string of varying lengths.
			ResultSet rs = statement.executeQuery(selectStatement);
			
			//Initialize blank result string
			String resultString ="";
			
			//while the ResultSet object has rows left, it will concatenate each row of the JSON string to a complete resultString
			while(rs.next()) {
			
			resultString = resultString.concat(rs.getObject(1).toString());

			}
			
			//if there were no rows, which is the case when the selectStatement returns nothing from the SQL server, 
			//resultString then has brackets appended to the empty string to ensure valid JSON format
			if (resultString.equals("")) {
				
				resultString = resultString + "[]";
			}
			
			//The SQL server only returns JSON Arrays, this logic allows us to either keep it as an array and print it using the JSONArray class
			//or to convert it to a single JSONObject and print it using the JSONObject class
			JSONArray jsonResultArray = new JSONArray(resultString);
			if(jsonResultArray.length()  == 1) {
				
				JSONObject jsonResultObject = new JSONObject(resultString.replace("]", "").replace("[", ""));
				out.println(jsonResultObject.toString(4));
				
			}else {
				
				out.println(jsonResultArray.toString(4));
				
			}
			
		//catches the SQL exceptions, uncomment 'out' to provide stacktrace on the webpage	
		} catch (SQLException e) {
			//TODO Auto-generated catch block
			e.printStackTrace(/*out*/);
			
		}
		
	}
	
}
