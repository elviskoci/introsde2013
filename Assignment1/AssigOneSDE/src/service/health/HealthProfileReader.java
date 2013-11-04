package service.health;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xpath.internal.NodeSet;


public class HealthProfileReader {

	 	private Document doc;
	    private XPath xpath;

	    public void loadXML() throws ParserConfigurationException, SAXException, IOException {

	        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        domFactory.setNamespaceAware(true);
	        DocumentBuilder builder = domFactory.newDocumentBuilder();
	        doc = builder.parse("HealthProfiles.xml");

	        //creating xpath object
	        getXPathObj();
	    }

	    public XPath getXPathObj() {

	        XPathFactory factory = XPathFactory.newInstance();
	        xpath = factory.newXPath();
	        return xpath;
	    }

	    public Double getWeight(String firstname, String lastname) throws XPathExpressionException{
	    
	    	XPathExpression expr= xpath.compile("/people/person[firstname='"+firstname+"' and '"+lastname+"' ]/healthprofile/weight/text()");
	    	Double weight = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
	    	return weight;
	    }
	    
	    public Double getHeight(String firstname, String lastname) throws XPathExpressionException{
		    
	    	XPathExpression expr= xpath.compile("/people/person[firstname='"+firstname+"' and '"+lastname+"' ]/healthprofile/height/text()");
	    	Double height = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
	    	return height;
	    }

	    public void printAll() throws XPathExpressionException{
	    	
	    	XPathExpression expr= xpath.compile("//person");
	    	NodeList people = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	    	
	    	String firstname, lastname, weight, height;
	    	Node person = null; 
	    	NodeList healthProfile = null;
	    
	    	System.out.format("\n%10s%35s%16s", "Name", "Weight" , "Height"); 
	    	
	    	for(int i=0;i<people.getLength();i++){
	    		
	    		person= (Node) people.item(i);
	    		NodeList person_info = person.getChildNodes();
	    		
	    		firstname= person_info.item(1).getTextContent();
	    		lastname = person_info.item(3).getTextContent();
	    		healthProfile = person_info.item(5).getChildNodes();
	    		weight = healthProfile.item(1).getTextContent();
	    		height = healthProfile.item(3).getTextContent();
	    		
	    		System.out.println();
	    		String name= firstname+lastname;
	    		
	    		if(name.length()<=14){
	    			System.out.format("%s %s\t\t\t\t%s\t\t%s", firstname, lastname, weight , height);
	    		}
	    		else{
	    			System.out.format("%s %s\t\t\t%s\t\t%s", firstname, lastname, weight , height);
	    		}
	        		
	    	}
	    }
	    
	    public String gethProfile(String firstname, String lastname) throws XPathExpressionException{
	    	
	    	//fullname.indexOf("_");
	    	//fullname.trim()
	    	
	    	XPathExpression expr= xpath.compile("/people/person[firstname='"+firstname+"' and '"+lastname+"' ]/healthprofile");
	    	Node health_profile = (Node) expr.evaluate(doc, XPathConstants.NODE);
	    	return health_profile.getTextContent();
	    }
	    
	    public NodeList getPersonByWeight(String weight, String condition) throws XPathExpressionException {

	        XPathExpression expr = xpath.compile("//ancestor::person[healthprofile/weight" + condition + "'" + weight + "']");
	    	// XPathExpression expr = xpath.compile("//ancestor::healthprofile[weight" + condition + "'" + weight + "']");
	    	NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	        return nodes;
	    }
	    
	    public static void main(String[] args) throws ParserConfigurationException, SAXException,
	            IOException, XPathExpressionException {

	    	String firstname="" , lastname ="" ;
	    	
	    	
	        HealthProfileReader test = new HealthProfileReader();
	        test.loadXML();

	        //getting weight by name
	        firstname = "Elvis";
	        lastname= "Choc";
	        Double weight  = test.getWeight(firstname,lastname);
	        System.out.println("The weight of "+firstname+" "+lastname+" is: "+ weight);
	        

	        //getting weight by name
	        firstname = "Angelina";
	        lastname= "Escobar";
	        Double height  = test.getHeight(firstname, lastname);
	        System.out.println("The height "+firstname+" "+lastname+" is: "+ height);
	        
	        //print all people
	        test.printAll();
	        
	        //find the health profile of a person
	        firstname = "Pinh Chau";
	        lastname= "Fren Ahn";
	        String health_profile = test.gethProfile(firstname, lastname);
	        System.out.println("\n\nThe health profile of "+firstname+" "+lastname+" is : "+health_profile);
	        
	        //Print all the person that satisfy the condition specified about the weight
	        NodeList nodes=test.getPersonByWeight("100", ">");
	        Node person = null;	
	        
	        System.out.println("\nThe persons that satisfy the conditions are:");
	        
    		for(int i=0;i<nodes.getLength();i++){
	    		
    			/*Node lname = nodes.item(i).getPreviousSibling().getPreviousSibling();
    			Node fname = lname.getPreviousSibling().getPreviousSibling();
    			System.out.println(fname.getTextContent()+" "+lname.getTextContent());*/
    			
	    		person= (Node) nodes.item(i);
	    		NodeList person_info = person.getChildNodes();
	    		
	    		firstname= person_info.item(1).getTextContent();
	    		lastname = person_info.item(3).getTextContent();
	    		System.out.println(""+firstname+" "+lastname);
    		}		  		
	    }
}
