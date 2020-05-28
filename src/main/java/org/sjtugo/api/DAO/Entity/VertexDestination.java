package org.sjtugo.api.DAO.Entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vertex_destination")
@IdClass(VertexDestinationID.class)
public class VertexDestination {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="placeid")
    @ApiModelProperty(value = "地图点ID", example = "141475")
    private Integer placeid;

    @Id
    @Column(name = "vertexid")
    @ApiModelProperty(value = "停车点ID", example = "141475")
    private Integer vertexid;


    @Column(name="reachtime")
    @ApiModelProperty(value = "到达时间", example = "60")
    private Integer reachtime;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((placeid == null) ? 0 : placeid.hashCode());
        result = prime * result + ((vertexid == null) ? 0 : vertexid.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        VertexDestination other = (VertexDestination) obj;
        if (placeid == null) {
            if (other.placeid != null) {
                return false;
            }
        } else if (!placeid.equals(other.placeid)) {
            return false;
        }
        if (vertexid == null) {
            return other.vertexid == null;
        } else {
            return vertexid.equals(other.vertexid);
        }
    }
}