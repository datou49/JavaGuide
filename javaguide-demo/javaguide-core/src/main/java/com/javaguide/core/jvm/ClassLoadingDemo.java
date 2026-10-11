package com.javaguide.core.jvm;

/**
 * JVM - 类加载过程与类加载器
 * <p>类加载 5 步：加载(Loading)->验证(Verification)->准备(Preparation)->解析(Resolution)->初始化(Initialization)。
 * 双亲委派：AppClassLoader -> Ext/PlatformClassLoader -> BootstrapClassLoader，
 * 防止核心类被篡改、避免重复加载。</p>
 */
public class ClassLoadingDemo {

    static {
        System.out.println("[static块] 类初始化阶段执行（准备阶段赋默认值，初始化阶段赋静态值）");
    }

    private static final int CONST = 42;

    public static void main(String[] args) {
        System.out.println("常量访问不触发类初始化（CONST=" + CONST + "，上面 static 块若被触发会先打印）");

        System.out.println("当前类加载器: " + ClassLoadingDemo.class.getClassLoader());
        System.out.println("父加载器: " + ClassLoadingDemo.class.getClassLoader().getParent());
        System.out.println("Bootstrap 加载器(String): " + String.class.getClassLoader() + "（null 表示 Bootstrap）");
        System.out.println("双亲委派模型：Bootstrap -> Platform -> App");
    }
}
