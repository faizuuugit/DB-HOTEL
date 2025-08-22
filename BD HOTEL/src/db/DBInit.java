package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInit {
	public static void init() {
		
			String createCustomer = "create table if not exists customers ( " + 
					"id int auto_increment primary key,"+
					"email varchar(50) unique not null,"+
					"first_name varchar(50) not null,"+
					"last_name varchar(50) not null"+
					")";
			
			
			String createRooms = "create table if not exists rooms ("+
			"id int auto_increment primary key,"+
			"room_number int unique not null,"+
			"price decimal(10,2) not null"+
			")";
			
			String createReservations = "create table if not exists reservations ("+
			"id int auto_increment primary key,"+
			"customer_id int not null,"+
			"room_id int not null,"+
			"check_in date not null,"+
			"check_out date not null,"+
			"foreign key (customer_id) references customers(id) on delete cascade,"+
			"foreign key (room_id) references rooms(id) on delete cascade"+
			")";
			
			
		try(Connection conn = DriverManager.
				getConnection("jdbc:mysql://localhost:3306/luxry_hotel", "root", "root");
				Statement stmt = conn.createStatement()){
			
			stmt.executeUpdate(createCustomer);
			stmt.executeUpdate(createRooms);
			stmt.executeUpdate(createReservations);
			System.out.println("db intializeduuu!!!");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
