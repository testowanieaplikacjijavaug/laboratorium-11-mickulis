package z1;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class DuckDuckGoPage
{
	public DuckDuckGoPage(WebDriver driver)
	{
		this.driver = driver;
	}

	protected WebDriver driver;

	private By searchTextField = By.className("js-search-input");
	private By searchTextField_Name = By.name("q");
	private By searchButton = By.id("search_button_homepage");
	private By searchResult = By.cssSelector(".result__title");

	public DuckDuckGoPage searchFor(String search)
	{
		driver.navigate().to("https://duckduckgo.com");
		driver.findElement(searchTextField_Name).sendKeys(search);

		WebElement button = driver.findElement(searchButton);

		// alternative click with Actions
		Actions action = new Actions(driver);
		action.moveToElement(button).click().build().perform();

		// another alternative click with JavascriptExecutor (this one works even if button is covered by another element)
		// JavascriptExecutor executor = (JavascriptExecutor) driver;
		// executor.executeScript("arguments[0].click();", button);
		return this;
	}

	public int getNumberOfResults()
	{
		return driver.findElements(searchResult).size();
	}

	public DuckDuckGoPage clickOnNthResult(int n)
	{
		if(n < 1)
			throw new InvalidArgumentException("");

		List<WebElement> list = driver.findElements(searchResult);
		if(list.size() < n)
			throw new NotFoundException();

		list.get(n-1).click();
		return this;
	}

	public DuckDuckGoPage navigateBack()
	{
		driver.navigate().back();
		return this;
	}
}
