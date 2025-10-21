package Model;

import Controller.FileManager;
import com.sun.security.jgss.GSSUtil;

import java.awt.geom.Area;
import java.util.ArrayList;

public class Dir {

    private String name;
    private static Dir currentDir;
    private Dir parent;
    private boolean isItHidden;
    private ArrayList<Dir> childrenDir = new ArrayList<>();
    private ArrayList<File> childrenFile = new ArrayList<>();

    public Dir(String name, Dir parent) {
        this.name = name;
        this.parent = parent;
        if (name.charAt(0) == '.') {
            this.isItHidden = true;
        }
    }

    public String getName() {
        return name;
    }

    public static Dir getCurrentDir() {
        return currentDir;
    }

    public Dir getParent() {
        return parent;
    }

    public boolean isItHidden() {
        return isItHidden;
    }

    public ArrayList<Dir> getChildrenDir() {
        return childrenDir;
    }

    public ArrayList<File> getChildrenFile() {
        return childrenFile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void setCurrentDir(Dir dir) {
        currentDir = dir;
    }

    public void setParent(Dir parent) {
        this.parent = parent;
    }

    public void setItHidden(boolean itHidden) {
        isItHidden = itHidden;
    }

    public void setChildrenDir(ArrayList<Dir> childrenDir) {
        this.childrenDir = childrenDir;
    }

    public void setChildrenFile(ArrayList<File> childrenFile) {
        this.childrenFile = childrenFile;
    }

    public String getFullNameAddress() {
        String unordered = "";
        Dir dir = this;
        while (dir != null) {
            unordered += dir.getName() + "/";
            dir = dir.getParent();
        }
        String[] names = unordered.split("/");
        unordered = "";
        for (int i = names.length - 1; i > 0; i--) {
            unordered += names[i];
            unordered += "/";
        }
        unordered += names[0];
        return unordered;
    }

    public static boolean isThisAddressValid(String address) {
        if (address == null) {
            return false;
        }
        if(address.equals("root")){
            return true;
        }

        String[] names = address.split("/");
        Dir dir1 = User.getCurrentUser().getRoot();
        if(names.length == 1){
            return false;
        }
        Dir dir= dir1;

        for (int i = 1; i < names.length; i++) {
            if (dir.getChildrenDir() == null) {
                if (i != names.length - 1) {
                    return false;
                }
            }

            if (dir.getChildrenDir().isEmpty()) {
                if (i != names.length - 1) {
                    return false;
                }
            }

            if (i != names.length - 1) {
                if (!doesThisDirHasTheTrueChild(dir, names[i])) {
                    return false;
                }
            }
            if (i == names.length - 1) {
                break;
            }

            dir = dir.getChildByChildName(names[i ]);
            // last time it was i + 1
        }
        if (!Dir.doesThisDirHasTheTrueChild(dir, names[names.length - 1])) {
            return false;
        }

        return true;

    }

    public static Dir getDirByFullAddress(String fullAddress) {
        if (!isThisAddressValid(fullAddress)) {
            return null;
        }
        if(fullAddress.equals("root")){
            return User.getCurrentUser().getRoot();
        }
        String[] names = fullAddress.split("/");
        Dir dir1 = User.getCurrentUser().getRoot();
        Dir dir = dir1;
        for (int i = 1; i < names.length; i++) {
            if (i != names.length - 1) {
                dir = dir.getChildByChildName(names[i]);
            }
        }

        dir = dir.getChildByChildName(names[names.length-1]);
        return dir;
    }

    public static boolean doesThisDirHasTheTrueChild(Dir targetDir, String child) {
        for (Dir dir : targetDir.getChildrenDir()) {
            if (dir.getName().equals(child)) {
                return true;
            }
        }
        return false;
    }

    public Dir getChildByChildName(String dirname) {
        for (Dir dir : this.childrenDir) {
            if (dir.getName().equals(dirname)) {
                return dir;
            }
        }
        return null;
    }

    public void addChildren(Dir dir) {
        childrenDir.add(dir);
    }

    public void addChildren(File file) {
        childrenFile.add(file);
    }

    public static void removeFromChildDir(String address) {
        Dir dir = Dir.getDirByFullAddress(address);
        for (Dir dir1 : dir.getParent().getChildrenDir()) {
            if (dir1.getFullNameAddress().equals(dir.getFullNameAddress())) {
                dir.getParent().getChildrenDir().remove(dir1);
            }
        }


    }

    public void deleteChildDir(Dir dir) {
        childrenDir.remove(dir);
    }

    public void deleteChildFile(File file) {
        childrenFile.remove(file);
    }


}
