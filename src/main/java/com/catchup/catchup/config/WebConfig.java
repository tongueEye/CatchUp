package com.catchup.catchup.config;

import com.catchup.catchup.interceptor.LoginInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()) // LogInterceptor 등록
                .order(1)	// 적용할 필터 순서 설정
                .addPathPatterns("/**") //인터셉터 적용할 패턴
                .excludePathPatterns(  // 인터셉터에서 제외할 패턴
                        "/index", "/notice", "/qna", "/comboard", "/eduboard"
                        , "/login","/loginresult","/loginalert", "/join", "/joinresult", "/IdCheck"
                        , "/css/**", "/font/**", "/img/**", "/js/**");

    }
}
