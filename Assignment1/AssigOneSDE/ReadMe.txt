The build.xml document contains all targets to initialize, compile, marshal, unmarshall, generate the classes from xsd file, and search the XML file using XPATH. The file HealthProfiles.xml contains 20 persons and their health profiles. 

1. HealthProfileReader.java is the class where the methods to search using XPATH are implemented. 
To execute this class from CMD follow the instruction below:

	 Usage: java HealthProfileReader [-option] [args...]
	 where options include:
	 -w <fullname>          			This is used to search person's weight.
	 -h <fullname>						This is used to search person's height.
	 -all								Prints all the persons and their info.
	 -hp <fullname>						Returns the health profile of the person.
	 -sbw <weight> <condition> 			Returns the list of persons that fulfill the condition about the weight
	 
	 Example1: -w Angelina Escobar
	 Example2: -sbw 80 <
	 
You can execute this class using: ant execute.XPathHPReader
You will be asked for the arguments, and then you have to enter one of the options above. 
Write the arguments separated by space.  

2. You run the marshalling and unmarshalling using targets execute.JAXBMarshaller and execute.JAXBUnMarshaller correspondingly. 
	a) The marshaller will create file JAXBHealthProfiles.xml . This is contains only 3 persons. Just two show how marshalling works.
	
	b) The file HealthProfiles.xml, which contains 20 persons, is used by the unmarshaller. I have tested umarshaller also with JAXBHealthProfiles.xml and it works the same.  

3. Classes are automatically generated from the xml schema SchemaHealthProfiles.xsd
You will find them in ./src/generated.