package org.sjtugo.api.service;

import org.sjtugo.api.entity.Strategy;

public abstract class AbstractPlanner {

    public abstract Strategy planOne(String beginPlace, String endPlace);

    public Strategy planAll(String beginPlace, String[] passPlaces, String endPlace){
        String currentPlace = beginPlace;
        String nextPlace = passPlaces.length>0 ? passPlaces[0] : endPlace;
        int i;
        Strategy resultStrategy = planOne(currentPlace,nextPlace);
        for (i=0; i<passPlaces.length; i++) {
            Strategy currentStrategy = planOne(currentPlace,nextPlace);
            resultStrategy.merge(currentStrategy);
            currentPlace = passPlaces[i];
            nextPlace = i+1<passPlaces.length ? passPlaces[i+1] : endPlace;
        }
        return resultStrategy;
    }
}