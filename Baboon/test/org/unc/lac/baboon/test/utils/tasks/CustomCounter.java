package org.unc.lac.baboon.test.utils.tasks;

import org.unc.lac.baboon.test.cases.DummyThreadTest;

/**
 * CustomCounter is a wrapper of an int
 * used for testing purposes.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * 
 * @see DummyThreadTest
 */
public class CustomCounter {
    private int count = 0;
    
    public void increase(){
        count++;
    }
    public int getVal(){
        return count;
    }
}
