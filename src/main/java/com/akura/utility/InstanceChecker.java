package com.akura.utility;

import com.akura.retrieval.models.Entity;
import org.apache.jena.ontology.OntModel;

public class InstanceChecker {

    public static boolean isEntityExists(OntModel m, String search, boolean isHash) {

        Entity entity = new Entity(m);
        String hashCode = isHash ? search : HashGeneratorClass.generateHashForString(search, "ENTITY");
        entity.getEntityByHash(hashCode);

        if (entity.instance != null) {
            return true;
        } else {
            return  false;
        }

    }
}
