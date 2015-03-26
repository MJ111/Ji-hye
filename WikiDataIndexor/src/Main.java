import java.io.IOException;
import java.util.Scanner;


public class Main {
	private static String WIKIDATA_PATH = "D:/kowiki";	
	
	public static void main(String args[]) {
		WikiIndexor wi = null;
		int input = 0;
		
		try {
			wi = new WikiIndexor(WIKIDATA_PATH);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}		
		
		try {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Start indexing? (1:yes, 2: no) : ");
			input = scanner.nextInt();
		} catch (Exception e ){
			e.printStackTrace();
			System.exit(1);
		}
		
		if(input == 1) {
			System.out.println("Start indexing");
			try {
				wi.startIndexing();
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			System.out.println("Exit program");
		}
		
	}
}