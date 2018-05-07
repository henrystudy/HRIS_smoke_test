package com.pactera.hris.util;

import org.testng.Assert;

/**
 * import testNG, not jUnit
 * @author zhenhaiw
 *	简单封装Assert类的方法，加入log4j和失败截图
 *	测试用例中循环调用Assertion时，最后统一断言一下Assertion.flag为true即可
 *	如果需要其他方法例如Assert.assertNotNull...参阅testNG API
 */
public class Assertion 
{
	public static boolean flag = true;
	private static Log logger = new Log(Assertion.class.getName());
	
	//无截图
	public static void verifyEquals(Object actual, Object expected)
	{
		try 
		{
			Assert.assertEquals(actual, expected);
		} catch (Exception e) 
		{
			flag = false;
			logger.logError("Assertion fail, logging...");
			//Take screenshot if failure
		}
	}
	
	//无截图带message
	public static void verifyEquals(Object actual, Object expected, String message)
	{
		try 
		{
			Assert.assertEquals(actual, expected, message);
		} catch (Exception e) 
		{
			flag = false;
			logger.logError("Assertion fail, logging...");
			//Take screenshot if failure
		}
	}
	
	/*失败截图操作用TestNG监听器实现
	//有截图
	public static void verifyEquals(Object actual, Object expected, WebDriver driver)
	{
		try 
		{
			Assert.assertEquals(actual, expected);
		} catch (Exception e) 
		{
			flag = false;
			logger.logError("Assertion fail, logging...");
			Screenshot.takeScreenshot((TakesScreenshot)driver, "./test-output/screenshot");
		}
	}
	
	//有截图带message
	public static void verifyEquals(Object actual, Object expected, String message, WebDriver driver)
	{
		try 
		{
			Assert.assertEquals(actual, expected, message);
		} catch (Exception e) 
		{
			flag = false;
			logger.logError("Assertion fail, logging...");
			Screenshot.takeScreenshot((TakesScreenshot)driver, "./test-output/screenshot");
		}
	}
	*/
}
