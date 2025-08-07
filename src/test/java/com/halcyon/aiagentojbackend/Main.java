package com.halcyon.aiagentojbackend;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

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
//        MyService target = new MyServiceImpl();
//        MyService proxy = (MyService) Proxy.newProxyInstance(
//                target.getClass().getClassLoader(),
//                target.getClass().getInterfaces(),
//                new MyInvocationHandler(target)
//        );
//
//        proxy.doSomething();
        String s = "【【【【【\n代码分析：\n1. 题目要求读取两个整数 A 和 B，并计算它们的和。\n2. 输入格式为一行，包含两个整数，中间用空格分隔。因此，我们可以使用 Java 的 Scanner 类来读取输入。\n3. 输出格式为一个整数，即两个输入整数的和。\n\n实现步骤：\n- 导入必要的包 java.util.Scanner 用于读取输入。\n- 创建主类 Main。\n- 在主方法中，创建 Scanner 对象来读取标准输入。\n- 使用 nextInt() 方法分别读取两个整数。\n- 计算它们的和并输出结果。\n\n注意事项：\n- 必须确保输入是两个整数，中间用空格分隔。Scanner 的 nextInt() 方法会自动处理这一情况。\n- 不需要处理异常输入，因为题目保证输入格式正确。\n\n【【【【【\n```java\nimport java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        int a = scanner.nextInt();\n        int b = scanner.nextInt();\n        System.out.println(a + b);\n    }\n}\n```";
        String[] split = s.split("【【【【【");
        System.out.println(Arrays.toString(split));
    }
}
