package serviceImplementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

public class DbService {
	private static Connection conn;

	static {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/luxry_hotel", "root", "root");
//		stmt.executeUpdate(createCustomer);
//		stmt.executeUpdate(createRooms);
//		stmt.executeUpdate(createReservations);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Customer createUser(String firstName, String lastName, String email) throws SQLException {
		if (customerExists(email)) {
			System.out.println("Account already exists with this email.");
			return getCustomer(email);
		}

		String query = "INSERT INTO customers(email, first_name, last_name) VALUES(?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, email);
		pstmt.setString(2, firstName);
		pstmt.setString(3, lastName);
		pstmt.executeUpdate();

		ResultSet rs = pstmt.getGeneratedKeys();
		int id = -1;
		if (rs.next()) {
			id = rs.getInt(1);
		}

		rs.close();
		pstmt.close();

		return new Customer(id, firstName, lastName, email);
	}

	public static boolean customerExists(String email) throws SQLException {
		String query = "SELECT COUNT(*) FROM customers WHERE email = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, email);
		ResultSet rs = pst.executeQuery();
		rs.next();
		boolean exists = rs.getInt(1) > 0;
		rs.close();
		pst.close();
		return exists;
	}

	public static Customer getCustomer(String email) throws SQLException {
		String q = "SELECT * FROM customers WHERE customers.email = ?";
		PreparedStatement pst = conn.prepareStatement(q);
		pst.setString(1, email);
		ResultSet rs = pst.executeQuery();

		Customer c = null;

		if (rs.next()) {
			int id = rs.getInt("id");
			String fn = rs.getString("first_name");
			String ln = rs.getString("last_name");
			String e = rs.getString("email");
			c = new Customer(id, fn, ln, e);
		}

		rs.close();
		pst.close();
		return c;
	}

	public static IRoom getRoom(Integer roomId) throws SQLException {
		String q = "SELECT * FROM rooms WHERE id=?";
		PreparedStatement pst = conn.prepareStatement(q);
		pst.setInt(1, roomId);
		ResultSet rs = pst.executeQuery();

		IRoom r = null;
		if (rs.next()) {
			int id = rs.getInt("id");
			int roomNumber = rs.getInt("room_number");
			double price = rs.getDouble("price");
			r = new Room(id, roomNumber, price);
		}
		rs.close();
		pst.close();
		return r;

	}

	public static void addRooms(int id, int i, double d) throws SQLException {
		String query = "insert into rooms(id, room_number,price) values(?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.setInt(2, i);
		pstmt.setDouble(3, d);
		pstmt.executeUpdate();
		pstmt.close();
	}

	public static ArrayList<Room> DBfindRooms(Date checkIn, Date checkOut) throws SQLException {

		ArrayList<Room> availableRooms = new ArrayList<>();
		String query = "select * from rooms r where r.id not in (select room_id from reservations where not ( ? < check_in or ? > check_out))";
		PreparedStatement st = conn.prepareStatement(query);
		st.setDate(1, new java.sql.Date(checkOut.getTime()));
		st.setDate(2, new java.sql.Date(checkIn.getTime()));
		ResultSet rs = st.executeQuery();

		// int n_c = rs.getMetaData().getColumnCount();

		while (rs.next()) {

			int id = rs.getInt("id");
			int roomNumber = rs.getInt("room_number");
			double price = rs.getDouble("price");

			Room room = new Room(id, roomNumber, price);
			availableRooms.add(room);
		}

		st.close();
		rs.close();

		return availableRooms;

	}

	public static List<Room> dbgetAllRooms() throws SQLException {
		List<Room> availableRooms = new ArrayList<>();

		String roomQuery = "SELECT * FROM rooms";
		Statement roomStmt = conn.createStatement();
		ResultSet roomRs = roomStmt.executeQuery(roomQuery);

		while (roomRs.next()) {
			int id = roomRs.getInt("id");
			int roomNumber = roomRs.getInt("room_number");
			double price = roomRs.getDouble("price");

			Room room = new Room(id, roomNumber, price);
			availableRooms.add(room);
		}

		roomRs.close();
		roomStmt.close();
		return availableRooms;
	}

	public static Collection<Reservation> getCustomerReservation(Customer c) throws SQLException {
		List<Reservation> reservs = new ArrayList<>();

		String q = "SELECT rom.id AS room_id, rom.room_number, rom.price, rsv.check_in, rsv.check_out FROM reservations rsv INNER JOIN rooms rom ON rsv.room_id = rom.id WHERE rsv.customer_id = ?";

		PreparedStatement pst = conn.prepareStatement(q);
		pst.setInt(1, c.getId());
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			int roomId = rs.getInt("room_id");
			int roomNumber = rs.getInt("room_number");
			double price = rs.getDouble("price");
			Date checkIn = rs.getDate("check_in");
			Date checkOut = rs.getDate("check_out");

			Room room = new Room(roomId, roomNumber, price);
			Reservation reservation = new Reservation(c, room, checkIn, checkOut);
			reservs.add(reservation);
		}

		rs.close();
		pst.close();
		return reservs;
	}

	public static void reserveRoom(Customer c, IRoom r, Date checkInDate, Date checkOutDate) throws SQLException {
		String q = "insert into reservations(customer_id, room_id, check_in, check_out) values(?,?,?,?)";
		PreparedStatement pst = conn.prepareStatement(q);
		pst.setInt(1, c.getId());
		pst.setInt(2, r.getId());
		pst.setDate(3, new java.sql.Date(checkInDate.getTime()));
		pst.setDate(4, new java.sql.Date(checkOutDate.getTime()));

		pst.executeUpdate();
		pst.close();

	}

}
