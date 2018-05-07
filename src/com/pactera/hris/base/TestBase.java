package com.pactera.hris.base;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.pactera.hris.info.Config;
import com.pactera.hris.info.Global;
import com.pactera.hris.util.Log;
import com.pactera.hris.util.Util;
import com.pactera.hris.util.XmlReader;

/**
 * 提供DataProvider
 * 对testcase做一些公共处理
 * @author zhenhaiw
 *
 */
public class TestBase 
{
	protected WebDriver driver;
	private XmlReader xr;
	private Map<String, String> commonMap;
	private Log logger = new Log(this.getClass().getName());
	
	/**
	 * Load xml 文件
	 */
	private void initialPx()
	{
		if(xr==null){			
			xr = new XmlReader("test-data/"+this.getClass().getSimpleName()+".xml");
		}
	}
	
	/**
	 * 读取xml中公共节点信息,单例模式，仅为空时执行一次
	 */
	private void getCommonMap()
	{
		if(commonMap==null){			
			Element element = xr.getElementObject("/*/common");
			commonMap = xr.getChildNodesByElement(element);
		}
	}
	
	/**
	 * 这里要重点理解，读取test-data/下与测试方法同名的xml二级节点下的元素(用例需要的类似用户信息)传到@dataprovider
	 * @param method
	 * @return
	 */
	@DataProvider
    public Object[][] providerMethod(Method method)
	{	
		this.initialPx();
		this.getCommonMap();
		String methodName = method.getName();
		//获取testMethod节点，可能会多个不同配置的同名testMethod
		List<Element> elements = xr.getElementObjects("/*/"+methodName);
		Object[][] object = new Object[elements.size()][];
		for(int i=0; i<elements.size(); i++)
		{
			//取得每个TestMethod下的所有子节点再合并common节点和Global节点
			Map<String, String> mergeCommon = this.getMergeMapData(xr.getChildNodesByElement(elements.get(i)), commonMap);
			Map<String, String> mergeGlobal = this.getMergeMapData(mergeCommon, Global.global);
			Object[] temp = new Object[] {mergeGlobal};
			object[i] = temp;
		}
		return object;
	}
	
	@Parameters("browser")
	@BeforeClass
	//Bring "browser" param from TestNG @Parameters("browser")
	public void initialDriver(String browser)
	{
		SeleniumDriver selenium = new SeleniumDriver(browser);
		driver = selenium.getDriver();
	}
	
//	public void initialDriver()
//	{
//		SeleniumDriver selenium = new SeleniumDriver();
//		driver = selenium.getDriver();
//	}
	
	@AfterClass
	public void closeDriver()
	{
//		BasePage bp = new BasePage();
		if(driver!=null)
		{
			try
			{
				Thread.sleep(Config.DRIVERSLEEPTIME);
//				bp.dealPotentialAlert(driver, true);
				driver.quit();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
				logger.logError("WebDriver close 异常!");
			}
		}
	}
	
	public WebDriver getDriver()
	{
		return driver;
	}
	
	public void goTo(String url){
		driver.get(url);		
		if(Config.BROWSER.equals("chrome")){
			Util.sleep(1);
		}
	}
	
	/**
	 * 两个map合并
	 * @param map1
	 * @param map2
	 * @return
	 */
	private Map<String, String> getMergeMapData(Map<String, String> map1, Map<String, String> map2)
	{
		Iterator<String> it = map2.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = map2.get(key);
			if(!map1.containsKey(key)){
				map1.put(key, value);
			}
		}
		return map1;
	}
}
