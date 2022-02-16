import ast.*;
import ast.AssignRHS.RHSType;
import ast.Expression.ExprType;
import ast.Statement.StatType;
import ast.Type.EType;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.List;
import java.util.Objects;

public class TraverseAST {
  SymbolTable currentST = new SymbolTable(null);

  private int errors = 0;

  private void printSemanticError(Statement statement) {
    String errorCause = "";

    switch (statement.getStatType()) {
      case DECLARATION:
        errorCause = "Declaration does not match actual type";
        break;
      case REASSIGNMENT:
        errorCause = "Cannot reassign to different variable type";
        break;
      case FREE:
        errorCause = "Invalid type for freeing";
        break;
      case EXIT:
        errorCause = "Exit code must be int";
        break;
      case IF:
        errorCause = "Conditional statement for IF must be boolean";
        break;
      case WHILE:
        errorCause = "Conditional statement for WHILE must be boolean";
        break;
    }

    String errorMsg = "Semantic Error: " + errorCause + "\n";
    errors++;
    System.out.println(errorMsg);

  }



  public Integer getNumberOfErrors() {
    return errors;
  }


  private Type getExpressionType(Expression expr) {
    switch(expr.getExprType()) {

      case INTLITER:
      case NEG:
      case ORD:
      case LEN:
      case DIVIDE:
      case MULTIPLY:
      case MODULO:
      case PLUS:
      case MINUS:
        return new Type(EType.INT);

      case BOOLLITER:
      case NOT:
      case GT:
      case GTE:
      case LT:
      case LTE:
      case EQ:
      case NEQ:
      case AND:
      case OR:
        return new Type(EType.BOOL);

      case CHARLITER:
      case CHR:
        return new Type(EType.CHAR);

      case STRINGLITER:
        return new Type(EType.STRING);

      case IDENT:
        return currentST.getType(expr.getIdent());

      case PAIRELEM:
        Type fstType = getExpressionType(expr.getExpression1());
        Type sndType = getExpressionType(expr.getExpression2());
        if (fstType.getType() == (EType.PAIR)) {
          fstType = new Type(EType.PAIR);
        }
        if (sndType.getType() == (EType.PAIR)) {
          sndType = new Type(EType.PAIR);
        }
        return new Type(EType.PAIR, fstType, sndType);

      case ARRAYELEM:
        if (expr.getArrayElem().getExpression().isEmpty()){
          return new Type(EType.ARRAY);
        }
        else {
          return new Type(EType.ARRAY,
              getExpressionType(expr.getArrayElem().getExpression().get(0)));
        }

      case BRACKETS:
        return (getExpressionType(expr.getExpression1()));

    }
    return new Type(EType.INT);
  }

  private Type getRHSType(AssignRHS rhs) {
    switch(rhs.getAssignType()){
      case EXPR:
        return getExpressionType(rhs.getExpression1());
      case ARRAY:
        if(rhs.getArray().isEmpty()){
          return new Type(EType.ARRAY);
        }
        else {
          return new Type(EType.ARRAY, getExpressionType(rhs.getArray().get(0)));
        }
      case NEWPAIR:
        Type fstType = getExpressionType(rhs.getExpression1());
        Type sndType = getExpressionType(rhs.getExpression2());
        if (fstType.getType() == (EType.PAIR)) {
          fstType = new Type(EType.PAIR);
        }
        if (sndType.getType() == (EType.PAIR)) {
          sndType = new Type(EType.PAIR);
        }
        return new Type(EType.PAIR, fstType, sndType);
      case PAIRELEM:
        return getExpressionType(rhs.getPairElem().getExpression());
      case CALL:
        return new Type(EType.INT);
    }
    return null;
  }

  private Type getLHSType(AssignLHS lhs) {
    switch (lhs.getAssignType()) {
      case IDENT: return currentST.getType(lhs.getIdent());
      case ARRAYELEM: return currentST.getType(lhs.getArrayElem().getIdent()).getArrayType();
      case PAIRELEM:
        if (lhs.getPairElem().getType() == PairElem.PairElemType.FST) {
          return getExpressionType(lhs.getPairElem().getExpression()).getFstType();
        } else {
          return getExpressionType(lhs.getPairElem().getExpression()).getSndType();
        }
    }
    return null;
  }

  private void validateFunctionReturns(Statement statement) {
    while (statement.getStatType() == StatType.CONCAT) {
      statement = statement.getStatement2();
    }
    if (statement.getStatType() == StatType.RETURN || statement.getStatType() == StatType.EXIT) {
      printSemanticError(statement);
    }
  }

  public void traverse(Program program) {
    for (Function function : program.getFunctions()) {
      traverse(function);
    }
    traverse(program.getStatement());
  }

  private void traverse(Function function) {
    validateFunctionReturns(function.getStatement());
    traverse(function.getParams());
    traverse(function.getStatement());
  }

  private void traverse(List<Param> params) {
    //TODO: Add params to function's symbol table
  }

  private void traverse(Expression expression) {
    switch (expression.getExprType()) {
      case INTLITER:
        break;
      case BOOLLITER:
        break;
      case CHARLITER:
        break;
      case STRINGLITER:
        break;
      case IDENT:
      case ARRAYELEM:
      case PAIRLITER:
        break;

      case NOT:
        if (!getExpressionType(expression.getExpression1()).equals(new Type(EType.BOOL))) {
          //TODO: Fix error messages
          System.out.println("Error: ! (not) operator can only be used on boolean expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        break;

      case NEG:
        if (!getExpressionType(expression.getExpression1()).equals(new Type(EType.INT))) {
          //TODO: Fix error messages
          System.out.println("Error: - (negate) operator can only be used on integer expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        break;

      case LEN:
        if (getExpressionType(expression.getExpression1()).getType() != EType.ARRAY) {
          //TODO: Fix error messages
          System.out.println("Error: len (length) operator can only be used on array expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        break;

      case CHR:
        if (!getExpressionType(expression.getExpression1()).equals(new Type(EType.INT))) {
          //TODO: Fix error messages
          System.out.println("Error: chr (character) operator can only be used on integer expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        break;

      case ORD:
        if (!getExpressionType(expression.getExpression1()).equals(new Type(EType.CHAR))) {
          //TODO: Fix error messages
          System.out.println("Error: ord (order) operator can only be used on character expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        break;

      case BRACKETS:
        traverse(expression.getExpression1());
        break;

      case DIVIDE:
        if (!getExpressionType(expression.getExpression1()).equals(new Type(EType.INT)) ||
            !getExpressionType(expression.getExpression2()).equals(new Type(EType.INT))) {
          //TODO: Fix error messages
          System.out.println("Error: / (divide) operator can only be used on integer expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        traverse(expression.getExpression2());
        break;

      case MULTIPLY:
        if (!getExpressionType(expression.getExpression1()).equals(new Type(EType.INT)) ||
                !getExpressionType(expression.getExpression2()).equals(new Type(EType.INT))) {
          //TODO: Fix error messages
          System.out.println("Error: * (multiply) operator can only be used on integer expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        traverse(expression.getExpression2());
        break;

      case MODULO:
        if (!getExpressionType(expression.getExpression1()).equals(new Type(EType.INT)) ||
                !getExpressionType(expression.getExpression2()).equals(new Type(EType.INT))) {
          //TODO: Fix error messages
          System.out.println("Error: % (modulo) operator can only be used on integer expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        traverse(expression.getExpression2());
        break;

      case PLUS:
        if (!getExpressionType(expression.getExpression1()).equals(new Type(EType.INT)) ||
                !getExpressionType(expression.getExpression2()).equals(new Type(EType.INT))) {
          //TODO: Fix error messages
          System.out.println("Error: + (plus) operator can only be used on integer expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        traverse(expression.getExpression2());
        break;

      case MINUS:
        if (!getExpressionType(expression.getExpression1()).equals(new Type(EType.INT)) ||
                !getExpressionType(expression.getExpression2()).equals(new Type(EType.INT))) {
          //TODO: Fix error messages
          System.out.println("Error: - (minus) operator can only be used on integer expressions");
          errors++;
        }
        traverse(expression.getExpression1());
        traverse(expression.getExpression2());
        break;

      case GT:
      case GTE:
      case LT:
      case LTE:
      case EQ:
      case NEQ:
      case AND:
      case OR:
        traverse(expression.getExpression1());
        traverse(expression.getExpression2());
    }
  }

  private boolean validDeclaration(Type lhs, AssignRHS rhs) {
    boolean sameType = lhs.equals(getRHSType(rhs));
    boolean emptyArray = rhs.getAssignType() == RHSType.ARRAY &&
            rhs.getArray().isEmpty() &&
            lhs.getType() == EType.ARRAY;
    boolean charArrayAsString = lhs.equals(new Type(EType.STRING)) &&
            Objects.equals(getRHSType(rhs), new Type(EType.ARRAY, new Type(EType.CHAR)));
    return sameType || emptyArray || charArrayAsString;
  }

  private boolean validReassignment(AssignLHS lhs, AssignRHS rhs) {
    boolean sameType = getLHSType(lhs).equals(getRHSType(rhs));
    boolean emptyArray = rhs.getAssignType() == RHSType.ARRAY &&
            rhs.getArray().isEmpty() &&
            getLHSType(lhs).getType() == EType.ARRAY;
    boolean charArrayAsString = getLHSType(lhs).equals(new Type(EType.STRING)) &&
            getRHSType(rhs).equals(new Type(EType.ARRAY, new Type(EType.CHAR)));
    return sameType || emptyArray || charArrayAsString;
  }

  private void traverse(Statement statement) {
    Expression expression = statement.getExpression();
    switch (statement.getStatType()) {

      case SKIP:
        break;

      case DECLARATION:
        currentST.newSymbol(statement.getLhsIdent(), statement.getLhsType());

        if(statement.getLhsType().getType() == EType.PAIR && expression == null) {
          break;
        }

        //TODO: possible error with nested types
        if (!validDeclaration(statement.getLhsType(), statement.getRHS())) {
          printSemanticError(statement);
        }

        if (getRHSType(statement.getRHS()).getType().equals(EType.ARRAY)) {
          for (Expression expression1 : statement.getRHS().getArray()) {
            traverse(expression1);
          }
        //TODO: TRAVERSE RHS?
        } else if (!(statement.getRHS().getAssignType() == RHSType.CALL)) {
          traverse(statement.getRHS().getExpression1());
        }
        break;

      case REASSIGNMENT:
        traverse(statement.getLHS());
        if (!validReassignment(statement.getLHS(), statement.getRHS())) {
          //TODO: FIX ERROR MESSAGE
          System.out.println("Error: Assigning value of different type to defined variable");
          errors++;
        }

        traverse(statement.getRHS());
        break;

      case READ:
        break;

      case FREE:
        if(statement.getRHS() == null
            ||statement.getRHS().getAssignType() != RHSType.ARRAY
            || statement.getRHS().getAssignType() != RHSType.PAIRELEM){
          printSemanticError(statement);
        }
        else{
          traverse(expression);
        }
        break;

      case RETURN:
      case PRINT:
      case PRINTLN:
        if(expression != null) {
          traverse(expression);
        }
        break;

      case EXIT:
        if(!getExpressionType(expression).equals(new Type(EType.INT))){
          printSemanticError(statement);
        }
        else {
          traverse(expression);
        }
        break;

      case IF:
        if(!getExpressionType(expression).equals(new Type(EType.BOOL))){
          printSemanticError(statement);
        }
        traverse(expression);
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
        break;

      case WHILE:
        if(!getExpressionType(expression).equals(new Type(EType.BOOL))) {
          printSemanticError(statement);
        }
        traverse(expression);
        traverse(statement.getStatement1());
        break;
      case BEGIN:
        traverse(statement.getStatement1());
        break;
      case CONCAT:
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
        break;
    }
  }

  private void traverse(AssignLHS lhs) {

    if (lhs.getAssignType() == AssignLHS.LHSType.IDENT && !currentST.contains(lhs.getIdent())) {
      //TODO: FIX ERROR MESSAGE
      System.out.println("Error: Assigning value to undefined variable");
      errors++;
    }

    if (lhs.getAssignType() == AssignLHS.LHSType.ARRAYELEM && !currentST.contains(lhs.getArrayElem().getIdent())) {
      //TODO: FIX ERROR MESSAGE
      System.out.println("Error: Assigning value to undefined variable");
      errors++;
    }

  }

  private void traverse(AssignRHS rhs) {

    if (rhs.getAssignType() == RHSType.EXPR) {
      traverse(rhs.getExpression1());
    }

  }
}
