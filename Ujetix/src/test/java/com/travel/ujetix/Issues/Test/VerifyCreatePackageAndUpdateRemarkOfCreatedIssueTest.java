package com.travel.ujetix.Issues.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.travel.ujetixObjectRepository.AdminHomePage;
import com.travel.ujetixObjectRepository.AdminLoginPage;
import com.travel.ujetixObjectRepository.CreatePackagePage;
import com.travel.ujetixObjectRepository.ManageIssuesPage;
import com.travel.ujetixObjectRepository.PackageListPage;
import com.travel.ujetixObjectRepository.UpdateComplaintPage;
import com.travel.ujetixObjectRepository.UserHomePage;
import com.travel.ujetixObjectRepository.WelcomePage;
import com.travel.ujetixObjectRepository.WriteUsPage;

import ujetix.GenericUtility.BaseClass;
@Listeners(ujetix.GenericUtility.ListenerImplementation.class)
public class VerifyCreatePackageAndUpdateRemarkOfCreatedIssueTest extends BaseClass
{

	@Test(retryAnalyzer = ujetix.GenericUtility.RetryAnalyzer.class)
	public void verifyCreatePackageAndUpdateRemarkOfCreatedIssueTest() throws EncryptedDocumentException, FileNotFoundException, IOException, InterruptedException 
	{
		String PACKAGENAME =null;				
		
		String remarkAdmin = "OK Remark";
		String partialUrlOfUpdateRemark="updateissue";
		String partialTitleToMain="manage Issues";

		//Navigate to Create Tour Package page
		AdminHomePage ahp=new AdminHomePage(driver);
		ahp.goToCreatePackagePage(driver, wutil);

		//Create Tour Package
		CreatePackagePage cpp=new CreatePackagePage(driver);
		cpp.createPackage();
		PACKAGENAME=cpp.packageName;
		String desc = PACKAGENAME+" Booking issue";

		Assert.assertTrue(cpp.confirmationStatus().equalsIgnoreCase("success"), "Package not created");
		Reporter.log("Package is created", true);
		//Admin Logout
		AdminLoginPage alp=new AdminLoginPage(driver);
		alp.adminLogout();

		//Login as User
		WelcomePage wp=new WelcomePage(driver);
		wp.signInUser(USERUSERNAME, USERPASSWORD);

		//Navigate to Tour Package Page
		UserHomePage uhp=new UserHomePage(driver);
		uhp.getTourPackagesLnk().click();

		PackageListPage plp=new PackageListPage(driver);
		Assert.assertTrue(plp.packagePresentOrNot(PACKAGENAME), "Package Not Present");
		Reporter.log("Package Is Present",true);

		//Navigate to Write us page
		uhp.getWriteUsLnk().click();
		wutil.pageLoadWait(driver);
		WriteUsPage wup=new WriteUsPage(driver);
		wup.createIssue(driver,2, desc);

		//User Logout
		uhp.userLogout();

		String homeUrl = driver.getCurrentUrl();
		Assert.assertTrue(homeUrl.contains("index"), "Logout as user Unsuccessful");
		Reporter.log("Logout as user Successfull", true);

		//Admin Login
		alp.adminLogin(ADMINUSERNAME, ADMINPASSWORD);

		String adminHome = driver.getCurrentUrl();
		Assert.assertTrue(adminHome.contains("dashboard"), "Admin Login Unsuccessfull");
		Reporter.log("Admin Login Successful",true);

		//Navigate to Manage Issues Page
		ahp.getManageIssueLnk().click();

		//Click on View Link of issue
		ManageIssuesPage mip=new ManageIssuesPage(driver);
		mip.clickOnView(desc);

		//Switch to update complaint window
		wutil.switchWindow(partialUrlOfUpdateRemark, driver);
		UpdateComplaintPage ucp=new UpdateComplaintPage(driver);
		ucp.updateRemark(driver, remarkAdmin, partialUrlOfUpdateRemark, partialTitleToMain);
	}
}
