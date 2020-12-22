package com.galaxybase.benchmark.tiger.item;

import com.galaxybase.benchmark.tiger.abstr.AbstractBFSMaster;

/**
 * @Author chenyanglin
 * @Date 2020/12/11 20:46
 * @Version 1.0
 */
public class KNeighbor5 extends AbstractBFSMaster {

    @Override
    public int getDepth() {
        return 5;
    }

    @Override
    public boolean isHop() {
        return false;
    }

}
