package com.galaxybase.benchmark.hugegraph.test;

/**
 * @author cl32
 * 2020/11/10
 */
public abstract class KNeighborTest extends SingleRowTest {
    protected final String gremlin;

    public KNeighborTest() {
        if (prop) {
            this.gremlin = "g.V('" + vertexLabelId + ":%s').as('a').repeat(outE('" + edgeType + "')"
                    + ".has('creationDate', gt(1293811200000).and(lt(1325347200000)))"
                    + ".inV().dedup()).times(" + depth() + ").emit().as('b').where('a',neq('b')).toSet().size()";
        } else {
            this.gremlin = "g.V('" + vertexLabelId + ":%s').as('a').repeat(out().dedup())"
                    + ".times(" + depth() + ").emit().as('b').where('a',neq('b')).toSet().size()";
        }
    }

    @Override
    protected String getGremlin(Object[] s) {
        return String.format(gremlin, s[0]);
    }

    @Override
    public String getTestName() {
        return "KNeighbor " + depth() + " Depth";
    }

    /**
     * 度数
     *
     * @return
     */
    protected abstract int depth();

    public static final class KNeighbor1 extends KNeighborTest {
        @Override
        protected int depth() {
            return 1;
        }
    }

    public static final class KNeighbor2 extends KNeighborTest {
        @Override
        protected int depth() {
            return 2;
        }
    }

    public static final class KNeighbor3 extends KNeighborTest {
        @Override
        protected int depth() {
            return 3;
        }
    }

    public static final class KNeighbor4 extends KNeighborTest {
        @Override
        protected int depth() {
            return 4;
        }
    }

    public static final class KNeighbor5 extends KNeighborTest {
        @Override
        protected int depth() {
            return 5;
        }
    }

    public static final class KNeighbor6 extends KNeighborTest {
        @Override
        protected int depth() {
            return 6;
        }
    }
}
