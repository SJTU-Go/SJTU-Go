package org.sjtugo.api.service.planner;

public class StrategyNotFoundException extends RuntimeException {
    StrategyNotFoundException () {
        super("Strategy is not found");
    }
}
