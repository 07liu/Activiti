package com.ruoyi.activiti.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.activiti.domain.BizSoftware;
import com.ruoyi.activiti.domain.BizSoftwareVo;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * 软件/网络申请Service接口
 * 
 * @author lzx@4229303
 * @date 2021-03-31
 */
public interface IBizSoftwareService 
{
    /**
     * 查询软件/网络申请
     * 
     * @param id 软件/网络申请ID
     * @return 软件/网络申请
     */
    public BizSoftwareVo selectBizSoftwareById(Long id);

    /**
     * 查询软件/网络申请列表
     * 
     * @param bizSoftware 软件/网络申请
     * @return 软件/网络申请集合
     */
    public List<BizSoftwareVo> selectBizSoftwareList(BizSoftwareVo bizSoftware);

    /**
     * 新增软件/网络申请
     * 
     * @param bizSoftware 软件/网络申请
     * @return 结果
     */
    public int insertBizSoftware(BizSoftwareVo bizSoftware);

    /**
     * 修改软件/网络申请
     * 
     * @param bizSoftware 软件/网络申请
     * @return 结果
     */
    public int updateBizSoftware(BizSoftwareVo bizSoftware);

    /**
     * 批量删除软件/网络申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizSoftwareByIds(String ids);

    /**
     * 删除软件/网络申请信息
     * 
     * @param id 软件/网络申请ID
     * @return 结果
     */
    public int deleteBizSoftwareById(Long id);


    /**
     * 启动流程
     * @param entity
     * @param applyUserId
     * @return
     */
    ProcessInstance submitApply(BizSoftwareVo entity, String applyUserId, String key, Map<String, Object> variables);

    /**
     * 查询我的待办列表
     * @param userId
     * @return
     */
    List<BizSoftwareVo> findTodoTasks(BizSoftwareVo bizSoftware, String userId);

    List<BizSoftwareVo> findDoneTasks(BizSoftwareVo bizLeaveVo, String userId);
}
