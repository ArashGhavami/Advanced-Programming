package Controller;

import Model.Dir;
import Model.File;
import Model.User;
import Model.Zip;
import View.InputOutput;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileManager {


    public static void run(String input, String regex) {
        Matcher matcher = null;


        //select directory:
        regex = "\\s*select\\s+-dir\\s+-N\\s+(?<number>\\S+)\\s+-names:(?<names>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            selectDirs(matcher);
            return;
        }

        //select files:
        regex = "\\s*select\\s+-file\\s+-N\\s+(?<number>\\S+)\\s+-names:(?<names>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            selectFiles(matcher);
            return;
        }

        User.getCurrentUser().setSelectedFiles(null);
        User.getCurrentUser().setSelectedDirs(null);


        //go to user menu:
        regex = "\\s*go\\s+to\\s+user\\s+menu\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            goToUserMenu();
            return;
        }

        //create dir:
        regex = "\\s*create\\s+-dir\\s+(?<dirname>.*\\S)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            createDir(matcher);
            return;
        }

        //create file:
        regex = "\\s*create\\s+-file\\s+(?<filename>.*\\S)\\s+-format\\s+(?<format>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            createFile(matcher);
            return;
        }

        //show hidden password:
        regex = "\\s*show_hidden\\s+-password\\s+(?<pass>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            showHiddenPass(matcher);
            return;
        }

        //show hidden:
        regex = "\\s*show_hidden\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            showHidden();
            return;
        }

        //jmp:
        regex = "\\s*cd\\s+-name\\s+(?<dirname>\\S+)\\s+-jmp\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            jump(matcher);
            return;
        }

        //cd:
        regex = "\\s*cd\\s+-name\\s+(?<dirname>.*\\S)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            changeDirectory(matcher);
            return;
        }

        // cd back:
        regex = "\\s*cd\\s?..\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            changeDirectoryBackWard();
            return;
        }




        //rename dir:
        regex = "\\s*rename\\s+-dir\\s+(?<oldname>\\S+)\\s+-new\\s+(?<newname>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            renameDir(matcher);
            return;
        }

        //rename file:
        regex = "\\s*rename\\s+-file\\s+(?<oldname>\\S+)\\s+-new\\s+(?<newname>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            renameFile(matcher);
            return;
        }

        //open file:
        regex = "\\s*open\\s+-name\\s+(?<filename>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            openFile(matcher);
            return;
        }


        //delete dir:
        regex = "\\s*delete\\s+-dir\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            System.out.println("no directory selected");
            return;
        }

        //delete file:
        regex = "\\s*delete\\s+-files\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            System.out.println("no file selected");
            return;
        }


        //zip:
        regex = "\\s*zip\\s+-name\\s+(?<zipname>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            System.out.println("nothing to zip");
            return;
        }


        //unzip:
        regex = "\\s*unzip\\s+-name\\s+(?<filename>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            unzip(matcher);
            return;
        }

        //close file:
        regex = "\\s*close\\s*";
        matcher = getCommandMatcher(input, regex);
        if(matcher.matches()){
            System.out.println("There's no open file");
            return;
        }



        //show:
        regex = "\\s*show\\s+current\\s+content\\s*";
        matcher = getCommandMatcher(input, regex);
        if(matcher.matches()){
            showContent();
            return;
        }

        //invalid
        System.out.println("invalid command");

    }

    public static void goToUserMenu() {
        System.out.println("welcome to user menu");
        InputOutput.setCurrrentClass("user menu");
    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static void createDir(Matcher matcher) {
        String dirname = matcher.group("dirname");
        if (Dir.isThisAddressValid(Dir.getCurrentDir().getFullNameAddress() + "/" + dirname)) {
            System.out.println("already exists");
            return;
        }
        if (!nameCheck(dirname)) {
            System.out.println("incorrect name format");
            return;
        }
        System.out.println("directory created successfully!");
        Dir dir = new Dir(dirname, Dir.getCurrentDir());
        Dir.getCurrentDir().addChildren(dir);
        User.getCurrentUser().addToMyDirs(dir);
    }

    public static boolean nameCheck(String name) {
        if(name == null){
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') {
                return false;
            }
        }
        char first = name.charAt(0);
        if (!(first == '.' || (first >= '0' && first <= '9') || (first >= 'a' && first <= 'z') || (first >= 'A' && first <= 'Z'))) {
            return false;
        }
        return true;
    }

    public static void createFile(Matcher matcher) {
        String format = matcher.group("format");
        String name = matcher.group("filename");
        String address = Dir.getCurrentDir().getFullNameAddress() + "/" + name + "." + format;

        if (File.isThisTrueAddress(address)) {
            System.out.println("already exists");
            return;
        }
        if (!formatCheck(format)) {
            System.out.println("incorrect format");
            return;
        }

        if(!nameCheck(name)){
            System.out.println("incorrect name format");
            return;
        }

        System.out.println("file created successfully!");
        File file = new File(name, format, false);
        Dir.getCurrentDir().addChildren(file);
        User.getCurrentUser().addToMyFiles(file);
    }

    public static boolean formatCheck(String format) {
        if (format.equals("java") || format.equals("c") || format.equals("exe") || format.equals("csv") || format.equals("dat") || format.equals("mp3") || format.equals("mp4") || format.equals("mkv") || format.equals("txt")) {
            return true;
        }
        return false;
    }

    public static void showHidden() {
        if (User.getCurrentUser().isItOneTime()) {
            showHiddenWithNoPassword();
        }
    }

    public static void showHiddenPass(Matcher matcher) {
        String pass = matcher.group("pass");
        if (!User.getCurrentUser().getPassword().equals(pass)) {
            System.out.println("invalid password");
            return;
        }
        showHiddenWithNoPassword();
    }

    public static void showHiddenWithNoPassword() {
        User user = User.getCurrentUser();
        ArrayList<Dir> hiddenDirs = user.getHiddenDirs();
        ArrayList<File> hiddenFiles = user.getHiddenFiles();
        ArrayList<File> hiddenZips = user.getHiddenZips();
        if (hiddenZips == null && hiddenFiles == null && hiddenDirs == null) {
            System.out.println("no hidden item");
            return;
        }
        if (hiddenDirs.size() == 0 && hiddenFiles.size() == 0 && hiddenZips.size() == 0) {
            System.out.println("no hidden item");
            return;
        }

        System.out.println("dir:");
        int index = 1;
        for (Dir dir : hiddenDirs) {
            System.out.println(index + ". " + dir.getFullNameAddress());
            index++;
        }
        System.out.println("file:");
        index = 1;
        for (File file : hiddenFiles) {
            System.out.println(index + ". " + file.getFileFullnameAddress());
            index++;
        }
        System.out.println("zip:");
        index = 1;
        for (File file : hiddenZips) {
            System.out.println(index + ". " + file.getFileFullnameAddress());
            index++;
        }


    }

    public static void changeDirectory(Matcher matcher) {
        String dirname = matcher.group("dirname");

        if (!Dir.isThisAddressValid(dirname)) {
            System.out.println("unable to open directory");
            System.out.println("no such directory");
            return;
        }
        String[] names = dirname.split("/");
        if (!Dir.doesThisDirHasTheTrueChild(Dir.getCurrentDir(), names[names.length - 1])) {
            System.out.println("unable to open directory");
            System.out.println("too deep");
            return;
        }
        Dir dir = Dir.getDirByFullAddress(dirname);
        if (dir.getName().charAt(0) == '.') {
            System.out.println("unable to open directory");
            System.out.println("hidden directory");
            return;
        }
        Dir.setCurrentDir(dir);
        System.out.println("entered directory " + dirname);
    }

    public static void changeDirectoryBackWard() {
        if (Dir.getCurrentDir().getParent() == null) {
            System.out.println("root doesn't have parent node");
            return;
        }
        System.out.println("entered directory " + Dir.getCurrentDir().getParent().getFullNameAddress());
        Dir.setCurrentDir(Dir.getCurrentDir().getParent());
    }

    public static void jump(Matcher matcher) {
        String dirname = matcher.group("dirname");
        if (!Dir.isThisAddressValid(dirname)) {
            System.out.println("unable to open directory");
            System.out.println("no such directory");
            return;
        }
        Dir dir = Dir.getDirByFullAddress(dirname);
        System.out.println("entered directory " + dirname);
        Dir.setCurrentDir(dir);
    }

    public static void renameDir(Matcher matcher) {
        String oldname = matcher.group("oldname");
        String newname = matcher.group("newname");

        if (newname.equals(oldname)) {
            System.out.println("same name!");
            return;
        }
        if (!Dir.isThisAddressValid(oldname)) {
            System.out.println("no such directory");
            return;
        }
        if (Dir.isThisAddressValid(newname)) {
            System.out.println("name is already taken");
            return;
        }

        System.out.println("renamed successfully");
        String[] names = newname.split("/");
        if(names[names.length-1].charAt(0) == '.'){
            Dir.getDirByFullAddress(oldname).setItHidden(true);
        }
        else {
            Dir.getDirByFullAddress(oldname).setItHidden(false);
        }
        Dir.getDirByFullAddress(oldname).setName(names[names.length - 1]);

    }

    public static void renameFile(Matcher matcher) {

        String oldname = matcher.group("oldname");
        String newname = matcher.group("newname");


        if (newname.equals(oldname)) {
            System.out.println("same name!");
            return;
        }

        if (!File.isThisTrueAddress(oldname)) {
            System.out.println("no such file");
            return;
        }

        if (File.isThisTrueAddress(newname)) {
            System.out.println("name is already taken");
            return;
        }


        System.out.println("renamed successfully");
        String[] names = newname.split("/");
        String[] name_format = names[names.length - 1].split("\\.");
        if(name_format[0].charAt(0) == '.'){
            File.getFileByFullAddress(oldname).setItHidden(true);
        }
        else{
            File.getFileByFullAddress(oldname).setItHidden(false);
        }
        File file = File.getFileByFullAddress(oldname);
        File.getFileByFullAddress(oldname).setFormat(name_format[1]);
        File.getFileByFullAddress(oldname).setName(name_format[0]);
        file.setFullName(names[names.length - 1]);

    }

    public static void openFile(Matcher matcher) {

        String address = matcher.group("filename");
        if (!File.isThisTrueAddress(address)) {
            System.out.println("no such file");
            return;
        }
        String[] names = address.split("/");
        File file = File.getFileByParentAndName(Dir.getCurrentDir(), names[names.length - 1]);
        if (file.isItHidden() || file.isItZipped()) {
            System.out.println("unable to open file");
            return;
        }
        System.out.println("entered file " + address);
        InputOutput.setCurrrentClass("open file");
        File.setCurrentFile(file);
    }

    public static void selectDirs(Matcher matcher) {
        String num = matcher.group("number");
        int number = Integer.parseInt(num);
        if (number > 9 || number < 1) {
            System.out.println("invalid number");
            return;
        }
        String namess = matcher.group("names");
        String[] names = namess.split(",");
        if (names.length != number) {
            System.out.println("invalid number of arguments");
            return;
        }
        ArrayList<Dir> dirs = new ArrayList<>();
        int counter = names.length;
        for (int i = 0; i < names.length; i++) {
            if (!Dir.isThisAddressValid(names[i])) {
                System.out.println("directory " + names[i] + " doesn't exist");
                counter--;
            } else {
                dirs.add(Dir.getDirByFullAddress(names[i]));
            }
        }
        if(counter == names.length) {
            System.out.println("selected successfully");
            User.getCurrentUser().setSelectedDirs(dirs);
        }
        User.getCurrentUser().setDirDays(0);
    }

    public static void selectFiles(Matcher matcher) {
        int number = Integer.parseInt(matcher.group("number"));
        if (number > 9 || number < 1) {
            System.out.println("invalid number");
            return;
        }
        String[] names = matcher.group("names").split(",");
        int counter = names.length;
        if (names.length != number) {
            System.out.println("invalid number of arguments");
            return;
        }
        ArrayList<File> files = new ArrayList<>();
        for (String name : names) {
            if (!File.isThisTrueAddress(name)) {
                System.out.println("file " + name + " doesn't exist");
                counter --;
            } else {
                files.add(File.getFileByFullAddress(name));
            }
        }
        if(counter == names.length) {
            System.out.println("selected successfully");
            User.getCurrentUser().setSelectedFiles(files);
        }
        User.getCurrentUser().setFileDays(0);
    }

    public static void unzip(Matcher matcher) {
        String address = matcher.group("filename");
        if (!File.isThisTrueAddress(address)) {
            System.out.println("no such file or directory");
            return;
        }

        System.out.println("unzipped successfully");
        String[] names = address.split("/");
        String parentAddress = "";
        for(int i=0; i<names.length-2; i++){
            parentAddress += names[i] + "/";
        }
        parentAddress += names[names.length -2];
        Dir parent = Dir.getDirByFullAddress(parentAddress);
        Zip zip = Zip.getZipByAddress(address);
        for(File file : zip.getFiles()){
            file.setParent(parent);
            parent.addChildren(file);
        }
        if(zip.getDirs()!= null) {
            for (Dir dir : zip.getDirs()) {
                dir.setParent(parent);
                parent.addChildren(dir);
            }
        }
        zip.getParent().deleteChildFile(zip);
        zip.setParent(null);
    }

    public static void showContent() {

        ArrayList<Dir> dirs = Dir.getCurrentDir().getChildrenDir();
        ArrayList<File> allFiles = Dir.getCurrentDir().getChildrenFile();
        ArrayList<File> files = new ArrayList<>();
        ArrayList<File> zip = new ArrayList<>();
        for (File file : allFiles) {
            if (file.isItZipped()) {
                zip.add(file);
            } else {
                files.add(file);
            }
        }
        if (dirs == null && zip == null && files == null) {
            System.out.println("directory empty");
            return;
        }

        if(dirs.size() == 0 && zip.size() == 0 && files.size() == 0){
            System.out.println("directory empty");
            return;
        }

            System.out.println("dir:");
            int index = 1;
        if (dirs != null) {
            for (Dir dir : dirs) {
                System.out.println(index + ". " + dir.getFullNameAddress());
                index++;
            }
        }

            System.out.println("file:");
             index = 1;
        if (files != null) {
            for (File file : files) {
                System.out.println(index + ". " + file.getFileFullnameAddress());
                index++;
            }
        }

            System.out.println("zip:");
             index = 1;
        if (zip != null) {
            for (File file : zip) {
                System.out.println(index + ". " + file.getFileFullnameAddress());
                index++;
            }
        }
    }




}