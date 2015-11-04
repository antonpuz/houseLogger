package houseLogger;

import org.apache.log4j.Logger;

public class houseLogger {
	
	private static Logger logger=Logger.getLogger("LoggingExample");
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Hello my kind friends");
		
		logger.error("######    Started a new run    #######");
		
		
	}

}
