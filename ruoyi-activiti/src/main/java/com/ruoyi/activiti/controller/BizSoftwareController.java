package com.ruoyi.activiti.controller;

import java.util.List;

import com.ruoyi.activiti.domain.BizSoftwareVo;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.activiti.domain.BizSoftware;
import com.ruoyi.activiti.service.IBizSoftwareService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 软件/网络申请Controller
 * 
 * @author lzx@4229303
 * @date 2021-03-31
 */
@Controller
@RequestMapping("/software")
public class BizSoftwareController extends BaseController
{
    private String prefix = "software";

    @Autowired
    private IBizSoftwareService bizSoftwareService;

//    @RequiresPermissions("activiti:software:view")
    @GetMapping()
    public String software(ModelMap mmap)
    {
        mmap.put("currentUser",ShiroUtils.getSysUser());
        return prefix + "/software";
    }

    /**
     * 查询软件/网络申请列表
     */
//    @RequiresPermissions("activiti:software:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizSoftwareVo bizSoftware)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            bizSoftware.setCreateBy(ShiroUtils.getLoginName());
        }
        bizSoftware.setProcessType("software");
        startPage();
        List<BizSoftwareVo> list = bizSoftwareService.selectBizSoftwareList(bizSoftware);
        return getDataTable(list);
    }

    /**
     * 导出软件/网络申请列表
     */
//    @RequiresPermissions("activiti:software:export")
//    @Log(title = "软件/网络申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizSoftwareVo bizSoftware)
    {
        bizSoftware.setProcessType("softwareNetApply");
        List<BizSoftwareVo> list = bizSoftwareService.selectBizSoftwareList(bizSoftware);
        ExcelUtil<BizSoftwareVo> util = new ExcelUtil<BizSoftwareVo>(BizSoftwareVo.class);
        return util.exportExcel(list, "software");
    }

    /**
     * 新增软件/网络申请
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存软件/网络申请
     */
//    @RequiresPermissions("activiti:software:add")
    @Log(title = "软件/网络申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizSoftwareVo bizSoftware)
    {
        return toAjax(bizSoftwareService.insertBizSoftware(bizSoftware));
    }

    /**
     * 修改软件/网络申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        BizSoftware bizSoftware = bizSoftwareService.selectBizSoftwareById(id);
        mmap.put("bizSoftware", bizSoftware);
        return prefix + "/edit";
    }

    /**
     * 修改保存软件/网络申请
     */
//    @RequiresPermissions("activiti:software:edit")
    @Log(title = "软件/网络申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizSoftwareVo bizSoftware)
    {
        return toAjax(bizSoftwareService.updateBizSoftware(bizSoftware));
    }

    /**
     * 删除软件/网络申请
     */
//    @RequiresPermissions("activiti:software:remove")
    @Log(title = "软件/网络申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bizSoftwareService.deleteBizSoftwareByIds(ids));
    }
}
