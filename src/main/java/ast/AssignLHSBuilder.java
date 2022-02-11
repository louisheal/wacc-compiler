package ast;

public class AssignLHSBuilder {

  private AssignLHS.LHSType assignType;
  private String ident;
  private ArrayElem arrayElem;
  private PairElem pairElem;

  public AssignLHS buildIdentLHS(String ident) {
    this.assignType = AssignLHS.LHSType.IDENT;
    this.ident = ident;
    return this.build();
  }

  public AssignLHS buildArrayLHS(ArrayElem arrayElem) {
    this.assignType = AssignLHS.LHSType.ARRAYELEM;
    this.arrayElem = arrayElem;
    return this.build();
  }

  public AssignLHS buildPairLHS( PairElem pairElem) {
    this.assignType = AssignLHS.LHSType.PAIRELEM;
    this.pairElem = pairElem;
    return this.build();
  }

  public AssignLHS build() {
    return new AssignLHS(assignType, ident, arrayElem, pairElem);
  }


}
