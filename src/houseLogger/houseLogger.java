package houseLogger;

import java.awt.AWTException;
import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import org.apache.log4j.Logger;

public class houseLogger {
	
	private static Logger logger;
	private static databaseManager dm;
	private static HashMap<String, String> area2url;
	
	public static void main(String[] args) {
		//initialize all global items
		init();
		logger.error("######    Started a new run    #######");
		//start working
		dm.run();
				
		//Enable two working modes:
		//1 = Reading from input file
		//2 = Running automatic crawl by yad2 link
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Choose crawling type:");
		System.out.println("1: Read content from file");
		System.out.println("2: Read content from web page");
		int myint = keyboard.nextInt();
		
		Vector<yad2ShortEntry> v = null;
		switch(myint)
		{
		case 1:
			String fileNameWithSource = "3.html";
			System.out.println("Reading content from file: " + fileNameWithSource);
			filesManager fileParser = new filesManager(logger, fileNameWithSource);
			v = fileParser.parseFile();
			
			if(v != null)
				dm.updateQueries(v, "shortinfo");
			
//			//check for additional pages
//			LinkedList<String> links2 = fileParser.getPages();
//			ListIterator<String> lsti2 = links2.listIterator();
//			while(lsti2.hasNext())
//			{
//				String url = lsti2.next();
//				System.out.println(url);
//			}
			break;
		case 2:
			//String websiteWithSource = "http://www.yad2.co.il/Nadlan/sales.php?City=%E1%E0%F8+%F9%E1%F2&Neighborhood=%F8%EE%E5%FA&HomeTypeID=&fromRooms=&untilRooms=&fromPrice=100000&untilPrice=1600000&PriceType=1&FromFloor=-1&ToFloor=-1&Info=";
			String websiteWithSource;
			HashMap<Integer, String> areas = new HashMap<Integer, String>();
			HashMap<Integer, String> areaName = new HashMap<Integer, String>();
			LinkedList<String> databaseHousesID = null;
			
			System.out.println("Choose the wanted area: ");
			int areaID = 0;
			for (Map.Entry<String, String> entry : area2url.entrySet())
			{
			    System.out.println(areaID + ": " + entry.getKey());
			    areaName.put(areaID, entry.getKey());
			    areas.put(areaID++, entry.getValue());
			}
			
			areaID = keyboard.nextInt();
			if(areaID > areas.size() || areaID < 0)
			{
				System.out.println("Area ID doesnt exist");
				return;
			}
			websiteWithSource = areas.get(areaID);
			
			System.out.println("Reading content from website: " + websiteWithSource);
			webRobot robot;
			try {
				robot = webRobot.getRobot();
				String data = robot.getSource(websiteWithSource);
				stringParser strParser = new stringParser(logger, data);
				v = strParser.parseStringData();
				databaseHousesID = dm.getHouseIDs(areaName.get(areaID));
				
				if(v == null)
					System.out.println("Something went wrong with parseStringData");
					
				dm.updateQueries(v, areaName.get(areaID));
				yad2ShortEntry.removeIDsFromList(v, databaseHousesID);
				
				//check for additional pages
				LinkedList<String> links = strParser.getPages();
				ListIterator<String> lsti = links.listIterator();
				Random rn = new Random();
				while(lsti.hasNext())
				{
					webRobot.sleep(10000 + rn.nextInt(10000));
					
					String url = lsti.next();
					System.out.println(url);
					
					data = robot.getSource(url);
					strParser = new stringParser(logger, data);
					v = strParser.parseStringData();
					
					if(v == null)
						System.out.println("Something went wrong with parseStringData");
					
					dm.updateQueries(v, areaName.get(areaID));
					yad2ShortEntry.removeIDsFromList(v, databaseHousesID);
				}
				
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				v = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!databaseHousesID.isEmpty())
			{
				dm.updateDeletedEntries(databaseHousesID, areaName.get(areaID));
			}
			
			break;
		default:
			System.out.println("Invalid entry");
			return;
		}
		

		
		
		logger.error("######    Finished data processing    #######");
	}
	
	private static void init()
	{
		logger=Logger.getLogger("LoggingExample");
		dm = new databaseManager(logger);
		
		area2url = new HashMap<String, String>();
		area2url.put("ramot", "http://www.yad2.co.il/Nadlan/sales.php?City=%E1%E0%F8+%F9%E1%F2&Neighborhood=%F8%EE%E5%FA&HomeTypeID=&fromRooms=&untilRooms=&fromPrice=100000&untilPrice=1600000&PriceType=1&FromFloor=-1&ToFloor=-1&Info=");
	}

}
