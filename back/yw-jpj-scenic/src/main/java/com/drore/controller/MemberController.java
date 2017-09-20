package com.drore.controller;

        import com.alibaba.fastjson.JSONObject;
        import com.drore.cloud.sdk.common.annotation.JsonArgument;
        import com.drore.cloud.sdk.common.resp.RestMessage;
        import com.drore.service.MemberService;
        import com.drore.util.JSONObjResult;
        import com.google.gson.JsonObject;
        import io.swagger.annotations.Api;
        import io.swagger.annotations.ApiImplicitParam;
        import io.swagger.annotations.ApiImplicitParams;
        import io.swagger.annotations.ApiOperation;
        import org.apache.commons.lang.StringUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.*;

/**
 * Created by wangl on 2017/8/31 0031.
 */
@Api(value = "会员管理")
@RestController
@RequestMapping("/cms/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "会员列表-王璐",notes = "会员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "当前页",dataType = "int",required = true),
            @ApiImplicitParam(name = "pageSize",value = "页码大小",dataType = "int",required = true),
            @ApiImplicitParam(name = "nick_name",value = "昵称",dataType = "int",required = true),
            @ApiImplicitParam(name = "startTime",value = "开始时间",dataType = "int",required = true),
            @ApiImplicitParam(name = "endTime",value = "结束时间",dataType = "int",required = true)
    })
    @PostMapping("/getMemberList")
    @ResponseBody
    public JSONObject getUserList(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @JsonArgument JsonObject params){
        RestMessage userList = memberService.getUserList(pageSize, pageNo, params);
        return JSONObjResult.toJSONObj(userList);
    }

    @ApiOperation(value = "会员收货地址-王璐",notes = "用户收货地址")
    @ApiImplicitParam(name = "memberId",value = "会员主键",dataType = "String",required = true)
    @PostMapping("/getMemberAddress")
    @ResponseBody
    public JSONObject getMemberAddress(String memberId){
        RestMessage memberAddress = memberService.getMemberAddress(memberId);
        return JSONObjResult.toJSONObj(memberAddress);
    }

    @ApiOperation(value = "会员详情-张豪",notes = "用户详情")
    @ApiImplicitParam(name = "memberId",value = "会员主键",dataType = "String",required = true)
    @PostMapping("/getMember")
    @ResponseBody
    public JSONObject getMember(String memberId){
        if(StringUtils.isEmpty(memberId)){
            return JSONObjResult.toJSONObj("主键不能为空");
        }
        RestMessage member = memberService.getMember(memberId);
        return JSONObjResult.toJSONObj(member);
    }
}
