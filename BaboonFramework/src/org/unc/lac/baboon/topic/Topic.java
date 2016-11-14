package org.unc.lac.baboon.topic;

import java.util.ArrayList;
import org.unc.lac.baboon.utils.TopicsJsonParser;
/**
 * Topic template. Instances of this class will be created
 * when reading the topics json file.
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see TopicsJsonParser
 */
public class Topic {
    public String name; 
    public String permission;
    public ArrayList<String> fireCallback = new ArrayList<String>();
    public ArrayList<String> setGuardCallback = new ArrayList<String>();;
    
}
