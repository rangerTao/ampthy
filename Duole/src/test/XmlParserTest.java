package test;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.duole.utils.parseXML;

public class XmlParserTest {

	public static void main(String args[]){
		
		try {
			parseXML.readXML(null, "C:\\Users\\Administrator\\Desktop\\DuoleCache\\itemlist.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
