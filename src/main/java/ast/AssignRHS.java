package ast;

import java.util.List;

public class AssignRHS {

  RHSType assignType;
  Expression expression1;
  Expression expression2;
  List<Expression> array;
  PairElem pairElem;
  String functionIdent;
  List<Expression> argList;

  public AssignRHS(RHSType assignType, Expression expression) {
    this.assignType = assignType;
    this.expression1 = expression;
    this.expression2 = null;
    this.array = null;
    this.pairElem = null;
    this.functionIdent = null;
    this.argList = null;
  }

  public AssignRHS(RHSType assignType, List<Expression> array) {
    this.assignType = assignType;
    this.expression1 = null;
    this.expression2 = null;
    this.array = array;
    this.pairElem = null;
    this.functionIdent = null;
    this.argList = null;
  }

  public AssignRHS(RHSType assignType, Expression expression1, Expression expression2) {
    this.assignType = assignType;
    this.expression1 = expression1;
    this.expression2 = expression2;
    this.array = null;
    this.pairElem = null;
    this.functionIdent = null;
    this.argList = null;
  }

  public AssignRHS(RHSType assignType, PairElem pairElem) {
    this.assignType = assignType;
    this.expression1 = null;
    this.expression2 = null;
    this.array = null;
    this.pairElem = pairElem;
    this.functionIdent = null;
    this.argList = null;
  }

  public AssignRHS(RHSType assignType, String functionIdent, List<Expression> argList) {
    this.assignType = assignType;
    this.expression1 = null;
    this.expression2 = null;
    this.array = null;
    this.pairElem = null;
    this.functionIdent = functionIdent;
    this.argList = argList;
  }

  public RHSType getAssignType() {
    return assignType;
  }

  public Expression getExpression1() {
    return expression1;
  }

  public Expression getExpression2() {
    return expression2;
  }

  public List<Expression> getArray() {
    return array;
  }

  public PairElem getPairElem() {
    return pairElem;
  }

  public String getFunctionIdent() {
    return functionIdent;
  }

  public List<Expression> getArgList() {
    return argList;
  }

  @Override
  public String toString() {

    StringBuilder result = new StringBuilder();

    if (assignType == RHSType.EXPR) {
      result.append(expression1);
    }

    if (assignType == RHSType.ARRAY) {
      result.append("[");
      if (!array.isEmpty()) {
        result.append(array.get(0));
        for (int i = 1; i < array.size(); i++) {
          result.append(", ").append(array.get(i));
        }
        result.append("]");
      }
    }

    if (assignType == RHSType.NEWPAIR) {
      result.append("newpair(").append(expression1).append(", ").append(expression2).append(")");
    }

    if (assignType == RHSType.PAIRELEM) {
      result.append(pairElem);
    }

    if (assignType == RHSType.CALL) {
      result.append("call ").append(functionIdent).append("(");
      if (!argList.isEmpty()) {
        result.append(argList.get(0));
        for (int i = 1; i < argList.size(); i++) {
          result.append(", ").append(argList.get(i));
        }
      }
      result.append(")");
    }

    return result.toString();
  }

  public enum RHSType {

    EXPR,
    ARRAY,
    NEWPAIR,
    PAIRELEM,
    CALL

  }

}
