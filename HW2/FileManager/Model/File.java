package Model;

public class File {

    public boolean applied;
    private String name;
    private String format;
    private String fullName;
    private boolean isItHidden;
    private boolean isItZipped;
    private Dir parent;
    private String cutOrCopy;

    private int firstIndexSelected;
    {
        firstIndexSelected = -1;
    }
    private int secondIndexSelected;
    {
        secondIndexSelected = -1;
    }
    private Text content=  new Text();

    private static File currentFile;

    public File(String name, String format, boolean isItZipped){
        this.name = name;
        this.format = format;
        this.fullName = name+"." + format;
        this.isItZipped = isItZipped;
        this.setParent(Dir.getCurrentDir());
        if(name.charAt(0) == '.'){
            isItHidden = true;
        }
    }
    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isItHidden() {
        return isItHidden;
    }

    public boolean isItZipped() {
        return isItZipped;
    }

    public Dir getParent() {
        return parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setItHidden(boolean itHidden) {
        isItHidden = itHidden;
    }

    public void setItZipped(boolean itZipped) {
        isItZipped = itZipped;
    }

    public void setParent(Dir parent) {
        this.parent = parent;
    }

    public String getDirFullNameAddress(){
        String unordered = this.fullName + "/";
        Dir dir = this.getParent();
        while (dir != null){
            unordered += dir.getName() + "/";
            dir = dir.getParent();
        }
        String[] names = unordered.split("/");
        unordered = "";
        for(int i = names.length -1 ; i> 0; i--){
            unordered+= names[i];
            unordered += "/";
        }
        unordered += names[0];
        return unordered;
    }

    public static boolean isThisTrueAddress(String address){
        String[] names = address.split("/");
        String newAddress = "";
        if(names.length < 2){
            return false;
        }
        for(int i=0; i< names.length-2; i++){
            newAddress += names[i] + "/";
        }
        newAddress += names[names.length-2];
        if(!Dir.isThisAddressValid(newAddress)){
            return false;
        }
        Dir dir = Dir.getDirByFullAddress(newAddress);
        if(isThisFileInChildrenForThisDir(dir, names[names.length-1])){
            return true;
        }
        return false;
    }

    public static boolean isThisFileInChildrenForThisDir(Dir dir, String filename){
        for(File file : dir.getChildrenFile()){
            if(file.getFullName().equals(filename)){
                return true;
            }
        }
        return false;
    }

    public static File getFileByFullAddress(String address){
        if(!isThisTrueAddress(address)){
            return null;
        }
        String[] names = address.split("/");
        String newAddress = "";
        if(names.length > 2) {
            for (int i = 0; i < names.length - 2; i++) {
                newAddress += names[i] + "/";
            }
        }
        newAddress += names[names.length-2];
        if(!Dir.isThisAddressValid(newAddress)){
            return null;
        }
        Dir dir = Dir.getDirByFullAddress(newAddress);
        File file = File.getFileByParentAndName(dir, names[names.length-1]);
        return file;
    }

    public static File getFileByParentAndName(Dir dir, String filename){
        for(File file : dir.getChildrenFile()){
            if(file.getFullName().equals(filename)){
                return file;
            }
        }
        return null;
    }

    public String getFileFullnameAddress(){
        String unordered = this.getFullName()+"/";
        Dir dir = this.getParent();
        while (dir != null) {
            unordered =  dir.getName() + "/" + unordered;
            dir = dir.getParent();
        }
        String[] names = unordered.split("/");
        unordered = "";
        for (int i = names.length - 1; i > 0; i--) {
            unordered += names[i];
            unordered += "/";
        }
        unordered = unordered + names[0];

        String[] names1 = unordered.split("/");
        unordered = "";
        for(int i=names1.length-1; i >0; i--){
            unordered +=   names1[i] + "/";
        }
        unordered += names1[0];




        return unordered;
    }

    public static File getCurrentFile() {
        return currentFile;
    }

    public static void setCurrentFile(File currentFile) {
        File.currentFile = currentFile;
    }

    public Text getText(){
        return content;
    }


    public Text getContent() {
        return content;
    }

    public void setContent(Text content) {
        this.content = content;
    }

    public void setFirstIndexSelected(int firstIndexSelected) {
        this.firstIndexSelected = firstIndexSelected;
    }

    public void setSecondIndexSelected(int secondIndexSelected) {
        this.secondIndexSelected = secondIndexSelected;
    }

    public int getFirstIndexSelected() {
        return firstIndexSelected;
    }

    public int getSecondIndexSelected() {
        return secondIndexSelected;
    }

    public void setCutOrCopy(String cutOrCopy) {
        this.cutOrCopy = cutOrCopy;
    }

    public String getCutOrCopy() {
        return cutOrCopy;
    }
}
