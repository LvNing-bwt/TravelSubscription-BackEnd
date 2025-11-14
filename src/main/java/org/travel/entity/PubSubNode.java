package org.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@TableName("pubsub_nodes")
@Builder
public class PubSubNode {
    @TableId(type = IdType.AUTO)
    private Long nodeId;
    @TableField("node_name")
    private String nodeName;
    @TableField("node_type")
    private NodeType nodeType;
    @TableField("location_id")
    private String locationId; // 如果是省市有
    @TableField("parent_node_id")
    private Long parentNodeId; //这个是树形结构的id
    @TableField("company_id")
    private Long companyId;
    @TableField("is_active")
    private Boolean active;
    @TableField("display_order")
    private Integer displayOrder; //同级节点使用顺序


    public enum NodeType{
        PROVINCE,
        CITY,
        COMPANY
    }
}
