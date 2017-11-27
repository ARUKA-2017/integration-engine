package com.akura.integration.dynamic;

import com.akura.integration.models.*;

import com.akura.logger.FileLogger;
import com.google.gson.Gson;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class representing a DynamicEntity.
 */
public class DynamicEntity {

    private OntModel dynamic;

    public static OntModel stat;

    public OntClass entityClass;
    public Individual instance;
    public ArrayList<Property> properties;
    public Boolean isEntity;
    public Review review;
    public Entity staticEntity;
    public Feature staticFeature;


    public DynamicEntity(OntModel dynamic, OntModel stat, Individual instance, Review review) {
        this.dynamic = dynamic;
        this.stat = stat;
        this.instance = instance;
        this.getProperties();
        try {
            this.isFeature();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.review = review;
        FileLogger.Log("Dynamic Entity Details",FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
        FileLogger.Log(new Gson().toJson(this),FileLogger.TYPE_JSON, FileLogger.DEST_INTEGRATION);
    }

    /**
     * Method used to get the properties of the entity.
     *
     * @return - list of properties,
     */
    public ArrayList<Property> getProperties() {


        this.properties = IntegrationHelper.listPropertiesToArrayList(this.instance);
        FileLogger.Log("Instance Properties",FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
//        FileLogger.Log(new Gson().toJson(this.properties),FileLogger.TYPE_JSON, FileLogger.DEST_INTEGRATION);
        return this.properties;
    }

    /**
     * Method used to check whether it is a feature.
     *
     * @return - boolean value.
     * @throws Exception - exception.
     */
    public boolean isFeature() throws Exception {
        this.isEntity = true;

        ParameterizedSparqlString ps = new ParameterizedSparqlString();
        ps.setCommandText(
                "prefix owl: <http://www.w3.org/2002/07/owl#> " +
                        "SELECT ?subject " +
                        "WHERE { " +
                        "?subject <" + IntegrationHelper.getPropertyURI(this.dynamic, "SubEntity") + ">" +
                        "<" + this.instance.getURI().toString() + ">" +
                        "}"
        );

        ps.setLiteral("identifier", this.instance.getURI().toString());

        Query query = QueryFactory.create(ps.toString());
        QueryExecution queryExecution = QueryExecutionFactory.create(query, this.dynamic);

        try {

            ResultSet resultSet = queryExecution.execSelect();
            if (resultSet.hasNext()) {

                this.isEntity = false;
                Individual instance = this.dynamic.getIndividual(resultSet.next().get("subject").toString());
                ArrayList<Property> indProperties = IntegrationHelper.listPropertiesToArrayList(instance);
                String name = IntegrationHelper.getLiteralPropertyValue(indProperties, "Name", instance);
                this.staticEntity = new Entity(this.stat, name);

            }
        } finally {
            queryExecution.close();
        }

        return !this.isEntity;

    }

    /**
     * Method used to set the static ontology entity instance.
     *
     * @param stat - static ontology model.
     * @return - Entity.
     * @throws Exception - exception.
     */
    public Entity setStaticOntoEntityInstance(OntModel stat) throws Exception {

        FileLogger.Log("Building an Instance to merge with system knowledge base",FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
        if (this.isEntity) {
            String name = IntegrationHelper.getLiteralPropertyValue(this.properties, "Name", this.instance);
            Entity ent = new Entity(stat, name);

            FileLogger.Log("Instance: "+ name,FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
            // set Properties
            Map<String, String> map = this.getStaticOntoProperties();
            FileLogger.Log("Setting Relationships for: "+ name,FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
            FileLogger.Log(new Gson().toJson(map),FileLogger.TYPE_JSON, FileLogger.DEST_INTEGRATION);
            for (Map.Entry<String, String> m : map.entrySet()) {
                ent.setProperty(m.getKey(), m.getValue());
            }

            // set Features
            this.setFeaturesFromEntity(ent);

            // set baseScore
            BaseScore score = new BaseScore(ent.model, this.review);
            score.setScore(IntegrationHelper.getLiteralPropertyValueFloat(this.properties, "BaseScore", this.instance));
            ent.setBaseScore(score.instance);
            this.setComparisonsForEntity(ent);

            return ent;

        } else {
            throw new Exception("Not an EntityService to create an Object. This should be a feature");
        }
    }

    /**
     * Method used to get the entity object.
     *
     * @param stat - static ontology model.
     * @return - entity.
     * @throws Exception -exception.
     */
    public Entity getEntityObject(OntModel stat) throws Exception {
        String name = IntegrationHelper.getLiteralPropertyValue(this.properties, "Name", this.instance);
        Entity ent = new Entity(stat, name);
        return ent;
    }

    /**
     * Method used to set features from the entity.
     *
     * @param ent - entity.
     * @throws Exception - exception.
     */
    public void setFeaturesFromEntity(Entity ent) throws Exception {

        ArrayList<Individual> featureEntites = IntegrationHelper
                .getObjectPropertyList(this.dynamic, this.properties, "SubEntity", this.instance);

        Iterator<Individual> iterator = featureEntites.iterator();

        while (iterator.hasNext()) {

            Individual ind = iterator.next();
            DynamicEntity featureEntity = new DynamicEntity(this.dynamic, this.stat, ind, this.review);
            Map<String, String> map = featureEntity.getStaticOntoProperties();

            String name = IntegrationHelper.getLiteralPropertyValue(featureEntity.properties,
                    "Name",
                    featureEntity.instance);

            Feature f = ent.setFeature(name, map);

            // set basescore to feature
            BaseScore score = new BaseScore(ent.model, this.review);
            score.setScore(IntegrationHelper.getLiteralPropertyValueFloat(featureEntity.properties, "BaseScore", featureEntity.instance));
            f.setBaseScore(score.instance);

            // set comparison to feature
            featureEntity.setComparisonsForFeature(f);
        }
    }


    /**
     * Method used get hashmap of dynamic properties in the ontology.
     *
     * @return - map.
     * @throws Exception - exception.
     */
    public Map<String, String> getStaticOntoProperties() throws Exception {
        Map<String, String> map = new HashMap<String, String>();

        ArrayList<Individual> propertyInstaces = IntegrationHelper
                .getObjectPropertyList(this.dynamic, this.properties, "HasProperty", this.instance);

        Iterator<Individual> iterator = propertyInstaces.iterator();
        while (iterator.hasNext()) {
            Individual ind = iterator.next();
            ArrayList<Property> indProperties = IntegrationHelper.listPropertiesToArrayList(ind);

            String key = IntegrationHelper.getLiteralPropertyValue(indProperties, "Key", ind);
            String val = IntegrationHelper.getLiteralPropertyValue(indProperties, "Value", ind);
            map.put(key, val);
        }

        return map;
    }

    /**
     * Method used to set comparisons for the entity.
     *
     * @param ent - entity.
     * @return - list of individuals.
     * @throws Exception - exception.
     */
    public ArrayList<Individual> setComparisonsForEntity(Entity ent) throws Exception {
        ArrayList<Individual> propertyInstaces = IntegrationHelper
                .getObjectPropertyList(this.dynamic, this.properties, "BetterThan", this.instance);

        Iterator<Individual> iterator = propertyInstaces.iterator();
        while (iterator.hasNext()) {
            Individual ind = iterator.next();
            ArrayList<Property> indProperties = IntegrationHelper.listPropertiesToArrayList(ind);

            // can be either entity or feature
            // can figure this out by existing type of the object
            // TODO make it generic

            String name = IntegrationHelper.getLiteralPropertyValue(indProperties, "Name", ind);
            Entity comparisonEntity = new Entity(stat, name);
            new Comparison(stat,
                    ent.hash,
                    comparisonEntity.hash,
                    ent.instance,
                    comparisonEntity.instance);
        }

        return propertyInstaces;
    }

    /**
     * Method used set comparisons for features.
     *
     * @param f - feature.
     * @return - lsit of individuals.
     * @throws Exception - exceptions.
     */
    public ArrayList<Individual> setComparisonsForFeature(Feature f) throws Exception {
        ArrayList<Individual> propertyInstaces = IntegrationHelper
                .getObjectPropertyList(this.dynamic, this.properties, "BetterThan", this.instance);

        Iterator<Individual> iterator = propertyInstaces.iterator();
        while (iterator.hasNext()) {
            Individual ind = iterator.next();
            ArrayList<Property> indProperties = IntegrationHelper.listPropertiesToArrayList(ind);

            // can be either entity or feature
            // can figure this out by existing type of the object
            // TODO make it generic

            String name = IntegrationHelper.getLiteralPropertyValue(indProperties, "Name", ind);
            DynamicEntity dn = new DynamicEntity(this.dynamic, this.stat, ind, this.review);

            Feature comparisonFeature = new Feature(stat, name, dn.staticEntity.hash);
            new Comparison(stat,
                    f.hash,
                    comparisonFeature.hash,
                    f.instance,
                    comparisonFeature.instance);
        }

        return propertyInstaces;
    }
}
