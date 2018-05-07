package com.pactera.hris.info;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class TestOnly
{
	public static void main(String[] args)
	{
		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver_0.20.0/geckodriver_x86.exe");
		WebDriver driver = new FirefoxDriver();
		
		try {
			Thread.sleep(1000);;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		driver.close();
		driver.quit();
	}
}
