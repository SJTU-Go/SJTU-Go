package org.sjtugo.api.entity;

import lombok.Data;

import java.util.List;

@Data
public class NavigateRequest {
    private String BeginPlaceID;
    private String ArrivePlaceID;
    private List<String> PassPlaceIDs;
}
