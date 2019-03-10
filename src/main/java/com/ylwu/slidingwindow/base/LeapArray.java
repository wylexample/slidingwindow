package com.ylwu.slidingwindow.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: Wuyulong
 * @Date: 2019/3/10 17:04
 * @Description:
 */
public abstract class LeapArray<T> {

    protected int windowLengthInMs;

    protected int sampleCount;

    protected int intervalInMs;

    protected final AtomicReferenceArray<WindowWrap<T>> array;

    /**
     * 用于当前桶废弃时
     */
    private final ReentrantLock updateLock = new ReentrantLock();

    public LeapArray(int sampleCount, int intervalInMs) {
        this.windowLengthInMs = intervalInMs / sampleCount;
        this.sampleCount = sampleCount;
        this.intervalInMs = intervalInMs;
        this.array = new AtomicReferenceArray<WindowWrap<T>>(sampleCount);
    }

    public abstract T newEmptyBucket();

    protected abstract WindowWrap<T> resetWindowTo(WindowWrap<T> windowWrap, long startTime);

    public int calculateTimeIdx(long timeMillis){
        long timeId = timeMillis / windowLengthInMs;
        return (int) (timeId % array.length());
    }

    public long calculateWindowStart(long timeMillis){
        return timeMillis - timeMillis % windowLengthInMs;
    }

    public WindowWrap<T> currentWindow() {
        return currentWindow(System.currentTimeMillis());
    }

    public WindowWrap<T> currentWindow(long timeMillis){
        if (timeMillis < 0){
            return null;
        }

        int idx = calculateTimeIdx(timeMillis);

        System.out.println("idx="+idx);

        long windowStart = calculateWindowStart(timeMillis);

        while (true){
            WindowWrap<T> old = array.get(idx);

            if (old == null) {
                WindowWrap<T> window = new WindowWrap<>(windowLengthInMs, windowStart, newEmptyBucket());
                if (array.compareAndSet(idx, null, window)){
                    return window;
                } else {
                    //礼让。别的线程设置成功了
                    Thread.yield();
                }
            } else if (windowStart == old.windowStart()) {
                return old;
            } else if (windowStart > old.windowStart()) {
                if (updateLock.tryLock()){
                    try {
                        return resetWindowTo(old, windowStart);
                    } finally {
                        updateLock.unlock();
                    }
                } else {
                    Thread.yield();
                }
            } else if (windowStart < old.windowStart()){
                return new WindowWrap<>(windowLengthInMs, windowStart, newEmptyBucket());
            }
        }

    }


    protected boolean isWindowDeprecated(WindowWrap<T> windowWrap){
        return System.currentTimeMillis() - windowWrap.windowStart() > intervalInMs;
    }

    public List<T> values() {
        int size = array.length();
        List<T> result = new ArrayList<T>(size);

        for (int i = 0; i < size; i++) {
            WindowWrap<T> windowWrap = array.get(i);
            if (windowWrap == null || isWindowDeprecated(windowWrap)) {
                continue;
            }
            result.add(windowWrap.value());
        }
        return result;
    }
}
