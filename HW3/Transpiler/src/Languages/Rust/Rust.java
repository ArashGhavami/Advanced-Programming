package Languages.Rust;

import Languages.Code;
import Transpiler.AbstractSyntaxTree;
import Transpiler.NodeType;
import Transpiler.Transpiler;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static Transpiler.RuleType.*;

public class Rust extends Code {

    public Rust(String code) {
        super(code);
    }

    public Rust(AbstractSyntaxTree ast) {
        super(ast);
    }

    @Override
    public AbstractSyntaxTree parseToAST() {
        RustLexer lexer = new RustLexer(new StringReader(this.code));
        RustParser parser = new RustParser(lexer);
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
                assignments(abstractSyntaxTree, result, false);
                break;
            case ASSIGNMENT:
                assignment(abstractSyntaxTree, result, false);
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
        }

    }

    public void nodeTypes(AbstractSyntaxTree abstractSyntaxTree, StringBuilder result, boolean isItDeclaration) {
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
                assignments(abstractSyntaxTree, result, isItDeclaration);
                break;
            case ASSIGNMENT:
                assignment(abstractSyntaxTree, result, isItDeclaration);
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
        result.append("fn ");
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" () {\n");
        nodeTypes(ast.getChildren().get(1), result);
        result.append("}\n");
    }

    public void statements(AbstractSyntaxTree ast, StringBuilder result) {
        for (AbstractSyntaxTree abstractSyntaxTree : ast.getChildren())
            nodeTypes(abstractSyntaxTree, result);
    }

    public void statement(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == DECLARE) {
            nodeTypes(ast.getChildren().getFirst(), result);
            result.append(" ;\n");
        } else if (ast.getSubType() == ASSIGNMENTS) {
            nodeTypes(ast.getChildren().getFirst(), result);
            result.append(";\n");
        } else if (ast.getSubType() == CONTINUE) {
            result.append("continue;\n");
        } else if (ast.getSubType() == BREAK) {
            result.append(" break;\n");
        } else if (ast.getSubType() == PRINT) {
            result.append("println! (");
            print(ast.getChildren().getFirst(), result);
            result.append(" );\n");
        } else {
            for (AbstractSyntaxTree abstractSyntaxTree : ast.getChildren())
                nodeTypes(abstractSyntaxTree, result);
        }
    }

    public void switchStatement(AbstractSyntaxTree ast, StringBuilder result) {
        result.append(" match ");
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" { \n");
        nodeTypes(ast.getChildren().get(1), result);
        result.append("}\n");
    }

    public void assignment(AbstractSyntaxTree ast, StringBuilder result, boolean declaration) {
        if(declaration)
            result.append("let ");
        nodeTypes(ast.getChildren().getFirst(), result);
        result.append(" = ");
        nodeTypes(ast.getChildren().get(1), result);
    }

    public void assignments(AbstractSyntaxTree ast, StringBuilder result, boolean declaraiton) {
        if (ast.getChildren().size() == 2) {
            nodeTypes(ast.getChildren().getFirst(), result, declaraiton);
            result.append(" ;\n");
            nodeTypes(ast.getChildren().get(1), result, declaraiton);
        } else {
            nodeTypes(ast.getChildren().getFirst(), result, declaraiton);
        }
    }

    public void declaration(AbstractSyntaxTree ast, StringBuilder result) {
        nodeTypes(ast.getChildren().get(0), result, true);
    }

    public void id(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getLexeme() != null) result.append(ast.getLexeme());
        if (ast.getChildren() != null || !ast.getChildren().isEmpty()) {
            for (AbstractSyntaxTree abstractSyntaxTree : ast.getChildren()) {
                nodeTypes(abstractSyntaxTree, result);
            }
        }
    }

    public void disjunction(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == SINGLE) {
            nodeTypes(ast.getChildren().get(0), result);
        } else if (ast.getSubType() == MULTI) {
            nodeTypes(ast.getChildren().getFirst(), result);
            result.append(" || ");
            nodeTypes(ast.getChildren().get(1), result);
        }
    }

    public void print(AbstractSyntaxTree ast, StringBuilder result){
        for(AbstractSyntaxTree abstractSyntaxTree : ast.getChildren()){
            nodeTypes(abstractSyntaxTree, result);
        }
    }

    public void conjunction(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == SINGLE) {
            nodeTypes(ast.getChildren().get(0), result);
        } else if (ast.getSubType() == MULTI) {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" && ");
            nodeTypes(ast.getChildren().get(1), result);
        }
    }

    public void comparison(AbstractSyntaxTree ast, StringBuilder result) {
        nodeTypes(ast.getChildren().getFirst(), result);
    }

    public void inversion(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().get(0).getType() == NodeType.COMPARISON) {
            nodeTypes(ast.getChildren().get(0), result);
        } else {
            result.append(" !");
            nodeTypes(ast.getChildren().get(0), result);
        }
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
            result.append(" -");
            nodeTypes(ast.getChildren().get(1), result);
        }
    }

    public void modulo(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() == 1)
            nodeTypes(ast.getChildren().get(0), result);
        else {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" % " );
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
            result.append(" -");
            nodeTypes(ast.getChildren().get(0), result);
        } else if (ast.getSubType() == POSITIVE) {
            result.append(" + ");
            nodeTypes(ast.getChildren().get(0), result);
        } else if (ast.getSubType() == PAR) {
            result.append(" ( ");
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" ) ");
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

    public void whileStatement(AbstractSyntaxTree ast, StringBuilder result) {
        result.append("while (");
        nodeTypes(ast.getChildren().get(0), result);
        result.append(") ");
        nodeTypes(ast.getChildren().get(1), result);
    }

    public void cases(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() == 3) {
            nodeTypes(ast.getChildren().get(0), result);
            result.append(" => ");
            nodeTypes(ast.getChildren().get(1), result);
            result.append(" , ");
            nodeTypes(ast.getChildren().get(2), result);
        }
        if (ast.getChildren().size() == 1) {
            result.append(" _ => ");
            nodeTypes(ast.getChildren().get(0), result);
        }
    }

    public void ifStatement(AbstractSyntaxTree ast, StringBuilder result) {
        result.append("if ( ");
        nodeTypes(ast.getChildren().get(0), result);
        result.append(" ) ");
        nodeTypes(ast.getChildren().get(1), result);
        result.append("else ");
        nodeTypes(ast.getChildren().get(2), result);
    }

    public void followStatements(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getSubType() == MULTI) {
            result.append( "{\n");
            nodeTypes(ast.getChildren().get(0), result);
            result.append("}\n");
        } else if (ast.getSubType() == SINGLE) {
            nodeTypes(ast.getChildren().getFirst(), result);
        } else if (ast.getSubType() == EMPTY) {
            result.append(" { } ");
        }
    }

    public void options(AbstractSyntaxTree ast, StringBuilder result) {
        if (ast.getChildren().size() == 2) {
            nodeTypes(ast.getChildren().get(0), result);
            result.append( " | ");
            nodeTypes(ast.getChildren().get(1), result);
        } else {
            nodeTypes(ast.getChildren().get(0), result);
        }
    }
}
