package org.sjtugo.api.entity;

import lombok.Data;

@Data
public class TransitionRoute extends Route {
    private String placeID;

    private String parkID;

    private boolean isDepart;
}
