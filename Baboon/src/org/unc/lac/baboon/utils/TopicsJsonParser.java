package org.unc.lac.baboon.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.topic.Topic;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts a json file containing an array of topics to Java objects
 * representation
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see Topic
 */
public class TopicsJsonParser {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads a json file containing an array of topics and returns a HashMap of
     * {@link Topic} objects indexed by topic name
     * 
     * @param jsonPath
     *            the path of the json file to parse.
     * @return a HashMap of the {@link Topic} objects described by the json file
     *         indexed by topic name.
     */
    public HashMap<String, Topic> getTopicsFromJson(String jsonPath)
            throws BadTopicsJsonFormat, NoTopicsJsonFileException {
        try {
            InputStream jsonFileStream = this.getClass().getResourceAsStream(jsonPath);
            if (jsonFileStream == null) {
                throw new FileNotFoundException("File " + jsonPath + " not found");
            }
            ArrayList<Topic> topics = objectMapper.readValue(jsonFileStream, new TypeReference<ArrayList<Topic>>() {
            });
            HashMap<String, Topic> topicsIndexedByName = new HashMap<String, Topic>();
            for (Topic topic : topics) {
                topicsIndexedByName.put(topic.getName(), topic);
            }
            return topicsIndexedByName;
        } catch (JsonParseException | JsonMappingException e) {
            throw new BadTopicsJsonFormat(e.getMessage());
        } catch (FileNotFoundException | NullPointerException e) {
            throw new NoTopicsJsonFileException("The path provided does not correspond to a file. " + e.getMessage());
        } catch (IOException e) {
            throw new NoTopicsJsonFileException(e.getMessage());
        }
    }

}
