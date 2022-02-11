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

  public AssignRHS buildExprRHS(Expression expression1) {
    this.assignType = AssignRHS.RHSType.EXPR;
    this.expression1 = expression1;
    return this.build();
  }

  public AssignRHS buildArrayRHS(List<Expression> array) {
    this.assignType = AssignRHS.RHSType.ARRAY;
    this.array = array;
    return this.build();
  }

  public AssignRHS buildNewPair(Expression expression1, Expression expression2) {
    this.assignType = AssignRHS.RHSType.NEWPAIR;
    this.expression1 = expression1;
    this.expression2 = expression2;
    return this.build();
  }

  public AssignRHS buildPairElem(PairElem pairElem) {
    this.assignType = AssignRHS.RHSType.PAIRELEM;
    this.pairElem = pairElem;
    return this.build();
  }

  public AssignRHS buildCallRHS(String functionIdent, List<Expression> argList) {
    this.assignType = AssignRHS.RHSType.CALL;
    this.functionIdent = functionIdent;
    this.argList = argList;
    return this.build();
  }

  private AssignRHS build() {
    return new AssignRHS(assignType, expression1, expression2, array, pairElem, functionIdent, argList);
  }

}
