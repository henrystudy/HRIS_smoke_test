package com.pactera.hris.info;

import com.pactera.hris.util.XmlReader;

/**
 * 从XML配置文件读取配置信息, 例如browser, OS等等, 并赋值到静态变量
 * @author zhenhaiw
 *
 */

public class Config 
{
	public static String BROWSER;
	public static int WAITTIME;
	public static int SLEEPTIME;
	public static int DRIVERSLEEPTIME;
	/**
	 * static{}，这种用法请大家务必搞清楚，这代表在用到Config这个类时，这个static{}里面的内容会被执行一次，且只被执行一次，就算多
	 * 次用到Config类，也只执行一次，所以，这个static{]一般就在加载一些配置文件，也可以说类似于单例模式。
	 */
	static
	{
		String path = "./config/Config.xml";
		XmlReader xr = new XmlReader(path);
		BROWSER = xr.getElementText("/config/browser");
		WAITTIME = Integer.valueOf(xr.getElementText("/config/waitTime"));
		SLEEPTIME = Integer.valueOf(xr.getElementText("/config/sleepTime"));
		DRIVERSLEEPTIME = Integer.valueOf(xr.getElementText("/config/driverSleepTime"));
	}
	
	public static void main(String[] args)
	{
		System.out.println(Config.BROWSER + Config.WAITTIME);
	}
}
