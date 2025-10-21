package model;


import java.util.ArrayList;

public class Pizza extends BaseFood{

    private static ArrayList<Pizza> allPizzas = new ArrayList<>();

    private final double basePrice;
    private double ingredientsPrice;

    public Pizza(String size) {
        if(size.equals("Large")){
            basePrice = 5;
        }
        else if(size.equals("Medium")){
            basePrice = 4;
        }
        else{
            basePrice = 2;
        }
        ingredientsPrice = 0;
        allPizzas.add(this);
    }

    @Override
    public double getPrice() {
        return ingredientsPrice + basePrice;
    }

    public static ArrayList<Pizza> getAllPizzas() {
        return allPizzas;
    }


    public  void addToPrice(double price){
        this.ingredientsPrice += price;
    }

}
