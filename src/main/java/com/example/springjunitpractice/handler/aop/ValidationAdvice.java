package com.example.springjunitpractice.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.springjunitpractice.handler.exception.CustomValidationException;

@Component
@Aspect
public class ValidationAdvice {
    
    // @PostMapping 메서드에 공통 기능 적용
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {}

    // @PutMapping 메서드에 공통 기능 적용
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {}

    // 핵심 기능 전/후에 실행할 공통 기능
    @Around("postMapping() || putMapping()")
    public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("공통 기능 실행");
        Object[] args = proceedingJoinPoint.getArgs(); // 핵심 기능 메서드의 매개변수 가져오기

        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;
                
                if (bindingResult.hasErrors()) {
                    Map<String, String> errorMap = new HashMap<>();
        
                    for (FieldError error : bindingResult.getFieldErrors()) {
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    }
        
                    throw new CustomValidationException("유효성 검사에 실패하였습니다.", errorMap);
                }
            }
        }

        // 유효성 검사 성공 시 핵심 기능 실행
        return proceedingJoinPoint.proceed();
    }
}
