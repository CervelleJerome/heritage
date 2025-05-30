package fr.limoges.valadon.testai.heritage.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Configuration class to make the request object available in Thymeleaf templates.
 * In Thymeleaf 3.1+, the request, session, servletContext, and response objects
 * are no longer available by default in template expressions.
 */
@Configuration
@ControllerAdvice
public class ThymeleafConfig {

    /**
     * Add the HttpServletRequest to the model of all controllers
     * This makes it available in Thymeleaf templates as ${request}
     */
    @ModelAttribute("request")
    public HttpServletRequest addRequestToModel(HttpServletRequest request) {
        return request;
    }
}
