package houseLogger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;

public class houseLogger {
	
	private static Logger logger;
	private static databaseManager dm;
	
	public static void main(String[] args) {
		//initialize all global items
		init();
		logger.error("######    Started a new run    #######");
		
		//start working
		dm.run();
		
		filesManager fileParser = new filesManager(logger, "2.html");
		Vector<yad2ShortEntry> v = fileParser.parseFile();
		
		dm.updateQueries(v);
		
		
		logger.error("######    Finished data processing    #######");
	}
	
	private static void init()
	{
		logger=Logger.getLogger("LoggingExample");
		dm = new databaseManager(logger);
	}

}
