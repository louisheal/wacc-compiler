package ast;

public class AssignLHS {

  private final LHSType assignType;
  private final String ident;
  private final ArrayElem arrayElem;
  private final PairElem pairElem;

  public AssignLHS(LHSType assignType, String ident, ArrayElem arrayElem, PairElem pairElem) {
    this.assignType = assignType;
    this.ident = ident;
    this.arrayElem = arrayElem;
    this.pairElem = pairElem;
  }

  public AssignLHS(LHSType assignType, String ident) {
    this.assignType = assignType;
    this.ident = ident;
    this.arrayElem = null;
    this.pairElem = null;
  }

  public AssignLHS(LHSType assignType, ArrayElem arrayElem) {
    this.assignType = assignType;
    this.ident = null;
    this.arrayElem = arrayElem;
    this.pairElem = null;
  }

  public AssignLHS(LHSType assignType, PairElem pairElem) {
    this.assignType = assignType;
    this.ident = null;
    this.arrayElem = null;
    this.pairElem = pairElem;
  }

  public LHSType getAssignType() {
    return assignType;
  }

  public String getIdent() {
    return ident;
  }

  public ArrayElem getArrayElem() {
    return arrayElem;
  }

  public PairElem getPairElem() {
    return pairElem;
  }

  @Override
  public String toString() {
    String result = "";
    if (assignType == LHSType.IDENT) {
      result += ident;
    }
    if (assignType == LHSType.ARRAYELEM) {
      result += arrayElem;
    }
    if (assignType == LHSType.PAIRELEM) {
      result += pairElem;
    }
    return result;
  }

  public enum LHSType {

    IDENT,
    ARRAYELEM,
    PAIRELEM

  }

}
