import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        do{
            input = scanner.nextLine();
            Pattern pattern1 = Pattern.compile("\\s*exit");
            Matcher matcher1 = pattern1.matcher(input);
            if (matcher1.matches()) {
                break;
            }
            Pattern pattern = Pattern.compile(
                    "(\\s*\\\"([^\\\"]*)\\\"\\s*(\\*\\s*[0-9]+\\s*)?\\+){0,}\\s*\\\"([^\\\"]*)\\\"\\s*(\\*\\s*[0-9]+\\s*)?");
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                pattern = Pattern.compile("\\s*\\\"([^\\\"]*)\\\"\\s*(\\*\\s*([0-9]+)\\s*)?");
                matcher = pattern.matcher(input);
                StringBuilder output = new StringBuilder();
                while (matcher.find()) {
                    if (matcher.group(3) == null) {
                        output.append(matcher.group(1));
                    }
                    else {
                        int numerOfRepeat = Integer.parseInt(matcher.group(3));
                        for (int i = 0; i < numerOfRepeat; i++) {
                            output.append(matcher.group(1));
                        }
                    }
                }
                System.out.println(output);
                output.setLength(0);
            }
            else {
                System.out.println("Invalid Command.");
            }
        } while (input.equals("exit") == false);
    }
}