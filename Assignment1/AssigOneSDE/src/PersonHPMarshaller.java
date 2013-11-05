import generated.*;

import javax.xml.bind.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class PersonHPMarshaller{
	
	public void generateXMLDocument(File xmlDocument) {
		try {
		
		JAXBContext jaxbContext = JAXBContext.newInstance("generated");
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output",new Boolean(true));
		generated.ObjectFactory factory = new generated.ObjectFactory();
		
		PeopleType people = factory.createPeopleType();
		
		PersonType person = factory.createPersonType();
		person.setFirstname("Elvis");
		person.setLastname("Koci");
		
		HealthProfileType healthProfile = factory.createHealthProfileType();
		BigDecimal weight =BigDecimal.valueOf(80.0);
		BigDecimal height =BigDecimal.valueOf(1.82);
		
		healthProfile.setWeight(weight);
		healthProfile.setHeight(height);
	
		person.setHealthprofile(healthProfile);
		
		List<PersonType> personList = people.getPerson();
		personList.add(person);
		
		person = factory.createPersonType();
		person.setFirstname("Andrea");
		person.setLastname("Maroni");
		
		healthProfile = factory.createHealthProfileType();	
		weight = BigDecimal.valueOf(74.0);
		height = BigDecimal.valueOf(1.95);
		healthProfile.setWeight(weight);
		healthProfile.setHeight(height);
	
		person.setHealthprofile(healthProfile);
		personList.add(person);
		
		person = factory.createPersonType();
		person.setFirstname("Maria");
		person.setLastname("Sharapova");
		
		healthProfile = factory.createHealthProfileType();	
		weight = BigDecimal.valueOf(55.0);
		height = BigDecimal.valueOf(1.75);
		healthProfile.setWeight(weight);
		healthProfile.setHeight(height);
	
		person.setHealthprofile(healthProfile);
		personList.add(person);

		JAXBElement<PeopleType> peopleElement=factory.createPeople(people);
		marshaller.marshal(peopleElement, new FileOutputStream(xmlDocument));
		
		} catch (IOException e) {
			System.out.println(e.toString());

		} catch (JAXBException e) {
			System.out.println(e.toString());

		}

	}
	
	public static void main(String[] argv) {
		String xmlDocument = "JAXBHealthProfiles.xml";
		PersonHPMarshaller personHPMarshaller = new PersonHPMarshaller();
		personHPMarshaller.generateXMLDocument(new File(xmlDocument));
	}
}