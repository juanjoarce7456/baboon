package org.unc.lac.baboon.subscription;

import org.unc.lac.baboon.actioncontroller.ActionController;
import org.unc.lac.baboon.actioncontroller.TaskActionController;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.topic.Topic;

import com.google.common.base.Strings;

import org.unc.lac.baboon.annotations.GuardProvider;

/**
 * This class defines an AbstractTaskControllerSubscription as a list of
 * {@link TaskActionController} objects, which are subscribed to a topic. 
 * It is used internally by framework and should not be known by user.
 *
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * 
 * @see AbstractActionControllerSubscription
 */
public abstract class AbstractTaskControllerSubscription extends AbstractActionControllerSubscription {

    /**
     * Constructor.
     * 
     * @param topic
     *            The topic to which {@link TaskActionController} objects will be
     *            subscribed
     * 
     * @throws NotSubscribableException
     *             <ul>
     *             <li>When the topic is null</li>
     *             <li>If there are guard callbacks on the topic and
     *             {@link Topic#setGuardCallback} and {@link Topic#permission}
     *             sizes are different.</li>
     *             <li>If {@link Topic#permission} is null.</li>
     *             <li>If {@link Topic#permission} is empty.</li>
     *             </ul>
     * 
     */
    public AbstractTaskControllerSubscription(Topic topic) throws NotSubscribableException {
        super(topic);
        if (topic.getPermission() == null || topic.getPermission().isEmpty()) {
            throw new NotSubscribableException("The topic's permission cannot be empty for a taskController subscription");
        }
    }

    /**
     * This method returns the {@link TaskActionController} element at specified
     * actionIndex on {@link #actionsList}.
     * 
     * @param actionIndex
     *            index of the {@link TaskActionController} object to return.
     * @return the {@link TaskActionController} element at specified actionIndex on
     *         {@link #actionsList}.
     * 
     */
    @Override
    public TaskActionController getAction(int actionIndex) {
        return (TaskActionController) super.getAction(actionIndex);
    }

    /**
     * This method appends the given {@link TaskActionController} to the end of
     * {@link #actionsList}
     * 
     * @param actionController
     *            {@link TaskActionController} object to be appended to
     *            {@link #actionsList}. The permission and guard callback
     *            corresponding to this {@link TaskActionController} are determined by the
     *            order (or index).
     * @return true if {@link #actionsList} changed as a result of the call
     * 
     * @throws NotSubscribableException
     *             <ul>
     *             <li>If the {@link TaskActionController} object does not have a
     *             {@link GuardProvider} annotated method to handle a guard
     *             declared in the topic</li>
     *             <li>If the {@link Topic} object have less permissions than
     *             the number of {@link TaskActionController} objects in
     *             {@link #actionsList}</li>
     *             <li>If the topic permission corresponding to this taskController is
     *             empty</li>
     *             <li>If the topic permission corresponding to this taskController is
     *             null</li>
     *             <li>If the actionController's type is not an instance of
     *             {@link TaskActionController}</li>
     *             </ul>
     * 
     *
     * @see Topic#permission
     * @see Topic#setGuardCallback
     * 
     */
    @Override
    protected boolean addAction(ActionController actionController) throws NotSubscribableException {
        if (topic.getPermission().size() < actionsList.size() + 1) {
            throw new NotSubscribableException("Cannot add more actionControllers than permits");
        }
        if (!(actionController instanceof TaskActionController)) {
            throw new NotSubscribableException("The actionController must be of type TaskActionController");
        }
        if (Strings.isNullOrEmpty(topic.getPermission().get(getSize()))) {
            throw new NotSubscribableException("The topic permission array cannot be empty or null for a taskController subscription");
        }
        return super.addAction(actionController);
    }

}
