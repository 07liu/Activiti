package com.ruoyi.activiti.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.ruoyi.activiti.domain.BizSoftwareVo;
import com.ruoyi.activiti.service.IProcessService;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.mapper.BizSoftwareMapper;
import com.ruoyi.activiti.domain.BizSoftware;
import com.ruoyi.activiti.service.IBizSoftwareService;
import com.ruoyi.common.core.text.Convert;

/**
 * 软件/网络申请Service业务层处理
 *
 * @author lzx@4229303
 * @date 2021-03-31
 */
@Service
public class BizSoftwareServiceImpl implements IBizSoftwareService {
    @Autowired
    private BizSoftwareMapper bizSoftwareMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IProcessService processService;

    @Autowired
    private RuntimeService runtimeService;


    /**
     * 查询软件/网络申请
     *
     * @param id 软件/网络申请ID
     * @return 软件/网络申请
     */
    @Override
    public BizSoftwareVo selectBizSoftwareById(Long id) {

        BizSoftwareVo software = bizSoftwareMapper.selectBizSoftwareById(id);
        SysUser sysUser = userMapper.selectUserByLoginName(software.getCreateBy());
        if (sysUser != null) {
            software.setApplyUserName(sysUser.getUserName());
        }
        return software;
//        return bizSoftwareMapper.selectBizSoftwareById(id);
    }

    /**
     * 查询软件/网络申请列表
     *
     * @param bizSoftware 软件/网络申请
     * @return 软件/网络申请
     */
    @Override
    public List<BizSoftwareVo> selectBizSoftwareList(BizSoftwareVo bizSoftware) {

        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        // PageHelper 仅对第一个 List 分页
        Page<BizSoftwareVo> list = (Page<BizSoftwareVo>) bizSoftwareMapper.selectBizSoftwareList(bizSoftware);
        Page<BizSoftwareVo> returnList = new Page<>();
        for (BizSoftwareVo software : list) {
            SysUser sysUser = userMapper.selectUserByLoginName(software.getCreateBy());
            if (sysUser != null) {
                software.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(software.getCreateBy());
            if (sysUser2 != null) {
                software.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(software.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(software.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    TaskEntityImpl task = (TaskEntityImpl) taskList.get(0);
                    software.setTaskId(task.getId());
                    if (task.getSuspensionState() == 2) {
                        software.setTaskName("已挂起");
                        software.setSuspendState("2");
                    } else {
                        software.setTaskName(task.getName());
                        software.setSuspendState("1");
                    }
                } else {
                    // 已办结或者已撤销
                    software.setTaskName("已结束");
                }
            } else {
                software.setTaskName("未启动");
            }
            returnList.add(software);
        }
        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
        returnList.setPageNum(pageNum);
        returnList.setPageSize(pageSize);
        return returnList;


//        return bizSoftwareMapper.selectBizSoftwareList(bizSoftware);
    }

    /**
     * 新增软件/网络申请
     *
     * @param bizSoftware 软件/网络申请
     * @return 结果
     */
    @Override
    public int insertBizSoftware(BizSoftwareVo bizSoftware) {

        return bizSoftwareMapper.insertBizSoftware(bizSoftware);
    }

    /**
     * 修改软件/网络申请
     *
     * @param bizSoftware 软件/网络申请
     * @return 结果
     */
    @Override
    public int updateBizSoftware(BizSoftwareVo bizSoftware) {
        bizSoftware.setUpdateTime(DateUtils.getNowDate());
        return bizSoftwareMapper.updateBizSoftware(bizSoftware);
    }

    /**
     * 删除软件/网络申请对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizSoftwareByIds(String ids) {
        return bizSoftwareMapper.deleteBizSoftwareByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除软件/网络申请信息
     *
     * @param id 软件/网络申请ID
     * @return 结果
     */
    @Override
    public int deleteBizSoftwareById(Long id) {
        return bizSoftwareMapper.deleteBizSoftwareById(id);
    }

    /**
     * 启动流程
     *
     * @param entity
     * @param applyUserId
     * @return
     */
    @Override
    public ProcessInstance submitApply(BizSoftwareVo entity, String applyUserId, String key, Map<String, Object> variables) {
        entity.setCreateBy(applyUserId);
        entity.setCreateTime(DateUtils.getNowDate());
        entity.setUpdateBy(applyUserId);
        bizSoftwareMapper.updateBizSoftware(entity);
        String businessKey = entity.getId().toString(); // 实体类 ID，作为流程的业务 key

        ProcessInstance processInstance = processService.submitApply(applyUserId, businessKey, entity.getProcessType(), entity.getReason(), key, variables);

        String processInstanceId = processInstance.getId();
        entity.setInstanceId(processInstanceId); // 建立双向关系
        bizSoftwareMapper.updateBizSoftware(entity);

        return processInstance;
    }

    @Override
    public List<BizSoftwareVo> findTodoTasks(BizSoftwareVo bizSoftware, String userId) {
        // 手动分页
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        Page<BizSoftwareVo> list = new Page<>();

        List<BizSoftwareVo> results = new ArrayList<>();
        List<Task> tasks = processService.findTodoTasks(userId, bizSoftware.getProcessType());
        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
            TaskEntityImpl taskImpl = (TaskEntityImpl) task;
            String processInstanceId = taskImpl.getProcessInstanceId();
            // 条件过滤 1
            if (StringUtils.isNotBlank(bizSoftware.getInstanceId()) && !bizSoftware.getInstanceId().equals(processInstanceId)) {
                continue;
            }
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String businessKey = processInstance.getBusinessKey();
            BizSoftwareVo leave2 = bizSoftwareMapper.selectBizSoftwareById(new Long(businessKey));
            // 条件过滤 2
            if (StringUtils.isNotBlank(bizSoftware.getProcessType()) && !bizSoftware.getProcessType().equals(leave2.getProcessType())) {
                continue;
            }
            leave2.setTaskId(taskImpl.getId());
            if (taskImpl.getSuspensionState() == 2) {
                leave2.setTaskName("已挂起");
            } else {
                leave2.setTaskName(taskImpl.getName());
            }
            SysUser sysUser = userMapper.selectUserByLoginName(leave2.getCreateBy());
            leave2.setApplyUserName(sysUser.getUserName());
            results.add(leave2);
        }

        List<BizSoftwareVo> tempList;
        if (pageNum != null && pageSize != null) {
            int maxRow = (pageNum - 1) * pageSize + pageSize > results.size() ? results.size() : (pageNum - 1) * pageSize + pageSize;
            tempList = results.subList((pageNum - 1) * pageSize, maxRow);
            list.setTotal(results.size());
            list.setPageNum(pageNum);
            list.setPageSize(pageSize);
        } else {
            tempList = results;
        }

        list.addAll(tempList);

        return list;
    }

    @Override
    public List<BizSoftwareVo> findDoneTasks(BizSoftwareVo bizLeaveVo, String userId) {
        return null;
    }

}
