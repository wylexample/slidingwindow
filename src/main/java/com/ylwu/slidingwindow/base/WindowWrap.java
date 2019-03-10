package com.ylwu.slidingwindow.base;

/**
 * @Auther: Wuyulong
 * @Date: 2019/3/10 16:59
 * @Description: 时间窗口的包装实体
 */
public class WindowWrap<T> {


    /**
     * 单个存储桶的窗口的时间长度
     */
    private final long windowLengthInMs;

    /**
     * 单个时间窗口的开始时间(毫秒值)
     */
    private long windowStart;

    /**
     * 时间窗口存储的数据
     */
    private T value;

    public WindowWrap(long windowLengthInMs, long windowStart, T value) {
        this.windowLengthInMs = windowLengthInMs;
        this.windowStart = windowStart;
        this.value = value;
    }

    public long windowLength() {
        return windowLengthInMs;
    }

    public long windowStart() {
        return windowStart;
    }

    public T value() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public WindowWrap<T> resetTo(long startTime){
        this.windowStart = startTime;
        return this;
    }

    @Override
    public String toString() {
        return "WindowWrap{" +
                "windowLengthInMs=" + windowLengthInMs +
                ", windowStart=" + windowStart +
                ", value=" + value +
                '}';
    }
}
