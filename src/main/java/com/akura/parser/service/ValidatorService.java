package com.akura.parser.service;

import com.akura.parser.config.Config;

import java.util.Map;

/**
 * Class representing a ValidatorService.
 */
public class ValidatorService {

    /**
     * Method used to validate the JSON.
     *
     * @param json - JSON to be validated.
     * @return - boolean value.
     */
    public static Boolean validateJSON(Map json) {

        Boolean relatioship = false;
        Boolean entity = false;

        if (json.get(Config.entities) != null) {
            entity = true;
        }

        if (json.get(Config.relationships) != null) {
            relatioship = true;
        }
        return (relatioship && entity);
    }

    /**
     * Method used to get the entities.
     *
     * @param json - JSON.
     * @return - Map response.
     */
    public static Map getEntities(Map json) {
        return (Map) json.get(Config.entities);
    }
}
