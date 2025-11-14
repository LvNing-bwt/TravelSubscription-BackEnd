package org.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("subscription_relations")
public class Subscription {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("subscriber_company_id")
    private Long subscriberCompanyId;

    @TableField("target_node_id")
    private Long targetNodeId;

    @TableField("subscription_time")
    private LocalDateTime subscriptionTime;

    @TableField("backend_status")
    private SubscriptionStatus backendStatus = SubscriptionStatus.ACTIVE;

    // 状态枚举
    public enum SubscriptionStatus {
        ACTIVE, CANCELLED
    }
}
