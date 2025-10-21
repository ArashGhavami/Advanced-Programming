package Controller;

import Model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginMenu {

    public static void run(Scanner scanner) {
        String input = "";
        String regex = "";
        Matcher matcher = null;

        outerloop:
        while (true) {
            input = scanner.nextLine();

            //user register:
            regex = "\\s*register\\s+username\\s+(?<username>\\S+)\\s+password\\s+(?<password>\\S+)\\s+email\\s+(?<email>\\S+)\\s*";
            matcher = getMatcherByCommand(input, regex);
            if (matcher.matches()) {
                userRegister(matcher);
                continue outerloop;
            }

            //login as user:
            regex = "\\s*login\\s+username\\s+(?<username>\\S+)\\s+password\\s+(?<password>\\S+)\\s*";
            matcher = getMatcherByCommand(input, regex);
            if (matcher.matches()) {
                userLogin(matcher, scanner);
                continue outerloop;
            }

            //show current menu:
            regex = "show current menu\\s*";
            matcher = getMatcherByCommand(input, regex);
            if (matcher.matches()) {
                System.out.println("login menu");
                continue outerloop;
            }

            //exit:
            regex = "exit\\s*";
            matcher = getMatcherByCommand(input, regex);
            if (matcher.matches()) {
                break outerloop;
            }

            //invalid command:
            System.out.println("invalid command");
            continue outerloop;

        }
    }

    public static Matcher getMatcherByCommand(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static void userRegister(Matcher matcher) {

        String username = matcher.group("username");
        boolean isUsernameFormatOK = isUsernameFormatOK(username);
        if (isUsernameFormatOK == false) {
            System.out.println("username format is invalid");
            return;
        }
        if (User.doesThisUsernameAlreadyExists(username) == true) {
            System.out.println("username already exists");
            return;
        }
        String password = matcher.group("password");
        boolean passwordLengthCheck = passwordLengthCheck(password);
        if (!passwordLengthCheck) {
            System.out.println("password length too small or short");
            return;
        }
        if (passwordFormatCheck(password) == false) {
            System.out.println("your password must have at least one special character");
            return;
        }
        if (passwordFirstChar(password) == false) {
            System.out.println("your password must start with english letters");
            return;
        }
        String email = matcher.group("email");
        boolean emailCheck = emailCheck(email);
        if (emailCheck) {
            System.out.println("user registered successfully");
            User user = new User(username, password, email);
            User.addUser(user);
        }
    }

    public static boolean isUsernameFormatOK(String username) {
        String regex = "[a-zA-Z_]+";
        Matcher matcher = getMatcherByCommand(username, regex);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean passwordLengthCheck(String password) {
        int passLength = password.length();
        if (passLength < 6 || passLength > 18) {
            return false;
        }
        return true;
    }

    public static boolean passwordFormatCheck(String password) {
        for (int i = 0; i < password.length(); i++) {
            char indexChar = password.charAt(i);
            if (indexChar == '@' || indexChar == '#' || indexChar == '$' || indexChar == '^' || indexChar == '!'
                    || indexChar == '&') {
                return true;
            }
        }
        return false;
    }

    public static boolean passwordFirstChar(String password) {
        String regex = "[a-zA-Z].*";
        Matcher matcher = getMatcherByCommand(password, regex);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean emailCheck(String email) {
        String regex = "(?<address>\\S+)@(?<something>\\S+)\\.com";
        Matcher matcher = getMatcherByCommand(email, regex);
        if (matcher.matches()) {
            String address = matcher.group("address");
            String something = matcher.group("something");
            String regex2 = "[a-z]+";
            Matcher matcher2 = getMatcherByCommand(something, regex2);
            if(matcher2.matches()){
                int howManyDots = 0;
                for (int i = 0; i < address.length(); i++) {
                    if (address.charAt(i) == '.') {
                        howManyDots++;
                    }
                }
                if (howManyDots > 1) {
                    System.out.println("you can't use special characters");
                    return false;
                }
                for (int i = 0; i < address.length(); i++) {
                    if (!((address.charAt(i) >= 'a' && address.charAt(i) <= 'z')
                            || (address.charAt(i) >= 'A' && address.charAt(i) <= 'Z')
                            || (address.charAt(i) >= '0' && address.charAt(i) <= '9') || address.charAt(i) == '.')) {
                        System.out.println("you can't use special characters");
                        return false;
                    }
                }
                return true;
            }
            else{
                System.out.println("email format is invalid");
                return false;
            }
        }
        else {
            System.out.println("email format is invalid");
            return false;
        }
    }

    public static void userLogin(Matcher matcher, Scanner scanner) {
        String username = matcher.group("username");
        String password = matcher.group("password");

        if (User.getUserByUsername(username) == null) {
            System.out.println("username doesn't exist");
            return;
        }
        if (!User.getUserByUsername(username).getPassword().equals(password)) {
            System.out.println("password is incorrect");
            return;
        }
        System.out.println("user logged in successfully");
        User.setLoggedInUser(User.getUserByUsername(username));
        MainMenu.run(scanner);
    }

}