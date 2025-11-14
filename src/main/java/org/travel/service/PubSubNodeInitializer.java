package org.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.travel.entity.Location;
import org.travel.entity.PubSubNode;
import org.travel.mapper.LocationMapper;
import org.travel.mapper.PubSubNodeMapper;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class PubSubNodeInitializer {
    @Autowired
    private PubSubNodeMapper pubSubNodeMapper;

    @Autowired
    private LocationMapper locationMapper;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional(rollbackFor = Exception.class)
    public void  initializePubSubNodes() {
        Long nodeCount = pubSubNodeMapper.selectCount(null);
        if(nodeCount > 0) {
            log.info("发布订阅节点树已初始化过");
            return;
        }

        PubSubNode rootNode = PubSubNode.builder()
                .nodeName("全国")
                .nodeType(PubSubNode.NodeType.PROVINCE)
                .parentNodeId(null)
                .active(true)
                .displayOrder(0)
                .build();
        pubSubNodeMapper.insert(rootNode);

        Long rootNodeId = rootNode.getNodeId();

        List<Location> provinces = locationMapper.selectByMap(
                Map.of("level",1,"is_active",true)
        );

        for(Location province : provinces){
            PubSubNode provinceNode = PubSubNode.builder()
                    .nodeName(province.getLocationName())
                    .nodeType(PubSubNode.NodeType.PROVINCE)
                    .locationId(province.getLocationId())
                    .parentNodeId(rootNodeId)
                    .active(true)
                    .displayOrder(province.getDisplayOrder())
                    .build();
            pubSubNodeMapper.insert(provinceNode);

            List<Location> cities = locationMapper.selectByMap(
                    Map.of("parent_id",province.getLocationId(),
                            "level",2,"is_active",true)
            );

            for(Location city : cities){
                PubSubNode cityNode = PubSubNode.builder()
                        .nodeName(city.getLocationName())
                        .nodeType(PubSubNode.NodeType.CITY)
                        .locationId(city.getLocationId())
                        .parentNodeId(provinceNode.getNodeId())
                        .active(true)
                        .displayOrder(city.getDisplayOrder())
                        .build();
                pubSubNodeMapper.insert(cityNode);
            }
        }
        log.info("发布订阅节点树初始化完成，共创建{}个节点",pubSubNodeMapper.selectCount(null));
    }
}
