package Model;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.HashMap;


public class Card {
    private String name;
    double buyValue;
    String type;
    double sellValue;
    private double hitpoint;
    private double power;
    private double resistence;

    private double energy1;
    private double energy2;

    private String energy1Name;
    private String energy2Name;

    {
        energy1 = 1;
        energy2 = 1;
        energy1Name = null;
        energy2Name = null;
    }

    private double maxHitpoint;
    private double shieldHitpoint;

    {
        shieldHitpoint = 0;
    }

    private boolean isItSlept;
    private boolean isItFired;
    private boolean isItInDeck;

    public Card(double sellValue, double buyValue, String name, String type) {
        this.buyValue = buyValue;
        this.sellValue = sellValue;
        this.name = name;
        this.type = type;
        this.hitpoint = getMaxHitpointByName(name);
        this.maxHitpoint = this.hitpoint;
        this.power = getMaxPowerByName(name);
        this.resistence = getResistenceByName(name);
        this.energy1 = 1;
        this.energy2 = 1;
        this.isItSlept = false;
        this.isItFired = false;
        this.shieldHitpoint = 0;
        if (getCardTypeByName(name) != null) {

            if (getCardTypeByName(name).equals("plant")) {
                this.shieldHitpoint = 15;
            }
        }
        this.isItInDeck = false;
    }

    public double getValue() {
        return buyValue;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getMaxHitpoint() {
        return maxHitpoint;
    }

    public static boolean isThisNameValid(String name) {
        ArrayList<String> validNames = new ArrayList<>();
        validNames.add("dragonite");
        validNames.add("tepig");
        validNames.add("lugia");
        validNames.add("ducklett");
        validNames.add("pineco");
        validNames.add("rowlet");
        validNames.add("pink");
        validNames.add("yellow");
        for (String string : validNames) {
            if (string.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static String getCardTypeByName(String name) {
        if (!isThisNameValid(name)) {
            return null;
        }
        if (name.equals("dragonite") || name.equals("tepig")) {
            return "fire";
        }
        if (name.equals("lugia") || name.equals("ducklett")) {
            return "water";
        }
        if (name.equals("pineco") || name.equals("rowlet")) {
            return "plant";
        }
        return "energy";

    }

    public Double getSellValue() {
        return sellValue;
    }

    public static double getCardBuyValue(String cardname) {
        HashMap<String, Double> values = new HashMap<>();
        values.put("dragonite", 10.0);
        values.put("tepig", 13.0);
        values.put("lugia", 11.0);
        values.put("ducklett", 15.0);
        values.put("pineco", 9.0);
        values.put("rowlet", 12.0);
        values.put("pink", 5.0);
        values.put("yellow", 5.0);

        return values.get(cardname);
    }

    public static double getCardSellValue(String cardname) {
        HashMap<String, Double> values = new HashMap<>();
        values.put("dragonite", 8.0);
        values.put("tepig", 10.0);
        values.put("lugia", 9.0);
        values.put("ducklett", 11.0);
        values.put("pineco", 7.0);
        values.put("rowlet", 9.0);
        values.put("pink", 3.0);
        values.put("yellow", 3.0);

        return values.get(cardname);
    }

    public void setHitpoint(double hitpoint) {
        this.hitpoint = hitpoint;
    }

    public double getHitpoint() {
        return hitpoint;
    }

    public double getPower() {
        return power;
    }

    public static Integer getMaxHitpointByName(String name) {
        HashMap<String, Integer> values = new HashMap<>();
        values.put("dragonite", 120);
        values.put("tepig", 140);
        values.put("lugia", 90);
        values.put("ducklett", 70);
        values.put("pineco", 110);
        values.put("rowlet", 180);
        values.put("yellow", 0);
        values.put("pink", 0);

        return values.get(name);
    }

    public static Integer getMaxPowerByName(String name) {
        HashMap<String, Integer> values = new HashMap<>();
        values.put("dragonite", 40);
        values.put("tepig", 25);
        values.put("lugia", 20);
        values.put("ducklett", 20);
        values.put("pineco", 25);
        values.put("rowlet", 40);
        values.put("yellow", 0);
        values.put("pink", 0);

        return values.get(name);
    }

    public static double getWeakness(String defenderName, String attackerType) {
        String fire = "fire";
        String plant = "plant";
        String water = "water";
        if (attackerType.equals(fire)) {
            if (defenderName.equals("lugia")) {
                return 1.3;
            }
            if (defenderName.equals("rowlet")) {
                return 1.3;
            }
            return 1;
        }
        if (attackerType.equals(plant)) {
            if (defenderName.equals("tepig")) {
                return 1.3;
            }
            if (defenderName.equals("ducklett")) {
                return 1.5;
            }
            return 1;
        }
        if (attackerType.equals(water)) {
            if (defenderName.equals("dragonite")) {
                return 1.2;
            }
            if (defenderName.equals("tepig")) {
                return 2;
            }
            return 1;
        }
        return 1;
    }

    public static Double getResistenceByName(String name) {
        HashMap<String, Double> values = new HashMap<>();
        values.put("dragonite", 0.7);
        values.put("tepig", 0.8);
        values.put("lugia", 0.7);
        values.put("ducklett", 0.6);
        values.put("pineco", 0.9);
        values.put("rowlet", 0.5);
        values.put("yellow", 0.0);
        values.put("pink", 0.0);

        return values.get(name);
    }

    public static double getEnergyByEnergyCardAndMainCard(String energy, String cardType) {
        if (energy.equals("pink")) {
            if (cardType.equals("fire")) return 1;
            if (cardType.equals("water")) return 1.05;
            if (cardType.equals("plant")) return 1.15;
        }
        if (energy.equals("yellow")) {
            if (cardType.equals("fire")) return 1;
            if (cardType.equals("water")) return 1.2;
            if (cardType.equals("plant")) return 1.2;
        }
        return 0;
    }

    public double getDamageOrHeal(double weakness, double resistence) {
        return (double) resistence * (double) this.power * (double) this.energy1 * (double) this.energy2 * (double) weakness;
    }

    public void setEnergy1(double energy1) {
        this.energy1 = energy1;
    }

    public void setEnergy2(double energy2) {
        this.energy2 = energy2;
    }

    public void setEnergy1Name(String energy1Name) {
        this.energy1Name = energy1Name;
    }

    public void setEnergy2Name(String energy2Name) {
        this.energy2Name = energy2Name;
    }

    public double getEnergy1() {
        return energy1;
    }

    public double getEnergy2() {
        return energy2;
    }

    public String getEnergy1Name() {
        return energy1Name;
    }

    public String getEnergy2Name() {
        return energy2Name;
    }

    public boolean isItSlept() {
        return isItSlept;
    }

    public boolean isItFired() {
        return isItFired;
    }

    public static void printCardEnergiesWithActive(Card active) {
        if (active != null) {
            System.out.print(active.getName());
            System.out.print("|");
            if (active.getEnergy1Name() != null) {
                System.out.print(active.getEnergy1Name());
            }
            System.out.print("|");
            if (active.getEnergy2Name() != null) {
                System.out.print(active.getEnergy2Name());
            }
            System.out.println();
        } else {
            System.out.println();
        }
    }

    public static void printCardData(Card card) {
        System.out.println("pokemon: " + card.getName());
        System.out.print("special condition: ");
        if (card.isItSlept() && card.isItFired()) {
            System.out.println("burning/sleep");
        } else if (card.isItFired()) {
            System.out.println("burning");
        } else if (card.isItSlept()) {
            System.out.println("sleep");
        } else {
            System.out.println();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("hitpoint: " + df.format(card.getHitpoint()) + "/" + df.format(Card.getMaxHitpointByName(card.getName())));
        System.out.print("energy 1: ");
        if (card.getEnergy1Name() != null) {
            System.out.println(card.getEnergy1Name());
        } else {
            System.out.println();
        }
        System.out.print("energy 2: ");
        if (card.getEnergy2Name() != null) {
            System.out.println(card.getEnergy2Name());
        } else {
            System.out.println();
        }


    }

    public void setItSlept(boolean itSlept) {
        isItSlept = itSlept;
    }

    public void setItFired(boolean itFired) {
        isItFired = itFired;
    }

    public double getShieldHitpoint() {
        return shieldHitpoint;
    }

    public void setShieldHitpoint(double shieldHitpoint) {
        this.shieldHitpoint = shieldHitpoint;
    }

    public boolean isItInDeck() {
        return isItInDeck;
    }

    public void setItInDeck(boolean itInDeck) {
        isItInDeck = itInDeck;
    }

    public double getResistence() {
        return this.resistence;
    }
}

