package org.sjtugo.api.service.NavigateService;

public class StrategyNotFoundException extends RuntimeException {
    StrategyNotFoundException (String msg) {
        super(msg);
    }
}
