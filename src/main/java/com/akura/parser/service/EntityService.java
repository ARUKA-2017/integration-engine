package com.akura.parser.service;

import com.akura.parser.config.Config;
import com.akura.parser.models.Entity;
import com.akura.parser.models.Ontology;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Class representing EntityService.
 */
public class EntityService {

    public Map entityList;
    public OntModel m;
    public UUID uuid = UUID.randomUUID();

    public EntityService(Map entities) {
        m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        entityList = entities;
        Entity ent = new Entity(Config.ROOTCLASSNAME + uuid.toString(), m);
        this.generateEntitiesFromObject(this.entityList, ent, ent.name);
    }

    /**
     * Method used to generate entities from object.
     *
     * @param _entityList - list of entity.
     * @param _ent        - entity.
     * @param _key        - key value.
     */
    public void generateEntitiesFromObject(Map _entityList, Entity _ent, String _key) {

        Set keys = _entityList.keySet();

        Entity ent = new Entity(_key, m);

        for (Object key : keys) {

            // simple type key value or complex time
            if (_entityList.get(key.toString()) != null && resolveType(_entityList.get(key.toString()),
                    ent, key.toString(), false)) {
                ent.addSimpleType(key.toString(), _entityList.get(key.toString()));
            }
        }

        // here ent processes should be over. This is the ideal time to save ent
        if (ent.simpleTypes.isEmpty() && ent.simpleComplexTypes.isEmpty()
                && ent.complexTypes.size() == 1 && ent.complexTypes.get(ent.complexTypes.keySet().iterator().next()).size() > 1 && _ent != null
                && !ent.name.equals(Config.ROOTCLASSNAME + uuid.toString())) {

            for (Object key : ent.complexTypes.keySet()) {
                ArrayList<Entity> entities = ent.complexTypes.get(key.toString());

                for (Entity childEnt : entities) {

                    if (childEnt.classURI != null) {

                        //todo this function has issues
//                        childEnt.changeClassName(ent.name);

                        _ent.addComplexType(ent.name, childEnt);
                    }

                }

            }
        } else {
            ent.saveToOntology();

            if (_ent != null) {
                _ent.addComplexType(_key, ent);
            }

        }
    }

    /**
     * Method used to generate entities from an array.
     *
     * @param _entityList - list of entity.
     * @param ent         - entity.
     * @param key         - key value.
     */
    public void generateEntitiesFromArray(ArrayList _entityList, Entity ent, String key) {

        for (Object obj : _entityList) {

            resolveType(obj, ent, key, true);
        }
    }

    /**
     * Method used to resolve types.
     *
     * @param obj            - object.
     * @param ent            - entity.
     * @param key            - key values
     * @param comesFromArray - whether it comes from an array.
     * @return - boolean value.
     */
    public Boolean resolveType(Object obj, Entity ent, String key, boolean comesFromArray) {

        if (obj.getClass().getName() == Config.arrayListClass) {

            this.generateEntitiesFromArray((ArrayList) obj, ent, key);
            return false;

        } else if (obj.getClass().getName() == Config.objectClass) {

            generateEntitiesFromObject((Map) obj, ent, key);
            return false;

        } else {
            if (comesFromArray) {
                ent.addSimpleComplexType(key, obj.toString());
            }
            return true;
        }
    }

    /**
     * Method used to save the ontology.
     */
    public void saveOntology() {
        Ontology.saveOntologyFile(this.m);
    }
}
