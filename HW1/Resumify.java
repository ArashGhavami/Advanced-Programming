import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Resumify {
    public static void main(String[] args) {
        String input;
        Scanner scanner = new Scanner(System.in);
        //register:
        outerLoop:
        while (true) {
            input = scanner.nextLine();
            input = input.replaceAll("^[\\s]*", "");
            input = input.replaceAll("[\\s]+", " ");
            String[] registerFlags = input.split(" ");
            if (registerFlags[0].equals("register")) {
                if (registerFlags.length != 5) {
                    System.out.println("wrong flag format");
                    continue outerLoop;
                } else {
                    if ((registerFlags[1].equals("-u") && registerFlags[3].equals("-p")) || (registerFlags[1].equals("-p") && registerFlags[3].equals("-u"))) {
                        //
                        String username = "";
                        String password = "";
                        for (int i = 0; i < registerFlags.length - 1; i++) {
                            if (registerFlags[i].equals("-u")) {
                                username = registerFlags[i + 1];
                            }
                            if (registerFlags[i].equals("-p")) {
                                password = registerFlags[i + 1];
                            }
                        }
                        Pattern pattern = Pattern.compile("[a-zA-Z]+([a-zA-Z0-9]+[._]?){0,}[a-zA-Z0-9]+");
                        Matcher matcher = pattern.matcher(username);
                        if (!matcher.matches()) {
                            pattern = Pattern.compile("[a-zA-Z]");
                            matcher = pattern.matcher(username);
                            if (!matcher.matches()) {
                                System.out.println("invalid username format");
                                continue outerLoop;
                            }
                        }
                        String[] notAllowedWordsInPassWithMoreThan3Chars = username.split("[^a-zA-Z]");
                        String[] notAllowedWords = new String[notAllowedWordsInPassWithMoreThan3Chars.length];
                        int counter = 0;
                        for (int i = 0; i < notAllowedWordsInPassWithMoreThan3Chars.length; i++) {
                            if (notAllowedWordsInPassWithMoreThan3Chars[i].length() > 3) {
                                notAllowedWords[counter] = notAllowedWordsInPassWithMoreThan3Chars[i];
                                counter++;
                            }
                        }
                        for (int i = 0; i < notAllowedWords.length; i++) {
                            if (notAllowedWords[i] != null) {
                                int index = password.indexOf(notAllowedWords[i]);
                                if (index == -1) {
                                    continue;
                                } else {
                                    System.out.println("weak password");
                                    continue outerLoop;
                                }
                            }
                        }
                        if (password.length() > 20 || password.length() < 6) {
                            System.out.println("weak password");
                            continue outerLoop;
                        }
                        for (int i = 0; i < password.length() - 3; i++) {
                            if (password.charAt(i) == password.charAt(i + 1)
                                    && password.charAt(i + 1) == password.charAt(i + 2)
                                    && password.charAt(i + 2) == password.charAt(i + 3)) {
                                System.out.println("weak password");
                                continue outerLoop;
                            }
                        }
                        boolean capital = false;
                        boolean small = false;
                        boolean special = false;
                        boolean number = false;
                        for (int i = 0; i < password.length(); i++) {
                            if (password.charAt(i) <= 'Z' && password.charAt(i) >= 'A') {
                                capital = true;
                            }
                            if (password.charAt(i) <= 'z' && password.charAt(i) >= 'a') {
                                small = true;
                            }
                            if (password.charAt(i) <= '9' && password.charAt(i) >= '0') {
                                number = true;
                            }
                            if (password.charAt(i) == '!' || password.charAt(i) == '@' || password.charAt(i) == '#'
                                    || password.charAt(i) == '$' || password.charAt(i) == '%'
                                    || password.charAt(i) == '^' || password.charAt(i) == '&'
                                    || password.charAt(i) == '*' || password.charAt(i) == '+'
                                    || password.charAt(i) == '-' || password.charAt(i) == '='
                                    || password.charAt(i) == ')' || password.charAt(i) == '(') {
                                special = true;
                            }
                        }
                        if (!(capital && small && special && number)) {
                            System.out.println("weak password");
                            continue outerLoop;
                        }
                        //

                    } else {
                        System.out.println("wrong flag format");
                        continue outerLoop;
                    }
                    System.out.println("register successful");
                    break outerLoop;
                }
            } else {
                System.out.println("invalid command");
                continue outerLoop;
            }
        }
        //data:
        String fn = "";
        String ln = "";
        String e = "";
        String ph = "";
        outerLoop1:
        while (true) {
            input = scanner.nextLine();
            input = input.replaceAll("^[\\s]*", "");
            input = input.replaceAll("[\\s]+", " ");
            String[] data = input.split(" ");
            if (data[0].equals("data")) {
                if (data.length == 9) {
                    for (int i = 1; i < 8; i = i + 2) {
                        for (int j = i + 2; j < 8; j = j + 2) {
                            if (data[i].equals(data[j])) {
                                System.out.println("wrong flag format");
                                continue outerLoop1;
                            }
                        }
                    }
                    for (int i = 1; i < 8; i = i + 2) {
                        if (data[i].equals("-fn") || data[i].equals("-ln") || data[i].equals("-ph")
                                || data[i].equals("-e")) {
                            continue;
                        } else {
                            System.out.println("wrong flag format");
                            continue outerLoop1;
                        }
                    }
                    String firstName = "";
                    for (int i = 0; i < 9; i++) {
                        if (data[i].equals("-fn")) {
                            firstName = data[i + 1];
                            break;
                        }
                    }
                    fn = firstName;
                    Pattern pattern = Pattern.compile("[a-zA-Z]+");
                    Matcher matcher = pattern.matcher(firstName);
                    if (!matcher.matches()) {
                        System.out.println("wrong name format");
                        continue outerLoop1;
                    }

                    String lastName = "";
                    for (int i = 0; i < 9; i++) {
                        if (data[i].equals("-ln")) {
                            lastName = data[i + 1];
                        }
                    }
                    ln = lastName;
                    pattern = Pattern.compile("[a-zA-Z]+-?[a-zA-Z]+");
                    matcher = pattern.matcher(lastName);
                    if (!matcher.matches()) {
                        pattern = Pattern.compile("[a-zA-Z]");
                        matcher = pattern.matcher(lastName);
                        if (!matcher.matches()) {
                            System.out.println("wrong name format");
                            continue outerLoop1;
                        }
                    }
                    String email = "";
                    for (int i = 0; i < 9; i++) {
                        if (data[i].equals("-e")) {
                            email = data[i + 1];
                        }
                    }
                    e = email;
                    pattern = Pattern.compile("([a-zA-Z0-9]+[._]){0,}[a-zA-Z0-9]+@([a-zA-Z]+\\.)?[a-zA-Z]+[.]com");
                    matcher = pattern.matcher(email);
                    if (!matcher.matches()) {
                        pattern = Pattern.compile("[a-zA-Z]@[a-zA-Z]+[.]com");
                        matcher = pattern.matcher(email);
                        if (!matcher.matches()) {
                            pattern = Pattern.compile("([a-zA-Z]\\.){0,}[a-zA-Z0-9]@[a-zA-Z]+.?[a-zA-Z]+[.]com");
                            matcher = pattern.matcher(email);
                            if (!matcher.matches()) {
                                System.out.println("invalid email format");
                                continue outerLoop1;
                            }
                        }
                    }
                    if (email.charAt(0) >= '0' && email.charAt(0) <= '9') {
                        System.out.println("invalid email format");
                        continue outerLoop1;
                    }
                    String phoneNumber = "";
                    for (int i = 0; i < 9; i++) {
                        if (data[i].equals("-ph")) {
                            phoneNumber = data[i + 1];
                        }
                    }
                    ph = phoneNumber;
                    pattern = Pattern.compile("\\+989[0-9]{9}|09[0-9]{9}");
                    matcher = pattern.matcher(phoneNumber);
                    if (!matcher.matches()) {
                        System.out.println("invalid phone number");
                        continue outerLoop1;
                    }

                    System.out.println("data saved successfully");
                    break outerLoop1;
                } else {
                    System.out.println("wrong flag format");
                    continue outerLoop1;
                }
            } else {
                System.out.println("invalid command");
                continue outerLoop1;
            }
        }
        //bio:
        outerLoop2:
        while (true) {
            input = scanner.nextLine();
            Pattern pattern = Pattern.compile("bio\\s+\\\"(.*)\\\"\\s*");
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String description = matcher.group(1);
                System.out.println("----------");
                //fn and ln and email toupper and tolower:\
                fn = Character.toUpperCase(fn.charAt(0)) + fn.substring(1).toLowerCase();
                int indexOfHyphenInLastName = 0;
                boolean DoesItHaveHyphen = false;
                for (int i = 0; i < ln.length(); i++) {
                    if (ln.charAt(i) == '-') {
                        DoesItHaveHyphen = true;
                        indexOfHyphenInLastName = i;
                    }
                }
                String a = ln;
                if (DoesItHaveHyphen == true) {
                    ln = Character.toUpperCase(a.charAt(0)) + a.substring(1, indexOfHyphenInLastName).toLowerCase()
                            + "-"
                            + Character.toUpperCase(a.charAt(indexOfHyphenInLastName + 1))
                            + a.substring(indexOfHyphenInLastName + 2).toLowerCase();
                } else {
                    ln = Character.toUpperCase(ln.charAt(0)) + ln.substring(1).toLowerCase();
                }
                e = e.toLowerCase();
                //
                System.out.println(fn + " " + ln);
                System.out.println("Email: " + e);
                //
                pattern = Pattern.compile("\\+989[0-9]{9}");
                matcher = pattern.matcher(ph);
                if (matcher.matches()) {
                    ph = "+98-" + ph.substring(3, 6) + "-" + ph.substring(6, 9) + "-" + ph.substring(9);
                } else {
                    ph = "+98-" + ph.substring(1, 4) + "-" + ph.substring(4, 7) + "-" + ph.substring(7);
                }
                System.out.println("Phone Number: " + ph);
                System.out.println();
                //
                if (description.length() == 0) {
                    System.out.println("Biography:");
                    System.out.println();
                    System.out.println("----------");
                    break outerLoop2;
                }
                int firstLetterIndex = 0;
                for (int i = 0; i < description.length(); i++) {
                    if (description.charAt(i) == ' ') {
                        continue;
                    }
                    if (description.charAt(i) == '\\') {
                        if (description.charAt(i + 1) == 'n') {
                            i++;
                            continue;
                        }
                    } else {
                        firstLetterIndex = i;
                        break;
                    }
                }
                int lastLetterIndex = 0;
                for (int i = description.length() - 1; i >= 0; i--) {
                    if (description.charAt(i) == ' ') {
                        continue;
                    }
                    if (description.charAt(i) == 'n') {
                        if (description.charAt(i - 1) == '\\') {
                            i--;
                            continue;
                        }
                    } else {
                        lastLetterIndex = i;
                        break;
                    }
                }
                description = description.substring(firstLetterIndex, lastLetterIndex + 1);
                pattern = Pattern.compile("[\\s]{2,}");
                matcher = pattern.matcher(description);
                String newDescription = "";
                int lastLetter = 0;
                while (matcher.find()) {
                    newDescription += description.substring(lastLetter, matcher.start());
                    newDescription += " ";
                    lastLetter = matcher.end();
                }
                newDescription += description.substring(lastLetter);
                pattern = Pattern.compile("(\\\\n" + //
                        "){3,}");
                matcher = pattern.matcher(newDescription);
                lastLetter = 0;
                String secondNewString = "";
                while (matcher.find()) {
                    secondNewString += newDescription.substring(lastLetter, matcher.start());
                    secondNewString += "\\n\\n";
                    lastLetter = matcher.end();
                }
                secondNewString = secondNewString + newDescription.substring(lastLetter);
                secondNewString = secondNewString.replace("\\n", "\n");
                String[] backSlashN = secondNewString.split("\n");
                System.out.println("Biography:");
                int howManyLetters = 0;
                for (int j = 0; j < backSlashN.length; j++) {
                    if (!backSlashN[j].equals("\n")) {
                        String[] spaces = backSlashN[j].split(" ");
                        for (int i = 0; i < spaces.length; i++) {
                            howManyLetters += spaces[i].length();
                            if (howManyLetters <= 40) {
                                System.out.print(spaces[i]);
                            } else if (howManyLetters > 40) {
                                System.out.println();
                                howManyLetters = spaces[i].length();
                                System.out.print(spaces[i]);
                            }
                            howManyLetters++;
                            if (howManyLetters > 40) {
                                System.out.println();
                                howManyLetters = 1;
                                System.out.print(" ");
                            } else if (howManyLetters == 40) {
                                System.out.println(" ");
                                howManyLetters = 0;
                            } else if (howManyLetters < 40) {
                                System.out.print(" ");
                            }
                        }
                        System.out.println();
                        howManyLetters = 0;
                    } else {
                        System.out.println();
                    }
                }
                System.out.println();
                System.out.println("----------");
                break;
            } else {
                System.out.println("invalid command");
                continue outerLoop2;
            }
        }
    }
}
