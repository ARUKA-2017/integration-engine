package com.akura.utility;

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

        if (entity.instance != null) {
            return true;
        } else {
            return false;
        }
    }
}
