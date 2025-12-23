package ru.bsuedu.cad.lab.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CSVParserTimingAspect {

    // Pointcut — перехватываем метод parse у CSVParser
    @Around("execution(* ru.bsuedu.cad.lab.parser.CSVParser.parse(..))")
    public Object measureParsingTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        // выполняем оригинальный метод
        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        System.out.println(
                "[AOP] CSV parsing took " + (end - start) + " ms"
        );

        return result;
    }
}
