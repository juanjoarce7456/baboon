package org.unc.lac.baboon.topic;

import java.util.ArrayList;
import java.util.List;

import org.unc.lac.baboon.action_controller.HappeningActionController;
import org.unc.lac.baboon.action_controller.TaskActionController;
import org.unc.lac.baboon.action_controller.ActionController;
import org.unc.lac.baboon.utils.TopicsJsonParser;

/**
 * Topic template. Instances of this class will be created when reading the
 * topics json file.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see TopicsJsonParser
 */
public class Topic {

    /**
     * Name given to this topic.
     */
    private String name;

    /**
     * {@link List} containing the arrays of guard names that belongs to each
     * {@link ActionController} subscribed to this topic (one array of guard names per
     * {@link ActionController}).
     */
    private ArrayList<String[]> setGuardCallback = new ArrayList<String[]>();
    /**
     * {@link List} of permission transitions names that belongs to each
     * {@link ActionController} subscribed to this topic (one permission per
     * {@link ActionController}).
     */
    private ArrayList<String> permission = new ArrayList<String>();
    /**
     * {@link List} of callback transitions names that belongs to all of the
     * {@link ActionController} objects subscribed to this topic. They are fired after
     * finish the execution of the last {@link ActionController} method.
     */
    private ArrayList<String> fireCallback = new ArrayList<String>();

    /**
     * Returns a {@link String} object describing the name of the {@link Topic}.
     * 
     * @return Name of this Topic instance.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a {@link List} of {@link String} object describing the
     * permissions of the {@link Topic}. A permission is the name of the
     * transition that triggers the taskController or happeningController subscribed to this
     * {@link Topic}.
     * 
     * @return permission of this Topic instance.
     */
    public ArrayList<String> getPermission() {
        return permission;
    }

    /**
     * Returns a {@link List} of {@link String} containing the fire callback of
     * the Topic. The fire callback is a list of transitions names that will be
     * fired on the ending of the taskControllers or happeningControllers subscribed to this
     * topic.
     * 
     * @return The fire callback of this Topic instance.
     * 
     */
    public ArrayList<String> getFireCallback() {
        return fireCallback;
    }

    /**
     * Returns a {@link List} of arrays of {@link String} containing the set
     * guard callback of the Topic. The set guard callback is a list of array of
     * guards names that will be setted on the ending of each taskController or happening
     * handler subscribed to this topic. Each array of guard names correspond to
     * one {@link TaskActionController} or {@link HappeningActionController} subscribed to
     * the topic.
     * 
     * @return The set guard callback of this Topic instance.
     */
    public ArrayList<String[]> getSetGuardCallback() {
        return setGuardCallback;
    }

    /**
     * Returns an array of {@link String} containing the set guard callback of
     * the Topic. The set guard callback is a list of guards names that will be
     * setted on the ending of the taskControllers or happeningControllers subscribed to this topic
     * by using methods annotated with {@link GuardProvider}. This methods
     * should be present on the {@link ActionController} that generated the set guard
     * callback event.
     * 
     * @return The set guard callback of this Topic instance.
     * 
     */
    public String[] getGuardCallback(int index) {
        try {
            return setGuardCallback.get(index);
        } catch (IndexOutOfBoundsException e) {
            return new String[] {};
        }
    }

}
