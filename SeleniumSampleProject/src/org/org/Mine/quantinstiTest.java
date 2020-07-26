package org.org.Mine;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class quantinstiTest {
	
	@FindBy(xpath = "//button[text()='Remind me later']")
	private List<WebElement> HomePagePopup;
	
	@FindBy(xpath = "//span[text()='Login']")
	private WebElement LoginButton;
	
	@FindBy(xpath = "//input[@name='email']")
	private WebElement email;
	
	@FindBy(xpath = "//input[@name='password']")
	private WebElement passWord;
	
	@FindBy(linkText = "Browse Courses")
	private WebElement BrowseCourses;
	
	@FindBy(xpath = "//span[text()='Sentiment Analysis in Trading']")
	private WebElement Courses;
	
	@FindBy(xpath = "//span[contains(text(),'Enroll Now+@10% OFF')]")
	private WebElement EnrollNow;
	
	@FindBy(xpath = "(//img[@title='Shopping Cart']//following-sibling::span)[2]")
	private List<WebElement> Courseicon;
	
	@FindBy(xpath = "//div[@class='cart-item__thumnail-info']")
	private List<WebElement> ListOfCourses;
	
	@FindBy(xpath = "(//div[@class='cart-summary-item'])[1]/div")
	private List<WebElement> BaseAmount;
	
	@FindBy(xpath = "//div[@class='cart-summary-item amt-payable-wrap']/div/h5")
	private List<WebElement> AmountPayable;
	
	@FindBy(xpath = "(//div[@class='cart-item-cta']/a)[1]")
	private WebElement ViewDetails;
	
	@FindBy(linkText = "Remove")
	private List<WebElement> RemoveCourse;
	
	@FindBy(xpath = "//div[@class='toasted-container top-center']")
	private WebElement ToastMessage;
	
	@FindBy(xpath = "//button[@class='vue-ui-button btn secondary ghost-button']")
	private WebElement ApplyCoupon;
	
	@FindBy(xpath = "//input[@placeholder='Type coupon code']")
	private WebElement EnterCoupon;
	
	@FindBy(xpath = "//span[text()='Apply']")
	private WebElement Apply;
	
	@FindBy(xpath = "//div[@class='coupon-modal-alert alert alert-danger']/span")
	private WebElement CouponMessageValidation;
	
	WebDriver driver=null;
	String icon=null;
	
	
	@BeforeSuite
	public void initialiseDriver() {
		//Open Application - https://quantra.quantinsti.com
		System.setProperty("webdriver.chrome.driver","C:\\AmiyaPersonal\\Amiya Project\\Reports\\WorkSpace\\Cucumber\\driver\\chromedriver.exe");
	    driver=new ChromeDriver();
	    driver.get("https://quantra.quantinsti.com/");
	    driver.manage().window().maximize();
        System.out.println(driver.getTitle());
	}

	@Test(dataProvider = "loginCredential")
	public void automateApplication(String UserName, String PassWord) throws InterruptedException {
		WebDriverWait wait=new WebDriverWait(driver, 20);
		PageFactory.initElements(driver, this);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		if(HomePagePopup.get(0).isDisplayed()) {
			HomePagePopup.get(0).click();
		}
		//Click on the login button
		LoginButton.click();
		//Log in as username & password
		email.sendKeys(UserName);
		passWord.sendKeys(PassWord);
		passWord.submit();
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h2[text()='Recommended Courses']"))));
		//Then click on the Browse Courses menu
		Actions actions=new Actions(driver);
		actions.moveToElement(BrowseCourses).build().perform();
		//And select the course - Sentiment Analysis in Trading
		actions.click(Courses).build().perform();
		//Get the Course name & Price
		String CourseName=driver.findElement(By.xpath("//div[@class='course-detail__left-view']/h1")).getText();
		System.out.println("Course Name-"+CourseName);
		String courseFee=driver.findElement(By.xpath("//div[@class='cd__data-unit__info']/span[2]")).getText();
		System.out.println("Course fees-"+courseFee);	
		//Then click on the Enroll now button (cart page gets open after clicking onenrol now button)
		EnrollNow.click();
		/*
		 * List<WebElement> frames=driver.findElements(By.tagName("iframe"));
		 * System.out.println(frames.size()); for(WebElement frameEle:frames) {
		 * driver.switchTo().frame(frameEle); if(Courseicon.size()>0) {
		 * icon=Courseicon.get(0).getText().trim(); System.out.println(icon); }
		 * driver.switchTo().defaultContent(); }
		 */
	//On the cart page, get the courses name & count the number of the courses present in the cart page and verify that the course count should match with the
	//number displayed in the cart icon.
	Thread.sleep(4000);
	JavascriptExecutor jsp=(JavascriptExecutor)driver;
	icon=(String)jsp.executeScript("return document.querySelector('span[class=\"cart-count\"]').innerHTML");
		int CartCount=Integer.parseInt(icon);
		int listOfCoursesInCart=ListOfCourses.size();
		Assert.assertEquals(CartCount, listOfCoursesInCart);
		List<WebElement> coursesName=driver.findElements(By.xpath("//div[@class='cart-item__thumnail-info']/span"));
		for(WebElement Cnames:coursesName) {
			System.out.println(Cnames.getText());
		}
		//Capture the Base Amount & Amount Payable info
		for(WebElement BA:BaseAmount) {
			System.out.print(BA.getText());
			System.out.print("-");
		}
		System.out.println(" ");
		for(WebElement AP:AmountPayable) {
			System.out.print(AP.getText());
			System.out.print("-");
		}
	     //Click on the View Details link of any course (a new browser tab gets open on
		//clicking on the View Details link. After that close the tab and get back to the cart page )
		Thread.sleep(4000);
		ViewDetails.click();
		String ParentWindow=driver.getWindowHandle();	
		for (String handle1 : driver.getWindowHandles()) {
         	System.out.println(handle1);
         	driver.switchTo().window(handle1);
         	}
         // Closing Pop Up window
         driver.close();
         driver.switchTo().window(ParentWindow);
		//11.After that remove any one course from the cart page by clicking remove link and get the alert message text
		
			RemoveCourse.get(0).click();
		
		if(ToastMessage.isDisplayed()) {
			String message=ToastMessage.getText();
			System.out.println(message);
		}
		//Then click on the Apply coupon button and type ABC into the text box and
		//click on the Apply button and get the error message.
		ApplyCoupon.click();
		EnterCoupon.sendKeys("ABC");
		Apply.click();
		wait.until(ExpectedConditions.visibilityOf(CouponMessageValidation));
		if(CouponMessageValidation.isDisplayed()) {
			String message=CouponMessageValidation.getText();
			System.out.println(message);
		}
		//Then close the modal.
		//And Sign out from the application
		driver.findElement(By.xpath("//span[@aria-hidden='true']")).click();
		driver.findElement(By.xpath("//div[@class='profile-pic-initials']")).click();
		driver.findElement(By.linkText("Logout")).click();
		
		
	}

	@DataProvider
	public Object[][] loginCredential() {
		return new Object[][] { new Object[] { "amiya.qanayak@gmail.com", "password" } };
	}
	
	@AfterSuite
	public void closeSession() {
		driver.close();
	}

}
