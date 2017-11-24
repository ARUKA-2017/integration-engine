package com.akura.adaptor.input;

public class Entity {

    public String id;
    public String text;
    public Float sentiment;
    public Float salience;
    public String category;
    public String nounCombination;
    public String nounCombinationCategory;


    public Entity(){
        this.id = System.currentTimeMillis() + this.text.replaceAll(" ","");
    }
}
