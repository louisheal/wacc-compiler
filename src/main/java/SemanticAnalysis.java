import ast.*;
import ast.AssignRHS.RHSType;
import ast.Type.EType;

import java.util.*;

import static java.lang.System.exit;

public class SemanticAnalysis {

  SymbolTable currentST;
  Map<String, List<Param>> functionParams = new HashMap<>();
  Map<String, Type> functionReturnTypes = new HashMap<>();
  String functionIdent;

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
      case REFERENCE:
        errorMsgs.add(e + " operator can only be used on variables (ident expressions)");
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

  private String typeToString(Type type) {
    if (type == null) {
      return "pair";
    } else if (type.getType() == EType.PAIR) {
      return "pair";
    } else if (type.getType() == EType.ARRAY) {
      return String.format("array_%s", typeToString(type.getArrayType()));
    } else {
      return type.getType().toString().toLowerCase();
    }
  }

  public String getIdentWithParams(AssignRHS rhs) {
    StringBuilder ident = new StringBuilder();

    for (Expression expression : rhs.getArgList()) {
      ident.append(typeToString(getExpressionType(expression))).append("_");
    }
    ident.append("f_").append(rhs.getFunctionIdent());

    return ident.toString();
  }

  public Type getExpressionType(Expression expr) {

    if (expr == null) {
      return null;
    }

    Type nextType;

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

      case ARRAYELEM:
        return currentST.getType(expr.getArrayElem().getIdent()).getArrayType();

      case BRACKETS:
        return getExpressionType(expr.getExpression1());

      case REFERENCE:
        nextType = getExpressionType(expr.getExpression1());
        if(nextType.getType() == EType.DEREFERENCE) {
          return nextType.getArrayType();
        }
        return new Type(EType.REFERENCE, nextType);

      case DEREFERENCE:
        nextType = getExpressionType(expr.getExpression1());
        if(nextType.getType() == EType.REFERENCE) {
          return nextType.getArrayType();
        }
        return new Type(EType.DEREFERENCE, nextType);

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
        if (pairType == null) {
          break;
        }
        if (rhs.getPairElem().getType() == PairElem.PairElemType.FST) {
          return pairType.getFstType();
        } else if (rhs.getPairElem().getType() == PairElem.PairElemType.SND) {
          return pairType.getSndType();
        }

      case CALL:
        return functionReturnTypes.get(getIdentWithParams(rhs));
    }
    return null;
  }

  private Type getLHSType(AssignLHS lhs) {
    switch (lhs.getAssignType()) {
      case IDENT:
        return currentST.getType(lhs.getIdent());
      case ARRAYELEM:
        ArrayElem arrayElem = lhs.getArrayElem();
        Type result = currentST.getType(arrayElem.getIdent());
        for (int i = 0; i < arrayElem.getExpression().size(); i++) {
          result = result.getArrayType();
        }
        return result;
      case PAIRELEM:
        if (lhs.getPairElem().getType() == PairElem.PairElemType.FST) {
          return getExpressionType(lhs.getPairElem().getExpression()).getFstType();
        } else {
          return getExpressionType(lhs.getPairElem().getExpression()).getSndType();
        }
    }
    return null;
  }

  private boolean validFunctionReturn(Statement statement) {
    switch (statement.getStatType()) {
      case CONCAT:
        return validFunctionReturn(statement.getStatement2());
      case IF:
        return validFunctionReturn(statement.getStatement1()) &&
               validFunctionReturn(statement.getStatement2());
      case RETURN:
      case EXIT:
        return true;
    }
    return false;
  }

  private boolean bothIntegers(Type t1, Type t2) {
    return Objects.equals(t1, new Type(EType.INT)) &&
           Objects.equals(t2, new Type(EType.INT));
  }

  private boolean bothCharacters(Type t1, Type t2) {
    return Objects.equals(t1, new Type(EType.CHAR)) &&
           Objects.equals(t2, new Type(EType.CHAR));
  }

  private boolean bothBooleans(Type t1, Type t2) {
    return Objects.equals(t1, new Type(EType.BOOL)) &&
           Objects.equals(t2, new Type(EType.BOOL));
  }

  public void traverse(Program program) {

    for (Function function : program.getFunctions()) {

      StringBuilder ident = new StringBuilder();
      for (Param param : function.getParams()) {
        ident.append(typeToString(param.getType())).append("_");
      }
      ident.append("f_").append(function.getIdent());

      function.setIdent(ident.toString());

      if (functionParams.containsKey(function.getIdent())) {
        errorMsgs.add("Second Declaration of function " + function.getIdent());
        errors++;
        program.getFunctions().remove(function);
        break;
      }
      functionParams.put(function.getIdent(), function.getParams());
      functionReturnTypes.put(function.getIdent(), function.getReturnType());
    }

    for (Function function : program.getFunctions()) {
      functionIdent = function.getIdent();
      traverse(function);
    }

    currentST = new SymbolTable(null);
    functionIdent = null;

    traverse(program.getStatement());
  }

  private void traverse(Function function) {

    //Check that function ends with a return statement
    if (!validFunctionReturn(function.getStatement())) {
      System.out.println("Syntax Error: Function does not return");
      exit(100);
    }

    //Initialise new symbol table as root of its symbol table tree
    currentST = new SymbolTable(null);

    //Add function parameters to symbol table
    for (Param param : function.getParams()) {
      currentST.newVariable(param.getIdent(), param.getType());
    }

    //Check semantics of function body
    traverse(function.getStatement());
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
    boolean validDereference = expression.getExprType() == Expression.ExprType.DEREFERENCE &&
                       !getExpressionType(expression.getExpression1()).getType().equals(EType.REFERENCE);

    if (validNot || validNeg || validLen || validChr || validOrd || validDereference) {
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

    if (expression.getExprType() == Expression.ExprType.IDENT &&
            currentST.getType(expression.getIdent()) == null) {
      errorMsgs.add("Variable not defined: " + expression.getIdent() + "\n In expression: " + expression);
      errors++;
    }

    if (expression.getExprType() == Expression.ExprType.REFERENCE) {
      if(expression.getExpression1().getExprType() != Expression.ExprType.IDENT) {
        printSemanticError(expression.getExprType());
      } else if (currentST.getType(expression.getExpression1().getIdent()) == null) {
        errorMsgs.add("Variable not defined: " + expression.getExpression1().getIdent() +
                "\n In expression: " + expression);
        errors++;
      }
    }

  }

  private boolean invalidAssignment(Type lhs, AssignRHS rhs) {

    if (lhs == null) {
      return true;
    }


    boolean sameType = lhs.equals(getRHSType(rhs));

    boolean emptyArray = rhs.getAssignType() == RHSType.ARRAY &&
            lhs.getType() == EType.ARRAY;

    boolean charArrayAsString = lhs.equals(new Type(EType.STRING)) &&
            Objects.equals(getRHSType(rhs), new Type(EType.ARRAY, new Type(EType.CHAR)));

    boolean nullPair = lhs.getType() == EType.PAIR && rhs.getExpression1() == null;

    return !(sameType || emptyArray || charArrayAsString || nullPair);
  }

  private void traverse(Statement statement) {
    Expression expression = statement.getExpression();
    switch (statement.getStatType()) {

      case DECLARATION:

        if(statement.getLhsType().getType() == EType.PAIR && expression == null) {
          currentST.newVariable(statement.getLhsIdent(), statement.getLhsType());
          break;
        }

        if (invalidAssignment(statement.getLhsType(), statement.getRHS()) ||
                currentST.contains(statement.getLhsIdent())) {
          printSemanticError(Error.MISMATCH_TYPE);
          break;
        }

        currentST.newVariable(statement.getLhsIdent(), statement.getLhsType());

        if (Objects.equals(getRHSType(statement.getRHS()).getType(), (EType.ARRAY)) &&
                statement.getRHS().getArray() != null) {
          for (Expression expression1 : statement.getRHS().getArray()) {
            if (Objects.equals(getExpressionType(expression1), statement.getLhsType()) && expression1 != null) {
              errorMsgs.add("Array Mismatch");
              errors++;
              break;
            }
            traverse(expression1);
          }
        }
        traverse(statement.getRHS());
        break;

      case REASSIGNMENT:
        traverse(statement.getLHS());
        if (invalidAssignment(getLHSType(statement.getLHS()), statement.getRHS())) {
          printSemanticError(Error.MISMATCH_TYPE);
        }

        traverse(statement.getRHS());
        break;

      case READ:

        if(statement.getLHS() == null) {
          return;
        }

        if(statement.getLHS().getAssignType() != AssignLHS.LHSType.ARRAYELEM &&
           statement.getLHS().getAssignType() != AssignLHS.LHSType.IDENT &&
           statement.getLHS().getAssignType() != AssignLHS.LHSType.PAIRELEM) {
          printSemanticError(Error.NOT_READABLE);
        } else if (!Objects.equals(getLHSType(statement.getLHS()), new Type(EType.INT)) &&
                   !Objects.equals(getLHSType(statement.getLHS()), new Type(EType.CHAR))) {
          printSemanticError(Error.NOT_READABLE);
        }
        break;

      case FREE:
        if (expression != null &&
            getExpressionType(expression).getType() != EType.ARRAY &&
            getExpressionType(expression).getType() != EType.PAIR) {
          printSemanticError(Error.NOT_FREEABLE);
        } else {
          traverse(expression);
        }
        break;

      case RETURN:
        if (!Objects.equals(getExpressionType(expression), functionReturnTypes.get(functionIdent)) &&
                expression != null) {
          errorMsgs.add("Function return type does not match");
          errors++;
          break;
        }
      case PRINT:
      case PRINTLN:
        if(expression != null) {
          traverse(expression);
        }
        break;

      case EXIT:
        if(!Objects.equals(getExpressionType(expression), new Type(EType.INT))){
          printSemanticError(Error.EXIT_NOT_INT);
        }
        else {
          traverse(expression);
        }
        break;

      case IF:
        currentST = new SymbolTable(currentST);
        traverse(statement.getStatement2());
        currentST = currentST.getParent();

      case WHILE:
        if(!Objects.equals(getExpressionType(expression), new Type(EType.BOOL))) {
          printSemanticError(Error.WHILE_NOT_BOOL);
          break;
        }
        traverse(expression);

      case BEGIN:
        currentST = new SymbolTable(currentST);
        traverse(statement.getStatement1());
        currentST = currentST.getParent();
        break;

      case CONCAT:
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
        break;
    }
  }

  private void traverse(AssignLHS lhs) {

    if (lhs.getAssignType() == AssignLHS.LHSType.IDENT &&
            currentST.getType(lhs.getIdent()) == null) {
      printSemanticError(Error.UNDEFINED);
    }

    if (lhs.getAssignType() == AssignLHS.LHSType.ARRAYELEM &&
            currentST.getType(lhs.getArrayElem().getIdent()) == null) {
      printSemanticError(Error.UNDEFINED);
    }

    if (lhs.getAssignType() == AssignLHS.LHSType.PAIRELEM &&
            currentST.getType(lhs.getPairElem().getExpression().getIdent()) == null) {
      printSemanticError(Error.UNDEFINED);
    }

  }

  private String getParamTypes(List<Param> params) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < params.size(); i++) {
      String functionParam = params.get(i).toString();
      result.append(functionParam, 0, functionParam.indexOf(','));
      if (i != params.size() - 1) {
        result.append("_");
      }
    }
    return result.toString();
  }

  private void traverse(AssignRHS rhs) {

    if (rhs.getAssignType() == RHSType.EXPR) {
      traverse(rhs.getExpression1());
    }

    if (rhs.getAssignType() == RHSType.CALL) {
      if (rhs.getArgList().size() != functionParams.get(getIdentWithParams(rhs)).size()) {
        errorMsgs.add("Wrong number of arguments in call to function: " + rhs);
        errors++;
        return;
      }
      for (int i = 0; i < rhs.getArgList().size(); i++) {
        if (!Objects.equals(getExpressionType(rhs.getArgList().get(i)),
                functionParams.get(getIdentWithParams(rhs)).get(i).getType()) &&
                rhs.getArgList().get(i) != null) {
          errorMsgs.add("Type mismatch in call to function!" +
                  "\n - Expected: " + functionParams.get(getIdentWithParams(rhs)).get(i).getType() +
                  "\n - Actual: " + rhs.getArgList().get(i) + ":" + getExpressionType(rhs.getArgList().get(i)) +
                  "\n - In expression: " + rhs);
          errors++;
        }
      }
      // Changes the name of the ident to include the parammeters for the converter
      rhs.setFunctionIdent(getIdentWithParams(rhs));
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
