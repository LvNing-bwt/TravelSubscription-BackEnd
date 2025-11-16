package org.travel.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.travel.entity.CompanyAccount;
import org.travel.entity.PubSubNode;
import org.travel.mapper.PubSubNodeMapper;

import java.util.List;

@Service
public class PubSubNodeService extends ServiceImpl<PubSubNodeMapper, PubSubNode> {
    public PubSubNode findByLocationId(String locationId){
        return this.lambdaQuery()  // 1. 创建Lambda查询构造器
                .eq(PubSubNode::getLocationId, locationId) // 2. 设置相等条件
                .one(); // 3. 执行查询并返回单个结果
    }
    public PubSubNode findByCompanyId(Long companyId){
        return this.lambdaQuery()
                .eq(PubSubNode::getCompanyId,companyId)
                .one();
    }
    public List<PubSubNode> findActiveProvinces() {
        return this.lambdaQuery()
                .eq(PubSubNode::getNodeType, PubSubNode.NodeType.PROVINCE)
                .eq(PubSubNode::getActive,true)
                .isNotNull(PubSubNode::getLocationId)
                .list();
    }

    public List<PubSubNode> findCitiesByProvinceId(Long provinceId){
        return this.lambdaQuery()
                .eq(PubSubNode::getNodeType, PubSubNode.NodeType.CITY)
                .eq(PubSubNode::getActive,true)
                .eq(PubSubNode::getParentNodeId,provinceId)
                .list();
    }
}
