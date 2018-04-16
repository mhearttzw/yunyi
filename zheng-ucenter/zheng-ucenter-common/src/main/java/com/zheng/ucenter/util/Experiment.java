package com.zheng.ucenter.util;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Experiment {

    public static void main(String[] args) {
        JSONObject params = new JSONObject();

        List lst = new ArrayList();
        lst.add("dfaf");
        lst.add("二分法");

        params.put("coverImg", "fsgfsgsgsgs");
        params.put("img", lst);

        System.out.println(params);
    }
}
