package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
public static WebDriver driver;
public static Properties conpro;
//method for launching Browser
public static WebDriver startBrowser()throws Throwable
{
	conpro = new Properties();
	//load property file
	conpro.load(new FileInputStream("./PropertyFiles/Environment.properties"));
	if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
	{
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	else if(conpro.getProperty("Browser").equalsIgnoreCase("Firefox"))
	{
		driver = new FirefoxDriver();
	}
	else
	{
		Reporter.log("Browser value is not matching",true);
	}
	return driver;
}
//method for launching url
public static void openUrl() 
{
	driver.get(conpro.getProperty("Url"));
}
//method for wait for any element
public static void waitForElement(String Ltype,String Lvalue,String Mywait)
{
	WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(Integer.parseInt(Mywait)));
	if(Ltype.equalsIgnoreCase("xpath"))
	{
		//wait until element is visible
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Lvalue)));
	}
	if(Ltype.equalsIgnoreCase("name"))
	{
		//wait until element is visible
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(Lvalue)));
	}
	if(Ltype.equalsIgnoreCase("id"))
	{
		//wait until element is visible
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(Lvalue)));
	}
}
//method for textboxes
public static void typeAction(String Ltype,String Lvalue,String TestData)
{
	if(Ltype.equalsIgnoreCase("xpath")) 
	{
		driver.findElement(By.xpath(Lvalue)).clear();
		driver.findElement(By.xpath(Lvalue)).sendKeys(TestData);
	}
	if(Ltype.equalsIgnoreCase("name")) 
	{
		driver.findElement(By.name(Lvalue)).clear();
		driver.findElement(By.name(Lvalue)).sendKeys(TestData);
	}
	if(Ltype.equalsIgnoreCase("id")) 
	{
		driver.findElement(By.id(Lvalue)).clear();
		driver.findElement(By.id(Lvalue)).sendKeys(TestData);
	}
}
//method for clickAction
public static void clickAction(String Ltype,String Lvalue)
{
	if(Ltype.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(Lvalue)).click();
	}
	if(Ltype.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(Lvalue)).click();
	}
	if(Ltype.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(Lvalue)).sendKeys(Keys.ENTER);
	}
}
//method to validatetitle
public static void validateTitle(String Expected_Title)
{
	String Actual_Title = driver.getTitle();
	try {
		Assert.assertEquals(Actual_Title, Expected_Title,"Title is not Matching");
	} catch (AssertionError e) {
		System.out.println(e.getMessage());
	}
}
//method for closing Browser
public static void closebrowser()
{
	driver.quit();
}
//method for dropdownlists
public static void dropDownAction(String Ltype,String Lvalue,String TestData)
{
	if(Ltype.equalsIgnoreCase("xpath"))
	{
		//convert testdata into integer
		int value = Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.xpath(Lvalue)));
		element.selectByIndex(value);
	}
	if(Ltype.equalsIgnoreCase("name"))
	{
		//convert testdata into integer
		int value = Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.name(Lvalue)));
		element.selectByIndex(value);
	}
	if(Ltype.equalsIgnoreCase("id"))
	{
		//convert testdata into integer
		int value = Integer.parseInt(TestData);
		Select element = new Select(driver.findElement(By.id(Lvalue)));
		element.selectByIndex(value);
	}
}
//method for captureStock
public static void captureStock(String Ltype,String Lvalue)throws Throwable
{
	String stockNum = "";
	if(Ltype.equalsIgnoreCase("xpath"))
	{
		stockNum = driver.findElement(By.xpath(Lvalue)).getAttribute("value");
	}
	if(Ltype.equalsIgnoreCase("name"))
	{
		stockNum = driver.findElement(By.name(Lvalue)).getAttribute("value");
	}
	if(Ltype.equalsIgnoreCase("id"))
	{
		stockNum = driver.findElement(By.id(Lvalue)).getAttribute("value");
	}
	//create notepad into capturedata folder and write
	FileWriter fw = new FileWriter("./CaptureData/stocknumber.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(stockNum);
	bw.flush();
	bw.close();
}
//method for stockTable
public static void stockTable() throws Throwable
{
	FileReader fr = new FileReader("./CaptureData/stocknumber.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
	    driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	    driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	    driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	    driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	    Thread.sleep(5000);
	    String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
	    Reporter.log(Exp_Data);
	    try {
			Assert.assertEquals(Act_Data,Exp_Data,"Stock Number not Found in Table");
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
}
//method for capturing Supplier number into notepad
public static void captureSup(String Ltype,String Lvalue) throws Throwable
{
	String supplierNum = "";
	if(Ltype.equalsIgnoreCase("xpath"))
	{
		supplierNum = driver.findElement(By.xpath(Lvalue)).getAttribute("value");
	}
	if(Ltype.equalsIgnoreCase("name"))
	{
		supplierNum = driver.findElement(By.name(Lvalue)).getAttribute("value");
	}
	if(Ltype.equalsIgnoreCase("id"))
	{
		supplierNum = driver.findElement(By.id(Lvalue)).getAttribute("value");
	}
	//create notepad 
	FileWriter fw = new FileWriter("./CaptureData/supplier.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(supplierNum);
	bw.flush();
	bw.close();
}
//method for supplier Table
public static void supplierTable() throws Throwable
{
	//read supplier number from above notepad
	FileReader fr = new FileReader("./CaptureData/supplier.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	    driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	    driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	    driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	    Thread.sleep(5000);
	    String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
	    Reporter.log(Exp_Data+"     "+Act_Data,true);
	    try {
			Assert.assertEquals(Act_Data, Exp_Data,"Supplier Number is Not matching");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
}
//method for capturing customer number into notepad
public static void captureCus(String Ltype,String Lvalue)throws Throwable
{
	String customerNum = "";
	if(Ltype.equalsIgnoreCase("xpath"))
	{
		customerNum = driver.findElement(By.xpath(Lvalue)).getAttribute("value");
	}
	if(Ltype.equalsIgnoreCase("name"))
	{
		customerNum = driver.findElement(By.name(Lvalue)).getAttribute("value");
	}
	if(Ltype.equalsIgnoreCase("id"))
	{
		customerNum = driver.findElement(By.id(Lvalue)).getAttribute("value");
	}
	//create notepad 
		FileWriter fw = new FileWriter("./CaptureData/customer.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(customerNum);
		bw.flush();
		bw.close();
}
//method for customer Table
public static void customerTable() throws Throwable
{
	//read supplier number from above notepad
	FileReader fr = new FileReader("./CaptureData/customer.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_Data = br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	    driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	    driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
	    driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	    Thread.sleep(5000);
	    String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
	    Reporter.log(Exp_Data+"     "+Act_Data,true);
	    try {
			Assert.assertEquals(Act_Data, Exp_Data,"Customer Number is Not matching");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
}
//generate date format
public static String generateDate()
{
	Date date = new Date();
	DateFormat df = new SimpleDateFormat("dd_MM_YYYY hh_mm");
	return df.format(date);
}
}
