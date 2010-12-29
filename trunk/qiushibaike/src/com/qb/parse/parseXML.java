package com.qb.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.qb.R.string;

import android.R.integer;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class parseXML {
	public static String filePath = "";
	//public static FileInputStream iStream = null;
	
	public static void initFile(InputStream is,DocumentBuilder dBuilder,File file) throws SAXException, IOException, TransformerException{
			Document document;
			document = dBuilder.parse(is);
			DOMSource domSource = new DOMSource(document);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();
			StreamResult streamResult = new StreamResult(file);
			transformer.transform(domSource, streamResult);

		
		
	}
	
	public static String[] readFile(String filePath,DocumentBuilder dBuilder,String[] result,StringBuffer sbresult) throws SAXException, IOException{
		
		parseXML.filePath = filePath + "/feed.xml";
		FileInputStream iStream = new FileInputStream(new File(parseXML.filePath));

		Document document = dBuilder.parse(iStream);

		NodeList nList = document.getElementsByTagName("item");
		result = new String[nList.getLength()];
		
		for (int i = 0; i < nList.getLength(); i++) {
			sbresult = new StringBuffer();
			Node node = nList.item(i);
			NodeList nodeList = node.getChildNodes();
			for (int j = 0; j < nodeList.getLength(); j++) {
				Node node2 = nodeList.item(j);
				if (node2.getNodeName().equalsIgnoreCase("name")) {
					sbresult.append(node2.getFirstChild().getNodeValue()
							+ ";");
				} else if (node2.getNodeName().equalsIgnoreCase("url")) {
					sbresult.append(node2.getFirstChild().getNodeValue()
							+ ";");
				} else if (node2.getNodeName().equalsIgnoreCase("id")) {
					sbresult.append(node2.getFirstChild().getNodeValue());
				}
			}

			result[i] = sbresult.toString();
		}
		return result;
	}
	
	public static String[] readXML(InputStream is , String filePath) throws IOException, TransformerException, SAXException {

		String[] result = null;
		StringBuffer sbresult = new StringBuffer();
		File file = new File(filePath + "/feed.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbf.newDocumentBuilder();
			if(!file.exists()){
				initFile(is,dBuilder,file);
			}
			
			result = readFile(filePath,dBuilder,result,sbresult);
			
		} catch (ParserConfigurationException e) {
			file.delete();
			initFile(is,dBuilder,file);
			result = readFile(filePath,dBuilder,result,sbresult);
		} catch (SAXException e) {
			file.delete();
			initFile(is,dBuilder,file);
			result = readFile(filePath,dBuilder,result,sbresult);
		} catch (IOException e) {
			e.printStackTrace();
		}
		is.close();
		return result;
	}

	public void deleteNode(int id) throws IOException {
		String[] result = null;
		StringBuffer sbresult = new StringBuffer();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbf.newDocumentBuilder();
			FileInputStream iStream = new FileInputStream(new File(parseXML.filePath));
			Document document = dBuilder.parse(iStream);
			///Document document = dBuilder.parse("E:\\workspace\\qiushibaike\\assets\\feed.xml");

			NodeList nList = document.getElementsByTagName("item");
			result = new String[nList.getLength()];

			for(int i = 0; i < nList.getLength(); i ++){
				sbresult = new StringBuffer();
				Node node = nList.item(i);
				NodeList nodeList = node.getChildNodes();
				for(int j = 0 ; j<nodeList.getLength();j++){
					Node node2 = nodeList.item(j);
					if(node2.getNodeName().equalsIgnoreCase("id")){
						if(node2.getFirstChild().getNodeValue().equalsIgnoreCase(id+"")){
							node.getParentNode().removeChild(node);
						
						}
					}
				}
				result[i] = sbresult.toString();
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			
			StreamResult streamResult = new StreamResult(new File(filePath));
			transformer.transform(domSource, streamResult);
			
			iStream.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addNode(String[] input) throws IOException {
		String[] result = null;
		StringBuffer sbresult = new StringBuffer();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbf.newDocumentBuilder();
			FileInputStream iStream = new FileInputStream(new File(parseXML.filePath));
			Document document = dBuilder.parse(iStream);

			//get the lasted index of id
			int id = Integer.parseInt(document.getElementsByTagName("index").item(0).getFirstChild().getNodeValue());
			document.getElementsByTagName("index").item(0).getFirstChild().setNodeValue(id+1+"");
			//the values
			Text name = document.createTextNode(input[0]);
			Text url = document.createTextNode(input[1]);
			Text tid = document.createTextNode(id+1+"");
			
			//new elements
			Element newElement = document.createElement("item");
			Element newNameElement = document.createElement("name");
			Element newUrlElement = document.createElement("url");
			Element newIdElement = document.createElement("id");
			newNameElement.appendChild(name);
			newUrlElement.appendChild(url);
			newIdElement.appendChild(tid);
			newElement.appendChild(newNameElement);
			newElement.appendChild(newUrlElement);
			newElement.appendChild(newIdElement);

			document.getDocumentElement().appendChild(newElement);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			
			StreamResult streamResult = new StreamResult(new File(filePath));
			transformer.transform(domSource, streamResult);
			iStream.close();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean editNode(String[] input) throws IOException{
		
		String[] result = null;
		StringBuffer sbresult = new StringBuffer();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbf.newDocumentBuilder();
			FileInputStream fileInputStream = new FileInputStream(new File(parseXML.filePath));
			Document document = dBuilder.parse(fileInputStream);
			
			int id = Integer.parseInt(input[2]);

			NodeList nList = document.getElementsByTagName("item");
			result = new String[nList.getLength()];
			Log.v("qb", result.toString());
			for(int i = 0; i < nList.getLength(); i ++){
				sbresult = new StringBuffer();
				Node node = nList.item(i);
				NodeList nodeList = node.getChildNodes();
				for(int j = 0 ; j<nodeList.getLength();j++){
					Node node2 = nodeList.item(j);
					if(node2.getNodeName().equalsIgnoreCase("id")){
						if(node2.getFirstChild().getNodeValue().equalsIgnoreCase(id+"")){
							node.getParentNode().removeChild(node);
							
							Text name = document.createTextNode(input[0]);
							Text url = document.createTextNode(input[1]);
							Text tid = document.createTextNode(input[2]);
							//new elements
							Element newElement = document.createElement("item");
							Element newNameElement = document.createElement("name");
							Element newUrlElement = document.createElement("url");
							Element newIdElement = document.createElement("id");
							newNameElement.appendChild(name);
							newUrlElement.appendChild(url);
							newIdElement.appendChild(tid);
							newElement.appendChild(newNameElement);
							newElement.appendChild(newUrlElement);
							newElement.appendChild(newIdElement);

							document.getDocumentElement().appendChild(newElement);
							
							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							Transformer transformer = transformerFactory.newTransformer();
							DOMSource domSource = new DOMSource(document);

							StreamResult streamResult = new StreamResult(new File(filePath));
							transformer.transform(domSource, streamResult);
						
						}
					}
				}
				result[i] = sbresult.toString();
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			
			StreamResult streamResult = new StreamResult(new File(filePath));
			transformer.transform(domSource, streamResult);
			FileOutputStream fos = new FileOutputStream(new File(filePath));

			fileInputStream.close();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
