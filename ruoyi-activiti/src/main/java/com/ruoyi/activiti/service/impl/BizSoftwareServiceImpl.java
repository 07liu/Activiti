package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.github.pagehelper.Page;
import com.ruoyi.activiti.domain.BizSoftwareVo;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
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
public class BizSoftwareServiceImpl implements IBizSoftwareService 
{
    @Autowired
    private BizSoftwareMapper bizSoftwareMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private TaskService taskService;


    /**
     * 查询软件/网络申请
     * 
     * @param id 软件/网络申请ID
     * @return 软件/网络申请
     */
    @Override
    public BizSoftwareVo selectBizSoftwareById(Long id)
    {

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
    public List<BizSoftwareVo> selectBizSoftwareList(BizSoftwareVo bizSoftware)
    {

        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        // PageHelper 仅对第一个 List 分页
        Page<BizSoftwareVo> list = (Page<BizSoftwareVo>) bizSoftwareMapper.selectBizSoftwareList(bizSoftware);
        Page<BizSoftwareVo> returnList = new Page<>();
        for (BizSoftwareVo software: list) {
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
    public int insertBizSoftware(BizSoftwareVo bizSoftware)
    {
        bizSoftware.setCreateBy(ShiroUtils.getLoginName());
        bizSoftware.setCreateTime(DateUtils.getNowDate());
        return bizSoftwareMapper.insertBizSoftware(bizSoftware);
    }

    /**
     * 修改软件/网络申请
     * 
     * @param bizSoftware 软件/网络申请
     * @return 结果
     */
    @Override
    public int updateBizSoftware(BizSoftwareVo bizSoftware)
    {
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
    public int deleteBizSoftwareByIds(String ids)
    {
        return bizSoftwareMapper.deleteBizSoftwareByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除软件/网络申请信息
     * 
     * @param id 软件/网络申请ID
     * @return 结果
     */
    @Override
    public int deleteBizSoftwareById(Long id)
    {
        return bizSoftwareMapper.deleteBizSoftwareById(id);
    }
}
