package ast;

import java.util.List;

public class AssignRHSBuilder {

  private AssignRHS.RHSType assignType;
  private Expression expression1;
  private Expression expression2;
  private List<Expression> array;
  private PairElem pairElem;
  private String functionIdent;
  private List<Expression> argList;

  public AssignRHSBuilder withAssignType(AssignRHS.RHSType assignType) {
    this.assignType = assignType;
    return this;
  }

  public AssignRHSBuilder withExpression1(Expression expression1) {
    this.expression1 = expression1;
    return this;
  }

  public AssignRHSBuilder withExpression2(Expression expression2) {
    this.expression2 = expression2;
    return this;
  }

  public AssignRHSBuilder withArray(List<Expression> array) {
    this.array = array;
    return this;
  }

  public AssignRHSBuilder withPairElem(PairElem pairElem) {
    this.pairElem = pairElem;
    return this;
  }

  public AssignRHSBuilder withFunctionIdent(String functionIdent) {
    this.functionIdent = functionIdent;
    return this;
  }

  public AssignRHSBuilder withArgList(List<Expression> argList) {
    this.argList = argList;
    return this;
  }

  public AssignRHS buildExpr() {
    return new AssignRHS(assignType, expression1);
  }

  public AssignRHS buildArray() {
    return new AssignRHS(assignType, array);
  }

  public AssignRHS buildNewPair() {
    return new AssignRHS(assignType, expression1, expression2);
  }

  public AssignRHS buildPairElem() {
    return new AssignRHS(assignType, pairElem);
  }

  public AssignRHS buildCall() {
    return new AssignRHS(assignType, functionIdent, argList);
  }

}


