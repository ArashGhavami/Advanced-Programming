package Controller;

import Model.Dir;
import Model.File;
import Model.Text;
import Model.User;
import View.InputOutput;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenFile {

    public static void run(String input, String regex) {
        Matcher matcher = null;

        String format = File.getCurrentFile().getFormat();
        if (format.equals("c") || format.equals("java") || format.equals("txt")) {
            runTotal(input, regex, matcher);
            return;
        }
        runRead(input, regex, matcher);


    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static void runTotal(String input, String regex, Matcher matcher) {

        //move cursor:
        regex = "\\s*move_curser\\s+-direction\\s+(?<direction>\\S+)\\s+-distance\\s+(?<distance>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            moveCursor(matcher);
            return;
        }

        //close file:
        regex = "\\s*close\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            close();
            return;
        }

        //show details:
        regex = "\\s*show\\s+details\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            showDetails();
            return;
        }

        //write:
        regex = "\\s*write\\s+-string\\s+\"(?<content>.*)\"\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            write(matcher);
            return;
        }

        //select:
        regex = "\\s*select\\s+-line\\s+(?<linenumber>\\S+)\\s+-pos\\s+(?<position>\\S+)\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            select(matcher);
            return;
        }

        //delete:
        regex = "\\s*delete\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            delete();
            return;
        }

        //paste:
        regex = "\\s*paste\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            paste();
            return;
        }

        //cut:
        regex = "\\s*cut\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            cut();
            return;
        }

        //apply changes for all:
        regex = "\\s*apply\\s+changes\\s+for\\s+all\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            File.getCurrentFile().getText().setPrevContent(File.getCurrentFile().getText().getContent());
            System.out.println("changes applied for all");
            File.getCurrentFile().applied = true;
            return;
        }

        //apply changes for me:
        regex = "\\s*apply\\s+changes\\s+for\\s+me\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            applyChangeForMe();
            File.getCurrentFile().applied = true;
            return;
        }


        //copy:
        regex = "\\s*copy\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            copy();
            return;
        }

        //show current cursor:
        regex = "\\s*scc\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            showCurrentCurser();
            return;
        }

        //show copied data:
        regex = "\\s*scd\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            showCopiedData();
            return;
        }


        //invalid:
        System.out.println("invalid command");

    }

    public static void runRead(String input, String regex, Matcher matcher) {

        //show details:
        regex = "\\s*show\\s+details\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            showDetails();
            return;
        }

        //close:
        regex = "\\s*close\\s*";
        matcher = getCommandMatcher(input, regex);
        if (matcher.matches()) {
            close();
            return;
        }


        //invalid:
        System.out.println("invalid command");

    }

    public static void moveCursor(Matcher matcher) {
        String direction = matcher.group("direction");
        int distance = Integer.parseInt(matcher.group("distance"));

        if (!(direction.equals("up") || direction.equals("down") || direction.equals("left") || direction.equals("right"))) {
            System.out.println("invalid direction");
            return;
        }

        if (distance > 10 || distance < 1) {
            System.out.println("invalid distance");
            return;
        }

        if (!cursorCheck(direction, distance)) {
            System.out.println("curser out of bounds");
            return;
        }

        File file = File.getCurrentFile();
        file.getText().setCursorLine(getNewCursorLine(direction, distance));
        file.getText().setCursorPos(getNewCursorPos(direction, distance));

        System.out.println("curser is in line " + file.getText().getCursorLine() + " and position " + file.getText().getCursorPos());

    }

    public static boolean cursorCheck(String direction, int distance) {
        File file = File.getCurrentFile();
        int newCursorPos = file.getText().getCursorPos();
        int newCursorLine = file.getText().getCursorLine();
        if (direction.equals("up")) {
            newCursorLine -= distance;
        }
        if (direction.equals("down")) {
            newCursorLine += distance;
        }
        if (direction.equals("left")) {
            newCursorPos -= distance;
        }
        if (direction.equals("right")) {
            newCursorPos += distance;
        }
        if (File.getCurrentFile().getText().getContent() == null)
            return false;

        String[] eachSentence = File.getCurrentFile().getText().getContent().split("\n");

        if (!(newCursorLine - 1 >= 0 && newCursorLine - 1 < eachSentence.length)) {
            return false;
        }
        if (!(newCursorPos <= eachSentence[newCursorLine - 1].length() && newCursorPos >= 0)) {
            return false;
        }
        return true;

    }

    public static void close() {
        InputOutput.setCurrrentClass("manage files");
        System.out.println("file closed. You are now in directory " + Dir.getCurrentDir().getFullNameAddress());
        if (!File.getCurrentFile().applied) {
            File.getCurrentFile().getText().setContent(File.getCurrentFile().getText().getPrevContent());
        }
        File.getCurrentFile().applied = false;
    }

    public static void showDetails() {
        File file = File.getCurrentFile();
        System.out.println("file name: " + file.getFileFullnameAddress());
        System.out.println("parent directory: " + file.getParent().getFullNameAddress());
        System.out.println("users with access:");
        String[] accessed = User.getAccessedUsers(file);
        for (int i = 0; i < accessed.length; i++) {
            System.out.println((i + 1) + ". " + accessed[i]);
        }
        fileContentShower(file);
    }

    public static void fileContentShower(File file) {

        if (file.getFormat().equals("txt") || file.getFormat().equals("java") || file.getFormat().equals("c")) {
            System.out.println("file content:");
            String data = file.getText().getContent();
            if (data == null)
                return;
            if (data.isEmpty())
                return;


            data = data.replaceAll("\\\\n", "\n");
            System.out.println(data);
        } else {
            System.out.println("file content:");
            System.out.println("unable to show content");
        }

    }

    public static int getCharIndex(File file) {

        String[] sentences = file.getText().getContent().split("\n");
        int pos = file.getText().getCursorPos();
        int line = file.getText().getCursorLine();
        line--;
        int result = 0;
        for (int i = 0; i < line; i++) {
            result += sentences[i].length();
        }
        result += pos;
        return result;
    }

    public static int getCharIndex(File file, int pos1, int line1) {

        String[] sentences = file.getText().getContent().split("\n");
        int pos = pos1;
        int line = line1;
        if (line > sentences.length + 1) {
            return -1;
        }
        if (!(sentences[line - 1].length() >= pos)) {
            return -1;
        }
        line--;
        int result = 0;
        for (int i = 0; i < line; i++) {
            result += sentences[i].length();
        }
        result += pos;
        return result;
    }

    public static int getNewCursorPos(String direction, int distance) {
        File file = File.getCurrentFile();
        int newCursorPos = file.getText().getCursorPos();
        int newCursorLine = file.getText().getCursorLine();
        if (direction.equals("up")) {
            newCursorLine -= distance;
        }
        if (direction.equals("down")) {
            newCursorLine += distance;
        }
        if (direction.equals("left")) {
            newCursorPos -= distance;
        }
        if (direction.equals("rigth")) {
            newCursorPos += distance;
        }
        return newCursorPos;
    }

    public static int getNewCursorLine(String direction, int distance) {
        File file = File.getCurrentFile();
        int newCursorPos = file.getText().getCursorPos();
        int newCursorLine = file.getText().getCursorLine();
        if (direction.equals("up")) {
            newCursorLine -= distance;
        }
        if (direction.equals("down")) {
            newCursorLine += distance;
        }
        if (direction.equals("left")) {
            newCursorPos -= distance;
        }
        if (direction.equals("rigth")) {
            newCursorPos += distance;
        }
        return newCursorLine;
    }

    public static int getPosByCharIndex(int charIndex, File file) {

        String content = file.getText().getContent();
        int pos = 0;
        for (int i = charIndex - 1; i >= 0; i--) {
            if (content.charAt(i) == '\n') {
                break;
            }
            pos++;
        }
        return pos;
    }

    public static int getLineByCharIndex(int charIndex, File file) {
        String content = file.getText().getContent();
        int line = 0;
        for (int i = charIndex - 1; i >= 0; i--) {
            if (content.charAt(i) == '\n') {
                line++;
            }
        }
        return line + 1;
    }

    public static void write(Matcher matcher) {
        String content = matcher.group("content");
        if (content.length() > 80) {
            System.out.println("string is too big!");
            return;
        }
        content = content.replaceAll("\\\\n", "\n");
        System.out.println("written successfully");


        String lastContent = File.getCurrentFile().getText().getContent();
        int charIndex = getCharIndex(File.getCurrentFile());
        String newContent = lastContent.substring(0, charIndex) + content + lastContent.substring(charIndex, lastContent.length());
        charIndex += content.length();
        File.getCurrentFile().getText().setContent(newContent);


        int newCursorLine = getLineByCharIndex(charIndex, File.getCurrentFile());
        int newCursorPos = getPosByCharIndex(charIndex, File.getCurrentFile());

        File.getCurrentFile().getText().setCursorPos(newCursorPos);
        File.getCurrentFile().getText().setCursorLine(newCursorLine);

    }

    public static void select(Matcher matcher) {
        int position = Integer.parseInt(matcher.group("position"));
        int line = Integer.parseInt(matcher.group("linenumber"));

        int charIndex = getCharIndex(File.getCurrentFile(), position, line);
        int lastCharIndex = getCharIndex(File.getCurrentFile());

        if (charIndex > File.getCurrentFile().getText().getContent().length() || charIndex == -1) {
            System.out.println("position out of bounds");
            return;
        }
        System.out.println("selected successfully");
        if (charIndex < lastCharIndex) {
            int temp = lastCharIndex;
            lastCharIndex = charIndex;
            charIndex = temp;
        }
        File.getCurrentFile().setFirstIndexSelected(lastCharIndex);
        File.getCurrentFile().setSecondIndexSelected(charIndex);
    }

    public static void delete() {
        File file = File.getCurrentFile();
        String text = file.getText().getContent();
        if (file.getFirstIndexSelected() == -1) {
            System.out.println("nothing to delete");
            return;
        }
        System.out.println("deleted successfully");
        String newString = text.substring(0, file.getFirstIndexSelected()) + text.substring(file.getSecondIndexSelected(), text.length());
        file.getText().setContent(newString);
        file.getText().setCursorLine(getLineByCharIndex(file.getFirstIndexSelected(), file));
        file.getText().setCursorPos(getPosByCharIndex(file.getFirstIndexSelected(), file));

        file.setSecondIndexSelected(-1);
        file.setFirstIndexSelected(-1);
    }

    public static void paste() {
        File file = File.getCurrentFile();
        Text text = file.getText();
        if (file.getCutOrCopy() == null) {
            System.out.println("nothing to paste");
            return;
        }
        int charIndex = getCharIndex(file);
        String newString = text.getContent().substring(0, charIndex) + file.getCutOrCopy() + text.getContent().substring(charIndex, text.getContent().length());
        text.setContent(newString);
        charIndex += file.getCutOrCopy().length();
        int newPos = getPosByCharIndex(charIndex, file);
        int newLine = getLineByCharIndex(charIndex, file);
        text.setCursorPos(newPos);
        text.setCursorLine(newLine);
        System.out.println("pasted successfully");
    }

    public static void cut() {
        File file = File.getCurrentFile();
        String content = file.getText().getContent();
        int first = file.getFirstIndexSelected();
        int second = file.getSecondIndexSelected();
        if (first == -1 || second == -1) {
            System.out.println("nothing to cut");
            return;
        }
        String wannaBeCut = file.getText().getContent().substring(first, second);
        String newString = content.substring(0, first) + content.substring(second, content.length());
        file.getText().setContent(newString);
        file.setCutOrCopy(wannaBeCut);
        file.setFirstIndexSelected(-1);
        file.setSecondIndexSelected(-1);
        System.out.println("cut successfully");
    }

    public static void showCurrentCurser() {
        System.out.println("line: " + File.getCurrentFile().getText().getCursorLine());
        System.out.println("pos: " + File.getCurrentFile().getText().getCursorPos());
    }

    public static void copy() {
        File file = File.getCurrentFile();
        int first = file.getFirstIndexSelected();
        int second = file.getSecondIndexSelected();
        if (first == -1 || second == -1) {
            System.out.println("nothing to copy");
            return;
        }
        String wannaBeCut = file.getText().getContent().substring(first, second);
        file.setCutOrCopy(wannaBeCut);
        System.out.println("copied successfully");
    }

    public static void applyChangeForMe() {
        File file = File.getCurrentFile();
        File lastFile = new File(file.getName(), file.getFormat(), file.isItZipped());
        lastFile.getText().setContent(file.getText().getPrevContent());
        for (User user : User.getAllUsers()) {
            if (user.doesThisFileSharedBefore(user, file)) {
                HashMap<File, User> map = user.getFilesSharedToMe();
                HashMap<File, User> newMap = new HashMap<>();
                for (Map.Entry<File, User> entry : map.entrySet()) {
                    if (!entry.getKey().equals(file)) {
                        newMap.put(entry.getKey(), entry.getValue());
                    } else {
                        newMap.put(lastFile, entry.getValue());
                    }
                }
                user.setFilesSharedToMe(newMap);
            }
        }
        System.out.println("changes applied for me");
    }

    public static void showCopiedData() {
        System.out.println(File.getCurrentFile().getCutOrCopy());
    }

}
