package jihye.indexor.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import jihye.indexor.WikiData;
import jihye.indexor.util.Utility;
import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class WikiParser {
	private String wikiDumpPath;
	private String wikiDataPath;
	private File rawWikiDump;
	
	
	public WikiParser(String wikiDumpPath) throws IOException{
		this(wikiDumpPath, wikiDumpPath);
	}
	
	public WikiParser(String wikiDumpPath, String wikiDataPath) throws IOException{
		this.wikiDumpPath = wikiDumpPath;
		this.wikiDataPath = wikiDataPath;
		
		rawWikiDump = getRawDumpFile();
	}
	
	private File getRawDumpFile() throws IOException {
		File [] files = Utility.getInstance().getFiles(wikiDumpPath, "xml");
		if(files.length == 0) throw new IOException("위키 데이터를 찾을 수 없습니다! (경로확인요)");
		else if(files.length != 1) throw new IOException("xml 파일이 여러개입니다. (1개만 놓아두세요)");
		System.out.println("Using Dump : " + files[0].getPath());
		return files[0];
	}
		
	public void startParse(final int split) {

		long lnStartTime, lnEndTime;
		final Vector<WikiData> tempvecWikiData = new Vector<WikiData>();
		//Here, make wiki data

		lnStartTime = System.currentTimeMillis();
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(rawWikiDump.getAbsolutePath());

		try{
			wxsp.setPageCallback(new PageCallbackHandler() 
			{
				int fileCounter = 1;
				int pageCounter = 0;

				public void process(WikiPage page) 
				{
					pageCounter++;
					String strTitle = deleteEscape(page.getTitle());
					String strTextWithoutInfoBox = page.getText();
					String strDocumentID = page.getID();

					WikiData wd = new WikiData(strTitle, strTextWithoutInfoBox, strDocumentID);
					tempvecWikiData.add(wd);

					if(pageCounter % split == 0) {
						try {
							File outfile = new File(String.format("%s/WikiData%02d.jhd", wikiDataPath, fileCounter++));
							FileOutputStream fos = new FileOutputStream(outfile);
							ObjectOutputStream oos = new ObjectOutputStream(fos); 
							
							oos.writeObject(tempvecWikiData);
							tempvecWikiData.clear();
							
							oos.close();
							fos.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("Processed Dump : " + pageCounter);
					}

				}
			});
			wxsp.parse();			
		}catch(Exception e) {
			e.printStackTrace();
		} 
		
		
		if(tempvecWikiData.size() != 0) {
			try {
				File outfile = new File(wikiDataPath + "/WikiData00" + ".jhd");
				FileOutputStream fos = new FileOutputStream(outfile);
				ObjectOutputStream oos = new ObjectOutputStream(fos); 

				oos.writeObject(tempvecWikiData);
				tempvecWikiData.clear();

				oos.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		lnEndTime = System.currentTimeMillis();
		System.out.println("WikiData 생성 시간 : " + (lnEndTime - lnStartTime) / 1000);
	}	
	
	private String deleteEscape(String Text) {
		int nExcapePos = Text.lastIndexOf('\n');
		String ret = Text.substring(0, nExcapePos);
		return ret;
	}
	
}