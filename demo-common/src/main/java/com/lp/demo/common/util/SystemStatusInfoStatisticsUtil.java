package com.lp.demo.common.util;

import com.jerolba.jmnemohistosyne.HistogramEntry;
import com.jerolba.jmnemohistosyne.Histogramer;
import com.jerolba.jmnemohistosyne.MemoryHistogram;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author lp
 * @date 2021/8/27 9:40
 **/
public class SystemStatusInfoStatisticsUtil {

    public static void main(String[] args) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>printCpuUsage<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        printCpuUsage();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>printThreadDump<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        printThreadDump();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>printClassHistogram<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        printClassHistogram();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>printDeadLock<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        printDeadLock();
    }

    /**
     * 打印当前进程cpu的使用
     */
    private static void printCpuUsage() {
        final OperatingSystemMXBean platformMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuLoad = platformMXBean.getProcessCpuLoad();
        System.out.println(cpuLoad);
    }

    /**
     * 打印线程堆栈
     */
    private static void printThreadDump() {
        final StringBuilder dump = new StringBuilder();
        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 100代表线程堆栈的层级
        final ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 100);
        for (ThreadInfo threadInfo : threadInfos) {
            dump.append('"');
            dump.append(threadInfo.getThreadName());
            dump.append("\" ");
            final Thread.State state = threadInfo.getThreadState();
            dump.append("\n   java.lang.Thread.State: ");
            dump.append(state);
            final StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
            for (final StackTraceElement stackTraceElement : stackTraceElements) {
                dump.append("\n        at ");
                dump.append(stackTraceElement);
            }
            dump.append("\n\n");
        }
        System.out.println(dump);
    }

    /**
     * 打印内存统计
     */
    private static void printClassHistogram() {
        Histogramer histogramer = new Histogramer();
        MemoryHistogram histogram = histogramer.createHistogram();

        HistogramEntry arrayList = histogram.get("java.util.ArrayList");
        System.out.println(arrayList.getInstances());
        System.out.println(arrayList.getSize());

        for (HistogramEntry entry : histogram) {
            System.out.println(entry);
        }
    }

    /**
     * 打印死锁
     * javadoc中指出，这是一个开销较大的操作
     */
    private static void printDeadLock() {
        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        final long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
        for (long deadlockedThread : deadlockedThreads) {
            final ThreadInfo threadInfo = threadMXBean.getThreadInfo(deadlockedThread);
            System.out.println(threadInfo + "deadLocked");
        }
    }

}
