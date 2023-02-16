package com.ssafy.Homezakaya.interceptor;

import com.ssafy.Homezakaya.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    // header 정보
    private static final String HEADER_AUTH = "accessToken";
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")){
            return true;
        }

        final String token = request.getHeader(HEADER_AUTH);
        System.out.println("token = " + token);

        // 유효성 검사
        if (token != null) {
            jwtUtil.checkAndGetClaims(token);
            return true;
        }else{
            throw new Exception("유효하지 않은 접근이다.");
        }
    }
}

