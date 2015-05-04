import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XmlParser {
	public ArrayList<String> parser(String xml) {
		ArrayList<String> resultStrs = new ArrayList<>();
		
//		System.out.println(xml);
		InputSource is = new InputSource(new StringReader(xml)); 
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			
			XPath  xpath = XPathFactory.newInstance().newXPath();
		   
			String expression = "//*/문형_구성_구획";
			NodeList structures = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
			
			for( int index = 0; index < structures.getLength(); index ++ ){				   
				NodeList structure = structures.item(index).getChildNodes();
				
				String resultStr = new String();
				for (int index2 = 0; index2 <structure.getLength(); index2++) {
					if (structure.item(index2).getNodeName().contains("문형")) {
						resultStr = "";
						String wordStructure = structure.item(index2).getTextContent();
						resultStr += wordStructure + "\n";
//						System.out.println("문형: "+ wordStructure);
					}
					if (structure.item(index2).getNodeName().contains("하위_센스_구획")) {
						NodeList constraints = structure.item(index2).getChildNodes();
						for( int index3 = 0; index3 < constraints.getLength(); index3 ++ ){
							if (constraints.item(index3).getNodeName().equals("선택_제약")) {
								String arg = constraints.item(index3).getAttributes().getNamedItem("arg").getNodeValue();
								Node thtItem = constraints.item(index3).getAttributes().getNamedItem("tht");
								String tht = new String();
								if (thtItem != null) {
									tht = thtItem.getNodeValue();
								}
								String type = constraints.item(index3).getTextContent();		
								resultStr += arg + "," + tht + "," + type + "\n";
//								System.out.println("선택 제약: " + arg + " " + tht + " " + type);
								
							}
						}
						resultStrs.add(resultStr);
//						System.out.println(resultStr);
					}
				}
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultStrs;
	}
}
