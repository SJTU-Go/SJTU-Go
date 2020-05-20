package org.sjtugo.api.service.planner;

public class StrategyNotFoundException extends RuntimeException {
    StrategyNotFoundException (String msg) {
        super(msg);
    }
}
