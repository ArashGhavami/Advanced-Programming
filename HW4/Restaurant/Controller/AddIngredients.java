package Controller;

import model.Pizza;
import model.Restaurant;
import model.Sandwich;

import java.util.ArrayList;

public class AddIngredients {

    public static String addIngredients(ArrayList<String> foods) {
        ArrayList<String> realAllFoods = getRealFoods(foods);
        if (!checkAvailability(realAllFoods)) {
            return "Order Dismissed!";
        }
        makeOrder(foods);
        return "Order Completed!";
    }

    private static boolean checkAvailability(ArrayList<String> foods) {
        ArrayList<String> availableIngredients = Restaurant.getRestaurant().getIngredientNames();
        ArrayList<Integer> ingredientsAmount = Restaurant.getRestaurant().getIngredientAmounts();
        for (int i = 0; i < availableIngredients.size(); i++) {
            for (int j = 0; j < foods.size(); j++) {
                if (availableIngredients.get(i).equals(foods.get(j))) {
                    if (ingredientsAmount.get(i) < howManyInArrayList(foods.get(j), foods)) return false;
                }
            }
        }
        return true;
    }

    private static ArrayList<String> getRealFoods(ArrayList<String> foods) {
        ArrayList<String> realAllFoods = new ArrayList<>();
        if (foods.get(0).equals("Sandwich")) {
            realAllFoods.add("Bread");
            realAllFoods.add("Bread");
            for (int i = 1; i < foods.size(); i++) {
                realAllFoods.add(foods.get(i));
            }
        } else {
            realAllFoods.add("Dough");
            for (int i = 2; i < foods.size(); i++) {
                realAllFoods.add(foods.get(i));
            }
        }
        return realAllFoods;
    }

    private static int howManyInArrayList(String target, ArrayList<String> arrayList) {
        int result = 0;
        for (String string : arrayList) {
            if (string.equals(target)) result++;
        }
        return result;
    }

    private static void makeOrder(ArrayList<String> foods){
        if(foods.getFirst().equals("Sandwich")){
            foods.remove(foods.getFirst());
            orderSandwich(foods);
        }
        else{
            foods.remove(foods.get(1));
            String size = foods.get(0);
            foods.remove(foods.get(0));
            orderPizza(foods, size);
        }
    }

    private static void orderSandwich(ArrayList<String> foods){
        Sandwich sandwich = new Sandwich();
        ArrayList<String> allFoods = Restaurant.getRestaurant().getIngredientNames();
        ArrayList<Double> prices = Restaurant.getRestaurant().getIngredientCosts();
        ArrayList<Integer> amounts = Restaurant.getRestaurant().getIngredientAmounts();
        for (String food : foods) {
            for (int j = 0; j < allFoods.size(); j++) {
                if (allFoods.get(j).equals(food)) {
                    amounts.set(j, amounts.get(j) - 1);
                    sandwich.addToPrice(prices.get(j));
                }
            }
        }
        amounts.set(2, amounts.get(2) -2);
    }

    private static void orderPizza(ArrayList<String> foods, String size){
        Pizza pizza  = new Pizza(size);
        ArrayList<String> allFoods = Restaurant.getRestaurant().getIngredientNames();
        ArrayList<Double> prices = Restaurant.getRestaurant().getIngredientCosts();
        ArrayList<Integer> amounts = Restaurant.getRestaurant().getIngredientAmounts();

        for (String food : foods) {
            for (int j = 0; j < allFoods.size(); j++) {
                if (allFoods.get(j).equals(food)) {
                    amounts.set(j, amounts.get(j) - 1);
                    pizza.addToPrice(prices.get(j));
                }
            }
        }
        amounts.set(6, amounts.get(6) -1);
    }


}
