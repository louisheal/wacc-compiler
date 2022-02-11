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

  public AssignLHS buildIdentLHS(AssignLHS.LHSType assignType, String ident) {
    this.assignType = assignType;
    this.ident = ident;
    return this.build();
  }

  public AssignLHS buildArrayLHS(AssignLHS.LHSType assignType, ArrayElem arrayElem) {
    this.assignType = assignType;
    this.arrayElem = arrayElem;
    return this.build();
  }

  public AssignLHS buildPairLHS(AssignLHS.LHSType assignType, PairElem pairElem) {
    this.assignType = assignType;
    this.pairElem = pairElem;
    return this.build();
  }

  public AssignLHS build() {
    return new AssignLHS(assignType, ident, arrayElem, pairElem);
  }


}
