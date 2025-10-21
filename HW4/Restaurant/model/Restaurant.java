package model;

import java.util.ArrayList;

public class Restaurant {

    private final ArrayList<String> ingredientNames =new ArrayList<>();
    private final ArrayList<Double> ingredientCosts = new ArrayList<>();
    private ArrayList<Integer> ingredientAmounts = new ArrayList<>();

    private static Restaurant restaurant;

    private Restaurant(){
        setIngredientNames();
        setIngredientCosts();
    }

    public static Restaurant getRestaurant(){
        if(restaurant == null) restaurant = new Restaurant();
        return restaurant;
    }

    private void setIngredientNames(){
        ingredientNames.add("Bacon");
        ingredientNames.add("Basil");
        ingredientNames.add("Bread");
        ingredientNames.add("Cheese");
        ingredientNames.add("Chicken");
        ingredientNames.add("Corn");
        ingredientNames.add("Dough");
        ingredientNames.add("Egg");
        ingredientNames.add("Fries");
        ingredientNames.add("Garlic");
        ingredientNames.add("Hamburger");
        ingredientNames.add("Jalapeno");
        ingredientNames.add("Lettuce");
        ingredientNames.add("Mushroom");
        ingredientNames.add("Olive");
        ingredientNames.add("Onion");
        ingredientNames.add("Pepper");
        ingredientNames.add("Pepperoni");
        ingredientNames.add("Pickles");
        ingredientNames.add("Salami");
        ingredientNames.add("Sauce");
        ingredientNames.add("Tomato");
        ingredientNames.add("Tuna");
    }

    private void setIngredientCosts(){
        ingredientCosts.add(2.2);
        ingredientCosts.add(1.2);
        ingredientCosts.add(0.8);
        ingredientCosts.add(1.0);
        ingredientCosts.add(2.4);
        ingredientCosts.add(1.0);
        ingredientCosts.add(0.0);
        ingredientCosts.add(1.9);
        ingredientCosts.add(3.6);
        ingredientCosts.add(3.6);
        ingredientCosts.add(2.8);
        ingredientCosts.add(4.0);
        ingredientCosts.add(1.8);
        ingredientCosts.add(1.6);
        ingredientCosts.add(1.6);
        ingredientCosts.add(3.5);
        ingredientCosts.add(1.8);
        ingredientCosts.add(3.0);
        ingredientCosts.add(2.8);
        ingredientCosts.add(1.5);
        ingredientCosts.add(1.0);
        ingredientCosts.add(3.2);
        ingredientCosts.add(2.8);
    }

    public void setIngredientAmounts(ArrayList<Integer> ingredientAmounts){
        this.ingredientAmounts = ingredientAmounts;
    }

    public ArrayList<String> getIngredientNames() {
        return ingredientNames;
    }

    public ArrayList<Double> getIngredientCosts() {
        return ingredientCosts;
    }

    public ArrayList<Integer> getIngredientAmounts() {
        return ingredientAmounts;
    }
}
