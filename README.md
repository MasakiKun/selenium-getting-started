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

2. Selenium이 브라우저를 제어할 수 있게 해주는 프로그램을 웹 드라이버라고 부른다.
크롬 웹 드라이버를 다운로드해서, 적당한 경로에 압축을 풀어둔다. 여기서는 크롬 외 브라우저의 웹 드라이버도 명시해둔다.

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
   만약 4번에서 ChromeOptions 인스턴스를 생성했다면, 생성자 인자로 이 인스턴스를 전달해준다.

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
```By.xpath()```를 이용하면 되는데, 이 문서에서는 생략한다.

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

~~하지만 솔까말 jQuery 쓰면 그냥 되는걸 이렇게 번잡하게...~~

이때, WebDriver 인터페이스는 스크립트를 실행할 수 있는 ```executeScript()``` 메서드를 제공하지 않으므로,
반드시 ```ChromeDriver``` 클래스로 캐스팅해야 한다.

```java
((ChromeDriver)driver).executeScript("document.getElementById('do-not-need-element').remove()");
```

### 현재 표시중인 웹페이지 URL 조회

WebDriver의 ```getCurrentUrl()``` 메서드를 이용해서 현재 표시중인 페이지의 URL을 조회할 수 있다.
로그인 페이지를 제어해서 로그인 시도 후, 로그인 성공 페이지로 전환되었는지 확인할 때 등에 사용해 볼 수 있겠다.

```java
System.out.println(driver.getCurrentUrl());
```

### 페이지 로딩 기다리기

사실 웹의 로직 처리라는 것이 기본적으로 비동기적으로 동작하기 때문에, Selenium의 웹 드라이버는 웹페이지의 처리중 상태를 확인하지는 않는다.
이건 말하자면, ```WebDriver.get()``` 메서드가 완료된 뒤에 ```WebDriver.findElement()``` 메서드를 호출한다 해도,
```get()``` 메서드가 완료된 시점에서 페이지의 로드가 끝나지 않았을수도 있음을 가리킨다.

사실 Selenium의 공식 문서를 살펴봐도, ```WebDriver.get()``` 메서드가 실행 완료되었을 때, 페이지의 로딩이 완료됬다고 가정해도 되는지는
명시되어 있지 않다. 그냥 웹사이트를 연다고만 적혀있을 뿐이다(Ref. [Browser navigation](https://www.selenium.dev/documentation/webdriver/browser/navigation/)).

게다가 근래의 웹은 웹페이지의 구조만 표시하고, 컨텐츠는 ajax로 받아와서 페이지를 갱신하는 구조일 수도 있다. 예를 들어, 아래 웹페이지를 생각해보자.

```html
<html>
  <head></head>
<body>
</body>
<script>
  window.addEventListener("load", () => {
    let req = new XMLHttpRequest();
    req.open("GET", "http://real.awesome.greet.word");
    req.onreadystatechange = function() {
      if(req.readyState == 4) {
        const newParagraph = document.createElement("p");
        newParagraph.textContent = req.responseText;
        newParagraph.id = "greet";
        document.body.appendChild(newParagraph);
      }
    };
    req.send();
  });
</script>
</html>
```

위와 같은 페이지 구조라면, 설령 ```WebDriver.get()``` 메서드가 페이지 로딩이 끝난 다음에 처리가 완료된다 하더라도,
http://real.awesome.greet.word API의 호출이 끝나기 전까지는 ID가 greet인 요소가 생성되어 있지 않았을 것이다.
이 시점에서 ```WebDriver.findElement(By.id("result"))``` 메서드를 실행하면 요소를 찾지 못하고 예외가 발생할 것이다.

이런 케이스의 해소를 위해, Selenium에서는 3가지의 대기 방법을 제공한다.
명시적 대기(Explicit wait)와 암시적 대기(Implicit wait), 능숙한 대기(Fluent wait)가 그것이다.

#### 명시적 대기 (Explicit wait)

명시적 대기는 특정 요소를 찾아낼 수 있을때까지, 지정된 시간 동안 대기한다.
일정시간 대기 후 해당 요소를 다시 탐색하고, 탐색에 성공하면 해당 요소를 반환한다.
대기중에 해당 요소를 찾아내면 대기를 즉시 중단하고, 찾아낸 요소를 반환한다.
대기시간 안에 요소를 찾아내지 못했다면 예외가 발생한다.

명시적 대기를 위해서는 ```WebDriverWait``` 클래스의 인스턴스를 이용한다.

```java
// #result 요소를 찾기 위해 30초간 대기한다.
// 30초가 지날때까지 #result를 찾지 못하면 예외가 발생한다.
WebElement el = new WebDriverWait(driver, Duration.ofSeconds(30))
        .until(drv -> drv.findElement(By.id("result")));
```

이 명시적 대기는 웹페이지 테스트 자동화에 써먹을 만한거 같은데, 실제로도 공식 문서에 테스트에 써먹을수 있을법한 단정문이 있는 것 같다(확인은 안해봄).

자세한건 후술할 공식문서를 참고하자.

#### 암시적 대기 (Implicit wait)

암시적 대기는 요소를 찾가 위해서, 지정된 시간 동안 폴링을 계속한다.
지정된 시간 동안 해당 요소를 찾아내면 반환하고, 찾아내지 못하면 예외가 발생하는 것은 명시적 대기와 동일하다.
명시적 대기와 다른 점은, 명시적 대기는 지정한 요소를 찾을 때의 대기 시간을 직접 지정하는 것이고, 암시적 대기는
어떤 요소이든 ```WebDriver.findElement()```로 요소를 찾을때마다 대기한다는 점이다.

암시적 대기를 위해서는 페이지를 표시하기 전에 WebDriver에 대기 시간을 직접 지정한다.
이후에는 ```WebDriver.findElement()``` 메서드가 호출될 때마다 대기시간동안 요소를 찾기 위해 대기한다.

```java
WebDriver driver = new ChromeDriver();
// 요소를 찾을때마다 암묵적으로 10초간은 계속 탐색한다.
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
driver.get("this.is.awesome.site");

// 암묵적 암시 10초가 지정되었으므로, 10초간 #result 요소를 계속 탐색한다.
WebElement element = driver.findElement(By.id("result"));
```

#### 능숙한 대기 (Fluent wait)

능숙한 대기는 요소를 찾는 조건을 세밀하게 조절할 수 있는 대기 방식이다.

요소를 찾는데 걸릴 최대 타임아웃이나 폴링 주기를 설정할 수 있고, 특정 예외는 무시하도록 설정할 수도 있다.

능숙한 대기는 제네릭 구성된 ```Wait``` 클래스의 인스턴스를 사용한다.

```java
Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
        .withTimeout(Duration.ofSeconds(30))      // 최대 30초간 탐색
        .pollingEvery(Duration.ofSeconds(5))      // 5초간 주기적으로 폴링
        .ignoring(NoSuchElementException.class);  // 탐색중 NoSuchElementException이 발생할 경우 무시

// 위에서 지정한 조건에 맞춰서 요소를 탐색한다.
WebElement element = wait.until(drv -> drv.findElement(By.id("result")));
```

위에서 언급한 Selenium의 3가지 대기 방법에 대해서는 [공식문서](https://www.selenium.dev/documentation/webdriver/waits/)를 참고하자.

#### 그런데 개인적으로는...

그런데 사실 개인적으로는 그냥 ```Thread.sleep()``` 메서드로 페이지가 완성될 때까지 기다리는게 제일 간편했다-_-

걍 대기탄 뒤에 요소 찾고, 요소를 못찾으면 try~catch 문에서 예외를 잡아낸 다음 다시 기다리는 식...

```java
Thread.sleep(10000);   // 10초 대기한다
```

### Reference

* [Selenium Documentation](https://www.selenium.dev/documentation/overview/)
* [Introduction to Chrome Headless with Java](https://www.scrapingbee.com/blog/introduction-to-chrome-headless/)
* [How to get Selenium to wait for a page to load](https://www.browserstack.com/guide/selenium-wait-for-page-to-load)
