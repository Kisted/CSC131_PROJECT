import java.io.IOException;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

public class RESTApiClient {
	
	public static void displayCategories() {
		
		//print all award categories
		System.out.println("	-ACTOR");
		System.out.println("	-ACTOR IN A LEADING ROLE");
		System.out.println("	-ACTOR IN A SUPPORTING ROLE");
		System.out.println("	-ACTRESS");
		System.out.println("	-ACTRESS IN A LEADING ROLE");
		System.out.println("	-ACTRESS IN A SUPPORTING ROLE");
		System.out.println("	-ANIMATED FEATURE FILM");
		System.out.println("	-ART DIRECTION");
		System.out.println("	-ASSISTANT DIRECTOR");
		System.out.println("	-AWARD OF COMMENDATION");
		System.out.println("	-BEST MOTION PICTURE");
		System.out.println("	-BEST PICTURE");
		System.out.println("	-CINEMATOGRAPHY");
		System.out.println("	-COSTUME DESIGN");
		System.out.println("	-DANCE DIRECTION");
		System.out.println("	-DIRECTING");
		System.out.println("	-DOCUMENTARY");
		System.out.println("	-ENGINEERING EFFECTS");
		System.out.println("	-FILM EDITING");
		System.out.println("	-FOREIGN LANGUAGE FILM");
		System.out.println("	-GORDON E. SAWYER AWARD");
		System.out.println("	-HONORARY AWARD");
		System.out.println("	-HONORARY FOREIGN LANGUAGE FILM AWARD");
		System.out.println("	-IRVING G. THALBERG MEMORIAL AWARD");
		System.out.println("	-JEAN HERSHOLT HUMANITARIAN AWARD");
		System.out.println("	-JOHN A. BONNER MEDAL OF COMMENDATION");
		System.out.println("	-MAKEUP");
		System.out.println("	-MAKEUP AND HAIRSTYLING");
		System.out.println("	-MEDAL OF COMMENDATION");
		System.out.println("	-MUSIC");
		System.out.println("	-OUTSTANDING MOTION PICTURE");
		System.out.println("	-OUTSTANDING PICTURE");
		System.out.println("	-OUTSTANDING PRODUCTION");
		System.out.println("	-PRODUCTION DESIGN");
		System.out.println("	-SCIENTIFIC AND TECHNICAL AWARD");
		System.out.println("	-SHORT FILM");
		System.out.println("	-SHORT SUBJECT");
		System.out.println("	-SOUND");
		System.out.println("	-SOUND EDITING");
		System.out.println("	-SOUND EFFECTS");
		System.out.println("	-SOUND EFFECTS EDITING");
		System.out.println("	-SOUND MIXING");
		System.out.println("	-SOUND RECORDING");
		System.out.println("	-SPECIAL ACHIEVEMENT AWARD");
		System.out.println("	-SPECIAL AWARD");
		System.out.println("	-SPECIAL EFFECTS");
		System.out.println("	-SPECIAL FOREIGN LANGUAGE FILM AWARD");
		System.out.println("	-SPECIAL VISUAL EFFECTS");
		System.out.println("	-UNIQUE AND ARTISTIC PICTURE");
		System.out.println("	-VISUAL EFFECTS");
		System.out.println("	-WRITING");
		
	}
	
	public static String getAwardData(String catInput, int yearInput, String winnerInput) throws IOException{
		
		String urlString = "http://localhost:8080/";
		
		if (catInput != null) {
			urlString += catInput + "/";
		}
		
		if(yearInput != 0) {
			urlString += yearInput + "/";
		}
		
		if(winnerInput != null) {
			urlString += winnerInput;
		}
		
		//url should be changed to presenters directories
		HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
		
		connection.setRequestMethod("GET");
		int responseCode = connection.getResponseCode();
		
		if(responseCode == 200) {
			Scanner scanner = new Scanner(connection.getInputStream());
			String response = " ";
			
			while(scanner.hasNextLine()) {
				response += scanner.nextLine();
				response += "\n";
			}
			scanner.close();
			
			return response;
		}
		
		return null;
		
	}
	
	public static void main(String[] args) throws IOException{
		
		Scanner scanner = new Scanner(System.in);
		
		//initial welcome prompt
		System.out.println("Welcome to the Oscar Awards Database\n");
		System.out.println("This database provides past Oscar winners dating back to 1927 and up to 2017");
		System.out.println("You may search the database by Year but also by Award Category");
		System.out.println("You can also decide to search for the winners or losers of the aforementioned year and/or category");
		System.out.println("Ommitting one or more of these criteria will display a collection of results that match your criteria\n");
		
		boolean exit = false;
		
		while (exit != true) {
			//search by award category prompt
			System.out.println("SEARCH BY AWARD CATEGORY");
			System.out.println("To view a list containing all award categories type \"LIST\"");
			System.out.println("To ommit SEARCH BY AWARD CATEGORY type \"SKIP\" and you will continue to SEARCH BY YEAR");
			System.out.println("Otherwise, please enter the Award Category below\n");		
			
			String catInput = scanner.nextLine();
			
			if (catInput == "LIST" || catInput == "List" || catInput == "list") {
				displayCategories();
				System.out.println("Please enter the Award Category below\n");
				catInput = scanner.nextLine();
			} else if (catInput == "SKIP" || catInput == "Skip" || catInput == "skip") {
				catInput = null;
			}
				
			
			//search by year prompt
			System.out.println("SEARCH BY YEAR");
			System.out.println("This database contains the results from 1927-2017");
			System.out.println("To ommit SEARCH BY YEAR type \"SKIP\" and you will continue to SEARCH FOR WINNERS/LOSERS");
			System.out.println("Otherwise, please enter a valid year below");
			System.out.println(" ");
			
			String yearInitialInput = scanner.nextLine();
			int yearInput = 0;
			
			
			if (yearInitialInput == "SKIP" || yearInitialInput == "Skip" || yearInitialInput == "skip") {
				yearInitialInput = null;
			}else {
				yearInput = Integer.parseInt(yearInitialInput);
			}
			
			//search by winner/loser prompt
			System.out.println("SEARCH BY WINNER/LOSER");
			System.out.println("To search for winner(s) type \"WINNER\" ");
			System.out.println("To search for losers type \"LOSER\" ");
			System.out.println("To search for all nominees type \"SKIP\" ");
			
			String winnerInput = scanner.nextLine();
			
			if (winnerInput == "WINNER" || winnerInput == "Winner" || winnerInput == "winner") {
				winnerInput = "winner";
			} else if (winnerInput == "LOSER" || winnerInput == "Loser" || winnerInput == "loser") {
				winnerInput = "losers";
			} else {
				winnerInput = null;
			}
			
			//Displaying Results
			String jsonString = getAwardData(catInput, yearInput, winnerInput);
			JSONObject jsonObject = new JSONObject(jsonString);
			
			System.out.println("RESULTS");
			
			JSONArray jsonArrayEntity = (JSONArray) jsonObject.get("Entity");
			JSONArray jsonArrayCategory = (JSONArray) jsonObject.get("Category");
			JSONArray jsonArrayYear = (JSONArray) jsonObject.get("Year");
			JSONArray jsonArrayWinner = (JSONArray) jsonObject.get("Winner");
			
			JSONObject jsonEntity = new JSONObject();
			JSONObject jsonCategory = new JSONObject();
			JSONObject jsonYear = new JSONObject();
			JSONObject jsonWinner = new JSONObject();
			
			int jsonItr = 0;
			while(jsonArrayEntity.length() > jsonItr) {
				jsonEntity = jsonArrayEntity.getJSONObject(jsonItr);
				jsonCategory = jsonArrayCategory.getJSONObject(jsonItr);
				jsonYear = jsonArrayYear.getJSONObject(jsonItr);
				jsonWinner = jsonArrayWinner.getJSONObject(jsonItr);
				
				System.out.println("Entity: " + jsonEntity.getString("entity"));
				System.out.println("Category: " + jsonEntity.getString("category"));
				System.out.println("Year: " + jsonEntity.getString("year"));
				System.out.println("Winner: " + jsonEntity.getString("winner"));
				
				jsonItr++;
			}
			
			
			System.out.println("To exit type \"EXIT\"");
			System.out.println("To make another search, press any key");
			String finalInput = scanner.nextLine();
			
			if (finalInput == "EXIT" || finalInput == "Exit" || finalInput == "exit") {
				exit = true;
			}
		}
		
	}

	//public static String getAwardData(String )
	
}


