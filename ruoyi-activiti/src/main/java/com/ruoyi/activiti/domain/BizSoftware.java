package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 软件/网络申请对象 biz_software
 * 
 * @author lzx@4229303
 * @date 2021-03-31
 */
public class BizSoftware extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 网络类型 */
    @Excel(name = "网络类型")
    private String netType;

    /** 软件类型 */
    @Excel(name = "软件类型")
    private String softType;

    /** 软件版本描述 */
    @Excel(name = "软件版本描述")
    private String softVersion;

    /** 业务类型 */
    @Excel(name = "业务类型")
    private String processType;

    /** 实例ID */
    @Excel(name = "实例ID")
    private String instanceId;

    /** 申请原因 */
    @Excel(name = "申请原因")
    private String reason;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setNetType(String netType) 
    {
        this.netType = netType;
    }

    public String getNetType() 
    {
        return netType;
    }
    public void setSoftType(String softType) 
    {
        this.softType = softType;
    }

    public String getSoftType() 
    {
        return softType;
    }
    public void setSoftVersion(String softVersion) 
    {
        this.softVersion = softVersion;
    }

    public String getSoftVersion() 
    {
        return softVersion;
    }
    public void setProcessType(String processType) 
    {
        this.processType = processType;
    }

    public String getProcessType() 
    {
        return processType;
    }
    public void setInstanceId(String instanceId) 
    {
        this.instanceId = instanceId;
    }

    public String getInstanceId() 
    {
        return instanceId;
    }
    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("netType", getNetType())
            .append("softType", getSoftType())
            .append("softVersion", getSoftVersion())
            .append("processType", getProcessType())
            .append("instanceId", getInstanceId())
            .append("reason", getReason())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
