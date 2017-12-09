package org.unc.lac.baboon.execution;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A pool of threads used by Baboon framework to execute the {@link DummyThread}
 * objects.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 */
public class DummiesExecutor extends ThreadPoolExecutor {

    public DummiesExecutor() {
        super(0, Integer.MAX_VALUE, Long.MAX_VALUE, TimeUnit.HOURS, new SynchronousQueue<Runnable>());
    }

    /**
     * This method submits a {@link DummyThread} object into the pool executor
     * and increases in one the size of the core pool.
     * @see ThreadPoolExecutor#submit(java.util.concurrent.Callable)
     * @see ThreadPoolExecutor#setCorePoolSize(int)
     * 
     * @param dummy
     *      the {@link DummyThread} object. The method {@link DummyThread#call()} 
     *      is called by this {@link ThreadPoolExecutor}.
     *       
     */
    public void executeDummy(DummyThread dummy) {
        this.setCorePoolSize(this.getCorePoolSize() + 1);
        this.submit(dummy);
    }
}