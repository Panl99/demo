package com.lp.demo.common.service;

import com.google.common.util.concurrent.AtomicDouble;
import com.lp.demo.common.dto.ThreadMetricsAux;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;

/**
 * @author lp
 * @date 2021/8/27 10:13
 * link: https://www.jianshu.com/p/a0abe36654d7?utm_campaign=hugo&utm_medium=reader_share&utm_content=note&utm_source=weixin-timeline
 **/
@Slf4j
@Service
public class ThreadMetricService {
    @Autowired
    private MeterRegistry meterRegistry;

    private final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

    private final HashMap<Long, ThreadMetricsAux> map = new HashMap<>();

    private final HashMap<Meter.Id, AtomicDouble> dynamicGauges = new HashMap<>();

    /**
     * one minutes
     */
    @Scheduled(cron = "0 * * * * ?")
    public void schedule() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ThreadMetricService.schedule<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        final long[] allThreadIds = threadBean.getAllThreadIds();
        for (long threadId : allThreadIds) {
            final ThreadInfo threadInfo = threadBean.getThreadInfo(threadId);
            if (threadInfo == null) {
                continue;
            }
            final long threadNanoTime = getThreadCPUTime(threadId);
            if (threadNanoTime == 0) {
                // 如果threadNanoTime为0，则识别为异常数据，不处理，并清理历史数据
                map.remove(threadId);
            }
            final long nanoTime = System.nanoTime();
            ThreadMetricsAux oldMetrics = map.get(threadId);
            // 判断是否有历史的metrics信息
            if (oldMetrics != null) {
                // 如果有，则计算CPU信息并上报
                double percent = (double) (threadNanoTime - oldMetrics.getUsedNanoTime()) / (double) (nanoTime - oldMetrics.getLastNanoTime());
                handleDynamicGauge("jvm.threads.cpu", "threadName", threadInfo.getThreadName(), percent);
            }
            map.put(threadId, new ThreadMetricsAux(threadNanoTime, nanoTime));
        }
    }

    // meter Gauge相关代码
    private void handleDynamicGauge(String meterName, String labelKey, String labelValue, double snapshot) {
        Meter.Id id = new Meter.Id(meterName, Tags.of(labelKey, labelValue), null, null, Meter.Type.GAUGE);

        dynamicGauges.compute(id, (key, current) -> {
            if (current == null) {
                AtomicDouble initialValue = new AtomicDouble(snapshot);
                meterRegistry.gauge(key.getName(), key.getTags(), initialValue);
                return initialValue;
            } else {
                current.set(snapshot);
                return current;
            }
        });
    }

    long getThreadCPUTime(long threadId) {
        long time = threadBean.getThreadCpuTime(threadId);
        /* thread of the specified ID is not alive or does not exist */
        return time == -1 ? 0 : time;
    }

    // 效果 curl localhost:8090/actuator/prometheus|grep cpu
/*    jvm_threads_cpu{threadName="RMI Scheduler(0)",} 0.0
    jvm_threads_cpu{threadName="http-nio-20001-exec-10",} 0.0
    jvm_threads_cpu{threadName="Signal Dispatcher",} 0.0
    jvm_threads_cpu{threadName="Common-Cleaner",} 3.1664628758074733E-7
    jvm_threads_cpu{threadName="http-nio-20001-Poller",} 7.772143763853949E-5
    jvm_threads_cpu{threadName="http-nio-20001-Acceptor",} 8.586978352515361E-5
    jvm_threads_cpu{threadName="DestroyJavaVM",} 0.0
    jvm_threads_cpu{threadName="Monitor Ctrl-Break",} 0.0
    jvm_threads_cpu{threadName="AsyncHttpClient-timer-8-1",} 2.524386571545477E-4
    jvm_threads_cpu{threadName="Attach Listener",} 0.0
    jvm_threads_cpu{threadName="scheduling-1",} 1.2269694160981585E-4
    jvm_threads_cpu{threadName="container-0",} 1.999795692406262E-6
    jvm_threads_cpu{threadName="http-nio-20001-exec-9",} 0.0
    jvm_threads_cpu{threadName="http-nio-20001-exec-7",} 0.0
    jvm_threads_cpu{threadName="http-nio-20001-exec-8",} 0.0
    jvm_threads_cpu{threadName="http-nio-20001-exec-5",} 0.0
    jvm_threads_cpu{threadName="Notification Thread",} 0.0
    jvm_threads_cpu{threadName="http-nio-20001-exec-6",} 0.0
    jvm_threads_cpu{threadName="http-nio-20001-exec-3",} 0.0
    jvm_threads_cpu{threadName="http-nio-20001-exec-4",} 0.0
    jvm_threads_cpu{threadName="Reference Handler",} 0.0
    jvm_threads_cpu{threadName="http-nio-20001-exec-1",} 0.0012674719289349648
    jvm_threads_cpu{threadName="http-nio-20001-exec-2",} 6.542541277148053E-5
    jvm_threads_cpu{threadName="RMI TCP Connection(idle)",} 1.3998786340454562E-6
    jvm_threads_cpu{threadName="Finalizer",} 0.0
    jvm_threads_cpu{threadName="Catalina-utility-2",} 7.920883054498174E-5
    jvm_threads_cpu{threadName="RMI TCP Accept-0",} 0.0
    jvm_threads_cpu{threadName="Catalina-utility-1",} 6.80101662787773E-5*/

}
