package com.pactera.hris.util;


import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.pactera.hris.base.TestBase;
import com.pactera.hris.info.Path;

/**
 * 实现监听器接口，加入失败截屏等
 * 这里也可是直接extends DotTestListener，只保留了几个关键方法
 * @author zhenhaiw
 *
 */
public class TestRetryListener implements ITestListener
{
	private Log logger = new Log(this.getClass().getName());
    public void onTestFailure(ITestResult result) 
    {  
    	TestBase tb = (TestBase)result.getInstance();//为了传driver进来
        Screenshot.takeScreenshot((TakesScreenshot)tb.getDriver(), Path.SCREENSHOTPATH);  
        logger.logInfo(result.getMethod().getMethodName() + " failed, the screenshot saved in: "  
                + Path.SCREENSHOTPATH + ", screenshot name: "  
                + Screenshot.getScreenshotName());  
        
//        Reporter.setCurrentTestResult(result);
//        result.setAttribute("screenshot", Path.SCREENSHOTPATH);
    }  
	  
    public void onTestStart(ITestResult result) {  
        // TODO Auto-generated method stub  
  
    }  
  
    public void onTestSuccess(ITestResult result) {  
        // TODO Auto-generated method stub  
    }  
  
    public void onTestSkipped(ITestResult result) {  
        // TODO Auto-generated method stub  
  
    }  
  
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {  
        // TODO Auto-generated method stub  
  
    }  
  
    public void onStart(ITestContext context) {  
        // TODO Auto-generated method stub  
  
    }  
    
    public void onFinish(ITestContext context) {  
        // TODO Auto-generated method stub  
  
    }  
}
