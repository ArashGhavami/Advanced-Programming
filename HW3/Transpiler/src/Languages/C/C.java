package Languages.C;

import Languages.Code;
import Transpiler.AbstractSyntaxTree;
import Transpiler.RuleType;

import java.io.StringReader;
import java.util.ArrayList;

import static Transpiler.RuleType.*;
import static Transpiler.RuleType.PAR;

public class C extends Code {

    public C(String code) {
        super(code);
    }

    public C(AbstractSyntaxTree ast) {
        super(ast);
    }

    @Override
    public AbstractSyntaxTree parseToAST() {
        CLexer lexer = new CLexer(new StringReader(this.code));
        CParser parser = new CParser(lexer);
        try {
            Object result = parser.parse().value;
            this.ast = (AbstractSyntaxTree) result;
            return this.ast;
        } catch (Exception e) {
            System.err.println("Parser error: " + e.getMessage());
            this.ast = null;
            return null;
        }
    }

    @Override
    public String generateCode() {
        StringBuilder result = new StringBuilder();
        nodeTypes(ast, result);
        System.out.println(result);
        return result.toString();
    }

    public void nodeTypes(AbstractSyntaxTree abstractSyntaxTree, StringBuilder result) {
        switch (abstractSyntaxTree.getType()) {
            case PROGRAM:
                program(abstractSyntaxTree, result);
                break;
            case STATEMENTS:
                statements(abstractSyntaxTree, result);
                break;
            case FOLLOW_STATEMENTS:
                followStatements(abstractSyntaxTree, result);
                break;
            case STATEMENT:
                statement(abstractSyntaxTree, result);
                break;
            case DECLARATION:
                declaration(abstractSyntaxTree, result);
                break;
            case ASSIGNMENTS:
                assignments(abstractSyntaxTree, result);
                break;
            case ASSIGNMENT:
                assignment(abstractSyntaxTree, result);
                break;
            case IF:
                ifStatement(abstractSyntaxTree, result);
                break;
            case SWITCH:
                switchStatement(abstractSyntaxTree, result);
                break;
            case CASES:
                cases(abstractSyntaxTree, result);
                break;
            case DISJUNCTION:
                disjunction(abstractSyntaxTree, result);
                break;
            case WHILE:
                whileStatement(abstractSyntaxTree, result);
                break;
            case CONJUNCTION:
                conjunction(abstractSyntaxTree, result);
                break;
            case INVERSION:
                inversion(abstractSyntaxTree, result);
                break;
            case COMPARISON:
                comparison(abstractSyntaxTree, result);
                break;
            case EQ:
                eqSum(abstractSyntaxTree, result);
                break;
            case LT:
                ltSum(abstractSyntaxTree, result);
                break;
            case GT:
                gtSum(abstractSyntaxTree, result);
                break;
            case SUM:
                sum(abstractSyntaxTree, result);
                break;
            case TERM:
                term(abstractSyntaxTree, result);
                break;
            case MODULO:
                modulo(abstractSyntaxTree, result);
                break;
            case FACTOR:
                factor(abstractSyntaxTree, result);
                break;
            case PRIMARY:
                primary(abstractSyntaxTree, result);
                break;
            case ID:
                id(abstractSyntaxTree, result);
                break;
            case NUM:
                num(abstractSyntaxTree, result);
                break;
            case OPTIONS:
                options(abstractSyntaxTree, result);
                break;
            case PRINT:
                print(abstractSyntaxTree, result);
                break;
        }
    }

    public void program(AbstractSyntaxTree ast, StringBuilder result) {
        result.append("void ");
        nodeTypes(ast.getChildren().getFirst(), result);
        result.append(" ( ) { \n");
        nodeTypes(ast.getChildren().get(1), result);
        result.append(" }\n");
    }

    public void options(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() > 1) {
            ast.addToOptions(Integer.parseInt(ast.getChildren().get(1).getLexeme()));
            nodeTypes(ast.getChildren().get(0), result);
        } else ast.addToOptions(Integer.parseInt(ast.getChildren().get(0).getLexeme()));
    }

    public void statements(AbstractSyntaxTree ast, StringBuilder result) {
        for (AbstractSyntaxTree abstractSyntaxTree : ast.getChildren()) {
            nodeTypes(abstractSyntaxTree, result);
        }
    }

    public void followStatements(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == RuleType.MULTI) {
            if (AbstractSyntaxTree.isIsFollowStatements()) {
                result.append(" {\n");
            }
            nodeTypes(ast.getChildren().getFirst(), result);
            if (AbstractSyntaxTree.isIsFollowStatements())
                result.append(" } \n");
        } else if (ast.getSubType() == RuleType.SINGLE) {
            nodeTypes(ast.getChildren().getFirst(), result);
        } else if (ast.getSubType() == RuleType.EMPTY) {
            if (AbstractSyntaxTree.isIsFollowStatements())
                result.append(" { } \n");
        }
    }

    public void statement(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == RuleType.DECLARE || ast.getSubType() == RuleType.ASSIGNMENTS) {
            nodeTypes(ast.getChildren().getFirst(), result);
            result.append(" ; \n");
        } else if (ast.getSubType() == RuleType.BREAK) {
            result.append(" break ;\n ");
        } else if (ast.getSubType() == RuleType.CONTINUE) {
            result.append(" continue ;\n ");
        } else if (ast.getSubType() == RuleType.PRINT) {
            result.append(" cout << ");
            nodeTypes(ast.getChildren().getFirst(), result);
            result.append(" ;\n");
        } else {
            nodeTypes(ast.getChildren().getFirst(), result);
        }
    }

    public void print(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() == 2) {
            nodeTypes(ast.getChildren().getFirst(), result);
            result.append(" << ");
            nodeTypes(ast.getChildren().get(1), result);
        } else {
            result.append(" << ");
            nodeTypes(ast.getChildren().getFirst(), result);
        }
    }

    public void declaration(AbstractSyntaxTree ast, StringBuilder result) {
        result.append("int ");
        nodeTypes(ast.getChildren().getFirst(), result);
    }

    public void assignments(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() == 2) {
            nodeTypes(ast.getChildren().getFirst(), result);
            result.append(" , ");
            nodeTypes(ast.getChildren().get(1), result);
        } else {
            nodeTypes(ast.getChildren().getFirst(), result);
        }
    }

    public void assignment(AbstractSyntaxTree ast, StringBuilder result) {
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" = ");
        nodeTypes(ast.getChildren().get(1), result);
    }

    public void ifStatement(AbstractSyntaxTree ast, StringBuilder result) {
        result.append(" if (");
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" ) \n");
        nodeTypes(ast.getChildren().get(1), result);
        result.append(" else ");
        nodeTypes(ast.getChildren().get(2), result);
    }

    public void switchStatement(AbstractSyntaxTree ast, StringBuilder result) {
        AbstractSyntaxTree.setIsFollowStatements(false);
        result.append(" switch ( ");
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" ) { \n");
        nodeTypes(ast.getChildren().get(1), result);
        result.append(" } \n");
    }

    public void cases(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() == 3) {
            AbstractSyntaxTree.getOptions().clear();
            nodeTypes(ast.getChildren().get(0), result);
            ArrayList<Integer> ints = new ArrayList<>();
            for (Integer integer : AbstractSyntaxTree.getOptions()) {
                ints.add(integer);
            }
            if (ints.size() > 1) {
                for (int i = ints.size() - 1; i >= 0; i--) {
                    result.append(" case ");
                    result.append(ints.get(i).toString());
                    result.append(" : ");
                    nodeTypes(ast.getChildren().get(1), result);
                }
            }
            AbstractSyntaxTree.getOptions().clear();
            nodeTypes(ast.getChildren().get(2), result);
        } else {
            result.append(" default : \n");
            nodeTypes(ast.getChildren().get(0), result);
        }
    }

    public void whileStatement(AbstractSyntaxTree ast, StringBuilder result) {
        result.append(" while ( ");
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" ) \n");
        nodeTypes(ast.getChildren().get(1), result);
    }

    public void disjunction(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() == 2) {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" || ");
            nodeTypes(ast.getChildren().get(1), result);
        } else {
            nodeTypes(ast.getChildren().get(0), result);
        }
    }

    public void conjunction(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() == 2) {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" && ");
            nodeTypes(ast.getChildren().get(1), result);
        } else {
            nodeTypes(ast.getChildren().get(0), result);
        }
    }

    public void inversion(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == RuleType.MULTI) {
            result.append(" ! ");
        }
        nodeTypes(ast.getChildren().get(0), result);
    }

    public void comparison(AbstractSyntaxTree ast, StringBuilder result) {
        nodeTypes(ast.getChildren().get(0), result);
    }

    public void sum(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == DEFAULT)
            nodeTypes(ast.getChildren().getFirst(), result);
        else if (ast.getSubType() == ADD) {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" + ");
            nodeTypes(ast.getChildren().get(1), result);
        } else {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" - ");
            nodeTypes(ast.getChildren().get(1), result);
        }
    }

    public void modulo(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() == 1)
            nodeTypes(ast.getChildren().get(0), result);
        else {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" % ");
            nodeTypes(ast.getChildren().get(1), result);
        }
    }

    public void term(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == DEFAULT)
            nodeTypes(ast.getChildren().get(0), result);
        else if (ast.getSubType() == DIVIDES) {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" / ");
            nodeTypes(ast.getChildren().get(1), result);
        } else if (ast.getSubType() == TIMES) {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" * ");
            nodeTypes(ast.getChildren().get(1), result);
        }

    }

    public void factor(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == DEFAULT)
            nodeTypes(ast.getChildren().getFirst(), result);
        else if (ast.getSubType() == NEGATIVE) {
            result.append(" - ");
            nodeTypes(ast.getChildren().get(0), result);
        } else if (ast.getSubType() == POSITIVE) {
            result.append(" + ");
            nodeTypes(ast.getChildren().get(0), result);
        } else if (ast.getSubType() == PAR) {
            result.append(" ( ");
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" ) \n");
        }
    }

    public void primary(AbstractSyntaxTree ast, StringBuilder result) {
        nodeTypes(ast.getChildren().get(0), result);
    }

    public void num(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getLexeme() != null)
            result.append(ast.getLexeme());
    }

    public void gtSum(AbstractSyntaxTree ast, StringBuilder result) {
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" > ");
        nodeTypes(ast.getChildren().get(1), result);
    }

    public void ltSum(AbstractSyntaxTree ast, StringBuilder result) {
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" < ");
        nodeTypes(ast.getChildren().get(1), result);
    }

    public void eqSum(AbstractSyntaxTree ast, StringBuilder result) {
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" == ");
        nodeTypes(ast.getChildren().get(1), result);
    }

    public void id(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getLexeme() != null) result.append(ast.getLexeme());
        if (ast.getChildren() != null || !ast.getChildren().isEmpty()) {
            for (AbstractSyntaxTree abstractSyntaxTree : ast.getChildren()) {
                nodeTypes(abstractSyntaxTree, result);
            }
        }
        result.append(" ");
    }
}
