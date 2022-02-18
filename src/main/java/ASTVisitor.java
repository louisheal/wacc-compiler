import ast.Program;
import ast.Function;
import ast.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ASTVisitor {

    /**
     * Returns the list of assembly instructions that represents the program given
     * to the function.
     *
     * @param program a program node from the abstract syntax tree
     * @return a list of assembly instructions that represent the given program
     */
    public List<String> visitProgram(Program program) {

        List<String> functionInstructions = new ArrayList<>();

        //Generate the assembly instructions for each function
        for (Function function : program.getFunctions()) {
            functionInstructions.addAll(visitFunction(function));
        }

        //Generate the assembly instructions for the
        List<String> statementInstructions = visitStatement(program.getStatement());
        return Stream.of(functionInstructions, statementInstructions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    //Returns the assembly instructions from function body
    public List<String> visitFunction(Function function) {
        return visitStatement(function.getStatement());
    }

    public List<String> visitStatement(Statement statement) {
        switch (statement.getStatType()) {
            case SKIP:
                return visitSkipStatement(statement);
            case DECLARATION:
                return visitDeclarationStatement(statement);
            case REASSIGNMENT:
                return visitReassignmentStatement(statement);
            case READ:
                return visitReadStatement(statement);
            case FREE:
                return visitFreeStatement(statement);
            case RETURN:
                return visitReturnStatement(statement);
            case EXIT:
                return visitExitStatement(statement);
            case PRINT:
                return visitPrintStatement(statement);
            case PRINTLN:
                return visitPrintlnStatement(statement);
            case IF:
                return visitIfStatement(statement);
            case WHILE:
                return visitWhileStatement(statement);
            case BEGIN:
                return visitBeginStatement(statement);
            //TODO: JOIN TWO BRANCHES OF CONCAT INSTEAD OF HAVING A VISITCONCAT FUNCTION
            case CONCAT:
                return visitConcatStatement(statement);
        }
        return null;
    }

    /** Visits a skip statement.
     *
     * Can call:
     * <code>getStatType</code>: Returns a StatType enum.
     *
     * @param statement skip statement
     * @return returns the assembly instructions for a skip statement
     */
    public List<String> visitSkipStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a declaration statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getLhsType()</b> - Returns Type class of the left-hand side.</p>
     * <p><b>getLhsIdent()</b> - Returns String of the variable name.</p>
     * <p><b>getRHS()</b> - Returns the RHS AST node.</p>
     *
     * @param statement declaration statement
     * @return returns the assembly instructions for a declaration statement
     */
    private List<String> visitDeclarationStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a reassignment statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getLHS()</b> - Returns the LHS AST node.</p>
     * <p><b>getRHS()</b> - Returns the RHS AST node.</p>
     *
     * @param statement reassignment statement
     * @return returns the assembly instructions for a reassignment statement
     */
    private List<String> visitReassignmentStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a read statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getLHS()</b> - Returns the LHS AST node.</p>
     *
     * @param statement read statement
     * @return returns the assembly instructions for a read statement
     */
    private List<String> visitReadStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a free statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getExpression()</b> - Returns the expression being freed.</p>
     *
     * @param statement free statement
     * @return returns the assembly instructions for a free statement
     */
    private List<String> visitFreeStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a return statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getExpression()</b> - Returns the expression being returned.</p>
     *
     * @param statement return statement
     * @return returns the assembly instructions for a return statement
     */
    private List<String> visitReturnStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits an exit statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getExpression()</b> - Returns the exit expression.
     * (Should evaluate to an integer type).</p>
     *
     * @param statement exit statement
     * @return returns the assembly instructions for an exit statement
     */
    private List<String> visitExitStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a print statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getExpression()</b> - Returns the expression being printed.</p>
     *
     * @param statement print statement
     * @return returns the assembly instructions for a print statement
     */
    private List<String> visitPrintStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a println statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getExpression()</b> - Returns the expression being printed.</p>
     *
     * @param statement println statement
     * @return returns the assembly instructions for a println statement
     */
    private List<String> visitPrintlnStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits an if statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getExpression()</b> - Returns the condition expression.</p>
     * <p><b>getStatement1()</b> - Returns the if branch of the statement.</p>
     * <p><b>getStatement2()</b> - Returns the else branch of the statement.</p>
     *
     * @param statement if statement
     * @return returns the assembly instructions for an if statement
     */
    private List<String> visitIfStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a while statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getExpression()</b> - Returns the condition expression.</p>
     * <p><b>getStatement1()</b> - Returns the body of the while loop.</p>
     *
     * @param statement while statement
     * @return returns the assembly instructions for a while statement
     */
    private List<String> visitWhileStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a begin statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getStatement1()</b> - Returns the body of the begin statement.</p>
     *
     * @param statement while statement
     * @return returns the assembly instructions for a while statement
     */
    private List<String> visitBeginStatement(Statement statement) {
        return new ArrayList<>();
    }

    /** <p>Visits a concat statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getStatement1()</b> - Returns the body of the first statement.</p>
     * <p><b>getStatement2()</b> - Returns the body of the second statement.</p>
     *
     * @param statement while statement
     * @return returns the assembly instructions for a while statement
     */
    private List<String> visitConcatStatement(Statement statement) {
        return new ArrayList<>();
    }

}
