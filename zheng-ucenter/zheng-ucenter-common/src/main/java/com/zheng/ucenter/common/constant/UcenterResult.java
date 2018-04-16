package com.zheng.ucenter.common.constant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zheng.common.base.BaseResult;

/**
 * ucenter系统常量枚举类
 * Created by shuzheng on 2017/4/26.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UcenterResult<T> extends BaseResult {

    private T data;

    public UcenterResult(UcenterResultConstant cmsResultConstant, T data) {
        super(cmsResultConstant.getCode(), cmsResultConstant.getMessage(), data);
    }

}
