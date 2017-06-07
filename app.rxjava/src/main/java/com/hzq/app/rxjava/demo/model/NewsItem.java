package com.hzq.app.rxjava.demo.model;

/**
 * @author: hezhiqiang
 * @date: 17/4/26
 * @version:
 * @description:
 */

public class NewsItem {
    public String id;
    public String title;
    public String source;
    public String firstImg;
    public String mark;
    public String url;

    public NewsItem(String id,String title){
        this.id = id;
        this.title = title;
    }
}
