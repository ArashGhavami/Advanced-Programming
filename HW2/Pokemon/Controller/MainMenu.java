package Controller;

import Model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu {

    public static void run(Scanner scanner) {
        String regex = "";
        Matcher matcher;
        String input = "";

        outerloop:
        while (true) {
            input = scanner.nextLine();

            //logout:
            regex = "\\s*logout\\s*";
            matcher = getCommandMatcher(input, regex);
            if(matcher.matches()){
                break outerloop;
            }

            //go to shop menu:
            regex = "\\s*go to shop menu\\s*";
            matcher = getCommandMatcher(input, regex);
            if(matcher.matches()){
                ShopMenu.run(scanner);
                continue outerloop;
            }

            //go to profile menu:
            regex = "\\s*go to profile menu\\s*";
            matcher = getCommandMatcher(input, regex);
            if(matcher.matches()){
                ProfileMenu.run(scanner);
                continue outerloop;
            }

            //start to play:
            regex = "\\s*start new game with (?<username>\\S+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                startNewGame(matcher, scanner);
                continue outerloop;
            }

            //show current menu:
            regex = "\\s*show current menu\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                System.out.println("main menu");
                continue outerloop;
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

    public static void startNewGame(Matcher matcher, Scanner scanner){
        String username = matcher.group("username");
        if (User.getUserByUsername(username) == null) {
            System.out.println("username is incorrect");
            return;
        }
        User firstUser = User.getLoggedInUser();
        User secondUser = User.getUserByUsername(username);
        if (firstUser.getNumberOfDecks() != 12) {
            System.out.println(firstUser.getUsername() + " has no 12 cards in deck");
            return;
        }
        if (secondUser.getNumberOfDecks() != 12) {
            System.out.println(secondUser.getUsername() + " has no 12 cards in deck");
            return;
        }
        firstUser.setFirstHandTotalHitPoint();
        secondUser.setFirstHandTotalHitPoint();
        GameMenu.run(scanner, firstUser, secondUser);
    }





}

   