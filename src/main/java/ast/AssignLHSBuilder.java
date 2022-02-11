package ast;

public class AssignLHSBuilder {

  private AssignLHS.LHSType assignType;
  private final String ident;
  private final ArrayElem arrayElem;
  private final PairElem pairElem;

  public AssignLHSBuilder withAssignType(AssignLHS.LHSType assignType) {
    this.assignType = assignType;
    return this;
  }

}
