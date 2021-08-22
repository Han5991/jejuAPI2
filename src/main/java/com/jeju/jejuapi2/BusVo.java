package com.jeju.jejuapi2;

import lombok.Data;
import org.json.JSONObject;

@Data
public class BusVo {
    String serviceKey;
    String cityCode;
    String nodeId;

    public BusVo(String params) {
        JSONObject jsonObject = new JSONObject(params);
        JSONObject jsonObjectAction = new JSONObject(jsonObject.get("action").toString());
        JSONObject jsonObjectParams = new JSONObject(jsonObjectAction.get("params").toString());

        this.serviceKey = jsonObjectParams.get("ServiceKey").toString();
        this.cityCode = jsonObjectParams.get("cityCode").toString();
        this.nodeId = jsonObjectParams.get("nodeId").toString();
    }
}
