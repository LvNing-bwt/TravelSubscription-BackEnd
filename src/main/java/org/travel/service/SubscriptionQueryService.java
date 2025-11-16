package org.travel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.travel.dto.CityNodeData;
import org.travel.dto.ProvinceNodeData;
import org.travel.entity.CompanyAccount;
import org.travel.entity.PubSubNode;
import org.travel.entity.Subscription;
import org.travel.entity.SubscriptionTier;
import org.travel.exception.SubscriptionException;
import org.travel.mapper.PubSubNodeMapper;
import org.travel.mapper.SubscriptionTierMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionQueryService {

    @Autowired
    CompanyAccountService companyAccountService;

    @Autowired
    SubscriptionTierMapper subscriptionTierMapper;

    @Autowired
    PubSubNodeService pubSubNodeService;

   public List<ProvinceNodeData> getProvinceTree(Long uid){
        // 获取公司id
       CompanyAccount account = companyAccountService.getById(uid);
        // 获取公司订阅权限
       SubscriptionTier subscriptionTier = subscriptionTierMapper.selectById(account.getCompanyId());

       // 获取公司所在省节点
       PubSubNode companyNode = pubSubNodeService.findByCompanyId(account.getCompanyId());
       PubSubNode cityNode = pubSubNodeService.getById(companyNode.getParentNodeId());
       PubSubNode provinceNode = pubSubNodeService.getById(cityNode.getParentNodeId());

       // 获取所有活跃的省份节点
       List<PubSubNode> allProvinces = pubSubNodeService.findActiveProvinces();

       return processProvinceByTierLevel(allProvinces,subscriptionTier.getTierLevel(),provinceNode.getNodeId());
   }

   private List<ProvinceNodeData> processProvinceByTierLevel(List<PubSubNode> allProvinces,
                                                             SubscriptionTier.TierLevel tierLevel,
                                                             Long curProvinceId){

       // 数据库的数据本身就是排好序的
//       List<PubSubNode> sortedProvinces = allProvinces.stream()
//               .sorted((a,b) -> {
//                   if(a.getNodeId().equals(curProvinceId)) return -1;
//                   if(b.getNodeId().equals(curProvinceId)) return 1;
//                   return a.getDisplayOrder().compareTo(b.getDisplayOrder());
//               }).toList();
       List<ProvinceNodeData> result = new ArrayList<>();

       switch (tierLevel) {
           case PROFESSIONAL -> {
               for(PubSubNode province : allProvinces) {
                   ProvinceNodeData nodeData = convertToProvinceNodeData(province);
                   nodeData.setSubscribable(true);
                   result.add(nodeData);
               }
           }case STANDARD -> {
               for(PubSubNode province : allProvinces) {
                   ProvinceNodeData nodeData = convertToProvinceNodeData(province);
                   nodeData.setSubscribable(province.getNodeId().equals(curProvinceId));
                   result.add(nodeData);
               }
           }case BASIC -> {
               for(PubSubNode province : allProvinces) {
                   ProvinceNodeData nodeData = convertToProvinceNodeData(province);
                   nodeData.setSubscribable(false);
                   result.add(nodeData);
               }
           }
       }
       return result;
   }

   private ProvinceNodeData convertToProvinceNodeData(PubSubNode province) {
       return ProvinceNodeData.builder()
               .id(province.getNodeId())
               .locationId(province.getLocationId())
               .name(province.getNodeName())
               .build();
   }

   public List<CityNodeData> getCitiesByProvince(Long uid,Long provinceId){
       // 获取公司id
       CompanyAccount account = companyAccountService.getById(uid);
       // 获取公司订阅权限
       SubscriptionTier subscriptionTier = subscriptionTierMapper.selectById(account.getCompanyId());

       // 获取公司所在市节点
       PubSubNode companyNode = pubSubNodeService.findByCompanyId(account.getCompanyId());
       PubSubNode cityNode = pubSubNodeService.getById(companyNode.getParentNodeId());
       PubSubNode provinceNode = pubSubNodeService.getById(cityNode.getParentNodeId());
       if(!provinceNode.getNodeId().equals(provinceId))
           throw SubscriptionException.noSubscriptionPermission(provinceNode.getNodeName());

       // 获取所有活跃的省份节点
       List<PubSubNode> allCities = pubSubNodeService.findCitiesByProvinceId(provinceId);
       return processCityByTierLevel(allCities,subscriptionTier.getTierLevel(),cityNode.getNodeId());
   }

   private List<CityNodeData> processCityByTierLevel(List<PubSubNode> allCities,
                                                              SubscriptionTier.TierLevel tierLevel,
                                                              Long curCityId){
       List<CityNodeData> result = new ArrayList<>();
       switch (tierLevel) {
           case PROFESSIONAL, STANDARD -> {
               for (PubSubNode city : allCities) {
                   CityNodeData cityNodeData = CityNodeData.builder()
                           .id(city.getNodeId())
                           .locationId(city.getLocationId())
                           .name(city.getNodeName())
                           .subscribable(true)
                           .build();
                   result.add(cityNodeData);
               }
           }
           case BASIC -> {
               for (PubSubNode city : allCities) {
                   CityNodeData cityNodeData = CityNodeData.builder()
                           .id(city.getNodeId())
                           .locationId(city.getLocationId())
                           .name(city.getNodeName())
                           .subscribable(city.getNodeId().equals(curCityId))
                           .build();
                   result.add(cityNodeData);
               }
           }
       }
       return result;
   }
}
