package com.wcreators.produceforexrate.service.parse.engine;

import com.wcreators.produceforexrate.config.SeleniumConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseEngineChromeSeleniumService implements ParseEngineSeleniumService {

    private final SeleniumConfig seleniumConfig;
    private final ChromeOptions options = new ChromeOptions();
    private WebDriver driver;
    private WebDriverWait wait;

    @PostConstruct
    public void init() {
        log.debug("Start init selenium chrome driver");
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty(seleniumConfig.getDriver().getType(), seleniumConfig.getDriver().getPath());

        options.setHeadless(seleniumConfig.isHeadless());
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(2000, 4000));

        wait = new WebDriverWait(driver, seleniumConfig.getTimeoutSec());

        log.info("Selenium chrome driver init successfully");
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    @Override
    public WebDriverWait getWait() {
        return wait;
    }

    @Override
    public void reload() {
        log.debug("Start reload selenium chrome driver");
        destroy();
        init();
        log.info("Selenium chrome driver reload successfully");
    }

    @PreDestroy
    public void destroy() {
        log.debug("Start quit selenium chrome driver");
        driver.quit();
        log.info("Selenium chrome driver quit successfully");
    }
}
