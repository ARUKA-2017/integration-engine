package com.akura.utility;

import com.akura.logger.FileLogger;
import com.akura.retrieval.models.Entity;

import org.apache.jena.ontology.OntModel;

/**
 * Class representing an InstanceChecker.
 */
public class InstanceChecker {

    /**
     * Method used to check whether an entity exists or not in the ontology.
     *
     * @param m      - Ontology model.
     * @param search - Search keyword
     * @param isHash - is hash based search.
     * @return - whether entity exists.
     */
    public static boolean isEntityExists(OntModel m, String search, boolean isHash) {

        Entity entity = new Entity(m);
        String hashCode = isHash ? search : HashGeneratorClass.generateHashForString(search, "ENTITY");
        entity.getEntityByHash(hashCode);

        FileLogger.Log("Checking if the search Entity Exists ",FileLogger.TYPE_TITLE, FileLogger.DEST_RETRIEVAL);
        if (entity.instance != null) {
            FileLogger.Log("Entity Exists in the knowledge base",FileLogger.TYPE_CONT, FileLogger.DEST_RETRIEVAL);
            return true;
        } else {
            FileLogger.Log("Entity doesn't exist in the knowledge base",FileLogger.TYPE_CONT, FileLogger.DEST_RETRIEVAL);
            return false;
        }
    }
}
