package Controller;

import Model.Dir;
import Model.User;
import View.InputOutput;

import java.util.regex.*;

public class LoignMenu {
    public static void run(String input,String regex){
        Matcher matcher = null;

        //register one time:
        regex = "\\s*create_user\\s+-username\\s+(?<username>.*\\S)\\s+-password\\s+(?<password>.*\\S)\\s+-one_time\\s*";
        matcher = getCommandMatcher(input, regex);
        if(matcher.matches()){
            registerOnetime(matcher);
            return;
        }

        //register normal:
        regex = "\\s*create_user\\s+-username\\s+(?<username>.*\\S)\\s+-password\\s+(?<password>.*\\S)\\s*";
        matcher = getCommandMatcher(input, regex);
        if(matcher.matches()){
            registerNormal(matcher);
            return;
        }


        //login
        regex = "\\s*login\\s+-username\\s+(?<username>.*\\S)\\s+-password\\s+(?<password>.*\\S)\\s*";
        matcher = getCommandMatcher(input, regex);
        if(matcher.matches()){
            login(matcher);
            return;
        }

        //logout:
        regex = "\\s*logout\\s*";
        matcher = getCommandMatcher(input, regex);
        if(matcher.matches()){
            System.out.println("There's no account to be logged out");
            return;
        }


        //invalid:
        System.out.println("invalid command");

    }

    public static Matcher getCommandMatcher(String input, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static boolean usernameCheck(String username){
        String regex = "[a-zA-Z0-9_]+";
        Matcher matcher = getCommandMatcher(username,regex);
        if(matcher.matches()){
            return  true;
        }
        return false;
    }

    public static boolean passwordCheck(String password){
        boolean upper = false;
        boolean lower = false;
        boolean num = false;
        if(password == null){
            return false;
        }
        if(password.length() <8){
            return false;
        }
        for(int i=0; i<password.length(); i++){
            char u = password.charAt(i);
            if(u >= 'a' && u <= 'z'){
                lower = true;
            }
            if(u >= '0' && u <= '9'){
                num = true;
            }
            if(u >= 'A' && u <= 'Z'){
                upper = true;
            }
        }
        if(upper && num && lower){
            return true;
        }
        return false;
    }

    public static void registerOnetime(Matcher matcher){
        String username = matcher.group("username");
        String password = matcher.group("password");
        if(User.getUserByUsername(username) != null){
            System.out.println("user already exists");
            return;
        }
        if(!usernameCheck(username)){
            System.out.println("invalid username format");
            return;
        }
        if (!passwordCheck(password)){
            System.out.println("invalid password");
            return;
        }
        System.out.println("register successful");
        User user = new User(username, password, true);
        User.addUser(user);
    }

    public static void registerNormal(Matcher matcher){
        String username = matcher.group("username");
        String password = matcher.group("password");
        if(User.getUserByUsername(username) != null){
            System.out.println("user already exists");
            return;
        }
        if(!usernameCheck(username)){
            System.out.println("invalid username format");
            return;
        }
        if (!passwordCheck(password)){
            System.out.println("invalid password");
            return;
        }
        System.out.println("register successful");
        User user = new User(username, password, false);
        User.addUser(user);
    }

    public static void login(Matcher matcher){
        String username = matcher.group("username");
        String password = matcher.group("password");

        if(User.getUserByUsername(username) == null){
            System.out.println("user doesn't exist");
            return;
        }

        User user = User.getUserByUsername(username);
        if(!user.getPassword().equals(password)){
            System.out.println("password doesn't match");
            return;
        }

        System.out.println("login successful");
        User.setCurrentUser(user);
        InputOutput.setCurrrentClass("user menu");
        if(user.getRoot() == null) {
            Dir dir = new Dir("root", null);
            Dir.setCurrentDir(dir);
            User.getCurrentUser().addToMyDirs(dir);
        }
        else{
            Dir.setCurrentDir(user.getRoot());
        }
    }






}
