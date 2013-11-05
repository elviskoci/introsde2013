import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

	    public String getWeight(String firstname, String lastname) throws XPathExpressionException{
	    
	    	XPathExpression expr= xpath.compile("/people/person[firstname='"+firstname+"' and lastname='"+lastname+"' ]/healthprofile/weight/text()");
	    	
	    	Double weight = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
	    	
	    	if (weight.isNaN()){	
	    		return "Unknown";
	    	}    	
	    	return ""+weight;
	    }
	    
	    public String getHeight(String firstname, String lastname) throws XPathExpressionException{
		    
	    	XPathExpression expr= xpath.compile("/people/person[firstname='"+firstname+"' and lastname='"+lastname+"' ]/healthprofile/height/text()");
	    	Double height = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
	    	
	    	if (height.isNaN()){
	    		return "Unknown";
	    	}
	    	return ""+height;
	    }

	    public String printAll() throws XPathExpressionException{
	    	
	    	XPathExpression expr= xpath.compile("//person");
	    	NodeList people = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	    	
	    	String firstname, lastname, weight, height;
	    	Node person = null; 
	    	NodeList healthProfile = null;
	    
	    	String person_list="       Name\t\t\t       Weight\t       Height\n";
	    	
	    	for(int i=0;i<people.getLength();i++){
	    		
	    		person= (Node) people.item(i);
	    		NodeList person_info = person.getChildNodes();
	    		
	    		firstname= person_info.item(1).getTextContent();
	    		lastname = person_info.item(3).getTextContent();
	    		healthProfile = person_info.item(5).getChildNodes();
	    		weight = healthProfile.item(1).getTextContent();
	    		height = healthProfile.item(3).getTextContent();
	    		
	    		String name= firstname+lastname;

	    		if(name.length()+1<=11){
	    			person_list=person_list+""+firstname+" "+lastname+"\t\t\t\t"+weight+"\t\t"+height+"\n";
	    		}
	    		else if(name.length()+1>=11 && name.length()+1<=19) {
	    			person_list=person_list+""+firstname+" "+lastname+"\t\t\t"+weight+"\t\t"+height+"\n";
	    		}
				else{
					person_list=person_list+""+firstname+" "+lastname+"\t\t"+weight+"\t\t"+height+"\n";
				}
	    	}
	 
	    	return person_list;
	    }
	    
	    public String gethProfile(String firstname, String lastname) throws XPathExpressionException{
	    	
	    	XPathExpression expr= xpath.compile("/people/person[firstname='"+firstname+"' and lastname='"+lastname+"' ]/healthprofile");
	    	
	    	Node health_profile;
	    	NodeList hplist;
	    	
	    	try{
	    		health_profile = (Node) expr.evaluate(doc, XPathConstants.NODE);
	    		hplist = health_profile.getChildNodes(); 
	    	}catch (NullPointerException e){	
	    		return "Unknown";
	    	}
	    
	    	return "weight="+ hplist.item(1).getTextContent()+ ", height=" + hplist.item(3).getTextContent();
	    }
	    
	    public String getPersonByWeight(Double weight, String condition) throws XPathExpressionException {

	        XPathExpression expr = xpath.compile("//ancestor::person[healthprofile/weight" + condition + "'" + weight + "']");
	    	// XPathExpression expr = xpath.compile("//ancestor::healthprofile[weight" + condition + "'" + weight + "']");
	    	
	        String person_list ="";
	       
	        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        	Node person = null;	

  			for(int i=0;i<nodes.getLength();i++){	
  	    		person= (Node) nodes.item(i);
  	    		NodeList person_info = person.getChildNodes();	
  	    		String firstname= person_info.item(1).getTextContent();
  	    		String lastname = person_info.item(3).getTextContent();
  	    		person_list= person_list.concat(""+firstname+" "+lastname+"\n");
  			}
	        return person_list;
	    }
	    
	    private static void printMainHelp()
		{
		     System.out.println("\nUsage: java HealthProfileReader [-option] [args...] ");
		     System.out.println("where options include:\n");
		     System.out.println("\t-w <fullname>\t\t\tSearch person's weight");
		     System.out.println("\t-h <fullname>\t\t\tSearch person's height");
		     System.out.println("\t-all\t\t\t\tPrint all the persons and their info");
		     System.out.println("\t-hp <fullname>\t\t\tReturns the health profile of the person");
		     System.out.println("\t-sbw <weight> <condition>\tReturns the list of persons that fulfill the \n\t\t\t\t\tcondition about the weight");
		}
	    
	    public static void main(String[] args) throws ParserConfigurationException, SAXException,
	            IOException, XPathExpressionException {
			
	         HealthProfileReader hpr = new HealthProfileReader();
	         hpr.loadXML();

	        /*COMMAND LINE EXECUTION
	    	 * 
	    	 */
			 System.out.print("\nArguments:");
			 for (int i=0;i<args.length;i++){
				
				System.out.print("  "+args[i]);
			 }
			 System.out.println();
			 
	     	 if ((args.length==0 || args == null) || (args.length > 3)) {
	     	   System.out.println("\nWrong number of arguments!!");
	     	   printMainHelp();
	           System.exit(1);
	         }
	     	  
	     	 if(args.length==3){
	     		 	 
	     		 if(args[0].compareTo("-w")==0){	
	     			String firstname = args[1];
		     		String lastname = args[2];	
	     			String weight  = hpr.getWeight(firstname,lastname);
	     	        System.out.println("\nThe weight of "+firstname+" "+lastname+" is: "+ weight);
	     		 }
	     		 else if(args[0].compareTo("-h")==0){
	     			String firstname = args[1];
	     			String lastname = args[2];
	     			String height  = hpr.getHeight(firstname, lastname);
	     		    System.out.println("\nThe height of "+firstname+" "+lastname+" is: "+ height);
	     		 }
	     		 else if(args[0].compareTo("-sbw")==0){
	     			 
	     			 Double weight= 100.0;
	     			 try{
	     				 weight = Double.valueOf(args[1]);
	     			 }
	     			 catch (NumberFormatException e){
	     				 System.out.println("\nWrong value.Expected double for weight");
	     				 printMainHelp();
	     		         System.exit(1);
	     			 }
	     			 String condition = args[2];
	     			 
	     			 if(condition.compareTo("<")==0 || condition.compareTo(">")==0 || condition.compareTo("=")==0 || condition.compareTo("<=")==0 ||condition.compareTo(">=")==0){
		     			 System.out.println("\nThe persons that satisfy the conditions are:");
		     			 String person_list = hpr.getPersonByWeight(weight,condition);
		     			 System.out.println(""+person_list);
	     			 }
	     			 else{
		     			 System.out.println("\nThe condition you provided was not correct.\nUse one of >, < , = , <=, >=");
	     				 printMainHelp();
	     				 System.exit(1);
	     			 }
	     		 }
	     		 else if(args[0].compareTo("-hp")==0){
	     			 
	     			String firstname = args[1];
	     			String lastname = args[2];
	     			String health_profile = hpr.gethProfile(firstname, lastname);
	    	        System.out.println("\nThe health profile of "+firstname+" "+lastname+" is : \n"+health_profile); 
	     		 }
	     		 else{
		     		 System.out.println("\nThe option you entered is not recognized!!");
	 				 printMainHelp();
	 		         System.exit(1);
		     	 }
	     	 }
	     	 else if(args[0].compareTo("-all")==0){
	     		 
	     		 if(args.length>1){		 
	     			 System.out.println("\nWrong number of arguments!!");
	     			 printMainHelp();
		  	         System.exit(1);
	     		 }
	     		 else{
	     			  System.out.println("\n"+hpr.printAll());
	     		 }
	     	 
	     	 }
	     	 else{
	     		 System.out.println("\nThe option you entered is not recognized or wrong number of arguments!!");
				 printMainHelp();
		         System.exit(1);
	     	 }
	     	
	     	
	     	/*IDE EXECUTION
		    	 * 
		    	 *
	     	String firstname, lastname;
	        //getting weight by name
	        firstname = "Elvis";
	        lastname= "Choc";
	        String weight  = hpr.getWeight(firstname,lastname);
	        System.out.println("The weight of "+firstname+" "+lastname+" is: "+ weight);
	        

	        //getting weight by name
	        firstname = "Angelina";
	        lastname= "Escobar";
	        String height  = hpr.getHeight(firstname, lastname);
	        System.out.println("The height of "+firstname+" "+lastname+" is: "+ height);
	        
	        
	        //print all people
	        System.out.println("\n"+hpr.printAll());
	        
	        
	        //find the health profile of a person
	        firstname = "Pinh Chau";
	        lastname= "Fren Ahn";
	        String health_profile = hpr.gethProfile(firstname, lastname);
	        System.out.println("\n\nThe health profile of "+firstname+" "+lastname+" is : \n"+health_profile);
	        
	        
	        //Print all the person that satisfy the condition specified about the weight
	        System.out.println("\nThe persons that satisfy the condition are:");
			String person_list = hpr.getPersonByWeight(80.0, ">=");
			System.out.println(""+person_list);
			*/
	    }
}
