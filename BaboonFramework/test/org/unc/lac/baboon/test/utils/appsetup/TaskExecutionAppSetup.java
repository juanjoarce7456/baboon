package org.unc.lac.baboon.test.utils.appsetup;

import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.exceptions.NotSubscribableException;
import org.unc.lac.baboon.main.BaboonApplication;
import org.unc.lac.baboon.main.BaboonFramework;
import org.unc.lac.baboon.test.utils.tasks.TaskExecutionController;
import org.unc.lac.javapetriconcurrencymonitor.petrinets.factory.PetriNetFactory.petriNetType;

/**
 * TaskExecutionAppSetup is used by {@link TaskExecutionTest} for testing
 * purposes. It creates a {@link TaskExecutionController}. Also a pnml file and
 * a topics file are used to initialize the framework. Finally it subscribes the
 * {@link TaskExecutionController#increaseNumber()} task to "topic1".
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @see BaboonApplication
 * @see BaboonFramework
 */
public class TaskExecutionAppSetup implements BaboonApplication {
    private static TaskExecutionController controller = new TaskExecutionController();
    private final String pnmlFile = "test/org/unc/lac/baboon/test/resources/pnml01.pnml";
    private final String topicsFile = "test/org/unc/lac/baboon/test/resources/topics04.json";

    @Override
    public void declare() {
        BaboonFramework.createPetriCore(pnmlFile, petriNetType.PLACE_TRANSITION, null);
        try {
            BaboonFramework.addTopicsFile(topicsFile);
        } catch (BadTopicsJsonFormat | NoTopicsJsonFileException e) {

        }
    }

    @Override
    public void subscribe() {
        try {
            BaboonFramework.subscribeToTopic("topic1", controller, "increaseNumber");
        } catch (NotSubscribableException e) {

        }
    }

    public static TaskExecutionController getController() {
        return controller;
    }

}