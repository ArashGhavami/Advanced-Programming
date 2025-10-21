package view;

import Controller.AddIngredients;
import model.Pizza;
import model.Restaurant;
import model.Sandwich;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class runProgram {

    private static final String regex = "(?<baseFood>(?<size>Large|Medium|Small) Pizza|Sandwich)((?<tarof> and| extra| also| with)+ \\S+)*\\s*";

    public static void start(Scanner scanner) {
        Restaurant restaurant = Restaurant.getRestaurant();
        ArrayList<Integer> ingredientAmounts = new ArrayList<>();
        int howManyOrders = scanner.nextInt();
        for (int i = 0; i < 23; i++) {
            ingredientAmounts.add(scanner.nextInt());
        }
        restaurant.setIngredientAmounts(ingredientAmounts);

        for(int i=0; i<=howManyOrders; i++){
            handleOrder(scanner);
        }
        System.out.printf("The final profit is: %.1f%n", getWholePrice());
    }

    private static Matcher getCommandMatcher(String input){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

    private static void handleOrder(Scanner scanner){
        String input = scanner.nextLine();
        Matcher matcher = getCommandMatcher(input);
        if(matcher.matches()){
            ArrayList<String> foods = getFoodAndIngredients(input);
            System.out.println(AddIngredients.addIngredients(foods));
        }
    }

    private static ArrayList<String> getFoodAndIngredients(String input){
        String[] notCompletedResult = input.split(" ");
        ArrayList<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(notCompletedResult));
        result.removeIf(str -> str.equals("and"));
        result.removeIf(str -> str.equals("with"));
        result.removeIf(str -> str.equals("also"));
        result.removeIf(str -> str.equals("extra"));
        result.removeIf(str -> str.equals(" "));
        return  result;
    }

    private static double getWholePrice(){
        double wholeMoney = 0;
        for(Pizza pizza : Pizza.getAllPizzas()){
            wholeMoney += pizza.getPrice();
        }
        for(Sandwich sandwich : Sandwich.getAllSandwiches()){
            wholeMoney += sandwich.getPrice();
        }
        return wholeMoney;
    }

}
