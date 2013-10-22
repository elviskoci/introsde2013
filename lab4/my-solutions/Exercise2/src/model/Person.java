package model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Person")
@XmlType(propOrder = { "age","address" })

public class Person {
	
	private int age;
	private String address;
	private String name;
	
	public Person(int age , String address, String name){
	
		this.age=age;
		this.address=address;
		this.name= name;
	}
		
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}