package model;

public class Customer {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	
	
	public String getEmail() {
		return email;
	}
	
	public int getId() {
		return this.id;
	}
	public String getFirstName() {
		return firstName;
	}
	public Customer(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
	public Customer(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	@Override
	public String toString() {
		return "Customer [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}
	
	
}
