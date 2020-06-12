package org.sjtugo.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.json.JSONObject;

import javax.persistence.*;


@Data
@Entity
@ApiModel(value = "用户信息")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "用户ID",example = "123")
    private Integer userID;

    @ApiModelProperty(value = "用户openID", example = "hi23guef")
    private String openId;

    @ApiModelProperty(value = "用户session_key", example = "834jf")
    private String sessionkey;

    @ApiModelProperty(value = "用户名", example = "nicolas")
    private String name;

    public User() {}
}
