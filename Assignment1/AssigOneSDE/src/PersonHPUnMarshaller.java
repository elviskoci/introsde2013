import generated.*;

import javax.xml.bind.*;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.List;

public class PersonHPUnMarshaller {
	public void unMarshall(File xmlDocument) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance("generated");

			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File(
					"SchemaHealthProfiles.xsd"));
			unMarshaller.setSchema(schema);
			CustomValidationEventHandler validationEventHandler = new CustomValidationEventHandler();
			unMarshaller.setEventHandler(validationEventHandler);

			JAXBElement<PeopleType> peopleElement = (JAXBElement<PeopleType>) unMarshaller
					.unmarshal(xmlDocument);

			PeopleType people = peopleElement.getValue();
			
			System.out.println("\nPrinting list of people and their health profile:\n");
			System.out.format("\n%10s%31s%16s", "Name", "Weight" , "Height");
			String firstname="", lastname="", weight="", height="";
			
			List<PersonType> personList = people.getPerson();
			for (int i = 0; i < personList.size(); i++) {
				
				PersonType  person = (PersonType) personList.get(i);	
				HealthProfileType healthProfile =  person.getHealthprofile();
				
				firstname = person.getFirstname();
				lastname = person.getLastname();
				weight = healthProfile.getWeight()+"";
				height = healthProfile.getHeight()+"";
				
				System.out.println();
				
				if(firstname.length()+lastname.length()+1<=11){
	    			System.out.format("%s %s\t\t\t\t%s\t\t%s", firstname, lastname, weight , height);
	
	    		}
	    		else if(firstname.length()+lastname.length()+1<=19 && firstname.length()+lastname.length()+1>=11 ){
	    			System.out.format("%s %s\t\t\t%s\t\t%s", firstname, lastname, weight , height);
		    	}
	    		else {
	    			System.out.format("%s %s\t\t%s\t\t%s", firstname, lastname, weight , height);
	    		}
				
			}
			
		} catch (JAXBException e) {
			System.out.println(e.toString());
		} catch (SAXException e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String[] argv) {
		File xmlDocument = new File("HealthProfiles.xml");
		//File xmlDocument = new File("JAXBHealthProfiles.xml");
		PersonHPUnMarshaller personHPUnmarshaller = new PersonHPUnMarshaller();

		personHPUnmarshaller.unMarshall(xmlDocument);

	}

	class CustomValidationEventHandler implements ValidationEventHandler {
		public boolean handleEvent(ValidationEvent event) {
			if (event.getSeverity() == ValidationEvent.WARNING) {
				return true;
			}
			if ((event.getSeverity() == ValidationEvent.ERROR)
					|| (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {

				System.out.println("Validation Error:" + event.getMessage());

				ValidationEventLocator locator = event.getLocator();
				System.out.println("at line number:" + locator.getLineNumber());
				System.out.println("Unmarshalling Terminated");
				return false;
			}
			return true;
		}

	}
}
