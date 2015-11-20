package houseLogger;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

//TODO: Add log handling
public class webRobot {	
	private static webRobot singRobot = null;
	private Robot robot = null;
	
	
	public String getSource(String url)
	{
		changeWindow(1);
		setLink(url);
		viewSource();
		copySourceCode();
		changeWindow(1);
		
		sleep(100);
		String data;
		try {
			data = (String) Toolkit.getDefaultToolkit()
			        .getSystemClipboard().getData(DataFlavor.stringFlavor);
			return data;
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	
	public void testingFunc()
	{
		changeWindow(1);
		setLink("https://www.facebook.com/");
		viewSource();
		copySourceCode();
		changeWindow(1);
		
		sleep(100);
		String data;
		try {
			data = (String) Toolkit.getDefaultToolkit()
			        .getSystemClipboard().getData(DataFlavor.stringFlavor);
			System.out.println(data);
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
        //sleep(1000);
        //robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        
        
		
		sleep(2000);
    	Point myP = MouseInfo.getPointerInfo().getLocation();
    	System.out.println("The position: " + myP);

    	Rectangle area = new Rectangle(myP.x, myP.y, 300, 300);
	}
	
	private void setLink(String string) {
		robot.mouseMove(247, 36); //go to link line
		
		//Paste the link
		sleep(50);
		StringSelection stringSelection = new StringSelection(string);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
        sleep(100);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		sleep(50);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        
        robot.keyPress(KeyEvent.VK_CONTROL);
		sleep(50);
		robot.keyPress(KeyEvent.VK_V);
		sleep(50);
		robot.keyRelease(KeyEvent.VK_V);
		sleep(50);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		sleep(50);
		robot.keyPress(KeyEvent.VK_ENTER);
		sleep(50);
		robot.keyRelease(KeyEvent.VK_ENTER);
		sleep(10000);
		
	}

	private void copySourceCode()
	{
		robot.mouseMove(30, 529); //set to code start
		sleep(300);
		robot.mouseWheel(-4000);
		sleep(300);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		sleep(50);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        sleep(500);
        
        robot.mouseMove(35, 283); //set to edit as html
        sleep(50);
        
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		sleep(50);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        sleep(5000);
        
        robot.keyPress(KeyEvent.VK_CONTROL);
		sleep(50);
        robot.keyPress(KeyEvent.VK_A);
		sleep(50);
		robot.keyRelease(KeyEvent.VK_A);
		sleep(50);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		sleep(3000);
        
        robot.keyPress(KeyEvent.VK_CONTROL);
		sleep(50);
        robot.keyPress(KeyEvent.VK_C);
		sleep(50);
		robot.keyRelease(KeyEvent.VK_C);
		sleep(50);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		sleep(3000);
	}
	
	private void viewSource()
	{
		robot.mouseMove(10, 91); //set to code start
		sleep(50);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		sleep(50);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        sleep(50);
        
        
        robot.mouseMove(95, 317); //set to code start
        sleep(50);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		sleep(50);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        sleep(5000);
	}
	
	private void changeWindow(int times)
	{
		robot.keyPress(KeyEvent.VK_ALT);
		sleep(50);
		for(int i=0;i<times;i++)
		{
			robot.keyPress(KeyEvent.VK_TAB);
			sleep(50);
			robot.keyRelease(KeyEvent.VK_TAB);
			sleep(50);
		}
		robot.keyRelease(KeyEvent.VK_ALT );
	}
	
	public static webRobot getRobot() throws AWTException
	{
		if(singRobot == null)
		{
			singRobot = new webRobot();
		}
		return singRobot;
	}
	
	//Single constructor
	private webRobot() throws AWTException
	{
		robot = new Robot();
		System.out.println("Initializing an instance of a robot!");
	}
	
	protected static void sleep(int miliseconds)
	{
		try {
		  Thread.sleep(miliseconds);
		} catch (InterruptedException ie) {
		    //Handle exception
		}
	}
}
