package com.galaxybase.benchmark.hugegraph.test.util;

import com.galaxybase.benchmark.common.util.Sample;

/**
 * @author cl32
 * 2020/11/10
 */
public abstract class SingleSampleTest extends HugeTest {

    @Override
    public Object[] getSample(Sample sample, int n) {
        return new Object[]{sample.get(n)};
    }
}
