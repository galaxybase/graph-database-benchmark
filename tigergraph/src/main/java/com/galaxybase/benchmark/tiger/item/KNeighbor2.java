package com.galaxybase.benchmark.tiger.item;

import com.galaxybase.benchmark.tiger.abstr.AbstractBFSMaster;

/**
 * @Author chenyanglin
 * @Date 2020/12/11 20:46
 * @Version 1.0
 */
public class KNeighbor2 extends AbstractBFSMaster {

    @Override
    public int getDepth() {
        return 2;
    }

    @Override
    public boolean isHop() {
        return false;
    }

}
