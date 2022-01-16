package com.wcreators.produceforexrate.service.parse.forex;

import com.wcreators.produceforexrate.config.ForexConfig;
import com.wcreators.produceforexrate.constant.Resource;
import com.wcreators.produceforexrate.service.parse.loader.LoaderService;
import com.wcreators.produceforexrate.models.Rate;
import com.wcreators.produceforexrate.service.parse.engine.ParseEngineSeleniumService;
import com.wcreators.produceforexrate.service.date.DateProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseForexRateServiceSelenium implements ParseRateService {

    private final ParseEngineSeleniumService parseEngine;
    private final DateProcessingService<Date> dateUtils;
    private final LoaderService loader;
    private final ForexConfig forexConfig;

    private final int MAX_PARS_RETRY = 10;

    @PostConstruct
    public void init() {
        login();
        openRates();
        loadSpecificRate("EUR/USD");
    }

    @Override
    public List<Rate> parse() {
        Date startParsingDate = dateUtils.now();
        log.info(String.format("Parsing %s", getResource().getName()));

        Optional<List<String>> parsedStrings = parseRowsWithRetries();
        if (parsedStrings.isEmpty()) {
            log.error(
                    "Can not parsing {} rows with {} retries, reload...",
                    getResource().getName(), MAX_PARS_RETRY
            );
            reload();
            return Collections.emptyList();
        }
        log.info("Parsed: {}", parsedStrings.get().size());

        List<Rate> rates = splitResultByNameValue(parsedStrings.get(), startParsingDate);
        log.info("End Parsing {} with {}", getResource().getName(), rates.size());

        return rates;
    }

    @Override
    public void reload() {
        parseEngine.reload();
        init();
    }

    @Override
    public Resource getResource() {
        return Resource.FOREX;
    }

    private void login() {
        parseEngine.getDriver().get("https://www.forex.com/en/account-login/");
        parseEngine.getWait().until(presenceOfElementLocated(By.name("Username")))
                .sendKeys(forexConfig.getUser().getLogin());
        parseEngine.getWait().until(presenceOfElementLocated(By.name("Password")))
                .sendKeys(forexConfig.getUser().getPassword());
        String logInButtonXpath = "//button[contains(text(),'Log In')]";
        parseEngine.getWait().until(presenceOfElementLocated(By.xpath(logInButtonXpath)))
                .sendKeys(Keys.ENTER);
    }

    private void openRates() {
        sleep(5000);
        loader.waitLoader();
        String ratesMenuXpath = "//li[contains(text(), ' FX ')]";
        parseEngine.getWait()
                .until(presenceOfElementLocated(By.xpath(ratesMenuXpath)))
                .click();
        loader.waitLoader();
        sleep(5000);
    }

    private void loadSpecificRate(String rate) {
        String placeHolderFilterXpath = "//input[@placeholder='Filter markets']";
        parseEngine.getWait()
                .until(presenceOfElementLocated(By.xpath(placeHolderFilterXpath)))
                .sendKeys(rate);
        loader.waitLoader();
        sleep(5000);
    }

    private Optional<List<String>> parseRowsWithRetries() {
        String ratesRowXpath = "//div[@class = 'ag-body-viewport ag-layout-normal ag-row-no-animation']";
        int retriesLeft = MAX_PARS_RETRY;
        while (retriesLeft > 0) {
            try {
                String text = parseEngine.getWait()
                        .until(presenceOfElementLocated(By.xpath(ratesRowXpath))).getText();
                return Optional.of(Arrays.asList(text.split("\n")));
            } catch (Exception e) {
                log.warn(
                        "Can not parse {} rows, retries left: {}, error: {}",
                        getResource().getName(), retriesLeft, e.getMessage()
                );
                retriesLeft--;
                sleep(500);
            }
        }
        return Optional.empty();
    }

    private void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private List<Rate> splitResultByNameValue(List<String> parsedResult, Date createdDate) {
        int indexStartRateNames = -1;
        int indexStartRateValues = -1;
        Function<String, Boolean> isRateName = (str) -> str.matches("^\\w{3}/\\w{3}|\\w*\\s\\(.*\\)$");
        Function<String, Boolean> isRateValue = (str) -> str.matches("^-?\\d+,?\\d*\\.?\\d*$");
        for (int i = 0; i < parsedResult.size(); i++) {
            String value = parsedResult.get(i);
            if (indexStartRateNames == -1 && isRateName.apply(value)) {
                indexStartRateNames = i;
            }
            if (isRateValue.apply(value)) {
                indexStartRateValues = i;
                break;
            }
        }
        List<String> names = parsedResult.subList(indexStartRateNames, indexStartRateValues);
        List<String> values = parsedResult.subList(indexStartRateValues, parsedResult.size());
        int valuesForName = 6;
        return names.stream().reduce(new ArrayList<>(names.size()), (rates, name) -> {
            int startValuesForName = names.indexOf(name) * valuesForName;
            String[] majorMinor = name.split("/");
            if (majorMinor.length < 2) {
                log.warn("Can not create game with incorrect name: {}", name);
                return rates;
            }
            Double sell = Double.parseDouble(values.get(startValuesForName));
            Double buy = Double.parseDouble(values.get(startValuesForName + 1));
            rates.add(
                    Rate.builder()
                            .major(majorMinor[0])
                            .minor(majorMinor[1])
                            .sell(sell)
                            .buy(buy)
                            .createdDate(createdDate)
                            .build()
            );
            return rates;
        }, (first, second) -> {
            first.addAll(second);
            return first;
        });
    }
}
