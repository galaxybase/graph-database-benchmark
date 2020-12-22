package com.galaxybase.benchmark.galaxybase.developer.item;

import com.galaxybase.benchmark.galaxybase.developer.item.abstr.AbstractBFS;

public class KNeighbor1 extends AbstractBFS {


    @Override
    protected int getDepth() {
        return 1;
    }

    @Override
    protected boolean isHop() {
        return false;
    }
}
