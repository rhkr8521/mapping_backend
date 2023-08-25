package com.mapping.mapping.user.filter;

import com.mapping.mapping.user.security.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Request가 들어왔을 때 Request Header의 Authorization 필드의 Bearer 토큰 값을 가져옴
    // 가져온 토큰 검증 및 검증 결과를 SecurityContext에 추가
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = parseBearerToken(request);
            //토큰값이 널이거나 문자가 null일경우
            if (token != null && !token.equalsIgnoreCase("null")) {
                //토큰 검증해서 payload의 userNickname가져옴
                String userNickname = tokenProvider.validate(token);

                //SecurityContext에 추가될 객체
                AbstractAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userNickname, null,
                        AuthorityUtils.NO_AUTHORITIES);
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

                //SecurityContext에 AbstractAuthenticationToken 객체를 추가해서 해당 Thread가 지속적으로 인증정보를 가질 수 있도록해줌
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }

        }catch (Exception exception){
            exception.printStackTrace();

        }
        filterChain.doFilter(request, response);
    }

    //Request Header에서 Authorization 필드의 Bearer Token을 가져오는 메서드
    private String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        //문자를 가지고 있는지 bearerToken이 맞는지 확인, 맞으면 7번째 문자부터 가져온다.
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }
}
