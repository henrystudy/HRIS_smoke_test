package com.pactera.hris.base;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.pactera.hris.info.Config;
import com.pactera.hris.util.Log;

/**
 * 定义BROWSER基本操作, start(),获取driver, close()等
 * @author zhenhaiw
 *
 */
public class SeleniumDriver 
{
	private WebDriver driver;
	private Log logger = new Log(this.getClass().getName());
	
	public SeleniumDriver()
	{
		this.initDriver();
	}
	
	//Will use browser param from TestNG.xml
	public SeleniumDriver(String browser)
	{
		this.initDriver(browser);
	}
	
	public WebDriver getDriver()
	{
		return driver;
	}
	
	/**
	 * Start WebDriver with specified BROWSER in config XML
	 * @param BROWSERName defined BROWSER type, such as [firefox]/[ie32bit]/[ie64bit]/[chrome]
	 * 通过TestNG.xml传浏览器参数进来，方便执行不同浏览器
	 */
	public void initDriver(String browser)
	{
		try 
		{
			if(browser != null)
			{
				if(browser.equals("firefox32bit"))//equals是值比较, ==是地址比较
				{
					System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver_0.20.0/geckodriver_x86.exe");
					driver = new FirefoxDriver();
				}
				else if(browser.equals("firefox64bit"))
				{
					System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver_0.20.0/geckodriver_x64.exe");
					driver = new FirefoxDriver();
				}
				/**
				 * On IE 7 or higher on Windows Vista or Windows 7, you must set the Protected Mode settings for each zone to be the same value. 
				 * The value can be on or off, as long as it is the same for every zone. To set the Protected Mode settings, choose "Internet Options..." 
				 * from the Tools menu, and click on the Security tab. For each zone, there will be a check box at the bottom of the tab labeled "Enable Protected Mode".
				 * 
				 * Additionally, "Enhanced Protected Mode" must be disabled for IE 10 and higher. This option is found in the Advanced tab of the Internet Options dialog.
				 * The browser zoom level must be set to 100% so that the native mouse events can be set to the correct coordinates.
				 * 
				 * For IE 11 only, you will need to set a registry entry on the target computer 
				 * so that the driver can maintain a connection to the instance of Internet Explorer 
				 * it creates. For 32-bit Windows installations, the key you must examine in the 
				 * registry editor is HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE. 
				 * For 64-bit Windows installations, the key is HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\
				 * Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE. 
				 * Please note that the FEATURE_BFCACHE subkey may or may not be present, and should be 
				 * created if it is not present. Important: Inside this key, create a DWORD value 
				 * named iexplore.exe with the value of 0.
				 */
				else if(browser.equals("ie32bit"))
				{
					System.setProperty("webdriver.ie.driver", "./drivers/IEDriverServer_3.11.1/IEDriverServer_x86.exe");
					InternetExplorerOptions option = new InternetExplorerOptions();
					//下面两个设置在WebDrive3.0貌似不起作用，如官方说明，需要手动更改Protection Mode为一致
					//option.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					//option.setCapability("ignoreProtectedModeSettings", true);
					driver = new InternetExplorerDriver(option);
				}
				else if(browser.equals("ie64bit"))
				{
					System.setProperty("webdriver.ie.driver", "./drivers/IEDriverServer_3.11.1/IEDriverServer_x64.exe");
					InternetExplorerOptions option = new InternetExplorerOptions();
					option.requireWindowFocus();//这个设置可以把光标调出来，可以加快输入速度，要不然单个字符输入太慢
					//下面两个设置在WebDrive3.0貌似不起作用，如官方说明，需要手动更改Protection Mode为一致
					//option.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					//option.setCapability("ignoreProtectedModeSettings", true);
					driver = new InternetExplorerDriver(option);
				}
				else if(browser.equals("chrome"))
				{
					System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver_2.38/chromedriver.exe");
					driver = new ChromeDriver();
				}
				else
				{
					logger.logError("xml配置文件的BROWSER节点配置[" + Config.BROWSER + "]不规范,请重新配置!");
				}
				driver.manage().window().maximize();
				driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
				logger.logInfo("浏览器初始化-ok!");
			}
		} catch (Exception e) 
		{
			logger.logError("浏览器初始化异常: " + this.getClass().getName());;
		}
	}
	
	//使用Config.BROWSER调用浏览器参数
	public void initDriver()
	{
		try 
		{
			if(Config.BROWSER != null)
			{
				if(Config.BROWSER.equals("firefox32bit"))//equals是值比较, ==是地址比较
				{
					System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver_0.20.0/geckodriver_x86.exe");
					driver = new FirefoxDriver();
				}
				else if(Config.BROWSER.equals("firefox64bit"))
				{
					System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver_0.20.0/geckodriver_x64.exe");
					driver = new FirefoxDriver();
				}
				/**
				 * On IE 7 or higher on Windows Vista or Windows 7, you must set the Protected Mode settings for each zone to be the same value. 
				 * The value can be on or off, as long as it is the same for every zone. To set the Protected Mode settings, choose "Internet Options..." 
				 * from the Tools menu, and click on the Security tab. For each zone, there will be a check box at the bottom of the tab labeled "Enable Protected Mode".
				 * 
				 * Additionally, "Enhanced Protected Mode" must be disabled for IE 10 and higher. This option is found in the Advanced tab of the Internet Options dialog.
				 * The browser zoom level must be set to 100% so that the native mouse events can be set to the correct coordinates.
				 * 
				 * For IE 11 only, you will need to set a registry entry on the target computer 
				 * so that the driver can maintain a connection to the instance of Internet Explorer 
				 * it creates. For 32-bit Windows installations, the key you must examine in the 
				 * registry editor is HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE. 
				 * For 64-bit Windows installations, the key is HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\
				 * Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE. 
				 * Please note that the FEATURE_BFCACHE subkey may or may not be present, and should be 
				 * created if it is not present. Important: Inside this key, create a DWORD value 
				 * named iexplore.exe with the value of 0.
				 */
				else if(Config.BROWSER.equals("ie32bit"))
				{
					System.setProperty("webdriver.ie.driver", "./drivers/IEDriverServer_3.11.1/IEDriverServer_x86.exe");
					InternetExplorerOptions option = new InternetExplorerOptions();
					//下面两个设置在WebDrive3.0貌似不起作用，如官方说明，需要手动更改Protection Mode为一致
					//option.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					//option.setCapability("ignoreProtectedModeSettings", true);
					driver = new InternetExplorerDriver(option);
				}
				else if(Config.BROWSER.equals("ie64bit"))
				{
					System.setProperty("webdriver.ie.driver", "./drivers/IEDriverServer_3.11.1/IEDriverServer_x64.exe");
					InternetExplorerOptions option = new InternetExplorerOptions();
					option.requireWindowFocus();//这个设置可以把光标调出来，可以加快输入速度，要不然单个字符输入太慢
					//下面两个设置在WebDrive3.0貌似不起作用，如官方说明，需要手动更改Protection Mode为一致
					//option.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					//option.setCapability("ignoreProtectedModeSettings", true);
					driver = new InternetExplorerDriver(option);
				}
				else if(Config.BROWSER.equals("chrome"))
				{
					System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver_2.38/chromedriver.exe");
					driver = new ChromeDriver();
				}
				else
				{
					logger.logError("xml配置文件的BROWSER节点配置[" + Config.BROWSER + "]不规范,请重新配置!");
				}
				driver.manage().window().maximize();
				driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
				logger.logInfo("浏览器初始化-ok!");
			}
		} catch (Exception e) 
		{
			logger.logError("浏览器初始化异常: " + this.getClass().getName());;
		}
	}
	
	/**
	 * Initial specific WebDriver with specific pre-setting
	 */
	public void initSpecifiedDriver()
	{
		try 
		{
			if(Config.BROWSER != null)
			{
				if(Config.BROWSER.equals("specifiedFirefox"))//equals是值比较, ==是地址比较
				{
//					两种导入proxy方式都可以
//					FirefoxProfile profile = new FirefoxProfile();
//					String proxyAdr = "web-proxy.cn.hpicorp.net";
//					int proxyPort = 8080;
//					
////					profile.setPreference("network.proxy.type", 1);
//					profile.setPreference("network.proxy.http", proxyAdr);
//					profile.setPreference("network.proxy.http_port", proxyPort);
//					profile.setPreference("network.proxy.ftp", proxyAdr);
//					profile.setPreference("network.proxy.ftp_port", proxyPort);
//					profile.setPreference("network.proxy.ssl", proxyAdr);
//					profile.setPreference("network.proxy.ssl_port", proxyPort);
//					profile.setPreference("network.proxy.share_proxy_settings", true);
//
//					driver = new FirefoxDriver(profile);
					String prx="web-proxy.cn.hpicorp.net:8080";
				    Proxy proxy = new Proxy();
				    proxy.setHttpProxy(prx).setFtpProxy(prx).setSslProxy(prx);
				    FirefoxOptions option= new FirefoxOptions();
				    option.setCapability(CapabilityType.PROXY,proxy);

				    driver = new FirefoxDriver(option);
				}
				else if(Config.BROWSER.equals("ie32bit"))
				{
					System.setProperty("webdrier.ie.driver", "./files/IEDriverServer_Win32_2.53.1/IEDriverServer.exe");
					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					capabilities.setCapability("ignoreProtectedModeSettings", true);
					driver = new InternetExplorerDriver();
				}
				else if(Config.BROWSER.equals("ie64bit"))
				{
					System.setProperty("webdriver.ie.driver", "./files/IEDriverServer_x64_2.53.1/IEDriverServer.exe");
					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					capabilities.setCapability("ignoreProtectedModeSettings", true);
					driver = new InternetExplorerDriver();
				}
				else if(Config.BROWSER.equals("chrome"))
				{
					System.setProperty("webdriver.chrome.driver", "./files/chromedriver_win32/chromedriver.exe");
					driver = new ChromeDriver();
				}
				else
				{
					logger.logError("xml配置文件的BROWSER节点配置[" + Config.BROWSER + "]不规范,请重新配置!");
				}
				driver.manage().window().maximize();
				driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
				logger.logInfo("浏览器初始化-ok!");
			}
		} catch (Exception e) 
		{
			logger.logError("浏览器初始化异常: " + this.getClass().getName());;
		}
	}
	
	public void close()
	{
//		driver.close();
		try
		{
			Thread.sleep(5000);
			driver.quit();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			logger.logError("WebDriver close 异常!");
		}
	}
}
