
public class Main {
	public static void main(String[] args) {
		CalculAvgIdf calculAvgIdf = new CalculAvgIdf();
		calculAvgIdf.fileRead();
		System.out.println(calculAvgIdf.calculator());
	}
}
