package com.qa.hubspot.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.hubspot.base.BasePage;
import com.qa.hubspot.page.HomePage;
import com.qa.hubspot.page.LoginPage;
import com.qa.hubspot.util.AppConstants;
import com.qa.hubspot.util.Credentials;




public class LoginPageTest {

	WebDriver driver;
	BasePage basePage;
	Properties prop;
	LoginPage loginPage;
	Credentials userCred;

	
	
	@BeforeMethod
	public void setUp() {
		
		basePage = new BasePage();
		prop = basePage.init_properties();
		String browserName = prop.getProperty("browser");	
		driver = basePage.init_driver(browserName);
		driver.get(prop.getProperty("url"));
		loginPage = new LoginPage(driver);
		userCred = new Credentials(prop.getProperty("username"), prop.getProperty("password"));
	}

	@Test(priority = 1)
	
	public void verifyLoginPageTitleTest(){
		String title = loginPage.getPageTitle();
		System.out.println("login page title is : " + title);
		AssertJUnit.assertEquals(title, AppConstants.LOGIN_PAGE_TITLE);
	}

	@Test(priority = 2, groups = "sanity")
	
	public void verifySignUpLinkTest() {
		Assert.assertTrue(loginPage.checkSignUpLink());
	}

	@Test(priority = 3)
	public void loginTest() {
		HomePage homePage = loginPage.doLogin(userCred);
		String accountName = homePage.getLoggedInUserAccountName();
		System.out.println(accountName);
		//Assert.assertEquals(accountName, prop.getProperty("accountname"));
	}
	
	
	@DataProvider
	public Object[][] getLoginInvaliData(){
		Object data[][] = { 
							{"test11111@gmail.com", "test123"},  
							{"test2@gmail.com", " "}, 
							{"  ", "test12345"}, 
							{"test", "test"},
							{" ", " "}
						  };
		
		return data;
	}
	
	
	@Test(priority=4, dataProvider = "getLoginInvaliData", enabled = false)
	public void login_InvalidTestCases(String username, String pwd){
		userCred.setAppUsername(username);
		userCred.setAppPassword(pwd);
		loginPage.doLogin(userCred);
		AssertJUnit.assertTrue(loginPage.checkLoginErrorMesg());
	}
	

	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

}