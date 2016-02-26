package testHarness;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TestHarness {
	public static void main(String[] args) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				
		while(true){
			System.out.print("Enter your regext: ");
			String regex = reader.readLine();
			System.out.print("Enter input string to search: ");
			String input = reader.readLine();
			Pattern pattern = 
					Pattern.compile(regex);
			Matcher matcher =
					pattern.matcher(input);
			
			boolean found = false;
			while(matcher.find()){
				System.out.println("I found the text " +
						matcher.group() + " starting at " +
						"index " + matcher.start() + " and " +
						"ending at " + matcher.end() + ".");
				found = true;
			}
			
			if (!found){
				System.out.println("No match fuond.");
			}
		}
	}
}
