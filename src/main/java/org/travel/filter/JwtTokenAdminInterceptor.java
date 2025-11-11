package org.travel.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.travel.dto.Response;
import org.travel.entity.CompanyAccount;
import org.travel.exception.LoginException;
import org.travel.service.CompanyAccountService;
import org.travel.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtTokenAdminInterceptor extends OncePerRequestFilter {

    @Autowired
    CompanyAccountService companyAccountService;


    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,
                                    @Nullable HttpServletResponse response,
                                    @Nullable FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request,response);  //为了放行公开接口
            return;
        }

        Long uid;
        try{
            Claims claims = JwtUtil.extractJwt(token);
            uid = Long.valueOf(claims.getSubject());
            log.info("当前用户id:{}",uid);

        } catch (ExpiredJwtException e){
            sendErrorResponse(response, Response.error(Response.Code.UNAUTHORIZED,"token已过期"));
            return;
        } catch (MalformedJwtException e){
            sendErrorResponse(response,Response.error(Response.Code.UNAUTHORIZED,"toke无效"));
            return;
        } catch (NumberFormatException e){
            sendErrorResponse(response,Response.error(Response.Code.UNAUTHORIZED,"tok格式错误"));
            return;
        } catch (Exception e) {
            sendErrorResponse(response,Response.error(Response.Code.SERVER_ERROR,"服务器繁忙"));
            return;
        }

        CompanyAccount user = companyAccountService.getById(uid);
        if(user == null){
            throw LoginException.userNotFound();
        }

        if(user.getStatus()==CompanyAccount.AccountStatus.DISABLED){
            throw LoginException.passwordError();
        }

        List<GrantedAuthority> authorities = getAuthorities(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        uid,
                        null,
                        authorities
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }

    private void sendErrorResponse(HttpServletResponse response, Response<?> errorResponse) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    private List<GrantedAuthority> getAuthorities(CompanyAccount user) {
        return switch (user.getRole()) {
            case SUPER_ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_COMPANY")
            );
            case ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_COMPANY")
            );
            case COMPANY -> List.of(
                    new SimpleGrantedAuthority("ROLE_COMPANY")
            );
        };
    }
}
