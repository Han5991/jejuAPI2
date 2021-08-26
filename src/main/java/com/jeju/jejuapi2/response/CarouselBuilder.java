package com.jeju.jejuapi2.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CarouselBuilder {
    private String type;
    private final JsonArray items = new JsonArray(); // 현재 1개만 가능
    private String description;
    private JsonObject profile;
    private final JsonArray buttons = new JsonArray(); // 1개 이상, 3개 이하
    private Integer price;
}
