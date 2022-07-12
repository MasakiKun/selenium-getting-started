# Selenium을 이용해서 자바로 Chrome 브라우저 자동화하기

Selenium은 프로그래밍 언어를 통해서 웹 브라우저를 자동화할 수 있는 도구이다.
이를테면, 브라우저를 제어할 수 있도록 API를 제공해주는 라이브러리라고 할 수도 있겠다.

실은 이거 예전에 했었는데 오랜만에 하려니 하나도 기억이 안나서-_- 간단하게 해보고 샘플 코드 및 설명을 남겨둔다.

## 전제조건

이 글은 윈도우 운영체제에서 크롬 브라우저를 제어하는 것만을 다룬다.

운영체제가 달라지는 경우에는, 아래 설명에서 등장하는 크롬 웹 드라이버를 해당 운영체제에 맞는 드라이버로 교체하면 될 것으로 생각한다.

브라우저가 달라지는 경우에는, 물론 웹 드라이버도 교체해야 하겠지만 옵션이나 환경변수 명이 다를 가능성이 있어서 아래 설명대로 진행해도 안될수도 있다.
꼭 필요하다면, 후술한 각 브라우저 별 웹 드라이버 문서를 참조하자.

## Selenium 사용 방법

1. 프로젝트에 Selenium Java의 의존성을 추가한다.

```
implementation("org.seleniumhq.selenium:selenium-java:4.3.0")
```

2. 크롬 웹 드라이버를 다운로드해서, 적당한 경로에 압축을 풀어둔다. 여기서는 크롬 외 브라우저의 웹 드라이버도 명시해둔다.

* Selenium이 지원하는 브라우저 목록
  * https://www.selenium.dev/downloads/#platforms-supported-by-selenium

* 각 브라우저별 웹 드라이버 페이지
  * Chrome https://chromedriver.chromium.org/home
    * 단, Chrome Canary 를 사용하는 경우 https://chromedriver.chromium.org/chromedriver-canary
  * Firefox https://github.com/mozilla/geckodriver/releases
  * Opera https://github.com/operasoftware/operachromiumdriver/releases
  * Edge https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/#downloads

3. 2에서 다운로드한 웹 드라이버의 전체 경로를, 파일명까지 포함해서 webdriver.chrome.driver 환경변수로 지정한다.

```java
System.setProperty("webdriver.chrome.driver", "c:\\selenium\\webdriver\\chromedriver.exe");
```

4. 크롬 실행시에 옵션을 주고 싶다면, ChromeOptions 인스턴스를 생성해서 인자를 추가한다.
딱히 설정할 것이 없다면 이 부분은 건너뛰어도 관계 없다.

* 헤드리스 모드(브라우저가 화면에 표시되지 않고 백그라운드로 동작한다)를 사용할 때는 --headless 인자를 추가한다.
* 만약 시스템에 여러개의 크롬이 설치되어 있고, 실행될 크롬을 지정하고 싶다면 setBinary() 메서드에 크롬이 설치된 경로를 지정해준다.
* ChromeOptions를 이용해서 제어할 수 있는 옵션 목록은 [크롬 웹 드라이버 문서](https://chromedriver.chromium.org/capabilities)를 참고한다.
~~그나저나 어째서인지 공식문서에 --headless 옵션은 없다-_-~~

```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless");
options.setBinary("/path/to/chrome/installed");
```

5. ChromeDriver의 인스턴스를 생성하면, 자동으로 브라우저가 표시된다. 
만약 4번에서 ChromeOptions 인스턴스를 생성했다면, 생성자 인자로 이 옵션을 전달해준다.

```java
WebDriver driver = new ChromeDriver(options);
```

이 코드를 통해 크롬 브라우저가 실행되면, 상단에 자동화된 테스팅 소프트웨어에 의해 제어되고 있다고 표시된다.

![](https://user-images.githubusercontent.com/12710869/178525331-6c7db943-af06-41de-8490-674a94f6b006.png)

6. 이제부터는 ChromeDriver 의 인스턴스를 이용해서 브라우저를 제어하면 된다.
만약 헤드리스 모드로 실행한 경우에는, 프로그램 마지막에 반드시 quit() 메서드를 실행해서 크롬 프로세스를 종료하도록 한다.

```java
driver.quit();
```

## 몇가지 사용방법 예시

브라우저가 표시된 뒤에는, 코드를 이용해서 실제로 브라우저를 제어해야 의미가 있다.
여기서는 브라우저를 제어하는 몇가지 예시를 살펴본다.

### 웹페이지 표시

웹 드라이버의 ```get()``` 메서드를 이용하면 브라우저에 웹 페이지가 표시된다.

```java
driver.get("https://datatracker.ietf.org/doc/html/rfc2616");  // HTTP/1.1 스펙을 명시한 RFC 2616 문서
```

### 요소 찾기

웹 드라이버의 ```findElement()``` 혹은 ```findElements()``` 메소드와 정적 클래스인 By를 조합해서,
현재 표시중인 웹페이지의 요소를 조회할 수 있다.

```java
// document.getElementById("section1.1");
WebElement el1 = driver.findElement(By.id("section1.1"));
// document.getElementsByTagName("input");
List<WebElement> els = driver.findElements(By.tagName("input"));
// document.querySelector("div.success");
List<WebElement> selectorBase = driver.findElements(By.cssSelector("div.success"));
```

Selenium은 특이하게(?) 자바스크립트에서 제공하는 요소 탐색 방법 외에도 XPath를 이용해서 요소를 찾는 방법도 제공한다.
By.xpath()를 이용하면 되는데, 이 문서에서는 생략한다.

### 요소에 클릭 이벤트 발생

```WebDriver.findElement()``` 메서드를 통해 찾아낸 요소에 ```click()``` 메서드를 이용해서 click 이벤트를 발생시킬 수 있다.

```java
driver.findElement(By.cssSelector("a.topLink")).click();
```

### 요소에 텍스트 입력

클릭 이벤트와 비슷하게, ```sendKeys()``` 메서드를 이용해서 키입력을 일으킬 수 있다.

```java
driver.findElements(By.tagName("input")).get(0).sendKeys("hello!");
```

### 요소 삭제

Selenium은 요소를 삭제하는 기능은 없지만, 제어중인 브라우저에서 자바스크립트를 직접 실행함으로써 요소를 삭제할 수 있다.
구체적으로는 ```document.getElementXXX()``` 함수를 이용해서 요소를 찾은 다음, ```remove()``` 함수를 호출해서 요소를 제거한다.

~~하지만 솔까말 jQuery 쓰면 그냥 되는걸...~~

이때, WebDriver 인터페이스는 스크립트를 실행할 수 있는 ```executeScript()``` 메서드를 제공하지 않으므로
반드시 ```ChromeDriver``` 클래스로 캐스팅해야 한다.

```java
((ChromeDriver)driver).executeScript("document.getElementById('do-not-need-element').remove();");
```

### 현재 표시중인 웹페이지 URL 조회

WebDriver의 ```getCurrentUrl()``` 메서드를 이용해서 현재 표시중인 페이지의 URL을 조회할 수 있다.
로그인 페이지를 제어해서 로그인 시도 후, 로그인 성공 페이지로 전환되었는지 확인할 때 등에 사용해 볼 수 있겠다.

```java
System.out.println(driver.getCurrentUrl());
```

### 페이지 로딩 기다리기

사실 Selenium은 HTML body 요소의 load 이벤트, 혹은 jQuery의 ready 이벤트에 해당하는 처리를 할 수 있는 방법이 없다.
즉, ```WebDriver.get()``` 메서드 호출 후 바로 ```WebDriver.findElement()```를 호출하면,
DOM이 다 형성되기 전에 요소를 찾으려고 시도하므로 요소가 없어서 예외가 발생하게 된다.
상식적으로 생각해보면 그냥 페이지 로딩이 끝날때까지 기다렸다가 처리하는 것이 좋겠지만,
상술한대로 body 요소의 load 이벤트와 비슷한 이벤트 핸들러를 만들 수 있는 방법이 없다.

정확히는 아예 방법이 없는것은 아니고, WebDriver의 implicitlyWait 메서드를 호출해서 DOM 형성이 끝날때까지 기다리는 방법이 있다고는 한다.

```java
WebDriver driver = new ChromeDriver();
driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);  // DOM이 형성되기까지 암묵적으로 10초를 기다린다
```

근데 테스트해보니까, 이렇게 해도 때때로 DOM이 형성되기 전에 로직이 넘어가서 예외가 발생하는 경우가 있었다.

그리고 최근의 웹페이지들은 아예 페이지 구조만 내려준 뒤, 실제 컨텐츠는 Ajax를 통해 조회 후 DOM을 갱신하는 경우도 많기 때문에,
implicitlyWait() 메서드만 믿었다가 페이지단에서 Ajax 통신중에 DOM 조회를 시도했다가 내용이 없어서 예외가 발생하는 경우도 왕왕 있다.

개인적으로는 웹사이트 특성에 따라서, 최초에 구조만 표시한 후 Ajax로 갱신하는 웹페이지에 경우에는 그냥
Sleep 메서드로 페이지가 완성될 때까지 기다리는게 제일 간편하고 좋은 결과를 얻을 수 있었다.

```java
Thread.sleep(10000);   // 10초 대기한다
```

### Reference

* [Introduction to Chrome Headless with Java](https://www.scrapingbee.com/blog/introduction-to-chrome-headless/)
* [How to get Selenium to wait for a page to load](https://www.browserstack.com/guide/selenium-wait-for-page-to-load)