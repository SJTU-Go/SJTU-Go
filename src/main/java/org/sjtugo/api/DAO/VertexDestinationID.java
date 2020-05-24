package org.sjtugo.api.DAO;


import lombok.Data;

import java.io.Serializable;

@Data
public class VertexDestinationID implements Serializable {
    private Integer placeid;

    private Integer vertexid;

    public VertexDestinationID(){

    }

    public VertexDestinationID(Integer placeid, Integer vertexid){
        this.placeid = placeid;
        this.vertexid = vertexid;
    }
}
