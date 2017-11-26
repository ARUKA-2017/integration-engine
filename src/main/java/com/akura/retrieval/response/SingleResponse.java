package com.akura.retrieval.response;

import com.akura.retrieval.models.Entity;
import com.akura.utility.HashGeneratorClass;

import org.apache.jena.ontology.OntModel;

/**
 * Class representing a SingleResponse.
 */
public class SingleResponse implements IRetrievalResponse {

    public String name;
    public String id;
    public Double avg_baseScore;
    public ComparisonResponse comparisons;
    public PropertyResponse[] properties;
    public FeatureResponse[] features;
    public String[] pros;
    public String[] cons;

    public SingleResponse(OntModel m, String search, boolean isHash) {

        Entity entity = new Entity(m);
        String hashCode = isHash ? search : HashGeneratorClass.generateHashForString(search, "ENTITY");
        entity.getEntityByHash(hashCode);
        this.InitFromEntity(entity);

    }

    /**
     * Method used to initialize the entity values.
     *
     * @param entity - entity.
     */
    public void InitFromEntity(Entity entity) {
        if (entity.instance != null) {
            this.name = entity.getName();
            this.id = entity.getHash();
            this.avg_baseScore = entity.getAvgBaseScore();
            this.features = entity.getFeatures();
            this.properties = entity.getProperties();
            this.comparisons = entity.getComparisons();
            this.pros = entity.getPros();
            this.cons = entity.getCons();

        }
    }
}
