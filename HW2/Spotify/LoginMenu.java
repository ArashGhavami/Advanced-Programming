import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu {

    public static void run(Scanner scanner) {
        outerloop:
        while (true) {
            String input = scanner.nextLine();
            String regex = "";
            Matcher matcher;
            boolean isInvalidCommand = true;
            //register as user:
            regex = "register as user -u (?<username>[a-zA-Z0-9]+) -p (?<password>[a-zA-Z0-9]+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                isInvalidCommand = false;
                userRegister(matcher);
                continue outerloop;
            }
            //register as artist:
            regex = "register as artist -u (?<username>[a-zA-Z0-9]+) -p (?<password>[a-zA-Z0-9]+) -n (?<nickname>[a-zA-Z0-9 ]+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                isInvalidCommand = false;
                artistRegister(matcher);
                continue outerloop;
            }
            //login as user:
            regex = "login as user -u (?<username>[a-zA-Z0-9]+) -p (?<password>[a-zA-Z0-9]+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                isInvalidCommand = false;
                userLogin(matcher, scanner);
                continue outerloop;
            }
            // login as artist
            regex = "login as artist -u (?<username>[a-zA-Z0-9]+) -p (?<password>[a-zA-Z0-9]+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                isInvalidCommand = false;
                artistLogin(matcher, scanner);
                continue outerloop;
            }

            //back:
            regex = "back\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                break outerloop;
            }

            //show menu name
            regex = "show menu name\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                System.out.println("login menu");
                isInvalidCommand = false;
                continue outerloop;
            }

            //exit:
            regex = "exit\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                break outerloop;
            }

            if (isInvalidCommand) {
                System.out.println("invalid command");
            }

        }
        return;
    }

    private static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    private static void artistRegister(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String nickname = matcher.group("nickname");
        boolean isPasswordWeak = isPasswordWeak(matcher);
        boolean isUsernameOK = isArtistUsernameOK(username);
        if (isUsernameOK == false) {
            System.out.println("username already exists");
            return;
        }
        if (isPasswordWeak == true) {
            System.out.println("password is not strong enough");
            return;
        }
        if (!isPasswordWeak && isUsernameOK) {
            System.out.println("artist registered successfully");
            Artist artist = new Artist(username, password, nickname);
            Artist.addArtist(artist);
        }
    }

    private static void userLogin(Matcher matcher, Scanner scanner) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        User user = User.getUserByUsername(username);
        boolean isEverythingOK = true;
        if (user == null) {
            System.out.println("username doesn't exist");
            isEverythingOK = false;
            return;
        }
        if (!user.getPassord().equals(password)) {
            System.out.println("password is wrong");
            isEverythingOK = false;
            return;
        }
        System.out.println("user logged in successfully");
        User.setLoggedInUser(user);
        Artist.setLoggedInArtist(null);
        UserMenu.run(scanner);
    }

    private static void userRegister(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        boolean isPasswordWeak = isPasswordWeak(matcher);
        boolean isUsernameOK = isUserUsernameOK(username);
        if (isUsernameOK == false) {
            System.out.println("username already exists");
            return;
        }
        if (isPasswordWeak == true) {
            System.out.println("password is not strong enough");
            return;
        }
        if (!isPasswordWeak && isUsernameOK) {
            User user = new User(username, password);
            System.out.println("user registered successfully");
            User.addUser(user);
        }
    }

    private static void artistLogin(Matcher matcher, Scanner scanner) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        Artist artist = Artist.getArtistByUsername(username);
        boolean isEverythingOK = true;
        if (artist == null) {
            isEverythingOK = false;
            System.out.println("username doesn't exist");
        }
        if (isEverythingOK) {
            if (!artist.getPassword().equals(password)) {
                System.out.println("password is wrong");
                isEverythingOK = false;
            }
        }
        if (isEverythingOK) {
            System.out.println("artist logged in successfully");
            User.setLoggedInUser(null);
            Artist.setLoggedInArtist(artist);
            ArtistMenu.run(scanner);
        }
    }

    private static boolean isPasswordWeak(Matcher matcher) {
        String password = matcher.group("password");
        boolean smallChar = false, capitalChar = false, number = false;
        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) >= '0' && password.charAt(i) <= '9') {
                number = true;
            }
            if (password.charAt(i) >= 'a' && password.charAt(i) <= 'z') {
                smallChar = true;
            }
            if (password.charAt(i) >= 'A' && password.charAt(i) <= 'Z') {
                capitalChar = true;
            }
        }
        if (smallChar == false || capitalChar == false || number == false) {
            return true;
        }
        else
            return false;
    }

    private static boolean isUserUsernameOK(String username) {
        User user = User.getUserByUsername(username);
        if (user == null)
            return true;
        else
            return false;
    }

    private static boolean isArtistUsernameOK(String username) {
        Artist artist = Artist.getArtistByUsername(username);
        if (artist == null)
            return true;
        else
            return false;
    }

}