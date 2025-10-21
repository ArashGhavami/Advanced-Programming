package Model;

import java.util.*;

public class User {

    private int selectDeadLine;
    private  ArrayList<Dir> myDirs = new ArrayList<>();
    private  ArrayList<File> myFiles = new ArrayList<>();
    private String username;
    private String password;
    private boolean isItOneTime;
    private ArrayList<Dir>selectedDirs = new ArrayList<>();
    private ArrayList<File> selectedFiles = new ArrayList<>();
    private int dirDays;
    private int fileDays;
    private static User currentUser = null;
    private static ArrayList<User> allUsers = new ArrayList<>();
    private ArrayList<User> iBlocked = new ArrayList<>();
    private ArrayList<User> theyBlocked = new ArrayList<>();
    private HashMap<File, User> filesSharedToMe = new HashMap<>();
    private HashMap<File, User> filesISent = new HashMap<>();
    private HashMap<Dir, User> dirsISent;
    {
        dirsISent = new HashMap<>();
    }
    private HashMap<Dir, User> dirsSharedToMe = new HashMap<>();
    public User(String username, String password, boolean isItOneTime){
        this.username = username;
        this.password = password;
        this.isItOneTime = isItOneTime;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public boolean isItOneTime() {
        return isItOneTime;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public ArrayList<User> getiBlocked() {
        return iBlocked;
    }

    public ArrayList<User> getTheyBlocked() {
        return theyBlocked;
    }

    public HashMap<File, User> getFilesSharedToMe() {
        return filesSharedToMe;
    }

    public HashMap<File, User> getFilesISent() {
        return filesISent;
    }

    public HashMap<Dir, User> getDirsISent() {
        return dirsISent;
    }

    public HashMap<Dir, User> getDirsSharedToMe() {
        return dirsSharedToMe;
    }

    public ArrayList<Dir> getSelectedDirs() {
        return selectedDirs;
    }

    public ArrayList<File> getSelectedFiles() {
        return selectedFiles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setItOneTime(boolean itOneTime) {
        isItOneTime = itOneTime;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public static void setAllUsers(ArrayList<User> allUsers) {
        User.allUsers = allUsers;
    }

    public void setiBlocked(ArrayList<User> iBlocked) {
        this.iBlocked = iBlocked;
    }

    public void setTheyBlocked(ArrayList<User> theyBlocked) {
        this.theyBlocked = theyBlocked;
    }

    public void setFilesSharedToMe(HashMap<File, User> filesSharedToMe) {
        this.filesSharedToMe = filesSharedToMe;
    }

    public void setFilesISent(HashMap<File, User> filesISent) {
        this.filesISent = filesISent;
    }

    public void setDirsISent(HashMap<Dir, User> dirsISent) {
        this.dirsISent = dirsISent;
    }

    public void setDirsSharedToMe(HashMap<Dir, User> dirsSharedToMe) {
        this.dirsSharedToMe = dirsSharedToMe;
    }

    public void setSelectedDirs(ArrayList<Dir> selectedDirs) {
        this.selectedDirs = selectedDirs;
    }

    public void setSelectedFiles(ArrayList<File> selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    public static User getUserByUsername(String username){
        for(User user : allUsers){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public static void addUser(User user){
        allUsers.add(user);
    }

    public void addToIBlocked(User user) {
        iBlocked.add(user);
    }

    public void addToTheyBlocked(User user) {
        theyBlocked.add(user);
    }

    public void removeFromIBlocked(User user) {
        iBlocked.remove(user);
    }

    public void removeFromTheyBlocked(User user) {
        theyBlocked.remove(user);
    }

    public static void removeUser(User user) {
        allUsers.remove(user);
    }

    public Dir getRoot(){
        for(Dir dir : myDirs){
            if(dir.getName().equals("root")){
                return dir;
            }
        }
        return null;
    }

    public boolean doesThisDirSharedBefore(User user, Dir dir) {
        HashMap<Dir, User> map = User.getCurrentUser().getDirsISent();
        if (map.get(dir) == null) return false;
        if (map.get(dir).equals(user)) {
            return true;
        }
        return false;
    }

    public boolean amIInBlocklist(User user) {
        for (User user1 : theyBlocked) {
            if (user1.getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }
    public void addDirToDirsSharedToMe(Dir dir, User sender) {
        dirsSharedToMe.put(dir, sender);
    }

    public void addDirToDirsISent(Dir dir, User taker) {
        HashMap<Dir, User> map = User.getCurrentUser().getDirsISent();
        map.put(dir, taker);
        User.getCurrentUser().setDirsISent(map);
    }

    public boolean doesThisFileSharedBefore(User user, File file) {
        HashMap<File, User> map = User.getCurrentUser().getFilesISent();
        if(map == null){
            return false;
        }
        for(Map.Entry<File , User> entry : map.entrySet()){
            if(entry.getKey().getFullName().equals(file.getFullName()) && entry.getValue().getUsername().equals(user.getUsername())){
                return true;
            }
        }
        return false;
    }

    public File getFileSharedBefore(User user, File file) {
        HashMap<File, User> map = User.getCurrentUser().getFilesISent();
        if(map == null){
            return null;
        }
        for(Map.Entry<File , User> entry : map.entrySet()){
            if(entry.getKey().getFullName().equals(file.getFullName()) && entry.getValue().getUsername().equals(user.getUsername())){
                return file;
            }
        }
        return null;
    }

    public void addFileToISent(File file, User taker) {
        filesISent.put(file, taker);
    }

    public void addFileToFilesSharedToMe(File file, User sender) {
        filesSharedToMe.put(file, sender);
    }

    public  void addToMyDirs(Dir dir){
        myDirs.add(dir);
    }

    public  void addToMyFiles(File file){
        myFiles.add(file);
    }

    public ArrayList<Dir> getHiddenDirs(){
        ArrayList <Dir> hiddenDirs = new ArrayList<>();
        for(Dir dir : myDirs){
            if(dir.isItHidden()){
                hiddenDirs.add(dir);
            }
        }
        return hiddenDirs;
    }

    public ArrayList<File> getHiddenFiles(){
        ArrayList<File> hiddenFiles = new ArrayList<>();
        for(File file : myFiles){
            if(file.isItHidden() && !file.isItZipped()){
                hiddenFiles.add(file);
            }
        }
        return hiddenFiles;
    }
    public ArrayList<File> getHiddenZips(){
        ArrayList<File> hiddenZips = new ArrayList<>();
        for(File file : myFiles){
            if(file.isItZipped() && file.isItHidden()){
                hiddenZips.add(file);
            }
        }
        return hiddenZips;
    }

    public ArrayList<Dir> getMyDirs() {
        return myDirs;
    }

    public ArrayList<File> getMyFiles() {
        return myFiles;
    }

    public void setMyDirs(ArrayList<Dir> myDirs) {
        this.myDirs = myDirs;
    }

    public void setMyFiles(ArrayList<File> myFiles) {
        this.myFiles = myFiles;
    }

    public  void removeFileFromAllFiles(File file){
        myFiles.remove(file);
    }

    public void removeDirFromAllDirs(Dir dir){
        myDirs.remove(dir);
    }

    public int getSelectDeadLine() {
        return selectDeadLine;
    }

    public void setSelectDeadLine(int selectDeadLine) {
        this.selectDeadLine = selectDeadLine;
    }

    public void setDirDays(int dirDays) {
        this.dirDays = dirDays;
    }

    public void setFileDays(int fileDays) {
        this.fileDays = fileDays;
    }

    public int getDirDays() {
        return dirDays;
    }

    public int getFileDays() {
        return fileDays;
    }

    public static String[] getAccessedUsers(File file){
        ArrayList<User> users = new ArrayList<>();
        for(User user : allUsers){
            if(user.doesThisFileSharedBefore(user, file)){
                users.add(user);
            }
        }
        users.add(User.getCurrentUser());
        String[] accessed = new String[users.size() ];
        for(int i=0; i<users.size(); i++){
            accessed[i] = users.get(i).getUsername();
        }

        for(int i=0; i<users.size(); i++){
            for(int j=i; j<users.size(); j++){
                int result = accessed[i].compareTo(accessed[j]);
                if(result > 0){
                    String temp = accessed[i];
                    accessed[i] = accessed[j];
                    accessed[j] = temp;
                }
            }
        }
        return accessed;
    }



}
