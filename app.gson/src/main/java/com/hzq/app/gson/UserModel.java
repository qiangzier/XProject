package com.hzq.app.gson;

import com.gson.JsonSerializable;

import java.io.Serializable;

/**
 * @author: hezhiqiang
 * @date: 17/4/24
 * @version:
 * @description:
 */

@JsonSerializable
public class UserModel implements Serializable{
    public String id;
    public String name;
    public String address;
}
