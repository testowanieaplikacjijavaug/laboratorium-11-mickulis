package z2;

import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import z1.DuckDuckGoPage;

import java.util.concurrent.TimeUnit;

@ExtendWith(SeleniumExtension.class)
public class DuckDuckGoTests
{
	WebDriver driver;

	private By searchTextField = By.className("js-search-input");
	private By searchTextField_Name = By.name("q");
	private By searchButton = By.id("search_button_homepage");
	private By searchResult = By.cssSelector(".result__title");

	public DuckDuckGoTests(ChromeDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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

	private boolean isElementPresent(By by)
	{
		try
		{
			driver.findElement(by);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Test
	public void findSearchButton()
	{
		Assertions.assertTrue(isElementPresent(searchButton));
	}

	@Test
	public void findTextField()
	{
		Assertions.assertTrue(isElementPresent(searchTextField));
	}

	@Test
	public void findSearchResult_NotFound()
	{
		Assertions.assertFalse(isElementPresent(searchResult));
	}
}
