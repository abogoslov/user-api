package ru.bogoslov.userapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import ru.bogoslov.userapi.model.RequestContext;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class RequestConfig {

    @Bean
    @RequestScope
    public RequestContext requestContext(HttpServletRequest request) {
        return new RequestContext(request.getHeader("x-api-role"));
    }
}
