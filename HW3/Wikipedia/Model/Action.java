package Model;


import java.util.ArrayList;

public class Action {

    private ArrayList<ArrayList<String>> strings = new ArrayList<>();

    public Action(ArrayList<ArrayList<String>> strings) {
        this.strings = strings;
    }

    public synchronized void addToStrings(String word) {
        int hashCode = word.toLowerCase().hashCode();
        int reminder = hashCode % 26;
        if (reminder < 0) reminder = -reminder;
        strings.get(reminder).add(word);


    }

}