package com.drore.domain.vo;

import com.drore.domain.store.StoreAuthority;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

/**
 * 浙江卓锐科技股份有限公司 版权所有  Copyright 2017<br/>
 * 说明:     <br/>
 * 项目名称: yw-jpj-scenic <br/>
 * 创建日期:  2017/8/31 10:51  <br/>
 * 作者:    wdz
 */
public class StoreAuthVo extends StoreAuthority
{
    /**
     * 浙江卓锐科技股份有限公司 版权所有 © Copyright 2016<br/>
     * 说明: <br/>
     * 项目名称: drore-tenant-manage <br/>
     * 创建日期: 2016年9月27日 下午1:30:07 <br/>
     * 作者: wdz
     */
    private static final long serialVersionUID = 524028812401017834L;

    public StoreAuthVo()
    {
    }

    public StoreAuthVo(StoreAuthority orig)
    {
        try
        {
            BeanUtils.copyProperties(this, orig);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private List<StoreAuthVo> children;

    public List<StoreAuthVo> getChildren()
    {
        return children;
    }

    public void setChildren(List<StoreAuthVo> children)
    {
        this.children = children;
    }

}