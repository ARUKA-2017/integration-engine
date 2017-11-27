package com.akura.retrieval.response;

import com.akura.config.Config;
import com.akura.logger.FileLogger;
import com.akura.retrieval.models.Entity;
import com.akura.utility.StringSimilarity;

import com.google.gson.Gson;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.ArrayList;

/**
 * Class used to represent EntityListResponse.
 */
public class EntityListResponse implements IRetrievalResponse {

    public EntityInstance[] entityInstanceList;

    public EntityListResponse(OntModel m, String search) {

        ArrayList<EntityInstance> entityList = new ArrayList<>();
        OntClass entityClass = (OntClass) m.getOntClass(Config.ENTITY);

        ExtendedIterator<Individual> indIterator = m.listIndividuals(entityClass);

        while (indIterator.hasNext()) {
            Entity entity = new Entity(m, indIterator.next());

            if(StringSimilarity.compareStrings(entity.getName(), search) >= Config.STRING_SIMILARITY_THRESHOLD) {

                EntityInstance entityInstance = new EntityInstance();
                entityInstance.entityName = entity.getName();
                entityInstance.hashID = entity.getHash();
                entityList.add(entityInstance);
            }
        }

        if(entityList.size() == 0) {
            indIterator = m.listIndividuals(entityClass);

            while (indIterator.hasNext()) {
                Entity entity = new Entity(m, indIterator.next());
                if(entity.getName().toLowerCase().startsWith(search.toLowerCase())) {
                    EntityInstance entityInstance = new EntityInstance();
                    entityInstance.entityName = entity.getName();
                    entityInstance.hashID = entity.getHash();
                    entityList.add(entityInstance);
                }
            }
        }

        FileLogger.Log("Retrieving  Suggestions",FileLogger.TYPE_TITLE, FileLogger.DEST_RETRIEVAL);
        FileLogger.Log(new Gson().toJson(this),FileLogger.TYPE_JSON, FileLogger.DEST_RETRIEVAL);

        this.entityInstanceList = entityList.toArray(new EntityInstance[entityList.size()]);

    }
}
