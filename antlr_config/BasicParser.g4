parser grammar BasicParser;

options {
  tokenVocab=BasicLexer;
}

unaryOper: NOT | MINUS | LEN | ORD | CHR;

binaryOper: PLUS | MINUS | MULTIPLY | DIVIDE | MODULO | GREATER_THAN | GREATER_THAN_OR_EQUAL | LESS_THAN | LESS_THAN_OR_EQUAL | EQUAL | NOT_EQUAL | AND | OR;

intLiter: intSign? INTEGER;

intSign: PLUS | MINUS;

expr: unaryOper expr
| expr binaryOper expr
| intLiter
| OPEN_PARENTHESES expr CLOSE_PARENTHESES
;

// EOF indicates that the program must consume to the end of the input.
prog: (expr)*  EOF ;
