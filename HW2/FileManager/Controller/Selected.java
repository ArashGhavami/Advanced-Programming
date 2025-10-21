package Controller;

import Model.Dir;
import Model.File;
import Model.User;
import Model.Zip;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Selected {

    public static void run(String input, String regex) {
        Matcher matcher = null;


        //delete dir:
        regex = "\\s*delete\\s+-dir\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            deleteDir();
            return;
        }

        //delete file:
        regex = "\\s*delete\\s+-files\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            deleteFile();
            return;
        }

        //zip:
        regex = "\\s*zip\\s+-name\\s+(?<zipname>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            zip(matcher);
            return;
        }

        //cut:
        regex = "\\s*cut\\s+-dir\\s+(?<dirname>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            cut(matcher);
            return;
        }


        //copy:
        regex = "\\s*copy\\s+-dir\\s+(?<dirname>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            copy(matcher);
            return;
        }


        //invalid:
        System.out.println("invalid command");


    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static void deleteDir() {
        if (User.getCurrentUser().getSelectedDirs() == null) {
            System.out.println("no directory selected");
            return;
        }
        if (User.getCurrentUser().getSelectedDirs().size() == 0) {
            System.out.println("no directory selected");
            return;
        }
        for (Dir dir : User.getCurrentUser().getSelectedDirs()) {
            if (dir.getParent() != null) {
                dir.getParent().deleteChildDir(dir);
                dir.setParent(null);
                User.getCurrentUser().removeDirFromAllDirs(dir);
            }
        }
        System.out.println("deleted successfully");
        User.getCurrentUser().setSelectedDirs(null);
    }

    public static void deleteFile() {

        if (User.getCurrentUser().getSelectedFiles() == null) {
            System.out.println("no file selected");
            return;
        }
        if (User.getCurrentUser().getSelectedFiles().size() == 0) {
            System.out.println("no file selected");
            return;
        }
        for (File file : User.getCurrentUser().getSelectedFiles()) {
            if (file.getParent() != null) {
                file.getParent().deleteChildFile(file);
                file.setParent(null);
                User.getCurrentUser().setSelectedFiles(null);
            }
        }
        System.out.println("deleted successfully");
        User.getCurrentUser().setSelectedFiles(null);
    }

    public static void zip(Matcher matcher) {
        if (User.getCurrentUser().getSelectedFiles() == null && User.getCurrentUser().getSelectedDirs() == null) {
            System.out.println("nothing to zip");
            return;
        }

        String name = matcher.group("zipname");
        String address = Dir.getCurrentDir().getFullNameAddress() + "/" + name + ".zip";
        if (File.isThisTrueAddress(address)) {
            System.out.println("file already exists");
            return;
        }
        System.out.println("zipped successfully");
        Zip zip = new Zip(name, User.getCurrentUser().getSelectedDirs(), User.getCurrentUser().getSelectedFiles());
        Dir.getCurrentDir().addChildren(zip);
        User.getCurrentUser().addToMyFiles(zip);
        for (File file : User.getCurrentUser().getSelectedFiles()) {
            file.getParent().deleteChildFile(file);
            file.setParent(null);
        }
        if (User.getCurrentUser().getSelectedDirs() != null) {
            for (Dir dir : User.getCurrentUser().getSelectedDirs()) {
                dir.getParent().deleteChildDir(dir);
                dir.setParent(null);
            }
        }
    }

    public static void cut(Matcher matcher) {
        if (User.getCurrentUser().getSelectedFiles() == null && User.getCurrentUser().getSelectedDirs() == null) {
            System.out.println("nothing selected");
            return;
        }
        String dirname = matcher.group("dirname");
        if (User.getCurrentUser().getSelectedDirs().isEmpty() && User.getCurrentUser().getSelectedFiles().isEmpty()) {
            System.out.println("nothing selected");
            return;
        }

        if (User.getCurrentUser().getSelectedDirs() != null) {
            for (Dir dir : User.getCurrentUser().getSelectedDirs()) {
                if (dir.getFullNameAddress().equals(dirname)) {
                    System.out.println("can't do action with a selected directory as destination");
                    return;
                }
            }
        }


        if (!Dir.isThisAddressValid(dirname)) {
            System.out.println("invalid destination");
            return;
        }

        if (User.getCurrentUser().getSelectedDirs() != null) {
            for (Dir dir1 : User.getCurrentUser().getSelectedDirs()) {
                String diir = dir1.getName();
                for(Dir dir8 : Dir.getDirByFullAddress(dirname).getChildrenDir()){
                    if(diir.equals(dir8.getName())){
                        diir = correctDiir(diir);
                    }
                }
                dir1.setName(diir);
                dir1.getParent().deleteChildDir(dir1);
                dir1.setParent(Dir.getDirByFullAddress(dirname));
                Dir.getDirByFullAddress(dirname).addChildren(dir1);
            }
        }

        System.out.println("cut successfully");


        if (User.getCurrentUser().getSelectedFiles() != null) {
            for (File file : User.getCurrentUser().getSelectedFiles()) {
                file.getParent().deleteChildFile(file);
                file.setParent(Dir.getDirByFullAddress(dirname));
                Dir.getDirByFullAddress(dirname).addChildren(file);
            }
        }
    }

    public static void copy(Matcher matcher) {

        if (User.getCurrentUser().getSelectedFiles() == null && User.getCurrentUser().getSelectedDirs() == null) {
            System.out.println("nothing selected");
            return;
        }
        String dirname = matcher.group("dirname");
        if (User.getCurrentUser().getSelectedDirs() == null && User.getCurrentUser().getSelectedFiles() == null) {
            System.out.println("nothing selected");
            return;
        }

        if (User.getCurrentUser().getSelectedDirs() != null) {
            for (Dir dir : User.getCurrentUser().getSelectedDirs()) {
                if (dir.getFullNameAddress().equals(dirname)) {
                    System.out.println("can't do action with a selected directory as destination");
                    return;
                }
            }
        }
        if (!Dir.isThisAddressValid(dirname)) {
            System.out.println("invalid destination");
            return;
        }

        Dir parent = Dir.getDirByFullAddress(dirname);
        ArrayList<Dir> dirs = User.getCurrentUser().getSelectedDirs();
        ArrayList<File> files = User.getCurrentUser().getSelectedFiles();

        if (dirs != null) {
            for (Dir dir : dirs) {
                String diir = dir.getName();
                for(Dir dir3 : parent.getChildrenDir()){
                    if(diir.equals(dir3.getName())){
                        diir = correctDiir(diir);
                    }
                }
                Dir dir1 = new Dir(diir, parent);
                dir1.setChildrenDir(dir.getChildrenDir());
                dir1.setChildrenFile(dir1.getChildrenFile());
                parent.addChildren(dir1);
            }
        }

        System.out.println("copy successfully");


        if (files != null) {
            for (File file1 : files) {
                String filename = file1.getName();
                for(File file5 : Dir.getDirByFullAddress(dirname).getChildrenFile()){
                    if (file5.getName().equals(filename) && file5.getFormat().equals(file1.getFormat())){
                        filename = correctFormat(filename);
                    }
                }
                File file = new File(filename, file1.getFormat(), false);
                file.setParent(Dir.getDirByFullAddress(dirname));
                Dir.getDirByFullAddress(dirname).addChildren(file);

            }
        }


    }

    public static String correctFormat(String name){
        for(int i=0 ;i<name.length(); i++){
            if(name.charAt(i) == '_'){
                return name;
            }
        }
        String correct ="";
        int index = name.length();
        for(int i=index -1; i>=0 ; i--){
            if(name.charAt(i) == '.'){
                index = i;
                break;
            }
        }
        correct = name.substring(0, index) +  "_" + Dir.getCurrentDir().getName() +name.substring(index, name.length());
        return correct;
    }

    public static String correctDiir(String name){
        for(int i=0 ;i<name.length(); i++){
            if(name.charAt(i) == '_'){
                return name;
            }
        }
        name = name + "_" + Dir.getCurrentDir().getName();
        return name;
    }

}
