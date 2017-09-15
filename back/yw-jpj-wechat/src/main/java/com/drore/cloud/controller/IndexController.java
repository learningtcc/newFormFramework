package com.drore.cloud.controller;

import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.sdk.domain.Pagination;
import com.drore.cloud.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * Created by 仁杰 on 2017/9/5
 */
@Api(value = "掌上商城——何仁杰")
@RestController
@RequestMapping("/wechat/shop")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @ApiOperation(value = "商城首页——何仁杰",notes = "返回参数data:{广告信息:advertising_list;特色商品:feature_list;热门商铺:store_list}")
    @PostMapping("/index")
    public RestMessage index(){
        return indexService.index();
    }

    //网上商铺
    @ApiOperation(value = "网上商铺列表——何仁杰",notes = "查询出售客户指定茶的商铺列表")
    @ApiImplicitParam(name = "id",value = "茶种类主键id",dataType = "string",required = true)
    @PostMapping("/onlineList")
    public RestMessage online(@RequestParam("id") String id,@RequestParam(value = "current_page",defaultValue = "1") Integer current_page,
                                  @RequestParam(value = "page_size", defaultValue = "4") Integer page_size){
        return indexService.online(id,current_page,page_size);
    }
}
