package com.pactera.hris.testcases;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.pactera.hris.base.TestBase;
import com.pactera.hris.info.Config;
import com.pactera.hris.pages.HomePage;
import com.pactera.hris.pages.MoreCity;
import com.pactera.hris.pages.MoreStore;
import com.pactera.hris.pages.NewStaffEntry;
import com.pactera.hris.pages.StaffObHome;
import com.pactera.hris.util.Assertion;
import com.pactera.hris.util.Log;
import com.pactera.hris.util.TestRetryListener;

/**
 * 入职模块验证
 * @author zhenhaiw
 *
 */

@Listeners({ TestRetryListener.class }) 
public class Onboarding extends TestBase
{
	Log logger = new Log(this.getClass().getName());
	
	@Test(dataProvider="providerMethod")
	private void ob_newRecord(Map<String, String> param)
	{
		String[] temp = null;
		//登录
		Login.preLogin(driver,param);
		
		//添加一条预入职记录
		HomePage hp = new HomePage(driver);
		logger.logInfo("点击展开\"入职管理面板\"...");
		hp.getElement("onboarding_parent").click();
		logger.logInfo("点击打开\"员工入职画面\"...");
		hp.getElement("staff_onboard").click();
		
		StaffObHome sOH = new StaffObHome(driver);
		logger.logInfo("点击新增一条员工预入职记录...");
		sOH.getElement("entry_new").click();
		
		//开始录入员工信息
		NewStaffEntry nSE = new NewStaffEntry(driver);
		logger.logInfo("开始录入员工信息...");
		logger.logInfo("输入员工姓名...");
		nSE.getElement("employee_name").sendKeys(param.get("employee_name"));
		logger.logInfo("输入员工姓名为: " + nSE.getElement("employee_name").getAttribute("value"));
		logger.logInfo("输入入职类型...");
		nSE.getElement("onboard_type").click();
		temp = new String[] {param.get("onboard_type")};
		nSE.getElement("onboard_type_selecor", temp).click();
		logger.logInfo("输入入职类型为: " + nSE.getElement("onboard_type").getAttribute("value"));
		logger.logInfo("输入城市...");
		nSE.getElement("city").clear();
		nSE.getElement("city").click();
		
		//注意这里是跟后边的store_id有对应关系的，每个账号的值可能也不同，
		//数据抽取出来到xml，执行的时候根据需要修改对应xml
		nSE.getElement("city_more").click();
		MoreCity mc = new MoreCity(driver);
		temp = new String[] {param.get("city")};
		mc.getElement("more_city_selector", temp).click();
		mc.getElement("button_select").click();
		logger.logInfo("输入城市为: " + nSE.getElement("city").getAttribute("value"));
		logger.logInfo("输入店铺号...");
		nSE.getElement("store_id").clear();
		nSE.getElement("store_id").click();
		nSE.getElement("store_more").click();
		MoreStore ms = new MoreStore(driver);
		temp = new String[] {param.get("store_id")};
		ms.getElement("more_store_selector", temp).click();
		ms.getElement("button_select").click();
		logger.logInfo("输入店铺号为: " + nSE.getElement("store_id").getAttribute("value"));
		
		logger.logInfo("输入职位...");
		nSE.getElement("position").clear();
		nSE.getElement("position").click();
		temp = new String[] {param.get("position")};
		nSE.getElement("position_selector", temp).click();
		logger.logInfo("输入职位为: " + nSE.getElement("position").getAttribute("value"));
		logger.logInfo("输入基本薪资...");
		nSE.getElement("basic_salary").clear();
		nSE.getElement("basic_salary").sendKeys(param.get("basic_salary"));
		logger.logInfo("输入基本薪资为: " + nSE.getElement("basic_salary").getAttribute("value"));
		logger.logInfo("输入手机...");
		nSE.getElement("cell_number").clear();
		nSE.getElement("cell_number").sendKeys(param.get("cell_number"));
		logger.logInfo("输入手机为: " + nSE.getElement("cell_number").getAttribute("value"));
		logger.logInfo("输入邮箱...");
		nSE.getElement("email").clear();
		nSE.getElement("email").sendKeys(param.get("email"));
		logger.logInfo("输入邮箱为: " + nSE.getElement("email").getAttribute("value"));
		logger.logInfo("输入员工类型...");
		nSE.getElement("staff_type").clear();
		nSE.getElement("staff_type").click();
		temp = new String[] {param.get("staff_type")};
		nSE.getElement("staff_type_selector", temp).click();
		logger.logInfo("输入员工类型为: " + nSE.getElement("staff_type").getAttribute("value"));
		logger.logInfo("输入工时类型...");
		nSE.getElement("timesheet_type").clear();
		nSE.getElement("timesheet_type").click();
		temp = new String[] {param.get("timesheet_type")};
		nSE.getElement("timesheet_type_selector", temp).click();
		logger.logInfo("输入工时类型为: " + nSE.getElement("timesheet_type").getAttribute("value"));
		logger.logInfo("输入合同开始日期...");
		nSE.getElement("contract_start").clear();
		//不加Enter键的话当前页面会停在日历页面，导致后边元素抓不到
		nSE.getElement("contract_start").sendKeys(param.get("contract_start") + Keys.ENTER);
		logger.logInfo("输入合同开始日期为: " + nSE.getElement("contract_start").getAttribute("value"));
		logger.logInfo("保存并提交记录...");
		nSE.getElement("save").click();;
		
		logger.logInfo("验证是否生成新纪录...");
		temp = new String[] {param.get("approve_status"), param.get("cell_number")};
		Assertion.verifyEquals(sOH.getElement("record", temp).isDisplayed(), true, "---新记录创建失败---");
		
		try {
			Thread.sleep(Config.SLEEPTIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Login.logout(driver);
	}
	
	@Test(dataProvider = "providerMethod")
	//还不好用，需要调整 思路1)利用页面高级筛选，一次删除；2)符合条件逐条删除，但是需要每次第一条删除后，后续记录的处理(目前删除一条之后，无法操作后续记录)
	public void ob_deleteRecord(Map<String, String> param)
	{
		String[] temp = null;
		Login.preLogin(driver, param);
		
		//查找入职记录并删除
		HomePage hp = new HomePage(driver);
		logger.logInfo("点击展开\"入职管理面板\"...");
		hp.getElement("onboarding_parent").click();
		logger.logInfo("点击打开\"员工入职画面\"...");
		hp.getElement("staff_onboard").click();
		
		StaffObHome sOH = new StaffObHome(driver);
		logger.logInfo("查找符合条件的员工预入职记录...");
		
		temp = new String[] {param.get("approve_status"), param.get("cell_number")};
		List<WebElement> records = sOH.getElements("record", temp);
		if(!records.isEmpty())
		{
			for(WebElement e : records)
			{
				e.click();
				sOH.getElement("entry_delete").click();
				sOH.dealPotentialAlert(driver, true);
				try {
					Thread.sleep(Config.SLEEPTIME);
				} catch (InterruptedException exp) {
					// TODO Auto-generated catch block
					exp.printStackTrace();
				}
			}
		}else
		{
			logger.logInfo("没有找到符合条件的记录...");
		}
		Assertion.verifyEquals(sOH.getElements("record", temp).isEmpty(), true, "---符合条件预入职记录没有被成功删除---");
		
		Login.logout(driver);
	}
}
