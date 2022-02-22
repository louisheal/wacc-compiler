import ast.Program;
import ast.Function;
import ast.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ASTVisitor<T> {

    /**
     * Returns the list of assembly instructions that represents the given program.
     *
     * @param program a program node from the abstract syntax tree
     * @return a list of assembly instructions that represent the given program
     */
    public T visitProgram(Program program) {
        return null;
    }

    /**
     * Returns the list of assembly instructions that represents the given function.
     *
     * @param function a function node from the abstract syntax tree
     * @return a list of assembly instructions that represent the given program
     */
    public T visitFunction(Function function) {
        return null;
    }

    /** Calls the corresponding visit statement function based on the statement type.
     *
     * @param statement a statement node from the abstract syntax tree
     * @return a list of assembly instructions that represent the given statement
     */
    public T visitStatement(Statement statement) {
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
            /* Returns the instructions of both statements appended together */
            //TODO: Apply Selthi-Ullman weights?
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
    public T visitSkipStatement(Statement statement) {
        return null;
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
    public T visitDeclarationStatement(Statement statement) {
        return null;
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
    public T visitReassignmentStatement(Statement statement) {
        return null;
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
    public T visitReadStatement(Statement statement) {
        return null;
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
    public T visitFreeStatement(Statement statement) {
        return null;
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
    public T visitReturnStatement(Statement statement) {
        return null;
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
    public T visitExitStatement(Statement statement) {
        return null;
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
    public T visitPrintStatement(Statement statement) {
        return null;
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
    public T visitPrintlnStatement(Statement statement) {
        return null;
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
    public T visitIfStatement(Statement statement) {
        return null;
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
    public T visitWhileStatement(Statement statement) {
        return null;
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
    public T visitBeginStatement(Statement statement) {
        return null;
    }

    /** <p>Visits a concatenation statement.</p>
     *
     * <p>Can call:</p>
     * <p><b>getStatType()</b> - Returns a StatType enum.</p>
     * <p><b>getStatement1()</b> - Returns the body of the first statement.</p>
     * <p><b>getStatement2()</b> - Returns the body of the second statement.</p>
     *
     * @param statement while statement
     * @return returns the assembly instructions for a while statement
     */
    public T visitConcatStatement(Statement statement) {
        return null;
    }

}
