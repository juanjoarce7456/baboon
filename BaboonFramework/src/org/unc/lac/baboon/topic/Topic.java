package org.unc.lac.baboon.topic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.unc.lac.baboon.utils.TopicsJsonParser;

import javassist.expr.NewArray;

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
    private String name;
    private ArrayList<String[]> setGuardCallback = new ArrayList<String[]>() ;
    private ArrayList<String> permission;
    private ArrayList<String> fireCallback= new ArrayList<String>();

    /**
     * Returns a {@link String} object describing the name of the {@link Topic}.
     * 
     * @return Name of this Topic instance.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a {@link String} object describing the permission of the
     * {@link Topic}. A permission is the name of the transition that triggers
     * the tasks and happenings subscribed to this {@link Topic}.
     * 
     * @return permission of this Topic instance.
     */
    public ArrayList<String> getPermission() {
        return permission;
    }

    /**
     * Returns an unmodifiable {@link List} of {@link String} containing the
     * fire callback of the Topic. The fire callback is a list of transitions
     * names that will be fired on the ending of the tasks or happenings
     * subscribed to this topic.
     * 
     * @return The fire callback of this Topic instance.
     * 
     * @see Collections#unmodifiableList(List)
     */
    public ArrayList<String> getFireCallback() {
        return fireCallback;
    }

    /**
     * Returns an unmodifiable {@link List} of {@link String} containing the set
     * guard callback of the Topic. The set guard callback is a list of guards
     * names that will be setted on the ending of the tasks or happenings
     * subscribed to this topic by using methods annotated with
     * {@link GuardProvider}. This methods should be present on the class that
     * generated the set guard callback event.
     * 
     * @return The set guard callback of this Topic instance.
     * 
     * @see Collections#unmodifiableList(List)
     */
    public ArrayList<String[]> getSetGuardCallback() {
        return setGuardCallback;
    }
    
    /**
     * Returns an unmodifiable {@link List} of {@link String} containing the set
     * guard callback of the Topic. The set guard callback is a list of guards
     * names that will be setted on the ending of the tasks or happenings
     * subscribed to this topic by using methods annotated with
     * {@link GuardProvider}. This methods should be present on the class that
     * generated the set guard callback event.
     * 
     * @return The set guard callback of this Topic instance.
     * 
     * @see Collections#unmodifiableList(List)
     */
    public String[] getGuardCallback(int index) {
        try{
            return setGuardCallback.get(index);
        }
        catch(IndexOutOfBoundsException e){
            return new String[]{};
        }
    }

}
