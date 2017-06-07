package com.hzq.app.rxjava.demo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hezhiqiang
 * @date: 17/4/26
 * @version:
 * @description:
 */

public class Response {
    public String reason;
    public News result;
    public int error_code;


    public static Response create(){
        Response response = new Response();
        response.reason = "first reason";
        News news = new News();
        List<NewsItem> list = new ArrayList<>();
        list.add(new NewsItem("1","title1"));
        list.add(new NewsItem("2","title2"));
        list.add(new NewsItem("3","title3"));
        list.add(new NewsItem("4","title4"));
        list.add(new NewsItem("5","title5"));
        news.list = list;
        response.result = news;
        return response;
    }
}
