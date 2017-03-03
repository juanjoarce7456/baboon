package org.unc.lac.baboon.task;

import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;

/**
 * This class defines an AbstractTaskSubscription as a list of
 * {@link TaskAction} objects, which are subscribed to a topic. It is used
 * internally by framework and should not be known by user.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * 
 * @see AbstractActionSubscription
 */
public abstract class AbstractTaskSubscription extends AbstractActionSubscription {

    /**
     * Constructor.
     * 
     * @param topic
     *            The topic to which {@link TaskAction} objects will be
     *            subscribed
     * 
     * @throws NotSubscribableException
     *             <li>When the topic is null</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             <li>If {@link Topic#permission} is null.</li>
     *             <li>If {@link Topic#permission} is empty.</li>
     * 
     */
    public AbstractTaskSubscription(Topic topic) throws NotSubscribableException {
        super(topic);
        if (topic.getPermission() == null || topic.getPermission().isEmpty()) {
            throw new NotSubscribableException("The topic's permission cannot be empty for a task subscription");
        }
    }

    /**
     * This method returns the {@link TaskAction} element at specified
     * actionIndex on {@link #actionsList}.
     * 
     * @param actionIndex
     *            index of the {@link TaskAction} object to return.
     * @return the {@link TaskAction} element at specified actionIndex on
     *         {@link #actionsList}.
     * 
     */
    @Override
    public TaskAction getAction(int actionIndex) {
        return (TaskAction) super.getAction(actionIndex);
    }

    /**
     * This method appends the given {@link TaskAction} to the end of
     * {@link #actionsList}
     * 
     * @param action
     *            {@link TaskAction} object to be appended to
     *            {@link #actionsList}. The permission and guard callback
     *            corresponding to this {@link TaskAction} are determined by the
     *            order (or index).
     * @return true if {@link #actionsList} changed as a result of the call
     * 
     * @throws NotSubscribableException
     *             <li>If the {@link TaskAction} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If the {@link Topic} object have less permissions than
     *             the number of {@link TaskAction} objects in
     *             {@link #actionsList}</li>
     *             <li>If the topic permission corresponding to this task is
     *             empty</li>
     *             <li>If the topic permission corresponding to this task is
     *             null</li>
     *             <li>If the action's type is not an instance of
     *             {@link TaskAction}</li>
     * 
     *
     * @see {@link Topic#permission}
     * @see {@link Topic#setGuardCallback}
     * 
     */
    @Override
    protected boolean addAction(Action action) throws NotSubscribableException {
        if (topic.getPermission().size() < actionsList.size() + 1) {
            throw new NotSubscribableException("Cannot add more actions than permits");
        }
        if (!(action instanceof TaskAction)) {
            throw new NotSubscribableException("The action must be of type TaskAction");
        }
        if (topic.getPermission().get(getSize()).isEmpty()) {
            throw new NotSubscribableException("The topic permission string cannot be empty for a task subscription");
        }
        if (topic.getPermission().get(getSize()) == null) {
            throw new NotSubscribableException("The topic permission array cannot be null for a task subscription");
        }
        return super.addAction(action);
    }

}
