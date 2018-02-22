package com.androidmproject.appium.baseutility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.androidmproject.appium.constants.GeneralConstants;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;

/**
 * This class maintains the application start and exit and contains some common
 * Functionalities like clicking on an element, getting text of particular
 * element and wait conditions
 * 
 * @author Satish
 *
 */
public class BaseUtility {

	protected AndroidDriver<WebElement> driver;
	private static final Logger LOGGER = Logger.getLogger(BaseUtility.class);
	private Properties projectConstants;
	private AppiumDriverLocalService serverStartOrStop;

	/**
	 * This method creates driver instance for the respective server url and also
	 * sets the desired capabilities
	 *
	 * @throws IOException
	 */
	@BeforeMethod
	public void preConditions() throws IOException {

		// Server stop and start to ensure the safe start of server
		serverStartOrStop = AppiumDriverLocalService.buildDefaultService();
		serverStartOrStop.stop();
		serverStartOrStop.start();
		LOGGER.info("Appium Server Started");

		// assigning the desired capabilities to the driver
		try {
			projectConstants = new Properties();
			projectConstants
					.load(getClass().getClassLoader().getResourceAsStream(GeneralConstants.PROJECT_CONSTANTS_FILE));
		} catch (NullPointerException npe) {
			Assert.fail("Please create the database and Email Report properties and "
					+ GeneralConstants.PROJECT_CONSTANTS_FILE + " before running the suite");
		}

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME,
				projectConstants.getProperty(GeneralConstants.BROWSER_NAME, GeneralConstants.PLATFORM));
		capabilities.setCapability(GeneralConstants.APP_PACKAGE,
				projectConstants.getProperty(GeneralConstants.APP_PACKAGE_NAME_PROJECT_PROPERTIES));
		capabilities.setCapability(GeneralConstants.APP_ACTIVITY,
				projectConstants.getProperty(GeneralConstants.APP_ACTIVITY_NAME_PROJECT_PROPERTIES));
		capabilities.setCapability(MobileCapabilityType.VERSION,
				projectConstants.getProperty(GeneralConstants.VERSION_PROJECT_PROPERTIES));
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
				projectConstants.getProperty(GeneralConstants.DEVICE_NAME_PROJECT_PROPERTIES));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, GeneralConstants.PLATFORM);
		try {
			driver = new AndroidDriver<WebElement>(new URL(projectConstants
					.getProperty(GeneralConstants.DRIVER_SERVER_URL, GeneralConstants.DEFAULT_DRIVER_SERVER_URL)),
					capabilities);

			// unlock works if not password protected
			driver.unlockDevice();
		} catch (MalformedURLException e) {
			LOGGER.info("Please check the server url- ", e);
		}
	}

	/**
	 * This method is used to waits till the element is found
	 *
	 * @param seconds
	 *            This parameter provides the number of seconds
	 * @param xPath
	 *            This parameter provides the xpath of the element
	 */
	public void explicitlyWait(int seconds, String xPath) {
		new WebDriverWait(driver, seconds).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xPath)));
	}

	/**
	 * This method is used to click on the element
	 *
	 * @param xPath
	 *            This parameter provides the xpath of the element
	 */
	public void inspectAndClickOnElement(String xPath) {
		WebElement element = inspectElement(xPath);
		String textOfElement = element.getText();
		element.click();
		LOGGER.info("Clicked on " + textOfElement);
	}

	/**
	 * This method provides the text of the given xpath
	 *
	 * @param xPath
	 *            This parameter provides the xpath
	 * @return the text of the element
	 */
	public String inspectElementAndGetText(String xPath) {
		WebElement element = inspectElement(xPath);
		return element.getText();
	}

	/**
	 * This method is to return the element
	 * 
	 * @param xPath
	 *            This parameter provides the xpath
	 * @return element
	 */
	public WebElement inspectElement(String xPath) {
		return driver.findElement(By.xpath(xPath));
	}

	/**
	 * This method is to close the application completely.
	 */
	@AfterMethod
	public void closeApp() {
		if (driver != null) {
			driver.quit();
		}
		serverStartOrStop.stop();
		LOGGER.info("Appium Server Stopped");
	}
}