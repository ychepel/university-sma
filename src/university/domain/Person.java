package university.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class Person {
	private String firstName = "";
	private String lastName = "";
	private String patronymicName = "";
	private Date birthDate = (new GregorianCalendar(1990, Calendar.JANUARY, 1)).getTime();
	private char gender = ' ';
	private String passport = "";
	private String nationality = "";
	private Address address;
	private Long personId;
	
	public Person() {
		this.address = new Address();
	}
	
	public String getFullName() {
		return lastName+" "+firstName+" "+patronymicName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPatronymicName(String patronymicName) {
		this.patronymicName = patronymicName;
	}
	
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public Long getPersonId() {
		return personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPatronymicName() {
		return patronymicName;
	}
	
}
