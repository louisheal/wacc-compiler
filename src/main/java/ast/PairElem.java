package ast;

public class PairElem {

  PairElemType type;
  Expression expression;

  enum PairElemType {

    FST,
    SND

  }

}
