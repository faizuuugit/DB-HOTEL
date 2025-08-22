package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.Customer;
import model.IRoom;
import model.Reservation;



import model.Room;
import serviceImplementation.DbService;

public class ReservationService {
	private Map<Integer, IRoom> rooms = new HashMap();
	private ArrayList<Reservation> reservations = new ArrayList();
	private static ReservationService INSTANCE;
	
	private ReservationService() throws SQLException {
		
//        rooms.put(101, new Room(1, 101, 100.0));
//        rooms.put(102, new Room(2, 102, 150.0));
//        rooms.put(103, new Room(3, 103, 200.0));
//        rooms.put(104, new Room(4, 104, 250.0));
//        rooms.put(105, new Room(5, 105, 300.0));
        
       
      
    }
	
	public static ReservationService getInstance() throws SQLException {
		if(INSTANCE==null) {
			INSTANCE = new ReservationService();
			
		}
		
			return INSTANCE;
		
	}

//	public void addRoom(Integer roomId, Double price) {
//		rooms.put(roomId, new Room(roomId, price));
//	}
//	
	public IRoom getRoom(Integer roomId) {
		return rooms.get(roomId);
	}
	
	public Collection<IRoom> getAllRooms(){
		return rooms.values();
	}
	
	public void reserveRoom(Customer c, IRoom r, Date checkInDate, Date checkOutDate) {
		reservations.add(new Reservation(c, r, checkInDate, checkOutDate));
	}
	
	public Collection<IRoom> findRooms(Date checkIn, Date checkOut){
		ArrayList<IRoom> availableRooms = new ArrayList<>(rooms.values());
		for(Reservation r: reservations) {
			if (!(checkOut.before(r.getCheckInDate()) || checkIn.after(r.getCheckOutDate()))) {
	            availableRooms.remove(r.getRoom());
	        }
		}
		
		return availableRooms;
	}
	
	public Collection<Reservation> getCustomerReservation(Customer c) {
		ArrayList<Reservation> reservs = new ArrayList();
		for(Reservation r: reservations) {
			if(r.getCustomer().getEmail().equals(c.getEmail())) {
				reservs.add(r);
			}
		}
		return reservs;
	}
	
	public void printAllReservations() {
		reservations.forEach(System.out::println);
	}
}
