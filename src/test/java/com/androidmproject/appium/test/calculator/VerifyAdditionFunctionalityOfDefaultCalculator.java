package com.androidmproject.appium.test.calculator;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.androidmproject.appium.baseutility.BaseUtility;
import com.androidmproject.appium.constants.DataConstants;
import com.androidmproject.appium.constants.XPathConstants;
import io.appium.java_client.MobileElement;

/**
 * This Test class is to verify the addition functionality of the default
 * Calculator
 * 
 * @author Satish
 */

public class VerifyAdditionFunctionalityOfDefaultCalculator extends BaseUtility {
	private static final Logger LOGGER = Logger.getLogger(VerifyAdditionFunctionalityOfDefaultCalculator.class);

	/**
	 * This test method is to verify the addition functionality
	 * 
	 * @param firstInput
	 *            This parameter provides the input data
	 * @param secondInput
	 *            This parameter provides the input data
	 */
	@Test(dataProvider = "getAdditionParameters")
	public void additionFunctionalityVerification(String firstInput, String secondInput) {

		explicitlyWait(60, XPathConstants.CALCULATOR_FORMULAE_XPATH);

		// Giving the input to the calculator
		WebElement formulaeField = inspectElement(XPathConstants.CALCULATOR_FORMULAE_XPATH);
		((MobileElement) formulaeField).setValue(firstInput + "+" + secondInput);

		// Clicking on the equal icon
		inspectAndClickOnElement(XPathConstants.CALCULATOR_EQUALS_XPATH);

		// Storing the output of the addition value
		String addedOutput = inspectElementAndGetText(XPathConstants.CALCULATOR_OUTPUT_AREA_XPATH).replace(",", "");

		String expectedValue;
		if (addedOutput.contains(".")) {
			expectedValue = String.valueOf(Double.parseDouble(firstInput) + Double.parseDouble(secondInput));
		} else {
			expectedValue = String.valueOf(Double.parseDouble(firstInput) + Double.parseDouble(secondInput))
					.split("[.]")[0];
		}

		// verifying the actual with the expected output value
		if (addedOutput.equals(expectedValue)) {
			LOGGER.info("Addition applied successfully for " + firstInput + " and " + secondInput + " and output is: "
					+ addedOutput);
		} else {
			Assert.fail("Problem in adding the given input digits. Expected : " + expectedValue + " Actual : "
					+ addedOutput);
		}
	}

	/**
	 * This method provides the data to the testcase
	 * 
	 * @return data
	 */
	@DataProvider
	private Object[][] getAdditionParameters() {
		final Object[][] data = { { DataConstants.FIRST_RANDOM_INPUT_DATA, DataConstants.SECOND_RANDOM_INPUT_DATA } };
		return data;
	}
}