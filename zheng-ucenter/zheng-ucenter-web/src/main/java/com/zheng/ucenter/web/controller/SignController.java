package com.zheng.ucenter.web.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.zheng.common.base.BaseController;
import com.zheng.common.util.MessageUtil;
import com.zheng.ucenter.common.constant.UcenterResult;
import com.zheng.ucenter.common.constant.UcenterResultConstant;
import com.zheng.ucenter.dao.cache.RedisDao;
import com.zheng.ucenter.dao.model.UcenterUser;
import com.zheng.ucenter.rpc.api.UcenterUserService;
//import com.zheng.ucenter.util.CommonUtil;
import com.zheng.ucenter.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPPart;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 注册控制器
 * Created by shuzheng on 2017/5/2.
 */
@RestController
@RequestMapping("/api")
public class SignController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignController.class);

    @Autowired
    private UcenterUserService ucenterUserService;

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        return thymeleaf("/reg");
    }


    /**
     * 用户注册
     * @param phone
     * @param password
     * @param nickname
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public UcenterResult signin(@RequestParam("phone") String phone,
                                @RequestParam("password") String password,
                                @RequestParam("nickname") String nickname) {
        UcenterResult urs;
        String saveCode = ucenterUserService.getCode(phone);
        // 检查昵称是否重复
        if (ucenterUserService.nicknameUnique(nickname)) {
            urs = new UcenterResult(UcenterResultConstant.NICKNAME_REPEAT, "");
        } else if (ucenterUserService.exists(phone)) {
            //1、用户已存在
            urs = new UcenterResult(UcenterResultConstant.USER_EXISTS, "");
        } else if ( !saveCode.equals("check") ) {
            //2、验证码不正确
            urs = new UcenterResult(UcenterResultConstant.CODE_ERROR, "");
        } else {
            // 生成用户uuid
            String userUuid = UUID.randomUUID().toString();
            // 用户信息插入数据库
            int v = ucenterUserService.insertSimple(phone, password, nickname, userUuid);
            if (v == 1) {
                // 如果注册通过，生成一个32位的token
                String token = ucenterUserService.saveToken1(userUuid);
                // 将这个token返回给前端
                Map<String, String> tokenMap = new HashMap<String, String>();
                tokenMap.put("token", token);
                //3、成功注册
                urs = new UcenterResult(UcenterResultConstant.SUCCESS, tokenMap);
            } else {
                //4、注册失败
                urs = new UcenterResult(UcenterResultConstant.FAILED, "注册失败");
            }
        }
        return urs;
    }

    /**
     * 发送短信验证码
     * @param phone
     * @return
     * @throws ClientException
     */
    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    public UcenterResult smsLogin(@RequestParam("phone") String phone) throws ClientException {
        UcenterResult urs;
        String random=(int)((Math.random()*9+1)*100000)+"";
        SendSmsResponse sendSmsResponse = MessageUtil.sendSms(random, phone);
        System.out.println("========:" + sendSmsResponse);
        System.out.println("===========code:" + sendSmsResponse.getCode());
        if (sendSmsResponse.getCode().equals("OK")) {
            ucenterUserService.saveMessageCode(phone, random, "expire");
            urs = new UcenterResult(UcenterResultConstant.SUCCESS, "已发送！");
        } else {
            urs = new UcenterResult(UcenterResultConstant.FAILED, "短信验证码发送失败！");
        }
        return urs;

    }

    /**
     * 用户验证码登录接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/sms/login", method = RequestMethod.POST)
    @ResponseBody
    public UcenterResult smsLogin(HttpServletRequest request, HttpServletResponse response) {
        String phone = request.getParameter("phone");
        String code = request.getParameter("messageCode");
        UcenterResult urs = null;
        //1、用手机号取出保存的短信验证码，能取到，并且与提交上来的code值相同则为校验通过
        String saveCode = ucenterUserService.getCode(phone);
        if (!saveCode.equals(code)) {
            urs = new UcenterResult(UcenterResultConstant.CODE_ERROR, "");
        } else {
            ucenterUserService.saveMessageCode(phone, "check", "noExpire");
            if (!ucenterUserService.exists(phone)) {
                urs = new UcenterResult(UcenterResultConstant.USER_NOT_EXISTS, "");
            } else {
                //2、从数据库获取用户uuid
                String userUuid = ucenterUserService.getUserUuid(phone);
                //3、生成一个32位的token作为登录凭证
                String token = ucenterUserService.saveToken1(userUuid);
                //4、将这个token返回给前端
                Map<String, String> tokenMap = new HashMap<String, String>();
                tokenMap.put("token", token);
                urs = new UcenterResult(UcenterResultConstant.SUCCESS, tokenMap);
            }
        }
        return urs;
    }

    /**
     * 手机号密码登录接口
     * @param req
     * @return
     */
    @RequestMapping(value = "/username/login", method = RequestMethod.POST)
    @ResponseBody
    public UcenterResult phoneLogin(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UcenterResult urs = null;
        if (!ucenterUserService.exists(username)) {
            urs = new UcenterResult(UcenterResultConstant.USER_NOT_EXISTS, "");
        } else if (ucenterUserService.isUser(username, password) || ucenterUserService.nickMatchPwd(username, password)){
            // 生成用户uuid
            String userUuid = UUID.randomUUID().toString();
            //1、如果校验通过，生成一个32位的token
            String token = ucenterUserService.saveToken1(userUuid);
            //4、将这个token返回给前端
            Map<String, String> tokenMap = new HashMap<String, String>();
            tokenMap.put("token", token);
            urs = new UcenterResult(UcenterResultConstant.SUCCESS, tokenMap);
        } else {
            urs = new UcenterResult(UcenterResultConstant.PASSWORD_ERROR, "");
        }
        return urs;
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    @ResponseBody
    public UcenterResult signOut(HttpServletRequest request) {
        String userToken = request.getHeader("user_token");
        UcenterResult urs;
        if (userToken == null) {
            urs = new UcenterResult(UcenterResultConstant.FAILED, "用户未登录！");
        } else {
            Long result = ucenterUserService.deleteToken(userToken);
            if (result == 0) {
                urs = new UcenterResult(UcenterResultConstant.FAILED, "用户未登录！");
            } else {
                urs = new UcenterResult(UcenterResultConstant.SUCCESS, result);
            }
        }
        return urs;
    }

    /**
     * 用户修改密码
     * @param newPassword
     * @param request
     * @return
     */
    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    @ResponseBody
    public UcenterResult passwordReset(@RequestParam("newPassword") String newPassword, @RequestParam("userName") String userName, HttpServletRequest request) {
        String userToken = request.getHeader("user_token");
        UcenterResult urs;
        if (userToken == null) {
            urs = new UcenterResult(UcenterResultConstant.FAILED, "用户未登录！");
        } else {
            int result = ucenterUserService.updatePassword(newPassword, userName);
            if (result == 0) {
                urs = new UcenterResult(UcenterResultConstant.FAILED, "用户未登录！");
            } else {
                urs = new UcenterResult(UcenterResultConstant.SUCCESS, result);
            }
        }
        return urs;
    }

}