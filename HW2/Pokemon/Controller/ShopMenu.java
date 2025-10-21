package Controller;

import Model.Card;
import Model.User;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopMenu {

    public static void run(Scanner scanner) {
        String regex = "";
        Matcher matcher;
        String input = "";

        outerloop:
        while (true) {
            input = scanner.nextLine();


            //buy card:
            regex = "\\s*buy card (?<cardname>\\S+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                buyCard(matcher);
                continue outerloop;
            }


            //sell card:
            regex = "\\s*sell card (?<cardname>\\S*)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                sellCard(matcher);
                continue outerloop;
            }

            //show current menu:
            regex = "\\s*show current menu\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                System.out.println("shop menu");
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

    public static void buyCard(Matcher matcher) {
        String cardname = matcher.group("cardname");
        cardname.toLowerCase();
        if (!Card.isThisNameValid(cardname)) {
            System.out.println("card name is invalid");
            return;
        }

        if (Card.getCardBuyValue(cardname) > User.getLoggedInUser().getNumberOfCoins()) {
            System.out.println("not enough coin to buy " + cardname);
            return;
        }

        System.out.println("card " + cardname + " bought successfully");
        Card card = new Card(Card.getCardSellValue(cardname), Card.getCardBuyValue(cardname), cardname,
                Card.getCardTypeByName(cardname));
        User.getLoggedInUser().addCard(card);
        User.getLoggedInUser().setCoin(User.getLoggedInUser().getNumberOfCoins() - card.getValue());
        User.getLoggedInUser().addToAllCards(card);
    }

    public static void sellCard(Matcher matcher) {
        String cardname = matcher.group("cardname");
        cardname = cardname.toLowerCase();
        if (!Card.isThisNameValid(cardname)) {
            System.out.println("card name is invalid");
            return;
        }
        if (!User.getLoggedInUser().doIHaveThisCard(cardname) && !User.getLoggedInUser().doIHaveThisNameInMyDeck(cardname)) {
            System.out.println("you don't have this type of card for sell");
            return;
        }
        System.out.println("card " + cardname + " sold successfully");
        User user = User.getLoggedInUser();
        user.setCoin(user.getNumberOfCoins() + Card.getCardSellValue(cardname));



        if (user.getFirstCardInList(cardname).isItInDeck() == false) {
            ArrayList<Card> cards = user.getCards();
            Card card = null;
            for (Card card2 : cards) {
                if (card2.getName().equals(cardname)) {
                    card = card2;
                    break;
                }
            }
            cards.remove(card);
            user.setCards(cards);
            user.removeFromAllCards(card);
        } else {
            ArrayList<Card> cards = user.getDeck();
            Card card = null;
            for (Card card1 : cards) {
                if (card1.getName().equals(cardname)) {
                    card = card1;
                    break;
                }
            }
            cards.remove(card);
            user.setDeckCards(cards);
            user.removeFromAllCards(card);

        }
    }
}

