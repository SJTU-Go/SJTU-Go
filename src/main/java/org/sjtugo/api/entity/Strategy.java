package org.sjtugo.api.entity;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import java.time.Duration;
import java.util.List;

@Data
@ApiModel("返回方案详情")
public class Strategy {
    @ApiModelProperty(value = "方案类型", example = "哈罗单车")
    private String type;
    @ApiModelProperty(value = "出发地点名称", example = "上院215")
    private String depart;
    @ApiModelProperty(value = "到达地点名称", example = "121.3,31.2附近的位置")
    private String arrive;
    @ApiModelProperty(value = "途径地点名称",
            example = "[121.3;31.2附近的位置, 电院4号楼]")
    private List<String> pass;

    @ApiModelProperty(value = "方案总用时，单位为秒", dataType = "int",
            example = "423")
    private Duration travelTime;

    @ApiModelProperty(value = "方案总距离，单位为米", example = "589")
    private int distance;

    @ApiModelProperty(value = "方案总花费，单位为分", example = "150")
    private int cost;

    @ApiModelProperty(value = "用户查询时提交的个性化选项", example = "[避开拥堵, 允许禁停区]")
    private List<String> preference;

    @ApiModelProperty(value = "路线方案详情")
    private List<Route> routeplan;

    public void merge(Strategy nextStrategy){
        // pre-condition: self.arrive == nextStrategy.depart, nextStrategy.pass = {}
        pass.add(arrive);
        arrive = nextStrategy.getArrive();
        travelTime = travelTime.plus(nextStrategy.travelTime);
        distance += nextStrategy.distance;
        cost += nextStrategy.cost;
        routeplan.addAll(nextStrategy.routeplan);
    }


}
