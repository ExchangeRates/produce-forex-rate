package com.wcreators.produceforexrate.service.parse.loader;

import com.wcreators.produceforexrate.service.parse.engine.ParseEngineSeleniumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Slf4j
@RequiredArgsConstructor
@Service
public class ForexLoader implements LoaderService {

    private final ParseEngineSeleniumService parseEngine;
    private final int SLEEP_ON_WAIT_MILLS = 100;
    private final int MAX_WAIT_LOADER_MILLS = 10000;

    @Override
    public boolean isLoaderPresent() {
        return parseEngine.getWait().until(presenceOfElementLocated(By.className("spinner"))).isDisplayed();
    }

    @Override
    public boolean waitLoader() {
        log.debug("Start wait loader");
        long startWaitTime = currentTimeMills();
        while (isLoaderPresent()) {
            sleep(SLEEP_ON_WAIT_MILLS);
            long currentTime = currentTimeMills();
            if (currentTime - startWaitTime > MAX_WAIT_LOADER_MILLS) {
                log.error(String.format(
                        "Loader wait is too long, current time: %d, start time: %d, max: %d",
                        currentTime, startWaitTime, MAX_WAIT_LOADER_MILLS
                ));
                return false;
            }
        }
        long timeOfWaitLoader = (currentTimeMills() - startWaitTime) / 1000;
        log.info(String.format("Success wait loader with %d sec.", timeOfWaitLoader));
        return true;
    }

    private void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long currentTimeMills() {
        return System.currentTimeMillis();
    }
}
