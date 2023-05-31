package com.travel.ujetix.ManageBooking.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.travel.ujetixObjectRepository.AdminHomePage;
import com.travel.ujetixObjectRepository.AdminLoginPage;
import com.travel.ujetixObjectRepository.ManageBookingsPage;
import com.travel.ujetixObjectRepository.MyTourHistoryPage;
import com.travel.ujetixObjectRepository.PackageDetailsPage;
import com.travel.ujetixObjectRepository.PackageListPage;
import com.travel.ujetixObjectRepository.UserHomePage;
import com.travel.ujetixObjectRepository.WelcomePage;

import ujetix.GenericUtility.BaseClass;
import ujetix.GenericUtility.JavaScriptUtility;
@Listeners(ujetix.GenericUtility.ListenerImplementation.class)
public class ConfirmBookingAsAdminAnVerifyAsUserTest extends BaseClass
{
	@Test(groups = "integrationTest",retryAnalyzer = ujetix.GenericUtility.RetryAnalyzer.class)
	public void confirmBookingAsAdminAnVerifyAsUserTest() throws EncryptedDocumentException, FileNotFoundException, IOException, InterruptedException 
	{
		String packageToBook=eutil.getDataFromExcel("Sheet1", 7, 0);
		String startDate=eutil.getDataFromExcel("Sheet1", 7, 1);
		String endDate=eutil.getDataFromExcel("Sheet1", 7, 2);
		String comment=eutil.getDataFromExcel("Sheet1", 7, 3)+jutil.getRandomNumber();
		JavaScriptUtility jsutil = new JavaScriptUtility(driver);
		
		//Admin Logout
		AdminLoginPage alp=new AdminLoginPage(driver);
		alp.adminLogout();
				
		//login as user
		WelcomePage wp=new WelcomePage(driver);
		wp.signInUser(USERUSERNAME, USERPASSWORD);
		
		//Navigate to tour package page
		UserHomePage uhp=new UserHomePage(driver);
		uhp.getTourPackagesLnk().click();
				
		//Select given package
		PackageListPage plp=new PackageListPage(driver);
		plp.clickOnDetailsButton(packageToBook);
				
		//Book the package
		PackageDetailsPage pdp=new PackageDetailsPage(driver);
		pdp.bookTourPackage(driver, startDate, endDate, comment);
				
		Assert.assertTrue(pdp.confirmationStatus().equalsIgnoreCase("Success"), "Booking package Unsuccessfull");
		Reporter.log("Booked package Successfully", true);
		
				uhp.userLogout();
		
		//Admin Login 
		alp.adminLogin(ADMINUSERNAME, ADMINPASSWORD);
		
		//Navigate to ManageBooking page
		AdminHomePage ahp=new AdminHomePage(driver);
		ahp.getManageBookingLnk().click();
		
		ManageBookingsPage mbp=new ManageBookingsPage(driver);
		mbp.confirmBooking(comment);
		
		//Admin Logout
		alp.adminLogout();
		
		//Login as User
		wp.signInUser(USERUSERNAME, USERPASSWORD);
		
		//Navigate to My Tour History page
		uhp.getMyTourHistoryLnk().click();
		MyTourHistoryPage mthp=new MyTourHistoryPage(driver);
		System.out.println(mthp.bookingStatus(comment));
		jsutil.scrollUp();
		
		//Log out as User
		uhp.userLogout();
		
		//Admin Login
		alp.adminLogin(ADMINUSERNAME, ADMINPASSWORD);
	}

}
