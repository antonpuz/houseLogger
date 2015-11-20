package houseLogger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class yad2ShortEntry {
	private String houseID;
	private String kind;
	private String address;
	private String price;
	private String rooms;
	private String floor;
	private String date;
	
	public yad2ShortEntry(String id, String kind, String address, String price, 
			String rooms, String floor, String date)
	{
		houseID = id.replace("'", "");
		this.kind = kind.replace("'", "");
		this.address = address.replace("'", "");
		this.price = price.replace("'", "");
		this.rooms = rooms.replace("'", "");
		this.floor = floor.replace("'", "");
		this.date = date.replace("'", "");
	}
	
	public boolean equalsToSQLRow(ResultSet rs) throws SQLException
	{
//		System.out.println("Comparing " + kind + " and " + rs.getString("kind") + " result " +  kind.equals(rs.getString("kind")));
//		System.out.println("Comparing " + address + " and " + rs.getString("address") + " result " +  address.equals(rs.getString("address")));
//		System.out.println("Comparing " + price + " and " + rs.getString("price") + " result " +  price.equals(rs.getString("price")));
//		System.out.println("Comparing " + rooms + " and " + rs.getString("rooms") + " result " +  rooms.equals(rs.getString("rooms")));
//		System.out.println("Comparing " + floor + " and " + rs.getString("floor") + " result " +  floor.equals(rs.getString("floor")));
//		System.out.println("Comparing " + date + " and " + rs.getString("date") + " result " +  date.equals(rs.getString("date")));
		
		return (kind.equals(rs.getString("kind")) &&
				address.equals(rs.getString("address")) &&
				price.equals(rs.getString("price")) &&
				rooms.equals(rs.getString("rooms")) &&
				floor.equals(rs.getString("floor"))  );
	}
	
	public String getHouseID()
	{
		return houseID;
	}

	public String prepareInsertQuery(String table) {
		
		return String.format("insert into `%s` values (0,'%s','%s','%s','%s','%s','%s','%s')", 
				table, houseID, kind, address, price, rooms, floor, date);
		
	}
	
	
	public String toString()
	{
		return String.format("House with Yad2ID: %s, of type %s located at %s, on %s floor with %s rooms posted on %s for %s", 
				houseID, kind, address, floor, rooms, date, price);
	}
}
