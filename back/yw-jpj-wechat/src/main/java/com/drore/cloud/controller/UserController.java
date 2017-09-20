package com.drore.cloud.controller;

import com.drore.cloud.constant.ConstantEnum;
import com.drore.cloud.model.AddressInfo;
import com.drore.cloud.sdk.client.CloudQueryRunner;
import com.drore.cloud.sdk.common.annotation.Login;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.service.AddressService;
import com.drore.cloud.service.InteractiveService;
import com.drore.cloud.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by 仁杰 on 2017/9/7
 */
@Api(value = "用户模块——何仁杰")
@RestController
@RequestMapping("/wechat/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private InteractiveService interactiveService;


//***********************用户评价************

    //评价列表
//    public Pagination<Map> evaluateList(@RequestParam(value = "current_page",defaultValue = "1") Integer current_page,
//                                        @RequestParam(value = "page_size",defaultValue = "5") Integer page_size){
//        return null;
//    }

//*******************我的收藏*********************
    @Login
    @ApiOperation(value = "用户收藏——何仁杰")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodity_id",value = "店铺/商品id",dataType = "string",required = true),
            @ApiImplicitParam(name = "collection_type",value = "收藏类型(Store:店铺;Commodity:商品)",dataType = "string",required = true),
            @ApiImplicitParam(name = "is_collection",value = "用户是否收藏",dataType = "string",required = true)
    })
    @PostMapping("/collection")
    public RestMessage collection(@RequestParam("commodity_id") String commodity_id,
                                  @RequestParam("collection_type") String collection_type,
                                  @RequestParam("is_collection") String is_collection){
        return userService.collection(commodity_id,collection_type,is_collection);
    }

    @Login
    @ApiOperation(value = "我的收藏——何仁杰",notes = "返回参数:店铺类型对应取值对象：store_info;商品类型对应取值")
    @ApiImplicitParam(name = "collection_type",value = "收藏类型(默认为店铺:Store;商品:Commodity)")
    @PostMapping("/userCollection")
    public Pagination<Map> userCollection(@RequestParam(value = "collection_type",defaultValue = "Store") String collection_type,
                                          @RequestParam(value = "current_page",defaultValue = "1") Integer current_page,
                                          @RequestParam(value = "page_size",defaultValue = "5") Integer page_size){
        return userService.userCollection(collection_type,current_page,page_size);
    }

    @Login
    @ApiOperation(value = "我的互动——张豪",notes = "返回参数:互动内容对应取值对象：interactive_content;互动内容对应取值")
    @PostMapping("/userInteractive")
    public RestMessage userInteractive(@RequestParam(value = "current_page",defaultValue = "1") Integer current_page,
                                           @RequestParam(value = "page_size",defaultValue = "5") Integer page_size){
        return interactiveService.getInteractiveContentListByPublisher(page_size,current_page);
    }

    //@Login
    @ApiOperation(value = "我的消息列表——张豪",notes = "返回参数:消息详情对应取值对象：MessageInfo;消息详情对应取值")
    @PostMapping("/getUserMessageList")
    public RestMessage getUserMessageList(@RequestParam(value = "current_page",defaultValue = "1") Integer current_page,
                                          @RequestParam(value = "page_size",defaultValue = "5") Integer page_size){
        return userService.getUserMessageList(current_page,page_size);
    }

    //@Login
    @ApiOperation(value = "查询消息详情——张豪",notes = "返回参数:消息详情对应取值对象：MessageInfo;消息详情对应取值")
    @ApiImplicitParam(name = "id",value = "消息主键",dataType = "string",required = true)
    @PostMapping("/getUserMessage")
    public RestMessage getUserMessage(@RequestParam("id") String id){

        return userService.getUserMessage(id);
    }

    @Login
    @ApiOperation(value = "删除消息——张豪",notes = "返回参数:删除成功或失败参数")
    @ApiImplicitParam(name = "id",value = "消息主键",dataType = "string",required = true)
    @PostMapping("/deleteUserMessage")
    public RestMessage deleteUserMessage(@RequestParam("id") String id){

        return userService.deleteUserMessage(id);
    }

    @Login
    @ApiOperation(value = "个人信息——张豪",notes = "返回参数:个人信息对应取值对象：member_info;个人信息对应取值")
    @PostMapping("/userInfo")
    public RestMessage userInfo(){
       return userService.userInfo();
    }

    @Login
    @ApiOperation(value = "修改个人信息（昵称）——张豪",notes = "返回参数:修改成功或失败参数")
    @ApiImplicitParam(name = "nickName",value = "昵称",dataType = "string",required = true)
    @PostMapping("/updateUserInfo")
    public RestMessage updateUserInfo(@RequestParam("nickName") String nickName){

        return userService.updateUserNickName(nickName);
    }

    @Login
    @ApiOperation(value = "修改个人信息（电话）——张豪",notes = "返回参数:修改成功或失败参数")
    @ApiImplicitParam(name = "tel",value = "电话",dataType = "string",required = true)
    @PostMapping("/updateUserTel")
    public RestMessage updateUserTel(@RequestParam("tel") String tel){
        return userService.updateUserTel(tel);
    }

//*******************地址管理*********************
//    @Login
    @ApiOperation(value = "获取用户地址列表——何仁杰",notes = "登录后获取用户地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current_page",value = "当前页",dataType = "int",required = true),
            @ApiImplicitParam(name = "page_size",value = "每页的条数",dataType = "int",required = true)})
    @RequestMapping(value = "/addressList",method = RequestMethod.POST)
    public Pagination<AddressInfo> addressList(@RequestParam(value = "current_page",defaultValue = "1") int current_page
            , @RequestParam(value = "page_size",defaultValue = "5") int page_size){
        return addressService.findMyAddress(current_page,page_size);
    }


    @Login
    @ApiOperation(value = "新增或者更新地址——何仁杰",notes = "新增或者更新地址")
    @RequestMapping(value = "/saveUpdate",method = RequestMethod.POST)
    public RestMessage saveOrUpdate(@ModelAttribute AddressInfo addressInfo){
        return addressService.save(addressInfo);
    }


    @Login
    @ApiOperation(value = "删除地址——何仁杰",notes = "删除地址接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "addressId",value = "地址ID",dataType = "String",required = true)})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public RestMessage saveOrUpdate(@RequestParam String addressId){
        return addressService.delete(addressId);
    }

    @Login
    @ApiOperation(value = "设置默认地址——何仁杰")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "地址id",dataType = "String",required = true)})
    @RequestMapping(value = "/defaultAddress",method = RequestMethod.POST)
    public RestMessage defaultAddress(@RequestParam("id") String id){
        return addressService.defaultAddress(id);
    }

//*******************消息通知*********************



}
