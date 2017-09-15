/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */

package com.drore.cloud.controller;

/***
 * 微信授权相关接口
 * @since:cloud-ims 1.0
 * @author <a href="mailto:baoec@drore.com">baoec@drore.com </a>
 * 2016/11/21 8:42
 */

import com.drore.cloud.constant.ConstantEnum;
import com.drore.cloud.constant.LocalConstant;
import com.drore.cloud.model.MemberInfo;
import com.drore.cloud.model.config.SystemConfig;
import com.drore.cloud.model.config.WxConfig;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.common.security.DES;
import com.drore.cloud.sdk.common.util.CommonUtil;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.sdk.domain.metadata.io.MaterialInfo;
import com.drore.cloud.sdk.util.EmojiFilter;
import com.drore.cloud.sdk.util.LogbackLogger;
import com.drore.cloud.service.MaterialService;
import com.drore.cloud.util.PackageConfigUtils;
import com.drore.cloud.wx.client.CloudWeixinService;
import com.drore.cloud.wx.model.AuthWeixinToken;
import com.drore.cloud.wx.model.WeixinUserInfo;
import com.drore.cloud.wx.utils.WeixinUtils;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@Api("微信授权")
@Controller
@RequestMapping("/macro/weixin")
public class WxApiController {

    @Autowired
    private CloudQueryRunner run;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CloudWeixinService wxService;

    /***
     * 授权url地址,获取用户
     * @param url
     * @param request
     * @param response
     */

    @ApiOperation(value = "授权url地址",notes = "获取用户")
    @ApiImplicitParam(name = "url",value = "微信授权后需要跳转的url",dataType = "string",required = true)
    @GetMapping(value = "/auth")
    public void auth(@RequestParam(value="url") String url, HttpServletRequest request, HttpServletResponse response){
        System.out.println("url+++++++++++++++"+url);
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        StringBuffer codeUrl=new StringBuffer("https://open.weixin.qq.com/connect/oauth2/authorize");
        codeUrl.append("?appid={0}")
                .append("&redirect_uri={1}")
                .append("&response_type=code")
                .append("&scope={2}")
                .append("&state={3}#wechat_redirect");
        SystemConfig systemConfig= PackageConfigUtils.getSystemConfig();
        String redurl=systemConfig.getWxConfig().getRedirectUrl();
        //
        url = DES.encrypt(url);
        if (StringUtils.indexOf(redurl,"?")>0){
            redurl+="&redirecturi="+url;
        }else{
            redurl+="?redirecturi="+url;
        }
        try {
            String realRedsendurl= MessageFormat.format(codeUrl.toString(),
                    systemConfig.getWxConfig().getAppid()
                    , URLEncoder.encode(redurl, Charsets.UTF_8.name()),
                    "snsapi_userinfo","123");
            LogbackLogger.info("重定向地址url:"+realRedsendurl);
            response.sendRedirect(realRedsendurl);
        } catch (UnsupportedEncodingException e) {
            LogbackLogger.error(e);
        } catch (IOException e) {
            LogbackLogger.error(e);
        }
    }



    /***
     * 重定向跳转链接url
     * @param code 微信回调参数code
     * @param redirecturi 前端重定向url跳转地址
     * @param request
     * @param response
     */

    @RequestMapping(value = "/getUserInfo")
    public void getUserInfo(@RequestParam(value="code") String code, @RequestParam(value="redirecturi") String redirecturi, HttpServletRequest request, HttpServletResponse response){
        SystemConfig systemConfig= PackageConfigUtils.getSystemConfig();
        System.out.println("-----------微信回调参数code:"+code);
        WxConfig wxConfig=systemConfig.getWxConfig();
        redirecturi = DES.decrypt(redirecturi);
        try {
            ///JSONObject jsonObject = WxAccessTokenAPI.getAccessTokenOAuth(wxConfig.getAppid(),wxConfig.getAppsecret(),code);
            AuthWeixinToken auth = wxService.auth(code);
            //第二步：通过code换取网页授权access_token
            String access_token=auth.getAccessToken();
            //update by 鲍恩撑   单个公众号取openid/多个平台公用 取 unionid
            String openid=auth.getOpenid();

            //拉取用户信息(需scope为 snsapi_userinfo)
            WeixinUserInfo userInfoJO = wxService.authuserInfo(auth);
            LogbackLogger.info("userInfoJO---------"+userInfoJO);
            LogbackLogger.info("userInfoJO---------"+new Gson().toJson(userInfoJO));
            String user_openid=userInfoJO.getOpenid();
            String user_nickname=userInfoJO.getNickname();
            //判断特殊字符
            if (EmojiFilter.containsEmoji(user_nickname)){
                user_nickname=EmojiFilter.filterEmoji(user_nickname);
            }
            String user_sex=userInfoJO.getSex();
            if("0".equals(user_sex)) {
                user_sex = "未知";
            }else if("1".equals(user_sex)) {
                user_sex = "男";
            }else {
                user_sex = "女";
            }
            String user_city=userInfoJO.getCity();
            String user_headimgurl=userInfoJO.getHeadimgurl();

            String url = redirecturi;
//            user_nickname = encodeUrl(user_nickname);
//            String params = "userId="+user_openid+"&nickname="+user_nickname+"&headimgurl="+user_headimgurl;
            String params = "openid="+user_openid;
            if(redirecturi.indexOf("?") != -1){
                url = redirecturi+"&"+params;
            }else{
                url = redirecturi+"?"+params;
            }
            //判断用户是否登录
            if(request.getSession().getAttribute(LocalConstant.SESSION_CURRENT_USER)==null){
                Map<String,Object> term= Maps.newHashMap();
                term.put("open_id",user_openid);
                Pagination<MemberInfo> pagination=run.queryListByExample(MemberInfo.class,"member_info",term);
                if (pagination.getCount()>0){
                    MemberInfo minfo=pagination.getData().get(0);
                    //用户已存在,更新用户名
                    Map<String,Object> m=Maps.newHashMap();
                    m.put("nick_name",user_nickname);
                    //m.put("logo_image",user_headimgurl);
                    run.update("member_info",minfo.getId(),m);
                    request.getSession().setAttribute(LocalConstant.SESSION_CURRENT_USER,minfo);
                }else{
                    //用户不存在
                    //上传头像至自己服务器
                    System.out.println("=====================>>用户头像为："+user_headimgurl);
                    MaterialInfo mat = materialService.uploadUrl(user_headimgurl);
                    String uploadUrlUrl = mat.getUrl();
                    int dot = uploadUrlUrl.lastIndexOf(".");
                    uploadUrlUrl=uploadUrlUrl.substring(0,dot)+"_small."+uploadUrlUrl.substring(dot+1);
                    System.out.println("====================================>>用户的微信头像为:"+uploadUrlUrl);
                    //,注册
                    MemberInfo member = new MemberInfo();
                    member.setNickName(user_nickname);
                    member.setSex(user_sex);
                    member.setHeadImageUrl(uploadUrlUrl);
                    member.setCity(user_city);
                    member.setCountry(userInfoJO.getCountry());
                    member.setProvince(userInfoJO.getProvince());
                    member.setOpenId(userInfoJO.getOpenid());
//                    member.setLogoImage(uploadUrlUrl);
//                    member.setIsUse(ConstantEnum.ToggleEnum.Y.name());
//                    member.setIsActivity(ConstantEnum.ToggleEnum.Y.name());
//                    member.setLastLoginTime(DateFormatUtils.format(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm:ss"));
//                    member.setLastLoginIp(CommonUtil.getIpAddr(request));
//                    member.setWxAppid(openid);
                    //微信登录，不需要用户名
//                    member.setAccountNo(openid);
//                    member.setEmail("  ");
//                    member.setTel("  ");
//                    member.setPassword("123456");
                    RestMessage restMessage=run.insert("member_info",member);
                    if (restMessage.isSuccess()){
                        member.setId(restMessage.getId());
                        request.getSession().setAttribute(LocalConstant.SESSION_CURRENT_USER,member);
                    }
                }
            }
            response.sendRedirect(url);  //处理完成后提交到前台
        } catch (Exception e) {
            LogbackLogger.error(e);
        }
    }




    /***
     * wap地址签名,调用h5支付接口等
     * @param wap_url
     * @param request
     * @param response
     * @return
     */

    @ApiOperation(value = "wap地址签名",notes = "调用h5支付接口等")
    @ApiImplicitParam(name = "url",value = "当前页面的url",dataType = "string",required = true)
    @PostMapping(value = "/sign")
    @ResponseBody
    public RestMessage sign(@RequestParam(value="url") String wap_url, HttpServletRequest request, HttpServletResponse response){
        Map<String, String> params=new HashMap<String, String>();
        WxConfig wx=PackageConfigUtils.getSystemConfig().getWxConfig();
        //智能语音识别功能
        String openId = request.getParameter("userId");
        String headimgurl = request.getParameter("headimgurl");
        if(openId != null){
            params.put("openId",openId);
        }
        if(headimgurl != null){
            params.put("headimgurl",headimgurl);
        }

        String jsapi_ticket= wxService.getJsapiTicket();
        String noncestr= WeixinUtils.random(32);
        String url=wap_url;
        params.put("timestamp", ObjectUtils.toString(Calendar.getInstance().getTimeInMillis()));
        params.put("jsapi_ticket", jsapi_ticket);
        params.put("noncestr", noncestr);
        params.put("url", url);
        params.put("Signa", WeixinUtils.buildJsApiSign(params));
        params.put("appId", wx.getAppid());
        return new RestMessage(params);
    }
}
