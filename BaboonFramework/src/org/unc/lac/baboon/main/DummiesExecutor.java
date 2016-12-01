package org.unc.lac.baboon.main;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A pool of threads used by Baboon framework to execute the {@link DummyThread} objects. 
 */
public class DummiesExecutor extends ThreadPoolExecutor {
    private static final int CORE_POOL_SIZE = 4;

    public DummiesExecutor() {
        super(CORE_POOL_SIZE, Integer.MAX_VALUE, Long.MAX_VALUE, TimeUnit.NANOSECONDS,
                new SynchronousQueue<Runnable>());
    }
}