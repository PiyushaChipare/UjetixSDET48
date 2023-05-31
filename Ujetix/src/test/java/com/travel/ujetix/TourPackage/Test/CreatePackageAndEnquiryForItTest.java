package com.travel.ujetix.TourPackage.Test;

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
import com.travel.ujetixObjectRepository.EnquiryPage;
import com.travel.ujetixObjectRepository.GuestTourPackagePage;
import com.travel.ujetixObjectRepository.ManageEnquiriesPage;
import com.travel.ujetixObjectRepository.WelcomePage;

import ujetix.GenericUtility.BaseClass;
import ujetix.GenericUtility.JavaScriptUtility;
@Listeners(ujetix.GenericUtility.ListenerImplementation.class)
public class CreatePackageAndEnquiryForItTest extends BaseClass
{
	@Test(retryAnalyzer = ujetix.GenericUtility.RetryAnalyzer.class)
	public void createPackageAndEnquiryForItTest() throws InterruptedException, EncryptedDocumentException, FileNotFoundException, IOException 
	{					
		String PACKAGENAME=null;
		String FULLNAME = eutil.getDataFromExcel("Sheet2", 1, 0);
		String EMAIL = eutil.getDataFromExcel("Sheet2", 1, 1);
		String MOBILE = eutil.getDataFromExcel("Sheet2", 1, 2);
		String SUBJECT = eutil.getDataFromExcel("Sheet2", 1, 3)+PACKAGENAME;
		String DESCRIPTION = eutil.getDataFromExcel("Sheet2", 1, 4)+PACKAGENAME;

		JavaScriptUtility jsutil = new JavaScriptUtility(driver);		

		//Navigate to Create Tour Package page
		AdminHomePage ahp=new AdminHomePage(driver);
		ahp.goToCreatePackagePage(driver, wutil);

		CreatePackagePage cpp=new CreatePackagePage(driver);
		cpp.createPackage();
		PACKAGENAME =cpp.packageName;


		//Admin Logout
		AdminLoginPage alp=new AdminLoginPage(driver);
		alp.adminLogout();

		//Navigate to guest tour package page

		WelcomePage wp=new WelcomePage(driver);
		wp.getTourPackagesLnk().click();

		//Check if package is present in package list page or not
		GuestTourPackagePage gtpp=new GuestTourPackagePage(driver);
		Assert.assertTrue(gtpp.packagePresentOrNot(PACKAGENAME), "Package is not present");
		Reporter.log("Package is present");

		jsutil.scrollUp();
		wp.getEnquiryLnk().click();

		//Create an enquiry
		EnquiryPage ep=new EnquiryPage(driver);
		ep.createEnquiry(FULLNAME, EMAIL, MOBILE, SUBJECT, DESCRIPTION);

		//Check if enquiry is created or not
		Assert.assertTrue(ep.enquiryCreatedOrNot("success"), "Enquiry is not created");
		Reporter.log("Enquiry is created");

		//Admin Login
		alp.adminLogin(ADMINUSERNAME, ADMINPASSWORD);

		//Navigate to manage issues page
		ahp.getManageEnquiriesLnk().click();
		ManageEnquiriesPage mep=new ManageEnquiriesPage(driver);
		mep.readPendingEnquiry(SUBJECT);				
	}
}


