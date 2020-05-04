package z6;

import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@ExtendWith(SeleniumExtension.class)
public class HtmlUnitTests
{
	WebDriver driver;
	WebDriverWait wait;

	@BeforeEach
	public void setup()
	{
		driver = new HtmlUnitDriver();
		wait = new WebDriverWait(driver, 10);
		driver.navigate().to("https://duckduckgo.com");
	}

	@AfterEach
	public void teardown()
	{
		driver.quit();
	}


	private void searchFor(String search)
	{
		// TITLE CONTAINS
		wait.until(ExpectedConditions.titleContains("DuckDuckGo"));

		// ELEMENT EXISTS
		wait.until(ExpectedConditions.presenceOfElementLocated(searchTextField));

		driver.findElement(searchTextField).sendKeys(search);

		// ELEMENT CLICKABLE
		wait.until(ExpectedConditions.elementToBeClickable(searchButton));

		WebElement button = driver.findElement(searchButton);
		button.click();
	}

	@Test
	public void searchForADuck()
	{
		searchFor("duck");
	}

	@Test
	public void dontSelectSearchButton()
	{
		// ELEMENT TO BE SELECTED
		Assertions.assertThrows(TimeoutException.class,
			() -> wait.until(ExpectedConditions.elementToBeSelected(searchButton)));
	}

	private By searchTextField = By.className("js-search-input");
	private By searchButton = By.id("search_button_homepage");
	private By searchResult = By.cssSelector(".result__title a");
}
