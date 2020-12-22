package com.galaxybase.benchmark.galaxybase.developer.item;

import com.galaxybase.benchmark.galaxybase.developer.item.abstr.AbstractBFS;

public class KNeighbor5 extends AbstractBFS {


    @Override
    protected int getDepth() {
        return 5;
    }

    @Override
    protected boolean isHop() {
        return false;
    }
}
