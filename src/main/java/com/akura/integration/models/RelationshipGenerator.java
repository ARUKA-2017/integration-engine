package com.akura.integration.models;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;

public class RelationshipGenerator {

    public static void setRelationship(
            ObjectProperty relationship,
            Individual domain,
            Individual range) {

        // eg: A baseScrore has an entity
        // bascore --> domain
        // entity --> range

//        ObjectProperty prop = model.getObjectProperty(OntologyClass.URI_NAMESPACE + relationship);

//        System.out.println("domain "+ domain);
//        System.out.println("relationship "+ relationship);
//        System.out.println("range "+ range);

        domain.addProperty(relationship,range);


    }
}
