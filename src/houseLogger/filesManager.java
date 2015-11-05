package houseLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class filesManager {
	private String fileName = null;
	private static Logger logger;
	
	public filesManager(Logger log4j, String name)
	{
		fileName = name;
		logger = log4j;
	}
	
	public Vector<yad2ShortEntry> parseFile()
	{
		if(fileName == null)
		{
			logger.info("parseFile was called with empty filename");
		}
		
		Vector<yad2ShortEntry> result = new Vector<yad2ShortEntry>();
		
		String content = file2String();
		Document htmlPage = Jsoup.parse(content);
		
		Elements elements = htmlPage.select("table.main_table tr");
		for (Element table : elements) {
			Elements TRelements = table.select("td");
			if(TRelements.size() != 20) continue;
			String fullHouseID = table.attr("id");
			
			
//			System.out.println("HouseID: " + fullHouseID.substring(fullHouseID.lastIndexOf("_") + 1));
//			System.out.println("Type: " + TRelements.get(6).text());
//			System.out.println("Address: " + TRelements.get(8).text());
//			System.out.println("Price: " + TRelements.get(10).text());
//			System.out.println("Rooms: " + TRelements.get(12).text());
//			System.out.println("Floor: " + TRelements.get(14).text());
//			System.out.println("Date: " + TRelements.get(18).text());
//			
			
			result.add(new yad2ShortEntry(fullHouseID.substring(fullHouseID.lastIndexOf("_") + 1), 
					TRelements.get(6).text(), 
					TRelements.get(8).text(), 
					TRelements.get(10).text(), 
					TRelements.get(12).text(), 
					TRelements.get(14).text(), 
					TRelements.get(18).text()));
		}
		
		
		return result;
	}
	
	
	private String file2String()
	{
		String str = null;
		String line;
		
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"));

			while ((line = br.readLine()) != null) {
				str += line;
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			logger.error(String.format("error while opening file: %s", fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(String.format("IOException was thrown while reading file"));
			logger.error(e.toString());
		}
	    
		
		return str;
	}
	
}
