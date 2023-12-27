package ru.study.t4_spring.annotation;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import ru.study.t4_spring.config.LoggerConfig;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
@AllArgsConstructor
public class LogTransformationBeanPostProcessor implements BeanPostProcessor {
    private LoggerConfig loggerConfig;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(LogTransformation.class)) {
            return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new InvocationHandler() {
                private final String fileName = clazz.getAnnotation(LogTransformation.class).filename();

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (Modifier.isAbstract(method.getModifiers())) {
                        Object objResult = method.invoke(bean, args);
                        String str = String.format("%s:\t %s::%s\t args: %s\t result: %s",
                                getCurrentDateToString(),
                                beanName,
                                method.getName(),
                                Arrays.toString(args),
                                objResult
                        );
                        saveToLog(str, fileName);
                        return objResult;
                    }
                    return method.invoke(bean, args);
                }
            });
        }
        return bean;
    }

    private void saveToLog(String data, String filename) {
        final String fullFileName = loggerConfig.getDir().concat("\\").concat(filename);
        try {
            if (!Files.exists(Path.of(loggerConfig.getDir())))
                Files.createDirectories(Path.of(loggerConfig.getDir()));
            Files.write(Path.of(fullFileName), data.concat("\r\n").getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Ошибка записи в лог: " + filename + " : " + e);
        }
    }

    private String getCurrentDateToString() {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(new Date());
    }
}
