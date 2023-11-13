package com.example.image.aspect;


import com.example.image.tool.RestClient;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class LoginAspect {
    @Before("execution(* com.example.image.controller.ImageController.addImage(..)) || " +
            "execution(* com.example.image.controller.ImageController.updateImage(..)) ||" +
            "execution(* com.example.image.controller.ImageController.deleteImage(..)) ")
    public void testToken() {
        RestClient<String, String> restClient = new RestClient<>("http://localhost:8081/api/auth/");
        HttpServletRequest servletRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String token = servletRequest.getHeader("Authorization");
        if(!restClient.testToken(token, String.class)) {
            throw new RuntimeException();
        }
    }
}
