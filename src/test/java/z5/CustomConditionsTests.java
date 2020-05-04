package z5;

import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

@ExtendWith(SeleniumExtension.class)
public class CustomConditionsTests
{
	WebDriver driver;
	WebDriverWait wait;

	public CustomConditionsTests(ChromeDriver driver)
	{
		this.driver = driver;
		wait = new WebDriverWait(driver, 30);
	}

	@AfterEach
	public void teardown()
	{
		driver.quit();
	}

	private void searchFor(String search)
	{
		driver.navigate().to("https://duckduckgo.com");
		wait.until(ExpectedConditions.titleContains("DuckDuckGo"));
		wait.until(ExpectedConditions.presenceOfElementLocated(searchTextField));
		driver.findElement(searchTextField).sendKeys(search);
		wait.until(ExpectedConditions.elementToBeClickable(searchButton));
		WebElement button = driver.findElement(searchButton);
		button.click();
	}

	@Test
	public void searchForDuckOnYoutube()
	{
		searchFor("duck");

		// WAIT FOR A LINK THAT CONTAINS "YOUTUBE"
		WebElement link = new WebDriverWait(driver, 5)
			.until((ExpectedCondition<WebElement>) d ->
				d.findElements(searchResult).stream()
					.filter(x -> x.getText().toLowerCase().contains("youtube"))
					.findFirst().orElseThrow(NotFoundException::new));
		link.click();
	}

	private By searchTextField = By.className("js-search-input");
	private By searchButton = By.id("search_button_homepage");
	private By searchResult = By.cssSelector(".result__title a");

	@Test
	public void getTime()
	{
		driver.navigate().to("https://time.is/");

		// WAIT FOR LAST DIGIT TO ROLL OVER -> AVOIDS POTENTIAL STALENESS
		wait.until((ExpectedCondition<Boolean>) d ->
			d.findElements(By.cssSelector("#clock span"))
				.stream().reduce((a, b) -> b).orElseThrow(NotFoundException::new) // get last element
				.getText().equals("0"));

		String time = driver.findElements(By.cssSelector("#clock span")).stream()
			.map(WebElement::getText) // get text of all elements
			.reduce((a, b) -> a + b).orElseThrow(NotFoundException::new);

		System.out.println(time);
	}

	@Test
	public void checkIfIndexIncreasedInTimeframe()
	{
		By firstIndexValue = By.cssSelector(".markets__group .price");
		driver.navigate().to("https://www.marketwatch.com/investing/stock/live");

		wait.until(ExpectedConditions.presenceOfElementLocated(firstIndexValue));

		String snapshotPriceText = driver.findElement(firstIndexValue).getText().replace(",", "");
		double snapshotPrice = Double.parseDouble(snapshotPriceText);
		Boolean valueIncreased = wait.until(
			(ExpectedCondition<Boolean>) d ->
			{
				String currentPriceText = d.findElement(firstIndexValue).getText();
				double currentPrice = Double.parseDouble(currentPriceText);
				return currentPrice > snapshotPrice;
			});

		if(valueIncreased)
			System.out.println("First index value recently increased");
		else
			System.out.println("First index value did not recently increase");
	}
}
