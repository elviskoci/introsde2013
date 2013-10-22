import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.Person;


public class PersonMain {

  private static final String PERSON_XML = "./person-jaxb.xml";

  public static void main(String[] args) throws JAXBException, IOException {

    ArrayList<Book> bookList = new ArrayList<Book>();

    // create books
    
    Person person = new Person();
    
        // create bookstore, assigning book
    Bookstore bookstore = new Bookstore();
    bookstore.setName("Fraport Bookstore");
    bookstore.setLocation("Frankfurt Airport");
    bookstore.setBookList(bookList);

    // create JAXB context and instantiate marshaller
    JAXBContext context = JAXBContext.newInstance(Bookstore.class);
    Marshaller m = context.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    // Write to System.out
    m.marshal(bookstore, System.out);

    // Write to File
    m.marshal(bookstore, new File(BOOKSTORE_XML));

    // get variables from our xml file, created before
    System.out.println();
    System.out.println("Output from our XML File: ");
    Unmarshaller um = context.createUnmarshaller();
    Bookstore bookstore2 = (Bookstore) um.unmarshal(new FileReader(BOOKSTORE_XML));
    ArrayList<Book> list = bookstore2.getBooksList();
    for (Book book : list) {
      System.out.println("Book: " + book.getName() + " from "
          + book.getAuthor());
    }
  }
} 