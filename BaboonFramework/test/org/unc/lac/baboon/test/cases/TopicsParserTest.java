package org.unc.lac.baboon.test.cases;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.topic.Topic;
import org.unc.lac.baboon.utils.TopicsJsonParser;

public class TopicsParserTest {

    private final String topicsPath01 = "/topics01.json";//"/org/unc/lac/baboon/test/resources/topics01.json";

    /**
     * <li>Given I have a topics json file containing three topics</li>
     * <li>And I declare the expected values the same as in json file</li>
     * <li>And I use {@link TopicsJsonParser#getTopicsFromJson(String)} to
     * obtain a java object representation of topics json</li>
     * <li>When I compare the size of the expected values and the java object
     * representation obtained from
     * {@link TopicsJsonParser#getTopicsFromJson(String)} (actual values)</li>
     * <li>And I compare the names of the expected values and the actual
     * values</li>
     * <li>And I compare the permissions of the expected values and the actual
     * values</li>
     * <li>And I compare the fire callbacks of the expected values and the
     * actual values</li>
     * <li>And I compare the set guard callbacks of the expected values and the
     * actual values</li>
     * <li>Then the sizes are equal</li>
     * <li>And the names are equal></li>
     * <li>And the permissions are equal></li>
     * <li>And the fire callbacks are equal></li>
     * <li>And the set guard callbacks are equal></li>
     */
    @Test
    public void test() {
        TopicsJsonParser parser = new TopicsJsonParser();
        final String[] expectedNames = { "topic1", "topic2", "topic3" };
        final String[] expectedPermissions = { "transition0", "transition2", "t6" };
        final String[][] expectedCallbacks = { { "t1", "t2", "tx" }, {}, { "pepe", "pedro", "juan" } };
        final String[][][] expectedSetGuardCallbacks = { {}, {}, {{"g1", "g2", "g3"},{"g4"},{},{"g5", "g6"}} };
        try {
            HashMap<String, Topic> topics = parser.getTopicsFromJson(topicsPath01);
            assertEquals(expectedNames.length, topics.size());
            String[] names = new String[topics.size()];
            String[] permissions = new String[topics.size()];
            for (int i = 0; i < topics.size(); i++) {
                names[i] = topics.get(expectedNames[i]).getName();
                permissions[i] = topics.get(names[i]).getPermission().get(0);
                for (int j = 0; j < expectedNames.length; j++) {
                    if (expectedNames[j].equals(topics.get(names[i]).getName())) {
                        String[] actualCallbacks = new String[expectedCallbacks[i].length];
                        actualCallbacks = topics.get(names[i]).getFireCallback().toArray(actualCallbacks);
                        assertArrayEquals(expectedCallbacks[i], actualCallbacks);                      
                        ArrayList<String[]> actualSetGuardCallbacks = topics.get(names[i]).getSetGuardCallback();
                        assertEquals(expectedSetGuardCallbacks[i].length,actualSetGuardCallbacks.size());                    
                        for(int k=0; k < expectedSetGuardCallbacks[i].length; k++){
                            assertArrayEquals(expectedSetGuardCallbacks[i][k], actualSetGuardCallbacks.get(k));
                        }
                        
                    }
                }
            }
            assertArrayEquals(expectedNames, names);
            assertArrayEquals(expectedPermissions, permissions);

        } catch (BadTopicsJsonFormat | NoTopicsJsonFileException e) {
            fail(e.getMessage());
        }
    }

}
