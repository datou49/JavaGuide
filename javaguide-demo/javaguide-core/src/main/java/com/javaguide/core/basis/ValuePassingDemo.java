package com.javaguide.core.basis;

/**
 * Java 基础 - 为什么 Java 中只有值传递？
 * <p>Java 中方法参数总是「按值传递」：基本类型传递副本，引用类型传递引用的副本（还是同一个对象）。
 * 因此方法内无法让外部变量指向新对象，但可以修改被引用对象的内容。</p>
 */
public class ValuePassingDemo {

    static void changeInt(int x) {
        x = 999;
    }

    static void changeRef(StringBuilder sb) {
        sb.append("-changed"); // 通过引用副本修改原对象 => 生效
        sb = new StringBuilder("new object"); // 让形参指向新对象 => 外部不受影响
    }

    public static void main(String[] args) {
        int a = 1;
        changeInt(a);
        System.out.println("基本类型 a = " + a + "（仍为 1，证明值传递）");

        StringBuilder sb = new StringBuilder("sb");
        changeRef(sb);
        System.out.println("引用类型 sb = " + sb + "（追加生效，但外部未指向新对象）");
    }
}
