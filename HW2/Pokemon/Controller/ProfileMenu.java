package Controller;

import Model.Card;
import Model.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProfileMenu {

    public static void run(Scanner scanner) {
        String regex = "";
        Matcher matcher;
        String input = "";

        outerloop:
        while (true) {
            input = scanner.nextLine();

            //show coins:
            regex = "\\s*show coins\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showCoins();
                continue outerloop;
            }

            //show experience:
            regex = "\\s*show experience\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showExperience();
                continue outerloop;
            }

            //show storage:
            regex = "\\s*show storage\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showStorage();
                continue outerloop;
            }

            //add card to deck:
            regex = "\\s*equip card (?<cardname>.*) to my deck\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                addCardToDeck(matcher);
                continue outerloop;
            }

            //show my deck:
            regex = "\\s*show my deck\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showDeck();
                continue outerloop;
            }

            //remove card from deck:
            regex = "\\s*unequip card (?<cardname>\\S*) from my deck\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                removeCardFromDeck(matcher);
                continue outerloop;
            }

            //show rank:
            regex = "\\s*show my rank\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showRank();
                continue outerloop;
            }

            //show ranking:
            regex = "\\s*show ranking\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showRanking();
                continue outerloop;
            }


            //show current menu:
            regex = "\\s*show current menu\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                System.out.println("profile menu");
                continue outerloop;
            }


            //back:
            regex = "\\s*back\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                break outerloop;
            }

            //invalid command:
            System.out.println("invalid command");
            continue outerloop;

        }
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static void showCoins() {
        User user = User.getLoggedInUser();
        System.out.print("number of coins:");
        System.out.println((int)user.getNumberOfCoins());
    }

    public static void showExperience() {
        User user = User.getLoggedInUser();
        System.out.print("experience:");
        System.out.println((int)user.getExperience());
    }

    public static void showStorage() {
        ArrayList<Card> cards = User.getLoggedInUser().getAllCards();
        int index = 1;
        for (Card card : cards) {
            System.out.println(index + "." + card.getType() + " " + card.getName() + " " + (int)card.getValue());
            index++;
        }
    }

    public static void addCardToDeck(Matcher matcher) {
        User user = User.getLoggedInUser();
        String cardname = matcher.group("cardname");
        if (!Card.isThisNameValid(cardname)) {
            System.out.println("card name is invalid");
            return;
        }
        if (!user.doIHaveThisCard(cardname)) {
            System.out.println("you don't have this type of card");
            return;
        }
        if (user.getNumberOfDecks() == 12) {
            System.out.println("your deck is already full");
            return;
        }
        if (!Card.getCardTypeByName(cardname).equals("energy")) {
            if (User.getLoggedInUser().doIHaveThisNameInMyDeck(cardname)) {
                System.out.println("you have already added this type of pokemon to your deck");
                return;
            }
        }
        System.out.println("card " + cardname + " equipped to your deck successfully");
        User.getLoggedInUser().addCardToDeck(cardname);

    }

    public static void removeCardFromDeck(Matcher matcher) {
        String cardname = matcher.group("cardname");
        if (!Card.isThisNameValid(cardname)) {
            System.out.println("card name is invalid");
            return;
        }
        if (!User.getLoggedInUser().doIHaveThisNameInMyDeck(cardname)) {
            System.out.println("you don't have this type of card in your deck");
            return;
        }
        System.out.println("card " + cardname + " unequipped from your deck successfully");
        User.getLoggedInUser().removeCardFromDeck(cardname);
    }

    public static void showDeck(){
        ArrayList<Card> cards = User.getLoggedInUser().getDeck();
        int index = 1;
        for (Card card : cards) {
            System.out.println(index + "." + card.getName());
            index++;
        }
    }

    public static void showRank() {
        ArrayList<User> users = User.getUsers();
        User[] orderedUsers = new User[users.size()];
        for (int i = 0; i < users.size(); i++) {
            orderedUsers[i] = users.get(i);
        }

        for (int i = 0; i < users.size(); i++) {
            for (int j = i; j < users.size(); j++) {
                if (orderedUsers[j].getExperience() > orderedUsers[i].getExperience()) {
                    User temp = orderedUsers[i];
                    orderedUsers[i] = orderedUsers[j];
                    orderedUsers[j] = temp;
                } else if (orderedUsers[i].getExperience() == orderedUsers[j].getExperience()) {
                    String iName = orderedUsers[i].getUsername();
                    String jName = orderedUsers[j].getUsername();
                    int compareResult = iName.compareTo(jName);
                    if (compareResult > 0) {
                        User temp = orderedUsers[i];
                        orderedUsers[i] = orderedUsers[j];
                        orderedUsers[j] = temp;
                    }
                }
            }
        }
        User user = User.getLoggedInUser();
        for (int i = 0; i < orderedUsers.length; i++) {
            if (user.getUsername().equals(orderedUsers[i].getUsername())) {
                System.out.println("your rank:" + (i + 1));
                break;
            }
        }
    }

    public static void showRanking() {
        ArrayList<User> users = User.getUsers();
        User[] orderedUsers = new User[users.size()];
        for (int i = 0; i < users.size(); i++) {
            orderedUsers[i] = users.get(i);
        }

        for (int i = 0; i < users.size(); i++) {
            for (int j = i; j < users.size(); j++) {
                if (orderedUsers[j].getExperience() > orderedUsers[i].getExperience()) {
                    User temp = orderedUsers[i];
                    orderedUsers[i] = orderedUsers[j];
                    orderedUsers[j] = temp;
                } else if (orderedUsers[i].getExperience() == orderedUsers[j].getExperience()) {
                    String iName = orderedUsers[i].getUsername();
                    String jName = orderedUsers[j].getUsername();
                    int compareResult = iName.compareTo(jName);
                    if (compareResult > 0) {
                        User temp = orderedUsers[i];
                        orderedUsers[i] = orderedUsers[j];
                        orderedUsers[j] = temp;
                    }
                }
            }
        }
        int index = 1;
        for (User user : orderedUsers) {
            System.out.println(index + ".username:" + user.getUsername() + " experience:" + user.getExperience());
            index++;
        }
    }

    public  static void ssnt(){
        ArrayList<Card> cards = User.getLoggedInUser().getCards();
        int index = 1;
        for (Card card : cards) {
            System.out.println(index + "." + card.getType() + " " + card.getName() + " " + (int)card.getValue());
            index++;
        }
    }


}