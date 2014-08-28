package d3m.span.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ProcessXMLTaxonomy {

	public static void main(String[] args){
		ProcessXMLTaxonomy processXMLTaxonomy = new ProcessXMLTaxonomy();
		processXMLTaxonomy.execute();
	}

	public void execute(){
		try {
			readXML();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void readXML() throws ParserConfigurationException, SAXException, IOException{

		File xmlFile = new File(FilesLocation.TAXONOMY_XML);

		//Como passar o XML para instancias da ontologia
		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = 
				DocumentBuilderFactory.newInstance();

		//Get the DOM Builder
		DocumentBuilder dbuilder = factory.newDocumentBuilder();

		//Load and Parse the XML document
		//document contains the complete XML as a Tree.
		Document document = dbuilder.parse(xmlFile);

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		document.getDocumentElement().normalize();
		
		NodeList classList  = document.getElementsByTagName("class");

		for(int i = 0; i < classList.getLength(); i++){
			System.out.println("Class nr."+(i+1));
			Node node = classList.item(i);

			NamedNodeMap attr = node.getAttributes();
			Node id = attr.getNamedItem("id");
			System.out.println("--> ID: " + id.getNodeValue());

			NodeList childList = node.getChildNodes();
			for (int j = 0; j < childList.getLength(); j++) {
				Node childNode = childList.item(j);

				if(childNode.getNodeName().equals("value")){

					String childValue = childNode.getTextContent();
					System.out.println("---> Item: " + childValue);
				}
			}
		}
	}
}
