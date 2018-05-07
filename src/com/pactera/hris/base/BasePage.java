package com.pactera.hris.base;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pactera.hris.info.Config;
import com.pactera.hris.util.Log;

/**
 * BaePage基类可以存放一些通用Action操作比如窗口切换，鼠标Action操作等
 * @author zhenhaiw
 *
 */

public class BasePage extends Locator
{
	private Log logger = new Log(this.getClass().getName());
	
	public BasePage()
	{
		
	}
	
	public BasePage(WebDriver driver)
	{
		super(driver);
		this.setYaml(this.getClass().getSimpleName());
		this.loadYaml();
	}
	
	protected Actions getAction(){
		return new Actions(driver);
	}
	
	/**
	 * 捕获处理页面可能出现的Alert JS 弹窗
	 * @param driver
	 * @param option true - OK, false - cancel
	 */
	
	public boolean isAlertPresent(WebDriver driver)
	{
		boolean flag = false;
		int waitTime = Config.WAITTIME;
		
		if(driver != null)
		{
			try 
			{
				Thread.sleep(Config.WAITTIME);
				new WebDriverWait(driver, waitTime).until(ExpectedConditions.alertIsPresent());
				flag = true;
			} catch (NoAlertPresentException e) 
			{
				logger.logInfo("There is no alert appear!");
				return flag;
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return flag;
	}
	
	public void dealPotentialAlert(WebDriver driver,boolean option) 
	{
		boolean flag = false;
		int waitTime = Config.WAITTIME;
		Alert alert = null;
		
		if(driver != null)
		{
			try 
			{
				Thread.sleep(Config.SLEEPTIME);
				alert = new WebDriverWait(driver, waitTime).until(ExpectedConditions.alertIsPresent());
				flag = true;
				
				if(flag)
				{
					alert = driver.switchTo().alert();
					if(option)
					{
						//注意alert.getText()放在前面，否则alert.accept()之后找不到alert就异常了
						logger.logInfo("Accept the alert: " + alert.getText());
						alert.accept();
					}else
					{
						logger.logInfo("Dismiss the alert: " + alert.getText());
						alert.dismiss();
					}
				}
			} catch (NoAlertPresentException e) 
			{
				logger.logInfo("There is no alert appear!");
			} catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else
		{
			return;
		}
	}
}
