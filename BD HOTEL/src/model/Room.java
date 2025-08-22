package model;

public class Room implements IRoom {
	private int id;
	private Integer roomNumber;
	private Double price;


	public Room(int id, Integer roomNumber, Double price) {
		super();
		this.id = id;
		this.roomNumber = roomNumber;
		this.price = price;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Room [roomNumber=" + roomNumber + ", price=" + price + "]";
	}
	
	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public Integer getRoomNumber() {
		// TODO Auto-generated method stub
		return roomNumber;
	}

	@Override
	public Double getPrice() {
		// TODO Auto-generated method stub
		return price;
	}

	
}
