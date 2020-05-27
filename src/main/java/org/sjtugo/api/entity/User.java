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
    private Integer id;

    @ApiModelProperty(value = "用户openID", example = "hi23guef")
    private String openid;

    @ApiModelProperty(value = "用户session_key", example = "834jf")
    private String session_key;

    @ApiModelProperty(value = "用户昵称", example = "asd")
    private String name;

    @ApiModelProperty(value = "用户偏好", example = "")
    private String preference;

    @ApiModelProperty(value = "用户行程", example = "")
    private String schedule;

    public User() {}
}
