package z4;

import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

@ExtendWith(SeleniumExtension.class)
public class DuckDuckGoTests
{
	WebDriver driver;

	public DuckDuckGoTests(ChromeDriver driver)
	{
		this.driver = driver;
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.MILLISECONDS);
		driver.manage().timeouts().setScriptTimeout(1, TimeUnit.SECONDS);
	}

	@AfterEach
	public void teardown()
	{
		driver.quit();
	}

	@Test
	public void executeLongScript()
	{
		JavascriptExecutor executor = (JavascriptExecutor) driver;

		// setTimeoutException puts an upper limit on how long an ASYNC JS script can run and throws an exception
		// if the execution time is exceeded

		Assertions.assertDoesNotThrow(
			() -> executor.executeAsyncScript("window.setTimeout(arguments[arguments.length - 1], 500);")); // 0.5s sleep, 1s timeout

		Assertions.assertThrows(ScriptTimeoutException.class,
			() -> executor.executeAsyncScript("window.setTimeout(arguments[arguments.length - 1], 2000);")); // 2s sleep, 1s timeout
	}

	@Test
	public void longLoadExample()
	{
		// pageLoadTimeout puts an upper limit on how long a site can load. This waits only for network request.
		// Javascript execution has to be accounted for by other means (implicit/explicit waits)

		Assertions.assertThrows(TimeoutException.class,
			() -> driver.navigate().to("http://www.worldslongestwebsite.com/")); // timeout 0.01s, load time ~0.15s
	}
}
