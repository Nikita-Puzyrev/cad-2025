package ru.bsuedu.cad.lab;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimingAspect {

    // Перехватываем метод readProducts в классе ResourceFileReader
    @Around("execution(* ru.bsuedu.cad.lab.ResourceFileReader.readProducts(..))")
    public Object measureParsingTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();

        Object result = joinPoint.proceed(); // Выполняем сам метод

        long end = System.nanoTime();
        long durationMs = (end - start) / 1_000_000;

        System.out.println("Парсинг CSV файла занял: " + durationMs + " мс");
        return result;
    }
}