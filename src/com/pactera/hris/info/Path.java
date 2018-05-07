package com.pactera.hris.info;

import com.pactera.hris.util.XmlReader;

public class Path 
{
	public static String SCREENSHOTPATH;
	
	static
	{
		String path = "./config/Path.xml";
		XmlReader xr = new XmlReader(path);
		SCREENSHOTPATH = xr.getElementText("/path/screenshotPath");
	}
}
