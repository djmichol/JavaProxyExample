package pl.michol;

import pl.michol.annotations.Autowired;
import pl.michol.annotations.Bean;
import pl.michol.annotations.LogExecutionTime;
import pl.michol.annotations.Runner;
import pl.michol.proxies.LogExecutionTimeProxy;
import pl.michol.utils.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BeanProcessor {

    private static Map<String, Object> beans = new HashMap<>();
    private static Map<String, Object> proxMap = new HashMap<>();

    public void runApplication() {
        try {
            createBeansMap();
            generateProxies();
            scanForAutowiredMethods();
            scanForRunnerAndRun();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void scanForRunnerAndRun() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            if (entry.getValue().getClass().isAnnotationPresent(Runner.class)) {
                Method runner = entry.getValue().getClass().getDeclaredMethod("run");
                runner.invoke(entry.getValue());
            }
        }
    }

    private void scanForAutowiredMethods() throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            for (Method method : entry.getValue().getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Autowired.class)) {
                    Parameter[] para = method.getParameters();
                    String name = para[0].getType().getName();
                    method.invoke(entry.getValue(), proxMap.get(name + "Impl"));
                }
            }
        }
    }

    private void generateProxies() {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            for (Class interfaceClass : entry.getValue().getClass().getInterfaces()) {
                if (interfaceClass.isAnnotationPresent(LogExecutionTime.class)) {
                    proxMap.put(entry.getKey(), Proxy.newProxyInstance(entry.getValue().getClass().getClassLoader(),
                            entry.getValue().getClass().getInterfaces(),
                            new LogExecutionTimeProxy(entry.getValue())));
                }
            }
        }
    }

    private void createBeansMap() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class[] classes = ReflectionUtils.getClasses("pl.michol");
        for (Class classToCheck : classes) {
            if (classToCheck.isAnnotationPresent(Bean.class)) {
                beans.put(classToCheck.getName(), classToCheck.getConstructor().newInstance());
            }
        }
    }

}
