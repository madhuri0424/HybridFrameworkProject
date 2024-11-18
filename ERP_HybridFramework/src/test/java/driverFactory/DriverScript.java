package driverFactory;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
WebDriver driver;
String Inputpath = "./FileInput/DataEngine.xlsx";
String Outputpath = "./FileOutput/HybridResults.xlsx";
String TCSheet = "MasterTestCases";
ExtentReports report;
ExtentTest logger;
public void startTest() throws Throwable
{
	String Module_Status = "";
	String Module_New = "";
	//create object for excel file util class
	ExcelFileUtil xl = new ExcelFileUtil(Inputpath);
	//count no of rows in TCSheet
	int rc = xl.rowCount(TCSheet);
	Reporter.log("No.of Rows are ::"+rc,true);
	//iterate all rows in TCSheet
	for(int i=1;i<=rc;i++)
	{
		if(xl.getCellData(TCSheet, i, 2).equalsIgnoreCase("Y"))
		{
		 //read corresponding sheet into one variable
			String TCModule = xl.getCellData(TCSheet, i, 1);
			//define path for html
			report = new ExtentReports("./target/Reports/"+TCModule+"   "+FunctionLibrary.generateDate()+"---"+".html");
			logger = report.startTest(TCModule);
			//iterate all rows in TCModule
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				//read all cells from TCModule
				String Description = xl.getCellData(TCModule, j, 0);
				String Object_Type = xl.getCellData(TCModule, j, 1);
				String Locator_Type = xl.getCellData(TCModule, j, 2);
				String Locator_Value = xl.getCellData(TCModule, j, 3);
				String Test_Data = xl.getCellData(TCModule, j, 4);
				try {
					//call methods from FunctionLibrary
					if(Object_Type.equalsIgnoreCase("startBrowser"))
					{
						driver = FunctionLibrary.startBrowser();
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("openUrl"))
					{
						FunctionLibrary.openUrl();
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("waitForElement"))
					{
						FunctionLibrary.waitForElement(Locator_Type, Locator_Value, Test_Data);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("typeAction"))
					{
						FunctionLibrary.typeAction(Locator_Type, Locator_Value, Test_Data);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("clickAction"))
					{
						FunctionLibrary.clickAction(Locator_Type, Locator_Value);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("validateTitle"))
					{
						FunctionLibrary.validateTitle(Test_Data);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("closebrowser"))
					{
						FunctionLibrary.closebrowser();
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("dropDownAction"))
					{
						FunctionLibrary.dropDownAction(Locator_Type, Locator_Value, Test_Data);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("captureStock"))
					{
						FunctionLibrary.captureStock(Locator_Type, Locator_Value);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("stockTable"))
					{
						FunctionLibrary.stockTable();
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("captureSup"))
					{
						FunctionLibrary.captureSup(Locator_Type, Locator_Value);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("supplierTable"))
					{
						FunctionLibrary.supplierTable();
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("captureCus"))
					{
						FunctionLibrary.captureCus(Locator_Type, Locator_Value);
						logger.log(LogStatus.INFO, Description);
					}
					if(Object_Type.equalsIgnoreCase("customerTable"))
					{
						FunctionLibrary.customerTable();
						logger.log(LogStatus.INFO, Description);
					}
					//write as pass into status cell in TCModule
					xl.setCellData(TCModule, j, 5, "Pass", Outputpath);
					Module_Status = "True";
					logger.log(LogStatus.PASS, Description);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					//write as fail into status cell in TCModule
					xl.setCellData(TCModule, j, 5, "Fail", Outputpath);
					Module_New = "False";
					logger.log(LogStatus.FAIL, Description);
				}
				report.endTest(logger);
				report.flush();
				
			}
			if(Module_Status.equalsIgnoreCase("True"))
			{
				//write pass into status cell in TCSheet
				xl.setCellData(TCSheet, i, 3, "Pass", Outputpath);
			}
			if(Module_New.equalsIgnoreCase("False"))
			{
				//write as fail into status cell in TCsheet
				xl.setCellData(TCSheet, i, 3, "Fail", Outputpath);
			}
		}
		else
		{
			//write as blocked for testcases which are flag to N
			xl.setCellData(TCSheet, i, 3, "Blocked", Outputpath);
		}
	}
}
}
