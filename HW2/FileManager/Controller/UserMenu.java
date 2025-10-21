package Controller;

import Model.Dir;
import Model.File;
import Model.User;
import View.InputOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserMenu {

    public static void run(String input, String regex) {
        Matcher matcher = null;

        //block:
        regex = "\\s*block\\s+-user\\s+(?<username>\\S*)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            blockUser(matcher);
            return;
        }


        //unblock:
        regex = "\\s*unblock\\s+-user\\s+(?<username>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            unblockUser(matcher);
            return;
        }

        //show blocklist:
        regex = "\\s*show\\s+blocklist\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            showBlockList();
            return;
        }

        //logout:
        regex = "\\s*logout\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            logout();
            return;
        }

        //share dir:
        regex = "\\s*share\\s+-dir\\s+(?<dirname>\\S+)\\s+-target\\s+(?<username>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            shareDir(matcher);
            return;
        }

        //share file:
        regex = "\\s*share\\s+-file\\s+(?<filename>\\S+)\\s+-target\\s+(?<username>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            shareFile(matcher);
            return;
        }


        //show shares:
        regex = "\\s*view\\s+-shared\\s+-username\\s+(?<username>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            viewShared(matcher);
            return;
        }

        //-me:
        regex = "\\s*view\\s+-shared\\s+-username\\s+(?<username>\\S+)\\s+-me\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            viewSharedME(matcher);
            return;
        }


        //-user:
        regex = "\\s*view\\s+-shared\\s+-username\\s+(?<username>\\S+)\\s+-user\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            viewSharedUser(matcher);
            return;
        }


        //share directory with all:
        regex = "\\s*share_all\\s+-dir\\s+(?<dirname>\\S*)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            shareAll(matcher);
            return;
        }

        //share file with all:
        regex = "\\s*share_all\\s+-file\\s+(?<dirname>\\S*)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            shareAllFile(matcher);
            return;
        }


        //logout:
        regex = "\\s*logout\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            logout();
            return;
        }

        //manage files:
        regex = "\\s*manage\\s+files\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            manageFiles();
            return;
        }


        System.out.println("invalid command");

    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static void blockUser(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null) {
            System.out.println("no such user exists");
            return;
        }
        for (User user1 : User.getCurrentUser().getiBlocked()) {
            if (user1.getUsername().equals(username)) {
                System.out.println("user already blocked");
                return;
            }
        }
        if (User.getCurrentUser().getUsername().equals(username)) {
            System.out.println("You can't interact with yourself!");
            return;
        }
        System.out.println("user " + username + " is now blocked");
        User.getCurrentUser().addToIBlocked(user);
        user.addToTheyBlocked(User.getCurrentUser());
    }

    public static void unblockUser(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null) {
            System.out.println("no such user exists");
            return;
        }
        boolean isItBLocked = false;
        for (User user1 : User.getCurrentUser().getiBlocked()) {
            if (user1.getUsername().equals(username)) {
                isItBLocked = true;
            }
        }
        if (User.getCurrentUser().getUsername().equals(username)) {
            System.out.println("You can't interact with yourself!");
            return;
        }
        if (!isItBLocked) {
            System.out.println("user is not blocked");
            return;
        }
        System.out.println("user " + username + " is now unblocked");
        User.getCurrentUser().removeFromIBlocked(user);
        user.removeFromTheyBlocked(User.getCurrentUser());
    }

    public static void showBlockList() {
        User user = User.getCurrentUser();
        if (user.getTheyBlocked().size() == 0 && user.getiBlocked().size() == 0) {
            System.out.println("block list empty");
            return;
        }
        System.out.println("blocked:");
        int index = 1;
        for (User user1 : user.getiBlocked()) {
            System.out.println(index + ". " + user1.getUsername());
            index++;
        }
        System.out.println("blocker:");
        index = 1;
        for (User user1 : user.getTheyBlocked()) {
            System.out.println(index + ". " + user1.getUsername());
            index++;
        }
    }

    public static void logout() {
        User user = User.getCurrentUser();
        if (user == null) {
            System.out.println("There's no account to be logged out");
            return;
        }
        System.out.println("logged out successfully");
        User.setCurrentUser(null);
        InputOutput.setCurrrentClass("login menu");
        if (user.isItOneTime()) {
            System.out.println("account removed!");
            User.removeUser(user);
        }


    }

    public static void shareDir(Matcher matcher) {
        String dirAddress = matcher.group("dirname");
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (!Dir.isThisAddressValid(dirAddress)) {
            System.out.println("no such file or directory");
            return;
        }
        if (user == null) {
            System.out.println("no such user exists");
            return;
        }

        if (dirAddress.equals("root")) {
            System.out.println("you can't share root");
            return;
        }

        Dir dir = Dir.getDirByFullAddress(dirAddress);
        if (User.getCurrentUser().doesThisDirSharedBefore(user, dir)) {
            System.out.println("already shared");
            return;
        }

        if (User.getCurrentUser().amIInBlocklist(user)) {
            System.out.println("can't share, you are blocked");
            return;
        }

        if (User.getCurrentUser().getUsername().equals(username)) {
            System.out.println("You can't interact with yourself!");
            return;
        }
        System.out.println("shared successfully");
        user.addDirToDirsSharedToMe(dir, User.getCurrentUser());
        User.getCurrentUser().addDirToDirsISent(dir, user);
    }

    public static void shareFile(Matcher matcher) {
        String filename = matcher.group("filename");
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        File file = File.getFileByFullAddress(filename);
        if (user == null) {
            System.out.println("no such user exists");
            return;
        }
        if (!File.isThisTrueAddress(filename)) {
            System.out.println("no such file or directory");
            return;
        }


        if (User.getCurrentUser().amIInBlocklist(user)) {
            System.out.println("can't share, you are blocked");
            return;
        }
        if (user.doesThisFileSharedBefore(user, file)) {
            System.out.println("already shared");
            return;
        }

        System.out.println("shared successfully");
        User.getCurrentUser().addFileToISent(file, user);
        user.addFileToFilesSharedToMe(file, User.getCurrentUser());

    }

    public static void viewShared(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null) {
            System.out.println("no such user exists");
            return;
        }
        if (User.getCurrentUser().getUsername().equals(username)) {
            System.out.println("You can't interact with yourself!");
            return;
        }
        if (!((getKeysByValueFile(User.getCurrentUser().getFilesSharedToMe(), user).isEmpty() && getKeysByValueFile(User.getCurrentUser().getFilesISent(), user).isEmpty() && getKeysByValueDir(User.getCurrentUser().getDirsSharedToMe(), user).isEmpty() && getKeysByValueDir(User.getCurrentUser().getDirsISent(), user).isEmpty()))) {
            System.out.println("shared to " + username + " by me:");
            int index = 1;
            if (getKeysByValueDir(User.getCurrentUser().getDirsISent(), user) != null) {
                System.out.println("dir:");
                for (Dir dir : getKeysByValueDir(User.getCurrentUser().getDirsISent(), user)) {
                    System.out.println(index + ". " + dir.getFullNameAddress());
                    index++;
                }
            }
            if (getKeysByValueFile(User.getCurrentUser().getFilesISent(), user) != null) {
                ArrayList<File> notZippedFiles = new ArrayList<>();
                ArrayList<File> zipped = new ArrayList<>();
                for (File file : getKeysByValueFile(User.getCurrentUser().getFilesISent(), user)) {
                    if (!file.isItZipped()) {
                        notZippedFiles.add(file);
                    } else {
                        zipped.add(file);
                    }
                }

                System.out.println("file:");
                index = 1;
                if (!notZippedFiles.isEmpty()) {
                    for (File file : notZippedFiles) {
                        System.out.println(index + ". " + file.getFileFullnameAddress());
                        index++;
                    }
//                    System.out.println("...");
                }

                System.out.println("zip:");
                index = 1;
                if (zipped.size() != 0) {
                    for (File file : zipped) {
                        System.out.println(index + ". " + file.getFileFullnameAddress());
                        index++;
                    }
//                    System.out.println("...");
                }
            }

            System.out.println("shared to me by " + username + ":");
            index = 1;
            if (getKeysByValueDir(User.getCurrentUser().getDirsSharedToMe(), user) != null) {
                System.out.println("dir:");
                for (Dir dir : getKeysByValueDir(User.getCurrentUser().getDirsSharedToMe(), user)) {
                    System.out.println(index + ". " + dir.getFullNameAddress());
                    index++;
                }
            }
            if (getKeysByValueFile(User.getCurrentUser().getFilesSharedToMe(), user) != null) {
                ArrayList<File> notZippedFiles = new ArrayList<>();
                ArrayList<File> zipped = new ArrayList<>();

                for (File file : getKeysByValueFile(User.getCurrentUser().getFilesSharedToMe(), user)) {
                    if (file.isItZipped()) {
                        zipped.add(file);

                    } else {
                        notZippedFiles.add(file);
                    }
                }

                System.out.println("file:");
                index = 1;
                if (notZippedFiles.size() != 0) {
                    for (File file : notZippedFiles) {
                        System.out.println(index + ". " + file.getFileFullnameAddress());
                        index++;
                    }
//                    System.out.println("...");
                }

                System.out.println("zip:");
                index = 1;
                if (zipped.size() != 0) {
                    for (File file : zipped) {
                        System.out.println(index + ". " + file.getFileFullnameAddress());
                        index++;
                    }
//                    System.out.println("...");
                }
            }

        } else {
            System.out.println("share list empty");
        }


    }

    public static ArrayList<Dir> getKeysByValueDir(HashMap<Dir, User> map, User user) {
        ArrayList<Dir> dirs = new ArrayList<>();
        for (Map.Entry<Dir, User> entry : map.entrySet()) {
            if (Objects.equals(user, entry.getValue())) {

                dirs.add(entry.getKey());
            }
        }
        return dirs;
    }

    public static ArrayList<File> getKeysByValueFile(HashMap<File, User> map, User user) {
        ArrayList<File> files = new ArrayList<>();
        for (Map.Entry<File, User> entry : map.entrySet()) {
            if (Objects.equals(user, entry.getValue())) {
                files.add((entry.getKey()));
            }
        }
        return files;
    }

    public static void viewSharedME(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null) {
            System.out.println("no such user exists");
            return;
        }
        if (!(getKeysByValueFile(User.getCurrentUser().getFilesISent(), user).size() == 0 && getKeysByValueDir(User.getCurrentUser().getDirsISent(), user).size() == 0)) {
            System.out.println("shared to " + username + " by me:");
            int index = 1;
            if (getKeysByValueDir(User.getCurrentUser().getDirsISent(), user) != null) {
                System.out.println("dir:");
                for (Dir dir : getKeysByValueDir(User.getCurrentUser().getDirsISent(), user)) {
                    System.out.println(index + ". " + dir.getFullNameAddress());
                    index++;
                }
//                System.out.println("...");
            }
            if (getKeysByValueFile(User.getCurrentUser().getFilesISent(), user) != null) {
                ArrayList<File> notZippedFiles = new ArrayList<>();
                ArrayList<File> zipped = new ArrayList<>();
                for (File file : getKeysByValueFile(User.getCurrentUser().getFilesISent(), user)) {
                    if (!file.isItZipped()) {
                        notZippedFiles.add(file);
                    } else {
                        zipped.add(file);
                    }
                }

                System.out.println("file:");
                index = 1;
                if (notZippedFiles.size() != 0) {
                    for (File file : notZippedFiles) {
                        System.out.println(index + ". " + file.getFileFullnameAddress());
                        index++;
                    }
//                    System.out.println("...");
                }

                System.out.println("zip:");
                index = 1;
                if (zipped.size() != 0) {
                    for (File file : zipped) {
                        System.out.println(index + ". " + file.getFileFullnameAddress());
                        index++;
                    }
//                    System.out.println("...");
                }
            }
        } else {
            System.out.println("share list empty");
            return;
        }
    }

    public static void viewSharedUser(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null) {
            System.out.println("no such user exists");
            return;
        }
        if (!(getKeysByValueFile(User.getCurrentUser().getFilesSharedToMe(), user).size() == 0 && getKeysByValueDir(User.getCurrentUser().getDirsSharedToMe(), user).size() == 0)) {
            int index = 1;
            System.out.println("shared to me by " + username + ":");
            index = 1;
            if (getKeysByValueDir(User.getCurrentUser().getDirsSharedToMe(), user) != null) {
                System.out.println("dir:");
                for (Dir dir : getKeysByValueDir(User.getCurrentUser().getDirsSharedToMe(), user)) {
                    System.out.println(index + ". " + dir.getFullNameAddress());
                    index++;
                }
            }
            if (getKeysByValueFile(User.getCurrentUser().getFilesSharedToMe(), user) != null) {
                ArrayList<File> notZippedFiles = new ArrayList<>();
                ArrayList<File> zipped = new ArrayList<>();

                for (File file : getKeysByValueFile(User.getCurrentUser().getFilesSharedToMe(), user)) {
                    if (file.isItZipped()) {
                        zipped.add(file);

                    } else {
                        notZippedFiles.add(file);
                    }
                }

                System.out.println("file:");
                index = 1;
                if (notZippedFiles.size() != 0) {
                    for (File file : notZippedFiles) {
                        System.out.println(index + ". " + file.getName());
                        index++;
                    }
//                    System.out.println("...");
                }

                System.out.println("zip:");
                index = 1;
                if (zipped.size() != 0) {
                    for (File file : zipped) {
                        System.out.println(index + ". " + file.getFileFullnameAddress());
                        index++;
                    }
//                    System.out.println("...");
                }
            }

        } else {
            System.out.println("share list empty");
            return;
        }


    }

    public static void shareAll(Matcher matcher) {
        String dirname = matcher.group("dirname");
        User currentUser = User.getCurrentUser();
        if (dirname.equals("root")) {
            System.out.println("you can't share root");
            return;
        }
        if (!Dir.isThisAddressValid(dirname)) {
            System.out.println("no such file or directory");
            return;
        }

        Dir dir = Dir.getDirByFullAddress(dirname);
        boolean anyone = false;
        ArrayList<User> usersToBeAdd = new ArrayList<>();
        for (User user : User.getAllUsers()) {
            if (user.getUsername().equals(User.getCurrentUser().getUsername())) {
                continue;
            }
            if (user.amIInBlocklist(User.getCurrentUser())) {
                continue;
            }
            if (currentUser.amIInBlocklist(user)) {
                continue;
            }
            if (User.getCurrentUser().doesThisDirSharedBefore(user, dir)) {
                continue;
            }

            user.addDirToDirsSharedToMe(dir, User.getCurrentUser());
            User.getCurrentUser().addDirToDirsISent(dir, user);
            anyone = true;
        }
        if (anyone) {
            System.out.println("shared successfully");
            return;
        }
        System.out.println("no available user");

    }

    public static void manageFiles() {
        InputOutput.setCurrrentClass("manage files");
        System.out.println("you are now in directory root");
    }

    public static void shareAllFile(Matcher matcher) {
        String dirname = matcher.group("dirname");
        User currentUser = User.getCurrentUser();
        if (!File.isThisTrueAddress(dirname)) {
            System.out.println("no such file or directory");
            return;
        }

        File dir = File.getFileByFullAddress(dirname);
        boolean anyone = false;
        for (User user1 : User.getAllUsers()) {
            if (user1.getUsername().equals(User.getCurrentUser().getUsername())) {
                continue;
            }
            if (user1.amIInBlocklist(User.getCurrentUser())) {
                continue;
            }
            if (currentUser.amIInBlocklist(user1)) {
                continue;
            }
            if (User.getCurrentUser().doesThisFileSharedBefore(user1, dir)) {
                continue;
            }
            currentUser.addFileToISent(dir, user1);
            user1.addFileToFilesSharedToMe(dir, currentUser);
            anyone = true;
        }
        if (anyone) {
            System.out.println("shared successfully");
            return;
        }
        System.out.println("no available user");

    }

}
