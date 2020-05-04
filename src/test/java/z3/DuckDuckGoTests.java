package z3;

import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@ExtendWith(SeleniumExtension.class)
public class DuckDuckGoTests
{
	WebDriver driver;
	WebDriverWait wait;

	public DuckDuckGoTests(ChromeDriver driver)
	{
		this.driver = driver;
		wait = new WebDriverWait(driver, 10);
	}

	@BeforeEach
	public void setup()
	{
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

	@Test
	public void searchForDuckDuckGoOnDuckDuckGO()
	{
		searchFor("duck");

		// TEXT TO ME PRESENT
		wait.until(ExpectedConditions.textToBePresentInElementLocated(searchResult, "DuckDuckGo"));

		driver.findElements(searchResult)
			.stream().filter(x -> x.getText().toLowerCase().contains("DuckDuckGo".toLowerCase()))
			.findFirst().orElseThrow(NotFoundException::new)
			.click();
	}

	private By searchTextField = By.className("js-search-input");
	private By searchButton = By.id("search_button_homepage");
	private By searchResult = By.cssSelector(".result__title a");
}
