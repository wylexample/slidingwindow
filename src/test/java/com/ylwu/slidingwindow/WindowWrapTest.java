package com.ylwu.slidingwindow;

import com.ylwu.slidingwindow.base.LeapArray;
import com.ylwu.slidingwindow.base.UnaryLeapArray;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Auther: Wuyulong
 * @Date: 2019/3/10 17:36
 * @Description:
 */
public class WindowWrapTest {

    private static LeapArray<LongAdder> data = new UnaryLeapArray(10, 10000);

    private static ScheduledExecutorService service = Executors.newScheduledThreadPool(6);
    public static void main(String[] args) {
        service.scheduleAtFixedRate(() -> {
            data.currentWindow().value().add((int)(Math.random()*10));
        }, 1, 1000, TimeUnit.MILLISECONDS);

//        service.scheduleAtFixedRate(() -> {
//            data.currentWindow().value().add((int)(Math.random()*20));
//        }, 1, 50, TimeUnit.MILLISECONDS);
//
//        service.scheduleAtFixedRate(() -> {
//            data.currentWindow().value().add((int)(Math.random()*120));
//        }, 1, 120, TimeUnit.MILLISECONDS);
//
//        service.scheduleAtFixedRate(() -> {
//            data.currentWindow().value().add((int)(Math.random()*30));
//        }, 1, 700, TimeUnit.MILLISECONDS);

        service.scheduleAtFixedRate(() -> {
            data.currentWindow().value().add((int)(Math.random()*60));
        }, 1, 700, TimeUnit.MILLISECONDS);


        service.scheduleAtFixedRate(() -> {
            System.out.println(data.values());
        }, 1, 1000, TimeUnit.MILLISECONDS);
    }
}
