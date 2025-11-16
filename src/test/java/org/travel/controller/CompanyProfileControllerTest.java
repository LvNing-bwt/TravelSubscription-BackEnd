package org.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.travel.dto.CityNodeData;
import org.travel.dto.ProvinceNodeData;
import org.travel.dto.Response;
import org.travel.dto.vo.CompanyProfileVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.travel.entity.CompanyProfile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class CompanyProfileControllerTest {

    @Autowired
    private CompanyProfileController companyProfileController;

    @Autowired
    private SubscriptionController subscriptionController;


    @Test
    void testFullProfile(){
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(17L,null,
                        List.of(new SimpleGrantedAuthority("COMPANY")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Response<CompanyProfileVO> response = companyProfileController.getProfile();
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("=== 响应JSON ===");
            System.out.println(mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(response));
        } catch (Exception e) {
            System.out.println("转换JSON失败: " + e.getMessage());
        }


        assertNotNull(response);
        assertNotNull(response.getData());
    }

    @Test
    void testGetProvinces(){
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(5L,null,
                        List.of(new SimpleGrantedAuthority("COMPANY")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Response<List<ProvinceNodeData>> response = subscriptionController.getProvinceTree();

        assertNotNull(response);
        assertNotNull(response.getData());
        // 或者使用JSON格式美化输出
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            System.out.println("JSON Response:\n" + json);
        } catch (Exception e) {
            System.out.println("转换JSON失败: " + e.getMessage());
        }
    }

    @Test
    void testGetCities() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(5L,null,
                        List.of(new SimpleGrantedAuthority("COMPANY")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Response<List<CityNodeData>> response = subscriptionController.getCitiesByProvinceId(112L);
        assertNotNull(response);
        assertNotNull(response.getData());
        // 或者使用JSON格式美化输出
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            System.out.println("JSON Response:\n" + json);
        } catch (Exception e) {
            System.out.println("转换JSON失败: " + e.getMessage());
        }
    }
}
