package kr.ayukawa.automatedbrowser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class EntryPoint {
	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "/path/to/chrome/web/driver");

//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--headless");
//		options.setBinary("/path/to/chrome/installed");

		WebDriver driver = new ChromeDriver();

		// 웹페이지 이동
		driver.get("https://datatracker.ietf.org/doc/html/rfc2616");

		// 현재 표시중인 웹URL 조회
		System.out.println(driver.getCurrentUrl());

		// 요소 찾기
		List<WebElement> preEls = driver.findElements(By.tagName("pre"));
		WebElement tableOfContents = null;

		if(preEls.size() > 3)
			tableOfContents = preEls.get(2);

		// 요소의 텍스트 노드 조회
		if(tableOfContents != null)
			System.out.println(tableOfContents.getText());

		// 요소에 클릭 이벤트 발생시키기
		List<WebElement> anchorEls = driver.findElements(By.tagName("a"));
		WebElement linkOfBasicRules = null;

		if(anchorEls.size() > 45)
			linkOfBasicRules = anchorEls.get(44);

		if(linkOfBasicRules != null)
			linkOfBasicRules.click();

		// 입력 가능 요소에 텍스트 입력
		// RFC 문서에는 <input> 태그가 없으므로 실제 코드는 생략
		// WebElement element = driver.findElement(By.tagName("input"));
		// element.sendKeys("Hello");

		// 요소 삭제
		// Selenium에는 요소를 삭제하는 기능이 없지만, 자바 스크립트를 직접 실행해서 요소를 삭제하는 방법이 있다.
		((ChromeDriver)driver).executeScript("document.getElementById('page-15').remove();");

		Thread.sleep(3000);

		driver.quit();
	}
}
