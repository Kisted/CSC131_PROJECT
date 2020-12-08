package servlets;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class APIHelper {
	
	public static String[] sanitizeEndPoint(URL url) {
		
		Path reqPath = Paths.get(url.getPath()).normalize();
		String reqPathString = reqPath.toString().replaceFirst("\\\\","");
		
		reqPathString = reqPathString.replace("winners", "1");
		reqPathString = reqPathString.replace("winner", "1");
		reqPathString = reqPathString.replace("losers", "0");
		
		reqPathString = reqPathString.replace("+"," ");
		
		String pathArray[] = reqPathString.split("\\\\");
		
		return pathArray;
	}

	public static String BuildSQLSelectStatement(String pathArray[]) {
		
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
	public static boolean isInt(String s) {
		
		try {
			Integer.parseInt(s);
		}catch(NumberFormatException e) {
			return false;
		}
		
		return true;
		
	}
	
	public static boolean isBinary(String s) {
		
		if (Integer.parseInt(s) == 0 || Integer.parseInt(s) == 1) {
			return true;
		}else {
			return false;
		}
	}
}
