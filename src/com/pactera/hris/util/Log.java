package com.pactera.hris.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;

/**
 * Log加入className Tag
 * 加入testNG Log
 * @author zhenhaiw
 *
 */

public class Log 
{
	private Logger logger;
	private String logTag;
	
	public Log(String logTag)
	{
		this.logTag = logTag;
		logger = LogManager.getLogger(this.logTag);
	}
	
	public void logInfo(Object message)
	{
		logger.info(message);
		Reporter.log(new TimeString().getSimpleDateFormat()+" : " + message);
	}
	
	public void logWarn(Object message)
	{
		logger.warn(message);
		Reporter.log(new TimeString().getSimpleDateFormat()+" : " + message);
	}
		
	public void logError(Object message)
	{
		logger.error(message);
		Reporter.log(new TimeString().getSimpleDateFormat()+" : " + message);
	}
}
