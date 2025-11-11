package me.hearta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hearta.dto.Response;
import me.hearta.dto.vo.CompanyProfileVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class CompanyProfileControllerTest {

    @Autowired
    private CompanyProfileController companyProfileController;


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
    void testUpdateNickname(){
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(17L,null,
                        List.of(new SimpleGrantedAuthority("COMPANY")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Response<Void> response = companyProfileController.updateNickname("旅行者");
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("=== 响应JSON ===");
            System.out.println(mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(response));
        } catch (Exception e) {
            System.out.println("转换JSON失败: " + e.getMessage());
        }


        assertNotNull(response);
//        assertNotNull(response.getData());
    }
}
