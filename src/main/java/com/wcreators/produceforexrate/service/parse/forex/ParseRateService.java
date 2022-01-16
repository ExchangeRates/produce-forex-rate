package com.wcreators.produceforexrate.service.parse.forex;

import com.wcreators.produceforexrate.constant.Resource;
import com.wcreators.produceforexrate.constant.ResourceAction;
import com.wcreators.produceforexrate.models.Rate;

import java.util.List;

public interface ParseRateService {
    List<Rate> parse() throws Exception;

    void reload();

    Resource getResource();

    default ResourceAction getResourceAction() {
        return ResourceAction.PARSE;
    }
}
