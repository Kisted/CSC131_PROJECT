import java.io.IOException;
import java.util.Scanner;

public class RESTApiClient {
	
	public static void main(String[] args) throws IOException{
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to the Oscar Awards Database\n");
		System.out.println("This database provides previous Oscar winners dating back to ?1900?");
		System.out.println("You may search this database by:");
		System.out.println("   - Name of Winner (actor/actress or movie)");
		System.out.println("   - Award Category");
		System.out.println("   - Year");
		System.out.println("Entering all of these categories will find your desired result immediately");
		System.out.println("If you only have two or one of these, you will receive a list of results that meet those criteria\n");
		
		System.out.println("Please enter the corresponding number(s) of which criteria you wish to search by:");
		System.out.println("If you wish to search by multiple criteria enter each number with a space in between each number");
		System.out.println("   ex. [1] or [1 2] or [1 2 3] \n");
		System.out.println("   1. Name of Winner (actor/actress or movie)");
		System.out.println("   2. Award Category");
		System.out.println("   3. Year");
		
		
		String keyword = scanner.nextLine();
		
		System.out.println(" ");
		System.out.println(keyword);
		
	}

	//public static String getAwardData(String )
	
}


