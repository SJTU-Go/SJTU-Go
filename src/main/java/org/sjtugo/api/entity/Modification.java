package org.sjtugo.api.entity;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@ApiModel("修改记录")
public class Modification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "记录ID",example = "1")
    private Integer modificationID;

    @ApiModelProperty(value = "修改者ID(管理员)", example = "1")
    private Integer adminID;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private LocalDateTime time;

    @ApiModelProperty(value = "具体修改信息",notes = "json格式的String")
    @Column(columnDefinition = "TEXT")
    private String contents;  //Json


    public JSONObject getContents() {
        return JSONObject.fromObject(contents);
    }

    public void setContents(JSONObject obj){
        contents = obj.toString();
    }

    public Modification() {}
}
