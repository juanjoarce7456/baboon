package org.unc.lac.baboon.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.unc.lac.baboon.exceptions.BadTopicsJsonFormat;
import org.unc.lac.baboon.exceptions.NoTopicsJsonFileException;
import org.unc.lac.baboon.topic.Topic;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Converts a json file containing an array of topics to Java objects representation
 * 
 * @author Ariel Ivan Rabinovich
 * @author Juan Jose Arce Giacobbe
 * @version 1.0
 * @see Topic
 */
public class TopicsJsonParser {
    static ObjectMapper objectMapper = new ObjectMapper();

    
    /**
     * Reads a json file containing an array of topics and returns 
     * an ArrayList of {@link Topic} objects
     * 
     * @param jsonPath the path of the json file to parse.
     * @return an ArrayList of {@link Topic} objects described by the json file.
     */
    public ArrayList<Topic> getTopicsFromJson(String jsonPath) throws BadTopicsJsonFormat, NoTopicsJsonFileException{
        try{
            File file = new File(jsonPath);
            ArrayList<Topic> topics= objectMapper.readValue(file, new TypeReference<ArrayList<Topic>>(){});
            return topics;
        }
        catch (JsonParseException | JsonMappingException e){
            throw new BadTopicsJsonFormat(e.getMessage());
        }
        catch (FileNotFoundException | NullPointerException e){
            throw new NoTopicsJsonFileException("The path provided does not correspond to a file. " + e.getMessage());
        }
        catch (IOException e){
            throw new NoTopicsJsonFileException(e.getMessage());
        }
    }
    
}

