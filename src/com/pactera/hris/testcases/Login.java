package com.pactera.hris.testcases;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.pactera.hris.base.TestBase;
import com.pactera.hris.info.Config;
import com.pactera.hris.pages.HomePage;
import com.pactera.hris.pages.LoginPage;
import com.pactera.hris.util.Assertion;
import com.pactera.hris.util.Log;
import com.pactera.hris.util.TestRetryListener;

/**
 * 登录验证
 * 1) 不同缺陷用户登陆成功验证
 * 2) 登录错误提示信息验证
 * 3) 提供其他测试用例使用的preLogin()方法
 * @author zhenhaiw
 *
 */

@Listeners({ TestRetryListener.class }) 
public class Login extends TestBase
{
	public Login()
	{
		//防止TestNG会去读取带参数constructor报错
	}
	
	public Login(WebDriver driver)
	{
		this.driver = driver;
	}
	
	
	Log logger = new Log(this.getClass().getName());
	
	/**
	 * 这一部分Login是抽出来公共部分给其他需要登录的TestCase使用
	 * @param param
	 */
	public static void preLogin(WebDriver driver, Map<String, String> param)
	{
		Log logger = (new Login()).logger;
		logger.logInfo("打开登录页面: " + param.get("url"));
		driver.get(param.get("url"));
		
		LoginPage lp = new LoginPage(driver);
		logger.logInfo("输入登录用户名为: " + param.get("username"));
		lp.getElement("login_name").sendKeys(param.get("username"));
		logger.logInfo("输入登录密码为: " + param.get("password"));
		lp.getElement("login_pwd").sendKeys(param.get("password"));
		logger.logInfo("点击登录按钮...");
		lp.getElement("login_button").click();
	}
	
	/**
	 * 抽出来logout()公共部分给其他TestCase调用
	 * @param driver
	 */
	public static void logout(WebDriver driver)
	{
		Log logger = (new Login()).logger;
		
		HomePage hp = new HomePage(driver);
		hp.getElement("personal_panel").click();
		logger.logInfo("点击退出登录...");
		hp.getElement("logout").click();
		logger.logInfo("点击确认退出弹窗...");
		logger.logInfo("有退出登录弹窗提醒? " + hp.isAlertPresent(driver));
		hp.dealPotentialAlert(driver, true);
		
		//防止循环用例调用时新开instance报错
		try {
			Thread.sleep(Config.SLEEPTIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 测试正常登录
	 * @param param
	 */
	@Test(dataProvider = "providerMethod")
	private void testLogin(Map<String, String> param)
	{
//		Assertion.flag = false;
		logger.logInfo("打开登录页面: " + param.get("url"));
		driver.get(param.get("url"));
		
		LoginPage lp = new LoginPage(driver);
		logger.logInfo("输入登录用户名为: " + param.get("username"));
		lp.getElement("login_name").sendKeys(param.get("username"));
		logger.logInfo("输入登录密码为: " + param.get("password"));
		lp.getElement("login_pwd").sendKeys(param.get("password"));
		logger.logInfo("点击登录按钮...");
		lp.getElement("login_button").click();
		
		//这里为了测试就以主页几个关键元素显示出来作为登录成功
		HomePage hp = new HomePage(driver);
		WebElement obParent = hp.getElement("onboarding_parent");
		WebElement monthlyReport = hp.getElement("monthly_report");
		WebElement paneWkCalendar = hp.getElement("pane_workingCanlendar");
		Assertion.verifyEquals(hp.waitIfElementBeDisplayed(obParent), true, "---入职管理模块显示失败---");
		Assertion.verifyEquals(hp.waitIfElementBeDisplayed(monthlyReport), true, "---月度报告模块显示失败---");
		Assertion.verifyEquals(hp.waitIfElementBeDisplayed(paneWkCalendar), true, "---工作日历面板显示失败---");
		
		//需要先退出，否则直接输url会免登录进到系统，无法定位登录信息进行多账户循环验证
		logout(driver);
	}
	
	/**
	 * 测试登录报错信息
	 * @param param
	 */
	
	@Test(dataProvider = "providerMethod")
	private void testLoginError(Map<String, String> param)
	{
//		Assertion.flag = false;
		logger.logInfo("打开登录页面: " + param.get("url"));
		driver.get(param.get("url"));
		
		LoginPage lp = new LoginPage(driver);
		logger.logInfo("输入登录用户名为: " + param.get("username"));
		lp.getElement("login_name").sendKeys(param.get("username"));
		logger.logInfo("输入登录密码为: " + param.get("password"));
		lp.getElement("login_pwd").sendKeys(param.get("password"));
		logger.logInfo("点击登录按钮...");
		lp.getElement("login_button").click();
		String errorMessage = lp.getElement("login_error").getText();
		Assertion.verifyEquals(errorMessage, "用户名或密码错误！xx");
	}
}
