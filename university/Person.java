package university;

import java.util.Calendar;
import java.util.Date;

public abstract class Person {
	private String firstName;
	private String lastName;
	private String patronymicName;
	private Date birthDate;
	private char gender;
	private String passport;
	private String nationality;
	private Address address;
	
	public String getFullName() {
		return lastName+" "+firstName+" "+patronymicName;
	}
}
