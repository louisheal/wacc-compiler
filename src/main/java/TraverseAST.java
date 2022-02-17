import ast.*;
import ast.AssignRHS.RHSType;
import ast.Statement.StatType;
import ast.Type.EType;

import java.util.ArrayList;
import java.util.List;

public class TraverseAST {

  SymbolTable currentST = new SymbolTable(null);

  private int errors = 0;
  private final List<String> errorMsgs = new ArrayList<>();

  private void printSemanticError(Error e) {
    switch (e) {
      case UNDEFINED:
        errorMsgs.add("Assigning value to undefined variable");
        break;
      case MISMATCH_TYPE:
        errorMsgs.add("Type mismatch when assigning value to defined variable");
        break;
      case IF_NOT_BOOL:
        errorMsgs.add("Conditional part of an if statement is not a boolean expression");
        break;
      case EXIT_NOT_INT:
        errorMsgs.add("Exit value is not an integer expression");
        break;
      case WHILE_NOT_BOOL:
        errorMsgs.add("Conditional part of a while statement is not a boolean expression");
        break;
      case NOT_FREEABLE:
        errorMsgs.add("Trying to free a variable that isn't an array or pair");
        break;
      case NOT_READABLE:
        errorMsgs.add("Trying to read a variable that is a pair or boolean");
        break;
    }
    errors++;
  }

  private void printSemanticError(Expression.ExprType e) {
    switch (e) {
      case NOT:
      case AND:
      case OR:
        errorMsgs.add(e + " operator can only be used on boolean expressions");
        break;
      case NEG:
      case CHR:
      case DIVIDE:
      case MULTIPLY:
      case MODULO:
      case PLUS:
      case MINUS:
        errorMsgs.add(e + " operator can only be used on integer expressions");
        break;
      case LEN:
        errorMsgs.add(e + " operator can only be used on array expressions");
        break;
      case ORD:
        errorMsgs.add(e + " operator can only be used on character expressions");
        break;
      case GT:
      case GTE:
      case LT:
      case LTE:
        errorMsgs.add(e + " operator can only be used on integer and character expressions");
        break;
      case EQ:
      case NEQ:
        errorMsgs.add(e + " operator can only be used on expressions with the same type");
        break;
    }
    errors++;
  }

  public Integer getNumberOfErrors() {
    return errors;
  }

  public List<String> getErrorMsgs() {
    return errorMsgs;
  }

  private Type getExpressionType(Expression expr) {

    if (expr == null) {
      return null;
    }

    switch (expr.getExprType()) {

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
        return getExpressionType(expr.getExpression1());

    }
    return null;
  }

  private Type getRHSType(AssignRHS rhs) {
    switch (rhs.getAssignType()) {

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
        Type pairType = getExpressionType(rhs.getPairElem().getExpression());
        if (rhs.getPairElem().getType() == PairElem.PairElemType.FST) {
          return pairType.getFstType();
        } else {
          return pairType.getSndType();
        }

      case CALL:
        if (currentST.getFunctionReturnType(rhs.getFunctionIdent()) == null) {
          //TODO: need to know what to when a function is called before being defined
          return new Type(EType.INT);
        }
        return currentST.getFunctionReturnType(rhs.getFunctionIdent());
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

    if (statement.getStatType() == StatType.IF) {
      statement = statement.getStatement1();
    }

    //TODO: Would be good to check if the else branch returns as well

    if (statement.getStatType() != StatType.RETURN) {
      if (statement.getStatType() != StatType.EXIT) {
        printSemanticError(Error.FUNCTION_NO_RETURN);
      }
    }
  }

  private boolean bothIntegers(Type t1, Type t2) {
    return t1.equals(new Type(EType.INT)) &&
           t2.equals(new Type(EType.INT));
  }

  private boolean bothCharacters(Type t1, Type t2) {
    return t1.equals(new Type(EType.CHAR)) &&
           t2.equals(new Type(EType.CHAR));
  }

  private boolean bothBooleans(Type t1, Type t2) {
    return t1.equals(new Type(EType.BOOL)) &&
           t2.equals(new Type(EType.BOOL));
  }

  public void traverse(Program program) {
    for (Function function : program.getFunctions()) {
      traverse(function);
    }
    traverse(program.getStatement());
  }

  private void traverse(Function function) {
    validateFunctionReturns(function.getStatement());
    currentST.newFunction(function.getIdent(), function.getParams());
    currentST.newFunctionReturn(function.getIdent(), function.getReturnType());
    traverse(function.getParams());
    traverse(function.getStatement());
  }

  private void traverse(List<Param> params) {
    //TODO: Add params to function's symbol table
  }

  private void traverse(Expression expression) {

    if (expression == null) {
      return;
    }

    boolean validNot = expression.getExprType() == Expression.ExprType.NOT &&
                       !getExpressionType(expression.getExpression1()).equals(new Type(EType.BOOL));
    boolean validNeg = expression.getExprType() == Expression.ExprType.NEG &&
                       !getExpressionType(expression.getExpression1()).equals(new Type(EType.INT));
    boolean validLen = expression.getExprType() == Expression.ExprType.LEN &&
                       getExpressionType(expression.getExpression1()).getType() != EType.ARRAY;
    boolean validChr = expression.getExprType() == Expression.ExprType.CHR &&
                       !getExpressionType(expression.getExpression1()).equals(new Type(EType.INT));
    boolean validOrd = expression.getExprType() == Expression.ExprType.ORD &&
                       !getExpressionType(expression.getExpression1()).equals(new Type(EType.CHAR));

    if (validNot || validNeg || validLen || validChr || validOrd) {
      printSemanticError(expression.getExprType());
    }

    traverse(expression.getExpression1());

    if (expression.getExprType() == Expression.ExprType.DIVIDE ||
        expression.getExprType() == Expression.ExprType.MULTIPLY ||
        expression.getExprType() == Expression.ExprType.MODULO ||
        expression.getExprType() == Expression.ExprType.MINUS ||
        expression.getExprType() == Expression.ExprType.PLUS) {
      if (!bothIntegers(getExpressionType(expression.getExpression1()),
                        getExpressionType(expression.getExpression2()))) {
        printSemanticError(expression.getExprType());
      }
      traverse(expression.getExpression2());
    }

    if (expression.getExprType() == Expression.ExprType.GT ||
        expression.getExprType() == Expression.ExprType.GTE ||
        expression.getExprType() == Expression.ExprType.LT ||
        expression.getExprType() == Expression.ExprType.LTE) {
      if (!bothIntegers(getExpressionType(expression.getExpression1()),
              getExpressionType(expression.getExpression2())) &&
          !bothCharacters(getExpressionType(expression.getExpression1()),
                  getExpressionType(expression.getExpression2()))) {
        printSemanticError(expression.getExprType());
      }
      traverse(expression.getExpression2());
    }

    if (expression.getExprType() == Expression.ExprType.EQ ||
        expression.getExprType() == Expression.ExprType.NEQ) {
      if (!getExpressionType(expression.getExpression1()).equals(getExpressionType(expression.getExpression2())) &&
          expression.getExpression1() != null && expression.getExpression2() != null) {
        printSemanticError(expression.getExprType());
      }
      traverse(expression.getExpression2());
    }

    if (expression.getExprType() == Expression.ExprType.AND ||
            expression.getExprType() == Expression.ExprType.OR) {
      if (!bothBooleans(getExpressionType(expression.getExpression1()),
              getExpressionType(expression.getExpression2()))) {
        printSemanticError(expression.getExprType());
      }
      traverse(expression.getExpression2());
    }
  }

  private boolean invalidAssignment(Type lhs, AssignRHS rhs) {
    boolean sameType = lhs.equals(getRHSType(rhs));
    boolean emptyArray = rhs.getAssignType() == RHSType.ARRAY &&
            rhs.getArray().isEmpty() &&
            lhs.getType() == EType.ARRAY;
    boolean charArrayAsString = lhs.equals(new Type(EType.STRING)) &&
            getRHSType(rhs).equals(new Type(EType.ARRAY, new Type(EType.CHAR)));
    boolean nullPair = lhs.getType() == EType.PAIR && rhs.getExpression1() == null;
    return !sameType && !emptyArray && !charArrayAsString && !nullPair;
  }

  private void traverse(Statement statement) {
    Expression expression = statement.getExpression();
    switch (statement.getStatType()) {

      case DECLARATION:
        currentST.newVariable(statement.getLhsIdent(), statement.getLhsType());

        if(statement.getLhsType().getType() == EType.PAIR && expression == null) {
          break;
        }

        //TODO: possible error with nested types
        if (invalidAssignment(statement.getLhsType(), statement.getRHS())) {
          //TODO
          printSemanticError((Error) null);
        }

        if (getRHSType(statement.getRHS()).getType().equals(EType.ARRAY)) {
          for (Expression expression1 : statement.getRHS().getArray()) {
            traverse(expression1);
          }
        }
        traverse(statement.getRHS());
        break;

      case REASSIGNMENT:
        traverse(statement.getLHS());
        if (invalidAssignment(getLHSType(statement.getLHS()), statement.getRHS())) {
          //TODO: FIX ERROR MESSAGE
          System.out.println("Error: Assigning value of different type to defined variable");
          errors++;
        }

        traverse(statement.getRHS());
        break;

      case READ:
        if(statement.getRHS() == null) {
          return;
        }
        if(statement.getRHS().getAssignType() == RHSType.NEWPAIR
                || getRHSType(statement.getRHS()).getType().equals(EType.BOOL)){
          printSemanticError(Error.NOT_READABLE);
        }
        break;

      case FREE:
        if(statement.getRHS() == null){
          break;
        }
        if(statement.getRHS().getAssignType() != RHSType.ARRAY
            || statement.getRHS().getAssignType() != RHSType.PAIRELEM){
          printSemanticError(Error.NOT_FREEABLE);
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
          printSemanticError(Error.EXIT_NOT_INT);
        }
        else {
          traverse(expression);
        }
        break;

      case IF:
        if(!getExpressionType(expression).equals(new Type(EType.BOOL))){
          printSemanticError(Error.IF_NOT_BOOL);
        }
        traverse(expression);
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
        break;

      case WHILE:
        if(!getExpressionType(expression).equals(new Type(EType.BOOL))) {
          printSemanticError(Error.WHILE_NOT_BOOL);
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
      printSemanticError(Error.UNDEFINED);
    }

    if (lhs.getAssignType() == AssignLHS.LHSType.ARRAYELEM && !currentST.contains(lhs.getArrayElem().getIdent())) {
      printSemanticError(Error.UNDEFINED);
    }

    if (lhs.getAssignType() == AssignLHS.LHSType.PAIRELEM &&
            !currentST.contains(lhs.getPairElem().getExpression().getIdent())) {
      printSemanticError(Error.UNDEFINED);
    }

  }

  private void traverse(AssignRHS rhs) {

    if (rhs.getAssignType() == RHSType.EXPR) {
      traverse(rhs.getExpression1());
    }

  }

  private enum Error {
    UNDEFINED,
    MISMATCH_TYPE,
    IF_NOT_BOOL,
    EXIT_NOT_INT,
    NOT_FREEABLE,
    WHILE_NOT_BOOL,
    FUNCTION_NO_RETURN,
    NOT_READABLE
  }
}
