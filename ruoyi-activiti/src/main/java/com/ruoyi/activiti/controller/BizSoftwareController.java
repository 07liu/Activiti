package com.ruoyi.activiti.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ruoyi.activiti.domain.BizSoftwareVo;
import com.ruoyi.activiti.service.IProcessService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.activiti.domain.BizSoftware;
import com.ruoyi.activiti.service.IBizSoftwareService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 软件/网络申请Controller
 *
 * @author lzx@4229303
 * @date 2021-03-31
 */
@Controller
@RequestMapping("/software")
public class BizSoftwareController extends BaseController {
    private String prefix = "software" ;

    @Autowired
    private IBizSoftwareService bizSoftwareService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IProcessService processService;


    //    @RequiresPermissions("activiti:software:view")
    @GetMapping()
    public String software(ModelMap mmap) {
        mmap.put("currentUser" , ShiroUtils.getSysUser());
        return prefix + "/software" ;
    }

    /**
     * 查询软件/网络申请列表
     */
//    @RequiresPermissions("activiti:software:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizSoftwareVo bizSoftware) {
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
    public AjaxResult export(BizSoftwareVo bizSoftware) {
        bizSoftware.setProcessType("softwareNetApply");
        List<BizSoftwareVo> list = bizSoftwareService.selectBizSoftwareList(bizSoftware);
        ExcelUtil<BizSoftwareVo> util = new ExcelUtil<BizSoftwareVo>(BizSoftwareVo.class);
        return util.exportExcel(list, "software");
    }

    /**
     * 新增软件/网络申请
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add" ;
    }

    /**
     * 新增保存软件/网络申请
     */
//    @RequiresPermissions("activiti:software:add")
    @Log(title = "软件/网络申请" , businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizSoftwareVo bizSoftware) {
        Long userId = ShiroUtils.getUserId();
        if (SysUser.isAdmin(userId)) {
            return error("提交申请失败：不允许管理员提交申请！");
        }
        bizSoftware.setProcessType("software");
        return toAjax(bizSoftwareService.insertBizSoftware(bizSoftware));
    }

    /**
     * 修改软件/网络申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        BizSoftware bizSoftware = bizSoftwareService.selectBizSoftwareById(id);
        mmap.put("bizSoftware" , bizSoftware);
        return prefix + "/edit" ;
    }

    /**
     * 修改保存软件/网络申请
     */
//    @RequiresPermissions("activiti:software:edit")
    @Log(title = "软件/网络申请" , businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizSoftwareVo bizSoftware) {
        bizSoftware.setUpdateBy(ShiroUtils.getLoginName());
        bizSoftware.setUpdateTime(new Date());
        return toAjax(bizSoftwareService.updateBizSoftware(bizSoftware));
    }

    /**
     * 删除软件/网络申请
     */
//    @RequiresPermissions("activiti:software:remove")
    @Log(title = "软件/网络申请" , businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizSoftwareService.deleteBizSoftwareByIds(ids));
    }


    /**
     * 提交申请
     */
    @Log(title = "请假业务" , businessType = BusinessType.UPDATE)
    @PostMapping("/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        BizSoftwareVo bizSoftware = bizSoftwareService.selectBizSoftwareById(id);
        String applyUserId = ShiroUtils.getLoginName();
        bizSoftwareService.submitApply(bizSoftware, applyUserId, "software" , new HashMap<>());
        return success();
    }


    @GetMapping("/softwareTodo")
    public String todoView() {
        return prefix + "/softwareTodo" ;
    }

    /**
     * 我的待办列表
     *
     * @return
     */
    @PostMapping("/taskList")
    @ResponseBody
    public TableDataInfo taskList(BizSoftwareVo bizSoftware) {
        bizSoftware.setProcessType("software");
        List<BizSoftwareVo> list = bizSoftwareService.findTodoTasks(bizSoftware, ShiroUtils.getLoginName());
        return getDataTable(list);
    }

    /**
     * 加载审批弹窗
     *
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap) {

        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        BizSoftwareVo bizSoftware = bizSoftwareService.selectBizSoftwareById(new Long(processInstance.getBusinessKey()));
        mmap.put("bizSoftware" , bizSoftware);
        mmap.put("taskId" , taskId);
        String verifyName = task.getTaskDefinitionKey().substring(0, 1).toUpperCase() + task.getTaskDefinitionKey().substring(1);
        return prefix + "/task" + verifyName;
    }

    @GetMapping("/showFormDialog/{instanceId}")
    public String showFormDialog(@PathVariable("instanceId") String instanceId, ModelMap mmap) {
        String businessKey = processService.findBusinessKeyByInstanceId(instanceId);
        BizSoftwareVo bizSoftware = bizSoftwareService.selectBizSoftwareById(new Long(businessKey));
        mmap.put("bizSoftware" , bizSoftware);
        return prefix + "/view" ;
    }

    /**
     * 完成任务
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}" , method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @RequestParam(value = "saveEntity" , required = false) String saveEntity,
                               @ModelAttribute("preloadSoft") BizSoftwareVo bizSoftware, HttpServletRequest request) {
        boolean saveEntityBoolean = BooleanUtils.toBoolean(saveEntity);
        processService.complete(taskId, bizSoftware.getInstanceId(), bizSoftware.getProcessType(), bizSoftware.getReason(), "software" , new HashMap<String, Object>(), request);
        if (saveEntityBoolean) {
            bizSoftwareService.updateBizSoftware(bizSoftware);
        }
        return success("任务已完成");
    }
}
