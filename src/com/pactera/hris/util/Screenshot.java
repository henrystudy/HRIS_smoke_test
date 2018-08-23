package com.pactera.hris.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;

public class Screenshot 
{
	private static String screenshotName;
	private static String screenshotPath;
	
	public static void takeScreenshot(TakesScreenshot driver, String folderPath)
	{
		Log logger = new Log(Screenshot.class.getName());
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
		String currentTime = format.format(new Date().getTime());
		try 
		{
			File source = driver.getScreenshotAs(OutputType.FILE);
			File desFolder = new File(folderPath);
			screenshotPath = desFolder.getAbsolutePath() + "/";
			screenshotName = currentTime + ".jpg";
			File destination = new File(screenshotPath + screenshotName);
			if(!desFolder.exists())
				desFolder.mkdirs();
			FileUtils.copyFile(source, destination);
			
			//destination.getAbsolutePath()，直接使用会存在转义的问题
			//window.open()会被有些浏览器block，需要看其他的实现方式，比如缩略图放大等等
			logger.logInfo("<img src='"+ destination.getAbsolutePath() + "' onclick=\"window.open('D:/eclipse_workspace/ad smoke test/test-output/screenshot/" + screenshotName + "')\" height='100' width='100'/>");
//			logger.logInfo("<a href="+ destination.getAbsolutePath() + "><img src='"+ destination.getAbsolutePath() + "' height='100' width='100'/></a>");
		} catch (WebDriverException e) 
		{
			logger.logError("Failed to take screenshot!");
		} catch (IOException e) 
		{
			logger.logError("Failed to save screenshot!");
		}
//		finally
//		{
//			logger.info("Screenshot is saved!");
//		}
	}
	
	public static String getScreenshotPath()
	{
		return screenshotPath;
	}
	
	public static String getScreenshotName()
	{
		return screenshotName;
	}
}
