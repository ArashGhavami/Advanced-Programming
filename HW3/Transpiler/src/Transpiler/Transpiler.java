package Transpiler;

import Languages.Code;
import Languages.Runnable;

import java.util.ArrayList;
import java.util.List;

public class Transpiler<T extends Runnable> {

    List<T> runnables = new ArrayList<>();

    public void addCode(T code) {
        runnables.add(code);
    }

    public List<AbstractSyntaxTree> getAbstractSyntaxTrees() {
        List<AbstractSyntaxTree> abstractSyntaxTrees = new ArrayList<>();
        for (T t : runnables) {
            abstractSyntaxTrees.add(t.parseToAST());
        }

        return abstractSyntaxTrees;
    }

    public List<String> getCodes() {
        /**
         * This function gathers each runnable object's string code and returns them.
         * @return list of each runnable object's string code.
         */
        // TODO: return codes of all runnable objects.

        List<String> codes = new ArrayList<>();
        for (T t : runnables) {
            codes.add(t.generateCode());
        }
        return codes;
    }

    public List<T> getSimilarRunnables(T runnable) {
        Code code = (Code) runnable;
        List<T> similarRunnables = new ArrayList<>();

        for (T t : runnables) {
            Code code1 = (Code) t;
            if (code1.parseToAST() != null && code.parseToAST() != null) {
                if (code1.parseToAST().equals(code.parseToAST())) {
                    similarRunnables.add(t);
                }
            }
            else{
                System.out.println(code1.getClass());
            }
        }
        return similarRunnables;
    }

    public List<T> getUniqueRunnables() {

        List<T> uniqueRunnables = new ArrayList<>();
        for (T runnable : runnables) {
            if (getSimilarRunnables(runnable).size() == 1)
                uniqueRunnables.add(runnable);
        }

        return uniqueRunnables;

    }
}