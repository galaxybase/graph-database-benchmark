package com.galaxybase.benchmark.nebula.test.util;

import com.galaxybase.benchmark.common.util.Sample;

/**
 * @author cl32
 * 2020/11/5
 */
public abstract class TwoSampleTest extends NebulaTest {

    @Override
    public final Object[] getSample(Sample sample, int n) {
        return sample.get(n).split(",");
    }
}
