package model;

import java.util.ArrayList;

public class Sandwich extends BaseFood{

    private static ArrayList<Sandwich> allSandwiches = new ArrayList<>();

    private final double basePrice = 2;
    private double ingredientsPrice;

    public Sandwich(){
        ingredientsPrice = 0;
        allSandwiches.add(this);
    }


    @Override
    public double getPrice() {
        return basePrice + ingredientsPrice;
    }

    public static ArrayList<Sandwich> getAllSandwiches() {
        return allSandwiches;
    }


    public void addToPrice(double price){
        this.ingredientsPrice += price;
    }
}
