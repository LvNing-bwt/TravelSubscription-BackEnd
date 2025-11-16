package org.travel.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("subscription_tiers")
public class SubscriptionTier {
    @TableId
    @TableField("company_id")
    private Long companyId;
    @TableField("tier_level")
    private TierLevel tierLevel = TierLevel.BASIC;
    @TableField("valid_until")
    private LocalDateTime validUntil =
            LocalDateTime.of(2099, 12, 31, 23, 59, 59);

    public enum TierLevel{
        BASIC,
        STANDARD,
        PROFESSIONAL
    }

    public SubscriptionTier(){

    }

    public SubscriptionTier(Long companyId){
        this.companyId = companyId;
    }
}
