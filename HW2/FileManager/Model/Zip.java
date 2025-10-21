package Model;

import java.util.ArrayList;

public class Zip extends File{

    private
    ArrayList<Dir> dirs = new ArrayList<>();
    ArrayList<File> files = new ArrayList<>();
    public Zip(String name, ArrayList<Dir> dirs, ArrayList<File> files){
        super(name, "zip", true);
        this.dirs = dirs;
        this.files = files;

    }
    public static Zip getZipByAddress(String address){
        String[] names = address.split("/");
        String paretName = "";
        for(int i=0; i<names.length-2; i++){
            paretName += names[i];
            paretName += "/";
        }
        paretName += names[names.length-2];
        Dir parent = Dir.getDirByFullAddress(paretName);
        for(File file : parent.getChildrenFile()){
            if ( file.getFullName().equals(names[names.length-1])){
                return (Zip) file;
            }
        }
        return null;
    }
    public ArrayList<Dir> getDirs() {
        return dirs;
    }
    public ArrayList<File> getFiles() {
        return files;
    }

}