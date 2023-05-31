package com.travel.ujetix.Issues.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.travel.ujetixObjectRepository.AdminHomePage;
import com.travel.ujetixObjectRepository.AdminLoginPage;
import com.travel.ujetixObjectRepository.IssueTicketsPage;
import com.travel.ujetixObjectRepository.ManageIssuesPage;
import com.travel.ujetixObjectRepository.UpdateComplaintPage;
import com.travel.ujetixObjectRepository.UserHomePage;
import com.travel.ujetixObjectRepository.WelcomePage;
import com.travel.ujetixObjectRepository.WriteUsPage;

import ujetix.GenericUtility.BaseClass;
@Listeners(ujetix.GenericUtility.ListenerImplementation.class)
public class CreateIssueAndVerifyAdminRemarkInUserTest extends BaseClass
{
	@Test(retryAnalyzer = ujetix.GenericUtility.RetryAnalyzer.class)
	public void createIssueAndVerifyAdminRemarkInUserTest() throws FileNotFoundException, IOException, InterruptedException 
	{
		//TO FETCH EXCEL DATA
		String PACKAGENAME = eutil.getDataFromExcel("Sheet1", 1, 0)+jutil.getRandomNumber();		
		String desc = PACKAGENAME+" Booking issue";
		String remarkAdmin = "OK Remark";
		String partialUrlOfUpdateRemark="updateissue";
		String partialTitleToMain="manage Issues";

		//AdminLogout
		AdminLoginPage alp=new AdminLoginPage(driver);
		alp.adminLogout();		

		//Login as User
		WelcomePage wp=new WelcomePage(driver);
		wp.signInUser(USERUSERNAME, USERPASSWORD);

		//Navigate to write us page
		UserHomePage uhp=new UserHomePage(driver);
		uhp.getWriteUsLnk().click();

		//Create Issue
		WriteUsPage wup=new WriteUsPage(driver);
		wup.createIssue(driver,1, desc);

		Assert.assertTrue(wup.issueCreatedOrNot(driver), "Issue not created");
		Reporter.log("Issue Created", true);

		//User Logout
		uhp.userLogout();

		//Admin Login			
		alp.adminLogin(ADMINUSERNAME, ADMINPASSWORD);
		Assert.assertTrue(alp.adminLoginSuccessFul(driver), "Admin login Unsuccessful");
		Reporter.log("Admin login Successful", true);

		AdminHomePage ahp=new AdminHomePage(driver);
		ahp.getManageIssueLnk().click();

		ManageIssuesPage mip=new ManageIssuesPage(driver);
		mip.clickOnView(desc);

		UpdateComplaintPage ucp=new UpdateComplaintPage(driver);
		ucp.updateRemark(driver, remarkAdmin, partialUrlOfUpdateRemark, partialTitleToMain);

		//Admin Logout
		alp.adminLogout();

		//User Login
		wp.signInUser(USERUSERNAME, USERPASSWORD);

		//Navigate to Issue Tickets Page
		uhp.getIssueTicketsLnk().click();
		IssueTicketsPage itp=new IssueTicketsPage(driver);

		String issueRemark = itp.adminRemarkText(desc);
		Assert.assertTrue(issueRemark.contains(remarkAdmin), "Remark updated Unsuccessfully");
		Reporter.log("Remark updated Successfully", true);
		//User Logout
		uhp.userLogout();
		Reporter.log("User Logout Successful",true);

		//Admin Login
		alp.adminLogin(ADMINUSERNAME, ADMINPASSWORD);
		System.out.println("Admin Logged in");

	}	
}