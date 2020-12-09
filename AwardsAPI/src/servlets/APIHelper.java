package servlets;

//imports
import java.nio.file.Path;
import javax.servlet.http.*;

public class APIHelper {
	
	//helper method that takes a path object, removes redundancies, and provides filtering of elements to enable SQL logic
	public static String[] sanitizeEndPoint(Path uri) {
		
		//removes redundancies
		Path reqPath = uri.normalize();
		
		//remove leading '\'
		String reqPathString = reqPath.toString().replaceFirst("\\\\","");
		
		//replace certain phrases with boolean logic values
		reqPathString = reqPathString.replace("winners", "1");
		reqPathString = reqPathString.replace("winner", "1");
		reqPathString = reqPathString.replace("losers", "0");
		
		//any '+' symbols used will be replaced with spaces. The API spec states that spaces are not allowed in the URL, 
		//and should be replaced with '+' to ensure there is no need for special character encoding
		//this replaces the plus signs to support the SQL queries which do depend on spaces
		reqPathString = reqPathString.replace("+"," ");
		
		//creates array with each index containing one element from the path
		String pathArray[] = reqPathString.split("\\\\");
		
		return pathArray;
	}
	
	//Default helper method to build our selectStatement
	public static String buildSQLSelectStatement(String pathArray[]) {
		
		//dummy variables to use in string logic
		String column1 = "year";
		String column2 = "category";
		String column3 = "winner";
		
		//counter variable to enable correct use of the 'and' SQL statement
		int count = 1;
		
		//initial select statement stub
		String selectStatement = "select * from awards where ";
		//iterates through each element in the path array
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
				selectStatement = selectStatement.concat("(" +column2 + " like '" + pathArray[i] + 
								"'" + " or " + column2 + " like '" + pathArray[i] + " (%)')");
			}
			//uses count variable to concatenate an 'and' between each string element added by the above logic
			if(count < pathArray.length) {
				selectStatement = selectStatement.concat(" and ");
				count++;
			}
		}
		//organizational statement appended to selectStatement to order our results
		selectStatement = selectStatement.concat(" ORDER BY year ASC, Entity ASC, category ASC FOR JSON AUTO");
		
		return selectStatement;
	}
	
	//Helper method when the servlet detects that a search query is being made
	public static String buildSearchSQLSelectStatement(HttpServletRequest req, HttpServletResponse resp) {
		
		// initial select Statement stub
		String stub = "select * from awards where ";
		String selectStatement = stub;
		
		//dummy variables to help implement string logic
		String column1 = "year";
		String column2 = "category";
		String column3 = "winner";
		
		//counter variable that assists in appending 'and' between appropriate string concatenations
		int count = 0;
		
		//Array to hold the responses of the HttpServlerRequest's 'getParameter' method
		String[] pathArray = new String[3];
		
		//fills the array if the parameter exists in the HttpServletRequest, otherwise initializes to null.
		pathArray[0] = req.getParameter(column1);
		pathArray[1] = req.getParameter(column2);
		pathArray[2] = req.getParameter(column3);
		
		//counts amount of non-null elements of the pathArray array
		for (int i = 0; i < pathArray.length; i++) {
			
			if(pathArray[i] != null) {
				count++;
			}
		}
		
		//same logic as the default method, but this time with a check to ensure the value isn't null before acting upon it
		//if the select string is unchanged, appends a search query that will ensure an empty set is returned
		for (int i = 0; i< pathArray.length; i++) {
			
			if(pathArray[i] != null) {
				
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
					selectStatement = selectStatement.concat("(" +column2 + " like '" + pathArray[i] + 
									"'" + " or " + column2 + " like '" + pathArray[i] + " (%)')");
				}
				
				//logic for appending 'and' between each string concatenation
				if(count > 1) {
					selectStatement = selectStatement.concat(" and ");
					count--;
				}
				
				//checks to see if selectStatement is unchanged
			} 
		}
		
		if (selectStatement.equals(stub)) {
			
			selectStatement = selectStatement.concat("year = -1" );
			
		}
		
		selectStatement = selectStatement.concat(" ORDER BY year ASC, Entity ASC, category ASC FOR JSON AUTO ");
		
		
		return selectStatement;
	}
	
	//checks if a string can be cast to an integer
	public static boolean isInt(String s) {
		
		//tries to use Integer Object's parseInt method. If it succeeds, the value is thrown away.
		try {
			Integer.parseInt(s);
			
		//if parseInt fails, it will throw the exception below, and the function will return false
		}catch(NumberFormatException e) {
			return false;
		}
		//otherwise, the function returns true
		return true;
		
	}
	//simple check to see if a string that is guaranteed to be Integer cast-able is a 0 or a 1
	public static boolean isBinary(String s) {
		
		if (Integer.parseInt(s) == 0 || Integer.parseInt(s) == 1) {
			return true;
		}else {
			return false;
		}
	}
}
