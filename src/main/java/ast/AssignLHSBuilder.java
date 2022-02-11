package ast;

public class AssignLHSBuilder {

  private AssignLHS.LHSType assignType;
  private String ident;
  private ArrayElem arrayElem;
  private PairElem pairElem;

  public AssignLHSBuilder withAssignType(AssignLHS.LHSType assignType) {
    this.assignType = assignType;
    return this;
  }

  public AssignLHSBuilder withIdent(String ident) {
    this.ident = ident;
    return this;
  }

  public AssignLHSBuilder withArrayElem(ArrayElem arrayElem) {
    this.arrayElem = arrayElem;
    return this;
  }

  public AssignLHSBuilder withPairElem(PairElem pairElem) {
    this.pairElem = pairElem;
    return this;
  }

  public AssignLHS buildIdent() {
    return new AssignLHS(assignType, ident);
  }

  public AssignLHS buildArray() {
    return new AssignLHS(assignType, arrayElem);
  }

  public AssignLHS buildPair() {
    return new AssignLHS(assignType, pairElem);
  }


}
