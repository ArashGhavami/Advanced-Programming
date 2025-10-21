package Model;

import java.util.ArrayList;

public class Friend extends Thread {

    ArrayList<String> wordsToBeChecked = new ArrayList<>();

    private Action action;

    public Friend (ArrayList<String> wordsToBeChecked, Action action){
        this.wordsToBeChecked = wordsToBeChecked;
        this.action =action;
    }

    @Override
    public void run() {
        for(String string : wordsToBeChecked){
            action.addToStrings(string);
        }
    }
}