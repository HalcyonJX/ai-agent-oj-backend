package com.halcyon.aiagentojbackend;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 目标接口
interface MyService {
    void doSomething();
}

// 目标对象的实现
class MyServiceImpl implements MyService {
    @Override
    public void doSomething() {
        System.out.println("Doing something...");
    }
}

// 动态代理处理器
class MyInvocationHandler implements InvocationHandler {
    private final Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before method call");
        Object result = method.invoke(target, args);
        System.out.println("After method call");
        return result;
    }
}

// 使用动态代理
public class Main {
    public static void main(String[] args) {
        MyService target = new MyServiceImpl();
        MyService proxy = (MyService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new MyInvocationHandler(target)
        );

        proxy.doSomething();
    }
}
