package org.unc.lac.baboon.task;

import java.lang.reflect.Method;

import org.unc.lac.baboon.topic.Topic;

public class HappeningHandlerSubscription extends AbstractActionSubscription {

    public HappeningHandlerSubscription(Object objInstance, Method objMethod, Topic topic) {
        super(topic);
        addAction(objInstance, objMethod);
    }

}
