package com.pactera.hris.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ho.yaml.Yaml;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pactera.hris.info.Config;
import com.pactera.hris.util.Log;

/**
 * 这一节涉及代码多次重构，多看几遍消化吸收
 * @author zhenhaiw
 *
 */

public class Locator 
{
//	private String yamlFile = this.getClass().getSimpleName();//yamlFile通常是Page类调用时的类名，用于查找同名的yaml文件,这个事情也可以拆分到BasePage去做匹配
	private String yamlFile;
	protected WebDriver driver;
	private Map<String, Map<String, String>> ml;//基于yaml文件格式，一层map是别名，二层map是key, value
	private Map<String, Map<String, String>> extendMl;
	private Log logger = new Log(this.getClass().getName());
	
	public Locator()
	{
		
	}
	
	public Locator(WebDriver driver)
	{
		this.driver = driver;
//		this.loadYaml();//拆分放到BasePage去做了
	}
	
	//Usage: Set yaml file to same name with Pagexx
	public void setYaml(String yamlFile)
	{
		this.yamlFile = yamlFile;
	}

	//Load yaml file	
	@SuppressWarnings("unchecked")
	protected void loadYaml()
	{
		File file = new File("locator/" + yamlFile + ".yaml");
		if(file != null && file.exists())
		{
			try
			{
				//类名.class 区别  new类.getClass()
				ml = Yaml.loadType(new FileInputStream(file.getAbsolutePath()), HashMap.class);
//				ml = Yaml.loadType(file, HashMap.class);
			}catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}else
		{
			logger.logError("Unable to find yaml file: " + file.getAbsolutePath() + ", please check.");
		}
	}
	
	/**
	 * Load multiple yaml files
	 * 比如，有一些元素对象是每个页面都会出现的，是公共的，这些公共的locator只是有时候要用到，大部分时候都不用，
	 * 所以，我们把这些公共的放在一个特定的文件里，在需要的时候通过外部加载来使用这些公共的locator,增加一个变量与方法
	 */
	@SuppressWarnings("unchecked")
	public void loadExtendYaml(String aYaml)
	{
		File file = new File(aYaml);
		if(file != null && file.exists())
		{
			try
			{
				extendMl = Yaml.loadType(new FileInputStream(file), HashMap.class);
				ml.putAll(extendMl);
			}catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}else
		{
			logger.logError("Unable to find yaml file: " + file.getAbsolutePath() + ", please check.");
		}
	}
	
	//封装By, 根据type判断取相应的value, driver.findElement(By类型)
	public By getBy(String type, String value)
	{
		By by = null;
		if(type.equals("id"))
		{
			by = By.id(value);
		}
		if(type.equals("name"));
		{
			by = By.name(value);
		}
		if(type.equals("className"))
		{
			by = By.className(value);
		}
		if(type.equals("linkText"))
		{
			by = By.linkText(value);
		}
		if(type.equals("xpath"))
		{
			by = By.xpath(value);
		}
		return by;
	}
	
	/*Phase1 - 没有元素等待
	public WebElement getWebElement(String key)//ml的第一层yaml别名，对象库的概念
	{
		String type = ml.get(key).get("type");
		String value = ml.get(key).get("value");
		WebElement element = driver.findElement(this.getBy(type, value));
		
		return element;
	}
	*/
	/*Phase2 - 加入元素等待getWaitingElement()，但没有判断是不是抓到的元素是隐藏元素
	public WebElement getWebElement(String key)//ml的第一层yaml别名，对象库的概念
	{
		String type = ml.get(key).get("type");
		String value = ml.get(key).get("value");
		WebElement element = this.getWaitingElement(this.getBy(type, value));
		
		return element;
	}
	*/
	/*Phase3 - 加入元素等待，并判断是不是隐藏元素waitIfElementBeDisplayed()，是隐藏元素则置空，正常显示则返回元素
	public WebElement getWebElement(String key)//ml的第一层yaml别名，对象库的概念
	{
		String type = ml.get(key).get("type");
		String value = ml.get(key).get("value");
		WebElement element = this.getWaitingElement(this.getBy(type, value));
		
		if(!this.waitIfElementBeDisplayed(element))
		{
			element = null;
		}
		
		return element;
	}
	*/
	
	/*Phase4 如果要判断元素不出现在页面上的处理
	private WebElement getElementNoWait(String key)
	{
		String type = ml.get(key).get("type");
		String value = ml.get(key).get("value");
		WebElement element = null;
		try
		{
			element = driver.findElement(this.getBy(type, value));
		}catch(Exception e)
		{
			element = null;
		}
		return element;
	}
	*/
	
	/*Phase5 发现Phase4 getElementNoWait() 和 Phase3 getElement()有很多重复代码，重构一下
	private WebElement getLocator(String key, boolean wait)
	{
		WebElement element = null;
		
		if(ml.containsKey(key))
		{
			String type = ml.get(key).get("type");
			String value = ml.get(key).get("value");
			By by = this.getBy(type, value);
			
			if(wait)
			{
				element = this.getWaitingElement(by);
				boolean flag = !this.waitIfElementBeDisplayed(element);
				if(!flag)
				{
					element = null;
				}
			}else
			{
				try 
				{
					element = driver.findElement(by);
				}catch(Exception e)
				{
					element = null;
				}
			}
		}else
		{
			logger.logError("Locator" + key + " is not exists in " + yamlFile + ".yaml");
		}
		return element;
	}
	*/
	
	/*Phase6 根据重构的方法getLocator()修改对应的getElement() / getElementNoWait()
	private WebElement getElement(String key)
	{
		return this.getLocator(key, true);
	}
	
	private WebElement getElementNoWait(String key)
	{
		return this.getLocator(key, false);
	}
	*/
	
	/**
	 * Phase7 yaml value参数化(使用通配符替换可能会变动的关键值)
	 * ，调用的时候直接替换使用
	 * @param key
	 * @param replace
	 * @param wait
	 * @return
	 */
	private WebElement getLocator(String key, String[] replace, boolean wait)
	{
		WebElement element = null;
		
		if(ml.containsKey(key))
		{
			String type = ml.get(key).get("type");
			String value = ml.get(key).get("value");
			if(replace != null)
			{
				value = this.getReplacedLocatorStrings(value, "%s", replace);
				logger.logInfo("获取页面元素地址value为: " + value);
			}
			By by = this.getBy(type, value);
			
			if(wait)
			{
				//这里加入了短暂的页面等待时间，可以避免很多奇怪的问题
				try 
				{
					Thread.sleep(Config.SLEEPTIME);
				} catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				element = this.getWaitingElement(by);
				boolean flag = this.waitIfElementBeDisplayed(element);
				if(!flag)
				{
					element = null;
				}
			}else
			{
				try 
				{
					Thread.sleep(Config.SLEEPTIME);
					element = driver.findElement(by);
				}catch(Exception e)
				{
					element = null;
				}
			}
		}else
		{
			logger.logError("Locator " + key + " is not exists in " + yamlFile + ".yaml");
		}
		return element;
	}
	
	/**
	 * Phase7 yaml value参数化(使用通配符替换可能会变动的关键值)
	 * ，调用的时候直接替换使用
	 * List - 抓取元素列表，比如一些非正规Select的下拉菜单等
	 * @param key
	 * @param replace
	 * @param wait
	 * @return
	 */
	private List<WebElement> getLocators(String key, String[] replace, boolean wait)
	{
		List<WebElement> elements = null;
		
		if(ml.containsKey(key))
		{
			String type = ml.get(key).get("type");
			String value = ml.get(key).get("value");
			if(replace != null)
			{
				value = this.getReplacedLocatorStrings(value, "%s", replace);
			}
			By by = this.getBy(type, value);
			
			if(wait)
			{
				try 
				{
					Thread.sleep(Config.SLEEPTIME);
				} catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				elements = this.getWaitingElements(by);
				boolean flag = this.waitIfElementsBeDisplayed(elements);
				if(!flag)
				{
					elements = null;
				}
			}else
			{
				try 
				{
					Thread.sleep(Config.SLEEPTIME);
					elements = driver.findElements(by);
				}catch(Exception e)
				{
					elements = null;
				}
			}
		}else
		{
			logger.logError("Locator " + key + " is not exists in " + yamlFile + ".yaml");
		}
		return elements;
	}
	
	/**
	 * Phase8 重新调整一下getElement() / getElementNoWait(),加入参数化
	 * @param key
	 * @return
	 */
	public WebElement getElement(String key)
	{
		return this.getLocator(key, null, true);
	}
	
	public WebElement getElementNoWait(String key)
	{
		return this.getLocator(key, null, false);
	}
	
	public WebElement getElement(String key, String[] replace)
	{
		return this.getLocator(key, replace, true);
	}
	
	public WebElement getElementNoWait(String key, String[] replace)
	{
		return this.getLocator(key, replace, false);
	}
	
	/**
	 * Phase8 重新调整一下getElement() / getElementNoWait(),加入参数化
	 * List - 对元素列表操作
	 * @param key
	 * @return
	 */
	public List<WebElement> getElements(String key)
	{
		return this.getLocators(key, null, true);
	}
	
	public List<WebElement> getElementsNoWait(String key)
	{
		return this.getLocators(key, null, false);
	}
	
	public List<WebElement> getElements(String key, String[] replace)
	{
		return this.getLocators(key, replace, true);
	}
	
	public List<WebElement> getElementsNoWait(String key, String[] replace)
	{
		return this.getLocators(key, replace, false);
	}
	
	/**
	 * 动态等待waitTime获取element
	 * @param by
	 * @return
	 */
	private WebElement getWaitingElement(final By by)//final, 只是等待by的element，没有更改的必要
	{
		WebElement element = null;
		int waitTime = Config.WAITTIME;
		
		try
		{
			element = (new WebDriverWait(driver,waitTime)).until(ExpectedConditions.presenceOfElementLocated(by));
			/*这是原写法，上面写法是否可行，并未验证，如果不行改用下面写法
			element = new WebDriverWait(driver, waitTime).until(new ExpectedCondition<WebElement>()
				{
					public WebElement apply(WebDriver d)
					{
						return d.findElement(by);
					}
				});
			*/
		}catch(Exception e)
		{
			logger.logError("Unable to catch " + by.toString() + " until " + waitTime + " s.");
		}
		
		return element;
	}
	
	/**
	 * 动态等待waitTime获取element
	 * List - 抓取列表
	 * @param by
	 * @return
	 */
	private List<WebElement> getWaitingElements(final By by)//final, 只是等待by的element，没有更改的必要
	{
		List<WebElement> elements = null;
		int waitTime = Config.WAITTIME;
		
		try
		{
			elements = new WebDriverWait(driver, waitTime).until(new ExpectedCondition<List<WebElement>>()
				{
					public List<WebElement> apply(WebDriver d)
					{
						return d.findElements(by);
					}
				});
		}catch(Exception e)
		{
			logger.logError("Unable to catch " + by.toString() + " until " + waitTime + " s.");
		}
		
		return elements;
	}
	
	/**
	 * 判断元素是不是等待可见(首先基于前提是元素是能定位到的)
	 * @param element
	 * @return
	 */
	public boolean waitIfElementBeDisplayed(final WebElement element)
	{
		boolean wait = false;
		int waitTime = Config.WAITTIME;
		
		if(element == null)
		{
			return wait;
		}
		try
		{
			wait = new WebDriverWait(driver, waitTime).until(new ExpectedCondition<Boolean>()
				{
					public Boolean apply(WebDriver d)	
					{
						return element.isDisplayed();
					}
				});
		}catch(Exception e)
		{
			logger.logError(element.toString() + " is not displayed.");
		}
		return wait;
	}
	
	/**
	 * 判断元素是不是等待可见(首先基于前提是元素是能定位到的)
	 * List - 判断元素列表是不是显示
	 * @param element
	 * @return
	 */
	public boolean waitIfElementsBeDisplayed(final List<WebElement> elements)
	{
		boolean wait = false;
		int waitTime = Config.WAITTIME;
		
		if(elements == null)
		{
			return wait;
		}
		try
		{
			wait = new WebDriverWait(driver, waitTime).until(new ExpectedCondition<Boolean>()
			{
				public Boolean apply(WebDriver d)	
				{
					boolean flag = true;
					for(WebElement e : elements)
					{
						if(!e.isDisplayed())
						{
							flag = false;
							break;
						}
					}
					return flag;
				}
			});
		}catch(Exception e)
		{
			logger.logError(elements.toString() + " is not displayed.");
		}
		return wait;
	}
	
	//判断元素是不是等待不可见(首先基于前提是元素是能定位到的)
	@SuppressWarnings("unused")
	private boolean waitIfElementBeHidden(final WebElement element)
	{
		boolean wait = false;
		int waitTime = Config.WAITTIME;
		
		if(element == null)
		{
			return wait;
		}
		wait = new WebDriverWait(driver, waitTime).until(new ExpectedCondition<Boolean>()
				{
					public Boolean apply(WebDriver d)
					{
						return !element.isDisplayed();
					}
				});
		
		return wait;
	}
	
	//替换字符串中的关键字(用关键字通配符KeyWord参数化/替换yaml中的value)
	@SuppressWarnings("unused")
	private String getReplacedLocatorString(String OriginalStr, String KeyWord, String ReplaceStr)
	{
		OriginalStr = OriginalStr.replaceAll(KeyWord,ReplaceStr);
		return OriginalStr;
	}
	
	//如果要替换多个关键字成拼接字符串例如%s%s > kw + 1，使用字符串数组
	private String getReplacedLocatorStrings(String OriginalStr, String KeyWord, String[] ReplaceStr)
	{
		for(String s : ReplaceStr)
		{
			OriginalStr = OriginalStr.replaceFirst(KeyWord, s);
		}
		return OriginalStr;
	}
	
	//用来批量替换yaml文件中多个value中的定义的统一变量，比如%productID%之类
	@SuppressWarnings("unused")
	private void replaceValueSet(String Variables, String replace)
	{
		Set<String> keys = ml.keySet(); 
		for(String key : keys)
		{
			System.out.println(ml.get(key).get("value"));
			String value = ml.get(key).get("value").replaceAll(Variables, replace);
			ml.get(key).put("value", value);
			System.out.println(ml.get(key).get("value"));
		}
	}
}
