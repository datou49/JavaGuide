package com.javaguide.core.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;

/**
 * JVM - Java 内存区域（JavaGuide 必看）
 * <p>运行时数据区：程序计数器、虚拟机栈(VM Stack)、本地方法栈、堆(Heap)、方法区(Method Area / Metaspace)。
 * 其中 Heap 和 Method Area 是线程共享，其余线程私有。</p>
 */
public class MemoryAreaDemo {

    public static void main(String[] args) {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = mem.getHeapMemoryUsage();
        MemoryUsage nonHeap = mem.getNonHeapMemoryUsage();

        System.out.println("Heap(堆,线程共享)   init=" + heap.getInit() / 1048576 + "MB, used=" + heap.getUsed() / 1048576
                + "MB, max=" + heap.getMax() / 1048576 + "MB");
        System.out.println("NonHeap(方法区/Metaspace,线程共享)  used=" + nonHeap.getUsed() / 1048576 + "MB");

        ThreadMXBean tm = ManagementFactory.getThreadMXBean();
        System.out.println("当前线程数 = " + tm.getThreadCount()
                + "（Java 线程与操作系统线程 1:1，栈是线程私有的）");
    }
}
