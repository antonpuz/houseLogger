package houseLogger;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class stringParser {
	private String content = null;
	private static Logger logger;
	
	public stringParser(Logger log4j, String data)
	{
		content = data;
		logger = log4j;
	}
	
	public Vector<yad2ShortEntry> parseStringData()
	{
		if(content == null)
		{
			logger.info("parseStringData was called with empty content");
		}
		
		Vector<yad2ShortEntry> result = new Vector<yad2ShortEntry>();
		
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
	
	public LinkedList<String> getPages()
	{
		LinkedList<String> result = new LinkedList<String>();
		Document htmlPage = Jsoup.parse(content);
		Elements elements = htmlPage.select(".pages");
		
		for (Element table : elements) {
			Elements a = table.select("a");
			for (Element aElem : a) {
				if(aElem.text().equals("1")) continue;
				result.add("http://www.yad2.co.il" + aElem.attr("href"));
			}

		}
		
		return result;
	}
	
	
}
