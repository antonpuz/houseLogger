package houseLogger;

import org.apache.log4j.Logger;

public class houseLogger {
	
	private static Logger logger;
	private static databaseManager dm;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		init();
		System.out.println("Hello my kind friends");
		
		dm.run();
		
		logger.error("######    Started a new run    #######");
		
		
	}
	
	private static void init()
	{
		logger=Logger.getLogger("LoggingExample");
		dm = new databaseManager(logger);
	}

}
