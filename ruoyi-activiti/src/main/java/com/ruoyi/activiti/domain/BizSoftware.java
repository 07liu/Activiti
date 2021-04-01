package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 软件/网络申请对象 biz_software
 *
 * @author lzx@4229303
 * @date 2021-03-31
 */
public class BizSoftware extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 网络类型
     */
    @Excel(name = "网络类型")
    private String netType;

    /**
     * 软件类型
     */
    @Excel(name = "软件类型")
    private String softType;

    /**
     * 软件版本描述
     */
    @Excel(name = "软件版本描述")
    private String softVersion;

    /**
     * 业务类型
     */
    @Excel(name = "业务类型")
    private String processType;

    /**
     * 实例ID
     */
    @Excel(name = "实例ID")
    private String instanceId;

    /**
     * 申请原因
     */
    @Excel(name = "申请原因")
    private String reason;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @Excel(name = "更新时间")
    private Date updateTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getNetType() {
        return netType;
    }

    public void setSoftType(String softType) {
        this.softType = softType;
    }

    public String getSoftType() {
        return softType;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getProcessType() {
        return processType;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public String getCreateBy() {
        return createBy;
    }


    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }


    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BizSoftware{" +
                "id=" + id +
                ", netType='" + netType + '\'' +
                ", softType='" + softType + '\'' +
                ", softVersion='" + softVersion + '\'' +
                ", processType='" + processType + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", reason='" + reason + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
