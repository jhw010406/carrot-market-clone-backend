package com.jhw.carrot_market_clone_backend.filter;

import com.jhw.carrot_market_clone_backend.repository.UserCertificateRepository;
import com.jhw.carrot_market_clone_backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 서버에 들어온 request에 대하여 순차적으로 filter를 적용시키는 과정 중,
// Spring security filter를 적용시키는 단계에서 일부 함수인 JwtAuthenticationFilter를 수정
// JwtAuthenticationFilter는 토큰이 유효한지 검증하고, securityContext에 authentication 객체를 저장한다.
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    UserCertificateRepository userCertificateRepository;

    String[] excludeUrl = {"/login", "/register"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
        String              accessToken = request.getHeader("Authorization");
        String              refreshToken = request.getHeader("Refresh-Token");
        String              userId;


        for (String url : excludeUrl) {
            if (request.getRequestURI().contains(url)) {
                // 필터 체인 내 다음 필터가 존재한다면, 해당 필터를 수행한다.
                // doFilter를 통해 요청에 대한 다음 처리 작업을 수행하므로, doFilter를 수행하지 않을 시 요청이 종료됨.
                filterChain.doFilter(request, response);

                return;
            }
        }


        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);

            System.out.println("check refresh token");
            try {
                userId = JwtService.getUserIdInToken(refreshToken);
                accessToken = JwtService.generateAccessToken(userId);
                refreshToken = JwtService.generateRefreshToken(userId);

                response.setHeader("Authorization", "Bearer " + accessToken);
                response.setHeader("Refresh-Token", "Bearer " + refreshToken);

                System.out.println("provide new tokens succeed");
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Refresh token expired");

                return;
            }
        }
        else if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);

            System.out.println("check access token");
            try {
                userId = JwtService.getUserIdInToken(accessToken);
                // UsernamePasswordAuthenticationToken : principal = 아이디, credentials = 비밀번호, authorities = 권한
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);

                System.out.println("validate access token succeed");
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

                return;
            }
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}