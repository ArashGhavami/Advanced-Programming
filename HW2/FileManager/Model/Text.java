package Model;

public class Text {
    private String content;

    {
        content = "";
    }

    private int cursorPos;
    private String prevContent;
    {
        prevContent = "";
    }

    {
        cursorPos = 0;
    }

    private int cursorLine;
    {
        cursorLine = 1;
    }

    public String getContent() {
        return content;
    }

    public int getCursorPos() {
        return cursorPos;
    }

    public int getCursorLine() {
        return cursorLine;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCursorPos(int cursorPos) {
        this.cursorPos = cursorPos;
    }

    public void setCursorLine(int cursorLine) {
        this.cursorLine = cursorLine;
    }

    public int getCharIndex(){
        int result = 0;
        int line = this.cursorLine - 1;
        while(line!= 0){
            if(File.getCurrentFile().getText().getContent().charAt(result) == '\n'){
                line--;
            }
            result++;
        }
        int pos = this.getCursorPos();
        result += pos;
        return pos;
    }

    public void setPrevContent(String prevContent) {
        this.prevContent = prevContent;
    }

    public String getPrevContent() {
        return prevContent;
    }
}
