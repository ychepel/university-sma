package university.domain;

public class Address {
	private String province;
	private String city;
	private String street;
	private String house;
	private int flat;
	private String phone;
	private String email;
	private Long id;
	
	public String getFullAdress() {
		return province + ", " + city + ", " + street + ", " + house + ", " + flat;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public void setId(Long id) {
		this.id = id;		
	}

	public Long getId() {
		return id;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getHouse() {
		return house;
	}

	public int getFlat() {
		return flat;
	}
	
}
