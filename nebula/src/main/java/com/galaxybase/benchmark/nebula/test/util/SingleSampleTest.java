package com.galaxybase.benchmark.nebula.test.util;

import com.galaxybase.benchmark.common.util.Sample;

/**
 * @author cl32
 * 2020/11/5
 */
public abstract class SingleSampleTest extends NebulaTest {

    @Override
    public final Object[] getSample(Sample sample, int n) {
        return new Object[]{sample.get(n)};
    }
}
