package org.travel.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("locations")
public class Location {
    @TableId
    @TableField("location_id")
    private String locationId;
    @TableField("location_name")
    private String locationName;

    @TableField("parent_id")
    private String parentId;

    private LocationLevel level;
    @TableField("display_order")
    private Integer displayOrder = 0;
    @TableField("is_active")
    private Boolean active = true;

    enum LocationLevel{
        PROVINCE(1),
        CITY(2);

        @EnumValue
        private final int code;
        LocationLevel(int code){
            this.code = code;
        }
    }
}
