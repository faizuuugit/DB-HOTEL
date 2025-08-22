package service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.Customer;

public class CustomerService {

	private Map<String,Customer> customers = new HashMap();
	private static CustomerService INSTANCE = new CustomerService();
	
	private CustomerService() {
		//dammunte object create chey
	}
	public static CustomerService getInstance() {
		return INSTANCE;
	}
	
	public void addCustomer(String firstName, String lastName, String email ) {
		customers.put(email, new Customer(firstName, lastName, email));
		
	}
	
	public Customer getCustomer(String email) {
		return customers.get(email);
	}
	
	public Collection<Customer> getAllCustomers(){
		return customers.values();
	}
}
