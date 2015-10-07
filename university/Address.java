package university;

import java.util.List;

public class Address {
	private String province;
	private String city;
	private String street;
	private String house;
	private int flat;
	private List<String> phones;
	private String email;
	
	public String getFullAdress() {
		return province + ", " + city + ", " + street + ", " + house + ", " + flat;
	}
	
	public List<String> getPhones() {
		return phones;
	}
	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public void setHouse(String house) {
		this.house = house;
	}
	public void setFlat(int flat) {
		this.flat = flat;
	}
	
	
	
}
