package org.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.travel.dto.CityNodeData;
import org.travel.dto.ProvinceNodeData;
import org.travel.dto.Response;
import org.travel.service.SubscriptionQueryService;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    SubscriptionQueryService subscriptionQueryService;

    private Long getCurrentUid() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/provinces")
    public Response<List<ProvinceNodeData>> getProvinceTree() {
        Long uid = getCurrentUid();
        List<ProvinceNodeData> result = subscriptionQueryService.getProvinceTree(uid);
        return Response.success(result);
    }

    @GetMapping("/{provinceId}/cities")
    public Response<List<CityNodeData>> getCitiesByProvinceId(@PathVariable Long provinceId) {
        Long uid = getCurrentUid();
        List<CityNodeData> result = subscriptionQueryService.getCitiesByProvince(uid,provinceId);
        return Response.success(result);
    }
}
