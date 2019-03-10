package com.ylwu.slidingwindow.base;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Auther: Wuyulong
 * @Date: 2019/3/10 17:10
 * @Description:
 */
public class UnaryLeapArray extends LeapArray<LongAdder>{

    public UnaryLeapArray(int sampleCount, int intervalInMs) {
        super(sampleCount, intervalInMs);
    }

    @Override
    public LongAdder newEmptyBucket() {
        return new LongAdder();
    }

    @Override
    protected WindowWrap<LongAdder> resetWindowTo(WindowWrap<LongAdder> windowWrap, long startTime) {
        windowWrap.value().reset();
        return windowWrap.resetTo(startTime);
    }
}
