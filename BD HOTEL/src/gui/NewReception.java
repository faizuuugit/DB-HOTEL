package gui;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.Customer;
import model.IRoom;
import model.Room;
import service.CustomerService;
import service.ReservationService;
import serviceImplementation.DbService;

public class NewReception {
	

	

	
	public static Scanner sc = new Scanner(System.in);

	public static void main(String... args) throws SQLException {
		db.DBInit.init();
		System.out.println("===========================================================");
		System.out.println("WELCOME TO LUXRY HOTEL MANAGEMENT CLI PROJECT [version:1.0]");

		boolean running = true;

		while (running) {
			displayMenu();
			int choice = Integer.parseInt(sc.nextLine());
			switch (choice) {
			case 1 -> showAvaiableRooms();
			case 2 -> bookRoom();
			case 3 -> showBookings();
			case 4 -> createUser();
			case 5 -> running = false;
			default -> System.out.println("Invalid entry, try again..");

			}
		}

	}

	private static void showAvaiableRooms() throws SQLException {
		List<Room> availableRooms = null;
		try {
			availableRooms = DbService.dbgetAllRooms();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		availableRooms.forEach(
				room -> System.out.println(room));
	}

	private static Customer createUser() throws SQLException {
	    System.out.println("Enter your First Name: ");
	    String firstName = sc.nextLine();
	    System.out.println("Enter your Last Name: ");
	    String lastName = sc.nextLine();
	    System.out.println("Enter your Email: ");
	    String email = sc.nextLine();

	    Customer c = DbService.createUser(firstName, lastName, email); // returns Customer with ID
	    System.out.println("Account Successfully created!");
	    return c;
	}


	private static Customer isValidUser() throws SQLException {
		System.out.println("Do you have an account? (1 for Yes / 0 for No): ");
		int hasAccount = Integer.parseInt(sc.nextLine());

		Customer customer;
		if (hasAccount == 1) {
			System.out.println("Enter your Email: ");
			String email = sc.nextLine();
			customer = DbService.getCustomer(email);
			if (customer == null) {
				System.out.println("No account found with that email. Please create one.");
				customer = createUser();
			}
		} else {
			customer = createUser();
		}

		return customer;
	}

	private static void showBookings() throws SQLException {

		Customer existingCustomer = isValidUser();
		System.out.println(DbService.getCustomerReservation(existingCustomer));
	}

	private static void bookRoom() throws SQLException {
		Date checkIn = getDate("Enter CHECK-IN date (dd-MM-yyyy): ");
		Date checkOut = getDate("Enter CHECK-OUT date (dd-MM-yyyy): ");

		
		if (checkIn.after(checkOut)) {
			System.out.println("Check-in date must be before check-out date.");
			return;
		}

		var availableRooms = DbService.DBfindRooms(checkIn, checkOut);
		if (availableRooms.isEmpty()) {
			System.out.println("Sorry, no rooms available for the selected dates.");
			return;
		}

		System.out.println("Available rooms:");
		availableRooms.forEach(
				room -> System.out.println("room number: " +  room.getRoomNumber() + "  price: " +  room.getPrice()));

		

		Customer customer = isValidUser();
		if (customer == null) {
			System.out.println("Booking failed. Invalid customer.");
			return;
		}

		System.out.println("Enter the Room ID you want to book: ");
		int roomId = Integer.parseInt(sc.nextLine());
		IRoom room = DbService.getRoom(roomId);

		if (room == null ) {
			System.out.println("here");
			return;
		}
		boolean found = false;
		for (IRoom r : availableRooms) {
		    if (((Room) r).getId() == roomId) {
		        found = true;
		        break;
		    }
		}
		if (!found) {
		    System.out.println("Invalid room selection or room not available.");
		    return;
		}


		DbService.reserveRoom(customer, room, checkIn, checkOut);
		System.out.println(
				"Room booked successfully for " + customer.getFirstName() + " from " + checkIn + " to " + checkOut);
	}

	public static void displayMenu() {

		System.out.println("This is Pushpa, How can I help you Today?");
		
		System.out.println("1. See our Hotel Rooms");
		System.out.println("2. Book a room");
		System.out.println("3. Show your bookings");
		System.out.println("4. Create account");
		System.out.println("5. Exit");
		System.out.print("Please select one option: ");
	}

	private static Date getDate(String a) {
		System.out.println(a);
		String input = sc.nextLine();
		Date date = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			date = formatter.parse(input);

		} catch (ParseException e) {
			System.out.println("Invalid date format. Please use dd-MM-yyyy.");
		}
		return date;
	}
}
