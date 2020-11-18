package me.shufy.john.survival.crafting.recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class IngredientPairList {
    private final Collection<IngredientPair> ingredientPairCollection = new ArrayList<>();
    public IngredientPairList(IngredientPair... ingredientPairs) {
        Collections.addAll(ingredientPairCollection, ingredientPairs);
    }
    public IngredientPair[] toArray() {
        return (IngredientPair[]) ingredientPairCollection.toArray();
    }
    public ArrayList<IngredientPair> toList() {
        return new ArrayList<>(ingredientPairCollection);
    }
    public Collection<IngredientPair> getCollection() {
        return ingredientPairCollection;
    }
}
