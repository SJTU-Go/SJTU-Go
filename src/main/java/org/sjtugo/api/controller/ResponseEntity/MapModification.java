package org.sjtugo.api.controller.ResponseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.entity.Modification;

@Data
public class MapModification {
    @ApiModelProperty(value = "修改详情")
    private Modification modification;
    @ApiModelProperty(value = "地点信息")
    private MapVertexInfo mapVertexInfo;

}
