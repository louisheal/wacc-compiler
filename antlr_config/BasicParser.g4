parser grammar BasicParser;

options {
  tokenVocab=BasicLexer;
}

expr: intLiter
| unaryOper expr
| expr binaryOper expr
| OPEN_PARENTHESES expr CLOSE_PARENTHESES
;

unaryOper: NOT | MINUS | LEN | ORD | CHR;

binaryOper: PLUS | MINUS | MULTIPLY | DIVIDE | MODULO | GREATER_THAN | GREATER_THAN_OR_EQUAL | LESS_THAN | LESS_THAN_OR_EQUAL | EQUAL | NOT_EQUAL | AND | OR;

intLiter: intSign? INTEGER;

intSign: PLUS | MINUS;

boolLiter: TRUE | FALSE;

charLiter: SINGLE_QUOTE character SINGLE_QUOTE;

strLiter: DOUBLE_QUOTE character* DOUBLE_QUOTE;

character: CHARACTER 
| escapedChar;

escapedChar: ZERO | BACK | TAB | NEWLINE | FORM_FEED | GIVE_A_NAME_FOR_R | DOUBLE_QUOTE | SINGLE_QUOTE | BACKSLASH;

arrayLiter: OPEN_SQUARE_BRACKETS (expr (COMMA expr)*)? CLOSED_SQUARE_BRACKETS;

// EOF indicates that the program must consume to the end of the input.
prog: (expr)*  EOF ;
