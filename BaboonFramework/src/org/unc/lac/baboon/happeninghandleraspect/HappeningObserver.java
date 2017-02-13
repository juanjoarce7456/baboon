package org.unc.lac.baboon.happeninghandleraspect;

public interface HappeningObserver {
    public void updateBefore(Object target, String methodName);
    public void updateAfter(Object target, String methodName);
}
