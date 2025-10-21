package View;

import Controller.*;
import Model.File;
import Model.User;

import java.util.Currency;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputOutput {
    private static String currentClass = "login menu";

    //login menu
    //user menu
    //manage files
    //open file
    //Selected

    public static void run(Scanner scanner){
        String input = null;
        String regex = null;
        Matcher matcher = null;
        while(true){
            input = scanner.nextLine();

            howManyDaysPassed();


            regex = "\\s*exit\\s*";
            matcher = getCommandMatcher(input, regex);
            if(matcher.matches()){
                break;
            }

            regex = "\\s*select\\s+-dir\\s+-N\\s+(?<number>\\S+)\\s+-names:(?<names>\\S+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if(matcher.matches()){
                FileManager.run(input, regex);
                continue;
            }

            regex = "\\s*select\\s+-file\\s+-N\\s+(?<number>\\S+)\\s+-names:(?<names>\\S+)\\s*";
            matcher = getCommandMatcher(input,regex);
            if(matcher.matches()){
                FileManager.run(input, regex);
                continue;
            }


            regex = "\\s*cut\\s+-dir\\s+(?<dirname>\\S+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if(matcher.matches()){
                Selected.run(input, regex);
                continue;
            }

            regex = "\\s*zip\\s+-name\\s+(?<zipname>\\S+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if(matcher.matches()){
                Selected.run(input,regex);
                continue;
            }



            regex = "\\s*copy\\s+-dir\\s+(?<dirname>\\S+)\\s*";
            matcher = getCommandMatcher(input,regex);
            if(matcher.matches()){
                Selected.run(input, regex);
                continue;
            }

            //delete dir:
            regex = "\\s*delete\\s+-dir\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                Selected.run(input, regex);
                continue;
            }

            //delete file:
            regex = "\\s*delete\\s+-files\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                Selected.run(input, regex);
                continue;
            }




            if(currentClass.equals("login menu")){
                LoignMenu.run(input, regex);
                continue;
            }
            if(currentClass.equals("user menu")){
                UserMenu.run(input,regex);
                continue;
            }

            if(currentClass.equals("manage files")){
                FileManager.run(input, regex);
                continue;
            }

            if(currentClass.equals("Selected")){
                Selected.run(input, regex);
                continue;
            }

            if(currentClass.equals("open file")){
                OpenFile.run(input, regex);
                continue;
            }
        }
    }

    public static void setCurrrentClass(String className){
        currentClass = className;
    }

    public static Matcher getCommandMatcher(String input, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher= pattern.matcher(input);
        return matcher;
    }

    public static void howManyDaysPassed() {
        if (User.getCurrentUser() != null) {
            User user = User.getCurrentUser();
            if (user.getSelectedFiles() != null) {
                user.setFileDays(user.getFileDays() + 1);
            }
            if(user.getFileDays() == 3){
                user.setFileDays(0);
                user.setSelectedFiles(null);
            }

            if(user.getSelectedDirs()!= null){
                user.setDirDays(user.getDirDays() + 1);
            }
            if(user.getDirDays() == 3){
                user.setDirDays(0);
                user.setSelectedDirs(null);
            }
        }
    }

}
