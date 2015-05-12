import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class CalculAvgIdf {
	private double idfSum = 0;
	private int termCount = 0;
	
	public void fileRead() {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader("./JihyeIndices00.jhidx");
			br = new BufferedReader(fr);
			
			String string = null;
			while((string = br.readLine())!=null) {
				termCount++;
				idfSum += Math.log(900000 / (string.split(",").length-1));
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(br != null) try{br.close();}catch(IOException e){}
			if(fr != null) try{fr.close();}catch(IOException e){}
		}
	}
	
	public double calculator() {
		return idfSum/termCount;
	}
}
